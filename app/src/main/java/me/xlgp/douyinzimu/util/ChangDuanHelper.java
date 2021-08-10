package me.xlgp.douyinzimu.util;

import android.content.Context;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.xlgp.douyinzimu.obj.changduan.ChangCi;

public class ChangDuanHelper {



    /**
     * 从存储中加载唱段名称
     *
     * @param context
     * @return
     */
    public static List<File> loadFileList(Context context) {
        List<File> fileList = new ArrayList<>();
        FileHelper.getFileList(FileHelper.getSourceDir(context), fileList);
        return fileList;
    }


    private static String getQitaContent(String line, int beginIndex) {
        return line.substring(beginIndex, line.length() - 1).trim();
    }

    private static ChangCi getChangCi(String line) {
        String[] strings = line.trim().split("]");
        ChangCi changCi = new ChangCi(strings[strings.length - 1].trim(), strings[0].substring(1, strings[0].length() - 1));
        return changCi;
    }

    public static long getDelayMillis(String time) {
        SimpleDateFormat format = new SimpleDateFormat("mm:ss.SSS");
        try {
            Date date = format.parse(time);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private static boolean matcher(String line, String reg) {
        Pattern p = Pattern.compile(reg);
        Matcher matcher = p.matcher(line);
        return matcher.find();
    }
}
