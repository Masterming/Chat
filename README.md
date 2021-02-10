Das User-Server-Programm wird als Konsolen-Anwendung realisiert!

# Meilenstein 1
## Server:
Der Server sollte mindestens folgende Anforderungen unterstützen:
- starten und beenden des ServerSockets
- Realisierung eines Netzwerk-Listener, der es neuen Users ermöglicht, sich anzumelden
- Realisierung eines Nachrichten-Listener, welcher die Voraussetzung für den Nachrichtenaustausch zwischen Users schafft
- die Möglichkeit, Namen und dazugehörige Passwörter zu speichern und zu verwalten – solange der Server läuft
- Realisierung eines Anmeldevorgangs, bei dem sich ein User mit Namen und Passwort anmeldet. Sollte der Name noch nicht registriert sein, werden diese Daten auf dem Server gespeichert
- nach einem Anmeldevorgang bekommt der sich gerade angemeldete User eine aktuelle Liste der Namen von bereits angemeldeten Users gesendet.
- nach einem Anmeldevorgang erhalten alle bisher angemeldeten Users weiter eine Meldung, dass sich ein neuer User angemeldet hat
 
## User:
Ein User sollte mindestens die folgenden Anforderungen unterstützen:
- Möglichkeit zum Aufbau und Trennen einer Verbindung zum Server 
- Weitergabe einer Eingabemaske mit Name und Passwort um sich beim Server anzumelden
- Möglichkeit zum Senden von Nachrichten an alle angemeldeten Users 
- Nachrichten, welche vom Server gesendet werden zu empfangen und zu visualisieren


# Meilenstein 2
## Server:
- Implementierung als grafische Benutzerschnittstelle (siehe Anhang 1)
- Realisierung einer visuellen Anzeige, einschließlich dynamischer Aktualisierung von Räume, angemeldete Benutzer mit Raumangabe
- Gestaltung einer Kommunikationsoberfläche zur Visualisierung aller Aktivitäten nach
dem Starten des Servers bis zum Beenden im Bezug auf Verbindung, Kommunikation
und Verwaltung
– außerdem sollen diese Ereignisse in einer Datei protokolliert werden (siehe Anhang 1)                                                                     //DONE
- Weiterentwicklung des Einraum-Systems zu einem Mehrraum-System
- Optimierung der Benutzerkontenverwaltung durch eine dauerhafte Speicherung der Daten auf der Festplatte und der Möglichkeit, diese Datensätze anzuzeigen  //DONE
- Bereitstellung von Methoden zur Verwaltung von Räumen (Erstellen, Editieren, Löschen)
- Bereitstellung von Methoden zur Verwaltung von Benutzern (Verwarnen, Kick, Ban)
- prüfung von doppelter anmeldung
 
## User:
- Implementierung als grafische Benutzerschnittstelle
- Realisierung einer visuellen Anzeige, einschließlich dynamischer Aktualisierung von Räume und alle Nutzer im Raum
- Anordnung einer Kommunikationsoberfläche, wodurch die Möglichkeit geschaffen wird, mit anderen Users zu kommunizieren
- möglichkeit des useren selber die verbindung sauber zu trennen

# Meilenstein 3
## Server:
- Einführung einer Kommunikation über private Räume zwischen 2 Clients

## User:
- Eröffnen eines privaten Raumes
- Senden von Nachrichten von einem Client zum anderen Client
- Schließen eines privaten Raumes
