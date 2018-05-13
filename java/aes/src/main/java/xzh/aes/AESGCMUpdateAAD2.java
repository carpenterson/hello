package xzh.aes;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Scanner;

import javax.crypto.AEADBadTagException;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;

import org.apache.commons.codec.binary.Hex;

public class AESGCMUpdateAAD2 {

	// AES-GCM parameters
	public static final int AES_KEY_SIZE = 128; // in bits
	public static final int GCM_NONCE_LENGTH = 12; // in bytes
	public static final int GCM_TAG_LENGTH = 16; // in bytes

	public static void main(String[] args) throws Exception {
		try (Scanner sc = new Scanner(System.in)) {

			int testNum = 0; // pass

			System.out.println("选择测试场景：0正常情况, 1解密时AAD不正确, 2密文被修改, 3tag被修改");
			testNum = sc.nextInt();
			sc.nextLine();
			if (testNum < 0 || testNum > 3) {
				System.out.println("只能输入 0, 1, 2, 3");
				System.exit(1);
			}

			System.out.println("输入待加密字符串");
			String src = sc.nextLine();

			byte[] input = src.getBytes("UTF-8");

			// Initialise random and generate key
			SecureRandom random = SecureRandom.getInstanceStrong();
			KeyGenerator keyGen = KeyGenerator.getInstance("AES");
			keyGen.init(AES_KEY_SIZE, random);
			SecretKey key = keyGen.generateKey(); // 输入1：加密Key

			// Encrypt
			Cipher cipher = Cipher.getInstance("AES/GCM/PKCS5Padding"); 
			final byte[] iv = new byte[GCM_NONCE_LENGTH]; 
			random.nextBytes(iv);
			GCMParameterSpec spec = new GCMParameterSpec(GCM_TAG_LENGTH * 8, iv); // 输入2：初始化向量IV
			cipher.init(Cipher.ENCRYPT_MODE, key, spec);

			byte[] aad = "Whatever I like".getBytes();
			cipher.updateAAD(aad); // 输入3：AAD。理解为生成MAC的key。

			byte[] cipherText = cipher.doFinal(input);

			System.out.println("加密后的字符串为：" +  cipherText.toString());
			System.out.println("加密后的字节为：" + Hex.encodeHexString(cipherText));

			// Decrypt; nonce is shared implicitly // nonce就是iv
			cipher.init(Cipher.DECRYPT_MODE, key, spec);

			// EXPECTED: Uncommenting this will cause an AEADBadTagException when decrypting
			// because AAD value is altered
			if (testNum == 1)
				aad[1]++;

			cipher.updateAAD(aad);

			// EXPECTED: Uncommenting this will cause an AEADBadTagException when decrypting
			// because the encrypted data has been altered
			if (testNum == 2)
				cipherText[10]++;

			// EXPECTED: Uncommenting this will cause an AEADBadTagException when decrypting
			// because the tag has been altered
			if (testNum == 3)
				cipherText[cipherText.length - 2]++;

			try {
				byte[] plainText = cipher.doFinal(cipherText);
				System.out.println("解密后的字符串：" + new String(plainText, "UTF-8"));

				if (testNum != 0) {
					System.out.println("Test Failed: expected AEADBadTagException not thrown");
				} else {
					// check if the decryption result matches
					if (Arrays.equals(input, plainText)) {
						System.out.println("Test Passed: match!");
					} else {
						System.out.println("Test Failed: result mismatch!");
						System.out.println(new String(plainText));
					}
				}
			} catch (AEADBadTagException ex) {
				if (testNum == 0) {
					System.out.println("Test Failed: unexpected ex " + ex);
					ex.printStackTrace();
				} else {
					System.out.println("Test Passed: expected ex " + ex);
				}
			}
		}
	}
}