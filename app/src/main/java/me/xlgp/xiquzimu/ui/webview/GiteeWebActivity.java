package me.xlgp.xiquzimu.ui.webview;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebSettings;

import me.xlgp.xiquzimu.databinding.ActivityGiteeWebBinding;
import me.xlgp.xiquzimu.ui.base.BaseToolBarActivity;

public class GiteeWebActivity extends BaseToolBarActivity {

    private ActivityGiteeWebBinding binding;
    private String title;
    private String url;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGiteeWebBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getFromIntent();
        setTitle(title);
        if (url != null) {
            binding.giteeWebView.loadUrl(url);
        }
        WebSettings settings = binding.giteeWebView.getSettings();
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setJavaScriptEnabled(true);


    }

    private void getFromIntent() {
        Intent intent = getIntent();
        if (intent == null) return;
        url = intent.getStringExtra("URL");
        title = intent.getStringExtra("title");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}