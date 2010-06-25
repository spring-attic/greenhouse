drop table if exists NetworkConnection;
drop table if exists User;

create table User (id identity,
                    firstName varchar not null, 
                    lastName varchar not null,
                    email varchar(320) not null unique,
                    password varchar not null,
                    username varchar(15) unique,
                    primary key (id));
                    
create table NetworkConnection (userId bigint not null,
                    network varchar not null,
                    accessToken varchar not null,
                    secret varchar not null,
                    primary key (userId, network),
                    foreign key (userId) references User(id));
                    
insert into User (firstName, lastName, email, password, username) values ('Craig' , 'Walls', 'cwalls@vmware.com', 'plano', 'habuma');

insert into NetworkConnection (userId, network, accessToken, secret) values (1, 'twitter', 'twitterToken', 'twitterTokenSecret');
