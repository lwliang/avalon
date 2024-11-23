/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.erp.addon.bank.sign;


import java.io.*;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;


import com.avalon.erp.addon.bank.utils.HttpClientUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

/**
 * 验签和加签工具类
 *
 * @author yzz
 */
@Component
@Slf4j
public class SignatureAndVerification {

    @Value("${pfxPath}")
    private String PFXPATH;

    @Value("${keystore_password}")
    private String KEYSTORE_PASSWORD;

    @Value("${cerPath}")
    private String CERPATH;

    @Value("${keystore_alias}")
    private String KEYSTORE_ALIAS;

    private String rootPath;

    public SignatureAndVerification(ResourceLoader resourceLoader) {
        rootPath = "";
    }

    /**
     * 加签名
     *
     * @param dataString
     * @return
     */
    public String signWhithsha1withrsa(String dataString) {
        String signatureString = null;
        String filePath = rootPath + File.separator + PFXPATH;
        try {
            KeyStore ks = KeyStore.getInstance("PKCS12");
            InputStream fis = this.getClass().getResource(filePath).openStream();
            char[] nPassword = null;
            if ((KEYSTORE_PASSWORD == null)
                    || KEYSTORE_PASSWORD.trim().equals("")) {
                nPassword = null;
            } else {
                nPassword = KEYSTORE_PASSWORD.toCharArray();
            }
            ks.load(fis, nPassword);
            fis.close();

            Enumeration<String> enums = ks.aliases();
            String keyAlias = null;
            if (enums.hasMoreElements()) {
                keyAlias = (String) enums.nextElement();

            }
            System.out.println("is key entry=" + ks.isKeyEntry(keyAlias));
            PrivateKey prikey = (PrivateKey) ks.getKey(keyAlias, nPassword);
            java.security.cert.Certificate cert = ks.getCertificate(keyAlias);
            PublicKey pubkey = cert.getPublicKey();

            // SHA1withRSA算法进行签名
            Signature sign = Signature.getInstance("SHA1withRSA");
            sign.initSign(prikey);
            byte[] data = dataString.getBytes("utf-8");
            byte[] dataBase = Base64.encodeBase64(data);
            // 更新用于签名的数据
            sign.update(dataBase);
            byte[] signature = sign.sign();
            signatureString = new String(Base64.encodeBase64(signature));
            log.info("加密完成，signature is : " + signatureString);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return signatureString;
    }

    /**
     * 读取cer并验证公钥签名
     *
     * @return
     */
    public boolean read_cer_and_verify_sign(String requestBody, String signature) {
        String filePath = rootPath + File.separator + CERPATH;
        X509Certificate cert = null;
        boolean flag = false;
        try {
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            InputStream fis = this.getClass().getResource(filePath).openStream();
            cert = (X509Certificate) cf.generateCertificate(fis);
            PublicKey publicKey = cert.getPublicKey();
            String publicKeyString = new String(Base64.encodeBase64(publicKey
                    .getEncoded()));
            log.warn("-----------------公钥--------------------");
            log.warn(publicKeyString);
            log.warn("-----------------公钥--------------------");
            Signature verifySign = Signature.getInstance("SHA1withRSA");
            verifySign.initVerify(publicKey);
            // 用于验签的数据
            verifySign.update(requestBody.getBytes("utf-8"));
            flag = verifySign.verify(com.alibaba.fastjson.util.Base64
                    .decodeFast(signature));
            // 验签由第三方做
            log.error("verifySign is {}", flag);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return flag;
    }

    /**
     * 接收报文返回requestBody和使用base64解析后的requestBody以及缴费中心传送的签名
     */

    public Map<String, String> requestBodyOfBase64(HttpServletRequest request) {
        Map<String, String> requestMap = new HashMap<String, String>();
        // 接收报文
        String requestContent = null;
        try {
            requestContent = HttpClientUtils.getRequestBody(request)
                    .trim();
        } catch (IOException e) {
            e.printStackTrace();
        }
		  /*requestContent=
		  "k6lECy5TcFx9SNniGM8xg94ZeocFBOIp8xF1wJg817gcBYuN6UHssjwr0/U5W2D1XZIRXJHQkgfluQ2qzZhDl5eiOyHpNgbxR0I/QYxUokaZy3XnSAjCi+uv6O6gti5MCnFs3ZP1l4cKdJrKMPaZowoQKR0aeUUFc3zWTH3LTcg=||eyJmb3JtYXQiOiJqc29uIiwibWVzc2FnZSI6eyJoZWFkIjp7ImJyYW5jaENvZGUiOiIyMTEwIiwiY2hhbm5lbCI6Ik1CTksiLCJ0aW1lU3RhbXAiOiIyMDE4MDkyMTE1MTg0Nzg2NyIsInRyYW5zQ29kZSI6InF1ZXJ5QmlsbCIsInRyYW5zRmxhZyI6IjAxIiwidHJhbnNTZXFOdW0iOiJCUDE4MDkyMTE1MTg1NzM5MDAwOSJ9LCJpbmZvIjp7ImVwYXlDb2RlIjoiSkYtRVBBWTIwMTgwODAyNjU2MDIiLCJpbnB1dDEiOiIxMjM0NTYiLCJtZXJjaGFudElkIjoiMTAzODgxMTA0NDEwMDAxIiwidHJhY2VObyI6IkpGMTgwOTIxMTUxODU3ODQ4NTkyIiwidXNlcklkIj"
		  + "oiMTYzNzUwNDYwMjk5NDM1NiJ9fX0=";*/
        //requestContent="EmYU9i0t+QkXRMT/adsTwnZCi/3DuEiufUthiWrVOPk/35P8a29wOAdMdpf//AREa7s5IEUs/SRTUyApvsZOMgK0wOyAugSiqFOzhp8A0+rihyrwcexDAy1oaRcp54cZV4Q6iiHKuzBYuIVp6OuYV0dogLBEeVC4yKQA6+syPj8=||eyAiZm9ybWF0IjogImpzb24iLCAibWVzc2FnZSI6IHsgImhlYWQiOiB7ICJjaGFubmVsIjogIk1CTksiLCAicmV0dXJuQ29kZSI6ICIwMDAwIiwgInJldHVybk1lc3NhZ2UiOiAi6LSm5Y2V5p+l6K+i5oiQ5Yqf77yM6L+U5Zue5oiQ5Yqf5qCH5b+XIiwgInRpbWVTdGFtcCI6ICIyMDE5MDUxMzEwNDcxNzU0NCIsInRyYW5zQ29kZSI6ICJxdWVyeUJpbGwiLCAidHJhbnNGbGFnIjogIjAyIiwgInRyYW5zU2VxTnVtIjogIkJQMTkwNDI4MTQ0MzMzNDE2MzUwIiB9LCAiaW5mbyI6IHsgImFtdFJ1bGUiOiAiMCIsICJiaWxscyI6IFt7ICJiaWxsTmFtZSI6ICLljZflt53liIblhazlj7gsLOmDkeacrOengDrmsJTotLnnvLTotLnljZUiLCAiYmlsbE5vIjogIjIwMTgwOSIsICJkZXNjRGV0YWlscyI6IFt7ICJzQ3B0IjogIjIwMTgwOSIsICJzVmFsIjogIjUuNjciIH1dLCAiZmVlQW10IjogIjAuMDAiLCAib3dlQW10IjogIjUuNjciIH1dLCAiY2FjaGVNZW0iOiAiY2hhcmdlRmVlMjAxODA5IiwgImN1c3RBZGRyZXNzIjogIuWNl+W3neawtOaxn+Wkp+m+mTPnu4Tmm5nlhYnliqDmsrnnq5nml4EgIiwgImN1c3ROYW1lIjogIumDkeacrOengCIsICJlcGF5Q29kZSI6ICJKRi1FUEFZMjAxOTA0MDg4NTQ4MSIsICJpbnB1dDEiOiAiMDMyMjAwMDE1IiwgIm1lcmNoYW50SWQiOiAiMTAzODgzMTY0OTkwMDMzIiwgInJlbWFyayI6ICLljZflt53liIblhazlj7gsLDEzNTk0NTg0NzE0LDAzMjIwMDAxNSIsICJ0b3RhbEJpbGxDb3VudCI6ICIxIiwgInRyYWNlTm8iOiAiSkYxOTA0MjgxNDQzMzM5ODA1MzEiIH0gfSB9";

        if (log.isWarnEnabled()) {
            log.info("收到的报文：{}", requestContent);
        }
        String signatureString = null;
        String requestBody = null;
        if (requestContent.contains("||")) {
            signatureString = requestContent.substring(0,
                    requestContent.indexOf("||"));
            requestBody = requestContent.substring(signatureString
                    .length() + 2);
        } else {
            try {
                requestBody = new String(requestContent.getBytes("GB2312"));

                log.info("转码后的报文：{}", requestBody);

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        log.info("截取报文的requestBody解密前:{}", requestBody);

        String requestBodyOfDecoded = new String(
                com.alibaba.fastjson.util.Base64.decodeFast(requestBody));

		/*if (requestBodyOfDecoded.contains("</Message>")) {
			requestBody = requestBodyOfDecoded.substring(
					requestContent.indexOf("</Message>"),requestContent.indexOf("</Message>")+10);

			signatureString = requestBodyOfDecoded.substring(
					requestContent.indexOf("<Signature>")+11,requestContent.indexOf("</Signature>"));
		}*/

        log.info("截取报文的signatureString:{}", signatureString);


        System.out.println("-----解析完成后的requestBody-------" + requestBodyOfDecoded);

        //使用base64解析完成后的requestBody
        requestMap.put("requestBodyOfDecoded", requestBodyOfDecoded);
        //解析前的requestBody
        requestMap.put("requestBody", requestBody);
        //获取缴费中心传送过来的签名
        requestMap.put("signatureString", signatureString);
        return requestMap;
    }
}
