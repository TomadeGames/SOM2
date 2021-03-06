package com.tomade.saufomat.activity.mainGame.task;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import static com.tomade.saufomat.activity.mainGame.task.TaskTextToken.LEFT_PLAYER;
import static com.tomade.saufomat.activity.mainGame.task.TaskTextToken.RANDOM_OTHER_PLAYER;
import static com.tomade.saufomat.activity.mainGame.task.TaskTextToken.RANDOM_PLAYER;
import static com.tomade.saufomat.activity.mainGame.task.TaskTextToken.RIGHT_PLAYER;
import static com.tomade.saufomat.activity.mainGame.task.TaskTextToken.WITH_LEAST_DRINKS;
import static com.tomade.saufomat.activity.mainGame.task.TaskTextToken.WITH_MOST_DRINKS;

/**
 * Definition aller Aufgaben
 * Created by woors on 17.10.2016.
 */

public class TaskDefinitions {
    private static final String TAG = TaskDefinitions.class.getSimpleName();

    public static List<Task> getDebugTasks() {
        List<Task> taskList = new ArrayList<>();
        String taskName = "DEBUG_TASK Random Player: " + RANDOM_PLAYER.getToken() + ", left Player: " + LEFT_PLAYER
                .getToken() + ", right Player: " + RIGHT_PLAYER.getToken() + ", least drinks: " + WITH_LEAST_DRINKS
                .getToken() + ", most drinks: " + WITH_MOST_DRINKS.getToken() + ", random other: " +
                RANDOM_OTHER_PLAYER.getToken();
        int cost = 1;
        int drinkCountLost = 2;
        TaskTarget taskTarget = TaskTarget.AD;
        taskList.add(new Task(taskName, TaskDifficult.EASY, drinkCountLost, cost, taskTarget));
        taskList.add(new Task(taskName, TaskDifficult.MEDIUM, drinkCountLost, cost, taskTarget));
        taskList.add(new Task(taskName, TaskDifficult.HARD, drinkCountLost, cost, taskTarget));
        taskList.add(new Task(taskName, TaskDifficult.EASY_WIN, drinkCountLost, cost, taskTarget));
        taskList.add(new Task(taskName, TaskDifficult.MEDIUM_WIN, drinkCountLost, cost, taskTarget));
        taskList.add(new Task(taskName, TaskDifficult.HARD_WIN, drinkCountLost, cost, taskTarget));
        return taskList;
    }

