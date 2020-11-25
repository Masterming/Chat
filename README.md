# Server:
Der Server sollte mindestens folgende Anforderungen unterstützen:
- starten und beenden des ServerSockets                                                 // done
- Realisierung eines Netzwerk-Listener, der es neuen Clients                            // done
 ermöglicht, sich anzumelden
 - Realisierung eines Nachrichten-Listener, welcher die Voraussetzung                   // done
 für den Nachrichtenaustausch zwischen Clients schafft
 - die Möglichkeit, Namen und dazugehörige Passwörter zu speichern                      // done
 und zu verwalten – solange der Server läuft
- Realisierung eines Anmeldevorgangs, bei dem sich ein Client mit Namen und             // done
 Passwort anmeldet. Sollte der Name noch nicht registriert sein, werden diese Daten
 auf dem Server gespeichert
- nach einem Anmeldevorgang bekommt der sich gerade angemeldete Client eine             // todo
 aktuelle Liste der Namen von bereits angemeldeten Clients gesendet.
- nach einem Anmeldevorgang erhalten alle bisher angemeldeten Clients                   // todo
 weiter eine Meldung, dass sich ein neuer Client angemeldet hat
# Client:
Ein Client sollte mindestens die folgenden Anforderungen unterstützen:
- Möglichkeit zum Aufbau und Trennen einer Verbindung zum Server                        // done
- Weitergabe einer Eingabemaske mit Name und Passwort um sich beim                      // done
 Server anzumelden
- Möglichkeit zum Senden von Nachrichten an alle angemeldeten Clients                   // done
- Nachrichten, welche vom Server gesendet werden zu empfangen und                       // done
 zu visualisieren
Realisieren Sie die Client-Server-Programme als Konsolen-Anwendung!
