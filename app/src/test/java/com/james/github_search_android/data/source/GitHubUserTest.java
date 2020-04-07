package com.james.github_search_android.data.source;

import android.util.Log;

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

import static com.james.github_search_android.Constants.Constants.API_QUERY_KEY_Q;

public class GitHubUserTest {
    private GitHubRepository mGitHubRepository;

    @Before
    public void setupGitHubRepository() {
        mGitHubRepository = GitHubRepository.getInstance(GitHubRemoteDataSource.getInstance(), new UserPagingDataSourceFactory());
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
    public void getUsersFromRemoteSource() {
//        mGitHubRepository.getUserResponse("jack", 0, new GitHubDataSource.GetUsersCallback() {
//                @Override
//                public void onUserLoaded(List<User.ItemsBean> users) {
//                    Gson gson = new GsonBuilder().setPrettyPrinting().create();
//                    System.out.println(gson.toJson(users));
//                    System.out.println(users.size());
//                }
//
//                @Override
//                public void onDataNotAvailable(Throwable throwable) {
//                    System.out.println(throwable);
//                }
//            });

//        try {
//            String response = mGitHubRepository.getUserResponse("jack").execute().headers().toString();
//            System.out.println(response);
//            String linkPart = mGitHubRepository.getUserResponse("jack").execute()
//                    .headers()
//                    .get("link")
//                    .split(",")[0]
//                    .split(";")[0]
//                    .trim();
//            String nextLink = linkPart.substring(1, linkPart.length() - 1);
//            System.out.println(nextLink);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

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
    public void getUsers() {
        Map<String,String> options = new HashMap<>();
        options.put(API_QUERY_KEY_Q, "jack");
        GitHubRemoteDataSource.getInstance()
                .rxGetUsers(options)
                .subscribe(new Consumer<Response<User>>() {
                    @Override
                    public void accept(Response<User> userResponse) throws Exception {
                        Gson gson = new GsonBuilder().setPrettyPrinting().create();
                        System.out.println(gson.toJson(userResponse));
                    }
                });
    }
}