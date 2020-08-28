package dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import logic.User;

public interface AdminMapper {
	
	@Select("select * from useraccount where userid !='admin'")
	List<User> select(Object object);
}
