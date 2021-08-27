package me.xlgp.douyinzimu;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import java.util.Objects;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zimu_list);

        SearchRecyclerviewLayout<ChangDuan> searchRecyclerviewLayout = findViewById(R.id.zimu_list_SearchRecyclerviewLayout);
        searchRecyclerviewLayout.build(this);
        searchRecyclerviewLayout.setPredicate(changDuan -> {
            try {
                return Objects.requireNonNull(changDuan.getName()).contains(searchRecyclerviewLayout.getFilterCharSequence());
            } catch (Exception e) {
                return false;
            }

        });

        ChangDuanListAdapter changDuanListAdapter = new ChangDuanListAdapter();
        changDuanListAdapter.setOnItemClickListener((itemView, data, position) -> {
            try {
                new ChangDuanService(compositeDisposable).delete(data, s -> Toast.makeText(ZimuListActivity.this, "删除成功", Toast.LENGTH_SHORT).show());
            } catch (Exception e) {
                Toast.makeText(ZimuListActivity.this, "删除数据失败", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        });
        searchRecyclerviewLayout.setSearchListAdapter(changDuanListAdapter);

        ChangDuanViewModel viewModel = new ViewModelProvider(this).get(ChangDuanViewModel.class);

        new ChangDuanService(compositeDisposable).list(changDuanList -> viewModel.getChangduanList().setValue(changDuanList));

        viewModel.getChangduanList().observe(this, changDuanListAdapter::updateData);
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
}