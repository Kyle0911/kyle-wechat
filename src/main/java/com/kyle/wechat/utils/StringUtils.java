package com.kyle.wechat.utils;

import com.alibaba.fastjson.JSON;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.*;

//import com.google.gson.Gson;

public class StringUtils {

    public static boolean isNullOrEmpty(String str) {
        return str == null || "".equals(str);
    }

    public static boolean isNotBlank(String str) {
        return !org.springframework.util.StringUtils.isEmpty(str);
    }

    public static boolean isEqual(String str1, String str2) {
        if (str1 == null || str2 == null) {
            return false;
        }
        return str1.equals(str2);
    }

    public static String md5(String data) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(data.getBytes());
        byte[] b = md.digest();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            String hex = Integer.toHexString(b[i] & 0xFF);
            hex = (hex.length() == 1 ? "0" : "") + hex;
            sb.append(hex);
        }
        return sb.toString();
    }

    public static String format(Date date) {
        return format(date, "yyyy-MM-dd HH:mm:ss");
    }

    public static String format(Date date, String pattern) {
        if (date == null || pattern == null || "".equals(pattern.trim()))
            return null;
        String str = null;
        try {
            SimpleDateFormat parser = new SimpleDateFormat(pattern);
            str = parser.format(date);
        } catch (Exception e) {
            return null;
        }
        return str;
    }

    /**
     * 获得随机字符串
     *
     * @param length
     * @return
     */
    public static String getRandomString(int length) {
        String baseStr = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder str = new StringBuilder();
        Random random = new Random();
        int baseLength = baseStr.length();
        for (int i = 0; i < length; i++) {
            str.append(baseStr.charAt(random.nextInt(baseLength - 1)));
        }
        return str.toString();
    }

    /**
     * 获得随机字符串
     *
     * @param length
     * @return
     */
    public static String getRandomString(int length, boolean isNumber) {
        String baseStr = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        if (isNumber) {
            baseStr = "0123456789";
        }
        StringBuilder str = new StringBuilder();
        Random random = new Random();
        int baseLength = baseStr.length();
        for (int i = 0; i < length; i++) {
            str.append(baseStr.charAt(random.nextInt(baseLength - 1)));
        }
        return str.toString();
    }

    public static String toJson(Object obj) {
        return JSON.toJSONString(obj);
    }

    /**
     * 分割姓名
     * 输入："ao ba ma"       ""             "aobama"
     * 输出：["Ao","Bama"]    ["",""]        ["Aobama",""]
     *
     * @param name
     * @return
     */
    public static String[] splitName(String name) {
        String[] ret = new String[]{"", ""};
        if (isNullOrEmpty(name)) {
            return ret;
        }
        String nameStr = name.trim();
        int w = nameStr.indexOf(" ");
        if (w > 0) {  //
            String fn = nameStr.substring(0, w);
            String ln = nameStr.substring(w).replaceAll(" ", "");
            ret[0] = upperFirstLetter(fn);
            ret[1] = upperFirstLetter(ln);
        } else {
            ret[0] = upperFirstLetter(nameStr);
        }
        return ret;
    }

    /**
     * 字符串首字母大写
     *
     * @param string
     * @return
     */
    public static String upperFirstLetter(String string) {
        if (isNullOrEmpty(string)) {
            return string;
        }
        byte[] items = string.getBytes();
        items[0] = (byte) ((char) items[0] - ('a' - 'A'));
        String newStr = new String(items);
        return newStr;
    }

    /**
     * 是否是 汉字
     */
    public static boolean isChinese(char c) {
        boolean result = false;
        if (c >= 19968 && c <= 171941) {// 汉字范围 \u4e00-\u9fa5 (中文)
            result = true;
        }
        return result;
    }

    /**
     * 是否是三字码
     */
    public static boolean isValidateIata(String iata) {
        boolean result = true;
        if (iata.length() != 3) {
            return false;
        }

        if (!iata.matches("^[a-zA-Z]+$")) {
            return false;
        }
        return result;
    }

    /**
     * 获取UUID
     *
     * @return
     */
    public static String getUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * 解析 queryString
     *
     * @param queryString
     * @return
     */
    public static Map<String, String> queryStringParse(String queryString) {
        Map<String, String> paramMap = new HashMap<>();
        StringTokenizer st = new StringTokenizer(queryString, "&");
        while (st.hasMoreTokens()) {
            String pairs = st.nextToken();
            String key = pairs.substring(0, pairs.indexOf('='));
            String value = pairs.substring(pairs.indexOf('=') + 1);
            paramMap.put(key, value);
        }
        return paramMap;
    }

    public static String utf8Togb2312(String str) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            switch (c) {
                case '+':
                    sb.append(' ');
                    break;
                case '%':
                    try {
                        sb.append((char) Integer.parseInt(
                                str.substring(i + 1, i + 3), 16));
                    } catch (NumberFormatException e) {
                        throw new IllegalArgumentException();
                    }
                    i += 2;
                    break;
                default:
                    sb.append(c);
                    break;
            }
        }          // Undo conversion to external encoding
        String result = sb.toString();

        return result;

    }


    public static String getMD5Str(String str) {
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(str.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            System.out.println("NoSuchAlgorithmException caught!");
            System.exit(-1);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        byte[] byteArray = messageDigest.digest();

        StringBuffer md5StrBuff = new StringBuffer();

        for (int i = 0; i < byteArray.length; i++) {
            if (Integer.toHexString(0xFF & byteArray[i]).length() == 1) {
                md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));
            } else {
                md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
            }
        }
        return md5StrBuff.toString();
    }

    public static int parseInt(String data) {
        int value = 0;
        try {
            value = Integer.parseInt(data);
        } catch (Exception e) {
        }
        return value;
    }

