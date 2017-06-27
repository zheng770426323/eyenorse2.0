package com.eyenorse.http;

import android.app.Dialog;
import android.content.Context;

import com.eyenorse.base.Config;
import com.loopj.android.http.RequestParams;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhengkq on 2016/12/23.
 */
public class ApiRequestData {
    public Context context;
    private String memberid = null;//用户ID
    private AsyncHttpUtils ahu;
    private static ApiRequestData ard;
    private final String limit = "20";//每页加载的条数
    //登录注册
    private String Register = Config.Base_Url + "member/register";//注册
    private String Send_Mobile_Unload = Config.Base_Url + "captcha/send/mobile/";//注册发送验证码
    private String Check_Mobile_Unload = Config.Base_Url + "captcha/check/mobile/";//注册校验验证码
    private String Member_Mobile_Check = Config.Base_Url + "member/mobile/check/";//验证手机号是否注册
    private String Member_Mobile_Login = Config.Base_Url + "member/login";//登录
    private String Member_Find_Password = Config.Base_Url + "member/password/find/usemobile/";//通过手机找回密码
    private String Member_Name_Check = Config.Base_Url + "member/name/check/";//验证用户名是否正确
    private String Change_Password = Config.Base_Url + "member/password/find/useold/";//通过原密码修改密码
    private String Agreement_Info = Config.Base_Url + "website/privacy/";//用户协议
    //我的
    private String Member_Info = Config.Base_Url + "member/info/";//获取用户信息
    private String Member_Change_Info = Config.Base_Url + "member/info/edit/";//修改用户信息
    private String Activate_Account = Config.Base_Url + "code/activation/";//激活账户
    private String Activate_Agency = Config.Base_Url + "agent/activate/";//激活代理
    private String Check_Agency_Codes = Config.Base_Url + "agent/check_codes/";//校验追踪码发货
    private String Check_Codes = Config.Base_Url + "agent/check_code/";//校验单个追踪码
    private String Add_Feed_Back = Config.Base_Url + "feedback/add/";//用户反馈
    private String Message_List = Config.Base_Url+ "notice/list/";//消息列表
    private String My_Collection = Config.Base_Url +"collection/list/";//我的收藏
    private String My_Collection_Delete = Config.Base_Url + "collection/delete/";//删除收藏
    private String My_Collection_Add = Config.Base_Url + "collection/add/";//添加收藏
    private String My_Order_List = Config.Base_Url + "order/list/";//我的订单
    private String Input_Check_Data = Config.Base_Url + "detectionrecords/release/";//录入自检数据
    private String Get_Check_Data = Config.Base_Url + "detectionrecords/list/";//获取自检数据列表
    private String Push_Message_List = Config.Base_Url + "push/list/";//推送消息列表
    private String Member_Card_List = Config.Base_Url + "goods/list/";//会员卡列表
    private String Order_Add = Config.Base_Url + "order/add/";//生成订单
    private String Invitation_Info = Config.Base_Url + "website/invitetext/";//邀请说明
    private String Invitation_Adress = Config.Base_Url + "member/invitationcode/";//邀请链接
    private String Invitation_List = Config.Base_Url + "invite/list/";//邀请列表
    private String Agency_List = Config.Base_Url + "agent/sub_agent_list/";//下级代理列表
    private String My_Buy_Videos = Config.Base_Url + "video/list/";//购买视频列表
    private String Member_Postal = Config.Base_Url + "code/recharge/member/";//会员充值
    private String Look_History = Config.Base_Url + "footmark/list/";//观看历史
    private String None_Read_Message = Config.Base_Url+ "member/noticenum/";//未读消息数量
    private String Ali_Pay = Config.Base_Url + "trade/notify/alipay/";//支付宝支付成功

    //public String Update_Apk_Path = Config.Base_Url + "";//检查更新
    //首页
    private String Banner_List = Config.Base_Url + "banner/list/";//banner列表
    private String Category_List = Config.Base_Url + "category/recommend_list/";//首页类目列表
    private String Label_List = Config.Base_Url + "label/list/";//标签列表
    private String All_Category_List = Config.Base_Url + "category/list/";//全部类目列表
    private String Sub_Set_List = Config.Base_Url + "category/subsetlist/";//获取下级类目(支持多个父级ID)
    private String Classify_Video_List = Config.Base_Url+"goods/list/";//视频列表
    private String Like_Video_List = Config.Base_Url + "goods/guess_like/";//猜你喜欢
    private String Video_info = Config.Base_Url + "goods/info/";//视频详情
    private String Video_Play = Config.Base_Url + "goods/view/";//视频播放
    private String Goods_Viewed = Config.Base_Url + "goods/viewend/";//视频退出后调用
    private String Promote_Video_Url = Config.Base_Url + "video/promote/";//推广视频
    private String Clarity_Change = Config.Base_Url + "goods/clarity/change/";//观看中切换清晰度
    private String Get_Hot_Search_Record = Config.Base_Url + "search/hotsearch/";//热门搜索
    private String Get_History_Search_Record = Config.Base_Url + "search/list/";//最近历史搜索
    private String Delete_History_Data = Config.Base_Url + "search/clear/";//删除历史搜索
    private String Add_Search_Data = Config.Base_Url + "search/add/";//添加历史记录
    //三方平台
    private String Third_Party_Pay = Config.Base_Url+"trade/pay/thirdparty/";//三方支付订单
    private String Third_Party_Isregister = Config.Base_Url + "thirdparty/check/";//三方平台是否注册
    private String Third_Party_Register = Config.Base_Url + "thirdparty/register/";//三方平台注册
    private String Third_Party_Login = Config.Base_Url + "thirdparty/login/";//三方登录
    private String Third_Party_Info = Config.Base_Url + "thirdparty/info/";//获取绑定的第三方平台信息
    private String Bind_Third_Party = Config.Base_Url + "thirdparty/bind/unlogin/";//绑定第三方平台账户

