CREATE SCHEMA `springbook` ;

CREATE TABLE users(
  id varchar(10) primary key,
  name varchar(10) not null,
  password varchar(10) not null
);

ALTER TABLE users ADD COLUMN level tinyint not null;
ALTER TABLE users ADD COLUMN login int not null;
ALTER TABLE users ADD COLUMN recommend int not null;