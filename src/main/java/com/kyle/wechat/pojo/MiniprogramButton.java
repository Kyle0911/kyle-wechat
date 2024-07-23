package com.kyle.wechat.pojo;

/**
 * 微信跳转小程序菜单按钮
 *
 * @author: kyle.he
 * @date 2022/1/28 
 */
public class MiniprogramButton extends Button {
    /**
     * 类型:
     */
    private String type;

    /**
     * 名称:
     */
    private String name;

    /**
     * 路径:
     */
    private String url;
    /**
     * 小程序 appid
     */
    private String appid;
    /**
     * 小程序的页面
     */
    private String pagepath;

    public MiniprogramButton() {
        super();
    }

    public MiniprogramButton(String type, String name, String url, String appid, String pagepath) {
        super();
        this.type = type;
        this.name = name;
        this.url = url;
        this.appid = appid;
        this.pagepath = pagepath;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getPagepath() {
        return pagepath;
    }

    public void setPagepath(String pagepath) {
        this.pagepath = pagepath;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((appid == null) ? 0 : appid.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((pagepath == null) ? 0 : pagepath.hashCode());
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        result = prime * result + ((url == null) ? 0 : url.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        MiniprogramButton other = (MiniprogramButton) obj;
        if (appid == null) {
            if (other.appid != null) {
                return false;
            }
        } else if (!appid.equals(other.appid)) {
            return false;
        }
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!name.equals(other.name)) {
            return false;
        }
        if (pagepath == null) {
            if (other.pagepath != null) {
                return false;
            }
        } else if (!pagepath.equals(other.pagepath)) {
            return false;
        }
        if (type == null) {
            if (other.type != null) {
                return false;
            }
        } else if (!type.equals(other.type)) {
            return false;
        }
        if (url == null) {
            if (other.url != null)
                return false;
        } else if (!url.equals(other.url))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "MiniprogramButton [type=" + type + ", name=" + name + ", url=" + url + ", appid=" + appid
                + ", pagepath=" + pagepath + "]";
    }
}