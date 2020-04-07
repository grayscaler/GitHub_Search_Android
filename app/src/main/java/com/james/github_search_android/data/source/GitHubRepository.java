package com.james.github_search_android.data.source;


import com.james.github_search_android.data.User;
import com.james.github_search_android.data.source.remote.GitHubRemoteDataSource;
import com.james.github_search_android.paing.UserPagingDataSourceFactory;

import androidx.paging.PagedList;
import androidx.paging.RxPagedListBuilder;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class GitHubRepository implements GitHubDataSource {

    private volatile static GitHubRepository INSTANCE = null;
    private final GitHubRemoteDataSource mGitHubRemoteDataSource;
    private final UserPagingDataSourceFactory mUserPagingDataSourceFactory;
    private final PagedList.Config pagedListConfig;

    private GitHubRepository(GitHubRemoteDataSource gitHubRemoteDataSource, UserPagingDataSourceFactory userPagingDataSourceFactory) {
        mGitHubRemoteDataSource = gitHubRemoteDataSource;
        mUserPagingDataSourceFactory = userPagingDataSourceFactory;

        pagedListConfig = new PagedList.Config.Builder()
                .setPageSize(30)
                .setPrefetchDistance(10)
                .build();
    }

    public static GitHubRepository getInstance(GitHubRemoteDataSource gitHubRemoteDataSource, UserPagingDataSourceFactory userPagingDataSourceFactory) {
        if (INSTANCE == null) {
            synchronized (GitHubRemoteDataSource.class) {
                if (INSTANCE == null) {
                    INSTANCE = new GitHubRepository(gitHubRemoteDataSource, userPagingDataSourceFactory);
                }
            }
        }
        return INSTANCE;
    }

//    @Override
//    public void getUserResponse(String keyWord, GetUsersCallback getUsersCallback) {
//        getUsersFromRemoteSource(keyWord, getUsersCallback);
//    }
//
//    private void getUsersFromRemoteSource(String keyWord, final GetUsersCallback getUsersCallback) {
//        Map<String, String> options = new HashMap<>();
//        options.put(API_QUERY_KEY_Q, keyWord);
//        mGitHubRemoteDataSource.rxGetUsers(options)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Consumer<Response<User>>() {
//                    @Override
//                    public void accept(Response<User> userResponse) throws Exception {
//                        getUsersCallback.onUserLoaded(userResponse);
//                    }
//                }, new Consumer<Throwable>() {
//                    @Override
//                    public void accept(Throwable throwable) throws Exception {
//                        getUsersCallback.onDataNotAvailable(throwable);
//                    }
//                }).dispose();
//    }

    @Override
    public Observable<PagedList<User.ItemsBean>> getUsersObservable() {
        return new RxPagedListBuilder(mUserPagingDataSourceFactory, pagedListConfig)
                .setFetchScheduler(Schedulers.io())
                .setNotifyScheduler(AndroidSchedulers.mainThread())
                .buildObservable();
    }
}
