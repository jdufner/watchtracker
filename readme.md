[![Build Status](https://travis-ci.org/jdufner/watchtracker.svg?branch=master)](https://travis-ci.org/jdufner/watchtracker)

# Watchtracker

Der Watchtracker ist zur Aufzeichnung von Abweichungen der Uhrzeit deiner 
Armbanduhr möglich.

Der Hintergrund ist, dass mechanische Uhren einen gewissen Fehler haben. Mit 
dieser Anwendung können die Abweichungen und die Korrekturen aufgezeichnet 
werden.

Die aufgezeichneten Daten können dann wieder dargestellt werden um die 
mittlere Abweichung über einen längeren Zeitraum zu ermitteln.

# Module

## Tracker

Der Tracker ist die eigentliche Webanwendung um die Daten manuell zu erfassen 
oder sich die aufgezeichneten Daten anzusehen. 

*Todo* Derzeit wird an einer Upload-Funktion für Fotos gearbeitet. 

## Recognizer

*Todo* Die Anwendung soll die Uhrzeit von der Uhr selbst lesen und mit dem
Zeitstempel aus den Metadaten des Fotos vergleichen um so die Abweichung 
zwischen angezeigter Uhrzeit und echter Uhrzeit zu ermitteln. Dabei liegt die
Annahme zugrunde, dass die Uhr des Fotoapperats _sehr_ genau ist. Diese Annahme
ist insofern begründet, dass als Fotoapperat vermutlich ein Handy benutzt wird,
welches für die GPS-Funktion eine sehr genaue Uhrzeit benötigt und die außerdem
die Uhrzeit von Mobilfunkprovider erhält.

## Metadataextractor

Der Metadataextractor ist eine Bibliothek, welche aus den EXIF-Metadaten eines 
Fotos den Aufnahmezeitpunkt ermittelt. Derzeit wird dazu das Tag 
`TAG_DATETIME_DIGITIZED` mit Wert `36868` / `0x9004` verwendet, siehe 
[Tag DateTimeDigitized](http://www.awaresystems.be/imaging/tiff/tifftags/privateifd/exif/datetimedigitized.html). 

# Deployment

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
