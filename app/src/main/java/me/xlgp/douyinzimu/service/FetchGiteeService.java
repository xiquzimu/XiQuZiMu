package me.xlgp.douyinzimu.service;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

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

    public LiveData<List<String>> getNameList() {
        GiteeService giteeService = RetrofitFactory.get(GiteeService.class);
        return giteeService.nameList();
    }

    public LiveData<List<String>> getChangDuan(String path) {
        GiteeService giteeService = RetrofitFactory.get(GiteeService.class);
        return giteeService.changDuan(path.substring(1));
    }
}
