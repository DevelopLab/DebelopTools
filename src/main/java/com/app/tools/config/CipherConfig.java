package com.app.tools.config;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;


/**
 * AES暗号処理クラス.
 *
 * @author mhiraishi
 */
@Configuration
@PropertySource("classpath:application.properties")
public class CipherConfig {

	private static final Logger LOGGER = LoggerFactory.getLogger(CipherConfig.class);

	private static Cipher cipher;

	private static SecretKeySpec keySpec;

	private static String encryptKey;

	private static String algorithm;

	@Autowired
	protected Environment env;

	/**
	 * サーバ起動時にAES暗号処理に必要な設定を行う。
	 */
	@Bean
	public void cipherSetting() {
		encryptKey = env.getRequiredProperty("aes.cipher.encrypt.key").trim();
		algorithm = env.getRequiredProperty("aes.cipher.encrypt.algorithm").trim();
		keySpec = new SecretKeySpec(encryptKey.getBytes(StandardCharsets.UTF_8), algorithm);

		// application.propertiesに正しい値が設定されているかを検証する。
		LOGGER.info("***** AES 暗号&復号 設定を行います。 *****");
		init(Cipher.ENCRYPT_MODE);
		init(Cipher.DECRYPT_MODE);
	}

    /**
     * 指定された文字列を暗号化する。
     *
     * @param rawValue 平文文字列
     * @return 暗号化文字列
     */
	public static String encrypt(String rawValue) {
		init(Cipher.ENCRYPT_MODE);
		String encrypt = null;
		try {
			encrypt = new String(Base64.getEncoder().encode(cipher.doFinal(rawValue.getBytes())));
		} catch (IllegalBlockSizeException | BadPaddingException e) {
			LOGGER.error("暗号化処理に失敗しました。{}", e.getMessage());
			e.printStackTrace();
		}
		return encrypt;
	}

	/**
	 * 指定された文字列を復号する。
	 *
	 * @param encryptValue 暗号化された値
	 * @return 復号化された値
	 */
	public static String decrypt(String encryptValue) {
		init(Cipher.DECRYPT_MODE);
		String decrypt = null;
		try {
			decrypt = new String(cipher.doFinal(Base64.getDecoder().decode(encryptValue.getBytes())));
		} catch (IllegalBlockSizeException | BadPaddingException e) {
			LOGGER.error("復号処理に失敗しました。{}", e.getMessage());
			e.printStackTrace();
		}
		return decrypt;
	}

	private static void init(int mode) {
		try {
			cipher = Cipher.getInstance(algorithm);
			cipher.init(mode, keySpec);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
			LOGGER.error("不正なアルゴリズムが設定されています。{}", e.getMessage());
		} catch (InvalidKeyException e) {
			LOGGER.error("不正なキーが設定されています。{}", e.getMessage());
		}
	}
}
