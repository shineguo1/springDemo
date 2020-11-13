package gxj.study.demo;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Joiner;
import gxj.study.util.AESUtil;
import gxj.study.util.EncryptStringUtil;
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
public class DBScript3 {

    private static final String BASE = "0RiVs7pV3ico2SdfTj96MPr2";
    private static final String PHONE = "[0-9]{11}";
    private static final Pattern CHINESE = Pattern.compile("[\u4e00-\u9fa5]");
    private static final Map<String, MixMethod> MIX_METHOD_CACHE = new HashMap<>();
    private static  DecryptMethod DECRYPT_METHOD = AESUtil::decrypt;
    private static  KeyMethod KEY_METHOD = DBScript3::lineToHump;

    interface MixMethod {
        String mix(String s);
    }

    interface DecryptMethod {
        String doDecrypt(String src, String key);
    }

    interface KeyMethod {
        String getKey(String attributeName);
    }

    public static void main(String[] args) throws IOException {
        DECRYPT_METHOD = AESUtil::decrypt;
        KEY_METHOD = DBScript3::lineToHump;
        String filePath = "C:\\Users\\xinjie_guo\\Desktop\\T_DATA_LEDGER_INFO.json";
        String tableName = "T_DATA_LEDGER_INFO";
        execute(filePath, tableName);
    }

    private static void execute(String filePath, String tableName) throws IOException {
        String jsonString = DBScript3.readFileByBytes(filePath);
        JSONObject json = JSON.parseObject(jsonString);
        JSONArray header = json.getJSONArray("header");
        //加密字段集
        List<String> attributes = header.getJSONArray(0).stream()
                .filter(o -> String.valueOf(o).endsWith("MIX"))
                .map(String::valueOf)
                .collect(Collectors.toList());
        //数据集
        JSONArray data = json.getJSONArray("data");
        //脱敏方法集
        JSONObject sample = data.getJSONObject(0);
        attributes.forEach(o -> setMixMethod(MIX_METHOD_CACHE, sample, o));

        StringBuilder builder = new StringBuilder();
        String template = "{0} = {1}";
        for (int i = 0; i < data.size(); i++) {
            List<String> sqlSegments = new ArrayList<>();
            JSONObject entity = data.getJSONObject(i);
            attributes.forEach(attribute -> {
                String newAttribute = String.valueOf(attribute).replace("_MIX", "");
                String encryptValue = entity.getString(newAttribute);
                String mixValue = getMixValue(encryptValue, MIX_METHOD_CACHE.get(attribute), KEY_METHOD.getKey(newAttribute));
                String sqlSegment = MessageFormat.format(template, attribute, mixValue);
                sqlSegments.add(sqlSegment);
            });
            String sqlSegment = Joiner.on(',').join(sqlSegments);
            builder.append("UPDATE ").append(tableName).append(" SET ").append(sqlSegment)
                    .append(" WHERE ID = ").append(entity.getString("ID")).append(";\n");
        }

        System.out.println(builder.toString());
    }

    private static String getMixValue(String encryptValue, MixMethod mixMethod, String decryptKey) {
        if (StringUtils.isBlank(encryptValue)) {
            return "\"\"";
        }
        if ("null".equals(encryptValue)) {
            return "NULL";
        }
        return "\"" + mixMethod.mix(DECRYPT_METHOD.doDecrypt(encryptValue, decryptKey)) + "\"";
    }

    private static void setMixMethod(Map<String, MixMethod> mixMethodCache, JSONObject sample, String attribute) {
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


    public static String readFileByBytes(String fileName) throws IOException {
        File file = new File(fileName);
        InputStream in = null;
        StringBuffer sb = new StringBuffer();


        if (file.isFile() && file.exists()) { //判断文件是否存在
            System.out.println("以字节为单位读取文件内容，一次读多个字节：");
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
