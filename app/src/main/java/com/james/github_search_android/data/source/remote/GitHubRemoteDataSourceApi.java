package com.james.github_search_android.data.source.remote;

import com.james.github_search_android.data.User;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public interface GitHubRemoteDataSourceApi {

    @GET("search/users")
    Observable<Response<User>> rxGetUsers(@QueryMap Map<String, String> options);
}
