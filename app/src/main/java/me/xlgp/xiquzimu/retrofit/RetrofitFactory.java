package me.xlgp.xiquzimu.retrofit;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;

public class RetrofitFactory {

    public static <T> T get(Class<T> tClass, String baseUrl) {
        Retrofit retrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .addConverterFactory(StringListConverterFactory.create())
                .baseUrl(baseUrl).build();
        return retrofit.create(tClass);
    }

    public static <T> T fromGitee(Class<T> clazz) {
        String baseUrl = "https://gitee.com/xlgp/opera-lyrics/raw/master/";
        return get(clazz, baseUrl);
    }

    /**
     * github比较慢，使用fastgit中转
     * @param tClass class
     * @param <T>    type
     * @return T
     */
    public static <T> T fromGithub(Class<T> tClass) {
        String baseUrl = "https://raw.fastgit.org/xlgp/opera-lyrics/master/";
        return get(tClass, baseUrl);
    }
}
