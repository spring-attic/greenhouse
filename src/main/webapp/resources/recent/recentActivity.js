$(document).ready(function() {
	var basePath = location.protocol + "//" + location.host + application.contextPath;
	
	function activityNotificationHandler(response) {
		var activityItems = $.parseJSON(response.responseBody);
		for (var i = 0; i < activityItems.length; i++) {
			var activity = activityItems[i];
			$("<li class='listing'></li>").addClass("newItem").append($("<img/>", { "src" : activity.memberPictureUrl }).addClass("profile")).
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

});