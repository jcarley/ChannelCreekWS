
    create table game (
        game_id bigint not null,
        game_date varchar(255),
        home_final_score integer,
        location varchar(75),
        visitor_final_score integer,
        home_team bigint,
        visitor_team bigint,
        primary key (game_id)
    );

    create table player (
        player_id bigint not null,
        active smallint not null,
        jersey_number integer not null,
        name varchar(50),
        position varchar(25),
        team_id bigint not null,
        primary key (player_id)
    );

    create table schedule (
        schedule_id bigint not null,
        season_name varchar(25),
        team_id bigint not null,
        primary key (schedule_id)
    );

    create table schedule_games (
        schedule_id bigint not null,
        game_id bigint not null,
        primary key (schedule_id, game_id)
    );

    create table team (
        team_id bigint not null,
        active smallint not null,
        name varchar(50),
        primary key (team_id)
    );

    alter table game 
        add constraint fk_visitor_team 
        foreign key (visitor_team) 
        references team;

    alter table game 
        add constraint fk_home_team 
        foreign key (home_team) 
        references team;

    alter table player 
        add constraint fk_player_team 
        foreign key (team_id) 
        references team;

    alter table schedule 
        add constraint fk_schedule_team 
        foreign key (team_id) 
        references team;

    alter table schedule_games 
        add constraint fk_schedule_games 
        foreign key (schedule_id) 
        references schedule;

    alter table schedule_games 
        add constraint fk_game_schedules 
        foreign key (game_id) 
        references game;

    create table hibernate_unique_key (
         next_hi integer 
    );

    insert into hibernate_unique_key values ( 0 );
