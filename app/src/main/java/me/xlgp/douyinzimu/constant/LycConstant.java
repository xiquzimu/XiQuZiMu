package me.xlgp.douyinzimu.constant;

public class LycConstant {
    //唱段名称
    public static final String TITLE_REG = "^\\[ti:.*\\]$";
    public static final int TITLE_START = 4;
    //时间间隔，其单位是毫秒，正值表示整体提前，负值相反。这是用于总体调整显示快慢的
    public static final String OFFSET_REG = "^\\[offset:\\d*\\]$";
    public static final int OFFSET_START = 8;
    //剧种，包括黄梅戏，越剧，京剧等
    public static final String JUZHONG_REG = "^\\[jz:.*\\]$";
    public static final int JUZHONG_START = 4;

    //剧目，如黄梅戏女驸马，天仙配等
    public static final String JUMU_REG = "^\\[jm:.*\\]$";
    public static final int JUMU_START = 4;
    //每句唱词显示时间，
    public static final String TIME_REG = "^\\[\\d*:\\d*\\.\\d*\\]";
    public static final int TIME_START = 1;
}
