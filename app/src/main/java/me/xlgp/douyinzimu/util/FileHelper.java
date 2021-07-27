package me.xlgp.douyinzimu.util;

import android.os.Environment;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class FileHelper {

    public static HashMap getFileMap(String filePath) {
        HashMap map = new HashMap();
        File[] files = new File(filePath).listFiles();
        if (files.length == 0) return map;
        for (File pathname : files) {
            if (pathname.isDirectory()) {
                map.put(pathname.getName(), getFileMap(pathname.getAbsolutePath()));
            } else if (pathname.isFile()) {
                map.put(pathname.getName(), pathname.getAbsolutePath());
            }
        }
        return map;
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
}
