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
	'Spring 3.0 established itself as a powerful basis for next-generation application design. Spring 3.1 adds dedicated conversation management facilities and many further improvements in Spring MVC, as well as explicit support for Servlet 3.0 and Tomcat 7 features. In this session, we will focus on selected key themes of Spring 3.1 in the context of modern web applications.', '#spr3.1', 'spr', 1, 3);
insert into EventSessionLeader (event, session, leader) values (1, 2, 2);


insert into Leader (name) values ('Mike Wiesner');
insert into EventSession (event, id, title, startTime, endTime, description, hashtag, track, venue, room) values (1, 3, 'How to create a secure Spring application?', '2010-10-20 13:30:00Z', '2010-10-20 15:00:00Z',
	'Application Security is more than just authentication and authorization, which is covered by Spring Security. To defend your application from potentials attacks, you also need to think about input validation, bindings, dynamic code invocation, generic interfaces and a lot more. But your code should still stay clean and maintainable. In this session, you will see practical solutions how you can use Spring to solve these issues and also how you can protect your application from future (unknown) attacks', '#sec', 'web', 1, 2);
insert into EventSessionLeader (event, session, leader) values (1, 3, 3);


insert into Leader (name) values ('Steve Mayzak');
insert into EventSession (event, id, title, startTime, endTime, description, hashtag, track, venue, room) values (1, 4, 'Visibility into Spring apps with tc server Spring Edition', '2010-10-20 13:30:00Z', '2010-10-20 15:00:00Z',
	'Steve will present how to use tc Server Spring Edition to gain visibility into your Spring based applications in real-time.  He will explain how tc server builds on the proven base of Tomcat to make it ready for the enterprise.  He will then demonstrate how to instrument an application with the Spring Edition of tc server, deploy it and then showcase real-world examples of what this visibility really means to you.', '#tcspr', 'int', 1, 1);
insert into EventSessionLeader (event, session, leader) values (1, 4, 4);


insert into Leader (name) values ('Justin Murray');
insert into EventSession (event, id, title, startTime, endTime, description, hashtag, track, venue, room) values (1, 5, 'Virtualizing your Java applications', '2010-10-20 13:30:00Z', '2010-10-20 15:00:00Z',
	'In this session, you will learn the key things to consider when you take your application to a virtualization platform, in particular to VMware ESX. The choice of configured memory sizes, virtual CPUs, timekeeping strategies and general virtual machine monitoring will be explored. Best practices will be detailed in each section. An example performance test project will also be shown. ', '#esx', 'emg', 1, 4);
insert into EventSessionLeader (event, session, leader) values (1, 5, 5);

 
insert into Leader (name) values ('Dave Carroll');
insert into EventSession (event, id, title, startTime, endTime, description, hashtag, track, venue, room) values (1, 6, 'Introduction to the Force.com Cloud Platform', '2010-10-20 13:30:00Z', '2010-10-20 15:00:00Z',
	'Force.com provides a full, integrated stack of services that lets you build, test and deploy applications in the cloud. Join this session and learn about Force.com, its database, the novel metadata-driven capabilities that underpin the user interface and web service APIs, and the mobile, collaboration, security and distribution features. This session will cover the SOAP and REST web service APIs for Force.com that give access to operational control of persistence, asynchronous callouts on data changes, and a metadata API to get the data behind the data.', '#force', 'vmw', 1, 5);
insert into EventSessionLeader (event, session, leader) values (1, 6, 6);


insert into Leader (name) values ('Venkat Subramaniam');
insert into EventSession (event, id, title, startTime, endTime, description, hashtag, track, venue, room) values (1, 7, 'Transforming Groovy', '2010-10-20 13:30:00Z', '2010-10-20 15:00:00Z',
	'Groovy is a elegant, dynamic, agile, OO language. I like to program in Groovy because it is fun and the code is concise and highly expressive. Writing code in a language is hardly about using its syntax, however. It is about using the right idioms. Come to this section to pick up some nice Groovy idioms.', '#grvidioms', 'grv', 1, 6);
