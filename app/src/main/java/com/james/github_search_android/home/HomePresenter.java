package com.james.github_search_android.home;

public class HomePresenter implements HomeContract.Presenter {

    private final HomeContract.View mView;

    HomePresenter(HomeContract.View view) {
        mView = view;

        mView.setPresenter(this);
    }

    @Override
    public void start() {
    }
}
