package me.xlgp.douyinzimu.view;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.function.Predicate;

import me.xlgp.douyinzimu.R;
import me.xlgp.douyinzimu.adapter.SearchListAdapter;
import me.xlgp.douyinzimu.databinding.SearchRecyclerviewLayoutBinding;

public class SearchRecyclerviewLayout<T> extends LinearLayout {

    private SearchViewModel searchViewModel;
    private SearchListAdapter<T> searchListAdapter;
    private Predicate<T> predicate;
    private SearchRecyclerviewLayoutBinding binding = null;

    public SearchRecyclerviewLayout(Context context) {
        super(context);
        init();
    }

    public SearchRecyclerviewLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SearchRecyclerviewLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public SearchRecyclerviewLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        searchViewModel = new SearchViewModel();

        View view = LayoutInflater.from(getContext()).inflate(R.layout.search_recyclerview_layout, this);

        binding = SearchRecyclerviewLayoutBinding.bind(view);

        binding.searchEditText.addTextChangedListener(new SearchTextWatcher(searchViewModel.getSearchTextLiveData()));

        binding.recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));

    }

    public MutableLiveData<CharSequence> getFilterCharSequenceLiveData() {
        return searchViewModel.getSearchTextLiveData();
    }

    public void build(LifecycleOwner lifecycleOwner) {
        searchViewModel.getSearchTextLiveData().observe(lifecycleOwner, charSequence -> searchListAdapter.filter(predicate));
    }

    public void setRefreshing(boolean refreshing) {
        binding.swipeRefreshLayout.setRefreshing(refreshing);
    }

    public void setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener onRefreshListener) {
        binding.swipeRefreshLayout.setOnRefreshListener(onRefreshListener);
    }

    public void setSearchListAdapter(SearchListAdapter<T> searchListAdapter) {
        this.searchListAdapter = searchListAdapter;
        binding.recyclerview.setAdapter(searchListAdapter);
    }

    /**
     * 过滤规则
     *
     * @param predicate Predicate
     */
    public void setPredicate(Predicate<T> predicate) {
        this.predicate = predicate;
    }

    static class SearchViewModel extends ViewModel {
        MutableLiveData<CharSequence> searchTextLiveData = new MutableLiveData<>();

        public MutableLiveData<CharSequence> getSearchTextLiveData() {
            return searchTextLiveData;
        }
    }

    static class SearchTextWatcher implements TextWatcher {
        MutableLiveData<CharSequence> liveData;

        public SearchTextWatcher(MutableLiveData<CharSequence> liveData) {
            this.liveData = liveData;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            liveData.setValue(s);
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }
}
