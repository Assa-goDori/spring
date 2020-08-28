package chap3;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import chap2.Executor;
import chap2.HomeController;
import chap2.InfraredRaySensor;
import chap2.WorkUnit;

public class Main2 {
	public static void main(String[] args) {
		//ApplicationContext : 객체를 저장하고 있는 컨테이너
		//AnnotationConfigApplicationContext : xml이 아닌 자바설정파일에서 객체 저장
		ApplicationContext ctx = new AnnotationConfigApplicationContext(AppCtx.class);
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