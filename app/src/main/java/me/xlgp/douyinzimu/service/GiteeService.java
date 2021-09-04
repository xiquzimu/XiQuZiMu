package me.xlgp.douyinzimu.service;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import io.reactivex.rxjava3.core.Observable;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

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

class StringListConverterFactory extends Converter.Factory {

    @Nullable
    @Override
    public Converter<ResponseBody, List<String>> responseBodyConverter(@NonNull Type type, @NonNull Annotation[] annotations, @NonNull Retrofit retrofit) {
        return new StringListRequestBodyConverter();
    }

    public static StringListConverterFactory create() {
        return new StringListConverterFactory();
    }
}

class LiveDataCallAdapter<T> implements CallAdapter<T, LiveData<T>> {
    Type type;

    public LiveDataCallAdapter(Type type) {
        this.type = type;
    }

    @NonNull
    @Override
    public Type responseType() {
        return type;
    }

    @NonNull
    @Override
    public LiveData<T> adapt(@NonNull Call<T> call) {
        return new LiveData<T>() {
            private final AtomicBoolean started = new AtomicBoolean(false);

            @Override
            protected void onActive() {
                if (started.compareAndSet(false, true)) {
                    call.enqueue(new Callback<T>() {
                        @Override
                        public void onResponse(@NonNull Call<T> call, @NonNull Response<T> response) {
                            postValue(response.body());
                        }

                        @Override
                        public void onFailure(@NonNull Call<T> call, @NonNull Throwable t) {

                        }
                    });
                }
            }
        };
    }
}

class LiveDataCallAdapterFactory<T> extends CallAdapter.Factory {

    @Nullable
    @Override
    public CallAdapter<?, ?> get(@NonNull Type returnType, @NonNull Annotation[] annotations, @NonNull Retrofit retrofit) {

        if (getRawType(returnType) != LiveData.class) {
            return null;
        }
        Type observableType = getParameterUpperBound(0, (ParameterizedType) returnType);

        Class<?> rawObservableType = getRawType(observableType);
        if (!(observableType instanceof ParameterizedType)) {
            throw new IllegalStateException("observableType must be ParameterizedType");
        }
        if (rawObservableType != List.class) {
            throw new IllegalArgumentException("type must be list");
        }
        return new LiveDataCallAdapter<T>(observableType);
    }
}

class RetrofitFactory {
    static String baseUrl = "https://gitee.com/xlgp/opera-lyrics/raw/master/";

    public static <T> T get(Class<T> clazz) {
        Retrofit retrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .addConverterFactory(StringListConverterFactory.create())
                .baseUrl(baseUrl).build();
        return retrofit.create(clazz);
    }

    public static <T> T getLiveData(Class<T> tClass) {
        Retrofit retrofit = new Retrofit.Builder().addCallAdapterFactory(new LiveDataCallAdapterFactory<List<String>>()).addConverterFactory(StringListConverterFactory.create()).baseUrl(baseUrl).build();
        return retrofit.create(tClass);
    }
}

public interface GiteeService {

    @GET("name.list")
    Observable<List<String>> nameList();

    @GET("{path}")
    Observable<List<String>> changDuan(@Path("path") String path);
}
