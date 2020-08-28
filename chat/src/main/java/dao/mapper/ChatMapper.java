package dao.mapper;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import logic.ChatRoom;
import logic.Message;

public interface ChatMapper {

	@Insert("insert into CHATROOM (USER_user_id , TUTOR_USER_user_id, CLASS_class_id) values(#{USER_user_id}, #{TUTOR_USER_user_id} , #{CLASS_class_id}")
	void insert(ChatRoom vo);

	@Select("select * from CHATROOM WHERE USER_user_id = #{USER_user_id} and TUTOR_USER_user_id = #{TUTOR_USER_user_id} and CLASS_class_id = #{CLASS_class_id}")
	ChatRoom selectOne(ChatRoom vo);

	@Insert("insert into MESSAGE (message_sender , message_receiver, message_content, " + 
			"CHATROOM_chatroom_id, USER_user_id , TUTOR_USER_user_id, CLASS_class_id)" +
			"values (#{message_sender}, #{message_receiver}, #{message_content} , #{CHATROOM_chatroom_id} , #{USER_user_id}, " +
			"#{TUTOR_USER_user_id} , #{CLASS_class_id})")
	void insertMessage(Message vo);

	@Select("SELECT USER_user_id from MESSAGE where TUTOR_USER_user_id = #{TUTOR_USER_user_id} and CLASS_class_id = #{CLASS_class_id}")
	List<Message> selectList(ChatRoom vo);

	@Select("select user_profileImagePath from USER where user_id = #{user_id}")
	String getProfile(String str);

	@Select("select user_name from USER where user_id = #{user_id}")
	String getName(String str);

	@Select("select m.* , user_name, user_profileImagePath from MESSAGE m left outer join USER u on m.message_sender = u.user_id where CHATROOM_chatroom_id = #{CHATROOM_chatroom_id}")
	List<Message> getMessageList(String str);

	@Select("select * from CHATROOM where USER_user_id = #{USER_user_id}")
	List<ChatRoom> getRoomList(String str);

	@Select("select * from CHATROOM where TUTOR_USER_user_id = #{TUTOR_USER_user_id}")
	List<ChatRoom> getRoomList2(String str);

	@Select("select m.* , class_name, class_id , TUTOR_tutor_id from MESSAGE m left outer join CLASS c on m.CLASS_class_id = c.class_id where CHATROOM_chatroom_id = #{CHATROOM_chatroom_id} order by message_id desc limit 1")
	Message getRecentMessage(String str);

	@Select("select tutor_id from TUTOR where USER_user_id = #{USER_user_id}")
	String getTutorId(String str);

	@Update("update MESSAGE set message_readTime = NOW() where TUTOR_USER_user_id = #{TUTOR_USER_user_id} AND CLASS_class_id = #{CLASS_class_id} AND message_readTime = message_sendTime and message_sender = TUTOR_USER_user_id and USER_user_id = #{USER_user_id}")
	void updateReadTime(HashMap<String, Object> map);

	@Select("select count(*) from MESSAGE where USER_user_id = #{USER_user_id} and TUTOR_USER_user_id = #{TUTOR_USER_user_id} AND CLASS_class_id = #{CLASS_class_id} AND message_readTime = message_sendTime and message_sender = TUTOR_USER_user_id")
	int getUnReadCount(HashMap<String, Object> map);

	@Select("select count(*) from MESSAGE WHERE (TUTOR_USER_user_id = #{TUTOR_USER_user_id} and message_readTime = message_sendTime and message_sender != #{USER_user_id}) or (USER_user_id = #{USER_user_id} and message_readTime = message_sendTime and message_sender != #{USER_user_id})")
	Integer getAllCount(HashMap<String, Object> map);

	@Update("update MESSAGE set message_readTime = NOW() where TUTOR_USER_user_id = #{TUTOR_USER_user_id} AND CLASS_class_id = #{CLASS_class_id} AND message_readTime = message_sendTime and message_sender = USER_user_id and USER_user_id = #{USER_user_id}")
	void updateReadTimeTutor(HashMap<String, Object> map);

	@Select("select count(*) from MESSAGE where TUTOR_USER_user_id =#{TUTOR_USER_user_id} and CLASS_class_id = #{CLASS_class_id} AND message_readTime = message_sendTime and message_sender = USER_user_id and USER_user_id = #{USER_user_id}")
	int getUnReadCountTutor(HashMap<String, Object> map);	
}
