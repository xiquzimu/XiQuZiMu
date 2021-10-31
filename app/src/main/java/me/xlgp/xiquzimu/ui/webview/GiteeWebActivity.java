package me.xlgp.xiquzimu.ui.webview;

import android.annotation.SuppressLint;
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

        binding.giteeWebView.loadUrl("https://xlgp.gitee.io/changci.html");

        WebSettings settings = binding.giteeWebView.getSettings();
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setJavaScriptEnabled(true);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}