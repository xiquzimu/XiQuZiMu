package me.xlgp.xiquzimu.data;

import java.util.List;
import java.util.stream.Collectors;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableSource;
import io.reactivex.rxjava3.functions.Function;
import me.xlgp.xiquzimu.retrofit.RetrofitFactory;

public class FetchGiteeRepository implements IFetchRemoteRepository{
    /**
     * 远程获取namelist
     *
     * @return Observable
     */
    public Observable<List<String>> getNameList() {
        IGiteeRepository IGiteeRepository = RetrofitFactory.fromGitee(IGiteeRepository.class);
        return IGiteeRepository.nameList().flatMap((Function<List<String>, ObservableSource<List<String>>>) list -> {
            list = list.stream().filter(s -> s.endsWith(".lrc")).collect(Collectors.toList());
            return Observable.just(list);
        });
    }

    public Observable<List<String>> changDuan(String path) {
        IGiteeRepository IGiteeRepository = RetrofitFactory.fromGitee(IGiteeRepository.class);
        return IGiteeRepository.changDuan(path);
    }
}
