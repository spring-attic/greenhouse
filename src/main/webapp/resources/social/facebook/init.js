FB.init({appId: '8f007e7ce33d82dc2f5485102b3504c2', status: true, cookie: true, xfbml: true});
FB.Event.subscribe('auth.sessionChange', function(response) {
    if (response.session) {
     //   alert("Hey!");
    } else {
     //   alert("See ya!");
    }
});