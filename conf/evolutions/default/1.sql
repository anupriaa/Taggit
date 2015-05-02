# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table entry (
  entry_id                  bigint not null,
  entry_type                varchar(255),
  timestamp                 varchar(255),
  email                     varchar(255),
  user_info_id              bigint,
  constraint pk_entry primary key (entry_id))
;

create table keywords (
  id                        bigint not null,
  keyword                   varchar(255),
  keyword_entry_id          bigint,
  entry_entry_id            bigint,
  constraint pk_keywords primary key (id))
;

create table url_info (
  url_id                    bigint not null,
  url_type                  varchar(255),
  url                       varchar(255),
  url_entry_id              bigint,
  entry_entry_id            bigint,
  constraint pk_url_info primary key (url_id))
;

create table user_info (
  id                        bigint not null,
  email                     varchar(255),
  password                  varchar(255),
  constraint pk_user_info primary key (id))
;

create sequence entry_seq;

create sequence keywords_seq;

create sequence url_info_seq;

create sequence user_info_seq;

alter table entry add constraint fk_entry_userInfo_1 foreign key (user_info_id) references user_info (id);
create index ix_entry_userInfo_1 on entry (user_info_id);
alter table keywords add constraint fk_keywords_entry_2 foreign key (entry_entry_id) references entry (entry_id);
create index ix_keywords_entry_2 on keywords (entry_entry_id);
alter table url_info add constraint fk_url_info_entry_3 foreign key (entry_entry_id) references entry (entry_id);
create index ix_url_info_entry_3 on url_info (entry_entry_id);



# --- !Downs

drop table if exists entry cascade;

drop table if exists keywords cascade;

drop table if exists url_info cascade;

drop table if exists user_info cascade;

drop sequence if exists entry_seq;

drop sequence if exists keywords_seq;

drop sequence if exists url_info_seq;

drop sequence if exists user_info_seq;

