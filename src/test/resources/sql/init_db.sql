create table users(
	id integer auto_increment primary key,
	username CHARACTER,
	password CHARACTER,
	is_non_locked boolean,
	created_at timestamp,
	email CHARACTER,
	description CHARACTER
);