//    public static String sha1(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException {
//        MessageDigest crypt = MessageDigest.getInstance("SHA-1");
//        crypt.reset();
//        crypt.update(str.getBytes("UTF-8"));
//        String signature = byteToHex(crypt.digest());
//        return signature;
//    }
//
//    private static String byteToHex(final byte[] hash) {
//        Formatter formatter = new Formatter();
//        for (byte b : hash) {
//            formatter.format("%02x", b);
//        }
//        String result = formatter.toString();
//        formatter.close();
//        return result;
//    }

    public static String sha1(String str) {
        if (str == null || str.length() == 0) {
            return null;
        }
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.update(str.getBytes());
            byte[] digest = md.digest();

            StringBuffer hexstr = new StringBuffer();
            String shaHex = "";
            for (int i = 0; i < digest.length; i++) {
                shaHex = Integer.toHexString(digest[i] & 0xFF);
                if (shaHex.length() < 2) {
                    hexstr.append(0);
                }
                hexstr.append(shaHex);
            }
            return hexstr.toString();
        } catch (Exception e) {
            return null;
        }

    }

    /**
     * 长度不够补齐
     *
     * @param str
     * @param strLength
     * @param rp        补位的串 （0/空格）
     * @param left      左补
     * @return
     */
    public static String addForStr(String str, int strLength, String rp, boolean left) {
        int strLen = str == null ? 0 : str.length();
        StringBuilder sb = new StringBuilder(str == null ? "" : str);
        while (strLen < strLength) {
            if (left) {
                sb.insert(0, rp);
            } else {
                sb.append(rp);
            }
            strLen++;
        }
        return sb.toString();
    }

    /**
     * Convert byte[] to hex string
     *
     * @param src byte[] data
     * @return hex string
     */
    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));

        }
        return d;
    }

    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    /**
     * 使用SHA1算法对字符串进行加密
     * @param str
     * @return
     */
    public static String sha1Encrypt(String str) {

        if (str == null || str.length() == 0) {
            return null;
        }

        char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f' };

        try {

            MessageDigest mdTemp = MessageDigest.getInstance("SHA1");
            mdTemp.update(str.getBytes("UTF-8"));

            byte[] md = mdTemp.digest();
            int j = md.length;
            char buf[] = new char[j * 2];
            int k = 0;

            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                buf[k++] = hexDigits[byte0 >>> 4 & 0xf];
                buf[k++] = hexDigits[byte0 & 0xf];
            }

            return new String(buf);

        } catch (Exception e) {
            return null;
        }
    }

//    public static void main(String[]args){

//        String [] res = getBirthdayAndGender("370802940221002");

//        System.out.println(sha1("jsapi_ticket=kgt8ON7yVITDhtdwci0qefPYKgKm9R4SWrypVC96RSFF2zHyiYmnt8WBgu1GN5r0cUoe4L4AvWr2rlyGl06pbA&noncestr=53a74bc7-dd25-4217-bd7c-fca419577481&timestamp=1476607943&url=http://dev.sale.ydt18.com/index.html"));
//        System.out.println("元素 “General” 的子元素 “Copy” 无效。应为可能元素的列表: “TerminalTime”。".matches("^[0-9]+.*$"));
//    }
}
