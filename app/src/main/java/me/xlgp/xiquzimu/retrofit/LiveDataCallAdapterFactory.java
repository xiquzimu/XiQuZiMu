package me.xlgp.xiquzimu.retrofit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

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

public class LiveDataCallAdapterFactory<T> extends CallAdapter.Factory {

    public static <T> LiveDataCallAdapterFactory<T> create() {
        return new LiveDataCallAdapterFactory<>();
    }

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
