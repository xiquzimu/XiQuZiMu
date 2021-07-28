package me.xlgp.douyinzimu.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import me.xlgp.douyinzimu.R;
import me.xlgp.douyinzimu.listener.FloatingMoveListener;
import me.xlgp.douyinzimu.service.FloatingService;
import me.xlgp.douyinzimu.util.ChangDuanHelper;

@SuppressLint("ViewConstructor")
public class ZimuFloatinglayout extends View {
    private View rootLayout = null;
    private WindowManager.LayoutParams layoutParams;
    RecyclerView recyclerView = null;
    private boolean isShou = false;

    public ZimuFloatinglayout(Context context, WindowManager.LayoutParams layoutParams) {
        super(context);
        rootLayout = getFloatingLayout(R.layout.floating_zimu_layout);
        this.layoutParams = layoutParams;
        initRecyclerView();
        onListener();
    }


    private void onListener() {
        this.rootLayout.findViewById(R.id.zimuListTitleBtn).setOnTouchListener(new FloatingMoveListener(rootLayout, layoutParams, (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE)));
        this.rootLayout.findViewById(R.id.closeFloatingBtn).setOnClickListener(v -> {
            FloatingService service = (FloatingService) getContext();
            service.closeFloatingWindow(rootLayout);
        });
        this.rootLayout.findViewById(R.id.kaiOrShouBtn).setOnClickListener(v -> {

            if (isShou) {
                layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
                isShou = false;
            } else {
                layoutParams.height = 160;
                isShou = true;
            }
            ((WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE)).updateViewLayout(rootLayout, layoutParams);
        });
    }

    private View getFloatingLayout(int resource) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return inflater.inflate(resource, null);
    }

    public View getFloatingLayout() {
        return rootLayout;
    }

    private void initRecyclerView() {
        recyclerView = this.rootLayout.findViewById(R.id.zimu_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        TextView textView = this.rootLayout.findViewById(R.id.currentZimuTitleTextView);
        recyclerView.setAdapter(new ChangDuanAdapter(ChangDuanHelper.getChangDuanList(getContext()), textView));
    }
}
