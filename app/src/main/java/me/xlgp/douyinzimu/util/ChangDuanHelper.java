package me.xlgp.douyinzimu.util;

import android.content.Context;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableTransformer;
import io.reactivex.rxjava3.schedulers.Schedulers;
import me.xlgp.douyinzimu.constant.LycConstant;
import me.xlgp.douyinzimu.obj.changduan.ChangCi;
import me.xlgp.douyinzimu.obj.changduan.ChangCiList;
import me.xlgp.douyinzimu.obj.changduan.ChangDuan;

public class ChangDuanHelper {
    public static ObservableTransformer<List<ChangDuan>, List<ChangDuan>> transformer() {
        return upstream -> upstream.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Observable<List<ChangDuan>> getChangDuanList(Context context) {
        return Observable.just("").map(s -> {
            //todo 唱段与唱词或许应该分开读取，现在是将文件名与文件内容同时扫描
            List<File> fileList = loadFileList(context);
            List<ChangDuan> changDuanList = new ArrayList<>();
            for (File file : Objects.requireNonNull(fileList)) {
                changDuanList.add(parse(FileHelper.readFile(file.getAbsolutePath())));
            }
            return changDuanList;
        }).compose(transformer());
    }

    /**
     * 从存储中加载唱段名称
     *
     * @param context
     * @return
     */
    public static List<File> loadFileList(Context context) {
        Path path = Paths.get(FileHelper.getExternalFileRootDir(context.getExternalFilesDir(null)), context.getPackageName());
        List<File> fileList = new ArrayList<>();
        FileHelper.getFileList(path.toString(), fileList);
        return fileList;
    }

    /**
     * 从存储中或网络中获取lrc文件后，格式化为 ChangDuan对象
     *
     * @param list
     * @return
     */
    public static ChangDuan parse(List<String> list) {
        ChangDuan changDuan = new ChangDuan();
        for (String line : list) {
            if (matcher(line, LycConstant.TIME_REG)) { //唱词
                changDuan.getChangeCiList().add(getChangCi(line));
            } else if (matcher(line, LycConstant.TITLE_REG)) { //名称
                changDuan.getChangeDuanQiTa().setTitle(getQitaContent(line, LycConstant.TITLE_START));
            } else if (matcher(line, LycConstant.JUMU_REG)) {
                changDuan.getChangeDuanQiTa().setJuMu(getQitaContent(line, LycConstant.JUMU_START));
            } else if (matcher(line, LycConstant.OFFSET_REG)) {
                String offset = getQitaContent(line, LycConstant.OFFSET_START);
                changDuan.getChangeDuanQiTa().setOffset((Long.parseLong(offset) * 1000));
            } else if (matcher(line, LycConstant.JUZHONG_REG)) {
                changDuan.getChangeDuanQiTa().setJuZhong(getQitaContent(line, LycConstant.JUZHONG_START));
            }
        }
        ChangCiList changCiList = changDuan.getChangeCiList();
        for (int i = 0; i < changCiList.size(); i++) {
            if (i != changCiList.size() - 1) {
                //时间间隔 = 后一句的时间-本句时间+时间补偿值（offset）,offset:一般表示唱词先于唱段声音出现
                long delaymillis = getDelayMillis(changCiList.get(i + 1).getTime()) - getDelayMillis(changCiList.get(i).getTime()) + changDuan.getChangeDuanQiTa().getOffset();
                changCiList.get(i).setDelayMillis(delaymillis);
            }
        }
        return changDuan;
    }

    private static String getQitaContent(String line, int beginIndex) {
        return line.substring(beginIndex, line.length() - 1).trim();
    }

    private static ChangCi getChangCi(String line) {
        String[] strings = line.trim().split("]");
        ChangCi changCi = new ChangCi(strings[strings.length - 1].trim(), strings[0].substring(1, strings[0].length() - 1));
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
