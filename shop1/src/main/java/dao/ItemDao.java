package dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import logic.Item;

public class ItemDao {
	//NamedParameterJdbcTemplate : spring의 jdbc template
	private NamedParameterJdbcTemplate template;
	//Mapper : 컬럼명과 Item의 property를 이용하여 데이터 저장
	private RowMapper<Item> mapper = new BeanPropertyRowMapper(Item.class);
	public void setDataSource(DataSource dataSource) {
		this.template = new NamedParameterJdbcTemplate(dataSource);
	}
	public List<Item> list() {
		return template.query("select * from item", mapper);
	}
	public Item selectOne(Integer id) {
		Map<String, Integer> param = new HashMap<>();
		param.put("id", id);
		return template.queryForObject("select * from item where id=:id",param,mapper);	//queryForObject : 결과가 1개의 레코드인 경우만 사용 가능.
	}
}