package com.bdsoft.crawler.common;

import org.springframework.util.StringUtils;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Random;

/**
 * 通用工具类
 *
 * @author 丁辰叶
 * @version 1.0.0
 * @date 2016-5-6
 */
public class Utils {

    // 操作系统名称
    private static String OS_NAME = null;

    public static String getOsName() {
        if (StringUtils.isEmpty(OS_NAME)) {
            OS_NAME = System.getProperty("os.name").trim().toLowerCase();
        }
        return OS_NAME;
    }

    /**
     * 是否是windows操作系统
     *
     * @return
     */
    public static boolean isWindows() {
        return getOsName().indexOf("windows") != -1;
    }

    /**
     * 是否是mac操作系统
     *
     * @return
     */
    public static boolean isMacos() {
        return getOsName().indexOf("mac") != -1;
    }

    /**
     * 是否是linux操作系统
     *
     * @return
     */
    public static boolean isLinux() {
        return getOsName().indexOf("linux") != -1;
    }

    /**
     * 数组转16进制
     *
     * @param data 字节数组
     * @return
     */
    public static String bytesToHexString(byte[] data) {
        StringBuilder stringBuilder = new StringBuilder();
        if (null == data || data.length <= 0) {
            return null;
        }
        for (int i = 0; i < data.length; i++) {
            int v = data[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    /**
     * 将字符转换成原样整形
     *
     * @param c 待转字符
     * @return 整形
     */
    public static int char2int(char c) {
        String s = Character.toString(c);
        return Integer.parseInt(s);
    }

    /**
     * 生成指定大小的随机数
     *
     * @param num 号码区间
     */
    public static int intRandom(int num) {
        Random random = new Random();
        return random.nextInt(num);
    }

    /**
     * 生成0-9的随机数
     */
    public static int intRandom() {
        Random random = new Random();
        return random.nextInt(9);
    }

    /**
     * 生成几位随机数
     *
     * @param num 位数
     * @return
     */
    public static String random(int num) {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < num; i++) {
            buffer.append(intRandom());
        }
        return buffer.toString();
    }

    /**
     * 人民币转成大写
     *
     * @param value
     * @return String
     */
    public static String hangeToBig(double value) {
        char[] hunit = {'拾', '佰', '仟'}; // 段内位置表示
        char[] vunit = {'万', '亿'}; // 段名表示
        char[] digit = {'零', '壹', '贰', '叁', '肆', '伍', '陆', '柒', '捌', '玖'}; // 数字表示
        long midVal = (long) (value * 100); // 转化成整形
        String valStr = String.valueOf(midVal); // 转化成字符串

        String head = valStr.substring(0, valStr.length() - 2); // 取整数部分
        String rail = valStr.substring(valStr.length() - 2); // 取小数部分

        String prefix = ""; // 整数部分转化的结果
        String suffix = ""; // 小数部分转化的结果
        // 处理小数点后面的数
        if (rail.equals("00")) { // 如果小数部分为0
            suffix = "整";
        } else {
            suffix = digit[rail.charAt(0) - '0'] + "角"
                    + digit[rail.charAt(1) - '0'] + "分"; // 否则把角分转化出来
        }
        // 处理小数点前面的数
        char[] chDig = head.toCharArray(); // 把整数部分转化成字符数组
        char zero = '0'; // 标志'0'表示出现过0
        byte zeroSerNum = 0; // 连续出现0的次数
        for (int i = 0; i < chDig.length; i++) { // 循环处理每个数字
            int idx = (chDig.length - i - 1) % 4; // 取段内位置
            int vidx = (chDig.length - i - 1) / 4; // 取段位置
            if (chDig[i] == '0') { // 如果当前字符是0
                zeroSerNum++; // 连续0次数递增
                if (zero == '0') { // 标志
                    zero = digit[0];
                } else if (idx == 0 && vidx > 0 && zeroSerNum < 4) {
                    prefix += vunit[vidx - 1];
                    zero = '0';
                }
                continue;
            }
            zeroSerNum = 0; // 连续0次数清零
            if (zero != '0') { // 如果标志不为0,则加上,例如万,亿什么的
                prefix += zero;
                zero = '0';
            }
            prefix += digit[chDig[i] - '0']; // 转化该数字表示
            if (idx > 0)
                prefix += hunit[idx - 1];
            if (idx == 0 && vidx > 0) {
                prefix += vunit[vidx - 1]; // 段结束位置应该加上段名如万,亿
            }
        }

        if (prefix.length() > 0)
            prefix += '元'; // 如果整数部分存在,则有圆的字样
        return prefix + suffix; // 返回正确表示
    }

    /**
     * 打开浏览器
     *
     * @param url
     */
    public static void openBrowser(String url) {
        try {
            browse(url);
        } catch (Exception e) {
        }
    }

    private static void browse(String url) throws ClassNotFoundException,
            IllegalAccessException, IllegalArgumentException,
            InterruptedException, InvocationTargetException, IOException,
            NoSuchMethodException {
        String osName = System.getProperty("os.name", "");
        if (osName.startsWith("Mac OS")) {
            Class<?> fileMgr = Class.forName("com.apple.eio.FileManager");
            Method openURL = fileMgr.getDeclaredMethod("openURL",
                    new Class[]{String.class});
            openURL.invoke(null, new Object[]{url});
        } else if (osName.startsWith("Windows")) {
            Runtime.getRuntime().exec(
                    "rundll32 url.dll,FileProtocolHandler " + url);
        } else { // assume Unix or Linux
            String[] browsers = {"firefox", "opera", "konqueror", "epiphany",
                    "mozilla", "netscape"};
            String browser = null;
            for (int count = 0; count < browsers.length && browser == null; count++)
                if (Runtime.getRuntime()
                        .exec(new String[]{"which", browsers[count]}).waitFor() == 0)
                    browser = browsers[count];
            if (browser == null)
                throw new NoSuchMethodException("Could not find web browser");
            else
                Runtime.getRuntime().exec(new String[]{browser, url});
        }
    }

    /**
     * 从字符串中提取数字
     *
     * @param str 字符串
     * @return
     */
    public static int getNumber(String str) {
        String num = str.replaceAll("[^\\d]", "");
        return Integer.parseInt(num);
    }

    public static void main(String[] args) {
        String str = "submitForm('2222');";
        int num = getNumber(str);
        System.out.println(num);
    }

}
