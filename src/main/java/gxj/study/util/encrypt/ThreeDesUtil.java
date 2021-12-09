package gxj.study.util.encrypt;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.beans.Introspector;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Method;
import java.util.Set;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.util.Locale.ENGLISH;

/**
 * 加密解密工具类
 * 字符串 DESede(3DES) 加密
 * ECB模式/使用PKCS7方式填充不足位,目前给的密钥是192位
 * 3DES（即Triple DES）是DES向AES过渡的加密算法（1999年，NIST将3-DES指定为过渡的
 * 加密标准），是DES的一个更安全的变形。它以DES为基本模块，通过组合分组方法设计出分组加
 * 密算法，其具体实现如下：设Ek()和Dk()代表DES算法的加密和解密过程，K代表DES算法使用的
 * 密钥，P代表明文，C代表密表，这样，
 * 3DES加密过程为：C=Ek3(Dk2(Ek1(P)))
 * 3DES解密过程为：P=Dk1((EK2(Dk3(C)))
 */
@Slf4j
@Component
public class ThreeDesUtil {

    /**
     * @param args在java中调用sun公司提供的3DES加密解密算法时，需要使
     * 用到$JAVA_HOME/jre/lib/目录下如下的4个jar包：
     * jce.jar
     * security/US_export_policy.jar
     * security/local_policy.jar
     * ext/sunjce_provider.jar
     */
    // 定义加密算法,可用 DES,DESede,Blowfish
    private static final String Algorithm = "DESede";

    private static String secKey = "0RiVs7pV3ico2SdfTj96MPr2";

    /**
     * keybyte为加密密钥，长度为24字节
     * src为被加密的数据缓冲区（源）
     *
     * @param keybyte
     * @param src
     * @return byte[]
     */
    public static byte[] encryptMode(byte[] keybyte, byte[] src) {
        try {
            // 生成密钥 
            SecretKey deskey = new SecretKeySpec(keybyte, Algorithm);
            // 加密  
            Cipher c1 = Cipher.getInstance(Algorithm);
            c1.init(Cipher.ENCRYPT_MODE, deskey);
            return c1.doFinal(src);// 在单一方面的加密或解密 
        } catch (java.security.NoSuchAlgorithmException e1) {
            log.error("encryptMode error", e1);
        } catch (javax.crypto.NoSuchPaddingException e2) {
            log.error("encryptMode error", e2);
        } catch (Exception e3) {
            log.error("encryptMode error", e3);
        }
        return null;
    }

    /**
     * keybyte为加密密钥，长度为24字节
     * src为加密后的缓冲区
     *
     * @param keybyte
     * @param src
     * @return byte[]
     */
    public static byte[] decryptMode(byte[] keybyte, byte[] src) {
        try {
            // 生成密钥
            SecretKey deskey = new SecretKeySpec(keybyte, Algorithm);
            // 解密
            Cipher c1 = Cipher.getInstance(Algorithm);
            c1.init(Cipher.DECRYPT_MODE, deskey);
            return c1.doFinal(src);
        } catch (java.security.NoSuchAlgorithmException e1) {
            log.error("decryptMode error", e1);
        } catch (javax.crypto.NoSuchPaddingException e2) {
            log.error("decryptMode error", e2);
        } catch (Exception e3) {
            log.error("decryptMode error", e3);
        }
        return null;
    }

