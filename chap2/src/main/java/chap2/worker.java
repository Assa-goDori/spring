package chap2;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

@Component	//객체화
//Scope를 주석처리시 해쉬코드가 같은, 같은객체가 나온다.
@Scope(value="prototype", proxyMode=ScopedProxyMode.TARGET_CLASS)	//TARGET : 호출되는 곳. Scope : 호출될때마다 재사용하지않고 다시 생성이 되도록 설정.
public class worker {
	public void work(WorkUnit unit) {
		System.out.println(this + ":work:" + unit);
		//객체의 해쉬코드가 다른경우 다른객체로 본다.
	}
}