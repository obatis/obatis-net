package com.obatis.net.factory;

public class HttpRequestConstant {

    protected enum ContentType {
        NORMAL,
        JSON
    }

    /**
     * Content-Type header key
     */
    public static final String CONTENT_TYPE_KEY = "Content-Type";
    /**
     * Content-Type json 方式
     */
    public static final String CONTENT_TYPE_JSON = "application/json";
}
