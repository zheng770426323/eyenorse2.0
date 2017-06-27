package com.eyenorse.utils;

import com.eyenorse.base.Config;

/**
 * Created by zhengkq on 2017/1/17. 微信信息
 */

public class ParameterConfig {
    /**
     * 微信
     */
    // appid
    public static final String WX_APP_ID = "wxa4a841954d5f20b2";
    // 商户号
    public static final String WX_MCH_ID = "1439656502";
    // API密钥，在商户平台设置
    public static final String WX_API_KEY = "Hangzhouyanhushishujukeji2017215";
    //服务器回调接口
    public static final String WX_notifyUrl = Config.Base_Url+"trade/notify/wxpay/";// 用于微信支付成功的回调（按自己需求填写）

    /**
     * 支付宝
     */
    // appid
    public static final String RSA_PRIVATE = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAKw0+zM7w9w3k3uaCUiyWKF4QBQp6Oq/oqfq0yKy9tzYvvRMcFKRfKTf1o2vKIjf4cYFqoB56HZa/exRg003SvwruYvJY7a8QYNXw5XJjOlrYTbnndJ8uOlVdfp/9uILG3euwZMrWGb6VryM16BhjOUe618qyEuXRbzbpzyYhtp3AgMBAAECgYEAlmRy2Wyhgapbt/9vijiPZP0S4Uk0rAcOs51GJTCsnXUBV3OGSOXcOFVwGC7XtKnpkDoUjL1qwip3RjJt/DqE1cR/fAWRH8nIzPVkAnE9JsRg7uAQIhKKw7WA773OVcmrtXh5YMfr5unSWYkBbZYtlMjknRalT/dWUFzOtkO7bJECQQDgdc2VaVNkjVsNHWdLDjHfAIYYRgjSujvUmop3DtTCMfiXqo/NQKwdgL72VptXcHUwvkQXO5OvodpQtOQ0VA1fAkEAxGeLmideigrWMVZ5Cnxx3UqxPmpl/m/0VnKieJm+gEhxs8twllN0r5MYDmf6do67BHU1CixE2A0fPOoA0jix6QJAWAqdCt/3T9eKQUipPW1h7mCcOGUwDMfzs96yNSTiGxHSulqycQXO/PuE68bow5FexjG7L4m00g0gPsvVPxlA7QJALh5WVJtcArQquakhgQnZvunUFuDrFET7rfT7G7CWYA8iy+JAjLwKEVRL+M4HrSE03wYar0nX3JewNyCHktWHSQJAHimqM99vbUkl7t1hLY6hdLQbIDztNUwNfwPytpnKxfi3sMF5ikYtXDfN5Yb6n243O59pVSf9V/7qpEcp98Gn8g==";
    //合作者身份ID
    public static final String PID = "2088521638856323";
    //卖家支付宝账号
    public static final String TARGET_ID = "hzyanhushi@163.com";
    //支付宝支付业务：入参app_id
    public static final String APPID = "";
    //服务器回调接口
    public static final String ALI_notifyUrl = Config.Base_Url+"trade/notify/alipay";// 用于微信支付成功的回调（按自己需求填写）
    }
