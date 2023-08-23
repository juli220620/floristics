--liquibase formatted sql
--changeset Yan:init-ddl failOnError:true

create table achievement
(
    id          varchar(256) not null
        primary key,
    name        varchar(256) not null,
    description text         not null
);

create table base_flower_dict
(
    id                   varchar(256)  not null
        primary key,
    name                 varchar(256)  not null,
    growth_time          bigint        not null,
    water_consumption    int default 1 not null,
    nutrient_consumption int default 1 not null,
    price                int default 0 not null,
    tier                 int default 0 not null,
    max_cycles           int           null,
    ideal_humidity       double        not null,
    ideal_light          double        not null,
    constraint check_humidity
        check ((0.1 <= `ideal_humidity`) <= 1),
    constraint check_light
        check ((0.1 <= `ideal_light`) <= 1)
);

create table currency_dict
(
    id             varchar(256)   not null
        primary key,
    name           varchar(256)   not null,
    factor_to_cash int default 10 not null
);

create table flower_harvest
(
    flower_id   varchar(256)  not null,
    currency_id varchar(256)  not null,
    amount      int default 0 not null,
    primary key (flower_id, currency_id),
    constraint flower_harvest_ibfk_1
        foreign key (flower_id) references base_flower_dict (id),
    constraint flower_harvest_ibfk_2
        foreign key (currency_id) references currency_dict (id)
);

create index currency_id
    on flower_harvest (currency_id);

create table flower_harvest_bonus_info
(
    flower_id             varchar(256)     not null,
    count                 bigint           not null,
    currency_id           varchar(256)     not null,
    multiplier            double default 1 not null,
    flat_bonus            int    default 0 not null,
    free_offspring_chance double default 0 not null,
    primary key (flower_id, count, currency_id),
    constraint flower_harvest_bonus_info_base_flower_dict_id_fk
        foreign key (flower_id) references base_flower_dict (id),
    constraint flower_harvest_bonus_info_currency_dict_id_fk
        foreign key (currency_id) references currency_dict (id)
);

create index flower_harvest_bonus_info_flower_id_index
    on flower_harvest_bonus_info (flower_id);

create table game_system_dict
(
    id   varchar(256) not null
        primary key,
    name varchar(256) not null
);

create table pot_dict
(
    id       varchar(256)    not null
        primary key,
    name     varchar(256)    not null,
    capacity int default 100 not null,
    price    int default 0   not null,
    size     int default 1   not null
);

create table user
(
    id       bigint auto_increment
        primary key,
    username varchar(256) not null,
    password varchar(256) not null
);

create table user_achievement
(
    user_id        bigint               not null,
    achievement_id varchar(256)         not null,
    achieved       tinyint(1) default 0 not null,
    date           date                 null,
    meta           json                 null,
    primary key (user_id, achievement_id),
    constraint user_achievement_ibfk_1
        foreign key (user_id) references user (id),
    constraint user_achievement_ibfk_2
        foreign key (achievement_id) references achievement (id)
);

create index achievement_id
    on user_achievement (achievement_id);

create index user_id
    on user_achievement (user_id);

create table user_currency
(
    user_id     bigint           not null,
    currency_id varchar(256)     not null,
    amount      bigint default 0 not null,
    primary key (user_id, currency_id),
    constraint user_currency_ibfk_1
        foreign key (user_id) references user (id),
    constraint user_currency_ibfk_2
        foreign key (currency_id) references currency_dict (id)
);

create index currency_id
    on user_currency (currency_id);

create table user_flower_count
(
    user_id   bigint           not null,
    flower_id varchar(256)     not null,
    count     bigint default 0 not null,
    primary key (user_id, flower_id),
    constraint user_flower_count_base_flower_dict_id_fk
        foreign key (flower_id) references base_flower_dict (id),
    constraint user_flower_count_user_id_fk
        foreign key (user_id) references user (id)
);

create index user_flower_count_user_id_index
    on user_flower_count (user_id);

create table user_game_system
(
    user_id      bigint        not null,
    system_id    varchar(256)  not null,
    system_level int default 1 not null,
    primary key (user_id, system_id),
    constraint user_game_system_game_system_dict_id_fk
        foreign key (system_id) references game_system_dict (id),
    constraint user_game_system_user_id_fk
        foreign key (user_id) references user (id)
);

create table user_room
(
    id       bigint auto_increment
        primary key,
    user_id  bigint        not null,
    area     int default 5 not null,
    name     varchar(256)  not null,
    humidity double        not null,
    light    double        not null,
    constraint user_room_ibfk_1
        foreign key (user_id) references user (id),
    constraint humidity_check
        check ((0.1 <= `humidity`) <= 1),
    constraint light_check
        check ((0.1 <= `light`) <= 1)
);

create table room_flower
(
    id            bigint auto_increment
        primary key,
    room_id       bigint                                 not null,
    flower_id     varchar(256)                           not null,
    pot_id        varchar(256)                           not null,
    water         int          default 0                 not null,
    nutrient      int          default 0                 not null,
    growth        bigint       default 0                 not null,
    updated       datetime     default CURRENT_TIMESTAMP not null,
    status        varchar(256) default 'GROWING'         not null,
    death_ticks   bigint       default 0                 not null,
    planted       datetime     default (now())           not null,
    current_cycle int                                    null,
    cycles        int                                    null,
    auto_harvest  bit          default b'0'              not null,
    constraint room_flower_ibfk_1
        foreign key (room_id) references user_room (id),
    constraint room_flower_ibfk_2
        foreign key (flower_id) references base_flower_dict (id),
    constraint room_flower_ibfk_3
        foreign key (pot_id) references pot_dict (id),
    check (`status` in ('GROWING', 'RIPE', 'DEAD'))
);

create index flower_id
    on room_flower (flower_id);

create index pot_id
    on room_flower (pot_id);

create index room_id
    on room_flower (room_id);

create index user_id
    on user_room (user_id);

