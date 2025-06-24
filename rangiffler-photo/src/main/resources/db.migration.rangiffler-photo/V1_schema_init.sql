create table if not exists `photo`
(
    id                      UUID unique not null default uuid_generate_v1(),
    user_firstname          varchar(255)        not null,
    country_name            varchar(255)        not null,
    `description`           varchar(255),
    photo                   longblob,
    created_date            datetime            not null,
    primary key (id),
    constraint ph_user_id foreign key (user_id) references `user` (id),
    constraint ph_country_id foreign key (country_id) references `country` (id)
);

create table if not exists `like`
(
    id                      UUID unique         not null default uuid_generate_v1(),
    user_id                 varchar(255)        not null,
    created_date            datetime            not null,
    primary key (id),
    constraint like_user_id foreign key (user_id) references `user` (id)
);

create table if not exists `photo_like`
(
    photo_id                 UUID               not null,
    like_id                  UUID               not null,
    primary key (photo_id, like_id),
    constraint ph_like_photo_id foreign key (photo_id) references `photo` (id),
    constraint lk_like_photo_id foreign key (like_id) references `like` (id)
);