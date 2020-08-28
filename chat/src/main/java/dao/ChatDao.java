package dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import dao.mapper.ChatMapper;
import logic.ChatRoom;
import logic.Message;

@Repository
public class ChatDao {
	@Autowired
	private SqlSessionTemplate template;
	Map<String, Object> param = new HashMap<>();

	public void createRoom(ChatRoom vo) {
		template.getMapper(ChatMapper.class).insert(vo);
	}

	public ChatRoom isRoom(ChatRoom vo){
		return template.getMapper(ChatMapper.class).selectOne(vo);
	}

	public void insertMessage(Message vo) {
		template.getMapper(ChatMapper.class).insertMessage(vo);
	}

	public String getPartner(ChatRoom vo) {
		List<Message> mvo = template.getMapper(ChatMapper.class).selectList(vo);
		return mvo.get(0).getUSER_user_id();
	}

	public String getProfile(String str) {
		return template.getMapper(ChatMapper.class).getProfile(str);
	}

	public String getName(String str) throws Exception {
		return template.getMapper(ChatMapper.class).getName(str);
	}

	public List<Message> getMessageList(String str) {
		return template.getMapper(ChatMapper.class).getMessageList(str);

	}

	public List<ChatRoom> getRoomList(String str) {
		return template.getMapper(ChatMapper.class).getRoomList(str);
	}

	public List<ChatRoom> getRoomList2(String str) {
		return template.getMapper(ChatMapper.class).getRoomList2(str);
	}

	public Message getRecentMessage(String str) {
		return template.getMapper(ChatMapper.class).getRecentMessage(str);
	}

	public String getTutorId(String str) {
		return template.getMapper(ChatMapper.class).getTutorId(str);
	}

	public List<ChatRoom> getRoomListTutor(String str) {
		return template.getMapper(ChatMapper.class).getRoomList2(str);
	}

	public void updateReadTime(int class_id, String user_id, String TUTOR_USER_user_id) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("TUTOR_USER_user_id", TUTOR_USER_user_id);
		map.put("USER_user_id", user_id);
		map.put("CLASS_class_id", class_id);
		template.getMapper(ChatMapper.class).updateReadTime(map);
	}

	public int getUnReadCount(String TUTOR_USER_user_id, int class_id, String user_id) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("TUTOR_USER_user_id", TUTOR_USER_user_id);
		map.put("USER_user_id", user_id);
		map.put("CLASS_class_id", class_id);
		return template.getMapper(ChatMapper.class).getUnReadCount(map);
	}

	public int getAllCount(String str) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("USER_user_id", str);
		map.put("TUTOR_USER_user_id", str);
		if (template.getMapper(ChatMapper.class).getAllCount(map) == null) {
			return 0;
		} else {
			return template.getMapper(ChatMapper.class).getAllCount(map);
		}
	}

	public void updateReadTimeTutor(int class_id, String user_id, String TUTOR_USER_user_id) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("TUTOR_USER_user_id", TUTOR_USER_user_id);
		map.put("USER_user_id", user_id);
		map.put("CLASS_class_id", class_id);
		template.getMapper(ChatMapper.class).updateReadTimeTutor(map);
	}

	public int getUnReadCountTutor(String TUTOR_USER_user_id, int class_id, String user_id)  {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("TUTOR_USER_user_id", TUTOR_USER_user_id);
		map.put("USER_user_id", user_id);
		map.put("CLASS_class_id", class_id);
		return template.getMapper(ChatMapper.class).getUnReadCountTutor(map);
	}

}