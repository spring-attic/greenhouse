create table EventTimeSlot (
	id identity,
	startTime timestamp not null,
	endTime timestamp not null,
	primary key (id)	
);

alter table EventSession add column timeslot bigint;
alter table EventSession add foreign key (timeslot) references EventTimeSlot(id);