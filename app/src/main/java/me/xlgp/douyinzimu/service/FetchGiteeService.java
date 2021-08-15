package me.xlgp.douyinzimu.service;

import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

import me.xlgp.douyinzimu.util.HttpURLConnectionUtil;

public class FetchGiteeService {

    public void getNameList(MutableLiveData<List<String>> data) {
        String httpBaseUrl = "https://gitee.com/xlgp/opera-lyrics/raw/master/name.list";
        HttpURLConnectionUtil.asyncGet(httpBaseUrl, list -> {
            List<String> nameList = new ArrayList<>(list.size());
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).trim().endsWith(".lrc")) {
                    nameList.add(list.get(i));
                }
            }
            data.setValue(nameList);
        });
    }
}
