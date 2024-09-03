package com.ysh.my_course.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class Encrypt {

	public static void main(String[] args) {
		Encrypt enc = new Encrypt();
		
		String pwd = "test1234";
		System.out.println("pwd : " + pwd);
		
		// salt
//		String salt = enc.getSalt();
//		System.out.println("salt : " + salt);
		
		// 최종 암호
		String res = enc.getEncrypt(pwd, "223694b4d0de7fe9145c0f5928710e40729bdcc7");
		
		System.out.println("암호 : " + res);
	}
	
	private String getSalt() {
		
		SecureRandom r = new SecureRandom();
		byte[] salt = new byte[20];
		
		// 난수 생성
		r.nextBytes(salt);
		
		StringBuffer sb = new StringBuffer();
		for(byte b: salt) {
			sb.append(String.format("%02x", b));
		}
		
		return sb.toString();
	}
	
	private String getEncrypt(String pwd, String salt) {
		String result = "";
		try {
			// SAH256 알고리즘 객체 생성
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			
			// 암호와 salt를 합친 물자열에 SHA256 wjrdyd
			System.out.println("비밀번호 + salt 적용 전 : " + pwd + salt);
			md.update((pwd+salt).getBytes());
			byte[] pwdsalt = md.digest();
			
			// byte to String(10진수의 문자열로 변경)
			StringBuffer sb = new StringBuffer();
			for(byte b : pwdsalt) {
				sb.append(String.format("%02x", b));
			}
			
			result = sb.toString();
			System.out.println("비밀번호 + salt 적용 후 : " + result);
		}catch(NoSuchAlgorithmException e) {
			System.out.println(e.getMessage());
		}
		
		return result;
	}

}
