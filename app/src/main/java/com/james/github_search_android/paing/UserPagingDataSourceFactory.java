package com.james.github_search_android.paing;

import com.james.github_search_android.data.User;

import androidx.paging.DataSource;

public class UserPagingDataSourceFactory extends DataSource.Factory<String, User.ItemsBean> {

    private String mKeyWord;

    @Override
    public DataSource<String, User.ItemsBean> create() {
        return new UserPagingDataSource(mKeyWord);
    }

    public void setKeyWord(String keyWord) {
        this.mKeyWord = keyWord;
    }
}
