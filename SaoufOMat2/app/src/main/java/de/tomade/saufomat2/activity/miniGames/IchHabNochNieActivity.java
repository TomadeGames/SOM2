package de.tomade.saufomat2.activity.miniGames;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import de.tomade.saufomat2.R;
import de.tomade.saufomat2.activity.ChooseMiniGameActivity;
import de.tomade.saufomat2.model.MiniGame;

public class IchHabNochNieActivity extends Activity implements View.OnClickListener {
    private static final String TAG = IchHabNochNieActivity.class.getSimpleName();
    private static final String HAB_NIE = "Ich hab noch nie\n";
    private static Random random;

    private List<String> currentQuestions;
    private List<String> allQuestions;

    private TextView taskView;

    private boolean tutorialShown = false;
    private String currentTask;

    private boolean fromMenue = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ich_hab_noch_nie);

        Bundle extras = this.getIntent().getExtras();
        if (extras != null) {
            fromMenue = extras.getBoolean("fromMenue");
        }

        random = new Random();

        this.currentQuestions = new ArrayList<>();
        this.allQuestions = new ArrayList<>();

        initLists();

        this.taskView = (TextView) this.findViewById(R.id.taskText);
        this.currentTask = getQuestion();
        this.taskView.setText(HAB_NIE + this.currentTask);

        ImageButton popup = (ImageButton) this.findViewById(R.id.popupButton);
        ImageButton tutorial = (ImageButton) this.findViewById(R.id.tutorialButton);
        ImageButton back = (ImageButton) this.findViewById(R.id.backButton);

        if (!this.fromMenue) {
            back.setVisibility(View.INVISIBLE);
            TextView backText = (TextView) this.findViewById(R.id.backText);
            backText.setVisibility(View.INVISIBLE);
        }

        popup.setOnClickListener(this);
        tutorial.setOnClickListener(this);
        back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.backButton) {
            Intent intent = new Intent(this.getApplicationContext(), ChooseMiniGameActivity.class);
            intent.putExtra("lastGame", MiniGame.ICH_HAB_NOCH_NIE);
            this.startActivity(intent);
        } else {
            if (this.tutorialShown) {
                this.tutorialShown = false;
                this.taskView.setText(HAB_NIE + this.currentTask);
            } else {
                switch (v.getId()) {
                    case R.id.popupButton:
                        nextQuestion();
                        break;
                    case R.id.tutorialButton:
                        showTutorial();
                        break;
                }
            }
        }
    }

    private void nextQuestion() {
        this.currentTask = getQuestion();
        this.taskView.setText(HAB_NIE + this.currentTask);
    }

    private void showTutorial() {
        this.taskView.setText("Anleitung:\nEs werden Sätze generiert, die mit \"Ich hab noch nie\" beginnen. Jeder, der die verneinte Handlung doch schon mal gemacht hat, muss trinken. Danach wird das Handy weitergegeben.");
        tutorialShown = true;
    }

    private void initLists() {
        this.allQuestions.add("ein Pferd geritten");
        this.allQuestions.add("ein meine Eltern nackt gesehen");
        String[] strings = {
                "so getan als würde ich vor einem Millionenpublikum stehen und singe",
                "ein Telefonat von mehr als 5 Stunden geführt",
                "das Handy meines Partner durchstöbert",
                "eine erotische Massage gegeben oder erhalten",
                "einen ganzen Tag ohne Kleidung verbracht",
                "so viel getrunken dass ich beim Sex eingeschlafen bin",
                "mit jemandem geschlafen den ich eine Stunde zuvor noch nicht kannte",
                "Fantasien gehabt in denen ich gefoltert oder gefesselt wurde",
                "einen Brief eingeworfen und versucht ihn wieder zu bekommen",
                "gewünscht die Eltern eines Freundes wären meine",
                "so viel Angst gehabt dass ich am ganzen Körper gezittert habe",
                "eine Party besucht mit der Absicht heute bloß nicht alleine nach Hause zu gehen",
                "während des Sex gähnen müssen",
                "meinen eigenen Intimbereich fotografiert",
                "etwas aus einem Hotelzimmer mitgehen lassen",
                "Rosen geschenkt bekommen",
                "etwas im Internet bestellt weil ich mich nicht getraut habe es im Laden zu kaufen",
                "ein sehr peinliches Rollenspiel mitgemacht",
                "einen ganzen Tag lang geweint",
                "von einem Partner ein erotisches Geschenk erhalten",
                "überlegt in ein SM- / Bondage Studio zu gehen",
                "jemanden für mich strippen lassen",
                "einen Dildo in meinen Händen gehalten",
                "einen Dildo auf meiner Stirn gehalten",
                "mehr als eine halbe Stunde im Internet nach einer Person recherchiert",
                "über einen Freund gedacht dass seine Eltern bei der Erziehung versagt haben",
                "'Ich liebe dich' gesagt obwohl ich es nicht so meinte",
                "Sex im Auto gehabt",
                "Sex an einem sehr ungewöhnlichen Ort gehabt",
                "einen Freund/eine Freundin meines Partners abgrundtief gehasst.",
                "jemanden nach seiner Telefonnummer gefragt und danach nie angerufen",
                "erotische Unterwäsche getragen",
                "unter einer falschen Identität versucht einen Bekannten/Freund über das Internet zu kontaktieren",
                "ein Auge auf die Eltern eines Freundes geworfen.",
                "mit einem fremden Auto einen Unfall gebaut",
                "jemanden gedated, der eigenartig war",
                "mit jemandem geschlafen um mich an jemand anderem zu rächen",
                "meinen eigenen Namen gegoogelt",
                "eine Fotokopie von meinem Hintern gemacht",
                "hunderte Kilometer zurück gelegt nur um einen bestimmten Menschen zu sehen",
                "für erotische Dienstleistungen bezahlt",
                "ein Fahrrad gestohlen",
                "das Haus verlassen ohne Unterwäsche drunter zu ziehen",
                "unter der Dusche gesungen",
                " jemanden gleichzeitig geliebt und gehasst",
                "Verhütungsmittel mitgeführt 'nur für den Fall'",
                "an den Ohrläppchen meines Partners geleckt",
                "jemandem die Füße geküsst",
                "gesagt bekommen ich sei gut im Bett",
                "auf einer Party mit jemandem rumgemacht",
                "etwas im Wert von über 500€ kaputt gemacht oder verlegt",
                "einen ganzen Tag Computer gespielt",
                "meinen bzw. den Penis meines Partner mit dem Lineal vermessen",
                "jemandem wildfremden auf der Straße geholfen",
                "während eines Dates eine Ausrede benutzt, um das Treffen frühzeitig zu beenden",
                "für erotisches Material bezahlt (Live-Cams, Pornohefte, Videos, etc)",
                "jemanden heimlich beobachtet",
                "wo anders als zuhause masturbiert",
                "im Vollrausch vergessen, wo ich wohne",
                "so getan als wäre ich betrunken",
                "einem Partner versaute Kosenamen gegeben",
                "in einem Kinofilm geschlafen",
                "jemanden geküsst, ohne seinen Namen zu wissen",
                "in die Dusche gepinkelt",
                "gleichgeschlechtliche Gefühle gehabt",
                "ein Wort gegoogelt, weil ich nicht wusste, wie man es schreibt",
                "Sex in einer Umkleidekabine gehabt",
                "nach dem Aufwachen gedacht: Was ist gestern Abend eigentlich noch passiert?",
                "eine Frage bei einem Trinkspiel falsch beantwortet",
                "ein Angebot für einen Dreier bekommen",
                "einen Sex-Ratgeber gelesen",
                "mit jemandem geschlafen den ich verleugnen würde",
                "erotische Fotos von mir schießen lassen",
                "per SMS mit jemandem Schluss gemacht",
                "im stehen gepinkelt",
                "einen Partner gehabt dessen Eltern ich nicht leiden konnte",
                "einen Liebesbrief erhalten",
                "den Geburtstag eines Familienangehörigen vergessen",
                "mit meinem Partner zusammen erotische Filme oder Bilder angeschaut",
                "etwas das mit Sex zu tun hat im Internet gesucht",
                "einen Partner gehabt der deutlich mehr Sex wollte als ich",
                "so getan als wäre ich krank nur um nicht mit jemandem schlafen zu müssen",
                "noch beim ersten Date mit jemandem geschlafen",
                "an einem FKK Strand gebadet",
                "wegen eines Katers die Schule / den Job geschwänzt",
                "einen Haarschnitt gehabt für den ich mich heute noch schäme",
                "als Notfall in einem Krankenwagen gelegen",
                "ein Kondom beim Überstreifen kaputt gemacht",
                "den Wunsch gehabt, in eine andere Stadt zu ziehen",
                "während dem Sex mit Haushaltsgegenständen experimentiert",
                "Lust gehabt, mit einem der anwesenden Spielern zu schlafen",
                "so viel getrunken dass ich nur mit fremder Hilfe nach Hause gekommen bin",
                "einen Lehrer/Vorgesetzten nackt vorgestellt",
                "den Sex'ausfallen' lassen weil keine Verhütungsmittel zur Hand waren",
                "meinen Eltern erzählen müssen ob ich noch Jungfrau bin",
                "eine Zahnspange getragen",
                "gesagt ich sei Single obwohl ich in einer Beziehung war",
                "ein Verkehrsschild überfahren",
                "eine Runde Strippoker gespielt",
                "Sex in einem Fahrstuhl",
                "einen Film mehr als 15 mal gesehen",
                "eine Spam-Nachricht angeklickt weil ich dachte dass sie echt ist",
                "mehr als 42 Stunden ohne Schlaf verbracht",
                "meine Unterwäsche irgendwo verloren oder vergessen",
                "einen Swingerclub von innen gesehen",
                "davon geträumt beherrscht oder dominiert zu werden",
                "Sexspielzeug von anderen Leuten gefunden",
                "große Schadenfreude verspürt als ich hörte dass einem anderen etwas sehr schlimmes zugestoßen ist",
                "in einem Wutanfall etwas zerstört",
                "mit meinen Eltern über Verhütung gesprochen",
                "einen Prominenten persönlich getroffen",
                "mit jemandem geschlafen ohne zu verhüten",
                "einen Knutschfleck gehabt",
                "einen Neujahrsvorsatz tatsächlich eingehalten",
                "ein Geschenk vergessen",
                "bei einem Date bezüglich meiner Alters oder meiner Herkunft gelogen",
                "Oralsex während dem Autofahren",
                "eine Mutprobe mitgemacht",
                "bei Bekannten in den Badezimmerschrank geschaut",
                "einen Partner gehabt bei dem ich Angst hatte ihn meinem Eltern vorzustellen",
                "mich selbst gestreichelt und mir vorgestellt es sei jemand anderes",
                "gesagt die Verbindung sei schlecht nur um ein Telefonat zu beendet",
                "jemanden vom gleichen Geschlecht attraktiv gefunden",
                "Sex mit einer Person gehabt mit der ich nicht zusammen war",
                "vor 13 Uhr so viel getrunken dass ich sturz besoffen war",
                "mein erstes Date im Kino verbracht",
                "Sex in einer Badewanne gehabt",
                "jemandem einfach so auf den Hintern gehauen (nicht mein Freund/meine Freundin)",
                "besoffen ein Geheimnis ausgeplaudert",
                "mehr als 2 Wochen am Stück keinen Alkohol angerührt (letzten 2 Jahre)",
                "versucht anstößige Geheimnisse über einen Freund herauszubekommen",
                "versucht, das Passwort eines Anderen zu erraten",
                "einen Strafzettel bekommen",
                "etwas geklaut",
                "auf der Arbeit rumgemacht",
                "jemandem das Leben gerettet",
                "gekifft",
                "härtere Drogen konsumiert",
                "mein Flugzeug verpasst",
                "jemanden entjungfert",
                "Fingernägel gekaut",
                "in der Öffentlichkeit in die Hose gepinkelt",
                "meine Eltern beim Sex erwischt",
                "eine Affäre mit einer verheirateten Person gehabt",
                "mit insgesamt mehr als 15 Leuten in meinem Leben geschlafen",
                "Geld gefunden und es einfach behalten",
                "eine Schwarzfahrt in Kauf genommen, statt mir ein Ticket zu kaufen",
                "eine Zigarre geraucht",
                "schlafgewandelt",
                "einen Fallschirmsprung gemacht",
                "Sex im Bett meiner Eltern gehabt",
                "einen Partner betrogen (auch Küssen)",
                "den Notruf gewählt",
                "etwas anderes als Klopapier benutzt",
                "vor der Polizei fliehen müssen",
                "einen Wettbewerb gewonnen",
                "mehr als eine Woche im Krankenhaus verbracht",
                "die Schule geschwänzt",
                "einen Autounfall mit mehr als 200€ Schaden gehabt",
                "drei Abende hintereinander einen Vollrausch",
                "mein Alter verleugnet nur um jemanden näher kennen zu lernen",
                "im Beisein meines Partners geweint",
                "Sex gehabt während andere Personen im Raum waren",
                "draußen im Gras geschlafen, weil ich so betrunken war",
                "auf eine andere Person gekotzt",
                "das Geheimnis eines anderen ausgeplaudert (versehentlich oder absichtlich)",
                "einen Korb erhalten wenn ich mit jemandem ausgehen wollte",
                "einen Rausschmiss in einem Club bekommen",
                "mich selbst in den Schlaf geweint",
                "eine Waffe abgefeuert",
                "einen Abend ohne Freunde oder Bekannte in einem Club verbracht",
                "eine Schlange in meinen Händen gehalten",
                "für jemanden gestripped",
                "falsche Angaben gemacht bei der Frage mit wie vielen Leuten ich geschlafen habe",
                "bei einem Dreier mitgemacht",
                "während dem Sex anfangen müssen zu lachen",
                "ein Möbelstück beim Sex kaputt gemacht",
                "hinter dem Kühlschrank sauber gemacht",
                "ein Date mit jemandem gehabt den ich über das Internet kennen gelernt habe",
                "eine Kneipenschlägerei gehabt",
                "mit jemandem aus einem anderen Land geschlafen",
                "einen Auftritt im Fernsehen gehabt",
                "eine Straftat begangen",
                "während dem Sex mit Lebensmitteln experimentiert",
                "jemanden kennen gelernt der schonmal im Gefängnis saß",
                "meinen Partner mit dem Namen meiner/meines Ex angesproche",
                "mehr als 200€ an einem Abend ausgegeben",
                "in der Öffentlichkeit gekotzt",
                "während des Sex gefurzt",
                "ein Angebot für Sex abgelehnt",
                "ein Tier auf den Mund geküsst"
        };
        for (String s : strings) {
            this.allQuestions.add(s);
        }

        refreshList();
    }

    private String getQuestion() {
        int index = random.nextInt(currentQuestions.size());
        String erg = currentQuestions.get(index);
        currentQuestions.remove(index);
        if (currentQuestions.size() <= 0) {
            refreshList();
        }
        return erg;
    }

    private void refreshList() {
        for (String s : this.allQuestions) {
            this.currentQuestions.add(s);
        }
    }
}
