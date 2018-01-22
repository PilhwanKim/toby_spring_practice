CREATE SCHEMA `springbook` ;

CREATE TABLE users(
  id varchar(10) primary key,
  name varchar(10) not null,
  password varchar(10) not null
);
