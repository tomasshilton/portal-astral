# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table student (
  id                            varchar(255) not null,
  first_name                    varchar(255),
  last_name                     varchar(255),
  constraint pk_student primary key (id)
);


# --- !Downs

drop table if exists student;

