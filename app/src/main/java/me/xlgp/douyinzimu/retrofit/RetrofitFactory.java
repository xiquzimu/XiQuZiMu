package me.xlgp.douyinzimu.retrofit;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;

public class RetrofitFactory {
    static String baseUrl = "https://gitee.com/xlgp/opera-lyrics/raw/master/";

    public static <T> T fromGitee(Class<T> clazz) {
        Retrofit retrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .addConverterFactory(StringListConverterFactory.create())
                .baseUrl(baseUrl).build();
        return retrofit.create(clazz);
    }

    /**
     * github比较慢，使用jsdelivr中转
     * baseUrl = https://cdn.jsdelivr.net/gh/{username}/{repository}@{branch}/{path}/{filename};
     * @param tClass class
     * @param <T> type
     * @return T
     */
    public static <T> T fromGithub(Class<T> tClass){
        String baseUrl = "https://cdn.jsdelivr.net/gh/xlgp/opera-lyrics@master/";
        Retrofit retrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .addConverterFactory(StringListConverterFactory.create())
                .baseUrl(baseUrl).build();
        return retrofit.create(tClass);
    }
}
