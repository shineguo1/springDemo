package gxj.study.demo;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Joiner;
import gxj.study.util.AESUtil;
import gxj.study.util.EncryptStringUtil;
import gxj.study.util.ReadExcelUtils;
import gxj.study.util.ThreeDesUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author xinjie_guo
 * @version 1.0.0 createTime:  2020/7/31 14:24
 * @description
 */
@Slf4j
public class DBScript5 {

    private static final String BASE = "0RiVs7pV3ico2SdfTj96MPr2";
    private static final String PHONE = "[0-9]{11}";
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
        initEncryptAttribute("MERCHANT_CONTACTS", ThreeDesUtil::doDecrypt, BASE, EncryptStringUtil::name);
        initEncryptAttribute("CONTACT_NUMBER", ThreeDesUtil::doDecrypt, BASE, EncryptStringUtil::phone);
        initEncryptAttribute("CONTACT_EMAIL", ThreeDesUtil::doDecrypt, BASE, EncryptStringUtil::email);
        initEncryptAttribute("SALES_CONTACT", ThreeDesUtil::doDecrypt, BASE, EncryptStringUtil::name);
        //json数据源
        String filePath = "C:\\Users\\xinjie_guo\\Desktop\\T_DATA_LEDGER_INFO-2.xls";
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

        //加密字段集(包含_MIX)
        List<String> attributes = header.stream()
                .filter(o -> String.valueOf(o).endsWith("MIX"))
                .map(String::valueOf)
                .collect(Collectors.toList());
        attributes = new ArrayList<>(MIX_METHOD_CACHE.keySet());
        //数据集
        Map<Integer, Map<String, Object>> data = readExcelUtils.readExcelContent2();

        StringBuilder builder = new StringBuilder();
        String template = "{0} = {1}";
        for (int i = 0; i < data.size(); i++) {
            List<String> sqlSegments = new ArrayList<>();
            JSONObject entity = new JSONObject(data.get(i+1));
            attributes.forEach(attribute -> {
                String newAttribute = String.valueOf(attribute).replace("_MIX", "");
                String encryptValue = entity.getString(newAttribute);
                String newEncryptValue = getNewEncruptValue(encryptValue, newAttribute);
                String sqlSegment1 = MessageFormat.format(template, newAttribute, newEncryptValue);
                sqlSegments.add(sqlSegment1);
                String mixValue = getMixValue(encryptValue, newAttribute);
                String sqlSegment2 = MessageFormat.format(template, attribute+"_MIX", mixValue);
                sqlSegments.add(sqlSegment2);
            });
            String sqlSegment = Joiner.on(',').join(sqlSegments);
            builder.append("UPDATE ").append(tableName).append(" SET ").append(sqlSegment)
                    .append(" WHERE ID = ").append(entity.getString("ID")).append(";\n");
        }

        System.out.println(builder.toString());
    }

    private static String getMixValue(String encryptValue, String attribute) {
        if (StringUtils.isBlank(encryptValue)) {
            return "\"\"";
        }
        if ("null".equals(encryptValue)) {
            return "NULL";
        }
        MixMethod mixMethod = MIX_METHOD_CACHE.get(attribute);
        DecryptMethod decryptMethod = DECRYPT_METHOD_CACHE.get(attribute);
        String decryptKey = DECRYPT_KEY_CACHE.get(attribute);
        return "\"" + mixMethod.mix(decryptMethod.doDecrypt(encryptValue, decryptKey)) + "\"";
    }

    private static String getNewEncruptValue(String encryptValue, String attribute) {
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

    public static String readFileByBytes(String fileName) throws IOException {
        File file = new File(fileName);
        InputStream in = null;
        StringBuffer sb = new StringBuffer();


        if (file.isFile() && file.exists()) { //判断文件是否存在
            // 一次读多个字节
            byte[] tempbytes = new byte[1024];
            int byteread = 0;
            in = new FileInputStream(file);
            // 读入多个字节到字节数组中，byteread为一次读入的字节数
            while ((byteread = in.read(tempbytes)) != -1) {
                //  System.out.write(tempbytes, 0, byteread);
                String str = new String(tempbytes, 0, byteread);
                sb.append(str);
            }
        } else {
            log.info("找不到指定的文件，请确认文件路径是否正确");
        }
        return sb.toString();
    }

    private static Pattern linePattern = Pattern.compile("_(\\w)");

    /**
     * 下划线转驼峰
     */
    public static String lineToHump(String str) {
        str = str.toLowerCase();
        Matcher matcher = linePattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }
}
