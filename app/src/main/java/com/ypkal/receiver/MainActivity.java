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
        
        // 자바스크립트에서 안드로이드 Native 공유 기능을 호출할 수 있도록 브릿지 연결
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
                ".filter-chip.active { background: #f59e0b; color: #000; font-weight: bold; border-color: #f59e0b; }" +
                ".order-card { background: #0f172a; border: 1px solid #334155; padding: 15px; border-radius: 12px; margin-bottom: 12px; }" +
                ".order-header { display: flex; justify-content: space-between; font-size: 13px; color: #94a3b8; border-bottom: 1px solid #334155; padding-bottom: 8px; margin-bottom: 8px; font-weight: bold; }" +
                ".order-body { font-size: 15px; font-weight: 500; margin-bottom: 10px; line-height: 1.5; color: #f8fafc; }" +
                ".order-footer { display: flex; justify-content: space-between; align-items: center; border-top: 1px dashed #334155; padding-top: 8px; }" +
                ".price { color: #22c55e; font-weight: bold; font-size: 16px; }" +
                ".ship-btn { background: #22c55e; color: #000; border: none; padding: 8px 14px; border-radius: 8px; font-size: 13px; font-weight: bold; cursor: pointer; }" +
                
                /* 전표 스타일 영수증 표 디자인 (캡처 영역) */
                "#captureArea { background: #ffffff; color: #111827; padding: 25px; border-radius: 12px; font-family: 'Malgun Gothic', sans-serif; }" +
                ".slip-title { text-align: center; font-size: 22px; font-weight: bold; margin-bottom: 5px; color: #111827; border-bottom: 3px double #111827; padding-bottom: 10px; }" +
                ".slip-subtitle { text-align: center; font-size: 14px; color: #4b5563; margin-bottom: 20px; }" +
                ".slip-table { width: 100%; border-collapse: collapse; margin-bottom: 20px; font-size: 13px; }" +
                ".slip-table th { background: #f3f4f6; color: #1f2937; border-top: 2px solid #111827; border-bottom: 2px solid #111827; padding: 10px 8px; text-align: center; font-weight: bold; }" +
                ".slip-table td { border-bottom: 1px solid #e5e7eb; padding: 10px 8px; text-align: center; color: #1f2937; }" +
                ".slip-table td.left { text-align: left; }" +
                ".slip-table td.right { text-align: right; font-weight: bold; }" +
                ".slip-total-box { background: #f9fafb; border: 2px solid #111827; border-radius: 8px; padding: 15px; display: flex; justify-content: space-between; align-items: center; }" +
                ".slip-total-label { font-size: 16px; font-weight: bold; color: #111827; }" +
                ".slip-total-val { font-size: 20px; font-weight: bold; color: #dc2626; }" +

                ".month-select { padding: 10px; font-size: 15px; font-weight: bold; border: 2px solid #cbd5e1; border-radius: 8px; background: #fff; color: #111827; width: 100%; margin-bottom: 15px; }" +
                ".filter-box-slip { display: flex; gap: 6px; overflow-x: auto; padding-bottom: 8px; margin-bottom: 15px; }" +
                ".filter-chip-slip { background: #f3f4f6; color: #4b5563; border: 1px solid #d1d5db; padding: 6px 12px; border-radius: 20px; font-size: 12px; cursor: pointer; white-space: nowrap; }" +
                ".filter-chip-slip.active { background: #111827; color: #fff; font-weight: bold; border-color: #111827; }" +

                ".btn-kakao { width: 100%; background: #fee500; color: #3c1e1e; border: none; padding: 14px; border-radius: 12px; font-size: 15px; font-weight: bold; cursor: pointer; margin-top: 15px; box-shadow: 0 4px 10px rgba(0,0,0,0.2); }" +
                ".empty-msg { text-align: center; color: #94a3b8; padding: 30px 0; font-size: 14px; }" +
                "</style></head><body>" +
                "<div class='container'>" +
                "<h2>🔔 양평칼국수 본점 수신 대시보드</h2>" +
                "<div class='subtitle'>실시간 항시 수신 및 지점별 월간 장부 관리</div>" +
                "<div class='status-bar'><div><span style='color: #22c55e; font-weight: bold;'>🟢 항시 자동 수신 가동 중</span></div><span style='font-size:12px; color:#94a3b8;'>실시간 동기화</span></div>" +
                "<div class='tabs'>" +
                "<div class='tab active' id='tabBtnRealtime' onclick='switchTab(\"realtime\")'>📥 당일 실시간 수신</div>" +
                "<div class='tab' id='tabBtnMonthly' onclick='switchTab(\>monthly\")'>📅 지점별 월간 장부</div>" +
                "</div>" +
                "<div id='tab-realtime' class='tab-content active'><div id='todayOrderList'><div class='empty-msg'>🎉 대기 중인 실시간 발주가 없습니다.</div></div></div>" +
                "<div id='tab-monthly' class='tab-content'>" +
                
                // 전표 스타일로 출력될 캡처 영역
                "<div id='captureArea'>" +
                "<div class='slip-title'>양평칼국수 월간 발주 전표</div>" +
                "<div class='slip-subtitle' id='slipSubTitle'>[전체 지점] 2026년 11월 정산 내역</div>" +
                "<select id='selectedMonth' class='month-select' onchange='renderMonthlyData()'></select>" +
                "<div class='filter-box-slip'>" +
                "<button class='filter-chip-slip active' onclick='filterBranch(\"전체\", event)'>전체</button>" +
                "<button class='filter-chip-slip' onclick='filterBranch(\"조치원점\", event)'>조치원점</button>" +
                "<button class='filter-chip-slip' onclick='filterBranch(\"부강점\", event)'>부강점</button>" +
                "<button class='filter-chip-slip' onclick='filterBranch(\"청주점\", event)'>청주점</button>" +
                "<button class='filter-chip-slip' onclick='filterBranch(\"천안점\", event)'>천안점</button>" +
                "<button class='filter-chip-slip' onclick='filterBranch(\"계룡점\", event)'>계룡점</button>" +
                "<button class='filter-chip-slip' onclick='filterBranch(\"하남점\", event)'>하남점</button>" +
                "</div>" +
                "<table class='slip-table'><thead><tr><th>일자</th><th>지점</th><th>발주 내역</th><th>합계금액</th></tr></thead><tbody id='slipTableBody'><tr><td colspan='4'>내역이 없습니다.</td></tr></tbody></table>" +
                "<div class='slip-total-box'><span class='slip-total-label'>청구 총액 합계</span><span class='slip-total-val' id='slipGrandTotal'>0원</span></div>" +
                "</div>" + // 캡처 영역 끝
                
                "<button class='btn-kakao' onclick='shareMonthlyImage()'>💬 전표 이미지로 카톡 바로 공유하기</button>" +
                "</div></div>" +
                "<script>" +
                "let db = JSON.parse(localStorage.getItem('yp_receiver_dashboard_db')) || { orders: [], shippedIds: [] };" +
                "let audioCtx = null, currentSelectedBranch = '전체';" +
                "function playBeep() { try { if (!audioCtx) audioCtx = new (window.AudioContext || window.webkitAudioContext)(); var osc = audioCtx.createOscillator(); var gain = audioCtx.createGain(); osc.type = 'sine'; osc.frequency.setValueAtTime(880, audioCtx.currentTime); gain.gain.setValueAtTime(0.5, audioCtx.currentTime); osc.connect(gain); gain.connect(audioCtx.destination); osc.start(); osc.stop(audioCtx.currentTime + 0.6); if (navigator.vibrate) navigator.vibrate([400, 200, 400]); } catch(e){} }" +
                "function checkServer() { fetch('https://ypkal-1.onrender.com/api/check-order').then(r => r.json()).then(d => { if(d.order) { const id = d.order.id || new Date(d.order.orderDate).getTime(); if(!db.orders.some(o => o.id === id)) { db.orders.push({ id: id, branch: d.order.branch || '지점 미지정', orderDate: d.order.orderDate, details: d.order.details, total: Number(d.order.total)||0 }); localStorage.setItem('yp_receiver_dashboard_db', JSON.stringify(db)); playBeep(); renderRealtimeOrders(); renderMonthlyData(); } } }).catch(e=>{}); }" +
                "function completeShipment(id) { if(!db.shippedIds.includes(id)) { db.shippedIds.push(id); localStorage.setItem('yp_receiver_dashboard_db', JSON.stringify(db)); } renderRealtimeOrders(); renderMonthlyData(); }" +
                "function switchTab(name) { document.getElementById('tabBtnRealtime').classList.remove('active'); document.getElementById('tabBtnMonthly').classList.remove('active'); document.getElementById('tab-realtime').classList.remove('active'); document.getElementById('tab-monthly').classList.remove('active'); if(name === 'realtime') { document.getElementById('tabBtnRealtime').classList.add('active'); document.getElementById('tab-realtime').classList.add('active'); renderRealtimeOrders(); } else { document.getElementById('tabBtnMonthly').classList.add('active'); document.getElementById('tab-monthly').classList.add('active'); renderMonthlyData(); } }" +
                "function initMonthSelector() { const s = document.getElementById('selectedMonth'); if(!s) return; s.innerHTML = ''; const now = new Date(); for(let i=-6; i<=6; i++){ const d = new Date(now.getFullYear(), now.getMonth()+i, 1); const v = d.getFullYear() + '-' + String(d.getMonth()+1).padStart(2,'0'); s.innerHTML += '<option value=\"' + v + '\">' + d.getFullYear() + '년 ' + (d.getMonth()+1) + '월</option>'; } s.value = now.getFullYear() + '-' + String(now.getMonth()+1).padStart(2,'0'); }" +
                "function renderRealtimeOrders() { const c = document.getElementById('todayOrderList'); if(!c) return; const list = db.orders.filter(o => !db.shippedIds.includes(o.id)); if(list.length === 0) { c.innerHTML = '<div class=\"empty-msg\">🎉 대기 중인 실시간 발주가 없습니다.</div>'; return; } c.innerHTML = ''; list.slice().reverse().forEach(o => { c.innerHTML += '<div class=\"order-card\"><div class=\"order-header\"><span>📍 [' + o.branch + ']</span><span>' + o.orderDate + '</span></div><div class=\"order-body\">' + o.details + '</div><div class=\"order-footer\"><div>합계: <span class=\"price\">' + o.total.toLocaleString() + '원</span></div><button class=\"ship-btn\" onclick=\"completeShipment(' + o.id + ')\">📦 출고완료</button></div></div>'; }); }" +
                "function filterBranch(b, e) { const chips = document.querySelectorAll('.filter-chip-slip'); for(let i=0; i<chips.length; i++) chips[i].classList.remove('active'); e.target.classList.add('active'); currentSelectedBranch = b; renderMonthlyData(); }" +
                "function renderMonthlyData() { const mSel = document.getElementById('selectedMonth'); if(!mSel) return; const m = mSel.value; const tbody = document.getElementById('slipTableBody'); if(!tbody) return; const list = db.orders.filter(o => o.orderDate && o.orderDate.includes(m) && (currentSelectedBranch === '전체' || o.branch === currentSelectedBranch)); let total = 0; tbody.innerHTML = ''; document.getElementById('slipSubTitle').innerText = '[' + currentSelectedBranch + '] ' + m.replace('-', '년 ') + '월 정산 내역'; if(list.length === 0) { tbody.innerHTML = '<tr><td colspan=\"4\" style=\"padding:20px; color:#6b7280;\">해당 월 발주 내역이 없습니다.</td></tr>'; document.getElementById('slipGrandTotal').innerText = '0원'; return; } list.forEach(o => { total += o.total; tbody.innerHTML += '<tr><td>' + o.orderDate.substring(5) + '</td><td><b>' + o.branch + '</b></td><td class=\"left\">' + o.details + '</td><td class=\"right\">' + o.total.toLocaleString() + '원</td></tr>'; }); document.getElementById('slipGrandTotal').innerText = total.toLocaleString() + '원'; }" +
                "function shareMonthlyImage() {" +
                "  const target = document.getElementById('captureArea');" +
                "  html2canvas(target, { backgroundColor: '#ffffff', scale: 2 }).then(canvas => {" +
                "    const base64Data = canvas.toDataURL('image/png').split(',')[1];" +
                "    if(window.AndroidBridge) {" +
                "      window.AndroidBridge.shareImage(base64Data, 'monthly_slip.png');" +
                "    } else {" +
                "      const link = document.createElement('a'); link.download = 'slip.png'; link.href = canvas.toDataURL(); link.click();" +
                "    }" +
                "  });" +
                "}" +
                "initMonthSelector(); renderRealtimeOrders(); renderMonthlyData(); setInterval(checkServer, 5000);" +
                "</script></body></html>";

        webView.loadDataWithBaseURL("https://ypkal-1.onrender.com", html, "text/html", "UTF-8", null);
        setContentView(webView);
    }

    // 안드로이드 네이티브와 웹뷰를 연결하여 카카오톡 등 SNS로 이미지 바로 공유하는 브릿지 클래스
    public static class WebAppInterface {
        Activity mContext;
        WebAppInterface(Activity c) { mContext = c; }

        @JavascriptInterface
        public void shareImage(String base64Data, String fileName) {
            try {
                byte[] decodedString = android.util.Base64.decode(base64Data, android.util.Base64.DEFAULT);
                File file = new File(mContext.getExternalCacheDir(), fileName);
                FileOutputStream fos = new FileOutputStream(file);
                fos.write(decodedString);
                fos.close();

                Uri uri = FileProvider.getUriForFile(mContext, mContext.getPackageName() + ".fileprovider", file);
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("image/png");
                intent.putExtra(Intent.EXTRA_STREAM, uri);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                mContext.startActivity(Intent.createChooser(intent, "월간 발주 전표 공유하기"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
