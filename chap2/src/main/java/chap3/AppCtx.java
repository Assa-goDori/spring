package chap3;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import chap2.Camera;
import chap2.DisplayMode;
import chap2.HomeController;
import chap2.InfraredRaySensor;

@Configuration	//설정을 위한 자바 프로그램.
@ComponentScan(basePackages = {"chap2", "chap3"})	//chap2내 @Component 어노테이션을 가진 클래스 객체화.(여러개 나열하여 사용가능)
public class AppCtx {
	@Bean	//객체화. <Bean id="camera1" ... />
	public Camera camera1() {
		Camera c = new Camera();
		c.setNumber(1);
		return c;
	}
	@Bean
	public Camera camera2() {
		Camera c = new Camera();
		c.setNumber(2);
		return c;
	}
	@Bean
	public Camera camera3() {
		Camera c = new Camera();
		c.setNumber(3);
		return c;
	}
	@Bean
	public Camera camera4() {
		Camera c = new Camera();
		c.setNumber(4);
		return c;
	}
	
	@Bean
	@Qualifier("intrusionDetection")	//별명설정
	public InfraredRaySensor windowSensor() {
		return new InfraredRaySensor("창센서");
	}
	
	@Bean
	@Qualifier("intrusionDetection")
	public InfraredRaySensor doorSensor() {
		return new InfraredRaySensor("현관센서");
	}
	
	@Bean
	@Qualifier("intrusionDetection")
	public InfraredRaySensor lampSensor() {
		return new InfraredRaySensor("전등센서");
	}
	
	@Bean
	public DisplayMode displayMode() {
		DisplayMode d = new DisplayMode();
		d.setType("GRID");
		return d;
	}
}
