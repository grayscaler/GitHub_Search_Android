package com.james.github_search_android.data.source;

import com.james.github_search_android.data.User;

import androidx.paging.PagedList;
import io.reactivex.Observable;

public interface GitHubDataSource {

    Observable<PagedList<User.ItemsBean>> getUsersObservable();
}
