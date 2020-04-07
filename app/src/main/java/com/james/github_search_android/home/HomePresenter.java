package com.james.github_search_android.home;

import com.james.github_search_android.data.User;
import com.james.github_search_android.data.source.GitHubRepository;

import androidx.paging.PagedList;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class HomePresenter implements HomeContract.Presenter {

    private final HomeContract.View mView;
    private final GitHubRepository mGitHubRepository;
    private final CompositeDisposable mDisposable = new CompositeDisposable();

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
        addDisposable(mGitHubRepository.getUsersObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<PagedList<User.ItemsBean>>() {
                    @Override
                    public void accept(PagedList<User.ItemsBean> itemsBeans) throws Exception {
                        mView.showUsers(itemsBeans);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                    }
                }));
    }

    private void addDisposable(Disposable disposable) {
        mDisposable.add(disposable);
    }

    @Override
    public void clearDisposable() {
        mDisposable.clear();
    }
}
