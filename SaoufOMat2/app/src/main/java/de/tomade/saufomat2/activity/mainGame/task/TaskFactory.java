package de.tomade.saufomat2.activity.mainGame.task;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by woors on 10.03.2016.
 */
public class TaskFactory {
    List<Task> easyTasks;
    List<Task> mediumTasks;
    List<Task> hardTasks;
    List<Task> easyWinTasks;
    List<Task> mediumWinTasks;
    List<Task> hardWinTasks;
    List<Task> allTasks;

    public TaskFactory(){
        easyTasks = new ArrayList<>();
        mediumTasks = new ArrayList<>();
        hardTasks = new ArrayList<>();
        easyWinTasks = new ArrayList<>();
        mediumWinTasks = new ArrayList<>();
        hardWinTasks = new ArrayList<>();
        allTasks = new ArrayList<>();
        initList();
        refreshList(TaskDifficult.EASY);
        refreshList(TaskDifficult.MEDIUM);
        refreshList(TaskDifficult.HARD);
        refreshList(TaskDifficult.EASY_WIN);
        refreshList(TaskDifficult.MEDIUM_WIN);
        refreshList(TaskDifficult.HARD_WIN);
    }

    private void refreshList(TaskDifficult difficult){
        for(Task t: allTasks){
            switch (t.getDifficult()){
                case EASY:
                    if(difficult == TaskDifficult.EASY) {
                        easyTasks.add(t);
                    }
                    break;
                case MEDIUM:
                    if(difficult == TaskDifficult.MEDIUM) {
                        mediumTasks.add(t);
                    }
                    break;
                case HARD:
                    if(difficult == TaskDifficult.HARD) {
                        hardTasks.add(t);
                    }
                    break;
                case EASY_WIN:
                    if(difficult == TaskDifficult.EASY_WIN) {
                        easyWinTasks.add(t);
                    }
                    break;
                case MEDIUM_WIN:
                    if(difficult == TaskDifficult.MEDIUM_WIN) {
                        mediumWinTasks.add(t);
                    }
                    break;
                case HARD_WIN:
                    if(difficult == TaskDifficult.HARD_WIN) {
                        hardWinTasks.add(t);
                    }
                    break;
            }
        }
    }

