package me.xlgp.xiquzimu.ui.about;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import me.xlgp.xiquzimu.BuildConfig;
import me.xlgp.xiquzimu.R;
import me.xlgp.xiquzimu.databinding.FragmentAboutBinding;

public class AboutFragment extends Fragment {

    private FragmentAboutBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        AboutViewModel viewModel = new ViewModelProvider(this).get(AboutViewModel.class);

        binding = FragmentAboutBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        viewModel.getPackageVersion().observe(getViewLifecycleOwner(), s -> binding.textPackageVersion.setText(s));
        viewModel.errorState.observe(getViewLifecycleOwner(), s -> {
            Snackbar.make(binding.getRoot(), s, Snackbar.LENGTH_SHORT).show();
        });

        viewModel.getDownloadUrlList().observe(getViewLifecycleOwner(), list -> {
            Log.i("TAG", "onCreateView: " + list);
            download(list);
        });

        binding.updateVersion.setOnClickListener(v -> viewModel.loadDownloadUrl());

        return root;
    }

    private boolean checkVersion(String apkName) {
        String[] list = apkName.split("_");
        if (list.length <= 1) {
            return false;
        }
        String version = list[1].substring(1);
        ;
        return BuildConfig.VERSION_NAME.compareTo(version) < 0;
    }

    private void download(List<String> list) {
        DownloadManager mDownloadManager = (DownloadManager) requireActivity().getSystemService(Context.DOWNLOAD_SERVICE);

        String url = list.get(0);
        String apkName = list.get(0).substring(list.get(0).lastIndexOf("/") + 1);

        if (!checkVersion(apkName)) {
            Toast.makeText(requireActivity(), "无版本可更新", Toast.LENGTH_SHORT).show();
            return;
        }

        String mApkDir = "Download/" + apkName;

        Uri mApkUri = Uri.parse("https://gitee.com" + url);
        DownloadManager.Request down = new DownloadManager.Request(mApkUri);

        down.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
        down.setTitle(requireActivity().getString(R.string.app_zh_name));
        down.setDescription("在正下载 " + apkName + " ...");
        down.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        down.setVisibleInDownloadsUi(true);
        down.setDestinationInExternalFilesDir(requireActivity(), Environment.DIRECTORY_DOWNLOADS, mApkDir);

        long mDownloadId = mDownloadManager.enqueue(down);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
