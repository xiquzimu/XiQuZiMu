package me.xlgp.douyinzimu.data;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import me.xlgp.douyinzimu.config.FetchRepositoryConfig;

public class FetchRemoteRepository implements IFetchRemoteRepository {

    private final IFetchRemoteRepository fetchRemoteRepository;

    public FetchRemoteRepository() {
        FetchRepositoryConfig.REPOSITORY_ENUM repositoryEnum = FetchRepositoryConfig.getRepositoryType();
        if (repositoryEnum == FetchRepositoryConfig.REPOSITORY_ENUM.GITEE) {
            fetchRemoteRepository = new FetchGiteeRepository();
        } else {
            fetchRemoteRepository = new FetchGithubRepository();
        }
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
