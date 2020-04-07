package com.james.github_search_android.home;

import com.james.github_search_android.BasePresenter;
import com.james.github_search_android.BaseView;
import com.james.github_search_android.data.User;

import java.util.List;

import androidx.paging.PagedList;

public interface HomeContract {

    interface Presenter extends BasePresenter {

    }

    interface View extends BaseView<Presenter> {

        void showUsers(PagedList<User.ItemsBean> users);
    }
}
