create table users(
    id serial primary key,
    email varchar(50),
    username varchar(50),
    password varchar(120),
    is_non_locked boolean,
    description text,
    status varchar(15),
    created_at timestamp 
);
