package rsa;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.Cipher;

/*
 * RSA : 공개키 암호 알고리즘. 비대칭키.
 * 		공개키로 암호화 -> 개인키로 복호화 가능 : 기밀성(인증)
 * 		개인키로 암호화 -> 공개키로 복호화 가능 : 부인 방지용 기능
 */
public class CipherRSA {
	static Cipher cipher;
	static PrivateKey priKey;	//개인키
	static PublicKey pubKey;	//공개키
	static {
		try {
			cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			//공개키 방식의 알고리즘에서 사용되는 키 쌍 생성 객체
			KeyPairGenerator key = KeyPairGenerator.getInstance("RSA");
			key.initialize(2048);	//	2048비트로 키 크기를 설정
			KeyPair keyPair = key.genKeyPair();
			priKey = keyPair.getPrivate();
			pubKey = keyPair.getPublic();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String encrypt(String plain) {
		byte[] cipherMsg = new byte[1024];
		try {
			cipher.init(Cipher.ENCRYPT_MODE, priKey);	//개인키로 암호화 설정. ECB모드이므로 iv(initial vector) 필요 없음
			cipherMsg = cipher.doFinal(plain.getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return byteToHex(cipherMsg);
	}
	
	private static String byteToHex(byte[] cipherMsg) {
		if (cipherMsg == null) return null;
		int len = cipherMsg.length;
		String str = "";
		for (byte b : cipherMsg) {
			str += String.format("%02X", b);
		}
		return str;
	}
	
	public static String decrypt(String cipherMsg) {
		byte[] plainMsg = new byte[1024];
		try {
			cipher.init(Cipher.DECRYPT_MODE, pubKey);	//공개키로 복호화 진행.
			plainMsg = cipher.doFinal(hexToByte(cipherMsg.trim()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new String(plainMsg).trim();
	}
	
	private static byte[] hexToByte(String str) {
		if (str == null || str.length() < 2) return null;
		byte[] buf = new byte[str.length() / 2];
		for (int i =0; i < buf.length; i++) {
			buf[i] = (byte)Integer.parseInt(str.substring(i * 2, i * 2 + 2), 16);
		}
		return buf;
	}
}