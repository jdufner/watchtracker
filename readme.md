[![Build Status](https://travis-ci.org/jdufner/watchtracker.svg?branch=master)](https://travis-ci.org/jdufner/watchtracker)

# Watchtracker

Der Watchtracker ist zur Aufzeichnung von Abweichungen der Uhrzeit deiner Armbanduhr möglich.

Der Hintergrund ist, dass mechanische Uhren einen gewissen Fehler haben. Mit dieser Anwendung
können die Abweichungen und die Korrekturen aufgezeichnet werden.

Die aufgezeichneten Daten können dann wieder dargestellt werden um die mittlere Abweichung über 
einen längeren Zeitraum zu ermitteln.

# Produktion

## Aufruf der Applikation

`java -Dspring.profiles.active=file -jar tracker-<VERSION>.jar`

## Servicewrapper

Service editieren
`sudo vi /etc/systemd/system/tracker.service`

Reload Daemon
`sudo systemctl daemon-reload`

Starte Service
`sudo systemctl start tracker.service`

Stoppe Service
`sudo systemctl stop tracker.service`
