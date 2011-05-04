var canLog = window.console && console.log;

function activityNotificationHandler(activity) {
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

function log(message) {
	if (canLog) {
		console.log(message);
	}
}

function error(message) {
	if (canLog) {
		console.error(message);
	}
}

var socket = new io.Socket(!location.hostname.match(/localhost/) ? 'greenhouse-notifications.cloudfoundry.com' : location.hostname, 
	{ port: document.location.port == 8080 ? 8088 : document.location.port, 
	  rememberTransport:false, 
	  transports: ['xhr-polling'] }
);

$(document).ready(function() {
    setTimeout(function () {
        socket.connect();
        socket.on('message', activityNotificationHandler);

        socket.on('connect',  function(){ log('Connected'); });
        socket.on('disconnect', function(){ error('Disconnected'); });
        socket.on('reconnect', function(){ log('Reconnected to server'); });
        socket.on('reconnecting', function(){ log('Attempting to re-connect to the server, next attempt in ' + nextRetry + 'ms'); });
        socket.on('reconnect_failed', function(){ error('Reconnected to server FAILED.'); });
    }, 1000);
});