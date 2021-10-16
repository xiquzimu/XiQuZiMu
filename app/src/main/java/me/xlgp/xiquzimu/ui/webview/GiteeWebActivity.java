package me.xlgp.xiquzimu.ui.webview;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import me.xlgp.xiquzimu.databinding.ActivityGiteeWebBinding;

public class GiteeWebActivity extends AppCompatActivity {

    ActivityGiteeWebBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGiteeWebBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.giteeWebView.loadUrl("https://gitee.com/xlgp/XiQuZiMu/releases");

    }
}