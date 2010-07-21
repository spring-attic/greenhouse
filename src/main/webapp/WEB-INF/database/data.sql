insert into Member (firstName, lastName, email, password, username) values ('Keith' , 'Donald', 'kdonald@vmware.com', 'melbourne', 'kdonald');
insert into Member (firstName, lastName, email, password, username) values ('Craig' , 'Walls', 'cwalls@vmware.com', 'plano', 'habuma');
insert into Member (firstName, lastName, email, password, username) values ('Roy', 'Clarkson', 'rclarkson@vmware.com', 'atlanta', 'rclarkson');

insert into App (consumerKey, name, description, website, callbackUrl, secret, owner) values ('a08318eb478a1ee31f69a55276f3af64', 'Greenhouse for the iPhone', 'Awesome', 'http://www.springsource.com', 'x-com-springsource-greenhouse://oauth-response', '80e7f8f7ba724aae9103f297e5fb9bdf', 2);

insert into Update (text, updateTimestamp, member) values ('Keith Donald signed up', 1278019179970, 1);
insert into Update (text, updateTimestamp, member) values ('Craig Walls signed up', 1278017173970, 2);
insert into Update (text, updateTimestamp, member) values ('Roy Clarkson signed up', 1274015177470, 3);

insert into MemberGroup (publicId, name, description, hashtag, leader) values ('SpringOne', 'SpringOne', 'The premier Spring Framework event.', '#springone', 1);

insert into Event (publicId, title, description, startTime, endTime, location, memberGroup, hashtag) values ('springone_2gx', 'SpringOne/2GX', 
    'SpringOne 2GX is a one-of-a-kind conference for application developers, solution architects, web operations and IT teams who develop, deploy and manage business applications.', 
    '2010-10-19', '2010-10-22', 'Chicago, IL', 1, '#springone2gx');

insert into EventTrack (name, description, chair, event) values ('Core Spring', 'The low-down on the core Spring Framework', 1, 1);

insert into EventSession (code, title, description, startTime, endTime, speaker, event, track, hashtag) values ('CS1', 
	'Developing Social-Ready Web Applications', 
	'Businesses are increasingly recognizing the value of connecting with their customers on a more personal level. Companies can utilize social networking to transition from "Big Faceless Corporation" to "Friend" by taking their wares to the online communities where their customers are. In this age of social media, those communities are found at social network sites such as Facebook, Twitter, and LinkedIn. In this session, you%27ll learn how to build Spring-based applications that interact with the various social networks. We%27ll talk about new features in the Spring portfolio to support integration with social networks as well as how to start building social features into your own applications.', 
	'2010-10-20', '2010-10-20', 2, 1, 1, '#s12gxcs1');

insert into ConnectedMember (connectionId, connectionApp, member) values ('1255689239', 'facebook', 1);
insert into ConnectedMember (connectionId, connectionApp, member) values ('738140579', 'facebook', 2);
insert into ConnectedMember (connectionId, connectionApp, member) values ('1533260333', 'facebook', 3);