    //测试SKU
    private String Test_Sku = "http://yanyiduo.caifutang.com/api/goods/info";//测试sku的链接地址，后续弃用
    private ApiRequestData() {
    }

    public static ApiRequestData getInstance(Context context) {
        if (ard == null) {
            synchronized (ApiRequestData.class) {
                ApiRequestData apiRequestData = ard;
                if (apiRequestData == null) {
                    apiRequestData = new ApiRequestData();
                    ard = apiRequestData;
                }
            }
        }
        ard.context = context;
        ard.ahu = AsyncHttpUtils.getInstance(context);
        /*ard.preferences= SharedPreferencesUtils.getInstance(context);
        Member members=ard.getMember();
        if(members!=null&&members.memberid!=null) {
            ard.memberid = ard.getMember().memberid;
        }*/
        return ard;
    }

    /**
     * 未登录时发送短信验证码（短信类型：1 注册 2找回密码 5(绑定第三方登陆账户),7(更换绑定手机(发送验证码给新手机))）
     * 注册发送验证码
     * @param iOAuthReturnCallBack
     * @param parameter
     * @return
     */
    public AsyncHttpUtils getCaptchaSendMobile(IOAuthReturnCallBack iOAuthReturnCallBack, String... parameter) {
        RequestParams parameterReq = new RequestParams();
        parameterReq.put("mobile", parameter[0]);//手机号
        parameterReq.put("type", parameter[1]);//1 注册 2找回密码 5(绑定第三方登陆账户),7(更换绑定手机(发送验证码给新手机))
        ahu.doPost(Send_Mobile_Unload, parameterReq, iOAuthReturnCallBack, true);
        return ahu;
    }

    /**
     * 未登录时验证短信验证码（短信类型：1 注册 2找回密码 5(绑定第三方登陆账户),7(更换绑定手机(发送验证码给新手机))）
     * 注册发送验证码
     * @param iOAuthReturnCallBack
     * @param parameter
     * @return
     */
    public AsyncHttpUtils getCaptchaCheckMobile(IOAuthReturnCallBack iOAuthReturnCallBack, String... parameter) {
        Map<String, String> parameterMap = new HashMap<String, String>();
        parameterMap.put("captcha", parameter[0]);
        parameterMap.put("mobile", parameter[1]);
        parameterMap.put("type", parameter[2]);//1 注册 2找回密码 5(绑定第三方登陆账户),7(更换绑定手机(发送验证码给新手机))
        ahu.doGet(Check_Mobile_Unload, parameterMap, iOAuthReturnCallBack);
        return ahu;
    }

