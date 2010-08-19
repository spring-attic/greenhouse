$(document).ready(function (){
	
	var basePath = location.protocol + "//" + location.host + "/greenhouse";
	
	function activityNotificationHandler(response) {
		var activity = $.parseJSON(response.responseBody);
		
		$("<li></li>").
	    	addClass("newItem").
	    	append($("<img/>", { "src" : activity.memberPictureUrl }).addClass("profile")).
	    	append(activity.text).
	    	append($("<img/>", { "src" : activity.imageUrl }).addClass("activity")).
	    	prependTo("#recentActivity");
		
		if ($("#recentActivity li").length == 6) {
		    $("#recentActivity li:last").slideUp("slow", function() {
		        $(this).remove();
		    });
		}
		$("#recentActivity li.newItem").slideDown("slow", function() {
		    $(this).removeClass("newItem");
		});
	}
	
	/* transport can be : long-polling, streaming or websocket */
	$.atmosphere.subscribe(basePath + '/pubsub/recent/notifications', activityNotificationHandler,
            $.atmosphere.request = { transport: "streaming", headers: { "Accept" : "application/json" } });
	
});