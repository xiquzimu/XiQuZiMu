package me.xlgp.douyinzimu.constant;

import java.util.regex.Pattern;

public class LycConstant {
    public static final String TITLE_REG = "^\\[ti:.*\\]$";
    public static final int TITLE_START = 4;
    public static final String OFFSET_REG = "^\\[offset:\\d*\\]$";
    public static final int OFFSET_START = 9;
    public static final String TIME_REG = "^\\[\\d*:\\d*\\.\\d*\\]";
    public static final int TIME_START = 1;
}
