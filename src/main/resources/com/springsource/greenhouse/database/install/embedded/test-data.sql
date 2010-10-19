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

<!-- SpringOne2gx -->
insert into Venue (name, postalAddress, latitude, longitude, locationHint, createdBy) values ('Westin Lombard Yorktown Center', '70 Yorktown Center Lombard, IL 60148', 41.8751108905486, -88.0184300761646, 'adjacent to Shopping Center', 1);
insert into VenueRoom (venue, id, name, capacity, locationHint) values (1, 1, 'Junior Ballroom A', 150, 'first floor');
insert into VenueRoom (venue, id, name, capacity, locationHint) values (1, 2, 'Junior Ballroom B', 150, 'first floor');
insert into VenueRoom (venue, id, name, capacity, locationHint) values (1, 3, 'Junior Ballroom C', 150, 'first floor');
insert into VenueRoom (venue, id, name, capacity, locationHint) values (1, 4, 'Lilac C', 100, 'first floor');
insert into VenueRoom (venue, id, name, capacity, locationHint) values (1, 5, 'Lilac D', 100, 'first floor');
insert into VenueRoom (venue, id, name, capacity, locationHint) values (1, 6, 'Harry Caray Balroom C', 200, 'first floor');
insert into VenueRoom (venue, id, name, capacity, locationHint) values (1, 7, 'Harry Caray Balroom D', 200, 'first floor');
insert into VenueRoom (venue, id, name, capacity, locationHint) values (1, 8, 'Magnolia', 50, 'first floor');
insert into VenueRoom (venue, id, name, capacity, locationHint) values (1, 9, 'Cypress', 50, 'first floor');
insert into VenueRoom (venue, id, name, capacity, locationHint) values (1, 10, 'Ballroom', 600, 'first floor');

insert into MemberGroup (name, slug, description, hashtag, leader) values ('SpringOne2gx', 's2gx', 'The premier conference series for Spring technologies', '#s2gx', 1);

insert into Event (title, timeZone, startTime, endTime, slug, description, memberGroup) values ('SpringOne2gx', 'America/Chicago', '2010-10-19 22:00:00Z', '2010-10-22 22:00:00Z', 'chicago', 'SpringOne 2GX is a one-of-a-kind conference for application developers, solution architects, web operations and IT teams who develop, deploy and manage business applications.', 1);
insert into EventVenue (event, venue) values (1, 1);

insert into EventTrack (event, code, name, description, chair) values (1, 'spr', 'Essential Spring', 'Spring techniques and technologies applicable to most classes of applications', 1);
insert into EventTrack (event, code, name, description, chair) values (1, 'web', 'Web Application Development', 'What you need to know to build modern web applications', 1);
insert into EventTrack (event, code, name, description, chair) values (1, 'int', 'Enterprise Production Systems', 'Enterprise integration and large-scale system architectures', 1);
insert into EventTrack (event, code, name, description, chair) values (1, 'emg', 'Emerging technology', 'Covers hot topics and trends in application development', 1);
insert into EventTrack (event, code, name, description, chair) values (1, 'vmw', 'VMforce', 'Developing for the cloud with VMforce', 1);
insert into EventTrack (event, code, name, description, chair) values (1, 'grv', 'Groovy In-depth', 'Inside the dynamic language for the Java VM', 1);
insert into EventTrack (event, code, name, description, chair) values (1, 'gxe', 'Groovy Ecosystem', 'Exploring the Groovy ecosystem', 1);
insert into EventTrack (event, code, name, description, chair) values (1, 'gra', 'Grails In-depth', 'Inside the rapid web application development framework', 1);
insert into EventTrack (event, code, name, description, chair) values (1, 'grf', 'Grails and Griffon', 'Combining Griffon and Grails for rapid desktop development', 1);

insert into Leader (name) values ('Rod Johnson');

insert into EventSession (event, id, title, startTime, endTime, description, hashtag, track, venue, room) values (1, 1, 'Opening Keynote', '2010-10-20 00:30:00Z', '2010-10-20 01:45:00Z',
	'Rod kicks off #s2gx with a bang.', '#opener', null, 1, 10);
insert into EventSessionLeader (event, session, leader) values (1, 1, 1);


insert into Leader (name) values ('Juergen Hoeller');

insert into EventSession (event, id, title, startTime, endTime, description, hashtag, track, venue, room) values (1, 2, 'What''s new in Spring Framework 3.1?', '2010-10-20 13:30:00Z', '2010-10-20 15:00:00Z',
	'Spring 3.0 established itself as a powerful basis for next-generation application design. Spring 3.1 adds dedicated conversation management facilities and many further improvements in Spring MVC, as well as explicit support for Servlet 3.0 and Tomcat 7 features. In this session, we will focus on selected key themes of Spring 3.1 in the context of modern web applications.',
	'#spr3.1', 'spr', 1, 3);