    private void initList(){
        allTasks.add(new Task("Trinke nichts", TaskDifficult.EASY, 0, 1, TaskTarget.SELF));
        allTasks.add(new Task("Trinke einen", TaskDifficult.EASY, 1, 0, TaskTarget.SELF));
        allTasks.add(new Task("Trinke zwei", TaskDifficult.MEDIUM, 2, 0, TaskTarget.SELF));
        allTasks.add(new Task("Trinke drei", TaskDifficult.HARD, 3, 0, TaskTarget.SELF));
        allTasks.add(new Task("Deine Nachbarn trinken einen", TaskDifficult.EASY, 1, 0, TaskTarget.NEIGHBOUR));
        allTasks.add(new Task("Sprich eine Runde lang wie ein Wal", TaskDifficult.MEDIUM, 0, 2, TaskTarget.SELF));
        allTasks.add(new Task("Wirf eine Münze, bei Kopf trinkst du, bei Zahl alle", TaskDifficult.MEDIUM, 1, 1, TaskTarget.COIN));
        allTasks.add(new Task("Jeder springt, der letzte trinkt", TaskDifficult.EASY, 1, 0, TaskTarget.CHOOSE_ONE));
        allTasks.add(new Task("Zieh eine Grimasse", TaskDifficult.EASY, 0, 1, TaskTarget.SELF));
        allTasks.add(new Task("Alle die nicht mehr weiterspielen wollen trinken 3", TaskDifficult.HARD, 3, 0, TaskTarget.CHOOSE_ALL));
        allTasks.add(new Task("Strecke deine Zunge eine Runde lang aus", TaskDifficult.HARD, 3, 0, TaskTarget.SELF));
        allTasks.add(new Task("Trinke einen ohne Hände zu benutzen", TaskDifficult.MEDIUM, 1, 0, TaskTarget.SELF));
        allTasks.add(new Task("Lege deinen Finger auf den Boden und renne drei mal drum herum", TaskDifficult.MEDIUM, 0, 2, TaskTarget.SELF));
        allTasks.add(new Task("Sitze eine Runde lang auf dem Boden", TaskDifficult.MEDIUM, 0, 2, TaskTarget.SELF));
        allTasks.add(new Task("Lege deinen Finger auf die Stirn deines linken Nachbarn bis du trinken musst. Wenn jemand lacht muss er trinken", TaskDifficult.MEDIUM, 0, 2, TaskTarget.SELF));
        allTasks.add(new Task("Jeder mit einer Allergie trinkt", TaskDifficult.EASY, 1, 0, TaskTarget.CHOOSE_ALL));
        allTasks.add(new Task("Alle Raucher trinken", TaskDifficult.EASY, 1, 0, TaskTarget.CHOOSE_ALL));
        allTasks.add(new Task("Die letzte Person die einen Fisch imitiert trinkt 2", TaskDifficult.MEDIUM, 2, 0, TaskTarget.CHOOSE_ALL));
        allTasks.add(new Task("Katzenbesitzer trinken", TaskDifficult.EASY, 1, 0, TaskTarget.CHOOSE_ALL));
        allTasks.add(new Task("Hundebesitzer trinken", TaskDifficult.EASY, 1, 0, TaskTarget.CHOOSE_ALL));
        allTasks.add(new Task("Der Besitzer des Handys trinkt", TaskDifficult.EASY, 1, 0, TaskTarget.CHOOSE_ONE));
        allTasks.add(new Task("Iphone besitzer trinken", TaskDifficult.EASY, 1, 0, TaskTarget.CHOOSE_ALL));
        allTasks.add(new Task("Game of Thrones Fans trinken, der Rest ist bestraft genug", TaskDifficult.EASY, 1, 0, TaskTarget.CHOOSE_ALL));
        allTasks.add(new Task("Sag einen Zungenbrecher fehlerfrei", TaskDifficult.EASY, 0, 1, TaskTarget.SELF));
        allTasks.add(new Task("Der Gruppen-Freak trinkt", TaskDifficult.EASY, 1, 0, TaskTarget.CHOOSE_ONE));
        allTasks.add(new Task("Alle mit Piercings trinken", TaskDifficult.EASY, 1, 0, TaskTarget.CHOOSE_ALL));
        allTasks.add(new Task("Männer(/Frauen) mit Bärten trinken", TaskDifficult.EASY, 1, 0, TaskTarget.CHOOSE_ALL));
        allTasks.add(new Task("Der mit dem vollsten Glas trinkt 3", TaskDifficult.HARD, 3, 0, TaskTarget.CHOOSE_ONE));
        allTasks.add(new Task("Jeder der sich zu Nüchtern fühlt trinkt 2", TaskDifficult.MEDIUM, 2, 0, TaskTarget.CHOOSE_ALL));
        allTasks.add(new Task("Alle mit Tattos trinken", TaskDifficult.EASY, 1, 0, TaskTarget.CHOOSE_ALL));
        allTasks.add(new Task("Der Jüngste trinkt 3", TaskDifficult.HARD, 3, 0, TaskTarget.CHOOSE_ONE));
        allTasks.add(new Task("Der Älteste übernimmt Verantwortung und trinkt 3", TaskDifficult.HARD, 3, 0, TaskTarget.CHOOSE_ONE));
        allTasks.add(new Task("Gestehe deinem linken Nachbarn deine Liebe", TaskDifficult.MEDIUM, 0, 2, TaskTarget.SELF));
        allTasks.add(new Task("Mach ein Gruppenfoto und sende es deinen Eltern", TaskDifficult.HARD, 0, 3, TaskTarget.SELF));
        allTasks.add(new Task("Du und deine Nachbarn trinken", TaskDifficult.EASY, 1, 0, TaskTarget.SELF_AND_NEIGHBOURS));
        allTasks.add(new Task("Jeder der einen Bruder hat trinkt", TaskDifficult.EASY, 1, 0, TaskTarget.CHOOSE_ALL));
        allTasks.add(new Task("Jeder mit geradem Alter trinkt", TaskDifficult.EASY, 1, 0, TaskTarget.CHOOSE_ALL));
        allTasks.add(new Task("Alle mit Brille trinken", TaskDifficult.EASY, 1, 0, TaskTarget.CHOOSE_ALL));
        allTasks.add(new Task("Flirte mit einem Gegenstand", TaskDifficult.MEDIUM, 0, 2, TaskTarget.SELF));
        allTasks.add(new Task("Sprich eine Runde lang Englisch", TaskDifficult.MEDIUM, 0, 2, TaskTarget.SELF));
        allTasks.add(new Task("Du entscheidest zwei Personen, die sich gegenseitig einen Kurzen geben", TaskDifficult.MEDIUM, 1, 0, TaskTarget.CHOOSE_TWO));
        allTasks.add(new Task("Dein linker Nachbar denkt sich einen Spitznamen für dich aus. Jeder der dich falsch anspricht muss trinken", TaskDifficult.MEDIUM, 0, 0, TaskTarget.SELF));
        allTasks.add(new Task("Zeige allen das letzte Bild, was du mit deinem Handy gemacht hast", TaskDifficult.HARD, 0, 3, TaskTarget.SELF));
        allTasks.add(new Task("Rufe jemanden an und frage wie spät es ist", TaskDifficult.HARD, 0, 3, TaskTarget.SELF));
        allTasks.add(new Task("Sage das Alphabet rückwärts auf", TaskDifficult.HARD, 0, 3, TaskTarget.SELF));
        allTasks.add(new Task("Sprich eine Runde lang wie ein GaNgStAaH", TaskDifficult.MEDIUM, 0, 2, TaskTarget.SELF));
        allTasks.add(new Task("Schau die anderen an und vollende dieses Gedicht: Rosen sind rot, Veilchen sind blau...", TaskDifficult.EASY, 0, 1, TaskTarget.SELF));
        allTasks.add(new Task("Dein rechter Nachbar macht ein Foto von dir", TaskDifficult.HARD, 0, 3, TaskTarget.SELF));
        allTasks.add(new Task("Streite dich mit der Wand", TaskDifficult.MEDIUM, 0, 2, TaskTarget.SELF));
        allTasks.add(new Task("Trinke soviel wie der Akkustand anzeigt. (100% ganzes Glas)", TaskDifficult.MEDIUM, 1, 0, TaskTarget.SELF));
        allTasks.add(new Task("Sieh dir jetzt eine Werbung an", TaskDifficult.MEDIUM, 0, 2, TaskTarget.SELF));
        allTasks.add(new Task("Geh auf Klo", TaskDifficult.MEDIUM, 0, 2, TaskTarget.SELF));
        allTasks.add(new Task("Stell dich auf ein Bein und rufe Kikeriki", TaskDifficult.MEDIUM, 0, 2, TaskTarget.SELF));
        allTasks.add(new Task("Stelle deinem rechten Nachbarn das Klo vor (Name, Alter, Herkunft)", TaskDifficult.MEDIUM, 0, 2, TaskTarget.SELF));
        allTasks.add(new Task("Alle ohne Brille trinken", TaskDifficult.EASY, 1, 0, TaskTarget.CHOOSE_ALL));
        allTasks.add(new Task("Jeder der keine 10 Euro hat trinkt", TaskDifficult.EASY, 0, 1, TaskTarget.CHOOSE_ALL));
        allTasks.add(new Task("Trinke und dein linker Nachbar darf einen verteilen", TaskDifficult.EASY, 0, 1, TaskTarget.SELF_AND_CHOOSE_ONE));
        allTasks.add(new Task("Alle unter 21 trinken 2", TaskDifficult.MEDIUM, 2, 0, TaskTarget.CHOOSE_ALL));
        allTasks.add(new Task("Jeder mit ungeraden Alter trinkt", TaskDifficult.EASY, 1, 0, TaskTarget.CHOOSE_ALL));
        allTasks.add(new Task("Jeder der eine Schwester hat trinkt", TaskDifficult.EASY, 1, 0, TaskTarget.CHOOSE_ALL));
        allTasks.add(new Task("Erzähle einen Witz. Wer lacht trinkt. Lacht keiner, trinkst du 3", TaskDifficult.MEDIUM, 1, 3, TaskTarget.CHOOSE_ALL));
        allTasks.add(new Task("Rufe jemanden an und gesteh ihm/ihr deine Liebe", TaskDifficult.HARD, 0, 3, TaskTarget.SELF));
        allTasks.add(new Task("Nenne 15 Biermarken in einer Minute", TaskDifficult.MEDIUM, 0, 2, TaskTarget.SELF));
        allTasks.add(new Task("Alle Singles trinken einen", TaskDifficult.EASY, 2, 0, TaskTarget.CHOOSE_ALL));
        allTasks.add(new Task("Entscheide wer trinkt", TaskDifficult.EASY, 1, 0, TaskTarget.CHOOSE_ONE));
        allTasks.add(new Task("Erzähle ein Liebesgedicht", TaskDifficult.MEDIUM, 0, 2, TaskTarget.SELF));
        allTasks.add(new Task("Verbiete ein Wort", TaskDifficult.HARD, 0, 0, TaskTarget.SELF));
        allTasks.add(new Task("Du bist jetzt der König. Wer dich anspricht muss trinken", TaskDifficult.MEDIUM, 0, 0, TaskTarget.SELF));
        allTasks.add(new Task("Die Person mit dem längsten Nachnamen trinkt", TaskDifficult.EASY, 1, 0, TaskTarget.CHOOSE_ONE));
        allTasks.add(new Task("Die Person, die am nächsten am Ausgang sitzt trinkt 2", TaskDifficult.MEDIUM, 2, 0, TaskTarget.CHOOSE_ONE));
        allTasks.add(new Task("Jeder ausser dir trinkt", TaskDifficult.MEDIUM, 1, 0, TaskTarget.ALL_BUT_SELF));
        allTasks.add(new Task("Nenne 10 Wörter die mit x enden in einer Minute", TaskDifficult.MEDIUM, 0, 3, TaskTarget.SELF));
        allTasks.add(new Task("Jeder mit einem A im Namen trinkt", TaskDifficult.EASY, 1, 0, TaskTarget.CHOOSE_ALL));
        allTasks.add(new Task("Tanze mit deinem linken Nachbarn", TaskDifficult.MEDIUM, 0, 2, TaskTarget.SELF));
        allTasks.add(new Task("Imitiere jemanden", TaskDifficult.EASY, 0, 1, TaskTarget.SELF));
        allTasks.add(new Task("Dein rechter Nachbar verpasst dir eine Ohrfeige", TaskDifficult.HARD, 0, 3, TaskTarget.SELF));
        allTasks.add(new Task("Alle trinken 1 mal", TaskDifficult.EASY, 1, 0, TaskTarget.ALL));
        allTasks.add(new Task("Alle trinken 2 mal", TaskDifficult.MEDIUM, 2, 0, TaskTarget.ALL));
        allTasks.add(new Task("Alle trinken 3 mal", TaskDifficult.HARD, 3, 0, TaskTarget.ALL));
        allTasks.add(new Task("Tausche den Platz mit deinem rechten Nachbarn", TaskDifficult.EASY, 0, 0, TaskTarget.SWITCH_PLACE_RIGHT));
        allTasks.add(new Task("Tausche den Platz mit deinem linken Nachbarn", TaskDifficult.EASY, 0, 0, TaskTarget.SWITCH_PLACE_LEFT));
        allTasks.add(new Task("Gib deinem rechten Nachbarn eine Massage", TaskDifficult.MEDIUM, 0, 2, TaskTarget.SELF));
        allTasks.add(new Task("Dein linker Nachbar liest deine letzten drei Nachrichten vor", TaskDifficult.HARD, 0, 3, TaskTarget.SELF));
        allTasks.add(new Task("Der Spieler, der am wenigsten getrunken hat, trinkt 2", TaskDifficult.MEDIUM, 2, 0, TaskTarget.CHOOSE_ONE));
        allTasks.add(new Task("Dein linker Nachbar trinkt 2", TaskDifficult.MEDIUM, 2, 0, TaskTarget.NEIGHBOUR_LEFT));
        allTasks.add(new Task("Stehe bis du wieder dran bist auf einem Bein", TaskDifficult.MEDIUM, 0, 2, TaskTarget.SELF));
        allTasks.add(new Task("Dein rechter Nachbar trinkt 2", TaskDifficult.MEDIUM, 2, 0, TaskTarget.NEIGHBOUR_RIGHT));
        allTasks.add(new Task("Der Spieler, der am meisten getrunken hat, trinkt 2", TaskDifficult.MEDIUM, 2, 0, TaskTarget.CHOOSE_ONE));
        allTasks.add(new Task("Trinke ein Glas Wasser", TaskDifficult.EASY, 0, 1, TaskTarget.SELF));
        allTasks.add(new Task("Ziehe ein Kleidungsstück aus", TaskDifficult.HARD, 0, 3, TaskTarget.SELF));
        allTasks.add(new Task("Dein linker Nachbar malt dir etwas auf deine Hand", TaskDifficult.MEDIUM, 0, 2, TaskTarget.SELF));
        allTasks.add(new Task("Dein linker Nachbar kneift dich", TaskDifficult.MEDIUM, 0, 2, TaskTarget.SELF));
        allTasks.add(new Task("Stelle ein Glas in die Mitte und fülle es mit 2cl eines beliebigen Getränks", TaskDifficult.EASY, 0, 0, TaskTarget.SELF));
        allTasks.add(new Task("Stelle ein Glas in die Mitte und fülle es mit 2cl eines beliebigen Getränks", TaskDifficult.EASY, 0, 0, TaskTarget.SELF));
        allTasks.add(new Task("Stelle ein Glas in die Mitte und fülle es mit 2cl eines beliebigen Getränks", TaskDifficult.EASY, 0, 0, TaskTarget.SELF));
        allTasks.add(new Task("Stelle ein Glas in die Mitte und fülle es mit 2cl eines beliebigen Getränks", TaskDifficult.EASY, 0, 0, TaskTarget.SELF));
        allTasks.add(new Task("Leere das Glas in der Mitte und fülle 2cl eines beliebigen Getränks ein", TaskDifficult.EASY, 1, 0, TaskTarget.SELF));
        allTasks.add(new Task("Alle Männer trinken", TaskDifficult.EASY, 1, 0, TaskTarget.MEN));
        allTasks.add(new Task("Arbeitslose trinken", TaskDifficult.EASY, 1, 0, TaskTarget.CHOOSE_ALL));
        allTasks.add(new Task("Alle Frauen trinken", TaskDifficult.EASY, 1, 0, TaskTarget.WOMEN));
        allTasks.add(new Task("Wenn du es schaffst dein Glas zu exen darfst du 5 verteilen", TaskDifficult.HARD, 5, 1, TaskTarget.CHOOSE_FIVE));
        allTasks.add(new Task("Alle Frauen ziehen ein Kleidungsstück aus oder trinken 3", TaskDifficult.HARD, 3, 0, TaskTarget.CHOOSE_ALL));
        allTasks.add(new Task("Alle Männer ziehen ein Kleidungsstück aus oder trinken 2", TaskDifficult.HARD, 2, 0, TaskTarget.CHOOSE_ALL));
        allTasks.add(new Task("Knutsche mit deinem rechten Nachbarn rum", TaskDifficult.HARD, 0, 3, TaskTarget.SELF));
        allTasks.add(new Task("Spiele mit dem Ohrläppchen von deinem linken Nachbarn", TaskDifficult.MEDIUM, 0, 2, TaskTarget.SELF));
        allTasks.add(new Task("Dein linker Nachbarn schreibt deinen Status bei Facebook", TaskDifficult.HARD, 0, 3, TaskTarget.SELF));
        allTasks.add(new Task("Macht ein Gruppenfoto, wer nicht mit macht trinkt 3", TaskDifficult.EASY, 3, 0, TaskTarget.CHOOSE_ALL));
        allTasks.add(new Task("Zeig deinen nackten Hintern", TaskDifficult.HARD, 0, 3, TaskTarget.SELF));
        allTasks.add(new Task("Zeig deine Brüste", TaskDifficult.HARD, 0, 3, TaskTarget.SELF));
        allTasks.add(new Task("Stell deinen Wecker auf 5 Uhr", TaskDifficult.HARD, 0, 5, TaskTarget.SELF));
        allTasks.add(new Task("Ziehe alle Jacken an", TaskDifficult.MEDIUM, 0, 2, TaskTarget.SELF));
        allTasks.add(new Task("Spiele Luftgittare", TaskDifficult.MEDIUM, 0, 2, TaskTarget.SELF));
        allTasks.add(new Task("Die anderen Spieler bestimmen einen Schnaps für dich", TaskDifficult.HARD, 1, 3, TaskTarget.SELF));
        allTasks.add(new Task("Tausche ein Kleidungsstück mit deinem linken Nachbarn", TaskDifficult.HARD, 0, 3, TaskTarget.SELF));
        allTasks.add(new Task("Halte einen Vortrag", TaskDifficult.MEDIUM, 0, 2, TaskTarget.SELF));
        allTasks.add(new Task("Mache 10 liegestützen", TaskDifficult.MEDIUM, 0, 2, TaskTarget.SELF));
        allTasks.add(new Task("Jeder, der das Spiel nicht installiert hat trinkt 2", TaskDifficult.MEDIUM, 2, 0, TaskTarget.CHOOSE_ALL));
        allTasks.add(new Task("Der letzte der Hans schreit trinkt 2", TaskDifficult.MEDIUM, 2, 0, TaskTarget.CHOOSE_ONE));
        allTasks.add(new Task("Ruf deine Eltern an", TaskDifficult.HARD, 0, 3, TaskTarget.SELF));

        allTasks.add(new Task("Deine Nachbarn trinken drei", TaskDifficult.EASY_WIN, 3, 0, TaskTarget.NEIGHBOUR));
        allTasks.add(new Task("Alle ausser dir trinken drei", TaskDifficult.MEDIUM_WIN, 3, 0 ,TaskTarget.ALL_BUT_SELF));
        allTasks.add(new Task("Suche drei Mitspieler aus, die einen Kurzen deiner Wahl trinken", TaskDifficult.HARD_WIN, 1, 0, TaskTarget.CHOOSE_THREE));
    }

