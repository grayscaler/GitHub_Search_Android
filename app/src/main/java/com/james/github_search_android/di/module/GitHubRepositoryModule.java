package com.james.github_search_android.di.module;

import com.james.github_search_android.paing.UserPagingDataSourceFactory;

import dagger.Module;
import dagger.Provides;

@Module
public class GitHubRepositoryModule {

    @Provides
    UserPagingDataSourceFactory provideUserPagingDataSourceFactory() {
        return new UserPagingDataSourceFactory();
    }
}
