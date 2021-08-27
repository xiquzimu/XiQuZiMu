package me.xlgp.douyinzimu.ui.main;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.function.Predicate;

import me.xlgp.douyinzimu.R;
import me.xlgp.douyinzimu.adapter.SearchListAdapter;

public class SearchRecyclerviewLayout<T> extends LinearLayout {

    private SearchViewModel searchViewModel;
    private SearchListAdapter<T> searchListAdapter;
    private Predicate<T> predicate;
    private RecyclerView recyclerView;

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

        LayoutInflater.from(getContext()).inflate(R.layout.search_recyclerview_layout, this);
        EditText searchEditText = findViewById(R.id.searchEditText);
        searchEditText.addTextChangedListener(new SearchTextWatcher(searchViewModel.getSearchTextLiveData()));

        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(() -> swipeRefreshLayout.setRefreshing(false));
    }

    public CharSequence getFilterCharSequence() {
        return searchViewModel.getSearchTextLiveData().getValue();
    }

    public void build(LifecycleOwner lifecycleOwner) {
        searchViewModel.getSearchTextLiveData().observe(lifecycleOwner, charSequence -> searchListAdapter.filter(predicate));
    }

    public void setSearchListAdapter(SearchListAdapter<T> searchListAdapter) {
        this.searchListAdapter = searchListAdapter;
        recyclerView.setAdapter(searchListAdapter);
    }


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
