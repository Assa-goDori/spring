package dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import dao.mapper.BoardMapper;
import logic.Board;

@Repository
public class BoardDao {
	
	@Autowired
	private SqlSessionTemplate template;
	Map<String, Object> param = new HashMap<>();
	
	public int maxnum() {
		return template.getMapper(BoardMapper.class).maxnum();
	}

	public void insert(Board board) { 
		template.getMapper(BoardMapper.class).insert(board);
	}

	public List<Board> boardList(int pageNum, int limit, String searchtype, String searchcontent) {
		param.clear();
		param.put("pageNum", (pageNum-1)*limit);
		param.put("limit", limit);
		param.put("searchtype", searchtype);
		param.put("searchcontent", searchcontent);
		return template.getMapper(BoardMapper.class).select(param);
	}

	public int boardCount(String searchtype, String searchcontent) {
		param.clear();
		param.put("searchtype", searchtype);
		param.put("searchcontent", searchcontent);
		return template.getMapper(BoardMapper.class).getCount(param);
	}

	public Board selectOne(int num) {
		param.clear();
		param.put("num", num);
		return template.getMapper(BoardMapper.class).select(param).get(0);
	}

	public void updatercnt(int num) {
		param.clear();
		param.put("num", num);
		template.getMapper(BoardMapper.class).updatecnt(num);
	}

	public void updateGrpStep(Board board) {
		template.getMapper(BoardMapper.class).updateGrpstep(board);
	}

	public void updateBoard(Board board) {
		template.getMapper(BoardMapper.class).updateBoard(board);
	}

	public void deleteBoard(int num) {
		template.getMapper(BoardMapper.class).delete(num);
	}

	public List<Map<String,Object>> graph1() {
		return template.getMapper(BoardMapper.class).graph1();
	}

	public List<Map<String,Object>> graph2() {
		return template.getMapper(BoardMapper.class).graph2();
	}
}