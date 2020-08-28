package spring;

public class MariadbArticleDao implements ArticleDao {
	public void insert() {
		System.out.println("MariadbArticleDao.insert() 메서드 호출");
	}
	public String select() {
		System.out.println("MariadbArticleDao.select() 메서드 호출");
		return "홍길동의 글";
	}
}