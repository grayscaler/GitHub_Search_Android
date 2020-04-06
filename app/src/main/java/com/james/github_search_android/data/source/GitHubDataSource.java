package com.james.github_search_android.data.source;

import com.james.github_search_android.data.User;

import java.util.List;

public interface GitHubDataSource {

    interface GetUsersCallback {

        void onUsersLoaded(List<User.ItemsBean> users);

        void onDataNotAvailable(Throwable throwable);
    }

    void getUsers(String keyWord, int page, GetUsersCallback getUsersCallback);
}
