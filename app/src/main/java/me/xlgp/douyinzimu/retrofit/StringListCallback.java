package me.xlgp.douyinzimu.retrofit;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StringListCallback implements Callback<List<String>> {
    protected List<String> responseList;
    @Override
    public void onResponse(@NotNull Call<List<String>> call, @NotNull Response<List<String>> response) {
        if (response.isSuccessful() && response.body() != null && response.body().size() > 0){
            responseList = response.body().stream().filter(s -> s != null && !"".equals(s)).collect(Collectors.toList());
        }
    }

    @Override
    public void onFailure(@NotNull Call<List<String>> call, @NotNull Throwable t) {

    }
}