package chap2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component	//객체화 시키는 annotaion. <bean id="executor" class="chap2.Executor">
public class Executor {
	@Autowired	//컨테이너에서 worker 객체 주입. DI(객체 재사용x, scope에 의해 호출마다 새로생성)
	private worker worker;
	public void addUnit(WorkUnit unit) {
		worker.work(unit);
	}
}