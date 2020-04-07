package com.james.github_search_android.data.source;

import com.james.github_search_android.data.User;

import androidx.paging.PagedList;
import io.reactivex.Observable;

public interface GitHubDataSource {

//    interface GetUsersCallback {
//
//        void onUserLoaded(Response<User> userResponse);
//
//        void onDataNotAvailable(Throwable throwable);
//    }

//    void getUserResponse(String keyWord, GetUsersCallback getUsersCallback);

    Observable<PagedList<User.ItemsBean>> getUsersObservable();
}
