package hash;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

public class DigestMain1 {
	public static void main(String[] args) throws NoSuchAlgorithmException {
		byte[] plain = null;
		byte[] hash = null;
		String[] algo = {"MD5", "SHA-1", "SHA-256", "SHA-512"};
		System.out.println("해쉬값을 구할 문자열을 입력하세요");
		Scanner scan = new Scanner(System.in);
		String str = scan.nextLine();
		plain = str.getBytes();
		for(String al : algo) {
			MessageDigest md = MessageDigest.getInstance(al);
			hash = md.digest(plain);
			System.out.println(al + "해쉬값 크기 : " + (hash.length*8) + "bits");
			System.out.println("해쉬값 : ");
			for(byte b : hash) System.out.printf("%02X",b);
			System.out.println();
		}
	}
}
