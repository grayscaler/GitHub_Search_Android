package com.james.github_search_android.data.source;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.james.github_search_android.data.User;
import com.james.github_search_android.data.source.remote.GitHubRemoteDataSource;
import com.james.github_search_android.paing.UserPagingDataSourceFactory;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.paging.PagedList;
import io.reactivex.Scheduler;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.internal.schedulers.ExecutorScheduler;
import io.reactivex.plugins.RxJavaPlugins;
import retrofit2.Response;

import static com.james.github_search_android.Constants.Constants.API_PAGE_SIZE;
import static com.james.github_search_android.Constants.Constants.API_QUERY_KEY_PAGE_SIZE;
import static com.james.github_search_android.Constants.Constants.API_QUERY_KEY_Q;

public class GitHubUserTest {

    private GitHubRepository mGitHubRepository;
    private GitHubRemoteDataSource mGitHubRemoteDataSource;

    @Before
    public void setupGitHubRepository() {
        mGitHubRepository = GitHubRepository.getInstance(new UserPagingDataSourceFactory());
        mGitHubRemoteDataSource = GitHubRemoteDataSource.getInstance();
    }

    @BeforeClass
    public static void setUpRxSchedulers() {
        final Scheduler immediate = new Scheduler() {
            @Override
            public Disposable scheduleDirect(@NonNull Runnable run, long delay, @NonNull TimeUnit unit) {
                // this prevents StackOverflowErrors when scheduling with a delay
                return super.scheduleDirect(run, 0, unit);
            }

            @Override
            public Worker createWorker() {
                return new ExecutorScheduler.ExecutorWorker(new Executor() {
                    @Override
                    public void execute(Runnable runnable) {
                        runnable.run();
                    }
                });
            }
        };

        RxJavaPlugins.setInitIoSchedulerHandler(new Function<Callable<Scheduler>, Scheduler>() {
            @Override
            public Scheduler apply(Callable<Scheduler> scheduler) throws Exception {
                return immediate;
            }
        });
        RxJavaPlugins.setInitComputationSchedulerHandler(new Function<Callable<Scheduler>, Scheduler>() {
            @Override
            public Scheduler apply(Callable<Scheduler> scheduler) throws Exception {
                return immediate;
            }
        });
        RxJavaPlugins.setInitNewThreadSchedulerHandler(new Function<Callable<Scheduler>, Scheduler>() {
            @Override
            public Scheduler apply(Callable<Scheduler> scheduler) throws Exception {
                return immediate;
            }
        });
        RxJavaPlugins.setInitSingleSchedulerHandler(new Function<Callable<Scheduler>, Scheduler>() {
            @Override
            public Scheduler apply(Callable<Scheduler> scheduler) throws Exception {
                return immediate;
            }
        });
        RxAndroidPlugins.setInitMainThreadSchedulerHandler(new Function<Callable<Scheduler>, Scheduler>() {
            @Override
            public Scheduler apply(Callable<Scheduler> scheduler) throws Exception {
                return immediate;
            }
        });
    }

    @Test
    public void getUsersObservable() {
        mGitHubRepository.getUsersObservable()
                .subscribe(new Consumer<PagedList<User.ItemsBean>>() {
                    @Override
                    public void accept(PagedList<User.ItemsBean> itemsBeans) throws Exception {
                        Gson gson = new GsonBuilder().setPrettyPrinting().create();
                        System.out.println(gson.toJson(itemsBeans));
                        System.out.println(itemsBeans.size());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        System.out.println(throwable);
                    }
                }).dispose();
    }

    @Test
    public void rxGetUsers() {
        Map<String,String> options = new HashMap<>();
        options.put(API_QUERY_KEY_Q, "jack");
        options.put(API_QUERY_KEY_PAGE_SIZE, String.valueOf(API_PAGE_SIZE));
        mGitHubRemoteDataSource.rxGetUsers(options)
                .subscribe(new Consumer<Response<User>>() {
                    @Override
                    public void accept(Response<User> userResponse) throws Exception {
                        Gson gson = new GsonBuilder().setPrettyPrinting().create();
                        System.out.println(gson.toJson(userResponse));
                        System.out.println(gson.toJson(userResponse.body().getItems().size()));
                    }
                });
    }
}