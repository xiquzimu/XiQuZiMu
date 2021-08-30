package me.xlgp.douyinzimu;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import java.util.Objects;
import java.util.function.Predicate;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import me.xlgp.douyinzimu.adapter.ChangDuanListAdapter;
import me.xlgp.douyinzimu.model.ChangDuan;
import me.xlgp.douyinzimu.service.ChangCiService;
import me.xlgp.douyinzimu.service.ChangDuanService;
import me.xlgp.douyinzimu.service.FetchGiteeService;
import me.xlgp.douyinzimu.ui.main.SearchRecyclerviewLayout;
import me.xlgp.douyinzimu.viewmodel.ChangDuanViewModel;

public class ZimuListActivity extends AppCompatActivity {

    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private SearchRecyclerviewLayout<ChangDuan> searchRecyclerviewLayout;
    private ChangDuanViewModel viewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zimu_list);
        searchRecyclerviewLayout = findViewById(R.id.zimu_list_SearchRecyclerviewLayout);
        initSearchRecyclerviewLayout();

        ChangDuanListAdapter changDuanListAdapter = new ChangDuanListAdapter();
        searchRecyclerviewLayout.setSearchListAdapter(changDuanListAdapter);

        viewModel= new ViewModelProvider(this).get(ChangDuanViewModel.class);
        viewModel.getChangduanList().observe(this, changDuanListAdapter::updateData);

        loadData();
    }

    private void initSearchRecyclerviewLayout(){

        searchRecyclerviewLayout.build(this);
        searchRecyclerviewLayout.setRefreshing(true);
        searchRecyclerviewLayout.setPredicate(new ChangDuanPredicate(searchRecyclerviewLayout.getFilterCharSequenceLiveData()));
        searchRecyclerviewLayout.setOnRefreshListener(this::loadData);
    }

    private void loadData(){
        new ChangDuanService(compositeDisposable).list(changDuanList -> {
            searchRecyclerviewLayout.setRefreshing(false);
            viewModel.getChangduanList().setValue(changDuanList);
        });
    }

    public void onFetch(View view) {
        new FetchGiteeService().getNameList(new ChangDuanService(compositeDisposable)::updateList);
    }

    public void onClearList(View view) {
        try {
            new ChangDuanService(compositeDisposable).deleteAll();
            Toast.makeText(this, "唱段删除完毕", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "唱段删除异常", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

        try {
            new ChangCiService(compositeDisposable).deleteAll();
            Toast.makeText(this, "唱词删除完毕", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "删除唱词异常", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (compositeDisposable != null) {
            compositeDisposable.clear();
            compositeDisposable = null;
        }
    }

    static class ChangDuanPredicate implements Predicate<ChangDuan>{
        private final MutableLiveData<CharSequence> liveData;

        public ChangDuanPredicate(MutableLiveData<CharSequence> liveData){
            this.liveData = liveData;
        }
        @Override
        public boolean test(ChangDuan changDuan) {
            try {
                return Objects.requireNonNull(changDuan.getName())
                        .contains(Objects.requireNonNull(liveData.getValue()));
            } catch (Exception e) {
                return false;
            }
        }
    }
}