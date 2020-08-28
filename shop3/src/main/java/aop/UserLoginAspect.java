package aop;

import javax.servlet.http.HttpSession;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import exception.LoginException;
import logic.User;

@Component
@Aspect		//AOP 실행 클래스
@Order(1)	//AOP 실행 순서
public class UserLoginAspect {
	@Around("execution(* controller.User*.loginCheck*(..)) && args(..,session)")
	//Around : 기본메서드 실행 전, 후
	//Pointcut : controller.UserController에 메서드 이름이 loginCheck로 시작하는 메서드.
	//args(..,session) : 마지막 매개변수가 session인 메서드
	public Object userLoginCheck(ProceedingJoinPoint joinPoint, HttpSession session) throws Throwable {
		User loginUser = (User)session.getAttribute("loginUser");
		if(loginUser == null) {
			throw new LoginException("[userlogin]로그인 후 거래하세요.","login.shop");
		}
		return joinPoint.proceed();
	}
	
	@Around("execution(* controller.User*.check*(..)) && args(id, session)")
	public Object LoginCheck(ProceedingJoinPoint joinPoint, String id, HttpSession session) throws Throwable {
		User loginUser = (User)session.getAttribute("loginUser");
		if(loginUser == null) {
			throw new LoginException("[userlogin]로그인 후 거래하세요.","login.shop");
		}
		if(!loginUser.getUserid().equals("admin") && !id.equals(loginUser.getUserid())) {
			throw new LoginException("[userlogin]본인 정보만 조회 가능.","main.shop");
		}
		return joinPoint.proceed();
	}
}