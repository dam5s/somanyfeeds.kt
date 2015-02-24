-- create database somanyfeeds_dev with encoding='utf-8'

drop table if exists article;

create table article (
  id      bigserial primary key,
  title   text,
  link    text,
  content text,
  date    timestamp
)
