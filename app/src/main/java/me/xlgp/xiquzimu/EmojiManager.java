package me.xlgp.xiquzimu;

public class EmojiManager {

    //蓝色正菱形
    public static String SMALL_BLUE_DIAMOND = getEmoji("1F539");

    //三个小星星
    public static String SPARKLES = getEmoji("2728");

    public static String getEmoji(String code) {
        //将当前 code 转换为 16 进制数
        int hex = Integer.parseInt(code, 16);
        //将当前 16 进制数转换成字符数组
        char[] chars = Character.toChars(hex);
        //将当前字符数组转换成 TextView 可加载的 String 字符串
        return new String(chars);
    }
}
