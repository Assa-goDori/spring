package xml;

public class UpdateMemberInfoTraceAdvice {
	//pointcut : 매개변수 목록 (String, UpdateInfo) 인 메서드
	public void traceReturn(Object result, String memberid, UpdateInfo info) {
		//result : 기본메서드의 리턴 값
		//memberid : 기본메서드의 첫번째 매개변수 String.
		//info : 기본메서드의 두번째 매개변수  UpdateInfo.
		System.out.println("[TA] 정보 수정 : 결과 = " + result + ", 대상회원 = " + memberid + ", 수정정보 = " + info);
	}
}