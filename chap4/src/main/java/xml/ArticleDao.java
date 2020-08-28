package xml;

public interface ArticleDao {
	void insert(Article Article);
	void updateReadCount(int id, int i);
}