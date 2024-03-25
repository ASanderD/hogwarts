--Описание структуры: у каждого человека есть машина.
--Причем несколько человек могут пользоваться одной машиной.
--У каждого человека есть имя, возраст и признак того, что у него есть права (или их нет).
--У каждой машины есть марка, модель и стоимость.
--Также не забудьте добавить таблицам первичные ключи и связать их.

create table cars(
	id serial primary key,
	brand varchar(40) not null,
	model varchar(40) not null,
	price numeric not null check (price > 0)
);

create table persons(
	id serial,
	name varchar(40) not null,
	age int2 not null check (age > 18),
	driver_license boolean default false,
	cars_id int references cars(id)
);