insert into EventSessionLeader (event, session, leader) values (1, 2, 2);


insert into Leader (name) values ('Mike Wiesner');
insert into EventSession (event, id, title, startTime, endTime, description, hashtag, track, venue, room) values (1, 3, 'How to create a secure Spring application?', '2010-10-20 13:30:00Z', '2010-10-20 15:00:00Z',
	'Application Security is more than just authentication and authorization, which is covered by Spring Security. To defend your application from potentials attacks, you also need to think about input validation, bindings, dynamic code invocation, generic interfaces and a lot more. But your code should still stay clean and maintainable. In this session, you will see practical solutions how you can use Spring to solve these issues and also how you can protect your application from future (unknown) attacks',
	'#sec', 'web', 1, 2);
insert into EventSessionLeader (event, session, leader) values (1, 3, 3);


insert into Leader (name) values ('Steve Mayzak');
insert into EventSession (event, id, title, startTime, endTime, description, hashtag, track, venue, room) values (1, 4, 'Visibility into Spring apps with tc server Spring Edition', '2010-10-20 13:30:00Z', '2010-10-20 15:00:00Z',
	'Steve will present how to use tc Server Spring Edition to gain visibility into your Spring based applications in real-time.  He will explain how tc server builds on the proven base of Tomcat to make it ready for the enterprise.  He will then demonstrate how to instrument an application with the Spring Edition of tc server, deploy it and then showcase real-world examples of what this visibility really means to you.',
	'#tcspr', 'int', 1, 1);
insert into EventSessionLeader (event, session, leader) values (1, 4, 4);


insert into Leader (name) values ('Justin Murray');
insert into EventSession (event, id, title, startTime, endTime, description, hashtag, track, venue, room) values (1, 5, 'Virtualizing your Java applications', '2010-10-20 13:30:00Z', '2010-10-20 15:00:00Z',
	'In this session, you will learn the key things to consider when you take your application to a virtualization platform, in particular to VMware ESX. The choice of configured memory sizes, virtual CPUs, timekeeping strategies and general virtual machine monitoring will be explored. Best practices will be detailed in each section. An example performance test project will also be shown. ',
	'#esx', 'emg', 1, 4);
insert into EventSessionLeader (event, session, leader) values (1, 5, 5);

 
insert into Leader (name) values ('Dave Carroll');
insert into EventSession (event, id, title, startTime, endTime, description, hashtag, track, venue, room) values (1, 6, 'Introduction to the Force.com Cloud Platform', '2010-10-20 13:30:00Z', '2010-10-20 15:00:00Z',
	'Force.com provides a full, integrated stack of services that lets you build, test and deploy applications in the cloud. Join this session and learn about Force.com, its database, the novel metadata-driven capabilities that underpin the user interface and web service APIs, and the mobile, collaboration, security and distribution features. This session will cover the SOAP and REST web service APIs for Force.com that give access to operational control of persistence, asynchronous callouts on data changes, and a metadata API to get the data behind the data.',
	'#force', 'vmw', 1, 5);
insert into EventSessionLeader (event, session, leader) values (1, 6, 6);


insert into Leader (name) values ('Venkat Subramaniam');
insert into EventSession (event, id, title, startTime, endTime, description, hashtag, track, venue, room) values (1, 7, 'Transforming Groovy', '2010-10-20 13:30:00Z', '2010-10-20 15:00:00Z',
	'Groovy is a elegant, dynamic, agile, OO language. I like to program in Groovy because it is fun and the code is concise and highly expressive. Writing code in a language is hardly about using its syntax, however. It is about using the right idioms. Come to this section to pick up some nice Groovy idioms.',
	'#grvidioms', 'grv', 1, 6);
insert into EventSessionLeader (event, session, leader) values (1, 7, 7);


insert into Leader (name) values ('Guillaume LaForge');
insert into EventSession (event, id, title, startTime, endTime, description, hashtag, track, venue, room) values (1, 8, 'Gaelyk for Google App Engine', '2010-10-20 13:30:00Z', '2010-10-20 15:00:00Z',
	'Guillaume will present Gaelyk, a lightweight Groovy toolkit for easily developing Groovy applications to be deployed on Google App Engine Java. We will learn more about what the toolkit provides, how to leverage it for your own needs through some demos.',
	'#gaelyk', 'gxe', 1, 7);
insert into EventSessionLeader (event, session, leader) values (1, 8, 8);


