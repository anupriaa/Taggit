# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table entry (
  entry_id                  bigint not null,
  entry_type                varchar(255),
  timestamp                 varchar(255),
  constraint pk_entry primary key (entry_id))
;

create table url_info (
  url_id                    bigint not null,
  url_type                  varchar(255),
  url                       varchar(255),
  url_entry_id              bigint,
  constraint pk_url_info primary key (url_id))
;

create sequence entry_seq;

create sequence url_info_seq;

alter table url_info add constraint fk_url_info_entry_1 foreign key (url_entry_id) references entry (entry_id);
create index ix_url_info_entry_1 on url_info (url_entry_id);



# --- !Downs

drop table if exists entry cascade;

drop table if exists url_info cascade;

drop sequence if exists entry_seq;

drop sequence if exists url_info_seq;

