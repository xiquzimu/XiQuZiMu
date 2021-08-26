package me.xlgp.douyinzimu.ui.main;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import me.xlgp.douyinzimu.R;
import me.xlgp.douyinzimu.adapter.BaseAdapter;

public class SearchRecyclerviewLayout extends LinearLayout {

    private SearchViewModel searchViewModel;
    private SearchListAdapter searchListAdapter;

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

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        searchListAdapter = new SearchListAdapter();

        recyclerView.setAdapter(searchListAdapter);
        SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
    }

    public void build(LifecycleOwner lifecycleOwner) {
        searchViewModel.getSearchTextLiveData().observe(lifecycleOwner, searchListAdapter::filter);
    }

    static class SearchViewModel extends ViewModel {
        MutableLiveData<CharSequence> searchTextLiveData = new MutableLiveData<>();

        public MutableLiveData<CharSequence> getSearchTextLiveData() {
            return searchTextLiveData;
        }
    }

    static class SearchListAdapter extends BaseAdapter<String> {
        List<String> filterLsit;
        List<String> sourceList = new ArrayList<>();

        public SearchListAdapter() {
            sourceList.add("222");
            sourceList.add("111");
            sourceList.add("333");
            sourceList.add("45322");
            sourceList.add("2234342");
            sourceList.add("6666");
            sourceList = list;
            filterLsit = list;
            super.updateData(filterLsit);
        }

        public SearchListAdapter(List<String> list) {
            super();
            sourceList = list;
            filterLsit = list;
            super.updateData(filterLsit);
        }

        public void filter(CharSequence charSequence) {
            filterLsit = sourceList.stream().filter(s -> s.contains(charSequence)).collect(Collectors.toList());
            updateData(filterLsit);
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.zimu_item_layout, parent, false);
            return new ViewHolder(view);
        }

        protected static class ViewHolder extends BaseAdapter.ViewHolder<String> {

            Button button = itemView.findViewById(R.id.zimu_item_btn);

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                button.setOnClickListener(v -> {
                    onItemClickListener.onItemClick(itemView, data, getAdapterPosition());
                });
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void setData(String data) {
                super.setData(data);
                button.setText((getAdapterPosition() + 1) + ". " + data);
            }
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
            Log.i("SearchTextWatcher", "onTextChanged: " + s.toString());
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }
}
