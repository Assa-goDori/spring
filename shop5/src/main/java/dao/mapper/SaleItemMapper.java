package dao.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import logic.SaleItem;

public interface SaleItemMapper {

	@Insert("insert into saleitem (saleid,seq,itemid,quantity) values (#{saleid},#{seq},#{itemid},#{quantity})")
	void insert(SaleItem si);

	@Select({
		"<script>",
		"select * from saleitem ",
		"<if test='saleid != null'> where saleid = #{saleid}</if>",
		"</script>"
	})
	List<SaleItem> select(Map<String, Object> param);
}