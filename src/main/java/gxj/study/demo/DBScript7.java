package gxj.study.demo;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import gxj.study.util.encrypt.EncryptStringUtil;
import gxj.study.util.ReadExcelUtils;
import gxj.study.util.encrypt.ThreeDesUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.regex.Pattern;

/**
 * @author xinjie_guo
 * @version 1.0.0 createTime:  2020/7/31 14:24
 * @description
 */
@Slf4j
public class DBScript7 {

    private static final String BASE = "0RiVs7pV3ico2SdfTj96MPr2";
    private static final String PHONE = "[0-9]{11}";
    private static final String IdCard = "[0-9]{15}|[0-9]{15}";
    private static final Pattern CHINESE = Pattern.compile("[\u4e00-\u9fa5]");
    private static final Map<String, MixMethod> MIX_METHOD_CACHE = new HashMap<>();
    private static final Map<String, DecryptMethod> DECRYPT_METHOD_CACHE = new HashMap<>();
    private static final Map<String, String> DECRYPT_KEY_CACHE = new HashMap<>();
    private static EncryptMethod ENCRYPT_METHOD;
    private static String ENCRYPT_KEY;

    interface MixMethod {
        String mix(String s);
    }

    interface DecryptMethod {
        String doDecrypt(String src, String key);
    }

    interface EncryptMethod {
        String doEncrypt(String src, String key);
    }

    interface KeyMethod {
        String getKey(String attributeName);
    }

    public static void main(String[] args) throws Exception {
        //加密方法
        ENCRYPT_METHOD = ThreeDesUtil::doEncrypt;
        //加密密钥
        ENCRYPT_KEY = BASE;
        //列名，解密方法，解密密钥，脱敏方法
//        initEncryptAttribute("MERCHANT_CONTACTS", ThreeDesUtil::doDecrypt, BASE, EncryptStringUtil::name);
        initEncryptAttribute("商户号", ThreeDesUtil::doDecrypt, BASE, null);
        initEncryptAttribute("电话", ThreeDesUtil::doDecrypt, BASE, null);
        //json数据源
        String filePath = "C:\\Users\\xinjie_guo\\Desktop\\商户信息导出16147505181862.xlsx";
        //表名
        String tableName = "T_DATA_LEDGER_INFO";
        execute(filePath, tableName);
    }

    private static void initEncryptAttribute(String attribute, DBScript3.DecryptMethod decryptMethod, String decryptKey, MixMethod mixMethod) {
        DECRYPT_METHOD_CACHE.put(attribute, decryptMethod::doDecrypt);
        DECRYPT_KEY_CACHE.put(attribute, decryptKey);
        MIX_METHOD_CACHE.put(attribute, mixMethod);
    }

    private static void execute(String filePath, String tableName) throws Exception {
        ReadExcelUtils readExcelUtils = new ReadExcelUtils(filePath);
        List<String> header = readExcelUtils.readExcelHeader();

        //加密字段集(密文字段，即非_MIX)
        List<String> attributes = new ArrayList<>(MIX_METHOD_CACHE.keySet());
        //数据集
        Map<Integer, Map<String, Object>> data = readExcelUtils.readExcelContent2();
        List<String> list = Arrays.asList("商户号");
        StringBuilder builder = new StringBuilder();
        String template = "{0} = {1}";
        System.out.println("\n");
        for (int i = 0; i < data.size(); i++) {
            List<String> sqlSegments = new ArrayList<>();
            JSONObject entity = new JSONObject(data.get(i + 1));
            list.forEach(k -> {
                String v = entity.getString(k);
                if (attributes.contains(k)) {
                    try {
                        if ("null".equals(v)) {
                            System.out.print("null" + "\t");
                        } else if ("/".equals(v)) {
                            System.out.print("/" + "\t");
                        } else {
                            System.out.print(DECRYPT_METHOD_CACHE.get(k).doDecrypt((String) v, BASE) + "\t");
                        }
                    } catch (Exception e) {
                        System.out.println("key:" + k + "   json:" + JSON.toJSONString(entity));
                    }
                } else {
                    System.out.print(v + "\t");
                }
            });
            System.out.print("\n");
        }
    }

    private static String getMixValue(String encryptValue, String attribute) {
        if (StringUtils.isBlank(encryptValue)) {
            return "\"\"";
        }
        if ("null".equals(encryptValue)) {
            return "NULL";
        }
        DecryptMethod decryptMethod = DECRYPT_METHOD_CACHE.get(attribute);
        String decryptKey = DECRYPT_KEY_CACHE.get(attribute);
        String decryptValue = decryptMethod.doDecrypt(encryptValue, decryptKey);
        MixMethod mixMethod = MIX_METHOD_CACHE.get(attribute);
        if (mixMethod == null) mixMethod = getMixMethod(decryptValue, attribute);
        return "\"" + mixMethod.mix(decryptValue) + "\"";
    }

    private static MixMethod getMixMethod(String decryptValue, String attribute) {
        boolean containChinese = CHINESE.matcher(decryptValue).find();
        boolean isPhone = Pattern.matches(PHONE, decryptValue);
        boolean isIdCard = Pattern.matches(IdCard, decryptValue);
        boolean isEmail = decryptValue.contains("@");
        boolean isAddress = attribute.toUpperCase().contains("ADDR");

        MixMethod mixMethod;
        if (isAddress) {
            mixMethod = EncryptStringUtil::address;
        } else if (isEmail) {
            mixMethod = EncryptStringUtil::email;
        } else if (isPhone) {
            mixMethod = EncryptStringUtil::phone;
        } else if (isIdCard) {
            mixMethod = EncryptStringUtil::idCardNo;
        } else if (containChinese) {
            mixMethod = EncryptStringUtil::name;
        } else {
            //意外情况,自定义
            mixMethod = String::valueOf;
        }

        return mixMethod;
    }

    private static String getNewEncryptValue(String encryptValue, String attribute) {
        if (StringUtils.isBlank(encryptValue)) {
            return "\"\"";
        }
        if ("null".equals(encryptValue)) {
            return "NULL";
        }
        DecryptMethod decryptMethod = DECRYPT_METHOD_CACHE.get(attribute);
        String decryptKey = DECRYPT_KEY_CACHE.get(attribute);
        String value = decryptMethod.doDecrypt(encryptValue, decryptKey);
        return "\"" + ENCRYPT_METHOD.doEncrypt(value, ENCRYPT_KEY) + "\"";
    }

}
