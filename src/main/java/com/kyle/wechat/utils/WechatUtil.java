package com.kyle.wechat.utils;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.kyle.wechat.pojo.AccessTokenAndTicket;
import com.kyle.wechat.pojo.AccessTokenInfo;
import com.kyle.wechat.pojo.LoginCodeParseResult;
import com.kyle.wechat.pojo.Menu;
import lombok.extern.slf4j.Slf4j;
import sun.misc.BASE64Decoder;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.URL;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.Set;

/**
 * 公众平台通用接口工具类
 *
 * @author liuyq
 * @date 2013-08-09
 */
@Slf4j
public class WechatUtil {
    //    private static Logger log = LoggerFactory.getLogger(WeixinUtil.class);
    public static String CBC_CIPHER_ALGORITHM = "AES/CBC/PKCS5Padding";
    public static String ECB_CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";
    private static final String KEY_ALGORITHM = "AES";
    private static final String UTF8 = "UTF-8";
    private static final String SECURERANDOM_MODE = "SHA1PRNG";
    // 获取access_token的接口地址（GET） 限200（次/天）
    public final static String access_token_url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
    public final static String ticket_token_url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=ACCESS_TOKEN&type=TYPE";
    // 菜单创建（POST） 限100（次/天）
    public static String menu_create_url = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";
    public static String menu_delete_url = "https://api.weixin.qq.com/cgi-bin/menu/delete?access_token=ACCESS_TOKEN";
    private static final String GEt_ACCESS_TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
    private static BASE64Decoder decoder = new BASE64Decoder();

    /**
     * 获取access_token
     *
     * @param appid     凭证
     * @param appsecret 密钥
     * @return
     */
    public static AccessTokenAndTicket getAccessToken(String appid, String appsecret) {
        AccessTokenAndTicket accessToken = null;

        String requestUrl = access_token_url.replace("APPID", appid).replace("APPSECRET", appsecret);
        JSONObject jsonObject = httpRequest(requestUrl, "GET", null);
        // 如果请求成功
        if (null != jsonObject) {
            try {
                accessToken = new AccessTokenAndTicket();
                accessToken.setToken(jsonObject.getString("access_token"));
                accessToken.setExpiresIn(jsonObject.getIntValue("expires_in"));
                accessToken.setCreateTime(new Date());
            } catch (JSONException e) {
                accessToken = null;
                // 获取token失败
                log.error("获取token失败 errcode:{} errmsg:{}", jsonObject.getIntValue("errcode"), jsonObject.getString("errmsg"));
            }
        }
        requestUrl = ticket_token_url.replace("ACCESS_TOKEN", accessToken.getToken());
        requestUrl = requestUrl.replace("TYPE", "jsapi");
        jsonObject = httpRequest(requestUrl, "GET", null);
        // 如果请求成功
        if (null != jsonObject) {
            try {
                accessToken.setJsTicket(jsonObject.getString("ticket"));
            } catch (JSONException e) {
                accessToken = null;
                // 获取token失败
                log.error("获取ticket失败 errcode:{} errmsg:{}", jsonObject.getIntValue("errcode"), jsonObject.getString("errmsg"));
            }
        }
        return accessToken;
    }

    /**
     * 创建菜单
     *
     * @param menu        菜单实例
     * @param accessToken 有效的access_token
     * @return 0表示成功，其他值表示失败
     */
    public static int createMenu(Menu menu, String accessToken) {
        int result = 0;

        // 拼装创建菜单的url
        String url = menu_create_url.replace("ACCESS_TOKEN", accessToken);
        // 将菜单对象转换成json字符串
        String jsonMenu = JSONObject.toJSONString(menu);
        // 调用接口创建菜单
        JSONObject jsonObject = httpRequest(url, "POST", jsonMenu);

        if (null != jsonObject) {
            if (0 != jsonObject.getIntValue("errcode")) {
                result = jsonObject.getIntValue("errcode");
                log.error("创建菜单失败 errcode:{} errmsg:{}", jsonObject.getIntValue("errcode"), jsonObject.getString("errmsg"));
            }
        }

        return result;
    }

    public static int deleteMenu(String accessToken) {
        int result = 0;

        // 拼装创建菜单的url
        String url = menu_delete_url.replace("ACCESS_TOKEN", accessToken);
        // 调用接口创建菜单
        JSONObject jsonObject = httpRequest(url, "GET", "{}");

        if (null != jsonObject) {
            if (0 != jsonObject.getIntValue("errcode")) {
                result = jsonObject.getIntValue("errcode");
                log.error("删除菜单失败 errcode:{} errmsg:{}", jsonObject.getIntValue("errcode"), jsonObject.getString("errmsg"));
            }
        }

        return result;
    }

