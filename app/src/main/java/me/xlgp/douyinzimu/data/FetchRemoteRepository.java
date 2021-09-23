package me.xlgp.douyinzimu.data;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;

public class FetchRemoteRepository implements IFetchRemoteRepository {

    private final IFetchRemoteRepository fetchRemoteRepository;

    public FetchRemoteRepository() {

        fetchRemoteRepository = new FetchGiteeRepository();
    }

    @Override
    public Observable<List<String>> getNameList() {
        return fetchRemoteRepository.getNameList();
    }

    @Override
    public Observable<List<String>> changDuan(String path) {
        return fetchRemoteRepository.changDuan(path);
    }
}
