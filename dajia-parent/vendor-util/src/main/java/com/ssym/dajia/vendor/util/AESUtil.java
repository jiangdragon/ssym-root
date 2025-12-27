package com.ssym.dajia.vendor.util;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.HashMap;

/**
 * @author WB20200724005
 * @Description
 * @Email jiangdragon@126.com | jianglong@sinosoft.com.cn
 * @Time 2025/12/19
 */
public class AESUtil {
    private static final String KEY_ALGORITHM = "AES";
    private static final String DEFAULT_CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";

    /**
     * 指定字符生成新密钥
     *
     * @param key 密钥字符
     * @return
     */
    private static SecretKeySpec getSecretKey(final String key) {
        //返回生成指定算法密钥生成器的 KeyGenerator 对象
        KeyGenerator kg = null;

        try {
            kg = KeyGenerator.getInstance(KEY_ALGORITHM);

            //AES 要求密钥长度为 128
            // 初始化算法,设置成“SHA1PRNG”是为了防止在linux环境下随机生成算法
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            secureRandom.setSeed(key.getBytes(StandardCharsets.UTF_8));
            kg.init(128, secureRandom);
            //生成一个密钥
            SecretKey secretKey = kg.generateKey();

            return new SecretKeySpec(secretKey.getEncoded(), KEY_ALGORITHM);// 转换为AES专用密钥
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * 集团AES加密
     *
     * @param cipherText json字符串
     * @param key        密钥
     * @return 密文
     */
    public static String jtJsonEncrypt(String cipherText, String key) {
        HashMap<String, String> paramMap = JSONUtil.toBean(cipherText, HashMap.class);
        if (ObjectUtil.isEmpty(paramMap)) {
            return "";
        }
        return AESUtil.jtEncrypt(paramMap.toString(), key);
    }

    /**
     * 集团AES加密
     *
     * @param cipherText 待加密字符串
     * @param key        密钥
     * @return 密文
     */
    public static String jtEncrypt(String cipherText, String key) {
        try {
            // 创建密码器
            Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
            byte[] byteContent = cipherText.getBytes(StandardCharsets.UTF_8);
            // 生成加密模式的密码器并cipher初始化
            cipher.init(Cipher.ENCRYPT_MODE, AESUtil.getSecretKey(key));
            // 加密
            byte[] result = cipher.doFinal(byteContent);
            // 通过Base64转码返回
            return Base64.getUrlEncoder().encodeToString(result);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return "";
    }
}