insert into Leader (name) values ('Tim Berglund');
insert into EventSession (event, id, title, startTime, endTime, description, hashtag, track, venue, room) values (1, 9, 'Grails in the Real World', '2010-10-20 13:30:00Z', '2010-10-20 15:00:00Z',
	'Grails has enabled your team to achieve productivity levels you never knew possible for web applications on the JVM. Now you have carefully crafted a piece of enterprise software to be deployed in a complex environment including a legacy database, monitoring and logging standards, and pushbutton deployment requirements. You are moving faster than ever, but it still seems harder than the blog-in-15-minutes demo you saw at that user group a couple of months ago. For a framework that sometimes seems biased towards the greenfield installation, this can be a challenge.',
	'#realgrails', 'gra', 1, 8);
insert into EventSessionLeader (event, session, leader) values (1, 9, 9);


insert into Leader (name) values ('Dave Klein');
insert into EventSession (event, id, title, startTime, endTime, description, hashtag, track, venue, room) values (1, 10, 'Grails: Bringing Rad Productivity to the JVM Part I', '2010-10-20 13:30:00Z', '2010-10-20 15:00:00Z',
	'The goal of this hands-on tutorial is to get started and get productive with Grails. We wil ll do this by jumping right in and building an application, from design to deployment.',
	'#radgrails', 'grf', 1, 9);
insert into EventSessionLeader (event, session, leader) values (1, 10, 10);


insert into Leader (name) values ('Arjen Poutsma');
insert into EventSession (event, id, title, startTime, endTime, description, hashtag, track, venue, room) values (1, 11, 'What''s new in Spring-WS 2.0?', '2010-10-20 15:15:00Z', '2010-10-20 16:45:00Z',
	'In this session, Arjen will take a look at the new features found in Spring Web Services 2.0. We will look at the improved @Endpoint programming model, Java 5+ support, the new Web service testing module, and more.',
	'#sws2', 'spr', 1, 3);
insert into EventSessionLeader (event, session, leader) values (1, 11, 11);
 

insert into Leader (name) values ('David Winterfeldt');
insert into EventSession (event, id, title, startTime, endTime, description, hashtag, track, venue, room) values (1, 12, 'Killer Flex RIAs with Spring ActionScript', '2010-10-20 15:15:00Z', '2010-10-20 16:45:00Z',
	'A hands on approach to developing a Flex application using Spring ActionScript with Adobe Cairngorm, remoting, annotation based autowiring, etc. Spring BlazeDS Integration will be used on the backend, but the focus will be developing the client side application.',
	'#flex', 'web', 1, 2);
insert into EventSessionLeader (event, session, leader) values (1, 12, 12);


insert into Leader (name) values ('Mark Thomas');
insert into EventSession (event, id, title, startTime, endTime, description, hashtag, track, venue, room) values (1, 13, 'Clustering and load-balancing tc Server / httpd', '2010-10-20 15:15:00Z', '2010-10-20 16:45:00Z',
	'When an application reaches production, the requirements for scalability and/or availability usually mean that some form of load-balancing or clustering is employed. The process of setting up a cluser is complicated by the fact that a small configuration error can have a major impact such as every request resulting in an infinite re-direct, unexpected components appearing in URLs, loss of user sessions and so on. This session will take you through the process of setting up and testing a cluster of tc Servers, highlighting the common pitfalls and explaining how to diagnose cluster configuration issues when they occur.',
	'#cluster', 'int', 1, 1);
insert into EventSessionLeader (event, session, leader) values (1, 13, 13);


insert into Leader (name) values ('Melanie Spatola');
insert into EventSession (event, id, title, startTime, endTime, description, hashtag, track, venue, room) values (1, 14, 'Devs to Ops: Monitoring/Management in the Real World with Hyperic', '2010-10-20 15:15:00Z', '2010-10-20 16:45:00Z',
	'This panel discussion will cover practical information for monitoring and managing production environments using VMware vFabric Hyperic HQ. The panelists will provide short case studies detailing their successful use of Hyperic and describe the direct benefits gained. The discussion portion of the session will cover real-world tips and tricks from users responsible for running Hyperic in mission critical systems.',
	'#hyperic', 'emg', 1, 4);
insert into EventSessionLeader (event, session, leader) values (1, 14, 14);


insert into Leader (name) values ('Ramnivas Laddad');
insert into Leader (name) values ('Quinton Wall');
insert into EventSession (event, id, title, startTime, endTime, description, hashtag, track, venue, room) values (1, 15, 'Application Development with VMforce.com', '2010-10-20 15:15:00Z', '2010-10-20 16:45:00Z',
	'With the announcement of VMforce, VMware and Salesforce are building an enterprise Java Cloud that combines the Spring programming model with VMware flexibility and Force.com reliability and data integrity. This session will discuss the architecture of this new Platform-as-a-Service solution and provide a technical overview of how to run Spring applications that integrate with Force.com services. Topics will include local development, basic Force.com database support, workflow and approval, compile and debug, deployment and management, as well as service utilizations.',
	'#forcedev', 'vmw', 1, 5);
