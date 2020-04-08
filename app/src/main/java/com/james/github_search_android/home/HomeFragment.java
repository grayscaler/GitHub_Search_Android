package com.james.github_search_android.home;

import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

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

    private HomeContract.Presenter mPresenter;

    private Context mContext;
    private UserAdapter mUserAdapter;
    RecyclerView recyclerView;
    ProgressBar progressBar;

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

        recyclerView = root.findViewById(R.id.recycler_view);
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

        final EditText editText = root.findViewById(R.id.et_input);
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH || keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    hideSoftKeyboard(editText);
                    searchUser(editText.getText().toString());
                }
                return false;
            }
        });

        progressBar = root.findViewById(R.id.progressBar);

        return root;
    }

    private void hideSoftKeyboard(EditText editText) {
        editText.clearFocus();

        if (getActivity() != null) {
            InputMethodManager in = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (in != null) {
                in.hideSoftInputFromWindow(editText.getWindowToken(), 0);
            }
        }
    }

    private void searchUser(String user) {
        mPresenter.clearDisposable();
        mPresenter.detachPageList();
        if (user == null || user.trim().isEmpty()) {
            setRecyclerViewVisibility(View.GONE);
            setProgressBarVisibility(View.GONE);
        } else {
            mPresenter.loadUsers(user);
        }
    }

    @Override
    public void showUsers(PagedList<User.ItemsBean> users) {
        mUserAdapter.submitList(users);
    }

    @Override
    public void setRecyclerViewVisibility(int visibility) {
        recyclerView.setVisibility(visibility);
    }

    @Override
    public void setProgressBarVisibility(int visibility) {
        progressBar.setVisibility(visibility);
    }

    @Override
    public void onPause() {
        super.onPause();
        mPresenter.clearDisposable();
    }
}
