package com.james.github_search_android.paing;

import android.util.Log;

import com.google.gson.Gson;
import com.james.github_search_android.data.User;
import com.james.github_search_android.data.source.remote.GitHubRemoteDataSource;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.paging.PageKeyedDataSource;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Response;

import static com.james.github_search_android.Constants.Constants.API_QUERY_KEY_Q;

public class UserPagingDataSource extends PageKeyedDataSource<String, User.ItemsBean> {

    public static final String TAG = UserPagingDataSource.class.getSimpleName();

    private final GitHubRemoteDataSource mGitHubRemoteDataSource;
    private Map<String, String> options = new HashMap<>();
    //    private String mKeyWord;
    private final OkHttpClient mOkHttpClient;

    public UserPagingDataSource(String keyWord) {
        Log.d(TAG, "UserPagingDataSource: ");
        mGitHubRemoteDataSource = GitHubRemoteDataSource.getInstance();
        options.put(API_QUERY_KEY_Q, keyWord);
        //        mKeyWord = keyWord;
        mOkHttpClient = new OkHttpClient();
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<String> params, @NonNull final LoadInitialCallback<String, User.ItemsBean> callback) {
        mGitHubRemoteDataSource.rxGetUsers(options)
//                .subscribeOn(Schedulers.io())
//                .observeOn(Schedulers.io())
                .subscribe(new Consumer<Response<User>>() {
                    @Override
                    public void accept(Response<User> userResponse) throws Exception {
                        if (userResponse.isSuccessful()) {
                            String nextPageUrl = parseNextPageUrl(userResponse.headers());
                            Log.d(TAG, "loadInitial: nextPageUrl:" + nextPageUrl);
                            Log.d(TAG, "loadInitial: response.toString():" + userResponse.toString());
                            Log.d(TAG, "loadInitial: response.body().toString():" + userResponse.body().toString());
                            Log.d(TAG, "loadInitial: response.body().body().toString():" + userResponse.body().toString());
                            List<User.ItemsBean> users = userResponse.body().getItems();
                            callback.onResult(users, null, nextPageUrl);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                }).dispose();
    }

    @Override
    public void loadBefore(@NonNull LoadParams<String> params, @NonNull LoadCallback<String, User.ItemsBean> callback) {

    }

    @Override
    public void loadAfter(@NonNull LoadParams<String> params, @NonNull LoadCallback<String, User.ItemsBean> callback) {
        String url = params.key;
        Log.d(TAG, "loadAfter: url:" + url);
        if (!url.isEmpty()) {
            try {
                okhttp3.Response response = mOkHttpClient.newCall(
                        new Request.Builder()
                                .url(url)
                                .build()
                ).execute();

                if (response.isSuccessful()) {
                    String nextPageUrl = parseNextPageUrl(response.headers());
                    Log.d(TAG, "loadAfter: nextPageUrl:" + nextPageUrl);
                    Log.d(TAG, "loadAfter: response:" + new Gson().toJson(response));
                    Log.d(TAG, "loadAfter: response.body():" + new Gson().toJson(response.body()));
                    Log.d(TAG, "loadAfter: response.body().string():" + response.body().string());
//                    User user = new Gson().fromJson(response.body().string(), User.class);
//                    List<User.ItemsBean> users = user.getItems();
                    callback.onResult(null, nextPageUrl);

//                    Type listType = new TypeToken<List<User.ItemsBean>>() {
//                    }.getType();
//                    List<User.ItemsBean> users = new Gson().fromJson(response.body().string(), listType);
//                    callback.onResult(users, nextPageUrl);
                }
            } catch (IOException e) {

            }
        }
    }

    private String parseNextPageUrl(Headers headers) {
        return new PageLinks(headers).getNext();
    }
}