package me.xlgp.douyinzimu.data;

import java.util.List;
import java.util.stream.Collectors;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableSource;
import io.reactivex.rxjava3.functions.Function;
import me.xlgp.douyinzimu.retrofit.RetrofitFactory;

public class FetchGithubRepository implements IFetchRemoteRepository{

    IRemoteRepository remoteRepository;

    public FetchGithubRepository(){
        remoteRepository = RetrofitFactory.fromGithub(IGithubRepository.class);
    }

    /**
     * 远程获取namelist
     *
     * @return Observable
     */
    public Observable<List<String>> getNameList() {

        return remoteRepository.nameList().flatMap((Function<List<String>, ObservableSource<List<String>>>) list -> {
            list = list.stream().filter(s -> s.endsWith(".lrc")).collect(Collectors.toList());
            return Observable.just(list);
        });
    }

    public Observable<List<String>> changDuan(String path) {
        return remoteRepository.changDuan(path);
    }
}
