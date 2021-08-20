package me.xlgp.douyinzimu.service;

import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import me.xlgp.douyinzimu.util.HttpURLConnectionUtil;

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

    public void getNameList(MutableLiveData<List<String>> data) {

        HttpURLConnectionUtil.asyncGet(httpBaseUrl, list -> data.setValue(parseNameList(list)));
    }

    public Disposable getNameList(Consumer<List<String>> consumer) {
        return HttpURLConnectionUtil.asyncGet(httpBaseUrl, list -> consumer.accept(parseNameList(list)));
    }
}
