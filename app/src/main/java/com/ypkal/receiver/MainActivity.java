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

        // 사장님 실시간 발주 관리 장부 HTML (서버 연동형)
        String html = "<<!DOCTYPE html>" +
                "<html lang='ko'>" +
                "<head>" +
                "<meta charset='UTF-8'>" +
                "<meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
                "<title>양평칼국수 통합 발주 장부</title>" +
                "<style>" +
                ":root { --bg-color: #0f172a; --card-bg: #1e293b; --border-color: #334155; --text-main: #f8fafc; --text-sub: #94a3b8; --primary: #3b82f6; --success: #22c55e; --accent: #f59e0b; }" +
                "* { box-sizing: border-box; margin: 0; padding: 0; font-family: sans-serif; }" +
                "body { background-color: var(--bg-color); color: var(--text-main); padding: 16px; }" +
                ".container { max-width: 600px; margin: 0 auto; }" +
                ".tabs { display: flex; background: var(--card-bg); border-radius: 12px; padding: 4px; margin-bottom: 20px; border: 1px solid var(--border-color); }" +
                ".tab-btn { flex: 1; background: transparent; border: none; color: var(--text-sub); padding: 10px; font-size: 14px; font-weight: bold; cursor: pointer; border-radius: 8px; }" +
                ".tab-btn.active { background: var(--primary); color: white; }" +
                ".card { background: var(--card-bg); border: 1px solid var(--border-color); border-radius: 16px; padding: 20px; margin-bottom: 16px; }" +
                "h2 { font-size: 18px; margin-bottom: 14px; }" +
                ".filter-box { display: flex; gap: 8px; overflow-x: auto; padding-bottom: 8px; margin-bottom: 14px; }" +
                ".filter-chip { background: var(--border-color); color: var(--text-sub); border: none; padding: 6px 12px; border-radius: 20px; font-size: 13px; cursor: pointer; white-space: nowrap; }" +
                ".filter-chip.active { background: var(--accent); color: #000; font-weight: bold; }" +
                ".order-item { background: #0f172a; border: 1px solid var(--border-color); padding: 14px; border-radius: 10px; margin-bottom: 10px; }" +
                ".order-header { display: flex; justify-content: space-between; font-size: 13px; color: var(--text-sub); margin-bottom: 6px; }" +
                ".order-body { font-size: 15px; font-weight: 500; margin-bottom: 8px; line-height: 1.4; }" +
                ".order-footer { display: flex; justify-content: space-between; align-items: center; font-size: 14px; margin-top: 10px; border-top: 1px solid rgba(255,255,255,0.05); paddingTop: 8px; }" +
                ".price { color: var(--success); font-weight: bold; font-size: 16px; }" +
                ".ship-btn { background: var(--success); color: #000; border: none; padding: 6px 12px; border-radius: 6px; font-size: 13px; font-weight: bold; cursor: pointer; }" +
                ".summary-box { background: rgba(59, 130, 246, 0.1); border: 1px solid var(--primary); padding: 16px; border-radius: 12px; margin-top: 16px; text-align: center; }" +
                ".summary-total { font-size: 20px; font-weight: bold; color: var(--primary); margin-top: 6px; }" +
                ".kakao-btn { width: 100%; background: #fee500; color: #3c1e1e; border: none; padding: 14px; border-radius: 12px; font-size: 16px; font-weight: bold; cursor: pointer; margin-top: 14px; }" +
                ".hidden { display: none; }" +
                ".empty-msg { text-align: center; color: var(--text-sub); padding: 30px 0; font-size: 14px; }" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<div class='container'>" +
                "<h2 style='text-align:center; margin-bottom:16px;'>🍜 양평칼국수 통합 발주 장부</h2>" +
                "<div class='tabs'>" +
                "<button class='tab-btn active' onclick='switchTab(1)'>1동 당일 수신</button>" +
                "<button class='tab-btn' onclick='switchTab(2)'>2동 지점별 월간</button>" +
                "</div>" +
                "<div id='tab1' class='card'>" +
                "<h2>📥 오늘 실시간 발주 내역</h2>" +
                "<div id='todayOrderList'><div class='empty-msg'>서버에서 발주 내역 불러오는 중...</div></div>" +
                "</div>" +
                "<div id='tab2' class='card hidden'>" +
                "<h2>📅 지점별 월간 발주 명세서</h2>" +
                "<div class='filter-box'>" +
                "<button class='filter-chip active' onclick='filterBranch(\"전체\")'>전체</button>" +
                "<button class='filter-chip' onclick='filterBranch(\"조치원\")'>조치원점</button>" +
                "<button class='filter-chip' onclick='filterBranch(\"부강\")'>부강점</button>" +
                "<button class='filter-chip' onclick='filterBranch(\"청주\")'>청주점</button>" +
                "<button class='filter-chip' onclick='filterBranch(\"천안\")'>천안점</button>" +
                "<button class='filter-chip' onclick='filterBranch(\"계룡\")'>계룡점</button>" +
                "<button class='filter-chip' onclick='filterBranch(\"하남\")'>하남점</button>" +
                "</div>" +
                "<div id='monthlyOrderList'>" +
                "<div class='order-item' data-branch='조치원'><div class='order-header'><span>[조치원점]</span><span>11월 10일</span></div><div class='order-body'>반죽 20박스, 파김치 5kg</div><div class='order-footer'><span>금액</span><span class='price'>250,000원</span></div></div>" +
                "</div>" +
                "<div class='summary-box'>" +
                "<div style='font-size: 14px; color: var(--text-sub);' id='selectedBranchTitle'>[전체 지점] 11월 총 발주 금액</div>" +
                "<div class='summary-total' id='monthlyTotal'>250,000원</div>" +
                "</div>" +
                "<button class='kakao-btn' onclick='sendKakaoReport()'>💬 카카오톡으로 월간 명세서 보내기</button>" +
                "</div>" +
                "</div>" +
                "<script>" +
                "function switchTab(n) {" +
                "  document.querySelectorAll('.tab-btn').forEach((b,i)=>b.classList.toggle('active', i===n-1));" +
                "  document.getElementById('tab1').classList.toggle('hidden', n!==1);" +
                "  document.getElementById('tab2').classList.toggle('hidden', n!==2);" +
                "}" +
                "function loadOrders() {" +
                "  fetch('https://ypkal-1.onrender.com/api/check-order')" +
                "    .then(r => r.json())" +
                "    .then(d => {" +
                "       const list = document.getElementById('todayOrderList');" +
                "       if(d.order) {" +
                "          list.innerHTML = `<div class='order-item' id='order-current'>" +
                "             <div class='order-header'><span>[` + (d.order.branch || '지점 미지정') + `]</span><span>` + d.order.orderDate + `</span></div>" +
                "             <div class='order-body'>` + d.order.details + `</div>" +
                "             <div class='order-footer'>" +
                "                <div>합계: <span class='price'>` + Number(d.order.total).toLocaleString() + `원</span></div>" +
                "                <button class='ship-btn' onclick='completeShipment()'>📦 출고완료</button>" +
                "             </div>" +
                "          </div>`;" +
                "       } else {" +
                "          list.innerHTML = '<div class='empty-msg'>접수된 실시간 발주가 없습니다.</div>';" +
                "       }" +
                "    }).catch(e => { document.getElementById('todayOrderList').innerHTML = '<div class='empty-msg'>서버 연결 대기 중...</div>'; });" +
                "}" +
                "function completeShipment() {" +
                "  if(confirm('해당 발주 건을 출고 완료 처리하시겠습니까?')) {" +
                "     document.getElementById('todayOrderList').innerHTML = '<div class=\"empty-msg\">🎉 오늘 들어온 모든 발주가 출고 완료되었습니다!</div>';" +
                "  }" +
                "}" +
                "function filterBranch(bName) {" +
                "  document.querySelectorAll('.filter-chip').forEach(c => c.classList.toggle('active', c.innerText.includes(bName) || (bName==='전체' && c.innerText==='전체')));" +
                "  const items = document.querySelectorAll('#monthlyOrderList .order-item');" +
                "  let total = 0;" +
                "  items.forEach(item => {" +
                "     const itemB = item.getAttribute('data-branch');" +
                "     if(bName === '전체' || itemB === bName) { item.style.display = 'block'; total += 250000; }" +
                "     else { item.style.display = 'none'; }" +
                "  });" +
                "  document.getElementById('selectedBranchTitle').innerText = '[' + bName + ' 지점] 11월 총 발주 금액';" +
                "  document.getElementById('monthlyTotal').innerText = total.toLocaleString() + '원';" +
                "}" +
                "function sendKakaoReport() {" +
                "  const text = '[양평칼국수] 11월 지점별 발주 명세서\\n- 총 합계 금액: 250,000원\\n- 내역 확인 완료';" +
                "  navigator.clipboard.writeText(text).then(() => alert('📋 월간 명세서가 복사되었습니다! 카톡에 붙여넣기 하세요.'));" +
                "}" +
                "loadOrders(); setInterval(loadOrders, 5000);" +
                "</script>" +
                "</body>" +
                "</html>";

        webView.loadDataWithBaseURL("https://ypkal-1.onrender.com", html, "text/html", "UTF-8", null);
        setContentView(webView);
    }
}