    public Task getTask(TaskDifficult difficult){
        Random rnd = new Random();
        int index;
        switch (difficult){
            case EASY:
                index = rnd.nextInt(easyTasks.size());
                return easyTasks.get(index);
            case MEDIUM:
                index = rnd.nextInt(mediumTasks.size());
                return mediumTasks.get(index);
            case HARD:
                index = rnd.nextInt(hardTasks.size());
                return hardTasks.get(index);
            case EASY_WIN:
                index = rnd.nextInt(easyWinTasks.size());
                return easyWinTasks.get(index);
            case MEDIUM_WIN:
                index = rnd.nextInt(mediumWinTasks.size());
                return mediumWinTasks.get(index);
            case HARD_WIN:
                index = rnd.nextInt(hardWinTasks.size());
                return hardWinTasks.get(index);
        }
        return null;
    }

    private void removeTask(Task t){
        switch (t.getDifficult()){
            case EASY:
                easyTasks.remove(t);
                if(easyTasks.size() <= 1){
                    refreshList(TaskDifficult.EASY);
                }
                break;
            case MEDIUM:
                mediumTasks.remove(t);
                if(mediumTasks.size() <= 1){
                    refreshList(TaskDifficult.MEDIUM);
                }
                break;
            case HARD:
                hardTasks.remove(t);
                if(hardTasks.size() <= 1){
                    refreshList(TaskDifficult.HARD);
                }
                break;
        }
    }
}
