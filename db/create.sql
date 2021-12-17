DROP DATABASE IF EXISTS addonis;
CREATE DATABASE IF NOT EXISTS addonis;
USE addonis;
create or replace table ides
(
    ide_id int auto_increment,
    name   varchar(50) not null,
    constraint ides_ide_id_uindex
        unique (ide_id),
    constraint ides_name_uindex
        unique (name)
);

alter table ides
    add primary key (ide_id);

create or replace table roles
(
    role_id int auto_increment
        primary key,
    name    varchar(20) null
);

create or replace table tags
(
    tag_id int auto_increment
        primary key,
    name   varchar(50) not null
);

create or replace table users
(
    user_id      int auto_increment,
    username     varchar(20)  not null,
    password     varchar(100) not null,
    email        varchar(50)  not null,
    phone_number varchar(10)  null,
    first_name   varchar(50)  null,
    last_name    varchar(50)  null,
    photo        longblob     null,
    role_id      int          null,
    status       varchar(20)  null,
    constraint users_email_uindex
        unique (email),
    constraint users_phone_number_uindex
        unique (phone_number),
    constraint users_user_id_uindex
        unique (user_id),
    constraint users_username_uindex
        unique (username),
    constraint users_roles_role_id_fk
        foreign key (role_id) references roles (role_id)
);

alter table users
    add primary key (user_id);

create or replace table addons
(
    addon_id        int auto_increment,
    name            varchar(100)            not null,
    creator_user_id int                     not null,
    description     varchar(65000)          null,
    status          varchar(20) default '0' null,
    ide_id          int                     null,
    image           longblob                null,
    downloads_count int                     null,
    upload_date     date                    null,
    type            varchar(100)            null,
    link            varchar(200)            null,
    is_featured     tinyint(1)              null,
    constraint addons_addon_id_uindex
        unique (addon_id),
    constraint addons_name_uindex
        unique (name),
    constraint addons_ides_ide_id_fk
        foreign key (ide_id) references ides (ide_id),
    constraint addons_users_id_fk
        foreign key (creator_user_id) references users (user_id)
);

create or replace index addons_addon_statuses_status_id_fk
    on addons (status);

create or replace index addons_addon_types_type_id_fk
    on addons (type);

alter table addons
    add primary key (addon_id);

create or replace table addon_codes
(
    id              int auto_increment
        primary key,
    code            varchar(50) not null,
    expiration_date date        null,
    addon_id        int         not null,
    constraint addon_codes_code_uindex
        unique (code),
    constraint addon_codes_addons_addon_id_fk
        foreign key (addon_id) references addons (addon_id)
            on update cascade on delete cascade
);

create or replace table addon_tags
(
    addon_id int null,
    tag_id   int null,
    constraint addon_tags_addons_addon_id_fk
        foreign key (addon_id) references addons (addon_id)
            on delete cascade,
    constraint addon_tags_tags_tag_id_fk
        foreign key (tag_id) references tags (tag_id)
);

create or replace table follow_connections
(
    follower_user_id int not null,
    followed_user_id int not null,
    constraint user_followers_users_user_id_fk
        foreign key (followed_user_id) references users (user_id),
    constraint user_followers_users_user_id_fk_2
        foreign key (follower_user_id) references users (user_id)
);

create or replace table invited_users
(
    email           varchar(50) not null,
    inviter_user_id int         null,
    constraint invited_users_users_user_id_fk
        foreign key (inviter_user_id) references users (user_id)
);

create or replace table ratings
(
    rating_id int auto_increment,
    addon_id  int null,
    user_id   int null,
    rating    int null,
    constraint ratings_rating_id_uindex
        unique (rating_id),
    constraint ratings_addons_addon_id_fk
        foreign key (addon_id) references addons (addon_id)
            on update cascade on delete cascade,
    constraint ratings_users_user_id_fk
        foreign key (user_id) references users (user_id)
);

alter table ratings
    add primary key (rating_id);

create or replace table reviews
(
    review_id   int auto_increment,
    user_id     int            null,
    description varchar(10000) null,
    addon_id    int            null,
    date        datetime       null,
    constraint reviews_review_id_uindex
        unique (review_id),
    constraint reviews_addons_addon_id_fk
        foreign key (addon_id) references addons (addon_id)
            on update cascade on delete cascade,
    constraint reviews_users_user_id_fk
        foreign key (user_id) references users (user_id)
);

alter table reviews
    add primary key (review_id);

create or replace table users_confirmation_tokens
(
    token_id           int auto_increment,
    confirmation_token varchar(50) null,
    created_date       date        null,
    user_id            int         not null,
    constraint users_email_verification_id_uindex
        unique (token_id),
    constraint users_email_verification_users_user_id_fk
        foreign key (user_id) references users (user_id)
);

alter table users_confirmation_tokens
    add primary key (token_id);

create or replace table users_verification
(
    id            int auto_increment,
    user_id       int      not null,
    id_card_photo longblob null,
    selfie_photo  longblob null,
    constraint users_verification_id_uindex
        unique (id),
    constraint users_verification_users_user_id_fk
        foreign key (user_id) references users (user_id)
);

alter table users_verification
    add primary key (id);


