package util;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import logic.User;

public class UserValidator implements Validator{

	@Override
	public boolean supports(Class<?> clazz) {	//User 객체인지 확인여부. 유효성검증
		return User.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		//user : 화면으로부터 입력된 파라미터를 저장하고 있는 객체
		User user = (User)target;
		String group = "error.required";
		if(user.getUserid() == null || user.getUserid().length()==0) {
			errors.rejectValue("userid", group);	//파라미터별 오류 등록
		}
		if(user.getPassword() == null || user.getPassword().length()==0) {
			errors.rejectValue("password", group);
		}
		if(user.getUsername() == null || user.getUsername().length()==0) {
			errors.rejectValue("username", group);
		}
		if(user.getPhoneno() == null || user.getPhoneno().length()==0) {
			errors.rejectValue("phoneno", group);
		}
		if(user.getPostcode() == null || user.getPostcode().length()==0) {
			errors.rejectValue("postcode", group);
		}
		if(user.getAddress() == null || user.getAddress().length()==0) {
			errors.rejectValue("address", group);
		}
		if(user.getEmail() == null || user.getEmail().length()==0) {
			errors.rejectValue("email", group);
		}
		if(errors.hasErrors()) {	//오류 발생
			errors.reject("error.input.user");	//reject : 글로벌오류
		}
	}	
}