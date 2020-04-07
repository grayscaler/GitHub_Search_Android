package com.james.github_search_android.home;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.james.github_search_android.R;
import com.james.github_search_android.adapter.UserAdapter;
import com.james.github_search_android.data.User;
import com.james.github_search_android.paing.UserDiffUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.james.github_search_android.Constants.Constants.GRID_LAYOUT_SPAN_COUNT;
import static com.james.github_search_android.Constants.Constants.USER_ITEM_GRID_1;
import static com.james.github_search_android.Constants.Constants.USER_ITEM_GRID_2;
import static com.james.github_search_android.Constants.Constants.USER_ITEM_TYPE_0;
import static com.james.github_search_android.Constants.Constants.USER_ITEM_TYPE_1;
import static com.james.github_search_android.Constants.Constants.USER_ITEM_TYPE_2;
import static com.james.github_search_android.Constants.Constants.USER_ITEM_TYPE_3;

public class HomeFragment extends Fragment implements HomeContract.View {

    public static final String TAG = HomeFragment.class.getSimpleName();

    private HomeContract.Presenter mPresenter;

    private Context mContext;
    private UserAdapter mUserAdapter;

    public HomeFragment() {
    }

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public void setPresenter(HomeContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUserAdapter = new UserAdapter(new UserDiffUtils());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.home_frag, container, false);

        RecyclerView recyclerView = root.findViewById(R.id.recycler_view);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, GRID_LAYOUT_SPAN_COUNT);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch (mUserAdapter.getItemViewType(position)) {
                    case USER_ITEM_TYPE_0:
                    case USER_ITEM_TYPE_1:
                        return USER_ITEM_GRID_1;
                    case USER_ITEM_TYPE_2:
                    case USER_ITEM_TYPE_3:
                    default:
                        return USER_ITEM_GRID_2;
                }
            }
        });
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(mUserAdapter);

        return root;
    }

    @Override
    public void onResume() {
        Log.d(TAG, "onResume: ");
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void showUsers(PagedList<User.ItemsBean> users) {
//        mUserAdapter.setUsers(users);
        mUserAdapter.submitList(users);
    }
}
