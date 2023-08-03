CREATE TABLE articles
(
    id             bigserial primary key not null,
    title          varchar,
    news_site      varchar,
    published_date timestamp,
    article        bytea
);