insert into EventSessionLeader (event, session, leader, rank) values (1, 15, 15, 1);
insert into EventSessionLeader (event, session, leader, rank) values (1, 15, 16, 2);


insert into Leader (name) values ('Hamlet D`Arcy');
insert into EventSession (event, id, title, startTime, endTime, description, hashtag, track, venue, room) values (1, 16, 'Slimmed Down Software: A Lean, Groovy Approach', '2010-10-20 15:15:00Z', '2010-10-20 16:45:00Z',
	'The Groovy Programming Language advertises itself as an "agile and dynamic Language for the JVM", but what does this mean exactly? This session explains Lean Software Development, and shows how your choice of programming language can help your entire process remain nimble and adaptive.',
	'#slim', 'grv', 1, 6);
insert into EventSessionLeader (event, session, leader) values (1, 16, 17);


insert into Leader (name) values ('Jeff Brown');
insert into EventSession (event, id, title, startTime, endTime, description, hashtag, track, venue, room) values (1, 17, 'GORM Inside And Out', '2010-10-20 15:15:00Z', '2010-10-20 16:45:00Z',
	'GORM is a super powerful ORM tool that makes ORM simple by leveraging the flexibility and expressiveness of a dynamic language like Groovy.  With GORM developers get access to all of the power and flexibility of an ORM tool like Hibernate without any of the complexity.',
	'#gorm', 'gxe', 1, 7);
insert into EventSessionLeader (event, session, leader) values (1, 17, 18);


insert into Leader (name) values ('Joshua Davis');
insert into EventSession (event, id, title, startTime, endTime, description, hashtag, track, venue, room) values (1, 18, 'Creating a Dynamic Portal using Grails', '2010-10-20 15:15:00Z', '2010-10-20 16:45:00Z',
	'This session will describe hands-on procedure to use the Grails Portal project as a launching pad for quickly creating web applications. The first part of the session will be spent discussing the advantages that Grails gives to developing User Portals.  The second part will have a detailed discussion and examples of integrating Apache Shiro security with Grails Web Flow and the reasoning behind selecting these plug-ins. The session will conclude with a discussion on how these features can be extended to create a fully dynamic portal with personalization and other popular plug-ins.',
	'#portal', 'gra', 1, 8);
insert into EventSessionLeader (event, session, leader) values (1, 18, 19);


insert into EventSession (event, id, title, startTime, endTime, description, hashtag, track, venue, room) values (1, 19, 'Grails: Bringing Rad Productivity to the JVM Part II', '2010-10-20 15:15:00Z', '2010-10-20 16:45:00Z',
	'In Part II of this session, we will continue the build out process with the Grails application.',
	'#radgrails2', 'grf', 1, 9);
insert into EventSessionLeader (event, session, leader) values (1, 19, 10);


insert into Leader (name) values ('Stefan Schmidt');
insert into Leader (name) values ('Andy Clement');
insert into EventSession (event, id, title, startTime, endTime, description, hashtag, track, venue, room) values (1, 20, 'Introduction to Spring Roo', '2010-10-20 17:45:00Z', '2010-10-20 19:15:00Z',
	'Delight your customers and impress your colleagues by delivering enterprise Spring applications faster than ever before. In this session we will introduce Spring Roo, an open source tool that makes it easy to build applications using the Java language, standards and technologies you already know. We will also show you an exciting new feature which lets you update running Java applications without restarting the server and the considerable time-savings that this provides.',
	'#roo', 'spr', 1, 3);
insert into EventSessionLeader (event, session, leader, rank) values (1, 20, 20, 1);
insert into EventSessionLeader (event, session, leader, rank) values (1, 20, 21, 2);


insert into Leader (name, member) values ('Keith Donald', 1);
insert into EventSession (event, id, title, startTime, endTime, description, hashtag, track, venue, room) values (1, 21, 'Mastering MVC 3', '2010-10-20 17:45:00Z', '2010-10-20 19:15:00Z',
	'A deep-dive into the latest capabilities of Spring MVC, Spring''s REST-ful web application development platform.',
	'#mvc', 'web', 1, 2);
insert into EventSessionLeader (event, session, leader) values (1, 21, 22);


insert into Leader (name) values ('Swapnil Bawaskar');
insert into Leader (name) values ('Costin Leau');
insert into EventSession (event, id, title, startTime, endTime, description, hashtag, track, venue, room) values (1, 22, 'Improve the performance of your Spring app', '2010-10-20 17:45:00Z', '2010-10-20 19:15:00Z',
	'A pragmatic look on how to achieve easy gains in a Spring application through caching. This session will analyze the usual bottlenecks found in common application stacks and ways to address them. Various caching patterns will be discussed, with focus not just on performance but also scalability.',
	'#cache', 'int', 1, 1);
