# Server:
Der Server sollte mindestens folgende Anforderungen unterstützen:
- starten und beenden des ServerSockets                                                 
- Realisierung eines Netzwerk-Listener, der es neuen Clients                            
 ermöglicht, sich anzumelden
 - Realisierung eines Nachrichten-Listener, welcher die Voraussetzung                   
 für den Nachrichtenaustausch zwischen Clients schafft
 - die Möglichkeit, Namen und dazugehörige Passwörter zu speichern                      
 und zu verwalten – solange der Server läuft
- Realisierung eines Anmeldevorgangs, bei dem sich ein Client mit Namen und             
 Passwort anmeldet. Sollte der Name noch nicht registriert sein, werden diese Daten
 auf dem Server gespeichert
- nach einem Anmeldevorgang bekommt der sich gerade angemeldete Client eine             
 aktuelle Liste der Namen von bereits angemeldeten Clients gesendet.
- nach einem Anmeldevorgang erhalten alle bisher angemeldeten Clients                   
 weiter eine Meldung, dass sich ein neuer Client angemeldet hat
 
# Client:
Ein Client sollte mindestens die folgenden Anforderungen unterstützen:
- Möglichkeit zum Aufbau und Trennen einer Verbindung zum Server                        
- Weitergabe einer Eingabemaske mit Name und Passwort um sich beim                      
 Server anzumelden
- Möglichkeit zum Senden von Nachrichten an alle angemeldeten Clients                   
- Nachrichten, welche vom Server gesendet werden zu empfangen und                       
 zu visualisieren
Realisieren Sie die Client-Server-Programme als Konsolen-Anwendung!                     
