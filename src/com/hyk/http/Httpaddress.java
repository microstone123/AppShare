package com.hyk.http;

/**
 * @ClassName: Httpaddress
 * @Description: TODO(接口管理地址)
 * @author linhaishi
 * @date 2013-9-16 下午5:13:16
 * 
 */
public class Httpaddress {

	/**
	 * 手机OS类型�?为andriod�?为IOS，如不传则默认为1
	 */
	public static String PHONE_TYPE = "0";
	/**
	 * QQ登陆type 0
	 */
	public static String QQ_TYPE = "0";
	/**
	 * 微博登陆type 1
	 */
	public static String WEIBO_TYPE = "1";
	/**
	 * ip地址
	 */
	public static String IP_ADDRESS = "http://14.17.99.197:80/";
	/**
	 * 登陆接口
	 */
	public static String LOGIN_ADDRESS = "aqs/u/login";
	/**
	 * 上传正在使用软件
	 */
	public static String UPLOAD_USERAPP = "aqs/a/uploaduserapp";
	
	/**
	 * 用户本地应用上传
	 */
	public static String UPLOADAPPINFO_ADDRESS = "aqs/u/uploadappinfo";

	/**
	 * 获取app是否更新
	 */
	public static String APP_UPDATE = "aqs/a/appupdate";
	/**
	 * 查询周边的用户APP信息
	 */
	public static String NEARESTUSER_ADDRESS = "aqs/a/nearestuser";
	/**
	 * 根据APPID查询APP详细信息
	 */
	public static String APPID_ADDRESS = "aqs/a/id";
	/**
	 * 根据APPID查询APP评论
	 */
	public static String APPID_REVIEW_ADDRESS = "aqs/a/userComment";
	/**
	 * 随机获取热门应用集合
	 */
	public static String APPHOT_ADDRESS = "aqs/a/hot";
	/**
	 * 根据关键字搜索应用信�?
	 */
	public static String APP_APPNAME = "aqs/a/name";
	/**
	 * �?��取用户的APP信息
	 */
	public static String APP_USERAPP = "aqs/a/userapp";
	/**
	 * 向服务器上传用户的位置点信息
	 */
	public static String APP_UPLOADLOCATION = "aqs/u/uploadlocation";
	/**
	 * 注册
	 */
	public static String SET_REGIST = "aqs/u/regphonenum";
	/**
	 * 修改密码
	 */
	public static String SET_PWD = "aqs/u/setpwd";
	/**
	 * 修改昵称
	 */
	public static String SET_USERNAME = "aqs/u/setname";
	/**
	 * 更改个性签名
	 */
	public static String SET_SIGN = "aqs/u/setsignname";
	/**
	 * 获取我的动态
	 */
	public static String GET_MYDYNAMIC = "aqs/u/usernews";
	/**
	 * 获取用户交际圈动态
	 */
	public static String USERNEWS_OTHER = "aqs/u/usernewsother";
	/**
	 * 上传头像
	 */
	public static String UPLOAD_IMAGE = "aqs/u/uploadheadpic";

	/**
	 * 我关注的好友的app
	 */
	public static String FRIENDS_APP = "aqs/a/friendsapp";

	/**
	 * 发表动态
	 */
	public static String SEND_NEWS = "aqs/u/sendnews";

	/**
	 * 发表评论
	 */
	public static String SEND_COMMENT = "aqs/u/sendcomment";

	/**
	 * 关注(取消关注和关注)
	 */
	public static String USER_FIREND = "aqs/u/userfirend";

	/**
	 * 我的好友
	 */
	public static String MY_FRIENDS = "aqs/u/myfriends";

	/**
	 * 用户信息
	 */
	public static String USER_INFO = "aqs/u/userinfo";
	
	/**
	 * 动态点赞
	 */
	public static String NEWS_PRAISE = "aqs/u/newspraise";
	
	/**
	 * 用户接收的最新的消息列表
	 */
	public static String NEWS_MESSAGELIST = "aqs/message/messagelist";

	/**
	 * 好友正在使用软件
	 */
	public static String USER_APPLIST = "aqs/a/userapplist";
	
	/**
	 * 用户反馈消息
	 */
	public static String USER_FEEDBACK = "aqs/message/feedback";
	
	/**
	 * 聊天历史记录
	 */
	public static String USER_CHATHISTORY = "aqs/message/chatHistory";
	/**
	 * 下载app接口
	 */
	public static String USER_DOWNAPP = "aqs/a/downapp";

}
