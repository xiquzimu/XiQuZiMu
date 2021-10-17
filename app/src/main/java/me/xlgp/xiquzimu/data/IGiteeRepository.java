package me.xlgp.xiquzimu.data;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.GET;

public interface IGiteeRepository extends IRemoteRepository {
    @GET("releases")
    Observable<List<String>> getDownloadUrl();
}
