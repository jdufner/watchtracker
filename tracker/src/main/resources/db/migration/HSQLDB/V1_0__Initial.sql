create sequence hibernate_sequence start with 1 increment by 1;
create table abweichung (id bigint not null, differenz integer not null, erfassungszeitpunkt timestamp not null, korrektur integer, primary key (id));
alter table abweichung add constraint erfassungszeitpunkt_UK unique (erfassungszeitpunkt);
