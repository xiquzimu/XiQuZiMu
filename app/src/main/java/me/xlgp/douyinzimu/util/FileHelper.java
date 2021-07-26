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
import java.util.List;

public class FileHelper {

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
