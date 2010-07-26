if(FB) {
	FB.requireSessionThenGoTo = function(url) {
		FB.getLoginStatus(function(response) {
			if (response.session) { 
				window.location = url; 
			} else {
				FB.login(function(response) {
					if (response.session) {
						window.location=url;
					}
				});
			}
		});
	};
	
	FB.logoutThenGoTo = function(url) {
		FB.logout(function(response) { 
			window.location=url; 
		});
	};
}