insert into EventSessionLeader (event, session, leader) values (1, 7, 7);


insert into Leader (name) values ('Guillaume LaForge');
insert into EventSession (event, id, title, startTime, endTime, description, hashtag, track, venue, room) values (1, 8, 'Gaelyk for Google App Engine', '2010-10-20 13:30:00Z', '2010-10-20 15:00:00Z',
	'Guillaume will present Gaelyk, a lightweight Groovy toolkit for easily developing Groovy applications to be deployed on Google App Engine Java. We will learn more about what the toolkit provides, how to leverage it for your own needs through some demos.', '#gaelyk', 'gxe', 1, 7);
insert into EventSessionLeader (event, session, leader) values (1, 8, 8);


insert into Leader (name) values ('Tim Berglund');
insert into EventSession (event, id, title, startTime, endTime, description, hashtag, track, venue, room) values (1, 9, 'Grails in the Real World', '2010-10-20 13:30:00Z', '2010-10-20 15:00:00Z',
	'Grails has enabled your team to achieve productivity levels you never knew possible for web applications on the JVM. Now you have carefully crafted a piece of enterprise software to be deployed in a complex environment including a legacy database, monitoring and logging standards, and pushbutton deployment requirements. You are moving faster than ever, but it still seems harder than the blog-in-15-minutes demo you saw at that user group a couple of months ago. For a framework that sometimes seems biased towards the greenfield installation, this can be a challenge.', '#realgrails', 'gra', 1, 8);
insert into EventSessionLeader (event, session, leader) values (1, 9, 9);


insert into Leader (name) values ('Dave Klein');
insert into EventSession (event, id, title, startTime, endTime, description, hashtag, track, venue, room) values (1, 10, 'Grails: Bringing Rad Productivity to the JVM Part I', '2010-10-20 13:30:00Z', '2010-10-20 15:00:00Z',
	'The goal of this hands-on tutorial is to get started and get productive with Grails. We wil ll do this by jumping right in and building an application, from design to deployment.', '#radgrails', 'grf', 1, 9);
insert into EventSessionLeader (event, session, leader) values (1, 10, 10);


insert into Leader (name) values ('Arjen Poutsma');
insert into Leader (name) values ('David Winterfeldt');
insert into Leader (name) values ('Mark Thomas');
insert into Leader (name) values ('Melanie Spatola');

insert into Leader (name) values ('Ramnivas Laddad');
insert into Leader (name) values ('Quinton Wall');

insert into Leader (name) values ('Hamlet D`Arcy');
insert into Leader (name) values ('Jeff Brown');
insert into Leader (name) values ('Joshua Davis');


insert into Leader (name) values ('Stefan Schmidt');
insert into Leader (name) values ('Andy Clement');

insert into Leader (name, member) values ('Keith Donald', 1);

insert into Leader (name) values ('Swapnil Bawaskar');
insert into Leader (name) values ('Costin Leau');

insert into Leader (name) values ('Alex Heneveld');
insert into Leader (name) values ('Aled Sage');

insert into Leader (name) values ('Reid Carlberg');
insert into Leader (name) values ('Paul King');
insert into Leader (name) values ('Peter Ledbrook');


insert into Leader (name) values ('Christian Dupuis');
insert into Leader (name) values ('Rossen Stoyanchev');
insert into Leader (name) values ('Ari Zilka');

insert into Leader (name) values ('Jon Brisbin');
insert into Leader (name) values ('Rob Woollenn');
insert into Leader (name) values ('Kenneth Kousen');


insert into Leader (name) values ('Mark Fisher');
insert into Leader (name) values ('Oleg Zhurakousky');

insert into Leader (name) values ('Paul Holmes-Higgin');
insert into Leader (name) values ('Scott Andrews');
insert into Leader (name) values ('Jon Travis');
insert into Leader (name) values ('Burt Beckwith');
insert into Leader (name) values ('Graeme Rocher');
insert into Leader (name) values ('Matt Stine');


insert into Leader (name) values ('Adrian Colyer');