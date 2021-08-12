package me.xlgp.douyinzimu.util;

import android.app.Activity;
import android.content.Context;
import android.os.Environment;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class FileHelper {

    /**
     * 获取dirPath 下所有文本文件
     *
     * @param dirPath
     * @param list
     */
    public static void getFileList(String dirPath, List<File> list) {
        File[] files = new File(dirPath).listFiles();
        if (files == null || files.length == 0) return;
        for (File pathname : files) {
            if (pathname.isDirectory()) {
                getFileList(pathname.getAbsolutePath(), list);
            } else if (pathname.isFile()) {
                list.add(pathname);
            }
        }
    }

    public static String getExternalFileRootDir(File file) {
        File externalFileRootDir = file;
        do {
            externalFileRootDir = Objects.requireNonNull(externalFileRootDir).getParentFile();
        } while (Objects.requireNonNull(externalFileRootDir).getAbsolutePath().contains("/Android"));
        return Objects.requireNonNull(externalFileRootDir).getAbsolutePath();
    }

    private static boolean isStorageMounted(File path) {
        if (path == null) {
            return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        } else {
            return Environment.getExternalStorageState(path).equals(Environment.MEDIA_MOUNTED);
        }

    }

    public static List<String> readFile(String path) {
        BufferedReader br = null;
        List<String> lineList = new ArrayList<>();
        try {
            if (isStorageMounted(new File(path))) {
                br = new BufferedReader(new InputStreamReader(new FileInputStream(path)));
                String line = "";
                while ((line = br.readLine()) != null) {
                    lineList.add(line);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return lineList;
    }

    public static void asyncMakeSourceDir(Activity activity) {
        Observable.create(emitter -> {
            try {
                emitter.onNext(FileHelper.mkdir(getSourceDir(activity)));
            } catch (Exception e) {
            }
        }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread()).subscribe();
    }

    public static boolean mkdir(String dirPath) {
        if (isStorageMounted(null)) {
            File dir = new File(dirPath);
            if (!dir.exists()) {
                if (!dir.mkdir()) {
                    if (!dir.exists()) {
                        return dir.mkdirs();
                    }
                }
            }
            return true;
        }
        return false;
    }

    public static String getSourceDir(Context context) {
        String rootDir = FileHelper.getExternalFileRootDir(context.getExternalFilesDir(null));
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            return Paths.get(rootDir, context.getPackageName()).toString();
        } else {
            return rootDir + "/" + context.getPackageName();
        }
    }

    public static void save(byte[] bytes, String filePath) {
        BufferedOutputStream bos = null;
        try {
            File file = new File(filePath);
            if (isStorageMounted(file)) {
                bos = new BufferedOutputStream(new FileOutputStream(file));
                bos.write(bytes);
                bos.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void save(Context context) {
        FileInputStream fis = null;
        FileOutputStream fos = null;
        try {
            fis = new FileInputStream(context.getDatabasePath("xiqu.db"));
            fos = new FileOutputStream(FileHelper.getSourceDir(context) + "/xiqu.db");
            byte[] bytes = new byte[1024];
            while (fis.read(bytes) != -1) {
                fos.write(bytes);
            }
            fos.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
