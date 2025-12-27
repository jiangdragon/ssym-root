package com.ssym.dajia.vendor.util;

import org.bouncycastle.asn1.gm.GMNamedCurves;
import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.engines.SM2Engine;
import org.bouncycastle.crypto.generators.ECKeyPairGenerator;
import org.bouncycastle.crypto.params.*;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.math.ec.custom.gm.SM2P256V1Curve;
import org.bouncycastle.util.encoders.Hex;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.security.spec.ECFieldFp;
import java.security.spec.EllipticCurve;

/**
 * @author WB20200724005
 * @Description SM2加解密
 * @Email jiangdragon@126.com | jianglong@sinosoft.com.cn
 * @Time 2025/11/27
 */
public class SM2Util {
    public static final SM2P256V1Curve CURVE = new SM2P256V1Curve();
    public final static BigInteger SM2_ECC_P = CURVE.getQ();
    public final static BigInteger SM2_ECC_A = CURVE.getA().toBigInteger();
    public final static BigInteger SM2_ECC_B = CURVE.getB().toBigInteger();
    public final static BigInteger SM2_ECC_N = CURVE.getOrder();
    public final static BigInteger SM2_ECC_H = CURVE.getCofactor();
    public final static BigInteger SM2_ECC_GX = new BigInteger(
            "32C4AE2C1F1981195F9904466A39C9948FE30BBFF2660BE1715A4589334C74C7", 16);
    public final static BigInteger SM2_ECC_GY = new BigInteger(
            "BC3736A2F4F6779C59BDCEE36B692153D0A9877CC62A474002DF32E52139F0A0", 16);
    public static final ECPoint G_POINT = CURVE.createPoint(SM2_ECC_GX, SM2_ECC_GY);
    public static final ECDomainParameters DOMAIN_PARAMS = new ECDomainParameters(CURVE, G_POINT,
            SM2_ECC_N, SM2_ECC_H);
//    public static final int CURVE_LEN = BCECUtil.getCurveLength(DOMAIN_PARAMS);
    //////////////////////////////////////////////////////////////////////////////////////
    public static final EllipticCurve JDK_CURVE = new EllipticCurve(new ECFieldFp(SM2_ECC_P), SM2_ECC_A, SM2_ECC_B);
    public static final java.security.spec.ECPoint JDK_G_POINT = new java.security.spec.ECPoint(
            G_POINT.getAffineXCoord().toBigInteger(), G_POINT.getAffineYCoord().toBigInteger());
    public static final java.security.spec.ECParameterSpec JDK_EC_SPEC = new java.security.spec.ECParameterSpec(
            JDK_CURVE, JDK_G_POINT, SM2_ECC_N, SM2_ECC_H.intValue());

    /**
     * 生成SM2密钥对
     *
     * @return 包含公钥和私钥的字符串数组
     */
    public static String[] generateKeyPair() {
        // 获取SM2曲线参数
        X9ECParameters sm2ECParameters = GMNamedCurves.getByName("sm2p256v1");
        ECDomainParameters domainParameters = new ECDomainParameters(
                sm2ECParameters.getCurve(),
                sm2ECParameters.getG(),
                sm2ECParameters.getN()
        );

        // 创建密钥生成器
        ECKeyPairGenerator keyPairGenerator = new ECKeyPairGenerator();
        keyPairGenerator.init(new ECKeyGenerationParameters(domainParameters, new SecureRandom()));

        // 生成密钥对
        AsymmetricCipherKeyPair keyPair = keyPairGenerator.generateKeyPair();

        // 提取公钥（椭圆曲线点）
        ECPoint publicKeyPoint = ((ECPublicKeyParameters) keyPair.getPublic()).getQ();
        String publicKey = Hex.toHexString(publicKeyPoint.getEncoded(false));

        // 提取私钥（256位整数）
        BigInteger privateKey = ((ECPrivateKeyParameters) keyPair.getPrivate()).getD();
        String privateKeyHex = privateKey.toString(16);

        return new String[]{publicKey, privateKeyHex};
    }

    /**
     * SM2加密
     *
     * @param publicKey 公钥
     * @param plainText 明文
     * @return 密文（十六进制字符串）
     */
    public static String encrypt(String plainText, String publicKey) {
        try {
            X9ECParameters sm2ECParameters = GMNamedCurves.getByName("sm2p256v1");
            ECDomainParameters domainParameters = new ECDomainParameters(
                    sm2ECParameters.getCurve(),
                    sm2ECParameters.getG(),
                    sm2ECParameters.getN()
            );
            // 解码公钥点
            ECPoint publicKeyPoint = sm2ECParameters.getCurve().decodePoint(Hex.decode(publicKey));
            ECPublicKeyParameters publicKeyParams = new ECPublicKeyParameters(publicKeyPoint, domainParameters);

            // 初始化SM2引擎进行加密
            SM2Engine sm2Engine = new SM2Engine();
            sm2Engine.init(true, new ParametersWithRandom(publicKeyParams, new SecureRandom()));

            byte[] plainBytes = plainText.getBytes("UTF-8");
            byte[] cipherBytes = sm2Engine.processBlock(plainBytes, 0, plainBytes.length);

            return Hex.toHexString(cipherBytes);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * SM2解密
     *
     * @param privateKey 私钥
     * @param cipherText 密文
     * @return 明文
     */
    public static String decrypt(String cipherText, String privateKey) {
        try {
            X9ECParameters sm2ECParameters = GMNamedCurves.getByName("sm2p256v1");
            ECDomainParameters domainParameters = new ECDomainParameters(
                    sm2ECParameters.getCurve(),
                    sm2ECParameters.getG(),
                    sm2ECParameters.getN()
            );
            // 创建私钥参数
            BigInteger privateKeyValue = new BigInteger(privateKey, 16);
            ECPrivateKeyParameters privateKeyParams = new ECPrivateKeyParameters(privateKeyValue, domainParameters);

            // 初始化SM2引擎进行解密
            SM2Engine sm2Engine = new SM2Engine();
            sm2Engine.init(false, privateKeyParams);

            byte[] cipherBytes = Hex.decode(cipherText);
            byte[] plainBytes = sm2Engine.processBlock(cipherBytes, 0, cipherBytes.length);

            return new String(plainBytes, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
