package com.ypkal.receiver;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WebView webView = new WebView(this);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setDatabaseEnabled(true);
        webView.setWebViewClient(new WebViewClient());

        String html = "<!DOCTYPE html><html lang='ko'><head><meta charset='UTF-8'><meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
                "<title>양평칼국수 본점 수신 대시보드</title>" +
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
                ".grand-total-box { background: #0f172a; border: 2px solid #1e3a8a; border-radius: 12px; padding: 20px; text-align: center; margin-bottom: 15px; }" +
                ".grand-total-label { font-size: 14px; color: #60a5fa; font-weight: 600; margin-bottom: 5px; }" +
                ".grand-total-value { font-size: 26px; color: #93c5fd; font-weight: bold; }" +
                ".btn-kakao { width: 100%; background: #fee500; color: #3c1e1e; border: none; padding: 14px; border-radius: 12px; font-size: 15px; font-weight: bold; cursor: pointer; margin-top: 15px; }" +
                ".empty-msg { text-align: center; color: #94a3b8; padding: 30px 0; font-size: 14px; }" +
                ".month-select { padding: 10px; font-size: 15px; font-weight: bold; border: 2px solid #334155; border-radius: 10px; background: #0f172a; color: #60a5fa; width: 100%; margin-bottom: 15px; }" +
                "</style></head><body>" +
                "<div class='container'>" +
                "<h2>🔔 양평칼국수 본점 수신 대시보드</h2>" +
                "<div class='subtitle'>실시간 항시 수신 및 지점별 월간 장부 관리</div>" +
                "<div class='status-bar'><div><span style='color: #22c55e; font-weight: bold;'>🟢 항시 자동 수신 가동 중</span></div><span style='font-size:12px; color:#94a3b8;'>실시간 동기화</span></div>" +
                "<div class='tabs'>" +
                "<div class='tab active' id='tabBtnRealtime' onclick='switchTab(\"realtime\")'>📥 당일 실시간 수신</div>" +
                "<div class='tab' id='tabBtnMonthly' onclick='switchTab(\"monthly\")'>📅 지점별 월간 장부</div>" +
                "</div>" +
                "<div id='tab-realtime' class='tab-content active'><div id='todayOrderList'><div class='empty-msg'>접수된 실시간 발주가 없습니다.</div></div></div>" +
                "<div id='tab-monthly' class='tab-content'>" +
                "<select id='selectedMonth' class='month-select' onchange='renderMonthlyData()'></select>" +
                "<div class='filter-box'>" +
                "<button class='filter-chip active' onclick='filterBranch(\"전체\", event)'>전체</button>" +
                "<button class='filter-chip' onclick='filterBranch(\"조치원점\", event)'>조치원점</button>" +
                "<button class='filter-chip' onclick='filterBranch(\"부강점\", event)'>부강점</button>" +
                "<button class='filter-chip' onclick='filterBranch(\"청주점\", event)'>청주점</button>" +
                "<button class='filter-chip' onclick='filterBranch(\"천안점\", event)'>천안점</button>" +
                "<button class='filter-chip' onclick='filterBranch(\"계룡점\", event)'>계룡점</button>" +
                "<button class='filter-chip' onclick='filterBranch(\"하남점\", event)'>하남점</button>" +
                "</div>" +
                "<div class='grand-total-box'><div class='grand-total-label' id='summaryTitle'>[전체 지점] 월간 총 발주 금액</div><div class='grand-total-value' id='summaryGrandTotal'>0원</div></div>" +
                "<div id='monthlyListContainer'></div>" +
                "<button class='btn-kakao' onclick='sendKakaoReport()'>💬 카카오톡으로 월간 명세서 공유하기</button>" +
                "</div></div>" +
                "<script>" +
                "let db = JSON.parse(localStorage.getItem('yp_receiver_dashboard_db')) || { orders: [], shippedIds: [] };" +
                "let audioCtx = null, currentSelectedBranch = '전체';" +
                "function playBeep() { try { if (!audioCtx) audioCtx = new (window.AudioContext || window.webkitAudioContext)(); var osc = audioCtx.createOscillator(); var gain = audioCtx.createGain(); osc.type = 'sine'; osc.frequency.setValueAtTime(880, audioCtx.currentTime); gain.gain.setValueAtTime(0.5, audioCtx.currentTime); osc.connect(gain); gain.connect(audioCtx.destination); osc.start(); osc.stop(audioCtx.currentTime + 0.6); if (navigator.vibrate) navigator.vibrate([400, 200, 400]); } catch(e){} }" +
                "function checkServer() { fetch('https://ypkal-1.onrender.com/api/check-order').then(r => r.json()).then(d => { if(d.order) { const id = d.order.id || new Date(d.order.orderDate).getTime(); if(!db.orders.some(o => o.id === id)) { db.orders.push({ id: id, branch: d.order.branch || '지점 미지정', orderDate: d.order.orderDate, details: d.order.details, total: Number(d.order.total)||0 }); localStorage.setItem('yp_receiver_dashboard_db', JSON.stringify(db)); playBeep(); renderRealtimeOrders(); renderMonthlyData(); } } }).catch(e=>{}); }" +
                "function completeShipment(id) { if(!db.shippedIds.includes(id)) { db.shippedIds.push(id); localStorage.setItem('yp_receiver_dashboard_db', JSON.stringify(db)); } renderRealtimeOrders(); renderMonthlyData(); }" +
                "function switchTab(name) { document.getElementById('tabBtnRealtime').classList.remove('active'); document.getElementById('tabBtnMonthly').classList.remove('active'); document.getElementById('tab-realtime').classList.remove('active'); document.getElementById('tab-monthly').classList.remove('active'); if(name === 'realtime') { document.getElementById('tabBtnRealtime').classList.add('active'); document.getElementById('tab-realtime').classList.add('active'); renderRealtimeOrders(); } else { document.getElementById('tabBtnMonthly').classList.add('active'); document.getElementById('tab-monthly').classList.add('active'); renderMonthlyData(); } }" +
                "function
