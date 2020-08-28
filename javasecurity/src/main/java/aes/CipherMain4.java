package aes;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/*
 * usersecurity 테이블의 email을 복호화하기.
 * key는 userid의 해쉬값의 문자열 앞 16자리로 정한다.
 */
public class CipherMain4 {
	public static void main(String[] args) throws Exception {
		Class.forName("org.mariadb.jdbc.Driver");
		Connection conn = DriverManager.getConnection("jdbc:mariadb://localhost:3306/classdb","scott","1234");
		PreparedStatement pstmt = conn.prepareStatement("select * from usersecurity");
		ResultSet rs = pstmt.executeQuery();
		while(rs.next()) {
			String userid = rs.getString("userid");
			String key = CipherUtil.makehash(userid).substring(0,16);
			String email = rs.getString("email");	//암호화된 이메일
			String newEmail = CipherUtil.decrypt(email,key);	//복호화
			System.out.println(userid + "의 이메일 : " + newEmail);
		}
	}
}