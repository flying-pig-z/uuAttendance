package com.flyingpig.dataobject.constant;

public class StatusCode {
    /**
     * 操作成功
     */
    public static final int SUCCESS = 1;

    /**
     * 请求参数异常或缺失
     */
    public static final int PARAMETERERROR = 400;

    /**
     * 资源不存在
     */
    public static final int NOTFOUND = 404;

    /**
     * 请求方法错误
     */
    public static final int METHODERROR = 405;

    /**
     * 服务端错误
     */
    public static final int SERVERERROR = 2;

    /**
     * 未经授权
     */
    public static final int UNAUTHORIZED = 0;

}
