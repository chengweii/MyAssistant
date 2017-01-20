window.assistant.showCountDown = function(containerId, year, month, day) {
	var interval = 10;
	var lpad = function(num, n) {
		return (Array(n).join(0) + num).slice(-n);
	}
	var countDown = function(year, month, day) {
		var now = new Date();
		var endDate = new Date(year, month - 1, day);
		var leftTime = parseInt((endDate.getTime() - now.getTime()) / 1000);
		var day1 = parseInt(leftTime / (60 * 60 * 24));
		var hour = parseInt(leftTime / (60 * 60) % 24);
		var minute = parseInt(leftTime / 60 % 60);
		var second = parseInt(leftTime % 60);
		var msecond = parseInt((endDate.getTime() - now.getTime() / 100) % 100);
		$("#" + containerId).html(
				"<span class='countDown-day'>" + day1
						+ "</span><span>D</span><span class='countDown'>"
						+ hour
						+ "</span><span>H</span><span class='countDown'>"
						+ minute
						+ "</span><span>m</span><span class='countDown'>"
						+ lpad(second, 2)
						+ "</span><span>s</span><span class='countDown'>"
						+ lpad(msecond, 2) + "</span>");
	}
	setInterval(function() {
		countDown(year, month, day);
	}, interval);
}

window.assistant.responseHandlers.push(function(msg_li) {
	var countdown = msg_li.find("countdown");
	$.each(countdown,function(index,item){
		var containerId = "countDown-" + new Date().getTime();
		$(countdown).replaceWith(
				"<p id='" + containerId + "' class='countDown-container'><p>");
		var value = $(countdown).attr("value").split(",");
		window.assistant.showCountDown(containerId, value[0], value[1], value[2]);
	});
});