    public static List<Task> getTasks() {
        List<Task> taskList = new ArrayList<>();

        taskList.add(new Task("Trinke nichts", TaskDifficult.EASY, 0, 1, TaskTarget.SELF));
        taskList.add(new Task("Trinke einen", TaskDifficult.EASY, 1, 0, TaskTarget.SELF));
        taskList.add(new Task("Trinke zwei", TaskDifficult.MEDIUM, 2, 0, TaskTarget.SELF));
        taskList.add(new Task("Trinke drei", TaskDifficult.HARD, 3, 0, TaskTarget.SELF));
        taskList.add(new Task("Deine Nachbarn trinken einen", TaskDifficult.EASY, 1, 0, TaskTarget.NEIGHBOUR));
        taskList.add(new Task("Sprich eine Runde lang wie ein Wal", TaskDifficult.MEDIUM, 0, 2, TaskTarget.SELF));
        taskList.add(new Task("Wirf eine Münze, bei Kopf trinkst du, bei Zahl alle", TaskDifficult.MEDIUM, 1, 0,
                TaskTarget.COIN));
        taskList.add(new Task("Jeder springt, der letzte trinkt", TaskDifficult.EASY, 1, 0, TaskTarget.CHOOSE_ONE));
        taskList.add(new Task("Zieh eine Grimasse", TaskDifficult.EASY, 0, 1, TaskTarget.SELF));
        taskList.add(new Task("Alle die nicht mehr weiterspielen wollen trinken 3", TaskDifficult.HARD, 0, 0,
                TaskTarget.CHOOSE_ALL));
        taskList.add(new Task("Strecke deine Zunge eine Runde lang aus", TaskDifficult.HARD, 0, 3, TaskTarget.SELF));
        taskList.add(new Task("Trinke einen ohne Hände zu benutzen", TaskDifficult.MEDIUM, 1, 0, TaskTarget.SELF));
        taskList.add(new Task("Lege deinen Finger auf den Boden und renne drei mal drum herum", TaskDifficult.MEDIUM,
                0, 2, TaskTarget.SELF));
        taskList.add(new Task("Sitze eine Runde lang auf dem Boden", TaskDifficult.MEDIUM, 0, 2, TaskTarget.SELF));
        taskList.add(new Task("Lege deinen Finger auf die Stirn von " + LEFT_PLAYER.getToken() + " bis du trinken " +
                "musst. Wenn jemand lacht muss er trinken", TaskDifficult.MEDIUM, 0, 2, TaskTarget.SELF));
        taskList.add(new Task("Jeder mit einer Allergie trinkt", TaskDifficult.EASY, 1, 0, TaskTarget.CHOOSE_ALL));
        taskList.add(new Task("Alle Raucher trinken", TaskDifficult.EASY, 1, 0, TaskTarget.CHOOSE_ALL));
        taskList.add(new Task("Die letzte Person die einen Fisch imitiert trinkt 2", TaskDifficult.MEDIUM, 2, 0,
                TaskTarget.CHOOSE_ALL));
        taskList.add(new Task("Katzenbesitzer trinken", TaskDifficult.EASY, 1, 0, TaskTarget.CHOOSE_ALL));
        taskList.add(new Task("Hundebesitzer trinken", TaskDifficult.EASY, 1, 0, TaskTarget.CHOOSE_ALL));
        taskList.add(new Task("Der Besitzer des Handys trinkt", TaskDifficult.EASY, 1, 0, TaskTarget.CHOOSE_ONE));
        taskList.add(new Task("Iphone besitzer trinken", TaskDifficult.EASY, 1, 0, TaskTarget.CHOOSE_ALL));
        taskList.add(new Task("Game of Thrones Fans trinken, der Rest ist bestraft genug", TaskDifficult.EASY, 1, 0,
                TaskTarget.CHOOSE_ALL));
        taskList.add(new Task("Sag einen Zungenbrecher fehlerfrei", TaskDifficult.EASY, 0, 1, TaskTarget.SELF));
        taskList.add(new Task("Der Gruppen-Freak trinkt", TaskDifficult.EASY, 1, 0, TaskTarget.CHOOSE_ONE));
        taskList.add(new Task("Alle mit Piercings trinken", TaskDifficult.EASY, 1, 0, TaskTarget.CHOOSE_ALL));
        taskList.add(new Task("Bärenalarm:\nMänner(/Frauen) mit Bärten trinken", TaskDifficult.EASY, 1, 0, TaskTarget
                .CHOOSE_ALL));
        taskList.add(new Task("Der mit dem vollsten Glas trinkt 3", TaskDifficult.HARD, 3, 0, TaskTarget.CHOOSE_ONE));
        taskList.add(new Task("Jeder der sich zu Nüchtern fühlt trinkt 2", TaskDifficult.MEDIUM, 2, 0, TaskTarget
                .CHOOSE_ALL));
        taskList.add(new Task("Alle mit Tattos trinken", TaskDifficult.EASY, 1, 0, TaskTarget.CHOOSE_ALL));
        taskList.add(new Task("Der Jüngste trinkt 3", TaskDifficult.HARD, 3, 0, TaskTarget.CHOOSE_ONE));
        taskList.add(new Task("Der Älteste übernimmt Verantwortung und trinkt 3", TaskDifficult.HARD, 3, 0,
                TaskTarget.CHOOSE_ONE));
        taskList.add(new Task("Gestehe deinem linken Nachbarn deine Liebe", TaskDifficult.MEDIUM, 0, 2, TaskTarget
                .SELF));
        taskList.add(new Task("Mach ein Gruppenfoto und sende es deinen Eltern", TaskDifficult.HARD, 0, 3, TaskTarget
                .SELF));
        taskList.add(new Task("Du und deine Nachbarn trinken", TaskDifficult.EASY, 1, 0, TaskTarget
                .SELF_AND_NEIGHBOURS));
        taskList.add(new Task("Jeder der einen Bruder hat trinkt", TaskDifficult.EASY, 1, 0, TaskTarget.CHOOSE_ALL));
        taskList.add(new Task("Jeder mit geradem Alter trinkt", TaskDifficult.EASY, 1, 0, TaskTarget.CHOOSE_ALL));
        taskList.add(new Task("Alle mit Brille trinken", TaskDifficult.EASY, 1, 0, TaskTarget.CHOOSE_ALL));
        taskList.add(new Task("Flirte mit einem Gegenstand", TaskDifficult.MEDIUM, 0, 2, TaskTarget.SELF));
        taskList.add(new Task("Sprich eine Runde lang Englisch", TaskDifficult.MEDIUM, 0, 2, TaskTarget.SELF));
        taskList.add(new Task("Du entscheidest zwei Personen, die sich gegenseitig einen Kurzen geben", TaskDifficult
                .MEDIUM, 1, 0, TaskTarget.CHOOSE_TWO));
        taskList.add(new Task("Dein linker Nachbar denkt sich einen Spitznamen für dich aus. Jeder der dich falsch " +
                "anspricht muss trinken", TaskDifficult.MEDIUM, 0, 0, TaskTarget.SELF));
        taskList.add(new Task("Zeige allen das letzte Bild, was du mit deinem Handy gemacht hast", TaskDifficult
                .HARD, 0, 3, TaskTarget.SELF));
        taskList.add(new Task("Rufe jemanden an und frage wie spät es ist", TaskDifficult.HARD, 0, 3, TaskTarget.SELF));
        taskList.add(new Task("Sage das Alphabet rückwärts auf", TaskDifficult.HARD, 0, 3, TaskTarget.SELF));
        taskList.add(new Task("Sprich eine Runde lang wie ein GaNgStAaH", TaskDifficult.MEDIUM, 0, 2, TaskTarget.SELF));
        taskList.add(new Task("Schau die anderen an und vollende dieses Gedicht: Rosen sind rot, Veilchen sind " +
                "blau...", TaskDifficult.EASY, 0, 1, TaskTarget.SELF));
        taskList.add(new Task("Dein rechter Nachbar macht ein Foto von dir", TaskDifficult.HARD, 0, 3, TaskTarget
                .SELF));
        taskList.add(new Task("Streite dich mit der Wand", TaskDifficult.MEDIUM, 0, 2, TaskTarget.SELF));
        taskList.add(new Task("Trinke soviel wie der Akkustand anzeigt. (100% ganzes Glas)", TaskDifficult.HARD, 1,
                0, TaskTarget.SELF));
        taskList.add(new Task("Sieh dir jetzt eine Werbung an", TaskDifficult.MEDIUM, 0, 2, TaskTarget.AD));
        taskList.add(new Task("Geh auf Klo", TaskDifficult.MEDIUM, 0, 2, TaskTarget.SELF));
        taskList.add(new Task("Stell dich auf ein Bein und rufe Kikeriki", TaskDifficult.MEDIUM, 0, 2, TaskTarget
                .SELF));
        taskList.add(new Task("Stelle " + RIGHT_PLAYER.getToken() + " das Klo vor (Name, Alter, Herkunft)",
                TaskDifficult
                        .MEDIUM, 0, 2, TaskTarget.SELF));
        taskList.add(new Task("Alle ohne Brille trinken", TaskDifficult.EASY, 1, 0, TaskTarget.CHOOSE_ALL));
        taskList.add(new Task("Jeder der keine 10 Euro hat trinkt", TaskDifficult.EASY, 0, 1, TaskTarget.CHOOSE_ALL));
        taskList.add(new Task("Trinke und " + LEFT_PLAYER.getToken() + " darf einen verteilen", TaskDifficult.EASY,
                0, 1,
                TaskTarget.SELF_AND_CHOOSE_ONE));
        taskList.add(new Task("Alle unter 24 trinken 2", TaskDifficult.MEDIUM, 2, 0, TaskTarget.CHOOSE_ALL));
        taskList.add(new Task("Jeder mit ungeradem Alter trinkt", TaskDifficult.EASY, 1, 0, TaskTarget.CHOOSE_ALL));
        taskList.add(new Task("Jeder der eine Schwester hat trinkt", TaskDifficult.EASY, 1, 0, TaskTarget.CHOOSE_ALL));
        taskList.add(new Task("Erzähle einen Witz. Wer lacht trinkt. Lacht keiner, trinkst du 3", TaskDifficult
                .MEDIUM, 1, 3, TaskTarget.CHOOSE_ALL));
        taskList.add(new Task("Rufe jemanden an und gesteh ihm/ihr deine Liebe", TaskDifficult.HARD, 0, 3, TaskTarget
                .SELF));
        taskList.add(new TimedTask("Nenne 15 Biermarken in einer Minute", TaskDifficult.MEDIUM, 2, TaskTarget.SELF,
                new SimpleTask("Geschafft!", 0, TaskTarget.UNDEFINED),
                new SimpleTask("Zu langsam!\nTrink 2", 2, TaskTarget.SELF), 60000));
        taskList.add(new Task("Alle Singles trinken einen", TaskDifficult.EASY, 2, 0, TaskTarget.CHOOSE_ALL));
        taskList.add(new Task("Entscheide wer trinkt", TaskDifficult.EASY, 1, 0, TaskTarget.CHOOSE_ONE));
        taskList.add(new Task("Erzähle ein Liebesgedicht", TaskDifficult.MEDIUM, 0, 2, TaskTarget.SELF));
        taskList.add(new Task("Verbiete ein Wort", TaskDifficult.HARD, 0, 0, TaskTarget.SELF));
        taskList.add(new Task("Du bist jetzt für die nächsten zwei Runden der König. Wer dich anspricht muss " +
                "trinken", TaskDifficult.MEDIUM, 0, 0,
                TaskTarget.SELF));
        taskList.add(new Task("Die Person mit dem längsten Nachnamen trinkt", TaskDifficult.EASY, 1, 0, TaskTarget
                .CHOOSE_ONE));
        taskList.add(new Task("Die Person, die am nächsten am Ausgang sitzt trinkt 2", TaskDifficult.MEDIUM, 2, 0,
                TaskTarget.CHOOSE_ONE));
        taskList.add(new Task("Jeder ausser dir trinkt", TaskDifficult.MEDIUM, 1, 0, TaskTarget.ALL_BUT_SELF));
        taskList.add(new TimedTask("Schaffst du es 10 Wörter die mit x enden in einer Minute zu nennen darfst 3 " +
                "verteilen", TaskDifficult.MEDIUM, 3, TaskTarget.SELF,
                new SimpleTask("Geschafft!\nDu darfst 3 verteilen!", 3, TaskTarget.CHOOSE_THREE),
                new SimpleTask("Verloren!\nTrink 3", 3, TaskTarget.SELF), 60000));
        taskList.add(new Task("Jeder mit einem A im Namen trinkt", TaskDifficult.EASY, 1, 0, TaskTarget.CHOOSE_ALL));
        taskList.add(new Task("Tanze mit " + LEFT_PLAYER.getToken(), TaskDifficult.MEDIUM, 0, 2, TaskTarget.SELF));
        taskList.add(new Task("Imitiere jemanden", TaskDifficult.EASY, 0, 1, TaskTarget.SELF));
        taskList.add(new Task(RIGHT_PLAYER.getToken() + " verpasst dir eine Ohrfeige", TaskDifficult.HARD, 0, 3,
                TaskTarget.SELF));
        taskList.add(new Task("Alle trinken 1 mal", TaskDifficult.EASY, 1, 0, TaskTarget.ALL));
        taskList.add(new Task("Alle trinken 2 mal", TaskDifficult.MEDIUM, 2, 0, TaskTarget.ALL));
        taskList.add(new Task("Alle trinken 3 mal", TaskDifficult.HARD, 3, 0, TaskTarget.ALL));
        taskList.add(new Task("Tausche den Platz mit " + RIGHT_PLAYER.getToken(), TaskDifficult.EASY, 0, 0, TaskTarget
                .SWITCH_PLACE_RIGHT));
        taskList.add(new Task("Tausche den Platz mit " + LEFT_PLAYER.getToken(), TaskDifficult.EASY, 0, 0, TaskTarget
                .SWITCH_PLACE_LEFT));
        taskList.add(new Task("Gib " + RIGHT_PLAYER.getToken() + " eine Massage", TaskDifficult.MEDIUM, 0, 2,
                TaskTarget.SELF));
        taskList.add(new Task(LEFT_PLAYER.getToken() + " liest deine letzten drei Nachrichten vor", TaskDifficult
                .HARD, 0, 3, TaskTarget.SELF));
        taskList.add(new Task("Der Spieler, der am wenigsten getrunken hat (" + WITH_LEAST_DRINKS.getToken() + "), " +
                "trinkt 2", TaskDifficult.MEDIUM, 2, 0, TaskTarget.CHOOSE_ONE));
        taskList.add(new Task(LEFT_PLAYER.getToken() + " trinkt 2", TaskDifficult.MEDIUM, 2, 0, TaskTarget
                .NEIGHBOUR_LEFT));
        taskList.add(new Task("Stehe bis du wieder dran bist auf einem Bein", TaskDifficult.MEDIUM, 0, 2, TaskTarget
                .SELF));
        taskList.add(new Task(RIGHT_PLAYER.getToken() + " trinkt 2", TaskDifficult.MEDIUM, 2, 0, TaskTarget
                .NEIGHBOUR_RIGHT));
        taskList.add(new Task("Der Spieler, der am meisten getrunken hat (" + WITH_MOST_DRINKS.getToken() + "), " +
                "trinkt 2", TaskDifficult.MEDIUM, 2, 0, TaskTarget.CHOOSE_ONE));
        taskList.add(new Task("Trinke ein Glas Wasser", TaskDifficult.EASY, 0, 1, TaskTarget.SELF));
        taskList.add(new Task("Ziehe ein Kleidungsstück aus", TaskDifficult.HARD, 0, 3, TaskTarget.SELF));
        taskList.add(new Task(LEFT_PLAYER.getToken() + " malt dir etwas auf deine Hand", TaskDifficult.MEDIUM, 0, 2,
                TaskTarget.SELF));
        taskList.add(new Task(LEFT_PLAYER.getToken() + " kneift dich", TaskDifficult.MEDIUM, 0, 2, TaskTarget.SELF));
        taskList.add(new TaskEvent("Stelle ein Glas in die Mitte und fülle es mit 2cl eines beliebigen Getränks",
                TaskDifficult.EASY, 0, 0, TaskTarget.TASK_EVENT, new Task[]{
                new Task("Fülle das Glas in der Mitte mit 2cl eines beliebiegen Getränks", TaskDifficult.MEDIUM, 0,
                        0, TaskTarget.UNDEFINED),
                new Task("Fülle das Glas in der Mitte mit 2cl eines beliebiegen Getränks", TaskDifficult.MEDIUM, 0,
                        0, TaskTarget.UNDEFINED),
                new Task("Fülle das Glas in der Mitte mit 2cl eines beliebiegen Getränks", TaskDifficult.MEDIUM, 0,
                        0, TaskTarget.UNDEFINED),
                new Task("Leere das Glas in der Mitte", TaskDifficult.MEDIUM, 4, 0, TaskTarget.SELF)
        }, 2, 4));
        taskList.add(new Task("Alle Männer trinken", TaskDifficult.EASY, 1, 0, TaskTarget.MEN));
        taskList.add(new Task("Arbeitslose trinken", TaskDifficult.EASY, 1, 0, TaskTarget.CHOOSE_ALL));
        taskList.add(new Task("Alle Frauen trinken", TaskDifficult.EASY, 1, 0, TaskTarget.WOMEN));
        taskList.add(new Task("Wenn du es schaffst dein Glas zu exen darfst du 5 verteilen", TaskDifficult.HARD, 5,
                1, TaskTarget.CHOOSE_FIVE));
        taskList.add(new Task("Alle Frauen ziehen ein Kleidungsstück aus oder trinken 3", TaskDifficult.HARD, 3, 0,
                TaskTarget.CHOOSE_ALL));
        taskList.add(new Task("Alle Männer ziehen ein Kleidungsstück aus oder trinken 2", TaskDifficult.HARD, 2, 0,
                TaskTarget.CHOOSE_ALL));
        taskList.add(new Task("Knutsche mit " + RIGHT_PLAYER.getToken() + " rum", TaskDifficult.HARD, 0, 3,
                TaskTarget.SELF));
        taskList.add(new Task("Spiele mit dem Ohrläppchen von " + RIGHT_PLAYER.getToken(), TaskDifficult.MEDIUM, 0, 2,
                TaskTarget.SELF));
        taskList.add(new Task(LEFT_PLAYER.getToken() + " schreibt deinen Status bei Facebook #SaufOMat", TaskDifficult
                .HARD, 0, 3, TaskTarget.SELF));
        taskList.add(new Task("Macht ein Gruppenfoto, wer nicht mit macht trinkt 3", TaskDifficult.EASY, 3, 0,
                TaskTarget.CHOOSE_ALL));
        taskList.add(new Task("Zeig deinen nackten Hintern", TaskDifficult.HARD, 0, 3, TaskTarget.SELF));
        taskList.add(new Task("Zeig deine Brüste", TaskDifficult.HARD, 0, 3, TaskTarget.SELF));
        taskList.add(new Task("Stell deinen Wecker auf 5 Uhr", TaskDifficult.HARD, 0, 5, TaskTarget.SELF));
        taskList.add(new Task("Ziehe alle Jacken an", TaskDifficult.MEDIUM, 0, 2, TaskTarget.SELF));
        taskList.add(new Task("Spiele Luftgittare", TaskDifficult.MEDIUM, 0, 2, TaskTarget.SELF));
        taskList.add(new Task("Die anderen Spieler bestimmen einen Kurzen für dich", TaskDifficult.HARD, 1, 3,
                TaskTarget.SELF));
        taskList.add(new Task("Tausche ein Kleidungsstück mit " + LEFT_PLAYER.getToken(), TaskDifficult.HARD, 0, 3,
                TaskTarget.SELF));
        taskList.add(new Task("Halte einen Vortrag", TaskDifficult.MEDIUM, 0, 2, TaskTarget.SELF));
        taskList.add(new Task("Mache 10 liegestützen", TaskDifficult.MEDIUM, 0, 2, TaskTarget.SELF));
        taskList.add(new Task("Jeder, der das Spiel nicht installiert hat trinkt 2", TaskDifficult.MEDIUM, 2, 0,
                TaskTarget.CHOOSE_ALL));
        taskList.add(new Task("Der letzte der Hans schreit trinkt 2", TaskDifficult.MEDIUM, 2, 0, TaskTarget
                .CHOOSE_ONE));
        taskList.add(new Task("Ruf deine Eltern an", TaskDifficult.HARD, 0, 3, TaskTarget.SELF));
//^Alt(115) vNeu
        taskList.add(new Task("Die schönste Person trinkt 3", TaskDifficult.MEDIUM, 3, 0, TaskTarget.CHOOSE_ONE));
        taskList.add(new Task("Tausche dein Glas mit " + LEFT_PLAYER.getToken(), TaskDifficult.HARD, 0, 4, TaskTarget
                .SELF));
        taskList.add(new Task("Du und deine Nachbarn seid jetzt drei Runden lang Trinkbuddys. Wenn einer trinken " +
                "muss, müssen alle trinken trinken", TaskDifficult.HARD, 0, 0, TaskTarget.SELF_AND_NEIGHBOURS));
        taskList.add(new Task("Such dir einen Trinkbuddy aus für drei Runden. Wenn einer von euch trinken muss, müsst" +
                " ihr beide trinken",
                TaskDifficult.MEDIUM, 0, 0, TaskTarget.SELF));
        taskList.add(new Task("Fansupport:\nJeder, der SaufOMat schon mehr als 7 mal gespielt hat, trinkt 3",
                TaskDifficult.MEDIUM, 3, 0, TaskTarget.CHOOSE_ALL));
        taskList.add(new Task("Einstiegshilfe:\nJeder, der noch nie SaufOMat gespielt hat, trinkt 3", TaskDifficult
                .MEDIUM, 3, 0, TaskTarget.CHOOSE_ALL));
        taskList.add(new TaskEvent("Jeder, der bei der letzten Aufgabe trinken musste, darf jetzt das doppelte " +
                "verteilen",
                TaskDifficult.MEDIUM, 0, 0, TaskTarget.CHOOSE_ALL, new Task[]{
                new Task("Jeder der bei der letzten Aufgabe trinken musste, darf jetzt das doppelte verteilen",
                        TaskDifficult.MEDIUM, 0, 0, TaskTarget.UNDEFINED),
                new Task("Jeder der bei der letzten Aufgabe trinken musste, darf jetzt das doppelte verteilen",
                        TaskDifficult.MEDIUM, 0, 0, TaskTarget.UNDEFINED)}, 1, 1));
        taskList.add(new Task("Trinke dein Glas auf Ex", TaskDifficult.HARD, 1, 0, TaskTarget.SELF));
        taskList.add(new Task("Die Person, die bei seinem ersten mal Saufen am jüngsten war trinkt 2", TaskDifficult
                .MEDIUM, 2, 0, TaskTarget.CHOOSE_ALL));
        taskList.add(new Task("Trinke eine Runde lang nur noch aus fremden Gläsern", TaskDifficult.HARD, 0, 0,
                TaskTarget.SELF));
        taskList.add(new Task("Beantworte eine Runde lang jede Frage die dir gestellt wird", TaskDifficult.MEDIUM, 2,
                0, TaskTarget.CHOOSE_ALL));
        taskList.add(new Task("Drehe drei Runden lang allen den Rücken zu", TaskDifficult.MEDIUM, 0, 0, TaskTarget
                .SELF));
        taskList.add(new Task("Beschimpfe eine Runde lang " + RIGHT_PLAYER.getToken() + " jedes Mal wenn du trinkst, " +
                "du Arschloch!", TaskDifficult.MEDIUM, 0, 0, TaskTarget.SELF));
        taskList.add(new Task("Papagei:\nWiederhole eine Runde lang alles, was " + RIGHT_PLAYER.getToken() + " sagt",
                TaskDifficult.HARD, 0, 0, TaskTarget.SELF));
        taskList.add(new Task("Die Person, die zuerst ihr Glas leert, darf einen bestimmen der sein Glas ext",
                TaskDifficult.HARD, 1, 0, TaskTarget.CHOOSE_ONE));
        taskList.add(new Task("Ihr dürft zwei Runden lang nicht mehr euren Nachbarn in die Augen sehen",
                TaskDifficult.MEDIUM, 0, 0, TaskTarget.SELF));
        taskList.add(new Task("Sag den Geburtstag von " + LEFT_PLAYER.getToken(), TaskDifficult.EASY, 0, 2,
                TaskTarget.SELF));
        taskList.add(new Task("Wenn du es schafst 5 Minuten nichts zu sagen, darfst du 6 verteilen", TaskDifficult
                .HARD, 0, 0, TaskTarget.SELF));
        taskList.add(new Task("Suche ein Tiergeräusch aus, dass eine Runde lang jeder vor dem Trinken machen muss",
                TaskDifficult.EASY, 0, 0, TaskTarget.SELF));
        taskList.add(new Task("Schließe deine Augen. " + RIGHT_PLAYER.getToken() + " muss deine Augenfarbe erraten " +
                "oder 2 trinken", TaskDifficult.EASY, 0, 0, TaskTarget.SELF));
        taskList.add(new Task("Verteile so viele Schlucke, wie du Geschwister hast", TaskDifficult.EASY, 1, 0,
                TaskTarget.CHOOSE_ALL));
        taskList.add(new Task("Setz dich auf den Schoß von " + RIGHT_PLAYER.getToken() + " für drei Runden",
                TaskDifficult.MEDIUM, 0, 2, TaskTarget.SELF));
        taskList.add(new Task("Die Person, die zuletzt ihr Glas gefüllt hat trinkt 2", TaskDifficult.EASY, 2, 0,
                TaskTarget.CHOOSE_ONE));
        taskList.add(new Task("Rede eine Runde lang mit einem Akzent", TaskDifficult.EASY, 0, 2, TaskTarget.SELF));
        taskList.add(new Task("Rauchverbot! Jeder der ab jetzt eine Raucht muss sein Glas vorher leeren",
                TaskDifficult.EASY, 0, 0, TaskTarget.ONCE));
        taskList.add(new Task("Hebe eine Runde lang jedes mal deinen Finger beim reden", TaskDifficult.MEDIUM, 0, 0,
                TaskTarget.SELF));
        taskList.add(new Task("Der Spieler mit dem stärksten Getränk darf 3 verteilen", TaskDifficult.HARD, 3, 0,
                TaskTarget.CHOOSE_THREE));
        taskList.add(new Task("Eine Runde lang wie Yoda sprechen du sollst", TaskDifficult.EASY, 0, 2, TaskTarget
                .SELF));
        taskList.add(new Task("Alle mit einem Alkoholproblem müssen trinken", TaskDifficult.EASY, 1, 0, TaskTarget
                .CHOOSE_ALL));
        taskList.add(new Task("Für drei Runden musst du jedes mal etwas nettes zu jemanden sagen, wenn er trinken " +
                "muss", TaskDifficult.MEDIUM, 0, 0, TaskTarget.UNDEFINED));
        taskList.add(new Task("Toilettenverbot! Bevor jemand auf die Toilette geht, muss er sein Glas leeren",
                TaskDifficult.HARD, 0, 0, TaskTarget.ONCE));
        taskList.add(new Task("Gruppenzwang: Wenn innerhalb der nächsten 4 Runden mehr als die hälfte der Spieler " +
                "trinken muss, müssen alle trinken", TaskDifficult.HARD, 0, 0, TaskTarget.UNDEFINED));
        taskList.add(new Task("Jeder, der bei der letzten Aufgabe nicht trinken musste holt das jetzt doppelt nach",
                TaskDifficult.HARD, 0, 0, TaskTarget.UNDEFINED));
        taskList.add(new Task("Alle, die einen Tanga tragen müssen trinken", TaskDifficult.EASY, 1, 0, TaskTarget
                .CHOOSE_ALL));
        taskList.add(new Task("Alle mit Körbchengröße A müssen trinken", TaskDifficult.EASY, 1, 0, TaskTarget
                .CHOOSE_ALL));
        taskList.add(new Task("Die/Der mit den größten Titten trinkt 2", TaskDifficult.EASY, 2, 0, TaskTarget
                .CHOOSE_ONE));
        taskList.add(new Task("Alle Veganer, die Heute noch nicht erwähnt haben, dass sie Veganer sind müssen einen " +
                "trinken. Die anderen Veganer trinken 3", TaskDifficult.EASY, 0, 0, TaskTarget.UNDEFINED));
        taskList.add(new Task("Wer von euch bis heute am meisten gekotzt hat, macht genau so weiter und Trinkt 5!",
                TaskDifficult.HARD, 0, 0, TaskTarget.CHOOSE_ONE));
        taskList.add(new Task("Der dümmste trinkt 2", TaskDifficult.EASY, 2, 0, TaskTarget.CHOOSE_ONE));
        taskList.add(new Task("Hodor hodor hodor Hodor! (sprich eine Runde lang wie Hodor)", TaskDifficult.MEDIUM, 0,
                2, TaskTarget.SELF));
        taskList.add(new Task("Vorarbeiter:\nFür die nächste Runde muss jeder Trinken, wenn du trinken musst",
                TaskDifficult.HARD, 0, 0, TaskTarget.UNDEFINED));
        taskList.add(new Task("Wilkommenskultur:\nAlle mit ausländischen Wurzeln trinken 2", TaskDifficult.HARD, 2,
                0, TaskTarget.CHOOSE_ALL));
        taskList.add(new Task("Romantiker:\nBeleidige die Person, die du am meisten liebst", TaskDifficult.HARD, 0,
                3, TaskTarget.SELF));
        taskList.add(new Task("\"Jahuuuuu Arschloch!\":\nDu hast jetzt zwei Runden lang Tourette, du Fotze!",
                TaskDifficult.HARD, 0, 3, TaskTarget.SELF));
        taskList.add(new Task("Erzähle eine peinliche Geschichte aus deinem Leben", TaskDifficult.HARD, 0, 3,
                TaskTarget.SELF));
        taskList.add(new Task("Twerke mit deinem bouncy booty", TaskDifficult.MEDIUM, 0, 2, TaskTarget.SELF));
        taskList.add(new Task("Schreibe deiner Mutter, wie oft du jede Woche Pornos guckst oder trinke 3",
                TaskDifficult.HARD, 0, 3, TaskTarget.SELF));
        taskList.add(new Task("Du bist eine Runde lang ein Hund und kannst nicht mehr sprechen nur noch knurren und " +
                "bellen", TaskDifficult.MEDIUM, 0, 2, TaskTarget.SELF));
        taskList.add(new Task("Denke dir ein Gangsta Handzeichen aus, um deine Gang Member zu grüßen. Jeder muss dich" +
                " damit begrüßen, wer es falsch macht trinkt 2", TaskDifficult.MEDIUM, 2, 0, TaskTarget.CHOOSE_ALL));
        taskList.add(new Task("Danke Papa:\nAlle mit Glatze und Geheimratsecken trinken (aber nur einen, die leiden " +
                "eh genug)", TaskDifficult.EASY, 1, 0, TaskTarget.CHOOSE_ALL));
        taskList.add(new Task("Flirten verlernt: Alle die seit mind. 3 Jahren in einer Beziehung sind, trinken 3",
                TaskDifficult.EASY, 1, 0, TaskTarget.CHOOSE_ALL));
        taskList.add(new Task("Dancebattle:\nAlle stehen auf zum Tanzen. Wer am lächerlichsten tanzt, darf 5 " +
                "verteilen", TaskDifficult.HARD, 5, 0, TaskTarget.CHOOSE_ALL));
        taskList.add(new Task("Erzähle das lächerlichste, was du gemacht hast, um jemanden zu beeindrucken " +
                "oder trinke 3", TaskDifficult.HARD, 0, 3, TaskTarget.SELF));
        taskList.add(new Task("Lies folgendes laut vor: \"Ich bin ein dummer Loser ohne SWAG!\" Wer nicht zugehört " +
                "hat trinkt 5. Haben alle zugehört, bist du schon gestraft genug, du dummer Loser haha",
                TaskDifficult.HARD, 5, 0, TaskTarget.CHOOSE_ALL));
        taskList.add(new TimedTask("Reibe 30 Sekunden an deinen Nippeln oder trinke 3",
                TaskDifficult.HARD, 3, TaskTarget.SELF,
                new SimpleTask("Du darfst jetzt aufhören, du Perversling!", 0, TaskTarget.UNDEFINED), 30000));
        taskList.add(new Task("Jeder, der keinen SWAG hat trinkt", TaskDifficult.EASY, 1, 0, TaskTarget.CHOOSE_ALL));
        taskList.add(new Task(LEFT_PLAYER.getToken() + " darf den Namen eines Kontaktes in deinem Handy ändern",
                TaskDifficult.HARD, 0, 3, TaskTarget.SELF));
        taskList.add(new Task("Du darfst für zwei Runden deine Mitspieler nurnoch mit den Namen ihrer Rechten " +
                "Nachbarn ansprechen", TaskDifficult.MEDIUM, 0, 0, TaskTarget.UNDEFINED));
        taskList.add(new Task(LEFT_PLAYER.getToken() + " darf den Namen von einen deiner Kontakte ändern", TaskDifficult
                .HARD, 0, 5, TaskTarget.SELF));
        taskList.add(new Task("Doppelt hält besser:\nTrinke zwei Runden lang das doppelte", TaskDifficult.MEDIUM, 0,
                0, TaskTarget.DOUBLE));
        taskList.add(new Task("Dreifach hält noch besser:\nTrinke drei Runde lang das dreifache", TaskDifficult.HARD,
                0, 0, TaskTarget.TRIPLE));
        taskList.add(new TemporaryTask("Du hast 2 Runden lang Torrette mit Tiernamen", TaskDifficult.MEDIUM, 0, 0,
                TaskTarget.UNDEFINED, 2, "Du bist jetzt vom Torrette geheilt"));
        taskList.add(new Task("Nenne 5 Dinge, die dir Rick Astley niemals antun würde", TaskDifficult.HARD, 0, 3,
                TaskTarget.UNDEFINED));
        taskList.add(new Task("\"Wer würde am ehesten einen fremden Menschen auf der Straße küssen?\" Denke dir eine " +
                "solche Frage aus. Anschließend zeigen alle auf die Person, die dies am ehesten tun würde. Jede " +
                "Person trinkt so oft, wie auf sie gezeigt wurde.",
                TaskDifficult.MEDIUM, 0, 0, TaskTarget.CHOOSE_ONE));
        taskList.add(new Task("Zahle 50 Cent an den Besitzer des Handys", TaskDifficult.MEDIUM, 0, 2, TaskTarget.SELF));
        taskList.add(new Task("Tanze mit einem Gegenstand", TaskDifficult.MEDIUM, 0, 2, TaskTarget.SELF));
        taskList.add(new TimedTask("Du hast 20 Sekunden Zeit um dich 10 Mal um deine eigene Achse zu drehen",
                TaskDifficult.HARD, 4, TaskTarget.UNDEFINED,
                new SimpleTask("Geschafft!", 0, TaskTarget.UNDEFINED),
                new SimpleTask("Zu langsam!\nTrink 4!", 4, TaskTarget.SELF), 20000));
        taskList.add(new TimedTask("Wenn du es schaffst 3 Kurze in 15 Sekunden zu trinken, muss jeder einen Kurzen " +
                "deiner Wahl trinken", TaskDifficult.HARD, 0, TaskTarget.UNDEFINED,
                new SimpleTask("Jeder bekommt einen Kurzen deiner Wahl", 1, TaskTarget.ALL),
                new SimpleTask("Zu langsam!\nDie anderen gehen leer aus", 0, TaskTarget.UNDEFINED), 15000));
        taskList.add(new TimedTask("Wenn du es schaffst ein Glas in 30 Sekunden zu exen, darfst du 5 verteilen",
                TaskDifficult.HARD, 2, TaskTarget.UNDEFINED,
                new SimpleTask("Du darfst 5 verteilen", 5, TaskTarget.CHOOSE_FIVE),
                new SimpleTask("Zu langsam!\nDie anderen gehen leer aus", 0, TaskTarget.UNDEFINED), 30000));
        taskList.add(new TimedTask("Nenne eine Kategorie (wie z.B. Automarken). " + TaskTextToken.RIGHT_PLAYER
                .getToken() + " hat 60 Sekunden Zeit 15 zu nennen. Wenn er/sie es nicht schafft muss er/sie zwei " +
                "trinken",
                TaskDifficult.MEDIUM, 0, TaskTarget.UNDEFINED,
                new SimpleTask("Du darfst trocken bleiben", 0, TaskTarget.UNDEFINED),
                new SimpleTask("Zu langsam!\nTrink zwei", 2, TaskTarget.NEIGHBOUR_RIGHT), 60000));
        taskList.add(new Task("Sing Karaoke mit den nächstbesten Gegenstand", TaskDifficult.EASY, 0, 2, TaskTarget
                .UNDEFINED));
        taskList.add(new Task("Sage im Vornherein, ob du passend 2,50€ an Kleingeld hast. Liegst du falsch musst du 5" +
                " trinken. Liegst du richtig trinken alle außer dir 2", TaskDifficult.HARD, 2, 3, TaskTarget
                .UNDEFINED));
        taskList.add(new TimedTask(TaskTextToken.RIGHT_PLAYER.getToken() + " darf 30 Sekunden lang dein Handy " +
                "durchsuchen", TaskDifficult.HARD, 4, TaskTarget.UNDEFINED, new SimpleTask("Die Zeit ist um!\nGib das" +
                " Handy wieder zurück", 0, TaskTarget.UNDEFINED), 30000));
        taskList.add(new Task("Halte eine Runde lang Händchen mit " + LEFT_PLAYER.getToken(), TaskDifficult.MEDIUM,
                0, 3, TaskTarget.UNDEFINED));
        taskList.add(new Task("Halte eine Runde lang deine Augen geschlossen", TaskDifficult.MEDIUM, 0, 3, TaskTarget
                .UNDEFINED));
        taskList.add(new Task("Gebe das Handy weiter ohne Hände zu benutzen", TaskDifficult.EASY, 0, 0, TaskTarget
                .UNDEFINED));
        taskList.add(new Task("Trinke aus den Glas von " + RIGHT_PLAYER.getToken(), TaskDifficult.HARD, 1, 0,
                TaskTarget.SELF));
        taskList.add(new Task("Haue deinen Kopf auf den Tisch", TaskDifficult.MEDIUM, 0, 3, TaskTarget.UNDEFINED));
        taskList.add(new Task("Deine Füße dürfen zwei Runden lang nicht mehr den Boden berühren", TaskDifficult
                .MEDIUM, 0, 0, TaskTarget.UNDEFINED));
        taskList.add(new Task("Schnür dir deine Schuhe mit den Schnürsenkeln deiner Nachbarn zu", TaskDifficult.HARD,
                0, 3, TaskTarget.UNDEFINED));
        taskList.add(new Task(LEFT_PLAYER.getToken() + " darf dir ein Haar ausziehen", TaskDifficult.HARD, 0, 3,
                TaskTarget.UNDEFINED));
        taskList.add(new Task("Ihr dürft 2 Runden lang Namen nur noch rückwärts aussprechen", TaskDifficult.EASY, 0,
                0, TaskTarget.UNDEFINED));

        //Hauptgewinne
        taskList.add(new Task("Deine Nachbarn trinken drei", TaskDifficult.EASY_WIN, 3, 0, TaskTarget.NEIGHBOUR));
        taskList.add(new Task("Such dir einen Sklaven aus. Er übernimmt eine Runde lang alle deine Aufgaben",
                TaskDifficult.EASY_WIN, 0, 0, TaskTarget.UNDEFINED));
        taskList.add(new Task("Wähle eine Person, die deine nächsten 3 Aufgaben machen muss. Wenn sie trinken muss, " +
                "trinkt sie die doppelte Menge", TaskDifficult.EASY_WIN, 0, 0, TaskTarget.UNDEFINED));
        //^ Alt
        //v Neu
        taskList.add(new Task("Alle ausser dir trinken drei", TaskDifficult.MEDIUM_WIN, 3, 0, TaskTarget.ALL_BUT_SELF));
        taskList.add(new Task("Vorarbeiter:\nFür die nächsten zwei Runden müssen alle trinken, wenn du trinken " +
                "musst", TaskDifficult.MEDIUM_WIN, 0, 0, TaskTarget.UNDEFINED));
        taskList.add(new Task("Suche einen aus der sein Glas leert", TaskDifficult.HARD_WIN, 1, 0, TaskTarget
                .CHOOSE_ONE));
        taskList.add(new Task("Suche drei Mitspieler aus, die einen Kurzen deiner Wahl trinken", TaskDifficult
                .HARD_WIN, 1, 0, TaskTarget.CHOOSE_THREE));
        taskList.add(new Task("Suche dir ein Opfer aus. Dieses Opfer trinkt 3 Runden lang deine Schlucke...\nDoppelt!",
                TaskDifficult.HARD_WIN, 1, 0, TaskTarget.VICTIM));
        taskList.add(new TimedTask("Du hast 30 Sekunden Zeit! Für jeden Kurzen, den du in dieser Zeit trinkst, darfst" +
                " du einen Kurzen deiner Wahl verteilen", TaskDifficult.HARD_WIN, 0, TaskTarget.UNDEFINED,
                new SimpleTask("Die Zeit ist um!\nJetzt darfst du die anderen abfüllen", 0, TaskTarget.UNDEFINED),
                30000));
        taskList.add(new Task("Du darfst jetzt so viele Kurze trinken, wie du willst. Dein linker Nachbar muss " +
                "mindestens genau so viele trinken. Sein linker Nachbar muss mindestens genau so viele trinken wie er" +
                ". usw.", TaskDifficult.HARD_WIN, 0, 0, TaskTarget.UNDEFINED));

        Log.i(TAG, taskList.size() + " tasks defined");
        return taskList;
    }
}