    /**
     * 转换成十六进制字符串
     *
     * @param b
     * @return String
     */
    public static String byte2Hex(byte[] b) {
        StringBuilder hs = new StringBuilder();
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = (Integer.toHexString(b[n] & 0XFF));
            if (stmp.length() == 1) {
                hs.append("0").append(stmp);
            } else {
                hs.append(stmp);
            }
            if (n < b.length - 1) {
                hs.append(":");
            }
        }
        return hs.toString().toUpperCase();
    }

    /**
     * 加密
     *
     * @param key 密钥
     * @param src 明文
     * @return String
     */
    public static String doEncrypt(String src,String key) {
        return Base64.encode(encryptMode(key.getBytes(), src.getBytes()));
    }

    /**
     * 解密
     *
     * @param key 密钥
     * @param src 密文
     * @return String
     */
    public static String doDecrypt(String src, String key) {
        if (StringUtils.isEmpty(src)) {
            return src;
        }
        byte[] decryptMode = new byte[0];
        try {
            decryptMode = decryptMode(key.getBytes("UTF-8"), Base64.decode(src));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (null == decryptMode || decryptMode.length < 1) {
            throw new RuntimeException("数据未加密");
        }
        String deSrc = new String(decryptMode);
        if (StringUtils.isEmpty(deSrc)) {
            return src;
        }
        return deSrc;
    }

    /**
     * 加密
     *
     * @param src 明文
     * @return String
     */
    public static String doEncrypt(String src) {
        if (StringUtils.isEmpty(src)) {
            return src;
        }
        return Base64.encode(encryptMode(secKey.getBytes(), src.getBytes()));
    }

    /**
     * 加密
     *
     * @param src 明文
     * @return String
     */
    public static Set<String> doEncrypt(Set<String> src) {
        if (CollectionUtils.isEmpty(src)) {
            return src;
        }
        return src.stream().map(ThreeDesUtil::doEncrypt).collect(Collectors.toSet());
    }

    /**
     * 解密
     *
     * @param src 密文
     * @return String
     */
    public static String doDecrypt(String src) {

        return doDecrypt(src, secKey);
    }

    /**
     * 解密
     *
     * @param src 密文集合
     * @return String
     */
    public static Set<String> doDecrypt(Set<String> src) {
        if (CollectionUtils.isEmpty(src)) {
            return src;
        }
        return src.stream().map(ThreeDesUtil::doDecrypt).collect(Collectors.toSet());
    }

    /**
     * 检查加密
     *
     * @param key 密钥
     * @param src 密文
     * @return String 原密文
     */
    public static String checkEncrypt(String key, String src) {
        if (StringUtils.isEmpty(src)) {
            return src;
        }
        byte[] mode = decryptMode(key.getBytes(), Base64.decode(src));
        if (mode == null) {
            log.error("ERROR checkEncrypt PARAM: {}", doEncrypt(key,src));
            throw new RuntimeException("数据未加密");
        }
        return src;
    }


    /**
     * 检查加密
     *
     * @param src 密文
     * @return String 原密文
     */
    public static String checkEncrypt(String src) {
        return checkEncrypt(secKey, src);
    }


    private static boolean isLetter(char c) {
        int k = 0x80;
        return c / k == 0;
    }

    /**
     * 得到一个字符串的长度,显示的长度,一个汉字或日韩文长度为2,英文字符长度为1
     *
     * @param s 需要得到长度的字符串
     * @return int 得到的字符串长度
     */
    public static int length(String s) {
        if (s == null) {
            return 0;
        }
        char[] c = s.toCharArray();
        int len = 0;
        for (int i = 0; i < c.length; i++) {
            len++;
            if (!isLetter(c[i])) {
                len++;
            }
        }
        return len;
    }

    /**
     * 基础服务合规加密
     */
    public static <T, R extends String> void injectProperty(T obj, Fn<T, R> fn, Mixer mixer) {
        try {
            String beanName = capitalize(fnToFieldName(fn));
            Method setMethod = obj.getClass().getMethod("set" + beanName, String.class);
            Method setMethodMix = obj.getClass().getMethod("set" + beanName + "Mix", String.class);

            String valueDes = fn.apply(obj);
            String value = doDecrypt(valueDes);
            if (StringUtils.isBlank(value) || value.contains("*")) {
                setMethod.invoke(obj, (String) null);
                setMethodMix.invoke(obj, (String) null);
            } else {
                String valueMix = mixer.mix(value);
                setMethod.invoke(obj, valueDes);
                setMethodMix.invoke(obj, valueMix);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static String capitalize(String name) {
        if (name == null || name.length() == 0) {
            return name;
        }
        return name.substring(0, 1).toUpperCase(ENGLISH) + name.substring(1);
    }

    public interface Mixer {
        String mix(String value);
    }

    public interface Fn<T, R> extends Function<T, R>, Serializable {
    }

    private static final Pattern GET_PATTERN = Pattern.compile("^get[A-Z].*");
    private static final Pattern IS_PATTERN = Pattern.compile("^is[A-Z].*");

    private static String fnToFieldName(Fn fn) {
        try {
            Method method = fn.getClass().getDeclaredMethod("writeReplace");
            method.setAccessible(Boolean.TRUE);
            SerializedLambda serializedLambda = (SerializedLambda) method.invoke(fn);
            String getter = serializedLambda.getImplMethodName();
            if (GET_PATTERN.matcher(getter).matches()) {
                getter = getter.substring(3);
            } else if (IS_PATTERN.matcher(getter).matches()) {
                getter = getter.substring(2);
            }
            return Introspector.decapitalize(getter);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }
}
