package com.bdsoft.crawler.modules.weibo;

/**
 * Created by Administrator on 2019/7/17.
 */
public class WeiboConfig {

    // 字符集
    public static String ZHSHH_CHARSET = "utf-8";

    // 微博类型： is_ori-原创，is_pic-图片，is_video-视频，is_music-音乐，is_article-文章，is_tag=标签

    // 微博列表：0-博主id，1-类型，2-分页码
    public static String WEIBO_LIST = "https://weibo.com/{0}?pids=Pl_Official_MyProfileFeed__21&{1}=1&page={2}&profile_ftype=1&ajaxpagelet=1";


}
