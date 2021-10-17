package me.xlgp.xiquzimu.retrofit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

class DownloadApkRequestBodyConverter implements Converter<ResponseBody, List<String>> {

    String reg = "/xlgp.*xiqu.*apk";

    Pattern pattern = Pattern.compile(reg);

    @Nullable
    @Override
    public List<String> convert(@NotNull ResponseBody value) {
        List<String> list = new ArrayList<>();
        try {
            Matcher matcher = pattern.matcher(value.string());
            while (matcher.find()) {
                list.add(matcher.group());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }
}

public class DownloadApkConverterFactory extends Converter.Factory {

    public static DownloadApkConverterFactory create() {
        return new DownloadApkConverterFactory();
    }

    @Nullable
    @Override
    public Converter<ResponseBody, List<String>> responseBodyConverter(@NonNull Type type, @NonNull Annotation[] annotations, @NonNull Retrofit retrofit) {
        return new DownloadApkRequestBodyConverter();
    }
}
