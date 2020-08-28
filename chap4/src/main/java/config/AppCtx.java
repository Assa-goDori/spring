package config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import main.MemberService;

@Configuration	//설정을 위한 자바 프로그램을 나타내기 위함
@ComponentScan(basePackages = {"annotation"})	//객체화되는 패키지 설정
@EnableAspectJAutoProxy	//annotation으로 AOP 설정
public class AppCtx {
	@Bean
	public MemberService memberService2() {
		return new MemberService();
	}
}