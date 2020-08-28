package chap2;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component	//객체화
public class HomeController {
	private AlarmDevice alarmDevice;	//SmsAlarmDevice 객체 주입
	private Viewer viewer;				//SmartPhoneViewer 객체 주입
	
	//Resource : 객체의 이름으로 주입
	@Resource(name="camera1")
	private Camera camera1;
	@Resource(name="camera2")
	private Camera camera2;
	@Resource(name="camera3")
	private Camera camera3;
	@Resource(name="camera4")
	private Camera camera4;
	
	private List<InfraredRaySensor> sensors;	//windowSensor, doorSensor 2개의 객체가 list에 담김
	
	@Autowired(required=false)	//'required=false' : 주입되는 객체가 없는 경우를 허용
	private Recorder recorder;

	@Autowired	//주입되는 객체가 반드시 있어야 함
				//AlarmDevice 객체 : 구현클래스의 객체 주입(구현클래스인 SmsAlarmDevice는 반드시 객체화가 되어야 함. Component)
				//Viewer 객체 : 구현클래스의 객체 주입(구현클래스인 SmartPhoneViewer 반드시 객체화가 되어야 함. Component)
	public void prepare(AlarmDevice alarmDevice, Viewer viewer) {
		this.alarmDevice = alarmDevice;
		this.viewer = viewer;
	}
	@PostConstruct	//필요한 모든 객체의 주입이 완료된 이후에 호출되는 메서드.(마지막에 실행)
	public void init() {
		System.out.println("init() 메서드 호출");
		viewer.add(camera1);
		viewer.add(camera2);
		viewer.add(camera3);
		viewer.add(camera4);
		viewer.draw();
	}
	
	@Autowired
	@Qualifier("intrusionDetection")	//별명 설정 qualifier의 이름이 "intrusionDetection"인 객체 모두 호출(리스트로 받아들임)
	public void setSensors(List<InfraredRaySensor> sensors) {
		System.out.println("setSensors() 메서드 호출");
		this.sensors = sensors;
		for(InfraredRaySensor s : sensors) {
			System.out.println(s.getName());
		}
	}
	public void checkSensorAndAlarm() {
		for(InfraredRaySensor s : sensors) {
			if(s.isObjectFounded()) {
				alarmDevice.alarm(s.getName());
			}
		}
	}
}

/*
	기본어노테이션
	1. 객체 생성 : @Component
		xml   : <Bean id="클래스이름(소문자시작)" class="패키지를 포함한 클래스 이름" >
		=> <context:component-scan base-package="chap2" /> : chap2 패키지 중 @Component을 가진 클래스를 객체화
	2. 객체 주입 : 
		@Autowired : 변수 선언, 메서드 위쪽에 표현 가능
					  객체 선택 기준이 자료형임. 같은 자료형을 가진 객체가 여러인경우 주의요함.
					 (required=false) : 객체가 없으면 null로 주입함.
		@Resource(이름) : 객체 중 이름으로 객체를 선택하여 주입함.
		@Required : 객체 선택 기준이 자료형임. 반드시 주입되어야 함(required 옵션 없음) => 거의 사용 X
	3. 그 외
		@PostConstruct : 객체 생성이 완료된 후 호출되는 메서드 지정.
						 (객체 생성 완료 : 필요한 객체 주입이 완료된 시점)
		@Qualifier(별명) : 객체에 설정된 별명을 사용함.
						  @Autowired와 함께 사용됨.
		@Scope(...) : 생성된 객체의 지속가능한 영역 설정.
					    일회용 객체 생성 가능
*/