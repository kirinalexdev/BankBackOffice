create table employees
(
    id serial primary key,
    first_name varchar(255) not null,
    last_name  varchar(255) not null,
    birthday   timestamp    not null
);

alter table employees
    owner to postgres;

create table contacts
(
    id  serial primary key,
    value varchar(255) not null,
    employee_id integer
        constraint fkahgr65l45anwaeoft98neymad
            references employees,
    type        varchar(10)
);

alter table contacts
    owner to postgres;

create table card_orders
(
    id serial primary key,
    created_on   timestamp,
    credit_limit numeric(15, 2) not null,
    client       varchar(255)   not null,
    agent_id     integer        not null
        constraint fkcs5aefchaqji91dkqvnb9wgak
            references employees
);

alter table card_orders
    owner to postgres;
