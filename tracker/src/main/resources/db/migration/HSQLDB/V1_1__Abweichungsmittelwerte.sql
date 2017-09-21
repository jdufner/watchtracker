alter table abweichung add abweichung_pro_tag_seit_letzter_messung double null;
alter table abweichung add abweichung_pro_tag_in_letzter_woche double null;
alter table abweichung add abweichung_pro_tag_in_letztem_monat double null;
create unique index abweichung_erfassungszeitpunkt on abweichung (erfassungszeitpunkt);
