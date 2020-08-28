package chap1;

import org.springframework.context.support.GenericXmlApplicationContext;

public class Main1 {

	public static void main(String[] args) {
		GenericXmlApplicationContext ctx = new GenericXmlApplicationContext("classpath:applicationContext.xml");
		//Greeter g = (Greeter)ctx.getBean("greeter"); 도 가능
		Greeter g = ctx.getBean("greeter",Greeter.class);
		System.out.println(g.greet("스프링"));
		//Message m : MessageKr 객체
		Message m = ctx.getBean("message", Message.class);
//		m.sayHello("홍길동");
		m.sayHello("Hong kil dong");
		
		Greeter g2 = ctx.getBean("greeter",Greeter.class);	//새로운 g2객체가 생성되는것이 아니라 g객체를 재사용함
		if(g==g2) System.out.println("g 참조변수 객체와 g2 참조변수 객체는 같은 객체임");
	}
}