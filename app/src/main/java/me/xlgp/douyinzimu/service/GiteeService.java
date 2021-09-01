package me.xlgp.douyinzimu.service;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.concurrent.atomic.AtomicBoolean;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.GET;

class StringListConverterFactory extends Converter.Factory {

    @Nullable
    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(@NonNull Type type, @NonNull Annotation[] annotations, @NonNull Retrofit retrofit) {

        return super.responseBodyConverter(type, annotations, retrofit);
    }

    public static StringListConverterFactory create() {
        return new StringListConverterFactory();
    }
}

class LiveDataCallAdapter<String> implements CallAdapter<String, LiveData<String>> {
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
    public LiveData<String> adapt(@NonNull Call<String> call) {
        return new LiveData<String>() {
            private final AtomicBoolean started = new AtomicBoolean(false);
            @Override
            protected void onActive() {
                if (started.compareAndSet(false,true)){
                    call.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                            postValue(response.body());
                        }

                        @Override
                        public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                            postValue((String) "错误");
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
        if (rawObservableType != String.class) {
            throw new IllegalArgumentException("type must be string");
        }

        if (!(observableType instanceof ParameterizedType)) {
            throw new IllegalStateException("response must be parameterized");
        }
        return new LiveDataCallAdapter<T>(getParameterUpperBound(0, (ParameterizedType) observableType));
    }
}

class RetrofitFactory {
    public static <T> T get(Class<T> clazz) {
        String baseUrl = "https://gitee.com/xlgp/opera-lyrics/raw/master/";
        Retrofit retrofit = new Retrofit.Builder().addConverterFactory(new StringListConverterFactory()).baseUrl(baseUrl).build();
        return retrofit.create(clazz);
    }
}

public interface GiteeService {

    @GET("name.list")
    Call<ResponseBody> nameList();
}
