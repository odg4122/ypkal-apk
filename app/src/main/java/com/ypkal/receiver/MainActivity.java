package com.ypkal.receiver;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.core.content.FileProvider;
import java.io.File;
import java.io.FileOutputStream;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WebView webView = new WebView(this);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        
        webView.addJavascriptInterface(new WebAppInterface(this), "AndroidBridge");
        webView.setWebViewClient(new WebViewClient());

        String html = "<!DOCTYPE html><html lang='ko'><head><meta charset='UTF-8'><meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
                "<title>양평칼국수 본점 수신 대시보드</title>" +
                "<script src='https://cdnjs.cloudflare.com/ajax/libs/html2canvas/1.4.1/html2canvas.min.js'></script>" +
                "<style>" +
                "* { box-sizing: border-box; margin: 0; padding: 0; font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif; }" +
                "body { background: #0f172a; color: #f8fafc; padding: 15px; }" +
                ".container { max-width: 600px; margin: 0 auto; background: #1e293b; padding: 20px; border-radius: 16px; border: 1px solid #334155; box-shadow: 0 4px 20px rgba(0,0,0,0.4); }" +
                "h2 { text-align: center; color: #fff; margin-bottom: 5px; font-size: 20px; }" +
                ".subtitle { text-align: center; font-size: 13px; color: #94a3b8; margin-bottom: 20px; }" +
                ".status-bar { background: #0f172a; border: 1px solid #334155; padding: 12px; border-radius: 10px; margin-bottom: 15px; display: flex; justify-content: space-between; align-items: center; font-size: 13px; }" +
                ".tabs { display: flex; background: #0f172a; padding: 4px; border-radius: 12px; margin-bottom: 20px; }" +
                ".tab { flex: 1; text-align: center; padding: 12px; font-size: 14px; font-weight: 600; cursor: pointer; border-radius: 8px; color: #94a3b8; }" +
                ".tab.active { background: #1e293b; color: #60a5fa; box-shadow: 0 2px 8px rgba(0,0,0,0.2); }" +
                ".tab-content { display: none; } .tab-content.active { display: block; }" +
                ".filter-box { display: flex; gap: 6px; overflow-x: auto; padding-bottom: 6px; margin-bottom: 15px; }" +
                ".filter-chip { background: #0f172a; color: #94a3b8; border: 1px solid #334155; padding: 6px 12px; border-radius: 20px; font-size: 13px; cursor: pointer; white-space: nowrap; }" +
                ".filter-chip.active { background: #f59e0b