insert into EventSessionLeader (event, session, leader, rank) values (1, 22, 23, 1);
insert into EventSessionLeader (event, session, leader, rank) values (1, 22, 24, 2);


insert into Leader (name) values ('Alex Heneveld');
insert into Leader (name) values ('Aled Sage');
insert into EventSession (event, id, title, startTime, endTime, description, hashtag, track, venue, room) values (1, 23, 'Build/run/manage elastic applications with Monterey', '2010-10-20 17:45:00Z', '2010-10-20 19:15:00Z',
	'In this session we introduce the Monterey Spring Edition, a powerful middleware platform enabling high performance, adaptive cloud applications targeted at the Spring developer. As well as providing an overview of its core capabilities, we highlight Monterey''s integration with STS to provide a comprehensive PaaS offering enabling the development, deployment and runtime management of policy-driven elastic applications that can span the globe. Using its powerful STS extensions we build, run and manage an elastic booking service in our Monterey Cloud &amp; refactor Spring Travel to access this service. Anyone following this who successfully books a room at the Hotel Monterey wins a mystery prize.',
	'#monterey', 'emg', 1, 4);
insert into EventSessionLeader (event, session, leader, rank) values (1, 23, 25, 1);
insert into EventSessionLeader (event, session, leader, rank) values (1, 23, 26, 2);


insert into Leader (name) values ('Reid Carlberg');
insert into EventSession (event, id, title, startTime, endTime, description, hashtag, track, venue, room) values (1, 24, 'Understanding Data Access in VMforce.com', '2010-10-20 17:45:00Z', '2010-10-20 19:15:00Z',
	'All enterprise developers know that the data access layer of the application has the potential to be a critical bottleneck for performance and a limiting factor on application design. This session will discuss the details behind the full set of data access techniques available in the VMforce cloud platform including an in depth discussion of the JPA implementation, Spring Roo tooling and best practices for high performance cloud applications.',
	'#forcedao', 'vmw', 1, 5);
insert into EventSessionLeader (event, session, leader, rank) values (1, 24, 15, 1);
insert into EventSessionLeader (event, session, leader, rank) values (1, 24, 27, 2);


insert into EventSession (event, id, title, startTime, endTime, description, hashtag, track, venue, room) values (1, 25, 'Improving your Groovy Code Quality', '2010-10-20 17:45:00Z', '2010-10-20 19:15:00Z',
	'Groovy is concise and expressive. However, writing good quality code takes effort and discipline. Come to this session to learn about good coding styles, ways to observe, and measure the quality of your Groovy code. We will take several Groovy code examples, identify smells in them, measure and refactor to improve the quality.',
	'#gqm', 'grv', 1, 6);
insert into EventSessionLeader (event, session, leader) values (1, 25, 7);


insert into Leader (name) values ('Paul King');
insert into EventSession (event, id, title, startTime, endTime, description, hashtag, track, venue, room) values (1, 26, 'Acceptance Testing with Groovy', '2010-10-20 17:45:00Z', '2010-10-20 19:15:00Z',
	'An ideal way to make use of Groovy''s great support for writing domain specific languages (DSLs) is when writing customer or acceptance tests. When using such an approach you typically use a high level English-like testing DSL to express the test. The approach is so popular that in fact numerous frameworks now exist for creating such tests.',
	'#spock', 'gxe', 1, 7);
insert into EventSessionLeader (event, session, leader) values (1, 26, 28);


insert into Leader (name) values ('Peter Ledbrook');
insert into EventSession (event, id, title, startTime, endTime, description, hashtag, track, venue, room) values (1, 27, 'Tuning Grails applications', '2010-10-20 17:45:00Z', '2010-10-20 19:15:00Z',
	'Grails makes it incredibly easy to get a web application up and running, but it makes no guarantees about how well that application will perform and scale. If you issue hundreds of database queries per request, your application won''t be a Speedy Gonzalez. In this session, find out how to track down and fix bottlenecks in various parts of your application, particularly database access and view rendering.',
	'#gtune', 'gra', 1, 8);
insert into EventSessionLeader (event, session, leader) values (1, 27, 29);


insert into EventSession (event, id, title, startTime, endTime, description, hashtag, track, venue, room) values (1, 28, 'Extending Grails with the Plugin System', '2010-10-20 17:45:00Z', '2010-10-20 19:15:00Z',
	'Grails is a super powerful framework for building applications for the Java platform.  Grails addresses the pain points that the other web applications frameworks leave you with.  This session covers the details you need to further extend Grails to help Grails help you in your environment.',
	'#gplug', 'grf', 1, 9);
