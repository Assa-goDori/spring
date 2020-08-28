package controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import logic.ShopService;
import logic.User;
import util.UserValidator;

public class UserEntryController {
	private ShopService shopService;
	private UserValidator userValidator;
	public void setShopService(ShopService shopService) {
		this.shopService = shopService;
	}
	public void setUserValidator(UserValidator userValidator) {
		this.userValidator = userValidator;
	}
	
	/*
	//RequestMapping(method=RequestMethod.GET) = GetMapping
	@GetMapping
	public String userEntryForm() {	//보내줄 데이터가 없는 경우 String 리턴함.
		return null;	//view 이름. null인경우 기본인 userEntry.jsp가 설정됨.
	}
	
	@ModelAttribute
	public User getUser() {
		return new User();
	}
	*/
	
	@GetMapping		//위 GetMapping과 ModelAttribute를 한번에 처리하는 방법
	public ModelAndView userEntryForm() {
		ModelAndView mav = new ModelAndView();
		User u = new User();
		mav.addObject(u);
		return mav;		// /WEB-INF/view/userEntry.jsp
	}
	
	@PostMapping	//post 방식 요청	(RequestMapping : get,post 둘다 처리 가능)
	//User : 매개변수에 User타입이 선언된 경우,
	//		  파라미터 값과 User에 있는 setProperty()가 동일한 값을 저장.
	public ModelAndView userEntry(User user, BindingResult bindResult) {
		ModelAndView mav = new ModelAndView();
		userValidator.validate(user, bindResult);
		if (bindResult.hasErrors()) {	//입력문제 발생
			mav.getModel().putAll(bindResult.getModel());
			return mav;
		}
		//정상입력
		try {
			shopService.insertUser(user);
			mav.addObject("user", user);
		} catch(DataIntegrityViolationException e) {	//중복키 오류 예외처리
			e.printStackTrace();
			bindResult.reject("error.duplicate.user");
			mav.getModel().putAll(bindResult.getModel());
			return mav;
		}
		mav.setViewName("userEntrySuccess");	// /WEB-INF/view/userEntrySuccess.jsp
		return mav;
	}
	
	@InitBinder		//파라미터값 형변환 기능을 가진 메서드
	public void initBinder(WebDataBinder binder) {
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		binder.registerCustomEditor(Date.class, new CustomDateEditor(format, true));
		//true : 파라미터 입력값 필수 x(입력안하면 오류메시지 출력 x)
		//false : 파라미터 입력값 필수(입력안하면 오류메시지 출력 o)
	}
}