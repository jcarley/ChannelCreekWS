
    create table game (
        game_id bigint not null,
        game_date varchar(255),
        home_final_score integer not null,
        location varchar(255),
        visitor_final_score integer not null,
        primary key (game_id)
    );

    create table player (
        player_id bigint not null,
        active smallint not null,
        date_from varchar(255),
        date_to varchar(255),
        jersey_number integer not null,
        name varchar(255),
        position varchar(255),
        team_id bigint,
        primary key (player_id)
    );

    create table schedule (
        schedule_id bigint not null,
        season_name varchar(255),
        team_id bigint,
        primary key (schedule_id)
    );

    create table schedule_games (
        schedule_id bigint not null,
        game_id bigint not null,
        primary key (schedule_id, game_id)
    );

    create table team (
        team_id bigint not null,
        name varchar(255),
        primary key (team_id)
    );

    alter table player 
        add constraint FKC53E9AE189144DE3 
        foreign key (team_id) 
        references team;

    alter table schedule 
        add constraint FKD666929789144DE3 
        foreign key (team_id) 
        references team;

    alter table schedule_games 
        add constraint FK1B739B19D19EDF23 
        foreign key (schedule_id) 
        references schedule;

    alter table schedule_games 
        add constraint FK1B739B19D32F69C3 
        foreign key (game_id) 
        references game;

    create table hibernate_unique_key (
         next_hi integer 
    );

    insert into hibernate_unique_key values ( 0 );
