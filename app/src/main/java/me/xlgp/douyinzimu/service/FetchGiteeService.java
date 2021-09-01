package me.xlgp.douyinzimu.service;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.reactivex.rxjava3.functions.Consumer;
import me.xlgp.douyinzimu.util.HttpURLConnectionUtil;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

    private List<String> onNameListResponse(@NonNull Response<ResponseBody> response){
        BufferedReader br = null;
        List<String> list = new ArrayList<>();
        try {
            br = new BufferedReader(Objects.requireNonNull(response.body()).charStream());
            String temp = "";
            while (null != (temp = br.readLine())) {
                if (temp.endsWith(".lrc")) {
                    list.add(temp);
                }
            }
            return list;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    public void getNameList(Consumer<List<String>> consumer) {
        GiteeService giteeService = RetrofitFactory.get(GiteeService.class);
        Call<ResponseBody> bodyCall = giteeService.nameList();

        bodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
               List<String> list = onNameListResponse(response);

            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                t.printStackTrace();
            }
        });
        //        return HttpURLConnectionUtil.asyncGet(httpBaseUrl, list -> consumer.accept(parseNameList(list)));
    }
}
