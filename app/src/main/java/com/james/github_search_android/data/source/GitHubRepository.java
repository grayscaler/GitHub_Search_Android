package com.james.github_search_android.data.source;


import com.james.github_search_android.data.User;
import com.james.github_search_android.data.source.remote.GitHubRemoteDataSource;
import com.james.github_search_android.paing.UserPagingDataSourceFactory;

import androidx.paging.PagedList;
import androidx.paging.RxPagedListBuilder;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.james.github_search_android.Constants.Constants.API_PAGE_SIZE;
import static com.james.github_search_android.Constants.Constants.API_PREFETCH_DISTANCE;

public class GitHubRepository implements GitHubDataSource {

    private volatile static GitHubRepository INSTANCE = null;
    private final UserPagingDataSourceFactory mUserPagingDataSourceFactory;
    private final PagedList.Config pagedListConfig;

    private GitHubRepository(UserPagingDataSourceFactory userPagingDataSourceFactory) {
        mUserPagingDataSourceFactory = userPagingDataSourceFactory;

        pagedListConfig = new PagedList.Config.Builder()
                .setPageSize(API_PAGE_SIZE)
                .setPrefetchDistance(API_PREFETCH_DISTANCE)
                .build();
    }

    public static GitHubRepository getInstance(UserPagingDataSourceFactory userPagingDataSourceFactory) {
        if (INSTANCE == null) {
            synchronized (GitHubRemoteDataSource.class) {
                if (INSTANCE == null) {
                    INSTANCE = new GitHubRepository(userPagingDataSourceFactory);
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public Observable<PagedList<User.ItemsBean>> getUsersObservable(String userName) {
        mUserPagingDataSourceFactory.setKeyWord(userName);
        return new RxPagedListBuilder(mUserPagingDataSourceFactory, pagedListConfig)
                .setFetchScheduler(Schedulers.io())
                .setNotifyScheduler(AndroidSchedulers.mainThread())
                .buildObservable();
    }
}
