$(document).ready(function() {
	var basePath = location.protocol + "//" + location.host + greenhouse.contextPath;
	
	function activityNotificationHandler(response) {
		var activityItems = $.parseJSON(response.responseBody);
		for (var i = 0; i < activityItems.length; i++) {
			var activity = activityItems[i];
			$("<li></li>").addClass("newItem").append($("<img/>", { "src" : activity.memberPictureUrl }).addClass("profile")).
				append("<p>" + activity.text + "</p>").
				append($("<img/>", { "src" : activity.imageUrl}).addClass("activity")).
				prependTo("#recentActivityList");	
			if ($("#recentActivityList li").length == 6) {
				$("#recentActivityList li:last").slideUp("slow", function() {
					$(this).remove();
				});
			}
			$("#recentActivityList li.newItem").slideDown("slow", function() {
				$(this).removeClass("newItem");
			});
		}
	}

	/* transport can be : long-polling, streaming or websocket */
	$.atmosphere.subscribe(basePath + '/pubsub/recent/notifications', activityNotificationHandler,
		$.atmosphere.request = {
			transport : "long-polling",
			headers : { "Accept" : "application/json" }
		}
	);
});