package me.xlgp.xiquzimu.util;

import com.github.promeg.pinyinhelper.Pinyin;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.xlgp.xiquzimu.EmojiManager;
import me.xlgp.xiquzimu.constant.LycConstant;
import me.xlgp.xiquzimu.model.ChangCi;
import me.xlgp.xiquzimu.model.ChangCiList;
import me.xlgp.xiquzimu.model.ChangDuan;
import me.xlgp.xiquzimu.model.ChangDuanInfo;
import me.xlgp.xiquzimu.params.LeftChangDuan;
import me.xlgp.xiquzimu.params.TagChangDuan;

public class ChangDuanHelper {

    public static StringBuilder copyFromChangDuanInfo(ChangDuanInfo changDuanInfo) {

        ChangDuan changDuan = changDuanInfo.getChangDuan();
        StringBuilder text = new StringBuilder();
        text.append("[ti:").append(changDuan.getName()).append("]").append("\n");
        text.append("[jz:").append(changDuan.getJuZhong()).append("]").append("\n");
        text.append("[jm:").append(changDuan.getJuMu()).append("]").append("\n");
        text.append("[offset:").append(changDuan.getOffset()).append("]").append("\n");

        ChangCiList changCiList = changDuanInfo.getChangeCiList();

        for (ChangCi changci : changCiList) {
            text.append("[").append(changci.getShowTime()).append("]").append(changci.getContent()).append("\n");
        }

        return text;
    }

    public static StringBuilder copyChunJingFromChangDuanInfo(ChangDuanInfo changDuanInfo) {

        ChangDuan changDuan = changDuanInfo.getChangDuan();
        StringBuilder text = new StringBuilder();
        text.append(changDuan.getName()).append("\n");
        text.append(changDuan.getJuZhong()).append("\n");
        text.append(changDuan.getJuMu()).append("\n");

        ChangCiList changCiList = changDuanInfo.getChangeCiList();

        for (ChangCi changci : changCiList) {
            text.append(changci.getContent()).append("\n");
        }

        return text;

    }

    public static List<LeftChangDuan> setLeftChangDuan(List<ChangDuan> list) {
        List<LeftChangDuan> leftchangDuanList = new ArrayList<>();
        String name = "";
        for (int i = 0; i < list.size(); i++) {
            ChangDuan changDuan = list.get(i);
            if (!name.equals(changDuan.getJuMu())) {
                LeftChangDuan leftchangDuan = new LeftChangDuan();
                leftchangDuan.setName(changDuan.getJuMu());
                leftchangDuan.setId(i);
                name = changDuan.getJuMu();
                leftchangDuanList.add(leftchangDuan);
            }
        }
        return leftchangDuanList;
    }

    public static List<TagChangDuan> getTagChangDuan(List<ChangDuan> list) {
        List<TagChangDuan> tagchangDuanList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            ChangDuan changDuan = list.get(i);
            TagChangDuan tagChangDuan = new TagChangDuan();
            tagChangDuan.setId(changDuan.getId());
            tagChangDuan.setName(changDuan.getName());
            tagChangDuan.setOffset(changDuan.getOffset());
            tagChangDuan.setJuMu(changDuan.getJuMu());
            tagChangDuan.setJuZhong(changDuan.getJuZhong());
            tagChangDuan.setTag(String.valueOf(Pinyin.toPinyin(changDuan.getJuMu().charAt(0)).charAt(0)));
            tagChangDuan.setCreateTime(changDuan.getCreateTime());
            tagchangDuanList.add(tagChangDuan);
        }
        return tagchangDuanList;
    }

    /**
     * 添加字幕前后缀
     */
    private static void addBeforeChangCi(ChangDuan changDuan, ChangCiList changCiList) {
        ChangCi preChangCi = new ChangCi();
        preChangCi.setDelayMillis(1000);
        preChangCi.setShowTime(formatDate("00:00.00", 1000));
        preChangCi.setContent(EmojiManager.SMALL_BLUE_DIAMOND + "请欣赏" + EmojiManager.SPARKLES + changDuan.getJuZhong() + "《" + changDuan.getJuMu() + "》" + "选段：" + changDuan.getName());
        changCiList.add(preChangCi);
    }

    private static String formatDate(String strDate, long delayMillis) {
        SimpleDateFormat format = new SimpleDateFormat("mm:ss.SS", Locale.CHINESE);
        try {
            long result = Objects.requireNonNull(format.parse(strDate)).getTime() + delayMillis;
            Date date = new Date();
            date.setTime(result);
            return format.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strDate;
    }

    private static void addAfterChangCi(ChangDuan changDuan, ChangCiList changCiList) {
        ChangCi afterChangCi = new ChangCi();
        long afterDelayMillis = 10000;
        afterChangCi.setDelayMillis(afterDelayMillis);
        afterChangCi.setShowTime(formatDate(changCiList.get(changCiList.size() - 1).getShowTime(), afterDelayMillis));
        afterChangCi.setContent(EmojiManager.SMALL_BLUE_DIAMOND + "本曲出自" + changDuan.getJuZhong() + "《" + changDuan.getJuMu() + "》选段：" + changDuan.getName());
        changCiList.add(afterChangCi);

        ChangCi thankChangCi = new ChangCi();
        long thankDelayMillis = 3000;
        thankChangCi.setDelayMillis(thankDelayMillis);
        thankChangCi.setShowTime(formatDate(changCiList.get(changCiList.size() - 1).getShowTime(), thankDelayMillis));
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

    public static void parseChangCiListByDelayMillis(ChangDuanInfo changDuanInfo) {
        ChangCiList changCiList = changDuanInfo.getChangeCiList();
        long beforeMillis = getDelayMillis("00:00.00");
        long offset = changDuanInfo.getChangDuan().getOffset() * 1000;
        for (int i = 0; i < changCiList.size(); i++) {
            //当前的时间的毫秒
            long curMillis = getDelayMillis(changCiList.get(i).getShowTime());
            //时间间隔 = 本句的时间-前句时间
            long delayMillis = curMillis - beforeMillis;
            if (i == 0 && delayMillis + offset > 0) { //若是第一句，加上时间差值,offset:一般表示唱词先于唱段声音出现
                delayMillis += offset;
            }
            changCiList.get(i).setDelayMillis(delayMillis);
            beforeMillis = curMillis;
        }
    }

    /**
     * 从存储中或网络中获取lrc文件后，格式化为 ChangDuan对象
     *
     * @param list list
     * @return ChangDuanInfo
     */
    public static ChangDuanInfo parse(List<String> list) {
        ChangDuanInfo changDuan = new ChangDuanInfo();
        for (String str : list) {
            String line = str.trim();
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
        parseChangCiListByDelayMillis(changDuan);
        return changDuan;
    }

    private static String getQitaContent(String line, int beginIndex) {
        return line.substring(beginIndex, line.length() - 1).trim();
    }

    private static ChangCi getChangCi(String line) {
        String[] strings = line.trim().split("]");
        ChangCi changCi = new ChangCi();
        changCi.setContent(strings[strings.length - 1].trim());
        changCi.setShowTime(strings[0].substring(1));
        return changCi;
    }

    private static long getDelayMillis(String time) {
        SimpleDateFormat format = new SimpleDateFormat("mm:ss.SSS", Locale.CHINESE);
        try {
            Date date = format.parse(time);
            assert date != null;
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
