create table if not exists sensor_data.sensor_data
(
    id        bigint                              not null
    primary key,
    type      varchar(30)                         not null,
    value     float                               not null,
    timestamp timestamp default CURRENT_TIMESTAMP null
    );