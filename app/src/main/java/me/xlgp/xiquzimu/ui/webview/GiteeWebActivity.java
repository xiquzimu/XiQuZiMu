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

import me.xlgp.xiquzimu.R;
import me.xlgp.xiquzimu.databinding.ActivityGiteeWebBinding;
import me.xlgp.xiquzimu.ui.base.BaseToolBarActivity;
import me.xlgp.xiquzimu.util.AnimationHelper;

public class GiteeWebActivity extends BaseToolBarActivity {

    private ActivityGiteeWebBinding binding;
    private String url;

    public static final String KEY_URL = "url";

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
            loading(true);
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
        url = intent.getStringExtra(KEY_URL);
    }

    private void loading(boolean loading) {
        if (loading) {
            binding.loadingImageView.setVisibility(View.VISIBLE);
            binding.loadingImageView.startAnimation(AnimationHelper.getRotateAnimation(this, R.anim.anim_circle_rotate));
        } else {
            binding.loadingImageView.clearAnimation();
            binding.loadingImageView.setVisibility(View.GONE);
        }
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
                loading(false);
                binding.loadingFramelayout.setVisibility(View.GONE);
            }
            setTitle(view.getTitle());
        }
    }
}