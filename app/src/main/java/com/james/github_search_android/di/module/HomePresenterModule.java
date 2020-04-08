package com.james.github_search_android.di.module;

import com.james.github_search_android.data.source.GitHubRepository;
import com.james.github_search_android.home.HomeContract;
import com.james.github_search_android.home.HomePresenter;

import dagger.Module;
import dagger.Provides;

@Module
public class HomePresenterModule {

    @Provides
    HomePresenter provideHomePresenter(HomeContract.View view, GitHubRepository gitHubRepository) {
        return new HomePresenter(view, gitHubRepository);
    }
}