    /**
     * 发起https请求并获取结果
     *
     * @param requestUrl    请求地址
     * @param requestMethod 请求方式（GET、POST）
     * @param outputStr     提交的数据
     * @return JSONObject(通过JSONObject.get ( key)的方式获取json对象的属性值)
     */
    public static JSONObject httpRequest(String requestUrl, String requestMethod, String outputStr) {
        JSONObject jsonObject = null;
        StringBuffer buffer = new StringBuffer();
        try {
            // 创建SSLContext对象，并使用我们指定的信任管理器初始化
            TrustManager[] tm = {new MyX509TrustManager()};
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
            sslContext.init(null, tm, new java.security.SecureRandom());
            // 从上述SSLContext对象中得到SSLSocketFactory对象
            SSLSocketFactory ssf = sslContext.getSocketFactory();

            URL url = new URL(requestUrl);
            HttpsURLConnection httpUrlConn = (HttpsURLConnection) url.openConnection();
            httpUrlConn.setSSLSocketFactory(ssf);

            httpUrlConn.setDoOutput(true);
            httpUrlConn.setDoInput(true);
            httpUrlConn.setUseCaches(false);
            // 设置请求方式（GET/POST）
            httpUrlConn.setRequestMethod(requestMethod);

            if ("GET".equalsIgnoreCase(requestMethod)) {
                httpUrlConn.connect();
            }

            // 当有数据需要提交时
            if (null != outputStr) {
                OutputStream outputStream = httpUrlConn.getOutputStream();
                // 注意编码格式，防止中文乱码
                outputStream.write(outputStr.getBytes("UTF-8"));
                outputStream.close();
            }

            // 将返回的输入流转换成字符串
            InputStream inputStream = httpUrlConn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String str = null;
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }
            log.debug("request result:" + buffer.toString());
            bufferedReader.close();
            inputStreamReader.close();
            // 释放资源
            inputStream.close();
            inputStream = null;
            httpUrlConn.disconnect();
            jsonObject = JSONObject.parseObject(buffer.toString());
        } catch (ConnectException ce) {
            log.error("Weixin server connection timed out.");
        } catch (Exception e) {
            log.error("https request error:{}", e);
        }
        return jsonObject;
    }

    /**
     * 校验登录凭证
     *
     * @param code
     * @param appid
     * @param appsecret
     * @return
     */
    public static LoginCodeParseResult jscode2session(String code, String appid, String appsecret) {
        try {
            String url = String.format("https://api.weixin.qq.com/sns/jscode2session?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code",
                    appid,
                    appsecret,
                    code
            );
            JSONObject jsonObject = httpRequest(url, "GET", null);
            String openIdKey = "openid";
            String session_key = "session_key";
            String unionidKey = "unionid";
            if (jsonObject.containsKey(openIdKey) && jsonObject.containsKey(session_key)) {
                String miniOpenid = jsonObject.getString(openIdKey);
                String sessionKey = jsonObject.getString(session_key);
                String unionid = jsonObject.getString(unionidKey);
                return new LoginCodeParseResult(miniOpenid, sessionKey, unionid);
            } else {
                log.error("登录凭证校验失败：{}", jsonObject.toJSONString());
                return null;
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    /**
     * 根据网页授权code获取accessToken
     *
     * @param code
     * @param appId
     * @param appSecret
     * @return
     */
    public static AccessTokenInfo authcode2AccessToken(String code, String appId, String appSecret) {

        String url = GEt_ACCESS_TOKEN_URL.replace("APPID", appId).replace("SECRET", appSecret);
        url = url.replace("CODE", code);
        JSONObject accessToken = WechatUtil.httpRequest(url, "GET", "");
        if (accessToken == null || accessToken.containsKey("errcode")) {
            return null;
        } else {
            AccessTokenInfo accessTokenInfo = new AccessTokenInfo();
            accessTokenInfo.setAccessToken(accessToken.getString("access_token"));
            accessTokenInfo.setExpiresIn(accessToken.getLong("expires_in"));
            accessTokenInfo.setRefreshToken(accessToken.getString("refresh_token"));
            accessTokenInfo.setOpenid(accessToken.getString("openid"));
            accessTokenInfo.setScope(accessToken.getString("scope"));
            return accessTokenInfo;
        }

    }

    /**
     * 微信敏感信息解密
     *
     * @param content     加密内容
     * @param skey        密钥
     * @param ivParameter 对称解密算法初始向量
     * @return
     */
    public static String weixinDecrypt(String content, String skey, String ivParameter) {
        try {
            // 根据微信文档要求需要把 密文、密钥、iv 使用BASE64进行解码
            byte[] keyByte = new BASE64Decoder().decodeBuffer(skey);
            byte[] contentByte = decoder.decodeBuffer(content);
            byte[] ivByte = decoder.decodeBuffer(ivParameter);
            // 生成密码
            SecretKeySpec keySpec = new SecretKeySpec(keyByte, KEY_ALGORITHM);
            // 生成IvParameterSpec
            IvParameterSpec iv = new IvParameterSpec(ivByte);
            // 初始化解密 指定模式 AES/CBC/PKCS5Padding
            Cipher cipher = Cipher.getInstance(CBC_CIPHER_ALGORITHM);
            // 指定解密模式 传入密码 iv
            cipher.init(Cipher.DECRYPT_MODE, keySpec, iv);
            // 解密
            byte[] result = cipher.doFinal(contentByte);
            return new String(result, UTF8);
        } catch (Exception ex) {
            log.error("【解密错误】{}", ex.getMessage());
            return null;
        }
    }


    public static String sha1Sign(Map<String, String> paramsMap) {
        return StringUtils.sha1Encrypt(sortParams(paramsMap));
    }

    /**
     * 根据参数名称对参数进行字典排序
     *
     * @param paramsMap
     * @return
     */
    private static String sortParams(Map<String, String> paramsMap) {
        Set<String> keySet = paramsMap.keySet();
        String[] keyArray = (String[]) keySet.toArray(new String[keySet.size()]);
        Arrays.sort(keyArray);
        StringBuilder sb = new StringBuilder();
        String[] var6 = keyArray;
        int length = keyArray.length;

        for (int i = 0; i < length; ++i) {
            String k = var6[i];
            if (!k.equals("sign") && (paramsMap.get(k)).trim().length() > 0) {
                sb.append(k).append("=").append((paramsMap.get(k)).trim()).append("&");
            }
        }
        return sb.substring(0, sb.lastIndexOf("&"));
    }

}
