package cn.dreamjun.common.utils;

/**
 * @Classname TextUtils
 * @Description TODO
 * @Date 2022/9/10 11:35
 * @Created by 翊
 */
public class TextUtils {

    /**
     * 判断字符串是否为空或者长度为 0
     * @param text 字符串
     * @return
     */
    public static boolean isEmpty(String text) {
        return text == null || text.length() == 0;
    }
}
