insert into Member (firstName, lastName, email, password, username, gender, birthdate) values ('Keith' , 'Donald', 'kdonald@vmware.com', 'melbourne', 'kdonald', 'M', '1977-12-29');
insert into Member (firstName, lastName, email, password, username, gender, birthdate) values ('Craig' , 'Walls', 'craig@habuma.com', 'freebird', 'habuma', 'M', '1975-3-4');
insert into Member (firstName, lastName, email, password, username, gender, birthdate) values ('Roy', 'Clarkson', 'rclarkson@vmware.com', 'atlanta', 'rclarkson', 'M', '1977-11-15');
insert into Member (firstName, lastName, email, password, username, gender, birthdate) values ('Jeremy', 'Grelle', 'jgrelle@vmware.com', 'churchkey', 'jgrelle', 'M', '1977-11-28');
insert into Member (firstName, lastName, email, password, username, gender, birthdate) values ('Mark', 'Fisher', 'mfisher@vmware.com', 'boston', 'mfisher', 'M', '1976-10-12');
insert into Member (firstName, lastName, email, password, username, gender, birthdate) values ('Keri', 'Donald', 'keridonald@gmail.com', 'donaldbarn', 'kkdonald', 'F', '1976-7-4');
insert into Member (firstName, lastName, email, password, username, gender, birthdate) values ('David', 'Winterfeldt', 'dwinterfeldt@vmware.com', 'sanfran', 'dwinterfeldt', 'M', '1973-11-4');

insert into App (name, slug, description, organization, website, apiKey, secret, callbackUrl) values ('Greenhouse for iPhone', 'greenhouse-for-iphone', 'Our slick mobile client for the Apple iPhone ', 'SpringSource', 'http://www.springsource.com', 'a08318eb478a1ee31f69a55276f3af64', '80e7f8f7ba724aae9103f297e5fb9bdf', 'x-com-springsource-greenhouse://oauth-response');
insert into AppDeveloper (app, member) values (1, 1);
insert into AppDeveloper (app, member) values (1, 2);
insert into AppDeveloper (app, member) values (1, 3);

insert into AccountProvider (name, displayName, implementation, apiKey, secret, requestTokenUrl, authorizeUrl, accessTokenUrl) values ('twitter', 'Twitter', 'com.springsource.greenhouse.connect.providers.TwitterAccountProvider', 'kqAm0GiPCT2owEtyTLPsug', '3461TFWV52VJuppKeaWMi8lKOxXMZtYLPGISq4nJ5s', 'https://twitter.com/oauth/request_token', 'https://twitter.com/oauth/authorize?oauth_token={token}', 'https://twitter.com/oauth/access_token');
insert into AccountProvider (name, displayName, implementation, apiKey, secret, appId) values ('facebook', 'Facebook', 'com.springsource.greenhouse.connect.providers.FacebookAccountProvider', '21aa96c8bc23259d0dd2ab99e496c306', 'f9f627194d471fb915dfbc856d347288', 157702280911244);
insert into AccountProvider (name, displayName, implementation, apiKey, secret, requestTokenUrl, authorizeUrl, accessTokenUrl) values ('linkedin', 'LinkedIn', 'com.springsource.greenhouse.connect.providers.LinkedInAccountProvider', 'elcLPr8RxIXifjn5RKwaTNxKPap4dYrr9ANuJ-abZNkTjbT3mSOVT7IhSfsF27XP', 'QiMWSBRBBM43wFuqpn8XtXqdfLB6A0TJUQslBjtuQAwYCOcIdvRaotT9c50I72pk', 'https://api.linkedin.com/uas/oauth/requestToken', 'https://www.linkedin.com/uas/oauth/authorize?oauth_token={token}', 'https://api.linkedin.com/uas/oauth/accessToken');
insert into AccountProvider (name, displayName, implementation, apiKey, secret, requestTokenUrl, authorizeUrl, accessTokenUrl) values ('tripit', 'TripIt', 'com.springsource.greenhouse.connect.providers.TripItAccountProvider', '739681c719f545d13d2376552d182b47c79b1d35', 'b6287a592afb3eccefb9261ee87c96d3b5f39286', 'https://api.tripit.com/oauth/request_token', 'https://www.tripit.com/oauth/authorize?oauth_token={token}&oauth_callback=http://localhost:8080/greenhouse/connect/tripit', 'https://api.tripit.com/oauth/access_token');

insert into MemberGroup (name, slug, description,hashtag, leader) values ('SpringOne2gx', 's2gx', 'The premier conference series for Spring technologies', '#s2gx', 1);
insert into MemberGroup (name, slug, description, hashtag, leader) values ('Atlanta Spring Users', 'atl', 'The Spring developers in the Greater Atlanta area', '#atlspring', 1);

