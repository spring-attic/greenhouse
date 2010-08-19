insert into Member (firstName, lastName, email, password, username) values ('Keith' , 'Donald', 'kdonald@vmware.com', '74655aa5c7defb5bd159dcaed45d5e8aba7fdc974413582193099a9661fbc99e:9af3da7c-584a-4cd7-a060-31fca6949347', 'kdonald');
insert into Member (firstName, lastName, email, password, username) values ('Craig' , 'Walls', 'craig@habuma.com', '4717223a6c271ed8b0a701ee661f2e4ffb7d7922909e64c84c946385b72adbd9:213535b9-d696-4349-8d24-a6da11b169e3', 'habuma');
insert into Member (firstName, lastName, email, password, username) values ('Roy', 'Clarkson', 'rclarkson@vmware.com', '808cdba5a11d9a67ac44ceee2667ae111f03037f41648da09430daef20e41eac:dc015d3d-b678-4f08-a14c-ad42e4fe32ff', 'rclarkson');
insert into Member (firstName, lastName, email, password, username) values ('Jeremy', 'Grelle', 'jgrelle@vmware.com', '80eba7f09f2955597d4c8a995f5a311581052711509c7a7ce74d9c8b502667f3:8537bd75-cc6d-4473-aad2-d2731c12c3d7', 'jgrelle');
insert into Member (firstName, lastName, email, password, username) values ('Mark', 'Fisher', 'mfisher@vmware.com', '87da6fd4e6a5454bb4c32f5ab99498dc03959302f21069aa444f0f4f8dad8dd7:2e43f0da-725f-4d8e-80f0-5b5c75b4aa40', 'mfisher');
insert into Member (firstName, lastName, email, password, username) values ('Keri', 'Donald', 'keridonald@gmail.com', 'cacbc4db15b707d60c029fa4c2fa2a7ea400ea3560998398787290cbaee3f429:09f006da-2e19-48b1-ae42-d2115d86ae0d', 'kkdonald');

insert into Badge (name, description, level, imageUrl) values ('Newbie', 'You joined!', 1, 'http://localhost:8080/resources/badges/newbie.png');

insert into App (consumerKey, name, description, website, callbackUrl, secret, owner) values ('a08318eb478a1ee31f69a55276f3af64', 'Greenhouse for the iPhone', 'Awesome', 'http://www.springsource.com', 'x-com-springsource-greenhouse://oauth-response', '80e7f8f7ba724aae9103f297e5fb9bdf', 2);

insert into MemberGroup (name, description, profileKey, hashtag, leader) values ('SpringOne2gx', 'The premier conference series for Spring technologies', 'springone2gx', '#springone2gx', 1);
insert into MemberGroup (name, description, profileKey, hashtag, leader) values ('Atlanta Spring Users Group', 'The Spring developers in the Greater Atlanta area', 'atl', '#atlspring', 1);

insert into Event (title, startTime, endTime, location, description, name, memberGroup) values ('SpringOne/2GX', '2010-10-19 21:00:00Z', '2010-10-22 21:00:00Z', 'Chicago, IL', 'SpringOne 2GX is a one-of-a-kind conference for application developers, solution architects, web operations and IT teams who develop, deploy and manage business applications.', 'chicago', 1);
insert into Event (title, startTime, endTime, location, description, name, memberGroup) values ('Spring Integration 2', '2010-09-21 22:30:00Z', '2010-09-22 00:00:00Z', 'Atlanta, GA', 'Come learn the latest the Spring Integration project has to offer', 'int', 2);

-- SpringOne Chicago 2010

insert into EventTrack (event, code, name, description, chair) values (1, 'spr', 'Essential Spring', 'Spring techniques and technologies applicable to most classes of applications', 2);
insert into EventTrack (event, code, name, description, chair) values (1, 'web', 'Web Application Development', 'What you need to know to build rich web applications', 1);

insert into EventSession (event, number, title, startTime, endTime, description, hashtag, track) values (1, 1, 'Mastering MVC 3', '2010-10-20 16:45:00Z', '2010-10-20 18:15:00Z',
	'A deep-dive into the latest capabilities of Spring MVC, Spring''s REST-ful web application development platform.', '#mvc', 'web');
insert into EventSessionLeader (event, session, leader) values (1, 1, 1);

insert into EventSession (event, number, title, startTime, endTime, description, hashtag, track) values (1, 2, 'Inside Web Flow 3 Development', '2010-10-20 18:45:00Z', '2010-10-20 20:15:00Z',
	'A look inside the development of Spring Web Flow 3, the next-generation version of Spring''s stateful controller framework for orchestrating multi-step user dialogs.', '#webflow', 'web');
insert into EventSessionLeader (event, session, leader) values (1, 2, 1);

insert into EventSession (event, number, title, startTime, endTime, description, hashtag, track) values (1, 3, 'Developing Social-Ready Web Applications', '2010-10-21 16:45:00Z', '2010-10-21 18:15:00Z',
	'Businesses are increasingly recognizing the value of connecting with their customers on a more personal level. Companies can utilize social networking to transition from "Big Faceless Corporation" to "Friend" by taking their wares to the online communities where their customers are. In this age of social media, those communities are found at social network sites such as Facebook, Twitter, and LinkedIn. In this session, you''ll learn how to build Spring-based applications that interact with the various social networks. We''ll talk about new features in the Spring portfolio to support integration with social networks as well as how to start building social features into your own applications.',
	'#social', 'web');
insert into EventSessionLeader (event, session, leader) values (1, 3, 2);
	
insert into EventSession (event, number, title, startTime, endTime, description, hashtag, track) values (1, 4, 'Choices in Mobile Application Development', '2010-10-21 18:45:00Z', '2010-10-21 20:15:00Z',
	'With the rising prevalence of advanced mobile platforms such as iPhone, Android, and Web OS, the desire for rich mobile clients as another means of accessing enterprise services is becoming something that can no longer be ignored. In this session, we will explore the current mobile development landscape and discuss what you as a Spring developer can do to support this increasingly important paradigm. We will examine the benefits and tradeoffs of native mobile client development vs. web-based mobile client development, and we will explore some of the emerging cross-platform options such as PhoneGap. We will look at the various strategies for utilizing a Spring back-end with these mobile platforms, such as consumption of RESTful services, authentication and authorization via OAuth, and server-push style messaging.',
	'#mobile', 'web');
insert into EventSessionLeader (event, session, leader) values (1, 4, 3);
insert into EventSessionLeader (event, session, leader) values (1, 4, 4);

-- Atlanta Spring Users Group September 2010

insert into EventSession (event, number, title, startTime, endTime, description) values (2, 1, 'Spring Integration 2', '2010-09-21 22:30:00Z', '2010-09-22 00:00:00Z', 'Spring Integration 2 inside and out.');
insert into EventSessionLeader (event, session, leader) values (2, 1, 5);