package spring;

public class WriteImpl {
	private ArticleDao dao;
	public WriteImpl(ArticleDao dao) {	//dao : spring.MariadbArticleDao 객체 주입
		this.dao = dao;
	}
	public void write() {
		System.out.println("WriteImpl.write 메서드 호출");
		dao.insert();
	}
}