    /**
     * 注册
     * @param iOAuthReturnCallBack
     * @param parameter
     * @return
     */
    public AsyncHttpUtils getRegister(IOAuthReturnCallBack iOAuthReturnCallBack, File file,String... parameter) {
        RequestParams parameterReq = new RequestParams();
        try {
            parameterReq.put("headimage", file);//头像
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        parameterReq.put("captcha", parameter[0]);
        parameterReq.put("mobile", parameter[1]);
        parameterReq.put("name", parameter[2]);
        parameterReq.put("password", parameter[3]);
        ahu.doPost(Register, parameterReq, iOAuthReturnCallBack, true);
        return ahu;
    }

    /**
     * 注册手机号验证
     * @param iOAuthReturnCallBack
     * @param parameter
     * @return
     */
    public AsyncHttpUtils getMemberMobileCheck(IOAuthReturnCallBack iOAuthReturnCallBack, String... parameter) {
        RequestParams parameterReq = new RequestParams();
        parameterReq.put("mobile", parameter[0]);//手机号
        ahu.doPost(Member_Mobile_Check, parameterReq, iOAuthReturnCallBack, true);
        return ahu;
    }
    /**
     * 验证用户名
     * @param iOAuthReturnCallBack
     * @param parameter
     * @return
     */
    public AsyncHttpUtils getMemberNameCheck(IOAuthReturnCallBack iOAuthReturnCallBack, String... parameter) {
        RequestParams parameterReq = new RequestParams();
        parameterReq.put("name", parameter[0]);//用户名
        ahu.doPost(Member_Name_Check, parameterReq, iOAuthReturnCallBack, true);
        return ahu;
    }

    /**
     * 登录
     * @param iOAuthReturnCallBack
     * @param parameter
     * @return
     */
    public AsyncHttpUtils getMemberLogin(IOAuthReturnCallBack iOAuthReturnCallBack, String... parameter) {
        RequestParams parameterReq = new RequestParams();
        parameterReq.put("name", parameter[0]);//用户名
        parameterReq.put("password", parameter[1]);//密码
        ahu.doPost(Member_Mobile_Login, parameterReq, iOAuthReturnCallBack, true);
        return ahu;
    }

    /**
     * 获取用户信息
     * @param iOAuthReturnCallBack
     * @param parameter
     * @return
     */
    public AsyncHttpUtils getMemberInfo(IOAuthReturnCallBack iOAuthReturnCallBack, String... parameter) {
        Map<String, String> parameterMap = new HashMap<String, String>();
        parameterMap.put("memberid", parameter[0]);//用户id
        parameterMap.put("token", parameter[1]);//用户id
        ahu.doGet(Member_Info, parameterMap, iOAuthReturnCallBack);
        return ahu;
    }

    /**
     * 通过手机找回密码
     * @param iOAuthReturnCallBack
     * @param parameter
     * @return
     */
    public AsyncHttpUtils getMemberPassword(IOAuthReturnCallBack iOAuthReturnCallBack, String... parameter) {
        RequestParams parameterReq = new RequestParams();
        parameterReq.put("captcha", parameter[0]);//验证码
        parameterReq.put("mobile", parameter[1]);//手机号
        parameterReq.put("newpassword", parameter[2]);//密码
        ahu.doPost(Member_Find_Password, parameterReq, iOAuthReturnCallBack, true);
        return ahu;
    }
    /**
     * 通过原密码修改密码
     * @param iOAuthReturnCallBack
     * @param parameter
     * @return
     */
    public AsyncHttpUtils getChangePassword(IOAuthReturnCallBack iOAuthReturnCallBack, String... parameter) {
        RequestParams parameterReq = new RequestParams();
        parameterReq.put("memberid", parameter[0]);
        parameterReq.put("newpassword", parameter[1]);//新密码
        parameterReq.put("password", parameter[2]);//原密码
        parameterReq.put("token", parameter[3]);
        ahu.doPost(Change_Password, parameterReq, iOAuthReturnCallBack, true);
        return ahu;
    }
    /**
     * 修改用户资料
     * @param iOAuthReturnCallBack
     * @param parameter
     * @return
     */
    public AsyncHttpUtils getChangeUserInfo(IOAuthReturnCallBack iOAuthReturnCallBack, String... parameter) {
        RequestParams parameterReq = new RequestParams();
        parameterReq.put("email", parameter[0]);//邮箱
        parameterReq.put("headimage", parameter[1]);//头像
        parameterReq.put("memberid", parameter[2]);//用户id
        parameterReq.put("username", parameter[3]);//用户名
        parameterReq.put("token", parameter[4]);//用户名
        ahu.doPost(Member_Change_Info, parameterReq, iOAuthReturnCallBack, true);
        return ahu;
    }

    /**
     * 用户反馈
     * @param iOAuthReturnCallBack
     * @param parameter
     * @return
     */
    public AsyncHttpUtils getAddFeedBack(IOAuthReturnCallBack iOAuthReturnCallBack, String... parameter) {
        RequestParams parameterReq = new RequestParams();
        parameterReq.put("contact", parameter[0]);//联系方式
        parameterReq.put("content", parameter[1]);//建议内容,urlencode编码数据
        parameterReq.put("memberid", parameter[2]);//用户id
        parameterReq.put("token", parameter[3]);
        ahu.doPost(Add_Feed_Back, parameterReq, iOAuthReturnCallBack, true);
        return ahu;
    }
    /**
     * 修改用户资料(上传图片)
     * @param iOAuthReturnCallBack
     * @param parameter
     * @return
     */
    public AsyncHttpUtils getChangeUserInfoImage(IOAuthReturnCallBack iOAuthReturnCallBack, File file, String... parameter) {
        RequestParams parameterReq = new RequestParams();
        //parameterReq.put("email", parameter[0]);//邮箱
        try {
            parameterReq.put("headimage", file);//头像
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        parameterReq.put("memberid", parameter[0]);//用户id
        parameterReq.put("token", parameter[1]);
        //parameterReq.put("username", parameter[3]);//用户名
        ahu.doPost(Member_Change_Info, parameterReq, iOAuthReturnCallBack, true);
        return ahu;
    }
    /**
     * 账号激活
     * @param iOAuthReturnCallBack
     * @param parameter
     * @return
     */
    public AsyncHttpUtils getActivateAccount(IOAuthReturnCallBack iOAuthReturnCallBack, String... parameter) {
        RequestParams parameterReq = new RequestParams();
        parameterReq.put("code", parameter[0]);//激活码
        parameterReq.put("memberid", parameter[1]);
        parameterReq.put("password", parameter[2]);//激活码密码
        parameterReq.put("token", parameter[3]);
        ahu.doPost(Activate_Account, parameterReq, iOAuthReturnCallBack, true);
        return ahu;
    }
    /**
     * 代理商激活
     * @param iOAuthReturnCallBack
     * @param parameter
     * @return
     */
    public AsyncHttpUtils getActivateAgency(IOAuthReturnCallBack iOAuthReturnCallBack, String... parameter) {
        RequestParams parameterReq = new RequestParams();
        parameterReq.put("card", parameter[0]);//激活码
        parameterReq.put("memberid", parameter[1]);
        parameterReq.put("pwd", parameter[2]);//激活码密码
        parameterReq.put("token", parameter[3]);
        ahu.doPost(Activate_Agency, parameterReq, iOAuthReturnCallBack, true);
        return ahu;
    }
    /**
     * 获取消息列表
     * @param iOAuthReturnCallBack
     * @param parameter
     * @return
     */
    public AsyncHttpUtils getMessageList(IOAuthReturnCallBack iOAuthReturnCallBack, String... parameter) {
        Map<String, String> parameterMap = new HashMap<String, String>();
        parameterMap.put("from", parameter[0]);
        parameterMap.put("limit", parameter[1]);
        parameterMap.put("memberid", parameter[2]);//用户id
        ahu.doGet(Message_List, parameterMap, iOAuthReturnCallBack);
        return ahu;
    }
    /**
     * 获取banner列表
     * @param iOAuthReturnCallBack
     * @param parameter
     * @return
     */
    public AsyncHttpUtils getBannerList(IOAuthReturnCallBack iOAuthReturnCallBack, String... parameter) {
        Map<String, String> parameterMap = new HashMap<String, String>();
        parameterMap.put("categoryid", parameter[0]);
        ahu.doGet(Banner_List, parameterMap, iOAuthReturnCallBack);
        return ahu;
    }
    /**
     * 获取首页类目列表
     * @param iOAuthReturnCallBack
     * @param parameter
     * @return
     */
    public AsyncHttpUtils getGategoryList(IOAuthReturnCallBack iOAuthReturnCallBack, String... parameter) {
        Map<String, String> parameterMap = new HashMap<String, String>();
        ahu.doGet(Category_List,parameterMap, iOAuthReturnCallBack);
        return ahu;
    }
    /**
     * 获取首页标签列表
     * @param iOAuthReturnCallBack
     * @param parameter
     * @return
     */
    public AsyncHttpUtils getLabelList(IOAuthReturnCallBack iOAuthReturnCallBack, String... parameter) {
        Map<String, String> parameterMap = new HashMap<String, String>();
        parameterMap.put("isrecommend",parameter[0]);
        ahu.doGet(Label_List,parameterMap, iOAuthReturnCallBack);
        return ahu;
    }
    /**
     * 获取全部类目列表
     * @param iOAuthReturnCallBack
     * @param parameter
     * @return
     */
    public AsyncHttpUtils getAllGategoryList(IOAuthReturnCallBack iOAuthReturnCallBack, String... parameter) {
        Map<String, String> parameterMap = new HashMap<String, String>();
        parameterMap.put("level",parameter[0]);
        parameterMap.put("parentcategoryid",parameter[1]);
        ahu.doGet(All_Category_List,parameterMap, iOAuthReturnCallBack);
        return ahu;
    }
    /**
     * 获取下级类目(支持多个父级ID)
     * @param iOAuthReturnCallBack
     * @param parameter
     * @return
     */
    public AsyncHttpUtils getSubsetList(IOAuthReturnCallBack iOAuthReturnCallBack, String... parameter) {
        Map<String, String> parameterMap = new HashMap<String, String>();
        parameterMap.put("parentcategoryids",parameter[0]);
        ahu.doGet(Sub_Set_List,parameterMap, iOAuthReturnCallBack);
        return ahu;
    }

     /**
     * 获取首页视频列表type:1推荐 2收费 3搜索
     * @param iOAuthReturnCallBack
     * @param parameter
     * @return
     */
    public AsyncHttpUtils getHomeRecommendVideoList(IOAuthReturnCallBack iOAuthReturnCallBack,int type,String... parameter) {
        Map<String, String> parameterMap = new HashMap<String, String>();
        parameterMap.put("from", parameter[0]);
        parameterMap.put("limit", parameter[1]);
        if (type==1){
            parameterMap.put("order", parameter[2]);
        }else if (type==2){
            parameterMap.put("isfree", parameter[2]);
        }else if (type==3){
            parameterMap.put("search", parameter[2]);
        }
        if (parameter.length==4){
            parameterMap.put("order", parameter[3]);
        }
        ahu.doGet(Classify_Video_List, parameterMap, iOAuthReturnCallBack);
        return ahu;
    }
    /**
     * 获取首页视频列表 搜索
     * @param iOAuthReturnCallBack
     * @param parameter
     * @return
     */
    public AsyncHttpUtils getSearchVideoList(IOAuthReturnCallBack iOAuthReturnCallBack,String... parameter) {
        Map<String, String> parameterMap = new HashMap<String, String>();
        parameterMap.put("from", parameter[0]);
        parameterMap.put("limit", parameter[1]);
        parameterMap.put("search", parameter[2]);
        ahu.doGet(Classify_Video_List, parameterMap, iOAuthReturnCallBack);
        return ahu;
    }
    /**
     * 获取首页视频列表 排序模式 可选值:1(综合排序),2(销量优先),3(价格从低到高),4(价格从高到低),5(发布时间),6(视频播放量),7(编辑时间) 默认值:1
     * @param iOAuthReturnCallBack
     * @param parameter
     * @return
     */
    public AsyncHttpUtils getOrderVideoList(IOAuthReturnCallBack iOAuthReturnCallBack,String... parameter) {
        Map<String, String> parameterMap = new HashMap<String, String>();
        parameterMap.put("from", parameter[0]);
        parameterMap.put("limit", parameter[1]);
        parameterMap.put("order", parameter[2]);
        ahu.doGet(Classify_Video_List, parameterMap, iOAuthReturnCallBack);
        return ahu;
    }
    /**
     * 获取首页最新视频列表
     * @param iOAuthReturnCallBack
     * @param parameter
     * @return
     */
    public AsyncHttpUtils getHomeRecentlyVideoList(IOAuthReturnCallBack iOAuthReturnCallBack, String... parameter) {
        Map<String, String> parameterMap = new HashMap<String, String>();
        parameterMap.put("from", parameter[0]);
        parameterMap.put("limit", parameter[1]);
        parameterMap.put("order", parameter[2]);
        ahu.doGet(Classify_Video_List, parameterMap, iOAuthReturnCallBack);
        return ahu;
    }
    /**
     * 获取类目视频
     * @param iOAuthReturnCallBack
     * @param parameter
     * @return
     */
    public AsyncHttpUtils getCategaryVideoList(IOAuthReturnCallBack iOAuthReturnCallBack, String... parameter) {
        Map<String, String> parameterMap = new HashMap<String, String>();
        parameterMap.put("from", parameter[0]);
        parameterMap.put("limit", parameter[1]);
        parameterMap.put("categoryids", parameter[2]);
        ahu.doGet(Classify_Video_List, parameterMap, iOAuthReturnCallBack);
        return ahu;
    }
    /**
     * 获取类目视频
     * @param iOAuthReturnCallBack
     * @param parameter
     * @return
     */
    public AsyncHttpUtils getLabelVideoList(IOAuthReturnCallBack iOAuthReturnCallBack, String... parameter) {
        Map<String, String> parameterMap = new HashMap<String, String>();
        parameterMap.put("from", parameter[0]);
        parameterMap.put("limit", parameter[1]);
        parameterMap.put("label_id", parameter[2]);
        ahu.doGet(Classify_Video_List, parameterMap, iOAuthReturnCallBack);
        return ahu;
    }
    /**
     * 获取猜你喜欢视频
     * @param iOAuthReturnCallBack
     * @param parameter
     * @return
     */
    public AsyncHttpUtils getLikeVideoList(IOAuthReturnCallBack iOAuthReturnCallBack, String... parameter) {
        Map<String, String> parameterMap = new HashMap<String, String>();
        parameterMap.put("goodsid", parameter[0]);
        parameterMap.put("memberid", parameter[1]);
        parameterMap.put("token", parameter[2]);
        ahu.doGet(Like_Video_List, parameterMap, iOAuthReturnCallBack);
        return ahu;
    }


    /**
     * 我的收藏
     * @param iOAuthReturnCallBack
     * @param parameter
     * @return
     */
    public AsyncHttpUtils getMyCollections(IOAuthReturnCallBack iOAuthReturnCallBack, String... parameter) {
        Map<String, String> parameterMap = new HashMap<String, String>();
        parameterMap.put("from", parameter[0]);
        parameterMap.put("limit", parameter[1]);
        parameterMap.put("memberid", parameter[2]);
        parameterMap.put("token",parameter[3]);
        ahu.doGet(My_Collection, parameterMap, iOAuthReturnCallBack);
        return ahu;
    }
    /**
     * 删除收藏
     * @param iOAuthReturnCallBack
     * @param parameter
     * @return
     */
    public AsyncHttpUtils getMyCollectionsDelete(IOAuthReturnCallBack iOAuthReturnCallBack, String... parameter) {
        RequestParams parameterReq = new RequestParams();
        parameterReq.put("memberid", parameter[0]);
        parameterReq.put("goodsids", parameter[1]);
        parameterReq.put("token", parameter[2]);
        ahu.doPost(My_Collection_Delete, parameterReq, iOAuthReturnCallBack, true);
        return ahu;
    }
    /**
     * 添加收藏
     * @param iOAuthReturnCallBack
     * @param parameter
     * @return
     */
    public AsyncHttpUtils getMyCollectionsAdd(IOAuthReturnCallBack iOAuthReturnCallBack, String... parameter) {
        RequestParams parameterReq = new RequestParams();
        parameterReq.put("memberid", parameter[0]);
        parameterReq.put("goodsid", parameter[1]);
        parameterReq.put("token", parameter[2]);
        ahu.doPost(My_Collection_Add, parameterReq, iOAuthReturnCallBack, true);
        return ahu;
    }
    /**
     * 代理列表
     * @param iOAuthReturnCallBack
     * @param parameter
     * @return
     */
    public AsyncHttpUtils getAgencyList(IOAuthReturnCallBack iOAuthReturnCallBack, String... parameter) {
        Map<String, String> parameterMap = new HashMap<String, String>();
        parameterMap.put("memberid", parameter[0]);
        parameterMap.put("token", parameter[1]);
        ahu.doGet(Agency_List, parameterMap, iOAuthReturnCallBack);
        return ahu;
    }
    /**
     * 校验单个追踪码
     * @param iOAuthReturnCallBack
     * @param parameter
     * @return
     */
    public AsyncHttpUtils getCheckCodes(IOAuthReturnCallBack iOAuthReturnCallBack, String... parameter) {
        RequestParams parameterReq = new RequestParams();
        parameterReq.put("code",parameter[0]);
        parameterReq.put("memberid", parameter[1]);//用户id
        parameterReq.put("token", parameter[2]);
        ahu.doPost(Check_Codes, parameterReq, iOAuthReturnCallBack, true);
        return ahu;
    }
    /**
     * 追踪码发货
     * @param iOAuthReturnCallBack
     * @param parameter
     * @return
     */
    public AsyncHttpUtils getCheckAgencyCodes(IOAuthReturnCallBack iOAuthReturnCallBack, List<String> list, String... parameter) {
        RequestParams parameterReq = new RequestParams();
        parameterReq.put("code",list);
        parameterReq.put("count", parameter[0]);
        parameterReq.put("memberid", parameter[1]);//用户id
        parameterReq.put("sub_agent_id", parameter[2]);
        parameterReq.put("token", parameter[3]);
        ahu.doPost(Check_Agency_Codes, parameterReq, iOAuthReturnCallBack, true);
        return ahu;
    }
    /**
     * 获取我的订单列表
     * @param iOAuthReturnCallBack
     * @param parameter
     * @return
     */
    public AsyncHttpUtils getMyOrderList(IOAuthReturnCallBack iOAuthReturnCallBack, String... parameter) {
        Map<String, String> parameterMap = new HashMap<String, String>();
        parameterMap.put("from", parameter[0]);
        parameterMap.put("limit", parameter[1]);
        parameterMap.put("memberid", parameter[2]);
        parameterMap.put("type", parameter[3]);
        ahu.doGet(My_Order_List, parameterMap, iOAuthReturnCallBack);
        return ahu;
    }
    /**
     * 视频详情
     * @param iOAuthReturnCallBack
     * @param parameter
     * @return
     */
    public AsyncHttpUtils getVideoInfo(IOAuthReturnCallBack iOAuthReturnCallBack, String... parameter) {
        Map<String, String> parameterMap = new HashMap<String, String>();
        parameterMap.put("goodsid", parameter[0]);
        if (parameter.length==3){
            parameterMap.put("memberid", parameter[1]);
            parameterMap.put("token", parameter[2]);
        }
        ahu.doGet(Video_info, parameterMap, iOAuthReturnCallBack);
        return ahu;
    }
    /**
     * 视频播放
     * @param iOAuthReturnCallBack
     * @param parameter
     * @return
     */
    public AsyncHttpUtils getVideoPlay(IOAuthReturnCallBack iOAuthReturnCallBack, String... parameter) {
        RequestParams parameterReq = new RequestParams();
        parameterReq.put("clarity", parameter[0]);//清晰度
        parameterReq.put("memberid", parameter[1]);
        parameterReq.put("videoid", parameter[2]);
        parameterReq.put("token", parameter[3]);
        ahu.doPost(Video_Play, parameterReq, iOAuthReturnCallBack, true);
        return ahu;
    }
    /**
     * 视频退出后
     * @param iOAuthReturnCallBack
     * @param parameter
     * @return
     */
    public AsyncHttpUtils getVideoPlayed(IOAuthReturnCallBack iOAuthReturnCallBack, String... parameter) {
        RequestParams parameterReq = new RequestParams();
        parameterReq.put("clarity", parameter[0]);//清晰度
        parameterReq.put("memberid", parameter[1]);
        parameterReq.put("videoid", parameter[2]);
        parameterReq.put("token", parameter[3]);
        parameterReq.put("duration", parameter[4]);
        ahu.doPost(Goods_Viewed, parameterReq, iOAuthReturnCallBack, true);
        return ahu;
    }

    /**
     * 推广视频
     * @param iOAuthReturnCallBack
     * @param parameter
     * @return
     */
    public AsyncHttpUtils getPromoteVideoUrl(IOAuthReturnCallBack iOAuthReturnCallBack, String... parameter) {
        Map<String, String> parameterMap = new HashMap<String, String>();
        ahu.doGet(Promote_Video_Url, parameterMap, iOAuthReturnCallBack);
        return ahu;
    }
    /**
     * 播放中切换视频
     * @param iOAuthReturnCallBack
     * @param parameter
     * @return
     */
    public AsyncHttpUtils getClarityChange(IOAuthReturnCallBack iOAuthReturnCallBack, String... parameter) {
        RequestParams parameterReq = new RequestParams();
        parameterReq.put("clarity", parameter[0]);//清晰度
        parameterReq.put("memberid", parameter[1]);
        parameterReq.put("videoid", parameter[2]);
        parameterReq.put("token", parameter[3]);
        ahu.doPost(Clarity_Change, parameterReq, iOAuthReturnCallBack, true);
        return ahu;
    }
    /**
     * 录入自检数据
     * @param iOAuthReturnCallBack
     * @param parameter
     * @return
     */
    public AsyncHttpUtils getInputCheckData(IOAuthReturnCallBack iOAuthReturnCallBack, String... parameter) {
        RequestParams parameterReq = new RequestParams();
        parameterReq.put("lefteye", parameter[0]);
        parameterReq.put("memberid", parameter[1]);
        parameterReq.put("righteye", parameter[2]);
        parameterReq.put("type",parameter[3]);
        parameterReq.put("token",parameter[4]);
        ahu.doPost(Input_Check_Data, parameterReq, iOAuthReturnCallBack);
        return ahu;
    }
    /**
     * 获取自检数据
     * @param iOAuthReturnCallBack
     * @param parameter
     * @return
     */
    public AsyncHttpUtils getCheckData(IOAuthReturnCallBack iOAuthReturnCallBack, String... parameter) {
        Map<String, String> parameterMap = new HashMap<String, String>();
        parameterMap.put("memberid", parameter[0]);
        parameterMap.put("token", parameter[1]);
        ahu.doGet(Get_Check_Data, parameterMap, iOAuthReturnCallBack);
        return ahu;
    }
    /**
     * 热门搜索记录
     * @param iOAuthReturnCallBack
     * @param parameter
     * @return
     */
    public AsyncHttpUtils getHotSearchRecord(IOAuthReturnCallBack iOAuthReturnCallBack, String... parameter) {
        Map<String, String> parameterMap = new HashMap<String, String>();
        ahu.doGet(Get_Hot_Search_Record, parameterMap, iOAuthReturnCallBack);
        return ahu;
    }

    /**
     * 最近历史搜索记录
     * @param iOAuthReturnCallBack
     * @param parameter
     * @return
     */
    public AsyncHttpUtils getHistorySearchRecord(IOAuthReturnCallBack iOAuthReturnCallBack, String... parameter) {
        Map<String, String> parameterMap = new HashMap<String, String>();
        parameterMap.put("memberid", parameter[0]);
        parameterMap.put("token", parameter[1]);
        ahu.doGet(Get_History_Search_Record, parameterMap, iOAuthReturnCallBack);
        return ahu;
    }
    /**
     * 清空历史搜索
     * @param iOAuthReturnCallBack
     * @param parameter
     * @return
     */
    public AsyncHttpUtils getDeleteHistoryData(IOAuthReturnCallBack iOAuthReturnCallBack, String... parameter) {
        RequestParams parameterReq = new RequestParams();
        parameterReq.put("memberid", parameter[0]);
        parameterReq.put("token", parameter[1]);
        ahu.doPost(Delete_History_Data, parameterReq, iOAuthReturnCallBack);
        return ahu;
    }
    /**
     * 添加历史记录
     * @param iOAuthReturnCallBack
     * @param parameter
     * @return
     */
    public AsyncHttpUtils getAddSearchData(IOAuthReturnCallBack iOAuthReturnCallBack, String... parameter) {
        RequestParams parameterReq = new RequestParams();
        parameterReq.put("memberid", parameter[0]);
        parameterReq.put("searchvalue", parameter[1]);
        parameterReq.put("token", parameter[2]);
        ahu.doPost(Add_Search_Data, parameterReq, iOAuthReturnCallBack);
        return ahu;
    }
    /**
     * 推送消息列表
     * @param iOAuthReturnCallBack
     * @param parameter
     * @return
     */
    public AsyncHttpUtils getPushMessageList(IOAuthReturnCallBack iOAuthReturnCallBack, String... parameter) {
        Map<String, String> parameterMap = new HashMap<String, String>();
        parameterMap.put("from", parameter[0]);
        parameterMap.put("limit", parameter[1]);
        parameterMap.put("memberid", parameter[2]);
        parameterMap.put("token", parameter[3]);
        ahu.doGet(Push_Message_List, parameterMap, iOAuthReturnCallBack);
        return ahu;
    }
    /**
     * 会员卡列表
     * @param iOAuthReturnCallBack
     * @param parameter
     * @return
     */
    public AsyncHttpUtils getMemberCardList(IOAuthReturnCallBack iOAuthReturnCallBack, String... parameter) {
        Map<String, String> parameterMap = new HashMap<String, String>();
        ahu.doGet(Member_Card_List, parameterMap, iOAuthReturnCallBack);
        return ahu;
    }
    /**
     * 订单生成
     * @param iOAuthReturnCallBack
     * @param parameter
     * @return
     */
    public AsyncHttpUtils getOrderCreate(IOAuthReturnCallBack iOAuthReturnCallBack, String... parameter) {
        RequestParams parameterReq = new RequestParams();
        parameterReq.put("goodsid", parameter[0]);
        parameterReq.put("count", parameter[1]);
        parameterReq.put("port", parameter[2]);
        parameterReq.put("memberid", parameter[3]);
        parameterReq.put("token", parameter[4]);
        ahu.doPost(Order_Add, parameterReq, iOAuthReturnCallBack);
        return ahu;
    }
    /**
     * 邀请说明
     * @param iOAuthReturnCallBack
     * @param parameter
     * @return
     */
    public AsyncHttpUtils getInvitationInfo(IOAuthReturnCallBack iOAuthReturnCallBack, String... parameter) {
        Map<String, String> parameterMap = new HashMap<String, String>();
        ahu.doGet(Invitation_Info, parameterMap, iOAuthReturnCallBack);
        return ahu;
    }
    /**
     * 邀请链接
     * @param iOAuthReturnCallBack
     * @param parameter
     * @return
     */
    public AsyncHttpUtils getInvitationAdress(IOAuthReturnCallBack iOAuthReturnCallBack, String... parameter) {
        Map<String, String> parameterMap = new HashMap<String, String>();
        parameterMap.put("memberid", parameter[0]);
        parameterMap.put("token", parameter[1]);
        ahu.doGet(Invitation_Adress, parameterMap, iOAuthReturnCallBack);
        return ahu;
    }
    /**
     * 邀请列表
     * @param iOAuthReturnCallBack
     * @param parameter
     * @return
     */
    public AsyncHttpUtils getInvitationList(IOAuthReturnCallBack iOAuthReturnCallBack, String... parameter) {
        Map<String, String> parameterMap = new HashMap<String, String>();
        parameterMap.put("from", parameter[0]);
        parameterMap.put("limit", parameter[1]);
        parameterMap.put("memberid", parameter[2]);
        parameterMap.put("token", parameter[3]);
        ahu.doGet(Invitation_List, parameterMap, iOAuthReturnCallBack);
        return ahu;
    }
    /**
     * 购买的视频列表
     * @param iOAuthReturnCallBack
     * @param parameter
     * @return
     */
    public AsyncHttpUtils getMyBuyVideos(IOAuthReturnCallBack iOAuthReturnCallBack, String... parameter) {
        Map<String, String> parameterMap = new HashMap<String, String>();
        parameterMap.put("from", parameter[0]);
        parameterMap.put("limit", parameter[1]);
        parameterMap.put("memberid", parameter[2]);
        parameterMap.put("token", parameter[3]);
        ahu.doGet(My_Buy_Videos, parameterMap, iOAuthReturnCallBack);
        return ahu;
    }
    /**
     * 会员充值
     * @param iOAuthReturnCallBack
     * @param parameter
     * @return
     */
    public AsyncHttpUtils getMemberPostal(IOAuthReturnCallBack iOAuthReturnCallBack, String... parameter) {
        RequestParams parameterReq = new RequestParams();
        parameterReq.put("code", parameter[0]);
        parameterReq.put("memberid", parameter[1]);
        parameterReq.put("password", parameter[2]);
        ahu.doPost(Member_Postal, parameterReq, iOAuthReturnCallBack);
        return ahu;
    }
    /**
     * 观看历史
     * @param iOAuthReturnCallBack
     * @param parameter
     * @return
     */
    public AsyncHttpUtils getLookHistory(IOAuthReturnCallBack iOAuthReturnCallBack, String... parameter) {
        Map<String, String> parameterMap = new HashMap<String, String>();
        parameterMap.put("from", parameter[0]);
        parameterMap.put("limit", parameter[1]);
        parameterMap.put("memberid", parameter[2]);
        parameterMap.put("token", parameter[3]);
        ahu.doGet(Look_History, parameterMap, iOAuthReturnCallBack);
        return ahu;
    }
    /**
     * 用户协议
     * @param iOAuthReturnCallBack
     * @param parameter
     * @return
     */
    public AsyncHttpUtils getAgreementInfo(IOAuthReturnCallBack iOAuthReturnCallBack, String... parameter) {
        Map<String, String> parameterMap = new HashMap<String, String>();
        ahu.doGet(Agreement_Info, parameterMap, iOAuthReturnCallBack);
        return ahu;
    }
    /**
     * 未读消息数量
     * @param iOAuthReturnCallBack
     * @param parameter
     * @return
     */
    public AsyncHttpUtils getNoneReadMessage(IOAuthReturnCallBack iOAuthReturnCallBack, String... parameter) {
        Map<String, String> parameterMap = new HashMap<String, String>();
        parameterMap.put("memberid", parameter[0]);
        parameterMap.put("token", parameter[1]);
        ahu.doGet(None_Read_Message, parameterMap, iOAuthReturnCallBack);
        return ahu;
    }

    /**
     * 支付宝支付成功
     * @param iOAuthReturnCallBack
     * @param parameter
     * @return
     */
    public AsyncHttpUtils getAlipay(IOAuthReturnCallBack iOAuthReturnCallBack, String... parameter) {
        RequestParams parameterReq = new RequestParams();
        ahu.doPost(Ali_Pay, parameterReq, iOAuthReturnCallBack);
        return ahu;
    }
    /**
     * 三方支付订单
     * @param iOAuthReturnCallBack
     * @param parameter
     * @return
     */
    public AsyncHttpUtils getThirdParty(IOAuthReturnCallBack iOAuthReturnCallBack, String... parameter) {
        RequestParams parameterReq = new RequestParams();
        parameterReq.put("memberid", parameter[0]);
        parameterReq.put("ordernumbers", parameter[1]);
        parameterReq.put("payway", parameter[2]);
        //parameterReq.put("token", parameter[3]);
        ahu.doPost(Third_Party_Pay, parameterReq, iOAuthReturnCallBack);
        return ahu;
    }
    /**
     * 验证三方平台是否注册
     * @param iOAuthReturnCallBack
     * @param parameter
     * @return
     */
    public AsyncHttpUtils getThirdPartyIsRegister(IOAuthReturnCallBack iOAuthReturnCallBack, String... parameter) {
        RequestParams parameterReq = new RequestParams();
        parameterReq.put("thirdpartyaccount", parameter[0]);
        parameterReq.put("thirdpartytype", parameter[1]);
        ahu.doPost(Third_Party_Isregister, parameterReq, iOAuthReturnCallBack);
        return ahu;
    }
    /**
     * 三方平台注册
     * @param iOAuthReturnCallBack
     * @param parameter
     * @return
     */
    public AsyncHttpUtils getThirdPartyRegister(IOAuthReturnCallBack iOAuthReturnCallBack,File file, String... parameter) {
        RequestParams parameterReq = new RequestParams();
        try {
            parameterReq.put("headimage", file);//头像
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        parameterReq.put("captcha", parameter[0]);
        parameterReq.put("mobile", parameter[1]);
        parameterReq.put("name", parameter[2]);
        parameterReq.put("password", parameter[3]);
        parameterReq.put("thirdpartyaccount", parameter[4]);
        parameterReq.put("thirdpartytype", parameter[5]);
        ahu.doPost(Third_Party_Register, parameterReq, iOAuthReturnCallBack);
        return ahu;
    }
    /**
     * 三方平台登录
     * @param iOAuthReturnCallBack
     * @param parameter
     * @return
     */
    public AsyncHttpUtils getThirdPartyLogin(IOAuthReturnCallBack iOAuthReturnCallBack, String... parameter) {
        RequestParams parameterReq = new RequestParams();
        parameterReq.put("thirdpartyaccount", parameter[0]);
        parameterReq.put("thirdpartytype", parameter[1]);
        ahu.doPost(Third_Party_Login, parameterReq, iOAuthReturnCallBack);
        return ahu;
    }
    /**
     * 获取三方平台信息
     * @param iOAuthReturnCallBack
     * @param parameter
     * @return
     */
    public AsyncHttpUtils getThirdPartyInfo(IOAuthReturnCallBack iOAuthReturnCallBack, String... parameter) {
        RequestParams parameterReq = new RequestParams();
        parameterReq.put("memberid", parameter[0]);
        parameterReq.put("token", parameter[1]);
        ahu.doPost(Third_Party_Info, parameterReq, iOAuthReturnCallBack);
        return ahu;
    }

    /**
     * 绑定已有平台账户
     * @param iOAuthReturnCallBack
     * @param parameter
     * @return
     */
    public AsyncHttpUtils getBindThirdParty(IOAuthReturnCallBack iOAuthReturnCallBack, String... parameter) {
        RequestParams parameterReq = new RequestParams();
        parameterReq.put("captcha", parameter[0]);
        parameterReq.put("mobile", parameter[1]);
        parameterReq.put("password", parameter[2]);
        parameterReq.put("thirdpartyaccount", parameter[3]);
        parameterReq.put("thirdpartytype", parameter[4]);
        ahu.doPost(Bind_Third_Party, parameterReq, iOAuthReturnCallBack);
        return ahu;
    }

    /**
     * 测试sku
     * @param iOAuthReturnCallBack
     * @param parameter
     * @return
     */
    public AsyncHttpUtils getTestSku(IOAuthReturnCallBack iOAuthReturnCallBack, String... parameter) {
        Map<String, String> parameterMap = new HashMap<String, String>();
        parameterMap.put("memberid", parameter[0]);
        parameterMap.put("goodsid", parameter[1]);
        ahu.doGet(Test_Sku, parameterMap, iOAuthReturnCallBack);
        return ahu;
    }

    /**
     * 启动数据加载效果
     * @param title
     * @return
     */
    public Dialog ShowDialog(String title){
        return ahu.ShowDialog(title);
    }
    /**
     * 数据加载框
     * @param title
     * @param isDdismiss
     * @return
     */
    public Dialog ShowDialog(String title,boolean isDdismiss) {
        Dialog dialog=ahu.ShowDialog(title);
        ahu.setDdismiss(isDdismiss);
        return dialog;
    }

    /**
     * 是否自动关闭数据加载对话框
     * @param isDdismiss
     */
    public void setDdismiss(boolean isDdismiss) {
        ahu.setDdismiss(isDdismiss);
    }
    /**
     * 关闭数据加载效果
     * @return
     */
    public Dialog getDialogDismiss(){
        return ahu.getDialogDismiss();
    }

    /**
     * 关闭数据请求
     */
    public void cancel(){
        ahu.cancel();
    }


}
