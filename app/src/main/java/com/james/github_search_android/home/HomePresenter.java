package com.james.github_search_android.home;

import android.util.Log;

import com.james.github_search_android.data.User;
import com.james.github_search_android.data.source.GitHubRepository;

import androidx.paging.PagedList;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class HomePresenter implements HomeContract.Presenter {

    public static final String TAG = HomePresenter.class.getSimpleName();

    private final HomeContract.View mView;
    private final GitHubRepository mGitHubRepository;

    HomePresenter(HomeContract.View view, GitHubRepository gitHubRepository) {
        mView = view;
        mGitHubRepository = gitHubRepository;

        mView.setPresenter(this);
    }

    @Override
    public void start() {
        loadUsers();
    }

    private void loadUsers() {
        mGitHubRepository.getUsersObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<PagedList<User.ItemsBean>>() {
                    @Override
                    public void accept(PagedList<User.ItemsBean> itemsBeans) throws Exception {
                        Log.d(TAG, "accept: ");
                        mView.showUsers(itemsBeans);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.d(TAG, "accept: throwable:" + throwable);
                    }
                }).dispose();
    }
}
