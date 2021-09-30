package me.xlgp.xiquzimu.retrofit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

class StringListRequestBodyConverter implements Converter<ResponseBody, List<String>> {
    @Nullable
    @Override
    public List<String> convert(@NotNull ResponseBody value) {
        BufferedReader br = null;
        List<String> list = new ArrayList<>();
        try {
            br = new BufferedReader(value.charStream());
            String temp;
            while (null != (temp = br.readLine())) {
                list.add(temp);
            }
            return list;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return list;
    }
}

public class StringListConverterFactory extends Converter.Factory {

    public static StringListConverterFactory create() {
        return new StringListConverterFactory();
    }

    @Nullable
    @Override
    public Converter<ResponseBody, List<String>> responseBodyConverter(@NonNull Type type, @NonNull Annotation[] annotations, @NonNull Retrofit retrofit) {
        return new StringListRequestBodyConverter();
    }
}
