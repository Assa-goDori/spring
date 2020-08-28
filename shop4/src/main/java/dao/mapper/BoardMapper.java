package dao.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import logic.Board;

public interface BoardMapper {

	@Select("select max(num) from board")
	int maxnum();

	@Insert("insert into board (num, name, pass, subject, content, file1, regdate, readcnt, grp, grplevel, grpstep)" +
			" values(#{num}, #{name}, #{pass}, #{subject}, #{content}, #{fileurl}, now(), 0, #{grp}, #{grplevel}, #{grpstep})")
	void insert(Board board);

	@Select({
		"<script>",
		"select num, name, pass, subject, content, file1 fileurl, regdate, readcnt, grp, grplevel, grpstep from board ",
		"<if test='num != null'> where num=#{num} </if>",
		"<if test='searchtype != null and searchcontent != null'> where ${searchtype} like '%${searchcontent}%' </if>",
		"<if test='pageNum != null and limit != null'> order by grp desc, grpstep asc limit #{pageNum}, #{limit} </if>",
		"</script>"
	})
	List<Board> select(Map<String, Object> param);

	@Select({
		"<script>",
		"select count(*) from board ",
		"<if test='searchtype != null and searchcontent != null'> where ${searchtype} like '%${searchcontent}%' </if>",
		"</script>"
	})
	int getCount(Map<String, Object> param);

	@Update("update board set readcnt=readcnt+1 where num=#{num}")
	void updatecnt(@Param("num")int num);

	@Update("update board set grpstep=grpstep+1 where grp = #{grp} and grpstep > #{grpstep}")
	void updateGrpstep(Board board);

	@Update("update board set name=#{name}, subject=#{subject}, content=#{content}, file1=#{fileurl} where num=#{num}")
	void updateBoard(Board board);

	@Delete("delete from board where num=#{num}")
	void delete(@Param("num")int num);

	@Select("select name, count(*) cnt from board group by name order by count(*) desc limit 0,7")
	List<Map<String,Object>> graph1();

	@Select("select date_format(regdate, '%Y-%m-%d') regdate, count(*) cnt from board group by date_format(regdate, '%Y%m%d') order by regdate desc limit 0,7")
	List<Map<String, Object>> graph2();
}