insert into EventSessionLeader (event, session, leader) values (1, 28, 18);


insert into Leader (name) values ('Christian Dupuis');
insert into EventSession (event, id, title, startTime, endTime, description, hashtag, track, venue, room) values (1, 29, 'Developer Tools to Push your Productivity', '2010-10-20 19:45:00Z', '2010-10-20 21:15:00Z',
	'Spring started off to greatly increase the developer productivity; and as you all know successfully delivered on that promise. But there is more SpringSource can do to make your life as a developer more productive. In this session, we will demo current state-of-art developer tools for Spring, Roo, Groovy and Grails. We will explain how these free tools can help you along the build-run-manage lifecycle of your application and prepare you for the cloud adventure.',
	'#tools', 'spr', 1, 3);
insert into EventSessionLeader (event, session, leader, rank) values (1, 29, 21, 1);
insert into EventSessionLeader (event, session, leader, rank) values (1, 29, 30, 2);


insert into Leader (name) values ('Rossen Stoyanchev');
insert into EventSession (event, id, title, startTime, endTime, description, hashtag, track, venue, room) values (1, 30, 'RIAs with Spring Web Flow and JSF 2', '2010-10-20 19:45:00Z', '2010-10-20 21:15:00Z',
	'It''s been a year since the Sun Mojarra JSF 2 runtime became available.  There is no lack of blogs and articles on what''s new and noteworthy. In this session you''ll learn what is important from a Spring developer''s point of view:',
	'#webflow', 'web', 1, 2);
insert into EventSessionLeader (event, session, leader) values (1, 30, 31);


insert into Leader (name) values ('Ari Zilka');
insert into EventSession (event, id, title, startTime, endTime, description, hashtag, track, venue, room) values (1, 31, 'Using Ehcache to overcome JVM gc limitations', '2010-10-20 19:45:00Z', '2010-10-20 21:15:00Z',
	'Many architects hope the G1 and concurrentmarksweep garbage collectors can help Java applications run optimally.  The problem: they randomly pause the applications &ndash; and as the heap gets bigger, the pauses grow from seconds to minutes. This talk will explore garbage collection in JVMs, the challenges of generic collectors, and how Ehcache solves the problem by managing the cache, figuring out what to keep and toss out, and hiding the cache''s contents from the collector.  We will also consider the implications of Java memory management on scaling. And we will demo a huge JVM running with its cache as heap objects versus letting Ehcache manage that same memory while still keeping the data inside the JVM.',
	'#ehcache', 'int', 1, 1);
insert into EventSessionLeader (event, session, leader) values (1, 31, 32);


insert into Leader (name) values ('Jon Brisbin');
insert into EventSession (event, id, title, startTime, endTime, description, hashtag, track, venue, room) values (1, 32, 'The Private Cloud: Amazon, Google ...and You!', '2010-10-20 19:45:00Z', '2010-10-20 21:15:00Z',
	'Public clouds like Amazon EC2 and Google AppEngine have revolutionized computing and application development. But what if you already have a bunch of servers in an air-conditioned room sucking City Power like angry beavers and you want to take advantage of the economies of scale and the efficiency of virtualization that make the big clouds work, but do it in your own data center? It''s not trivial, but you can host a private cloud in your own data center and your organization can benefit from transitioning your internal development to a PaaS or SaaS architecture. This session will be an end-to-end outline of creating a private cloud.',
	'#pcloud', 'emg', 1, 4);
insert into EventSessionLeader (event, session, leader) values (1, 32, 33);


insert into Leader (name) values ('Rob Woollenn');
insert into EventSession (event, id, title, startTime, endTime, description, hashtag, track, venue, room) values (1, 33, 'Inside the Force.com Cloud Platform', '2010-10-20 19:45:00Z', '2010-10-20 21:15:00Z',
	'To truly understand the sophistication and potential of the Force.com platform we are going to peek under the kimono in this session. Join salesforce.com principal architect, Rob Woollen, as he discusses the Force.com multi-tenant architecture implementation. You’ll learn the details of how the Force.com platform maintains high performance and scalability for over 80,000 customers and thousands of cloud applications.',
	'#inforce', 'vmw', 1, 5);
insert into EventSessionLeader (event, session, leader) values (1, 33, 34);


insert into Leader (name) values ('Kenneth Kousen');
insert into EventSession (event, id, title, startTime, endTime, description, hashtag, track, venue, room) values (1, 34, 'Groovy Web Services, Part I: REST', '2010-10-20 19:45:00Z', '2010-10-20 21:15:00Z',
	'Groovy has excellent networking capabilities and is great at processing XML, which makes it a natural for working with RESTful web services.',
	'#gws', 'grv', 1, 6);
