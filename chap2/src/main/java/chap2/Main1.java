package chap2;

import org.springframework.context.support.GenericXmlApplicationContext;

public class Main1 {
	public static void main(String[] args) {
		//ctx : 객체를 생성해서 컨테이너에 저장을 함.
		//GenericXmlApplicationContext : xml을 이용하여 객체를 저장
		GenericXmlApplicationContext ctx = new GenericXmlApplicationContext("classpath:annotation.xml");
		Executor exec = ctx.getBean("executor", Executor.class);
		exec.addUnit(new WorkUnit());
		exec.addUnit(new WorkUnit());
		
		HomeController home = ctx.getBean("homeController",HomeController.class);
		home.checkSensorAndAlarm();
		
		//창문에 침입
		System.out.println("========창문에 침입자 발견========");
		InfraredRaySensor sensor = ctx.getBean("windowSensor", InfraredRaySensor.class);
		sensor.foundObject();
		home.checkSensorAndAlarm();
		//현관에 침입
		System.out.println("========현관에 침입자 발견========");
		sensor = ctx.getBean("doorSensor", InfraredRaySensor.class);
		sensor.foundObject();
		home.checkSensorAndAlarm();
		//전등에 침입
		System.out.println("========전등에 침입자 발견========");
		sensor = new InfraredRaySensor("전등센서");	//컨테이너 밖의 다른 객체이므로 homeController에서는 불러올 수 없다.
		sensor.foundObject();
		home.checkSensorAndAlarm();
	}
}