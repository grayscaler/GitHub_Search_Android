package com.james.github_search_android.home;

import com.james.github_search_android.data.User;
import com.james.github_search_android.data.source.GitHubDataSource;
import com.james.github_search_android.data.source.GitHubRepository;

import java.util.List;

public class HomePresenter implements HomeContract.Presenter {

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
        mGitHubRepository.getUsers("jack", 1, new GitHubDataSource.GetUsersCallback() {
            @Override
            public void onUsersLoaded(List<User.ItemsBean> users) {
                mView.showUsers(users);
            }

            @Override
            public void onDataNotAvailable(Throwable throwable) {

            }
        });
    }
}
