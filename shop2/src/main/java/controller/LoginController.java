package controller;

import javax.servlet.http.HttpSession;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import logic.ShopService;
import logic.User;

public class LoginController {
	private ShopService shopService;
	private Validator loginValidator;
	
	public void setShopService(ShopService shopService) {
		this.shopService = shopService;
	}
	public void setLoginValidator(Validator validator) {
		this.loginValidator = validator;
	}
	
	@GetMapping
	public ModelAndView loginForm() {	//Model : view에 전달할 객체
		ModelAndView mav = new ModelAndView();	//view이름 미설정시 호출된 url로 view이름 설정 : login.jsp
		User u = new User();
		mav.addObject(u);
		return mav;	
	}
	
	@PostMapping	//session 매개변수로 설정시 알아서 설정해줌
	public ModelAndView login(User user, BindingResult bresult, HttpSession session) {
		ModelAndView mav = new ModelAndView();
		loginValidator.validate(user, bresult);
		if(bresult.hasErrors()) {
			mav.getModel().putAll(bresult.getModel());
			return mav;
		}
		/*
		 * 1. DB에서 userid에 해당하고 고객정보를 읽어 User 객체에 저장
		 * 2. 입력된 비밀번호와 DB의 비밀번호 비교하여 일치하는 경우
		 * 		session.setAttribute("loginUser", dbuser) 실행
		 * 3. 입력된 비밀번호와 db의 비밀번호 비교하여 불일치하는 경우
		 * 		유효성 검증으로 "비밀번호를 확인하세요." 메세지를 login.jsp 페이지로 전송
		 * 4. 로그인이 정상적인 경우 loginSuccess.jsp 페이지 이동
		 */
		try {
			//1번	
			User dbuser = shopService.getUser(user.getUserid());
			//2번
			if(user.getPassword().equals(dbuser.getPassword())) {
				session.setAttribute("loginUser", dbuser);
				mav.setViewName("loginSuccess");	//4번
				return mav;
			} else {	//3번
				bresult.reject("error.login.password");
				mav.getModel().putAll(bresult.getModel());
				return mav;
			}
		} catch (EmptyResultDataAccessException e) {
			//dbuser의 정보가 없는 경우(mybatis에서는 null값 리턴, spring jdbc에서는 exception 발생)
			bresult.reject("error.login.id");
			mav.getModel().putAll(bresult.getModel());
			return mav;
		}
	}
}