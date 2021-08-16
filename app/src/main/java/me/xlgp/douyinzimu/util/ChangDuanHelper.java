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

import me.xlgp.douyinzimu.EmojiManager;
import me.xlgp.douyinzimu.constant.LycConstant;
import me.xlgp.douyinzimu.model.ChangCi;
import me.xlgp.douyinzimu.model.ChangDuan;
import me.xlgp.douyinzimu.obj.changduan.ChangCiList;
import me.xlgp.douyinzimu.obj.changduan.ChangDuanInfo;

public class ChangDuanHelper {

    /**
     * 添加字幕前后缀
     */
    private static void addBeforeChangCi(ChangDuan changDuan, ChangCiList changCiList) {
        ChangCi preChangCi = new ChangCi();
        preChangCi.setContent(EmojiManager.SMALL_BLUE_DIAMOND + "请欣赏" + changDuan.getJuZhong() + "《" + changDuan.getJuMu() + "》" + "选段：" + changDuan.getName());
        preChangCi.setDelayMillis(1000);
        changCiList.add(preChangCi);
    }

    private static void addAfterChangCi(ChangDuan changDuan, ChangCiList changCiList) {

        changCiList.get(changCiList.size() - 1).setDelayMillis(2500);

        ChangCi afterChangCi = new ChangCi();
        afterChangCi.setDelayMillis(1000);
        afterChangCi.setContent(EmojiManager.SMALL_BLUE_DIAMOND + "本曲出自" + changDuan.getJuZhong() + "《" + changDuan.getJuMu() + "》选段：" + changDuan.getName());
        changCiList.add(afterChangCi);

        ChangCi thankChangCi = new ChangCi();
        thankChangCi.setContent(EmojiManager.SMALL_BLUE_DIAMOND + "谢谢各位聆听");
        changCiList.add(thankChangCi);
    }

    public static ChangCiList parseChangCiList(ChangDuan changDuan, List<ChangCi> changCis) {
        ChangCiList changCiList = new ChangCiList();

        addBeforeChangCi(changDuan, changCiList);
        for (int i = 0; i < changCis.size(); i++) {
            changCis.get(i).setContent(EmojiManager.SMALL_BLUE_DIAMOND + changCis.get(i).getContent());
            changCiList.add(changCis.get(i));
        }
        addAfterChangCi(changDuan, changCiList);

        changCiList.setCursor(0);
        return changCiList;
    }

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

    /**
     * 从存储中或网络中获取lrc文件后，格式化为 ChangDuan对象
     *
     * @param list
     * @return
     */
    public static ChangDuanInfo parse(List<String> list) {
        ChangDuanInfo changDuan = new ChangDuanInfo();
        for (String line : list) {
            if (matcher(line, LycConstant.TIME_REG)) { //唱词
                changDuan.getChangeCiList().add(getChangCi(line));
            } else if (matcher(line, LycConstant.TITLE_REG)) { //名称
                changDuan.getChangDuan().setName(getQitaContent(line, LycConstant.TITLE_START));
            } else if (matcher(line, LycConstant.JUMU_REG)) {
                changDuan.getChangDuan().setJuMu(getQitaContent(line, LycConstant.JUMU_START));
            } else if (matcher(line, LycConstant.OFFSET_REG)) {
                String offset = getQitaContent(line, LycConstant.OFFSET_START);
                changDuan.getChangDuan().setOffset(Integer.parseInt(offset) * 1000);
            } else if (matcher(line, LycConstant.JUZHONG_REG)) {
                changDuan.getChangDuan().setJuZhong(getQitaContent(line, LycConstant.JUZHONG_START));
            }
        }
        ChangCiList changCiList = changDuan.getChangeCiList();
        for (int i = 0; i < changCiList.size(); i++) {
            if (i != changCiList.size() - 1) {
                //时间间隔 = 后一句的时间-本句时间+时间补偿值（offset）,offset:一般表示唱词先于唱段声音出现
                long delaymillis = getDelayMillis(changCiList.get(i + 1).getShowTime()) - getDelayMillis(changCiList.get(i).getShowTime()) + changDuan.getChangDuan().getOffset();
                changCiList.get(i).setDelayMillis(delaymillis);
            }
        }
        changCiList.setCursor(0); // 唱词初始化
        return changDuan;
    }

    private static String getQitaContent(String line, int beginIndex) {
        return line.substring(beginIndex, line.length() - 1).trim();
    }

    private static ChangCi getChangCi(String line) {
        String[] strings = line.trim().split("]");
        ChangCi changCi = new ChangCi();
        changCi.setContent(strings[strings.length - 1].trim());
        changCi.setShowTime(strings[0].substring(1, strings[0].length() - 1));
        return changCi;
    }

    private static long getDelayMillis(String time) {
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
