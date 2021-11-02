package me.xlgp.xiquzimu.ui.webview;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebSettings;

import androidx.appcompat.app.AppCompatActivity;

import me.xlgp.xiquzimu.databinding.ActivityGiteeWebBinding;

public class GiteeWebActivity extends AppCompatActivity {

    ActivityGiteeWebBinding binding;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGiteeWebBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String url = getUrl();
        if (url != null) {
            binding.giteeWebView.loadUrl(url);
        }
        WebSettings settings = binding.giteeWebView.getSettings();
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setJavaScriptEnabled(true);


    }

    private String getUrl() {
        Intent intent = getIntent();
        if (intent == null) return null;
        return intent.getStringExtra("URL");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}