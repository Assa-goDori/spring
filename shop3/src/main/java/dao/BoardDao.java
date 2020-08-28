package dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import logic.Board;

@Repository
public class BoardDao {
	RowMapper<Board> mapper = new BeanPropertyRowMapper<Board>(Board.class);
	Map<String, Object> param = new HashMap<>();
	private NamedParameterJdbcTemplate template;
	private String boardcolumn = "select num, name, pass, subject, content, file1 fileurl, regdate, readcnt, grp, grplevel, grpstep from board";
	
	@Autowired
	public void setDataSource(DataSource dataSource) {
		template = new NamedParameterJdbcTemplate(dataSource);
	}
	
	public int maxnum() {
		return template.queryForObject("select max(num) from board", param, Integer.class);
	}

	public void insert(Board board) {
		SqlParameterSource prop = new BeanPropertySqlParameterSource(board);
		String sql = "insert into board (num, name, pass, subject, content, file1, regdate, readcnt, grp, grplevel, grpstep)" +
					" values(:num, :name, :pass, :subject, :content, :fileurl, now(), 0, :grp, :grplevel, :grpstep)";
		template.update(sql, prop);
	}

	public List<Board> boardList(int pageNum, int limit, String searchtype, String searchcontent) {
		param.clear();
		param.put("pageNum", (pageNum-1)*limit);
		param.put("limit", limit);
		String sql = "";
		String where = " where " + searchtype + " like " + "'%" + searchcontent + "%' ";
		if(searchtype == null && searchcontent == null) {
			sql = boardcolumn + " order by grp desc, grpstep asc limit :pageNum, :limit";
		} else {
			sql = boardcolumn + where + " order by grp desc, grpstep asc limit :pageNum, :limit";
		}
		return template.query(sql, param, mapper);
	}

	public int boardCount(String searchtype, String searchcontent) {
		String sql = "select count(*) from board ";
		if(searchtype != null && searchcontent != null) {
			String where = " where " + searchtype + " like " + "'%" + searchcontent + "%' ";
			sql += where;
		}
		return template.queryForObject(sql, param,Integer.class);
	}

	public Board selectOne(int num) {
		param.clear();
		param.put("num", num);
		String sql = boardcolumn + " where num=:num";
		return template.queryForObject(sql, param, mapper);
	}

	public void updatercnt(int num) {
		param.clear();
		param.put("num", num);
		template.update("update board set readcnt=readcnt+1 where num=:num", param);
	}

	public void updateGrpStep(Board board) {
		SqlParameterSource prop = new BeanPropertySqlParameterSource(board);
		String sql = "update board set grpstep=grpstep+1 where grp = :grp and grpstep > :grpstep";
		template.update(sql, prop);
	}

	public void updateBoard(Board board) {
		SqlParameterSource prop = new BeanPropertySqlParameterSource(board);
		String sql = "update board set name=:name, subject=:subject, content=:content, file1=:fileurl where num=:num";
		template.update(sql, prop);
	}

	public void deleteBoard(int num) {
		param.clear();
		param.put("num", num);
		String sql = "delete from board where num=:num";
		template.update(sql, param);
	}
}