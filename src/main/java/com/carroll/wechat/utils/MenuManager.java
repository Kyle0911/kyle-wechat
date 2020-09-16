package com.carroll.wechat.utils;

import com.carroll.wechat.pojo.*;
import lombok.extern.slf4j.Slf4j;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * 菜单管理器类
 *
 * @author liufeng
 * @date 2013-08-08
 */
@Slf4j
public class MenuManager {

    public static void main(String[] args) {
        // 第三方用户唯一凭证
        String appId = "wx7ac8c5ed9b048cc0";
        // 第三方用户唯一凭证密钥
        String appSecret = "718706e2b3fa19a175e8b4a1f9a532f4";

        // 调用接口获取access_token
        AccessTokenAndTicket at = WeixinUtil.getAccessToken(appId, appSecret);

        if (null != at) {
            // 调用接口创建菜单
            int result = 0;
            WeixinUtil.deleteMenu(at.getToken());
            result = WeixinUtil.createMenu(getMenu(), at.getToken());



            // 判断菜单创建结果
            if (0 == result) {
                log.info("菜单创建成功！");
            } else {
                log.info("菜单创建失败，错误码：" + result);
            }
        }
    }

    /**
     * 组装菜单数据
     *
     * @return
     */
    private static Menu getMenu() {
//        CommonButton btn11 = new CommonButton();
//        btn11.setName("行业咨询");
//        btn11.setType("click");
//        btn11.setKey("info");

        ViewButton btn12 = new ViewButton();
        btn12.setName("塔吊系统");
        btn12.setType("view");
//        btn12.setType("click");
//        btn12.setKey("sale_service");
        String url="https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx7ac8c5ed9b048cc0&redirect_uri=";
        try {
            url+= URLEncoder.encode("http://towercrane.aistave.com","utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        url+="&response_type=code&scope=snsapi_base&state=wx&connect_redirect=1#wechat_redirect";
        btn12.setUrl(url);

//        CommonButton btn13 = new CommonButton();
//        btn13.setName("公司动态");
//        btn13.setType("click");
//        btn13.setKey("company_info");
//
//        ViewButton btn14 = new ViewButton();
//        btn14.setName("今日推荐");
//        btn14.setType("view");
//        btn14.setUrl("http://dev.sale.ydt18.com/");
//
//        ViewButton btn15 = new ViewButton();
//        btn15.setName("历史战况");
//        btn15.setType("view");
//        btn15.setUrl("http://dev.sale.ydt18.com/");
//
//        ComplexButton mainBtn1 = new ComplexButton();
//        mainBtn1.setName("宏文天地");
//        mainBtn1.setSub_button(new Button[] { btn14, btn15 });


        /**
         * 这是公众号xiaoqrobot目前的菜单结构，每个一级菜单都有二级菜单项<br>
         *
         * 在某个一级菜单下没有二级菜单的情况，menu该如何定义呢？<br>
         * 比如，第三个一级菜单项不是“更多体验”，而直接是“幽默笑话”，那么menu应该这样定义：<br>
         * menu.setButton(new Button[] { mainBtn1, mainBtn2, btn33 });
         */
        Menu menu = new Menu();
//        menu.setButton(new Button[] { btn11,btn12,mainBtn1});
        menu.setButton(new Button[] { btn12});

        return menu;
    }
}
