package me.xlgp.xiquzimu.ui.webview;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.RequiresApi;

import me.xlgp.xiquzimu.databinding.ActivityGiteeWebBinding;
import me.xlgp.xiquzimu.ui.base.BaseToolBarActivity;

public class GiteeWebActivity extends BaseToolBarActivity {

    private ActivityGiteeWebBinding binding;
    private String url;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGiteeWebBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getFromIntent();
        setTitle("网络文档");
        if (url != null) {
            binding.giteeWebView.loadUrl(url);
        }
        binding.giteeWebView.setWebViewClient(new GiteeWebViewClient());
        WebSettings settings = binding.giteeWebView.getSettings();
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setJavaScriptEnabled(true);
    }


    private void getFromIntent() {
        Intent intent = getIntent();
        if (intent == null) return;
        url = intent.getStringExtra("URL");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    class GiteeWebViewClient extends WebViewClient {

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (binding != null) {
                binding.loadingFramelayout.setVisibility(View.GONE);
            }
            setTitle(view.getTitle());
        }
    }
}