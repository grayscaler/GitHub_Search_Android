package com.james.github_search_android.data.source.remote;

import com.james.github_search_android.BuildConfig;
import com.james.github_search_android.data.User;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class GitHubRemoteDataSource {

    private volatile static GitHubRemoteDataSource INSTANCE;
    private GitHubRemoteDataSourceApi mApi;

    public static GitHubRemoteDataSource getInstance() {
        if (INSTANCE == null) {
            synchronized (GitHubRemoteDataSource.class) {
                if (INSTANCE == null) {
                    INSTANCE = new GitHubRemoteDataSource();
                }
            }
        }
        return INSTANCE;
    }

    private GitHubRemoteDataSource() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(httpClient.build())
                .build();

        mApi = retrofit.create(GitHubRemoteDataSourceApi.class);
    }

    public Observable<User> rxGetUsers(Map<String, String> options) {
        return mApi.rxGetUsers(options);
    }
}
