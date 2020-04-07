package com.james.github_search_android.paing;

import android.util.Log;

import com.james.github_search_android.data.User;

import androidx.paging.DataSource;

public class UserPagingDataSourceFactory extends DataSource.Factory<String, User.ItemsBean> {

    public static final String TAG = UserPagingDataSourceFactory.class.getSimpleName();

    @Override
    public DataSource<String, User.ItemsBean> create() {
        Log.d(TAG, "create: ");
        return new UserPagingDataSource("jack");
    }
}
