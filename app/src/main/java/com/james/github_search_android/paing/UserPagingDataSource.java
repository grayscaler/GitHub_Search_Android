package com.james.github_search_android.paing;

import com.google.gson.Gson;
import com.james.github_search_android.data.User;
import com.james.github_search_android.data.source.remote.GitHubRemoteDataSource;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.paging.PageKeyedDataSource;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Response;

import static com.james.github_search_android.Constants.Constants.API_PAGE_SIZE;
import static com.james.github_search_android.Constants.Constants.API_QUERY_KEY_PAGE_SIZE;
import static com.james.github_search_android.Constants.Constants.API_QUERY_KEY_Q;

public class UserPagingDataSource extends PageKeyedDataSource<String, User.ItemsBean> {

    private final GitHubRemoteDataSource mGitHubRemoteDataSource;
    private Map<String, String> options;
    private final OkHttpClient mOkHttpClient;
    private final CompositeDisposable mDisposable;

    public UserPagingDataSource(String keyWord) {
        mGitHubRemoteDataSource = GitHubRemoteDataSource.getInstance();

        options = new HashMap<>();
        options.put(API_QUERY_KEY_Q, keyWord);
        options.put(API_QUERY_KEY_PAGE_SIZE, String.valueOf(API_PAGE_SIZE));

        mOkHttpClient = new OkHttpClient();
        mDisposable = new CompositeDisposable();
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<String> params, @NonNull final LoadInitialCallback<String, User.ItemsBean> callback) {
        mDisposable.add(mGitHubRemoteDataSource.rxGetUsers(options)
                .subscribe(new Consumer<Response<User>>() {
                    @Override
                    public void accept(Response<User> userResponse) throws Exception {
                        if (userResponse.isSuccessful()) {
                            String nextPageUrl = parseNextPageUrl(userResponse.headers());
                            List<User.ItemsBean> users = userResponse.body().getItems();
                            callback.onResult(users, null, nextPageUrl);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                }));
    }

    @Override
    public void loadBefore(@NonNull LoadParams<String> params, @NonNull LoadCallback<String, User.ItemsBean> callback) {

    }

    @Override
    public void loadAfter(@NonNull LoadParams<String> params, @NonNull LoadCallback<String, User.ItemsBean> callback) {
        mDisposable.clear();
        String url = params.key;
        if (!url.isEmpty()) {
            try {
                okhttp3.Response response = mOkHttpClient.newCall(
                        new Request.Builder()
                                .url(url)
                                .build()
                ).execute();

                if (response.isSuccessful()) {
                    String nextPageUrl = parseNextPageUrl(response.headers());
                    User user = new Gson().fromJson(response.body().string(), User.class);
                    List<User.ItemsBean> users = user.getItems();
                    callback.onResult(users, nextPageUrl);
                } else {
                    // TODO: 2020-04-07 handle unsuccessful
                }

            } catch (IOException e) {
            }
        }
    }

    private String parseNextPageUrl(Headers headers) {
        return new PageLinks(headers).getNext();
    }
}