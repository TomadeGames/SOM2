package de.tomade.saufomat2.activity.mainGame.task;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by woors on 10.03.2016.
 */
public class TaskFactory {
    private List<Task> easyTasks;
    private List<Task> mediumTasks;
    private List<Task> hardTasks;
    private List<Task> easyWinTasks;
    private List<Task> mediumWinTasks;
    private List<Task> hardWinTasks;
    private List<Task> allTasks;

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
        for(Task t: getAllTasks()){
            switch (t.getDifficult()){
                case EASY:
                    if(difficult == TaskDifficult.EASY) {
                        getEasyTasks().add(t);
                    }
                    break;
                case MEDIUM:
                    if(difficult == TaskDifficult.MEDIUM) {
                        getMediumTasks().add(t);
                    }
                    break;
                case HARD:
                    if(difficult == TaskDifficult.HARD) {
                        getHardTasks().add(t);
                    }
                    break;
                case EASY_WIN:
                    if(difficult == TaskDifficult.EASY_WIN) {
                        getEasyWinTasks().add(t);
                    }
                    break;
                case MEDIUM_WIN:
                    if(difficult == TaskDifficult.MEDIUM_WIN) {
                        getMediumWinTasks().add(t);
                    }
                    break;
                case HARD_WIN:
                    if(difficult == TaskDifficult.HARD_WIN) {
                        getHardWinTasks().add(t);
                    }
                    break;
            }
        }
    }

    private void initList(){
        getAllTasks().add(new Task("Trinke nichts", TaskDifficult.EASY, 0, 1, TaskTarget.SELF));
        getAllTasks().add(new Task("Trinke einen", TaskDifficult.EASY, 1, 0, TaskTarget.SELF));
        getAllTasks().add(new Task("Trinke zwei", TaskDifficult.MEDIUM, 2, 0, TaskTarget.SELF));
        getAllTasks().add(new Task("Trinke drei", TaskDifficult.HARD, 3, 0, TaskTarget.SELF));
        getAllTasks().add(new Task("Deine Nachbarn trinken einen", TaskDifficult.EASY, 1, 0, TaskTarget.NEIGHBOUR));
        getAllTasks().add(new Task("Sprich eine Runde lang wie ein Wal", TaskDifficult.MEDIUM, 0, 2, TaskTarget.SELF));
        getAllTasks().add(new Task("Wirf eine Münze, bei Kopf trinkst du, bei Zahl alle", TaskDifficult.MEDIUM, 1, 1, TaskTarget.COIN));
        getAllTasks().add(new Task("Jeder springt, der letzte trinkt", TaskDifficult.EASY, 1, 0, TaskTarget.CHOOSE_ONE));
        getAllTasks().add(new Task("Zieh eine Grimasse", TaskDifficult.EASY, 0, 1, TaskTarget.SELF));
        getAllTasks().add(new Task("Alle die nicht mehr weiterspielen wollen trinken 3", TaskDifficult.HARD, 3, 0, TaskTarget.CHOOSE_ALL));
        getAllTasks().add(new Task("Strecke deine Zunge eine Runde lang aus", TaskDifficult.HARD, 3, 0, TaskTarget.SELF));
        getAllTasks().add(new Task("Trinke einen ohne Hände zu benutzen", TaskDifficult.MEDIUM, 1, 0, TaskTarget.SELF));
        getAllTasks().add(new Task("Lege deinen Finger auf den Boden und renne drei mal drum herum", TaskDifficult.MEDIUM, 0, 2, TaskTarget.SELF));
        getAllTasks().add(new Task("Sitze eine Runde lang auf dem Boden", TaskDifficult.MEDIUM, 0, 2, TaskTarget.SELF));
        getAllTasks().add(new Task("Lege deinen Finger auf die Stirn deines linken Nachbarn bis du trinken musst. Wenn jemand lacht muss er trinken", TaskDifficult.MEDIUM, 0, 2, TaskTarget.SELF));
        getAllTasks().add(new Task("Jeder mit einer Allergie trinkt", TaskDifficult.EASY, 1, 0, TaskTarget.CHOOSE_ALL));
        getAllTasks().add(new Task("Alle Raucher trinken", TaskDifficult.EASY, 1, 0, TaskTarget.CHOOSE_ALL));
        getAllTasks().add(new Task("Die letzte Person die einen Fisch imitiert trinkt 2", TaskDifficult.MEDIUM, 2, 0, TaskTarget.CHOOSE_ALL));
        getAllTasks().add(new Task("Katzenbesitzer trinken", TaskDifficult.EASY, 1, 0, TaskTarget.CHOOSE_ALL));
        getAllTasks().add(new Task("Hundebesitzer trinken", TaskDifficult.EASY, 1, 0, TaskTarget.CHOOSE_ALL));
        getAllTasks().add(new Task("Der Besitzer des Handys trinkt", TaskDifficult.EASY, 1, 0, TaskTarget.CHOOSE_ONE));
        getAllTasks().add(new Task("Iphone besitzer trinken", TaskDifficult.EASY, 1, 0, TaskTarget.CHOOSE_ALL));
        getAllTasks().add(new Task("Game of Thrones Fans trinken, der Rest ist bestraft genug", TaskDifficult.EASY, 1, 0, TaskTarget.CHOOSE_ALL));
        getAllTasks().add(new Task("Sag einen Zungenbrecher fehlerfrei", TaskDifficult.EASY, 0, 1, TaskTarget.SELF));
        getAllTasks().add(new Task("Der Gruppen-Freak trinkt", TaskDifficult.EASY, 1, 0, TaskTarget.CHOOSE_ONE));
        getAllTasks().add(new Task("Alle mit Piercings trinken", TaskDifficult.EASY, 1, 0, TaskTarget.CHOOSE_ALL));
        getAllTasks().add(new Task("Männer(/Frauen) mit Bärten trinken", TaskDifficult.EASY, 1, 0, TaskTarget.CHOOSE_ALL));
        getAllTasks().add(new Task("Der mit dem vollsten Glas trinkt 3", TaskDifficult.HARD, 3, 0, TaskTarget.CHOOSE_ONE));
        getAllTasks().add(new Task("Jeder der sich zu Nüchtern fühlt trinkt 2", TaskDifficult.MEDIUM, 2, 0, TaskTarget.CHOOSE_ALL));
        getAllTasks().add(new Task("Alle mit Tattos trinken", TaskDifficult.EASY, 1, 0, TaskTarget.CHOOSE_ALL));
        getAllTasks().add(new Task("Der Jüngste trinkt 3", TaskDifficult.HARD, 3, 0, TaskTarget.CHOOSE_ONE));
        getAllTasks().add(new Task("Der Älteste übernimmt Verantwortung und trinkt 3", TaskDifficult.HARD, 3, 0, TaskTarget.CHOOSE_ONE));
        getAllTasks().add(new Task("Gestehe deinem linken Nachbarn deine Liebe", TaskDifficult.MEDIUM, 0, 2, TaskTarget.SELF));
        getAllTasks().add(new Task("Mach ein Gruppenfoto und sende es deinen Eltern", TaskDifficult.HARD, 0, 3, TaskTarget.SELF));
        getAllTasks().add(new Task("Du und deine Nachbarn trinken", TaskDifficult.EASY, 1, 0, TaskTarget.SELF_AND_NEIGHBOURS));
        getAllTasks().add(new Task("Jeder der einen Bruder hat trinkt", TaskDifficult.EASY, 1, 0, TaskTarget.CHOOSE_ALL));
        getAllTasks().add(new Task("Jeder mit geradem Alter trinkt", TaskDifficult.EASY, 1, 0, TaskTarget.CHOOSE_ALL));
        getAllTasks().add(new Task("Alle mit Brille trinken", TaskDifficult.EASY, 1, 0, TaskTarget.CHOOSE_ALL));
        getAllTasks().add(new Task("Flirte mit einem Gegenstand", TaskDifficult.MEDIUM, 0, 2, TaskTarget.SELF));
        getAllTasks().add(new Task("Sprich eine Runde lang Englisch", TaskDifficult.MEDIUM, 0, 2, TaskTarget.SELF));
        getAllTasks().add(new Task("Du entscheidest zwei Personen, die sich gegenseitig einen Kurzen geben", TaskDifficult.MEDIUM, 1, 0, TaskTarget.CHOOSE_TWO));
        getAllTasks().add(new Task("Dein linker Nachbar denkt sich einen Spitznamen für dich aus. Jeder der dich falsch anspricht muss trinken", TaskDifficult.MEDIUM, 0, 0, TaskTarget.SELF));
        getAllTasks().add(new Task("Zeige allen das letzte Bild, was du mit deinem Handy gemacht hast", TaskDifficult.HARD, 0, 3, TaskTarget.SELF));
        getAllTasks().add(new Task("Rufe jemanden an und frage wie spät es ist", TaskDifficult.HARD, 0, 3, TaskTarget.SELF));
        getAllTasks().add(new Task("Sage das Alphabet rückwärts auf", TaskDifficult.HARD, 0, 3, TaskTarget.SELF));
        getAllTasks().add(new Task("Sprich eine Runde lang wie ein GaNgStAaH", TaskDifficult.MEDIUM, 0, 2, TaskTarget.SELF));
        getAllTasks().add(new Task("Schau die anderen an und vollende dieses Gedicht: Rosen sind rot, Veilchen sind blau...", TaskDifficult.EASY, 0, 1, TaskTarget.SELF));
        getAllTasks().add(new Task("Dein rechter Nachbar macht ein Foto von dir", TaskDifficult.HARD, 0, 3, TaskTarget.SELF));
        getAllTasks().add(new Task("Streite dich mit der Wand", TaskDifficult.MEDIUM, 0, 2, TaskTarget.SELF));
        getAllTasks().add(new Task("Trinke soviel wie der Akkustand anzeigt. (100% ganzes Glas)", TaskDifficult.MEDIUM, 1, 0, TaskTarget.SELF));
        getAllTasks().add(new Task("Sieh dir jetzt eine Werbung an", TaskDifficult.MEDIUM, 0, 2, TaskTarget.SELF));
        getAllTasks().add(new Task("Geh auf Klo", TaskDifficult.MEDIUM, 0, 2, TaskTarget.SELF));
        getAllTasks().add(new Task("Stell dich auf ein Bein und rufe Kikeriki", TaskDifficult.MEDIUM, 0, 2, TaskTarget.SELF));
        getAllTasks().add(new Task("Stelle deinem rechten Nachbarn das Klo vor (Name, Alter, Herkunft)", TaskDifficult.MEDIUM, 0, 2, TaskTarget.SELF));
        getAllTasks().add(new Task("Alle ohne Brille trinken", TaskDifficult.EASY, 1, 0, TaskTarget.CHOOSE_ALL));
        getAllTasks().add(new Task("Jeder der keine 10 Euro hat trinkt", TaskDifficult.EASY, 0, 1, TaskTarget.CHOOSE_ALL));
        getAllTasks().add(new Task("Trinke und dein linker Nachbar darf einen verteilen", TaskDifficult.EASY, 0, 1, TaskTarget.SELF_AND_CHOOSE_ONE));
        getAllTasks().add(new Task("Alle unter 21 trinken 2", TaskDifficult.MEDIUM, 2, 0, TaskTarget.CHOOSE_ALL));
        getAllTasks().add(new Task("Jeder mit ungeraden Alter trinkt", TaskDifficult.EASY, 1, 0, TaskTarget.CHOOSE_ALL));
        getAllTasks().add(new Task("Jeder der eine Schwester hat trinkt", TaskDifficult.EASY, 1, 0, TaskTarget.CHOOSE_ALL));
        getAllTasks().add(new Task("Erzähle einen Witz. Wer lacht trinkt. Lacht keiner, trinkst du 3", TaskDifficult.MEDIUM, 1, 3, TaskTarget.CHOOSE_ALL));
        getAllTasks().add(new Task("Rufe jemanden an und gesteh ihm/ihr deine Liebe", TaskDifficult.HARD, 0, 3, TaskTarget.SELF));
        getAllTasks().add(new Task("Nenne 15 Biermarken in einer Minute", TaskDifficult.MEDIUM, 0, 2, TaskTarget.SELF));
        getAllTasks().add(new Task("Alle Singles trinken einen", TaskDifficult.EASY, 2, 0, TaskTarget.CHOOSE_ALL));
        getAllTasks().add(new Task("Entscheide wer trinkt", TaskDifficult.EASY, 1, 0, TaskTarget.CHOOSE_ONE));
        getAllTasks().add(new Task("Erzähle ein Liebesgedicht", TaskDifficult.MEDIUM, 0, 2, TaskTarget.SELF));
        getAllTasks().add(new Task("Verbiete ein Wort", TaskDifficult.HARD, 0, 0, TaskTarget.SELF));
        getAllTasks().add(new Task("Du bist jetzt der König. Wer dich anspricht muss trinken", TaskDifficult.MEDIUM, 0, 0, TaskTarget.SELF));
        getAllTasks().add(new Task("Die Person mit dem längsten Nachnamen trinkt", TaskDifficult.EASY, 1, 0, TaskTarget.CHOOSE_ONE));
        getAllTasks().add(new Task("Die Person, die am nächsten am Ausgang sitzt trinkt 2", TaskDifficult.MEDIUM, 2, 0, TaskTarget.CHOOSE_ONE));
        getAllTasks().add(new Task("Jeder ausser dir trinkt", TaskDifficult.MEDIUM, 1, 0, TaskTarget.ALL_BUT_SELF));
        getAllTasks().add(new Task("Nenne 10 Wörter die mit x enden in einer Minute", TaskDifficult.MEDIUM, 0, 3, TaskTarget.SELF));
        getAllTasks().add(new Task("Jeder mit einem A im Namen trinkt", TaskDifficult.EASY, 1, 0, TaskTarget.CHOOSE_ALL));
        getAllTasks().add(new Task("Tanze mit deinem linken Nachbarn", TaskDifficult.MEDIUM, 0, 2, TaskTarget.SELF));
        getAllTasks().add(new Task("Imitiere jemanden", TaskDifficult.EASY, 0, 1, TaskTarget.SELF));
        getAllTasks().add(new Task("Dein rechter Nachbar verpasst dir eine Ohrfeige", TaskDifficult.HARD, 0, 3, TaskTarget.SELF));
        getAllTasks().add(new Task("Alle trinken 1 mal", TaskDifficult.EASY, 1, 0, TaskTarget.ALL));
        getAllTasks().add(new Task("Alle trinken 2 mal", TaskDifficult.MEDIUM, 2, 0, TaskTarget.ALL));
        getAllTasks().add(new Task("Alle trinken 3 mal", TaskDifficult.HARD, 3, 0, TaskTarget.ALL));
        getAllTasks().add(new Task("Tausche den Platz mit deinem rechten Nachbarn", TaskDifficult.EASY, 0, 0, TaskTarget.SWITCH_PLACE_RIGHT));
        getAllTasks().add(new Task("Tausche den Platz mit deinem linken Nachbarn", TaskDifficult.EASY, 0, 0, TaskTarget.SWITCH_PLACE_LEFT));
        getAllTasks().add(new Task("Gib deinem rechten Nachbarn eine Massage", TaskDifficult.MEDIUM, 0, 2, TaskTarget.SELF));
        getAllTasks().add(new Task("Dein linker Nachbar liest deine letzten drei Nachrichten vor", TaskDifficult.HARD, 0, 3, TaskTarget.SELF));
        getAllTasks().add(new Task("Der Spieler, der am wenigsten getrunken hat, trinkt 2", TaskDifficult.MEDIUM, 2, 0, TaskTarget.CHOOSE_ONE));
        getAllTasks().add(new Task("Dein linker Nachbar trinkt 2", TaskDifficult.MEDIUM, 2, 0, TaskTarget.NEIGHBOUR_LEFT));
        getAllTasks().add(new Task("Stehe bis du wieder dran bist auf einem Bein", TaskDifficult.MEDIUM, 0, 2, TaskTarget.SELF));
        getAllTasks().add(new Task("Dein rechter Nachbar trinkt 2", TaskDifficult.MEDIUM, 2, 0, TaskTarget.NEIGHBOUR_RIGHT));
        getAllTasks().add(new Task("Der Spieler, der am meisten getrunken hat, trinkt 2", TaskDifficult.MEDIUM, 2, 0, TaskTarget.CHOOSE_ONE));
        getAllTasks().add(new Task("Trinke ein Glas Wasser", TaskDifficult.EASY, 0, 1, TaskTarget.SELF));
        getAllTasks().add(new Task("Ziehe ein Kleidungsstück aus", TaskDifficult.HARD, 0, 3, TaskTarget.SELF));
        getAllTasks().add(new Task("Dein linker Nachbar malt dir etwas auf deine Hand", TaskDifficult.MEDIUM, 0, 2, TaskTarget.SELF));
        getAllTasks().add(new Task("Dein linker Nachbar kneift dich", TaskDifficult.MEDIUM, 0, 2, TaskTarget.SELF));
        getAllTasks().add(new Task("Stelle ein Glas in die Mitte und fülle es mit 2cl eines beliebigen Getränks", TaskDifficult.EASY, 0, 0, TaskTarget.SELF));
        getAllTasks().add(new Task("Stelle ein Glas in die Mitte und fülle es mit 2cl eines beliebigen Getränks", TaskDifficult.EASY, 0, 0, TaskTarget.SELF));
        getAllTasks().add(new Task("Stelle ein Glas in die Mitte und fülle es mit 2cl eines beliebigen Getränks", TaskDifficult.EASY, 0, 0, TaskTarget.SELF));
        getAllTasks().add(new Task("Stelle ein Glas in die Mitte und fülle es mit 2cl eines beliebigen Getränks", TaskDifficult.EASY, 0, 0, TaskTarget.SELF));
        getAllTasks().add(new Task("Leere das Glas in der Mitte und fülle 2cl eines beliebigen Getränks ein", TaskDifficult.EASY, 1, 0, TaskTarget.SELF));
        getAllTasks().add(new Task("Alle Männer trinken", TaskDifficult.EASY, 1, 0, TaskTarget.MEN));
        getAllTasks().add(new Task("Arbeitslose trinken", TaskDifficult.EASY, 1, 0, TaskTarget.CHOOSE_ALL));
        getAllTasks().add(new Task("Alle Frauen trinken", TaskDifficult.EASY, 1, 0, TaskTarget.WOMEN));
        getAllTasks().add(new Task("Wenn du es schaffst dein Glas zu exen darfst du 5 verteilen", TaskDifficult.HARD, 5, 1, TaskTarget.CHOOSE_FIVE));
        getAllTasks().add(new Task("Alle Frauen ziehen ein Kleidungsstück aus oder trinken 3", TaskDifficult.HARD, 3, 0, TaskTarget.CHOOSE_ALL));
        getAllTasks().add(new Task("Alle Männer ziehen ein Kleidungsstück aus oder trinken 2", TaskDifficult.HARD, 2, 0, TaskTarget.CHOOSE_ALL));
        getAllTasks().add(new Task("Knutsche mit deinem rechten Nachbarn rum", TaskDifficult.HARD, 0, 3, TaskTarget.SELF));
        getAllTasks().add(new Task("Spiele mit dem Ohrläppchen von deinem linken Nachbarn", TaskDifficult.MEDIUM, 0, 2, TaskTarget.SELF));
        getAllTasks().add(new Task("Dein linker Nachbarn schreibt deinen Status bei Facebook", TaskDifficult.HARD, 0, 3, TaskTarget.SELF));
        getAllTasks().add(new Task("Macht ein Gruppenfoto, wer nicht mit macht trinkt 3", TaskDifficult.EASY, 3, 0, TaskTarget.CHOOSE_ALL));
        getAllTasks().add(new Task("Zeig deinen nackten Hintern", TaskDifficult.HARD, 0, 3, TaskTarget.SELF));
        getAllTasks().add(new Task("Zeig deine Brüste", TaskDifficult.HARD, 0, 3, TaskTarget.SELF));
        getAllTasks().add(new Task("Stell deinen Wecker auf 5 Uhr", TaskDifficult.HARD, 0, 5, TaskTarget.SELF));
        getAllTasks().add(new Task("Ziehe alle Jacken an", TaskDifficult.MEDIUM, 0, 2, TaskTarget.SELF));
        getAllTasks().add(new Task("Spiele Luftgittare", TaskDifficult.MEDIUM, 0, 2, TaskTarget.SELF));
        getAllTasks().add(new Task("Die anderen Spieler bestimmen einen Schnaps für dich", TaskDifficult.HARD, 1, 3, TaskTarget.SELF));
        getAllTasks().add(new Task("Tausche ein Kleidungsstück mit deinem linken Nachbarn", TaskDifficult.HARD, 0, 3, TaskTarget.SELF));
        getAllTasks().add(new Task("Halte einen Vortrag", TaskDifficult.MEDIUM, 0, 2, TaskTarget.SELF));
        getAllTasks().add(new Task("Mache 10 liegestützen", TaskDifficult.MEDIUM, 0, 2, TaskTarget.SELF));
        getAllTasks().add(new Task("Jeder, der das Spiel nicht installiert hat trinkt 2", TaskDifficult.MEDIUM, 2, 0, TaskTarget.CHOOSE_ALL));
        getAllTasks().add(new Task("Der letzte der Hans schreit trinkt 2", TaskDifficult.MEDIUM, 2, 0, TaskTarget.CHOOSE_ONE));
        getAllTasks().add(new Task("Ruf deine Eltern an", TaskDifficult.HARD, 0, 3, TaskTarget.SELF));

        getAllTasks().add(new Task("Deine Nachbarn trinken drei", TaskDifficult.EASY_WIN, 3, 0, TaskTarget.NEIGHBOUR));
        getAllTasks().add(new Task("Alle ausser dir trinken drei", TaskDifficult.MEDIUM_WIN, 3, 0 ,TaskTarget.ALL_BUT_SELF));
        getAllTasks().add(new Task("Suche drei Mitspieler aus, die einen Kurzen deiner Wahl trinken", TaskDifficult.HARD_WIN, 1, 0, TaskTarget.CHOOSE_THREE));
    }

    public Task getTask(TaskDifficult difficult){
        Random rnd = new Random();
        int index;
        switch (difficult){
            case EASY:
                index = rnd.nextInt(getEasyTasks().size());
                return getEasyTasks().get(index);
            case MEDIUM:
                index = rnd.nextInt(getMediumTasks().size());
                return getMediumTasks().get(index);
            case HARD:
                index = rnd.nextInt(getHardTasks().size());
                return getHardTasks().get(index);
            case EASY_WIN:
                index = rnd.nextInt(getEasyWinTasks().size());
                return getEasyWinTasks().get(index);
            case MEDIUM_WIN:
                index = rnd.nextInt(getMediumWinTasks().size());
                return getMediumWinTasks().get(index);
            case HARD_WIN:
                index = rnd.nextInt(getHardWinTasks().size());
                return getHardWinTasks().get(index);
        }
        return null;
    }

    private void removeTask(Task t){
        switch (t.getDifficult()){
            case EASY:
                getEasyTasks().remove(t);
                if(getEasyTasks().size() <= 1){
                    refreshList(TaskDifficult.EASY);
                }
                break;
            case MEDIUM:
                getMediumTasks().remove(t);
                if(getMediumTasks().size() <= 1){
                    refreshList(TaskDifficult.MEDIUM);
                }
                break;
            case HARD:
                getHardTasks().remove(t);
                if(getHardTasks().size() <= 1){
                    refreshList(TaskDifficult.HARD);
                }
                break;
        }
    }

    public List<Task> getEasyTasks() {
        return easyTasks;
    }

    public void setEasyTasks(List<Task> easyTasks) {
        this.easyTasks = easyTasks;
    }

    public List<Task> getMediumTasks() {
        return mediumTasks;
    }

    public void setMediumTasks(List<Task> mediumTasks) {
        this.mediumTasks = mediumTasks;
    }

    public List<Task> getHardTasks() {
        return hardTasks;
    }

    public void setHardTasks(List<Task> hardTasks) {
        this.hardTasks = hardTasks;
    }

    public List<Task> getEasyWinTasks() {
        return easyWinTasks;
    }

    public void setEasyWinTasks(List<Task> easyWinTasks) {
        this.easyWinTasks = easyWinTasks;
    }

    public List<Task> getMediumWinTasks() {
        return mediumWinTasks;
    }

    public void setMediumWinTasks(List<Task> mediumWinTasks) {
        this.mediumWinTasks = mediumWinTasks;
    }

    public List<Task> getHardWinTasks() {
        return hardWinTasks;
    }

    public void setHardWinTasks(List<Task> hardWinTasks) {
        this.hardWinTasks = hardWinTasks;
    }

    public List<Task> getAllTasks() {
        return allTasks;
    }

    public void setAllTasks(List<Task> allTasks) {
        this.allTasks = allTasks;
    }
}
