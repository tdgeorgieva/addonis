create table addon_statuses
(
    status_id   int auto_increment,
    status_name varchar(20) null,
    constraint addon_statuses_status_id_uindex
        unique (status_id)
);

alter table addon_statuses
    add primary key (status_id);

create table addon_types
(
    type_id   int auto_increment,
    type_name varchar(20) not null,
    constraint addon_types_type_id_uindex
        unique (type_id)
);

alter table addon_types
    add primary key (type_id);

create table ides
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

create table repository_details
(
    repository_id       int auto_increment,
    origin_link         varchar(200) not null,
    open_issues_count   int          null,
    pull_requests_count int          null,
    last_commit_date    date         null,
    last_commit_title   varchar(100) null,
    constraint repository_details_origin_link_uindex
        unique (origin_link),
    constraint repository_details_repository_id_uindex
        unique (repository_id)
);

alter table repository_details
    add primary key (repository_id);

create table roles
(
    role_id int auto_increment
        primary key,
    name    varchar(20) null
);

create table tags
(
    tag_id int auto_increment
        primary key,
    name   varchar(50) not null
);

create table user_statuses
(
    status_id   int auto_increment,
    status_name varchar(20) not null,
    constraint statuses_status_id_uindex
        unique (status_id),
    constraint statuses_status_name_uindex
        unique (status_name)
);

alter table user_statuses
    add primary key (status_id);

create table users
(
    user_id      int auto_increment,
    username     varchar(20) not null,
    email        varchar(50) not null,
    phone_number int(10)     not null,
    role_id      int         null,
    first_name   varchar(50) not null,
    last_name    varchar(50) not null,
    photo        varchar(20) null,
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

create table addons
(
    addon_id        int auto_increment,
    name            varchar(30)   not null,
    creator_user_id int           not null,
    description     varchar(200)  null,
    binary_content  longblob      null,
    status_id       int default 0 not null,
    ide_id          int           null,
    rating          float         null,
    image           blob          null,
    downloads_count int           null,
    download_link   varchar(200)  null,
    repository_id   int           null,
    upload_date     date          null,
    type_id         int           null,
    constraint addons_addon_id_uindex
        unique (addon_id),
    constraint addons_name_uindex
        unique (name),
    constraint addons_addon_statuses_status_id_fk
        foreign key (status_id) references addon_statuses (status_id),
    constraint addons_addon_types_type_id_fk
        foreign key (type_id) references addon_types (type_id),
    constraint addons_ides_ide_id_fk
        foreign key (ide_id) references ides (ide_id),
    constraint addons_repository_details_repository_id_fk
        foreign key (repository_id) references repository_details (repository_id),
    constraint addons_users_id_fk
        foreign key (creator_user_id) references users (user_id)
);

alter table addons
    add primary key (addon_id);

create table addon_codes
(
    code            int      not null,
    expiration_date datetime null,
    addon_id        int      not null,
    constraint addon_codes_code_uindex
        unique (code),
    constraint addon_codes_addons_addon_id_fk
        foreign key (addon_id) references addons (addon_id)
);

alter table addon_codes
    add primary key (code);

create table addon_tags
(
    addon_id int null,
    tag_id   int null,
    constraint addon_tags_addons_addon_id_fk
        foreign key (addon_id) references addons (addon_id),
    constraint addon_tags_tags_tag_id_fk
        foreign key (tag_id) references tags (tag_id)
);

create table blocked_users
(
    user_id    int  not null
        primary key,
    blocked_by int  not null,
    block_date date not null,
    constraint blocked_users_users_user_id_fk
        foreign key (user_id) references users (user_id),
    constraint blocked_users_users_user_id_fk_2
        foreign key (blocked_by) references users (user_id)
);

create table invited_users
(
    email           varchar(50) not null,
    inviter_user_id int         null,
    constraint invited_users_users_user_id_fk
        foreign key (inviter_user_id) references users (user_id)
);

create table joint_addons_collaborators
(
    addon_id int not null,
    user_id  int not null,
    constraint joint_addons_collaborators_addons_addon_id_fk
        foreign key (addon_id) references addons (addon_id),
    constraint joint_addons_collaborators_users_user_id_fk
        foreign key (user_id) references users (user_id)
);

create table user_followers
(
    follower_user_id int not null,
    followed_user_id int not null,
    constraint user_followers_users_user_id_fk
        foreign key (followed_user_id) references users (user_id),
    constraint user_followers_users_user_id_fk_2
        foreign key (follower_user_id) references users (user_id)
);

create table users_credentials
(
    user_id  int         not null,
    password varchar(30) not null,
    constraint users_credentials_password_uindex
        unique (password),
    constraint users_credentials_user_id_uindex
        unique (user_id),
    constraint users_credentials_users_user_id_fk
        foreign key (user_id) references users (user_id)
);

alter table users_credentials
    add primary key (user_id);

create table users_statuses
(
    user_id   int not null,
    status_id int null,
    constraint users_statuses_user_statuses_status_id_fk
        foreign key (status_id) references user_statuses (status_id),
    constraint users_statuses_users_user_id_fk
        foreign key (user_id) references users (user_id)
);

create table users_verification
(
    user_id                 int          not null
        primary key,
    id_card_photo           blob         not null,
    selfie_photo            blob         not null,
    verification_email_link varchar(100) not null,
    constraint users_verification_users_user_id_fk
        foreign key (user_id) references users (user_id)
);


