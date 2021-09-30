package me.xlgp.xiquzimu.data;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface IRemoteRepository {
    @GET("name.list")
    Observable<List<String>> nameList();

    @GET("{path}")
    Observable<List<String>> changDuan(@Path("path") String path);
}
