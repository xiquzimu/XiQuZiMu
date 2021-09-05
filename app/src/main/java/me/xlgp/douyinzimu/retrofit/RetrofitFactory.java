package me.xlgp.douyinzimu.retrofit;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;

public class RetrofitFactory {
    static String baseUrl = "https://gitee.com/xlgp/opera-lyrics/raw/master/";

    public static <T> T get(Class<T> clazz) {
        Retrofit retrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .addConverterFactory(StringListConverterFactory.create())
                .baseUrl(baseUrl).build();
        return retrofit.create(clazz);
    }
}
