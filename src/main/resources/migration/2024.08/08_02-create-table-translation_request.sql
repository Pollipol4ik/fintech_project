-- --liquibase formatted sql
--
-- --changeset Polina Kuptsova:create-translation_request
-- create sequence translation_request_pk_seq start 1 increment 1;
-- create table if not exists translation_request
-- (
--     id bigint not null primary key,
--     ip_address text   not null check (length(ip_address) > 0),
--     input_text text   not null check (length(input_text) > 0),
--     translated_text text   not null check (length(translated_text) > 0)
-- );
-- alter table translation_request alter column id set default nextval('translation_request_pk_seq');
-- --rollback drop table translation_request;

--liquibase formatted sql

--changeset Polina Kuptsova:create-ip-address-table
create sequence ip_address_pk_seq start 1 increment 1;
create table if not exists ip_address
(
    id bigint not null primary key,
    ip_address text not null unique check (length(ip_address) > 0)
);
alter table ip_address alter column id set default nextval('ip_address_pk_seq');

create sequence translation_request_pk_seq start 1 increment 1;

create table if not exists translation_request
(
    id bigint not null primary key,
    ip_id bigint not null,
    input_text text not null check (length(input_text) > 0),
    translated_text text not null check (length(translated_text) > 0),
    constraint fk_ip foreign key (ip_id) references ip_address(id) on delete cascade
);


alter table translation_request alter column id set default nextval('translation_request_pk_seq');

--rollback drop table translation_request;
--rollback drop table ip_address;