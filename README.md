# team01-napp-projekt1-racetrack

# Beschreibung

Dieses Repository enthält den Code für das Übungsprojekt "Racetrack" (https://en.wikipedia.org/wiki/Racetrack_(game)) 


# Organisation
Alles Issues und Aufgaben sind hier zu finden: https://github.zhaw.ch/PM2-IT21taWIN-bles-gan-kars/team01-napp-projekt1-racetrack/projects/1

# Klassendiagramm

<img src="http://www.filib.ch/ZHAW/Klassendiagramm_Racetrack.png" alt="Klassendiagramm"/>
Hier in voller Grösse: http://www.filib.ch/ZHAW/Klassendiagramm_Racetrack.png

# Team

# Gruppen-Mitglieder
* Adrian Büchi 
* Philippe Weber 
* Patric Fuchs
* Nico Wartmann 

## Teamrules
* Wir versuchen den Code in Englisch zu schreiben d.h. zum Beispiel ConsoleInputReader.java oder AddParagraph(String text, int paragraphNumber) anstelle von deutschen Namen.
* Wir arbeiten grundsätzlich mit Branches. Branches werden auf Englisch benammst und sollten beschreiben, was in diesem Branch gemacht wird.
* Wenn eine Änderung komplett ist, sollte diese im Idealfall in Review bei allen gestellt werden bevor auf den Master Branch gepushed wird. (4 Augen Prinzip)
* Git Commits bitte auf Englisch und nur Zustände comitten, die mindestens kompilieren und nach Möglichkeit nur auf die eigenen Feature Branches.
* Wenn wir feststellen, dass etwas nicht funktioniert bitte frühzeitig melden, wenn die oben genannten Teamrules nur hinderlich sind dies ansprechen, dann werden die Neu definiert.

### Git Workflow

Wir verwenden den Standard Git-Workflow, d.h. wir arbeiten grundsätzlich mit eigenen Branches. 

Das Naming Pattern, welches wir auf den Branches verwenden ist folgendes: 
* Für Feature Branches: prefix mit `feature`
* Für Bugfix Branches: prefix mit `bugfix`

Daraus resultiert z.B. folgender Name für einen Branch: `feature/implement-foo-bar` oder `bugfix\fix-button-click-not-working`

Alles Weitere wie z.B. `release` oder vglw. wird für den Rahmen des Projektes nicht benötigt.

Der Hauptbranch ist bei uns der `master`. Auf diesen sollte nicht direkt gepushed werden (wird via rules auch verhindert). 

Grundsätzlich ist die Idee mit Pull-Requests zu arbeiten anstelle von direkten Pushes auf den `master`. 
Ein Pull-Request wird immer von einem Feature/Bug Branch aus gemacht. Pull-Requests sollten einen Zustand repräsentieren, der fertig ist, d.h. nicht noch TODOs oder unaufgeräumten Code enthalten. 
Es wird immer mind. ein Teammitglied den Code reviewen müssen um zu mergen. Idealerweise schauen aber alle Teammitglieder auf den Code und geben ihr Review ab. Adrian Büchi sollte grundsätzlich aufgrund seiner Erfahrung **immer** als Reviewer darauf sein. Nach einem Review sollten allfällige Änderungen entweder umgesetzt oder Rückfragen gestellt werden um Unklarheiten oder vglw. aufzulösen. Wenn das Feedback umgesetzt wurde, beginnt der Reviewzyklus von vorne.

Wenn ein Pull-Request gemerged wurde, sollte der dazugehörige Branch immer gelöscht werden. Dies dient dazu, dass keine "toten" branches herumliegen die potentiell verwirren könnten.

Commit Messages sollten nach Möglichkeit wiederspiegeln, was gemacht wurde. D.h. nicht "Did stuff" "Fixed" oder vglw. sondern repräsentativ für den Change stehen.


