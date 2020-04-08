package com.james.github_search_android.home;

import android.view.View;

import com.james.github_search_android.data.User;
import com.james.github_search_android.data.source.GitHubRepository;

import androidx.paging.PagedList;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class HomePresenter implements HomeContract.Presenter {

    private final HomeContract.View mView;
    private final GitHubRepository mGitHubRepository;
    private final CompositeDisposable mDisposable = new CompositeDisposable();
    private PagedList<User.ItemsBean> pagedList;

    public HomePresenter(HomeContract.View view, GitHubRepository gitHubRepository) {
        mView = view;
        mGitHubRepository = gitHubRepository;

        mView.setPresenter(this);
    }

    @Override
    public void loadUsers(String userName) {
        mGitHubRepository.getUsersObservable(userName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<PagedList<User.ItemsBean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        addDisposable(d);
                        mView.setRecyclerViewVisibility(View.VISIBLE);
                        mView.setProgressBarVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onNext(PagedList<User.ItemsBean> itemsBeans) {
                        pagedList = itemsBeans;
                        mView.showUsers(itemsBeans);
                        mView.setProgressBarVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.setRecyclerViewVisibility(View.GONE);
                        mView.setProgressBarVisibility(View.GONE);
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    private void addDisposable(Disposable disposable) {
        mDisposable.add(disposable);
    }

    @Override
    public void clearDisposable() {
        mDisposable.clear();
    }

    @Override
    public void detachPageList() {
        if (pagedList != null) {
            pagedList.detach();
        }
    }
}
