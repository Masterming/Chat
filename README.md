Das Client-Server-Programm wird als Konsolen-Anwendung realisiert!

# Meilenstein 1
## Server:
Der Server sollte mindestens folgende Anforderungen unterstützen:
- starten und beenden des ServerSockets
- Realisierung eines Netzwerk-Listener, der es neuen Clients ermöglicht, sich anzumelden
- Realisierung eines Nachrichten-Listener, welcher die Voraussetzung für den Nachrichtenaustausch zwischen Clients schafft
- die Möglichkeit, Namen und dazugehörige Passwörter zu speichern und zu verwalten – solange der Server läuft
- Realisierung eines Anmeldevorgangs, bei dem sich ein Client mit Namen und Passwort anmeldet. Sollte der Name noch nicht registriert sein, werden diese Daten auf dem Server gespeichert
- nach einem Anmeldevorgang bekommt der sich gerade angemeldete Client eine aktuelle Liste der Namen von bereits angemeldeten Clients gesendet.
- nach einem Anmeldevorgang erhalten alle bisher angemeldeten Clients weiter eine Meldung, dass sich ein neuer Client angemeldet hat
 
## Client:
Ein Client sollte mindestens die folgenden Anforderungen unterstützen:
- Möglichkeit zum Aufbau und Trennen einer Verbindung zum Server 
- Weitergabe einer Eingabemaske mit Name und Passwort um sich beim Server anzumelden
- Möglichkeit zum Senden von Nachrichten an alle angemeldeten Clients 
- Nachrichten, welche vom Server gesendet werden zu empfangen und zu visualisieren


# Meilenstein 2
## Server:
- Implementierung als grafische Benutzerschnittstelle (siehe Anhang 1)
- Realisierung einer visuellen Anzeige, einschließlich dynamischer Aktualisierung von Räume, angemeldete Benutzer mit Raumangabe
- Gestaltung einer Kommunikationsoberfläche zur Visualisierung aller Aktivitäten nach
dem Starten des Servers bis zum Beenden im Bezug auf Verbindung, Kommunikation
und Verwaltung
– außerdem sollen diese Ereignisse in einer Datei protokolliert werden (siehe Anhang 1)
- Weiterentwicklung des Einraum-Systems zu einem Mehrraum-System
- Optimierung der Benutzerkontenverwaltung durch eine dauerhafte Speicherung der Daten auf der Festplatte und der Möglichkeit, diese Datensätze anzuzeigen
- Bereitstellung von Methoden zur Verwaltung von Räumen (Erstellen, Editieren, Löschen)
- Bereitstellung von Methoden zur Verwaltung von Benutzern (Verwarnen, Kick, Ban)
- prüfung von doppelter anmeldung
 
## Client:
- Implementierung als grafische Benutzerschnittstelle
- Realisierung einer visuellen Anzeige, einschließlich dynamischer Aktualisierung von Räume und alle Nutzer im Raum
- Anordnung einer Kommunikationsoberfläche, wodurch die Möglichkeit geschaffen wird, mit anderen Clients zu kommunizieren
- möglichkeit des clienten selber die verbindung sauber zu trennen
