package gxj.study.demo;

import com.alibaba.dubbo.common.utils.StringUtils;
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
public class DBScript6 {

    private static final String BASE = "0RiVs7pV3ico2SdfTj96MPr2";
    private static final String PHONE = "[0-9]{11}";
    private static final Pattern CHINESE = Pattern.compile("[\u4e00-\u9fa5]");
    private static final Map<String, MixMethod> MIX_METHOD_CACHE = new HashMap<>();
    private static final Map<String, DecryptMethod> DECRYPT_METHOD_CACHE = new HashMap<>();
    private static final Map<String, String> DECRYPT_KEY_CACHE = new HashMap<>();
    private static EncryptMethod ENCRYPT_METHOD;
    private static String ENCRYPT_KEY;

    private static DBScript6.DecryptMethod DECRYPT_METHOD = AESUtil::decrypt;
    private static DBScript6.KeyMethod KEY_METHOD = DBScript6::lineToHump;

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
        ENCRYPT_KEY = "0RiVs7pV3ico2SdfTj96MPr2";
        ENCRYPT_METHOD = ThreeDesUtil::doEncrypt;
        DECRYPT_METHOD = AESUtil::decrypt;
        // 渠道台账表，phone字段的key是mobile
        KEY_METHOD = s->{
            if(s.toUpperCase().equals("PHONE"))return "mobile";
            else return lineToHump(s);
        };
        String filePath = "C:\\Users\\xinjie_guo\\Desktop\\T_CHANNEL_DATA_LEDGER_INFO.xls";
        String tableName = "T_CHANNEL_DATA_LEDGER_INFO";
        execute(filePath, tableName);
    }


    private static void execute(String filePath, String tableName) throws Exception {
        ReadExcelUtils readExcelUtils = new ReadExcelUtils(filePath);
        List<String> header = readExcelUtils.readExcelHeader();

        //加密字段集
        List<String> attributes = header.stream()
                .filter(o -> String.valueOf(o).endsWith("MIX"))
                .map(String::valueOf)
                .collect(Collectors.toList());
        //数据集
        Map<Integer, Map<String, Object>> data = readExcelUtils.readExcelContent2();
        //脱敏方法集
        Map<String, Object> sample = data.get(1);
        attributes.forEach(o -> setMixMethod(MIX_METHOD_CACHE, new JSONObject(sample), o));

        StringBuilder builder = new StringBuilder();
        String template = "{0} = {1}";
        for (int i = 0; i < data.size(); i++) {
            List<String> sqlSegments = new ArrayList<>();
            JSONObject entity = new JSONObject(data.get(i + 1));
            attributes.forEach(attribute -> {
                String newAttribute = String.valueOf(attribute).replace("_MIX", "");
                String encryptValue = entity.getString(newAttribute);
                String newEncryptValue = getNewEncruptValue(encryptValue, newAttribute);
                String sqlSegment1 = MessageFormat.format(template, newAttribute, newEncryptValue);
                sqlSegments.add(sqlSegment1);
                String mixValue = getMixValue(encryptValue, newAttribute);
                String sqlSegment2 = MessageFormat.format(template, attribute, mixValue);
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
        MixMethod mixMethod = MIX_METHOD_CACHE.get(attribute+"_MIX");
        String decryptKey = KEY_METHOD.getKey(attribute);
        return "\"" + mixMethod.mix(DECRYPT_METHOD.doDecrypt(encryptValue, decryptKey)) + "\"";
    }

    private static String getNewEncruptValue(String encryptValue, String attribute) {
        if (StringUtils.isBlank(encryptValue)) {
            return "\"\"";
        }
        if ("null".equals(encryptValue)) {
            return "NULL";
        }
//        DecryptMethod decryptMethod = DECRYPT_METHOD_CACHE.get(attribute);
//        String decryptKey = DECRYPT_KEY_CACHE.get(attribute);
        String decryptKey = KEY_METHOD.getKey(attribute);
        String value = DECRYPT_METHOD.doDecrypt(encryptValue, decryptKey);
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


    private static void setMixMethod(Map<String, DBScript6.MixMethod> mixMethodCache, JSONObject sample, String attribute) {
        String newAttribute = String.valueOf(attribute).replace("_MIX", "");
        String encryptValue = sample.getString(newAttribute);
        String value = DECRYPT_METHOD.doDecrypt(encryptValue, KEY_METHOD.getKey(newAttribute));

        boolean containChinese = CHINESE.matcher(value).find();
        boolean isPhone = Pattern.matches(PHONE, value);
        boolean isEmail = value.contains("@");
        boolean isAddress = attribute.toUpperCase().contains("ADDR");

        if (isAddress) {
            mixMethodCache.put(attribute, EncryptStringUtil::address);
        } else if (isEmail) {
            mixMethodCache.put(attribute, EncryptStringUtil::email);
        } else if (isPhone) {
            mixMethodCache.put(attribute, EncryptStringUtil::phone);
        } else if (containChinese) {
            mixMethodCache.put(attribute, EncryptStringUtil::name);
        } else {
            //意外情况,自定义
            mixMethodCache.put(attribute, String::valueOf);
        }
    }
}
