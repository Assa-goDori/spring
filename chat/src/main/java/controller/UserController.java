package controller;

import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import exception.LoginException;
import logic.Item;
import logic.Sale;
import logic.SaleItem;
import logic.ShopService;
import logic.User;

@Controller
@RequestMapping("user")
public class UserController {
	
	@Autowired
	private ShopService service;
	
	@GetMapping("*")
	public ModelAndView form() {
		ModelAndView mav = new ModelAndView();
		mav.addObject(new User());
		return mav;
	}
	
	@PostMapping("userEntry")
	public ModelAndView userEntryForm(@Valid User user, BindingResult bresult) {
		ModelAndView mav = new ModelAndView();
		if (bresult.hasErrors()) {
			bresult.reject("error.input.user");
			mav.getModel().putAll(bresult.getModel());
			return mav;
		}
		try {
			service.userInsert(user);
			mav.setViewName("redirect:login.shop");
		} catch(DataIntegrityViolationException e) {
			e.printStackTrace();
			bresult.reject("error.duplicate.user");
			mav.getModel().putAll(bresult.getModel());
		}
		return mav;
	}
	
	@PostMapping("login")
	public ModelAndView login(@Valid User user, BindingResult bresult, HttpSession session) {
		ModelAndView mav = new ModelAndView();
		if(bresult.hasErrors()) {
			bresult.reject("error.input.user");
			return mav;
		}
		try {
			//1. DB의 정보의 id와 password 비교
			User dbUser = service.getUser(user.getUserid());
			//2. 일치하는 경우, session loginUser 정보 저장
			if(user.getPassword().equals(dbUser.getPassword())) {
				session.setAttribute("loginUser", dbUser);
				mav.setViewName("redirect:main.shop");
			} else {
				//3. 불일치하는 경우, '비밀번호 확인' 내용 출력
				bresult.reject("error.login.password");
			}
		} catch (IndexOutOfBoundsException e) {
		//4. DB에 해당 id정보가 없는 경우(null값 리턴이 아닌 예외 발생), '아이디 확인' 내용 출력
			//EmptyResultDataAccessException : jdbc에서만 발생하는 예외. mybatis사용시는 Exception으로 해주어야함.
			e.printStackTrace();
			bresult.reject("error.login.id");
		}
		return mav;
	}
	
	@RequestMapping("logout")
	public String loginChecklogout(HttpSession session) {
		session.invalidate();
		return "redirect:login.shop";
	}
	
	@RequestMapping("main")	//로그인이 되어야 실행 가능. 메서드이름 : loginCheck***로 지정.
	public String loginCheckmain(HttpSession session) {
		return null;
	}
	
	
	/*
	 * AOP 설정하기
	 * 1. UserController의 check로 시작하는 메서드에 매개변수가 id, session인 경우
	 * 		- 로그인 안된 경우 : 로그인 하세요. => login.shop 페이지 이동
	 * 		- admin이 아니면서, 다른 아이디 정보 조회시 : 본인정보만 조회가능. => main.shop 페이지 이동
	 */
	@RequestMapping("mypage")
	public ModelAndView checkmypage(String id, HttpSession session) {
		ModelAndView mav = new ModelAndView();
		//sale 테이블에서 saleid, userid, saledate 컬럼값만 저장된 Sale객체를 List로 리턴
		User user = service.getUser(id);
		List<Sale> salelist = service.salelist(id);
		for(Sale sa : salelist) {
			List<SaleItem> saleitemlist = service.saleItemList(sa.getSaleid());
			int tot = 0;
			for(SaleItem si : saleitemlist) {
				Item item = service.getItem(Integer.parseInt(si.getItemid()));
				si.setItem(item);
				tot += si.getQuantity()*si.getItem().getPrice();
			}
			sa.setTotal(tot);
			sa.setItemList(saleitemlist);
		}
		mav.addObject("user", user);
		mav.addObject("salelist",salelist);
		return mav;
	}
	
	@GetMapping(value= {"update","delete"})
	public ModelAndView checkview(String id, HttpSession session) {
		ModelAndView mav = new ModelAndView();
		User user = service.getUser(id);
		mav.addObject("user", user);
		return mav;
	}
	
	/*
	 * 1. 유효성검증
	 * 2. 비밀번호 검증 
	 * 		- 일치 : update실행, 로그인정보 수정(admin 아닌 경우)
	 * 		- 불일치 : 유효성 출력 error.login.password 코드 실행
	 * 3. 수정완료 => mypage.shop 이동
	 */
	
	@PostMapping("update")
	public ModelAndView checkupdate(@Valid User user, BindingResult bresult, HttpSession session) {
		ModelAndView mav = new ModelAndView();
		//유효성 검증
		if(bresult.hasErrors()) {
			bresult.reject("error.input.user");
			return mav;
		}
		//비밀번호 검증
		User loginUser = (User)session.getAttribute("loginUser");
		//로그인한 정보의 비밀번호와 입력된 비밀번호 검증
		if(!user.getPassword().equals(loginUser.getPassword())) {
			bresult.reject("error.login.password");
			return mav;
		}
		//비밀번호가 일치하는 경우
		try {
			service.userUpdate(user);
			mav.setViewName("redirect:mypage.shop?id="+user.getUserid());
			//로그인 정보 수정
			if(loginUser.getUserid().equals(user.getUserid())) {
				session.setAttribute("loginUser", user);
			}
		} catch(Exception e) {
			e.printStackTrace();
			bresult.reject("error.user.update");
		}
		return mav;
	}
	
	/*
	 * 회원탈퇴
	 * 1. 비밀번호 검증 불일치 : "비밀번호 오류" 메세지 출력. delete.shop 이동
	 * 2. 비밀번호 검증 일치 : 회원DB에서 delete.
	 * 		- 본인인 경우 로그아웃 후 login.shop으로 페이지 이동.
	 * 		- 관리자인 경우 탈퇴 후 main.shop으로 이동.
	 */
	
	@PostMapping("delete")
	public ModelAndView checkdelete(String userid, String password, HttpSession session) {
		ModelAndView mav = new ModelAndView();
		User loginUser = (User)session.getAttribute("loginUser");
		if(userid.equals("admin")) {
			throw new LoginException("관리자는 탈퇴 불가합니다.", "main.shop");
		}
		
		//관리자 로그인 : 관리자비밀번호 검증
		//사용자 로그인 : 본인비밀번호 검증
		if(!password.equals(loginUser.getPassword())) {
			throw new LoginException("비밀번호 오류","delete.shop?id="+userid);
		}
		
		if(loginUser.getUserid().equals("admin")) {
			mav.setViewName("redirect:main.shop");
		} else {
			mav.setViewName("redirect:logout.shop");
		}
		service.deleteUser(userid);
		return mav;
	}
}