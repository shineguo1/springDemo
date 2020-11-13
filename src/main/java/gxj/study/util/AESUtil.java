package gxj.study.util;

/**
 * @author xinjie_guo
 * @version 1.0.0 createTime:  2020/9/3 18:09
 * @description
 */
//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import com.sun.org.apache.xml.internal.security.utils.Base64;
import gxj.study.demo.DBScript;
import lombok.Getter;
import lombok.Setter;
import sun.misc.BASE64Decoder;
import sun.security.krb5.internal.crypto.dk.Des3DkCrypto;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.google.common.base.Preconditions.checkNotNull;
import static gxj.study.demo.DBScript3.lineToHump;

public class AESUtil {
    private static final String KEY_ALGORITHM = "AES";
    private static final String CIPHER_ALGORITHM = "AES/CBC/PKCS5Padding";
    private static final String IV_STRING = "XINYAN-AES000000";

    public AESUtil() {
    }

    public static String encrypt(String source, String key) {
        try {
            if(key != null && !"".equals(source) && source != null) {
                byte[] e = produceKey(key).getBytes();
                SecretKeySpec skeySpec = new SecretKeySpec(e, "AES");
                byte[] initParam = "XINYAN-AES000000".getBytes();
                IvParameterSpec ivParameterSpec = new IvParameterSpec(initParam);
                Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
                cipher.init(1, skeySpec, ivParameterSpec);
                byte[] encrypted = cipher.doFinal(source.getBytes(Charset.forName("UTF-8")));
                return Base64.encode(encrypted);
            } else {
                return null;
            }
        } catch (Exception var8) {
            throw new SecurityException("AES加密失败", var8);
        }
    }

    public static String decrypt(String source, String key) {
        try {
            if(key != null && !"".equals(source) && source != null) {
                byte[] e = produceKey(key).getBytes();
                SecretKeySpec skeySpec = new SecretKeySpec(e, "AES");
                byte[] initParam = "XINYAN-AES000000".getBytes();
                IvParameterSpec ivParameterSpec = new IvParameterSpec(initParam);
                Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
                cipher.init(2, skeySpec, ivParameterSpec);
                byte[] sourceBytes = (new BASE64Decoder()).decodeBuffer(source);
                byte[] original = cipher.doFinal(sourceBytes);
                return new String(original, Charset.forName("UTF-8"));
            } else {
                return null;
            }
        } catch (Exception var9) {
            return source;
        }
    }


    public static String produceKey(String key) {
        try {
            return key.length() < 16?padStart(key, 16, '0'):(key.length() > 16?key.substring(0, 16):key);
        } catch (Exception var2) {
            throw new SecurityException("AES生成密钥失败", var2);
        }
    }
    public static String padStart(String string, int minLength, char padChar) {
        checkNotNull(string);
        if(string.length() >= minLength) {
            return string;
        } else {
            StringBuilder sb = new StringBuilder(minLength);

            for(int i = string.length(); i < minLength; ++i) {
                sb.append(padChar);
            }

            sb.append(string);
            return sb.toString();
        }
    }

    public static String doDecrypt(String  s){
        return decrypt(s, "contactEmail");
    }

    public static void main(String[] args) throws Exception {
//        String merchant_contacts = "mobile";
//        System.out.println("下划线:" + merchant_contacts);
//        String hump = lineToHump(merchant_contacts);
//        System.out.println("驼峰:" + hump);
//        String decrypt3 = decrypt("6sA6JZUli4CC9R+g984ajQ==", "merchantContacts");
//        String decrypt4 = decrypt("VFN7vLZw9ivZwVzyx7/xqg==", "contactEmail");
//        System.out.println("decrypt3:" + decrypt3);
//        System.out.println("decrypt3:" + decrypt4);


        System.out.println(doDecrypt("gBeoR/FZALq9afoBo5Qk5w=="));
            System.out.println(doDecrypt("iISklm8Ce4VLNap3eTwXFRFy+TFuh5/wWlZWmXBj4gQ7ebIfGj8HTKSAcafRDvuQ"));
        System.out.println(doDecrypt("g3QN7O3ic3R1hrbd5lPAzg=="));
        System.out.println(doDecrypt("VXHGklzfcTotU466q81ScTJ0CILfSPI5DnmbCA9bEeY="));
            System.out.println(doDecrypt("P6DXmg3HulHdLzzvvUt6/ikyUWEMKuYKNbBbu7b+zIAl4tIZFjZ+j1HdOfdRdFkN"));
        System.out.println(doDecrypt("6sA6JZUli4CC9R+g984ajQ=="));
        System.out.println(doDecrypt("EhBCNbSSfVMmnBhzCFYJVXLDZrXyHBsopc9B2yjnbFA="));
    }
}