package me.xlgp.douyinzimu.data;

import java.util.List;
import java.util.stream.Collectors;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableSource;
import io.reactivex.rxjava3.functions.Function;
import me.xlgp.douyinzimu.retrofit.RetrofitFactory;

public class FetchGiteeRepository {

    /**
     * 远程获取namelist
     *
     * @return Observable
     */
    public Observable<List<String>> getNameList() {
        GiteeService giteeService = RetrofitFactory.get(GiteeService.class);
        return giteeService.nameList().flatMap((Function<List<String>, ObservableSource<List<String>>>) list -> {
            list = list.stream().filter(s -> s.endsWith(".lrc")).collect(Collectors.toList());
            return Observable.just(list);
        });
    }

    public Observable<List<String>> changDuan(String path) {
        GiteeService giteeService = RetrofitFactory.get(GiteeService.class);
        return giteeService.changDuan(path);
    }
}
