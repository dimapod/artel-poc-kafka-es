package com.artel.poc.indexer.service.bean;

import java.util.List;

public class AuthInfo {

    List<String> cookies;
    String xsrf;

    public AuthInfo(List<String> cookies, String xsrf) {
        this.cookies = cookies;
        this.xsrf = xsrf;
    }

    public List<String> getCookies() {
        return cookies;
    }

    public String getXsrf() {
        return xsrf;
    }
}
