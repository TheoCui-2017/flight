package flight.spider.web.utility;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

public class securityUtil {
	public static final String SHASheed = "flightspider2016_20161128";

	// 加密向量
	private static byte[] aes_iv = { 0x12, 0x34, 0x56, 0x78, (byte) 0x90,
			(byte) 0xAB, (byte) 0xCD, (byte) 0xEF, 0x12, 0x34, 0x56, 0x78,
			(byte) 0x90, (byte) 0xAB, (byte) 0xCD, (byte) 0xEF };
	// 默认密钥
	private static byte[] aes_key = new byte[0];
	static {
		try {
			aes_key = "1234567890123abc".getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	public static String AESKey = AESDecrypt(
			"8d5AFlq9rDMPga7SvW07ap0GkhnRQnoV4wsy+5cQVtA=", "");

	// / <summary>
	// / AES加密算法
	// / </summary>
	// / <param name="plainText">明文字符串</param>
	// / <param name="key">密钥（16个英文字符）</param>
	// / <returns>返回加密后字符串，加密失败抛出异常</returns>
	public static String AESEncrypt(String plainText, String key) {// 对应的js解密在doc目录里有
		try {
			byte[] keyarr = aes_key;
			if (key != null && key.length() == 16) {
				keyarr = key.getBytes("UTF-8");
			}

			SecretKeySpec skeySpec = new SecretKeySpec(keyarr, "AES");

			// "算法/模式/补码方式", 使用CBC模式，需要一个向量iv，可增加加密算法的强度
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			IvParameterSpec iv = new IvParameterSpec(aes_iv);
			cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
			byte[] encrypted = cipher.doFinal(plainText.getBytes("UTF-8"));
			return Base64.encodeBase64String(encrypted).replace("\n", "")
					.replace("\r", "");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return "";
	}

	// / <summary>
	// / AES解密
	// / </summary>
	// / <param name="cipherText">密文字符串</param>
	// / <param name="key">密钥（16个英文字符）</param>
	// / <returns>返回解密后字符串，解密失败抛出异常</returns>
	public static String AESDecrypt(String cipherText, String key) {

		try {
			byte[] keyarr = aes_key;
			if (key != null && key.length() == 16) {
				keyarr = key.getBytes("UTF-8");
			}
			byte[] cipherText2 = Base64.decodeBase64(cipherText);
			SecretKeySpec skeySpec = new SecretKeySpec(keyarr, "AES");
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			IvParameterSpec iv = new IvParameterSpec(aes_iv);
			cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
			byte[] original = cipher.doFinal(cipherText2);

			return new String(original, "UTF-8");
		} catch (NoSuchAlgorithmException e) {
//			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
//			e.printStackTrace();
		} catch (InvalidKeyException e) {
//			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e) {
//			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
//			e.printStackTrace();
		} catch (BadPaddingException e) {
//			e.printStackTrace();
		}
		return "";
	}

	// / <summary>
	// / MD5加密
	// / </summary>
	// / <param name="str">原字符串</param>
	// / <returns></returns>
	public static String MD5Encrypt(String str) {
		if (str == null || str.equals("")) {
			return "";
		}
		MessageDigest messageDigest = null;
		try {
			messageDigest = MessageDigest.getInstance("MD5");
			messageDigest.reset();
			messageDigest.update(str.getBytes("UTF-8"));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		byte[] byteArray = messageDigest.digest();
		StringBuffer md5StrBuff = new StringBuffer();
		for (int i = 0; i < byteArray.length; i++) {
			if (Integer.toHexString(0xFF & byteArray[i]).length() == 1) {
				md5StrBuff.append("0").append(
						Integer.toHexString(0xFF & byteArray[i]));
			} else {
				md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
			}
		}
		return md5StrBuff.toString();
	}

	// / <summary>
	// / SHA256散列算法加密（不可逆）
	// / </summary>
	public static String SHA256Encrypt(String str) {
		try {
			byte[] arrHashInput = str.getBytes("UTF-16LE");
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			digest.update(arrHashInput);
			byte[] arrOut = digest.digest();

			StringBuilder hexString = new StringBuilder();
			for (int i = 0; i < arrOut.length; i++) {
				String hex = Integer.toHexString(arrOut[i] & 0xFF);
				if (hex.length() == 1) {
					hexString.append("0");
				}
				hexString.append(hex.toUpperCase());
				if (i < arrOut.length - 1) {
					hexString.append("-");
				}
			}
			return hexString.toString();

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return "";

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return "";

		}
	}

	// / <summary>
	// / 密码加密 md5(SHA256(pwd+slat))
	// / </summary>
	// / <param name="str">源密码</param>
	public static String PasswordEncrypt(String str) {
		if (str == null || str.equals("")) {
			return "";
		}

		// System.out.println("str:" + str);

		// System.out.println("Settings.SHASheed:" + SHASheed);
		str += SHASheed;

		// System.out.println("str:" + str);

		str = SHA256Encrypt(str);
		// System.out.println("SHA256Encrypt(str):" + str);

		str = MD5Encrypt(str).toLowerCase();

		// System.out.println("MD5Encrypt(str).ToLower():" + str);

		return str;
	}

	// / <summary>
	// / 对客户端传来的加密密码进行解密
	// / </summary>
	// / <param name="key">username or email</param>
	// / <param name="val">md6(password)</param>
	// / <returns></returns>
	public static String PwdDecrypt(String key, String val) {
		try {
			if (key == null || val == null || key.equals("") || val.equals("")) {
				return "";
			}
			String md5s = MD5Encrypt(key).toLowerCase();
			byte[] keyarr = md5s.getBytes("UTF-8");
			byte[] aes_iv = md5s.substring(0, 16).getBytes("UTF-8");

			val = val.replace(md5s.substring(0, 4), "+")
					.replace(md5s.substring(4, 4), "/")
					.replace(md5s.substring(8, 4), "=");
			byte[] cipherText2 = Base64.decodeBase64(val);
			SecretKeySpec skeySpec = new SecretKeySpec(keyarr, "AES");
			Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
			IvParameterSpec iv = new IvParameterSpec(aes_iv);
			cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
			byte[] original = cipher.doFinal(cipherText2);

			return new String(original, "UTF-8");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		return "";
	}

	public static String CodeEncrypt(String str) {
		return PasswordEncrypt(str);
	}
}
