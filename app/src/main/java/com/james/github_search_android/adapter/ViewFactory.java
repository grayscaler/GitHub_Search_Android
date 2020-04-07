package com.james.github_search_android.adapter;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.james.github_search_android.R;
import com.james.github_search_android.util.ScreenUtils;

import static com.james.github_search_android.Constants.Constants.USER_ITEM_TYPE_0;
import static com.james.github_search_android.Constants.Constants.USER_ITEM_TYPE_1;
import static com.james.github_search_android.Constants.Constants.USER_ITEM_TYPE_2;
import static com.james.github_search_android.Constants.Constants.USER_ITEM_TYPE_3;

class ViewFactory {
    static View createView(Context context, ViewGroup parent, int viewType) {
        View view;
        if (viewType == USER_ITEM_TYPE_0) {
            view = new UserViewType0(context, parent).getView();
        } else if (viewType == USER_ITEM_TYPE_1) {
            view = new UserViewType1(context, parent).getView();
        } else if (viewType == USER_ITEM_TYPE_2) {
            view = new UserViewType2(context, parent).getView();
        } else if (viewType == USER_ITEM_TYPE_3) {
            view = new UserViewType3(context, parent).getView();
        } else {
            view = new UserViewType2(context, parent).getView();
        }
        return view;
    }
}

class UserView {
    ViewGroup parent;
    final LayoutInflater layoutInflater;
    final int margin;
    final int deviceWidth;
    final int deviceHeight;
    View view;

    UserView(Context context, ViewGroup parent) {
        layoutInflater = LayoutInflater.from(context);
        DisplayMetrics displaymetrics = ScreenUtils.getDisplayMetrics(context);
        margin = (int) ScreenUtils.pxFromDp(context, 5);
        deviceWidth = (displaymetrics.widthPixels - 6 * margin) / 2;
        deviceHeight = (displaymetrics.widthPixels - 6 * margin) / 2;
    }

    View getView() {
        return view;
    }
}

class UserViewType0 extends UserView {

    UserViewType0(Context context, ViewGroup parent) {
        super(context, parent);
        view = layoutInflater.inflate(R.layout.user_item_one, parent, false);
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.width = deviceWidth;
        params.height = deviceHeight;
        ((ViewGroup.MarginLayoutParams) params).setMargins(margin * 2, 0, margin, margin * 2);
        view.setLayoutParams(params);
    }
}

class UserViewType1 extends UserView {

    UserViewType1(Context context, ViewGroup parent) {
        super(context, parent);
        view = layoutInflater.inflate(R.layout.user_item_one, parent, false);
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.width = deviceWidth;
        params.height = deviceHeight;
        ((ViewGroup.MarginLayoutParams) params).setMargins(margin, 0, margin * 2, margin * 2);
        view.setLayoutParams(params);
    }
}

class UserViewType2 extends UserView {

    UserViewType2(Context context, ViewGroup parent) {
        super(context, parent);
        view = layoutInflater.inflate(R.layout.user_item_two, parent, false);
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.height = deviceHeight;
        ((ViewGroup.MarginLayoutParams) params).setMargins(margin * 2, 0, margin * 2, margin * 2);
        view.setLayoutParams(params);
    }
}

class UserViewType3 extends UserView {

    UserViewType3(Context context, ViewGroup parent) {
        super(context, parent);
        view = layoutInflater.inflate(R.layout.user_item_three, parent, false);
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.height = deviceHeight * 2;
        ((ViewGroup.MarginLayoutParams) params).setMargins(margin * 2, 0, margin * 2, margin * 2);
        view.setLayoutParams(params);
    }
}