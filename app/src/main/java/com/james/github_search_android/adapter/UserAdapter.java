package com.james.github_search_android.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.james.github_search_android.R;
import com.james.github_search_android.data.User;
import com.james.github_search_android.paing.UserDiffUtils;

import androidx.annotation.NonNull;
import androidx.core.widget.TextViewCompat;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import static com.james.github_search_android.Constants.Constants.USER_ITEM_TYPE_COUNT;

public class UserAdapter extends PagedListAdapter<User.ItemsBean, RecyclerView.ViewHolder> {

    private Context mContext;

    public UserAdapter(UserDiffUtils userDiffUtils) {
        super(userDiffUtils);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = ViewFactory.createView(mContext, parent, viewType);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        User.ItemsBean user = getItem(position);
        UserViewHolder userViewHolder = (UserViewHolder) holder;

        Glide.with(mContext)
                .load(user.getAvatar_url())
                .apply(new RequestOptions().centerCrop())
                .into(userViewHolder.mAvatar);
        userViewHolder.mName.setText(user.getLogin());
    }

    @Override
    public int getItemViewType(int position) {
        return position % USER_ITEM_TYPE_COUNT;
    }

    private class UserViewHolder extends RecyclerView.ViewHolder {

        ImageView mAvatar;
        TextView mName;

        public UserViewHolder(View view) {
            super(view);
            mAvatar = view.findViewById(R.id.image);
            mName = view.findViewById(R.id.name);
            TextViewCompat.setAutoSizeTextTypeWithDefaults(mName, TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM);
        }
    }
}
