package me.xlgp.douyinzimu.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableSource;
import io.reactivex.rxjava3.functions.Function;
import me.xlgp.douyinzimu.retrofit.RetrofitFactory;

public class FetchGiteeService {
    private final String httpBaseUrl = "https://gitee.com/xlgp/opera-lyrics/raw/master/name.list";

    private List<String> parseNameList(List<String> list) {
        List<String> nameList = new ArrayList<>(list.size());
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).trim().endsWith(".lrc")) {
                nameList.add(list.get(i));
            }
        }
        return nameList;
    }

    /**
     * 远程获取namelist
     * @return Observable
     */
    public Observable<List<String>> getNameList() {
        GiteeService giteeService = RetrofitFactory.get(GiteeService.class);
        return giteeService.nameList().flatMap((Function<List<String>, ObservableSource<List<String>>>) list -> {
            list = list.stream().filter(s -> s.endsWith(".lrc")).collect(Collectors.toList());
            return Observable.just(list);
        });
    }
}
