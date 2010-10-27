alter table AccountProvider rename to ServiceProvider;
update ServiceProvider set implementation = 'com.springsource.greenhouse.connect.providers.TwitterServiceProvider' where name = 'twitter';
update ServiceProvider set implementation = 'com.springsource.greenhouse.connect.providers.FacebookServiceProvider' where name = 'facebook';
update ServiceProvider set implementation = 'com.springsource.greenhouse.connect.providers.LinkedInServiceProvider' where name = 'linkedin';
update ServiceProvider set implementation = 'com.springsource.greenhouse.connect.providers.TripItServiceProvider' where name = 'tripit';