insert into Venue (name, postalAddress, latitude, longitude, locationHint, createdBy) values ('Westin Lombard Yorktown Center', '70 Yorktown Center Lombard, IL 60148', 41.8751108905486, -88.0184300761646, 'adjacent to Shopping Center', 1);
insert into VenueRoom  (venue, id, name, capacity, locationHint) values (1, 1, 'Junior Ballroom A', 150, 'first floor');
insert into VenueRoom  (venue, id, name, capacity, locationHint) values (1, 2, 'Junior Ballroom B', 150, 'first floor');
insert into VenueRoom  (venue, id, name, capacity, locationHint) values (1, 3, 'Junior Ballroom C', 150, 'first floor');
insert into VenueRoom  (venue, id, name, capacity, locationHint) values (1, 4, 'Lilac C', 100, 'first floor');
insert into VenueRoom  (venue, id, name, capacity, locationHint) values (1, 5, 'Lilac D', 100, 'first floor');
insert into VenueRoom  (venue, id, name, capacity, locationHint) values (1, 6, 'Hary Cary Balroom C', 200, 'first floor');
insert into VenueRoom  (venue, id, name, capacity, locationHint) values (1, 7, 'Hary Cary Balroom D', 200, 'first floor');
insert into VenueRoom  (venue, id, name, capacity, locationHint) values (1, 8, 'Magnolia', 50, 'first floor');
insert into VenueRoom  (venue, id, name, capacity, locationHint) values (1, 9, 'Cypress', 50, 'first floor');

-- SpringOne Chicago 2010

insert into Leader (name, member) values ('Keith Donald', 1);
insert into Leader (name, member) values ('Craig Walls', 2);
insert into Leader (name, member) values ('Roy Clarkson', 3);
insert into Leader (name, member) values ('Jeremy Grelle', 4);

insert into Event (title, timeZone, startTime, endTime, slug, description, memberGroup) values ('SpringOne2gx', 'America/Chicago', '2010-10-19 22:00:00Z', '2010-10-22 22:00:00Z', 'chicago', 'SpringOne 2GX is a one-of-a-kind conference for application developers, solution architects, web operations and IT teams who develop, deploy and manage business applications.', 1);
insert into EventVenue (event, venue) values (1, 1);

insert into EventTrack (event, code, name, description, chair) values (1, 'spr', 'Essential Spring', 'Spring techniques and technologies applicable to most classes of applications', 2);
insert into EventTrack (event, code, name, description, chair) values (1, 'web', 'Web Application Development', 'What you need to know to build rich web applications', 1);

insert into EventSession (event, id, title, startTime, endTime, description, hashtag, track, venue, room) values (1, 1, 'Mastering MVC 3', '2010-10-20 17:45:00Z', '2010-10-20 19:15:00Z',
	'A deep-dive into the latest capabilities of Spring MVC, Spring''s REST-ful web application development platform.', '#mvc', 'web', 1, 2);
insert into EventSessionLeader (event, session, leader) values (1, 1, 1);

insert into EventSession (event, id, title, startTime, endTime, description, hashtag, track, venue, room) values (1, 2, 'Inside Web Flow 3 Development', '2010-10-20 19:45:00Z', '2010-10-20 21:15:00Z',
	'A look inside the development of Spring Web Flow 3, the next-generation version of Spring''s stateful controller framework for orchestrating multi-step user dialogs.', '#webflow', 'web', 1, 2);
insert into EventSessionLeader (event, session, leader) values (1, 2, 1);

insert into EventSession (event, id, title, startTime, endTime, description, hashtag, track, venue, room) values (1, 3, 'Developing Social-Ready Web Applications', '2010-10-21 17:45:00Z', '2010-10-21 19:15:00Z',
	'Businesses are increasingly recognizing the value of connecting with their customers on a more personal level. Companies can utilize social networking to transition from "Big Faceless Corporation" to "Friend" by taking their wares to the online communities where their customers are. In this age of social media, those communities are found at social network sites such as Facebook, Twitter, and LinkedIn. In this session, you''ll learn how to build Spring-based applications that interact with the various social networks. We''ll talk about new features in the Spring portfolio to support integration with social networks as well as how to start building social features into your own applications.',
	'#social', 'web', 1, 2);
insert into EventSessionLeader (event, session, leader) values (1, 3, 2);
	
insert into EventSession (event, id, title, startTime, endTime, description, hashtag, track, venue, room) values (1, 4, 'Choices in Mobile Application Development', '2010-10-21 19:45:00Z', '2010-10-21 21:15:00Z',
	'With the rising prevalence of advanced mobile platforms such as iPhone, Android, and Web OS, the desire for rich mobile clients as another means of accessing enterprise services is becoming something that can no longer be ignored. In this session, we will explore the current mobile development landscape and discuss what you as a Spring developer can do to support this increasingly important paradigm. We will examine the benefits and tradeoffs of native mobile client development vs. web-based mobile client development, and we will explore some of the emerging cross-platform options such as PhoneGap. We will look at the various strategies for utilizing a Spring back-end with these mobile platforms, such as consumption of RESTful services, authentication and authorization via OAuth, and server-push style messaging.',
	'#mobile', 'web', 1, 2);
insert into EventSessionLeader (event, session, leader, rank) values (1, 4, 3, 1);
insert into EventSessionLeader (event, session, leader, rank) values (1, 4, 4, 2);