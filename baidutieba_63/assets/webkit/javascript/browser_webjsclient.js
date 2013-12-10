function flyflow_WebJsClient() {
	window.document.onclick = function(ev) {
		ev = ev || window.event; // 事件
		var target = ev.target || ev.srcElement; // 获得事件源
		var tagName = target.tagName.toLowerCase(); // 获取标签名
		if (tagName = 'a') {
			var hrefValue = target.href;
			window.flyflow_webkit_js.onClick(hrefValue);
		}
	};
	
	window.history.back =  function(){
		window.flyflow_webkit_js.onGoBack();
	};
	
	window.history.forward =  function(){
		window.flyflow_webkit_js.onGoForward();
	};
	
	window.history.go =  function(step){
		window.flyflow_webkit_js.onGo(step);
	};
	
	window.flyflow_webkit_js.onWebJsClientFinished();
}