package com.james.github_search_android.data.source;


import com.james.github_search_android.data.User;
import com.james.github_search_android.data.source.remote.GitHubRemoteDataSource;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static com.james.github_search_android.Constants.Constants.API_QUERY_KEY_PAGE;
import static com.james.github_search_android.Constants.Constants.API_QUERY_KEY_Q;

public class GitHubRepository implements GitHubDataSource {

    private volatile static GitHubRepository INSTANCE = null;
    private final GitHubRemoteDataSource mGitHubRemoteDataSource;

    private GitHubRepository(GitHubRemoteDataSource gitHubRemoteDataSource) {
        mGitHubRemoteDataSource = gitHubRemoteDataSource;
    }

    public static GitHubRepository getInstance(GitHubRemoteDataSource gitHubRemoteDataSource) {
        if (INSTANCE == null) {
            synchronized (GitHubRemoteDataSource.class) {
                if (INSTANCE == null) {
                    INSTANCE = new GitHubRepository(gitHubRemoteDataSource);
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public void getUsers(String keyWord, int page, GetUsersCallback getUsersCallback) {
        getUsersFromRemoteSource(keyWord, page, getUsersCallback);
    }

    private void getUsersFromRemoteSource(String keyWord, int page, final GetUsersCallback getUsersCallback) {
        Map<String, String> options = new HashMap<>();
        options.put(API_QUERY_KEY_Q, keyWord);
        options.put(API_QUERY_KEY_PAGE, String.valueOf(page));
        mGitHubRemoteDataSource.rxGetUsers(options)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<User>() {
                    @Override
                    public void accept(User user) throws Exception {
                        getUsersCallback.onUsersLoaded(user.getItems());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        getUsersCallback.onDataNotAvailable(throwable);
                    }
                });
    }
}
