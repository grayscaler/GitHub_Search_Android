package com.james.github_search_android.di.component;

import com.james.github_search_android.di.module.GitHubRepositoryModule;
import com.james.github_search_android.di.module.HomePresenterModule;
import com.james.github_search_android.home.HomeActivity;
import com.james.github_search_android.home.HomeContract;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;

@Singleton
@Component(modules = {HomePresenterModule.class, GitHubRepositoryModule.class})
public interface HomeComponent {

    void inject(HomeActivity activity);

    @Component.Builder
    interface Builder {
        HomeComponent build();

        @BindsInstance
        Builder view(HomeContract.View view);
    }
}