insert into EventSessionLeader (event, session, leader) values (1, 34, 35);


insert into EventSession (event, id, title, startTime, endTime, description, hashtag, track, venue, room) values (1, 35, 'Groovy.DSLs(from: beginner, to: expert)', '2010-10-20 19:45:00Z', '2010-10-20 21:15:00Z',
	'There have been many attempts to create languages which allow us to express our problems in higher-level languages: from COBOL to Object-Oriented languages, from Logic processing languages and SQL to rules engines. All have taken us forward in leaps and bounds but have failed to get very close to the language of the subject matter expert.',
	'#dsl', 'gxe', 1, 7);
insert into EventSessionLeader (event, session, leader) values (1, 35, 28);


insert into EventSession (event, id, title, startTime, endTime, description, hashtag, track, venue, room) values (1, 36, 'Grails Integration Strategies ', '2010-10-20 19:45:00Z', '2010-10-20 21:15:00Z',
	'It''s amazing how quickly one can build web applications with Grails in a greenfield environment, but most of us do not have that luxury. We have existing infrastructure and applications that we have to maintain and extend. We have legacy databases (or legacy database administrators) to deal with. Does this mean we cannot benefit from the magic of Grails?  No way! The ease of use and productivity of Grails is matched by its power and flexibility. In this session we will discuss some of the ways that Grails can be integrated with the enterprise; EJB/JSF applications, Spring/Hibernate, legacy databases, and even non-Java applications.',
	'#gint', 'gra', 1, 8);
insert into EventSessionLeader (event, session, leader) values (1, 36, 10);


insert into EventSession (event, id, title, startTime, endTime, description, hashtag, track, venue, room) values (1, 37, 'Grails Without SQL', '2010-10-20 19:45:00Z', '2010-10-20 21:15:00Z',
	'Out of the box, Grails famously relies on Hibernate for database persistence through the agency of GORM, the Grails Object-Relational Mapping API. But are Grails apps permanently beholden to relational datastores, even when the relational model is not an appropriate solution for the problem at hand? No!',
	'#nosql', 'grf', 1, 9);
insert into EventSessionLeader (event, session, leader) values (1, 37, 9);


insert into Leader (name) values ('Mark Fisher');
insert into Leader (name) values ('Oleg Zhurakousky');
insert into EventSession (event, id, title, startTime, endTime, description, hashtag, track, venue, room) values (1, 38, 'What''s new in Spring Integration 2.0?', '2010-10-20 21:30:00Z', '2010-10-20 23:00:00Z',
	'Mark and Oleg will provide a guided tour of the new features of Spring Integration 2.0 which would include the unveiling of Spring Integration ROO add-on. Along the way, you will learn about Spring Integration''s support for Spring Framework 3.0 features such as the Spring Expression Language, ConversionService, and RestTemplate. You will also learn about several new adapters including AMQP, XMPP, TCP/UDP, JDBC, JMX, and more.',
	'#int2', 'spr', 1, 3);
insert into EventSessionLeader (event, session, leader, rank) values (1, 38, 36, 1);
insert into EventSessionLeader (event, session, leader, rank) values (1, 38, 37, 2);


insert into Leader (name) values ('Paul Holmes-Higgin');
insert into EventSession (event, id, title, startTime, endTime, description, hashtag, track, venue, room) values (1, 39, 'Building Content Rich Applications with Spring MVC', '2010-10-20 21:30:00Z', '2010-10-20 23:00:00Z',
	'Building Content Rich Applications with Spring MVC',
	'#content', 'web', 1, 2);
insert into EventSessionLeader (event, session, leader) values (1, 39, 38);


insert into Leader (name) values ('Scott Andrews');
insert into Leader (name) values ('Jon Travis');
insert into EventSession (event, id, title, startTime, endTime, description, hashtag, track, venue, room) values (1, 40, 'Diagnosing Performance Issues with Spring Insight before it''s a Problem', '2010-10-20 21:30:00Z', '2010-10-20 23:00:00Z',
	'What if a user reports your application is slow? At that point, it’s too late. Runtime performance is more than an ops team’s concern. Continuous performance profiling is an important part of the agile developer’s bag of tools. Spring Insight enables developers to watch what their application is actually doing in real time and zero in on performance issues. Come to this session to learn about Spring Insight from its creators. Learn how to easily enhance the default instrumentation to gain even greater visibility into your application. ',
	'#insight', 'int', 1, 1);
insert into EventSessionLeader (event, session, leader, rank) values (1, 40, 39, 2);
insert into EventSessionLeader (event, session, leader, rank) values (1, 40, 40, 1);


