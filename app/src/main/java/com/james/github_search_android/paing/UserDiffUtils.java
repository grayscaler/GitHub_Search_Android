package com.james.github_search_android.paing;

import com.james.github_search_android.data.User;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

public class UserDiffUtils extends DiffUtil.ItemCallback<User.ItemsBean> {

    @Override
    public boolean areItemsTheSame(@NonNull User.ItemsBean oldItem, @NonNull User.ItemsBean newItem) {
        return oldItem.getId() == newItem.getId();
    }

    @Override
    public boolean areContentsTheSame(@NonNull User.ItemsBean oldItem, @NonNull User.ItemsBean newItem) {
        return oldItem.equals(newItem);
    }
}
