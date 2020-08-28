package hash;

import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

/*
 * 화면에서 아이디와 비밀번호를 입력받아 해당 아이디가 usersecurity 테이블에 없으면 "아이디 확인" 출력
 * 해당 아이디의 비밀번호를 비교해서 맞으면 "반갑습니다. 아이디님" 출력
 * 해당 아이디의 비밀번호를 비교해서 틀리면 "비밀번호 확인" 출력
 */
public class DigestMain3 {
	public static void main(String[] args) throws Exception {
		Class.forName("org.mariadb.jdbc.Driver");
		Connection conn = DriverManager.getConnection("jdbc:mariadb://localhost:3306/classdb", "scott", "1234");
		while (true) {
			System.out.println("아이디를 입력하세요-->");
			Scanner scan = new Scanner(System.in);
			String id = scan.nextLine();
			if(id.equals("exit")) break;
			PreparedStatement pstmt = conn.prepareStatement("select * from usersecurity where userid = ?");
			pstmt.setString(1, id);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				String dbpass = rs.getString(2);
				System.out.println("비밀번호를 입력하세요-->");
				String pass = scan.nextLine();
				MessageDigest md = MessageDigest.getInstance("SHA-256");
				String hashpass = "";
				byte[] plain = pass.getBytes();
				byte[] hash = md.digest(plain); // 해쉬 실행
				for (byte b : hash) {
					hashpass += String.format("%02X", b);
				}
				if (hashpass.equals(dbpass)) {
					System.out.println("환영합니다. " + id + "님");
					break;
				} else {
					System.out.println("비밀번호를 확인하세요.");
				}
			} else {
				System.out.println("아이디를 확인하세요");
			}
		}
	}
}