insert into EventSession (event, id, title, startTime, endTime, description, hashtag, track, venue, room) values (1, 41, 'Hello, RooBot: Writing and Distributing Your Own Spring Roo Add-Ons.', '2010-10-20 21:30:00Z', '2010-10-20 23:00:00Z',
	'One of the most exciting improvements in Spring Roo 1.1 is the addition of a powerful OSGi-based add-on discovery and distribution feature. This new feature allows anyone to write Spring Roo add-ons and have them immediately and easily made available to the entire Spring Roo community. In this session we will introduce RooBot, the automatic provisioning server which underpins this new feature. We'll then write a Spring Roo add-on and make it immediately available to all the Spring Roo installations on attendee laptops. Also in this session we'll explore some of the architectural background necessary to write add-ons, plus offer practical advice and time-saving hints for those wanting to extend Spring Roo into new capability areas.',
	'#roobot', 'emg', 1, 4);
insert into EventSessionLeader (event, session, leader) values (1, 41, 20);


insert into EventSession (event, id, title, startTime, endTime, description, hashtag, track, venue, room) values (1, 42, 'Special Topic: Lightning VMforce Overview', '2010-10-20 21:30:00Z', '2010-10-20 23:00:00Z',
	'In this lightning session, developers will learn all the essential elements necessary to use the VMforce enterprise Java cloud application platform. The session will cover basic project configuration, Force.com database support, connectivity to Force.com services, integration with the Spring framework including a quick tour of the Spring Roo add-on for VMforce. If developers can only make it to one VMforce conference session, then this Lightning Overview will give them all the information they need to get started.',
	'#lightning', 'vmw', 1, 5);
insert into EventSessionLeader (event, session, leader) values (1, 42, 16);


insert into EventSession (event, id, title, startTime, endTime, description, hashtag, track, venue, room) values (1, 43, 'Groovy Web Services, Part II: SOAP and JAX-WS', '2010-10-20 21:30:00Z', '2010-10-20 23:00:00Z',
	'Although RESTful web services have gotten better press, SOAP-based web services are often the backbone of many large enterprises.  The user-friendly advances in JAX-WS 2.* make developing such services much easier. As with most Java topics, Groovy simplifies the development of web services as well.  Since it is particularly well-suited to XML processing, Groovy is quite helpful in the web services and SOA worlds.',
	'#gws2', 'grv', 1, 6);
insert into EventSessionLeader (event, session, leader) values (1, 43, 35);


insert into Leader (name) values ('Burt Beckwith');
insert into EventSession (event, id, title, startTime, endTime, description, hashtag, track, venue, room) values (1, 44, 'Advanced GORM - Performance, Customization and Monitoring ', '2010-10-20 21:30:00Z', '2010-10-20 23:00:00Z',
	'You''ve used GORM in Grails apps, you''ve written custom criteria and HQL queries, and now you''re ready to take database access in Grails to the next level.',
	'#gorm2', 'gxe', 1, 7);
insert into EventSessionLeader (event, session, leader) values (1, 44, 41);


insert into Leader (name) values ('Graeme Rocher');
insert into EventSession (event, id, title, startTime, endTime, description, hashtag, track, venue, room) values (1, 45, 'Grails 1.3 Update', '2010-10-20 21:30:00Z', '2010-10-20 23:00:00Z',
	'Grails provides a dynamic and agile development framework for web applications that is based on the most stable and productive open source libraries, including the Spring framework. ',
	'#grails13', 'gra', 1, 8);
insert into EventSessionLeader (event, session, leader) values (1, 45, 42);


insert into Leader (name) values ('Matt Stine');
insert into EventSession (event, id, title, startTime, endTime, description, hashtag, track, venue, room) values (1, 46, 'Building SOFEA''s with Grails and YUI', '2010-10-20 21:30:00Z', '2010-10-20 23:00:00Z',
	'The Service-Oriented Front-End Architecture (SOFEA) was first described in an article posted to The Server Side in late 2007. In that article, Ganesh Prasad, Rajat Taneja and Vikrant Todankar introduced a new architectural style for building web applications. Since that time numerous frameworks have begun to facilitate building applications in this style. This talk will focus on the combination of Grails and the Yahoo! User Interface Library for building SOFEA applications.',
	'#sofea', 'grf', 1, 9);
insert into EventSessionLeader (event, session, leader) values (1, 46, 43);


insert into Leader (name) values ('Adrian Colyer');
insert into EventSession (event, id, title, startTime, endTime, description, hashtag, track, venue, room) values (1, 47, 'Technical Keynote', '2010-10-21 00:30:00Z', '2010-10-21 01:45:00Z',
	'Adrian shares all the exciting technologial work happening in the Spring community.',
	'#keynote', null, 1, 10);
insert into EventSessionLeader (event, session, leader) values (1, 47, 44);
