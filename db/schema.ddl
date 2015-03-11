-- create database somanyfeeds_dev with encoding='utf-8'

drop table if exists article;

create table article (
  id      bigserial primary key,
  title   text,
  link    text,
  content text,
  date    timestamp
);

drop type if exists feed_type;

create type feed_type as enum ('RSS', 'ATOM');

drop table if exists feed;

create table feed (
  id      bigserial primary key,
  name   text,
  slug   text,
  url    text,
  type   feed_type
);
