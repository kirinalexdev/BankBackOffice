create table users
(
    id serial primary key,
    password varchar(255),
    username varchar(100),
    enabled boolean
);

alter table users owner to postgres;

create table authorities
(
    id serial primary key,
    name varchar(100)
);

alter table authorities
    owner to postgres;

create table users_authorities
(
    user_id integer not null
        constraint fkq3lq694rr66e6kpo2h84ad92q
            references users,
    authority_id integer not null
        constraint fkdsfxx5g8x8mnxne1fe0yxhjhq
            references authorities
);

alter table users_authorities owner to postgres;

