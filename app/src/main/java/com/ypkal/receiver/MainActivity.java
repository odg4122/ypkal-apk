package com.ypkal.receiver;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.POST_NOTIFICATIONS}, 101);
            }
        }

        FirebaseMessaging.getInstance().subscribeToTopic("orders");

        WebView webView = new WebView(this);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.setWebViewClient(new WebViewClient());

        String html = "<html><body style='background:#0f172a;color:#fff;font-family:sans-serif;text-align:center;padding:30px;'>"
                + "<div style='background:#1e293b;padding:25px;border-radius:16px;border:1px solid #334155;margin-top:40px;'>"
                + "<h2>🔔 양평 발주 수신기</h2>"
                + "<p style='color:#4ade80;font-weight:bold;'>✅ 백그라운드 푸시 활성화됨</p>"
                + "<p style='color:#94a3b8;font-size:14px;line-height:1.5;'>이제 앱을 완전히 끄거나 화면을 꺼두셔도<br>발주가 들어오면 상단 바에 알림이 울립니다!</p>"
                + "<hr style='border:0;border-top:1px solid #334155;margin:20px 0;'>"
                + "<div id='box' style='text-align:left;font-size:15px;color:#cbd5e1;'>최근 발주 내역 불러오는 중...</div>"
                + "</div>"
                + "<script>"
                + "function load(){ fetch('https://ypkal-1.onrender.com/api/check-order').then(r=>r.json()).then(d=>{"
                + "if(d.order){ document.getElementById('box').innerHTML = '<b>📅 최근 발주 ('+d.order.orderDate+')</b><br><br>'+d.order.details+'<br><br><b style=\"color:#60a5fa;font-size:18px;\">합계: '+Number(d.order.total).toLocaleString()+'원</b>'; }"
                + "else { document.getElementById('box').innerHTML = '아직 접수된 발주가 없습니다.'; }"
                + "}).catch(e=>{ document.getElementById('box').innerHTML = '서버 연결 대기 중...'; }); }"
                + "load(); setInterval(load, 5000);"
                + "</script></body></html>";

        webView.loadDataWithBaseURL("https://ypkal-1.onrender.com", html, "text/html", "UTF-8", null);
        setContentView(webView);
    }
}
