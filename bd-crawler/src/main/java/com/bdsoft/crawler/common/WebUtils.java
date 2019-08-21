package com.bdsoft.crawler.common;

import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;

/**
 * web工具类
 */
public class WebUtils {

    /**
     * 打开浏览器
     *
     * @param url 指定网址
     */
    public static void openBrowser(String url) {
        try {
            String osName = System.getProperty("os.name", "");
            if (osName.startsWith("Mac OS")) {
                Class<?> fileMgr = Class.forName("com.apple.eio.FileManager");
                Method openURL = fileMgr.getDeclaredMethod("openURL",
                        new Class[]{String.class});
                openURL.invoke(null, new Object[]{url});
            } else if (osName.startsWith("Windows")) {
                Runtime.getRuntime().exec(
                        "rundll32 url.dll,FileProtocolHandler " + url);
            } else {
                String[] browsers = {"firefox", "opera", "konqueror", "epiphany",
                        "mozilla", "netscape"};
                String browser = null;
                for (int count = 0; count < browsers.length && browser == null; count++) {
                    if (Runtime.getRuntime()
                            .exec(new String[]{"which", browsers[count]}).waitFor() == 0) {
                        browser = browsers[count];
                    }
                }
                if (browser == null) {
                    throw new NoSuchMethodException("Could not find web browser");
                } else {
                    Runtime.getRuntime().exec(new String[]{browser, url});
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 提取url参数
     *
     * @param url  带参数的链接
     * @param name 参数名称
     * @return
     */
    public static String getUrlParam(String url, String name) {
        if (StringUtils.isEmpty(url)) {
            throw new RuntimeException("url无效");
        }

        String paramFlag = "?";
        if (url.indexOf(paramFlag) > 0) {
            Optional<String> param = Arrays.stream(url.split("\\?")[1].split("&"))
                    .filter(p -> p.split("=")[0].equals(name))
                    .findFirst();
            if (param.isPresent()) {
                return param.get().split("=")[1];
            }
        }
        throw new RuntimeException("参数[" + name + "]不存在");
    }

}
