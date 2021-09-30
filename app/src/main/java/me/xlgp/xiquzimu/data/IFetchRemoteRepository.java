package me.xlgp.xiquzimu.data;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;

public interface IFetchRemoteRepository {
    Observable<List<String>> getNameList();

    Observable<List<String>> changDuan(String path);
}
