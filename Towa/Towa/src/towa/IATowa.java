package towa;

import java.io.IOException;
import static java.lang.Integer.parseInt;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import static towa.PionsAdverses.estDansPlateau;
import static towa.PionsAdverses.positionSuivante;
import static towa.PionsAdverses.suivante;

/**
 * Votre IA pour le jeu Towa.
 */
public class IATowa {

    /**
     * Hôte du grand ordonnateur.
     */
    String hote = null;

    /**
     * Port du grand ordonnateur.
     */
    int port = -1;

    /**
     * Couleur de votre joueur (IA) : 'N'oir ou 'B'lanc.
     */
    final char couleur;

    /**
     * Le séparateur de chaque actions
     */
    final static String SEPARATEUR = ",";

    final static int HAUTEUR_MAX = 4;

    /**
     * Interface pour le protocole du grand ordonnateur.
     */
    TcpGrandOrdonnateur grandOrdo = null;

    /**
     * Nombre maximal de tours de jeu.
     */
    static final int NB_TOURS_JEU_MAX = 40;

    /**
     * Constructeur.
     *
     * @param hote Hôte.
     * @param port Port.
     * @param uneCouleur couleur du joueur
     */
    public IATowa(String hote, int port, char uneCouleur) {
        this.hote = hote;
        this.port = port;
        this.grandOrdo = new TcpGrandOrdonnateur();
        this.couleur = uneCouleur;
    }

    /**
     * Connexion au Grand Ordonnateur.
     *
     * @throws IOException exception sur les entrées/sorties
     */
    void connexion() throws IOException {
        System.out.print(
                "Connexion au Grand Ordonnateur : " + hote + " " + port + "...");
        System.out.flush();
        grandOrdo.connexion(hote, port);
        System.out.println(" ok.");
        System.out.flush();
    }

    /**
     * Boucle de jeu : envoi des actions que vous souhaitez jouer, et réception
     * des actions de l'adversaire.
     *
     * @throws IOException exception sur les entrées/sorties
     */
    void toursDeJeu() throws IOException {
        // paramètres
        System.out.println("Je suis le joueur " + couleur + ".");
        // le plateau initial
        System.out.println("Réception du plateau initial...");
        Case[][] plateau = grandOrdo.recevoirPlateauInitial();
        System.out.println("Plateau reçu.");
        // compteur de tours de jeu (entre 1 et 40)
        int nbToursJeu = 1;
        // la couleur du joueur courant (change à chaque tour de jeu)
        char couleurTourDeJeu = Case.CAR_NOIR;
        // booléen pour détecter la fin du jeu
        boolean fin = false;
        while (!fin) {
            boolean disqualification = false;

            if (couleurTourDeJeu == couleur) {
                // à nous de jouer !
                jouer(plateau, nbToursJeu);
            } else {
                // à l'adversaire de jouer
                disqualification = adversaireJoue(plateau, couleurTourDeJeu);
            }
            if (nbToursJeu == NB_TOURS_JEU_MAX || disqualification) {
                // fini
                fin = true;
            } else {
                // au suivant
                nbToursJeu++;
                couleurTourDeJeu = suivant(couleurTourDeJeu);
            }
        }
    }

    /**
     * Fonction exécutée lorsque c'est à notre tour de jouer. Cette fonction
     * envoie donc l'action choisie au serveur.
     *
     * @param plateau le plateau de jeu
     * @param nbToursJeu numéro du tour de jeu
     * @throws IOException exception sur les entrées / sorties
     */
    void jouer(Case[][] plateau, int nbToursJeu) throws IOException {
        String actionJouee = actionChoisie(plateau, nbToursJeu);
        if (actionJouee != null) {
            // jouer l'action
            System.out.println("On joue : " + actionJouee);
            grandOrdo.envoyerAction(actionJouee);
            mettreAJour(plateau, actionJouee, couleur);
        } else {
            // Problème : le serveur vous demande une action alors que vous n'en
            // trouvez plus...
            System.out.println("Aucun action trouvée : abandon...");
            grandOrdo.envoyerAction("ABANDON");
        }
    }

    /**
     * L'action choisie par notre IA.
     *
     * @param plateau le plateau de jeu
     * @param nbToursJeu numéro du tour de jeu
     * @return l'action choisie sous forme de chaîne
     */
    public String actionChoisie(Case[][] plateau, int nbToursJeu) {
        // on instancie votre implémentation
        JoueurTowa joueurTowa = new JoueurTowa();
        // choisir aléatoirement une action possible
        String[] actionsPossibles = ActionsPossibles.nettoyerTableau(
                joueurTowa.actionsPossibles(plateau, couleur, 8));
        String actionJouee = null;
        if (actionsPossibles.length > 0) {
            actionJouee = ActionsPossibles.enleverVitalites(this.meilleurAction(actionsPossibles, plateau));
        }

        return actionJouee;
    }

    String meilleurAction(String[] actionPossible, Case[][] plateau) {
        int max = evaluation(actionPossible[0], plateau);
        String meillAction = "";
        int indiceM=0;
        for (int i = 0; i < actionPossible.length; i++) {
            String action = actionPossible[i];
            if (max <= evaluation(action, plateau)) {
                max = evaluation(action, plateau);
                meillAction = action;
                indiceM=i;
            }
        }

        char actionType = meillAction.charAt(0);
        Random r = new Random();
        if (actionType == 'A') {
            Coordonnees coord = Coordonnees.depuisCars(meillAction.charAt(1),meillAction.charAt(2) );
            if (!checkCasesAdjacentesEnemi(coord, couleur, plateau)) {
                meillAction="P"+meillAction.substring(1);
                if (!estDansActions(actionPossible,meillAction)) {
                    actionPossible[indiceM] =null;
                    String[] actions=ActionsPossibles.nettoyerTableau(actionPossible);
                    meillAction=meilleurAction(actions, plateau);
                }
            }
        }

//        System.out.println(plateauDepuisTexte(plateau));
        return meillAction;
    }

    int evaluation(String action, Case[][] plateau) {
        return pionsGagnee(action) + pionsTuable(action, plateau);
    }

    int pionsGagnee(String action) {
        int idSeparateur = action.indexOf(",");
        int idDeuxiemeSep = action.indexOf(",", idSeparateur + 1);

        int nbJoueurN = parseInt(action.substring(idSeparateur + 1, idDeuxiemeSep));
        int nbJoueurB = parseInt(action.substring(idDeuxiemeSep + 1));

        int nbPions = 0;
        if (this.couleur == Case.CAR_NOIR) {
            nbPions = nbJoueurN - nbJoueurB;
        }
        if (this.couleur == Case.CAR_BLANC) {
            nbPions = nbJoueurB - nbJoueurN;
        }
        return nbPions;
    }

    int pionsTuable(String action, Case[][] plateau) {
        int aTuer = 0;
        Coordonnees coord = Coordonnees.depuisCars(action.charAt(1), action.charAt(2));
        aTuer += casesAdjacentesEnemi(coord, couleur, plateau);
        aTuer += PionsAdverses.estDansLigneEtColonne(coord, couleur, plateau, 8);
        return aTuer;
    }

    static int casesAdjacentesEnemi(Coordonnees coord, char couleur, Case[][] plateau) {
        int nbAdversairesAdjacents = 0;
        for (Direction d : Direction.cardinales2()) {
            Coordonnees pionSuivant = suivante(coord, d);
            // Si il est dans le plateau
            if (estDansPlateau(pionSuivant, Coordonnees.NB_LIGNES)) {
                // Si c'est un adversaire
                if (plateau[pionSuivant.ligne][pionSuivant.colonne].couleur != couleur && plateau[pionSuivant.ligne][pionSuivant.colonne].couleur != Case.CAR_VIDE) {
                    nbAdversairesAdjacents += plateau[pionSuivant.ligne][pionSuivant.colonne].hauteur;
                }
            }
        }
        return nbAdversairesAdjacents;
    }

    static boolean checkCasesAdjacentesEnemi(Coordonnees coord, char couleur, Case[][] plateau) {
        boolean estAssesGrand = false;
        int hauteurTour = plateau[coord.ligne][coord.colonne].hauteur;
        for (Direction d : Direction.cardinales2()) {
            Coordonnees pionSuivant = suivante(coord, d);
            // Si il est dans le plateau
            if (estDansPlateau(pionSuivant, Coordonnees.NB_LIGNES)) {
                // Si c'est un adversaire
                if (plateau[pionSuivant.ligne][pionSuivant.colonne].couleur != couleur && plateau[pionSuivant.ligne][pionSuivant.colonne].couleur != Case.CAR_VIDE) {
                    if (hauteurTour > plateau[pionSuivant.ligne][pionSuivant.colonne].hauteur) {
                        estAssesGrand = true;
                    }
                }
            }
        }
        return estAssesGrand;
    }
    
    static boolean estDansActions(String[] actions,String laAction){
        boolean estDansAction=false;
        for (String action : actions) {
            if (action.equals(laAction)) {
                estDansAction=true;
            }
        }
        return estDansAction;
    }

    public static String plateauDepuisTexte(Case[][] plateau) {
        String plateauStr = "";
        String[] alpha = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p"};
        for (int i = 0; i < alpha.length; i++) {
            plateauStr += "    " + alpha[i] + "";
        }
        plateauStr += "\n";
        for (int lig = 0; lig < Coordonnees.NB_LIGNES; lig++) {
            plateauStr += alpha[lig] + " |";
            for (int col = 0; col < Coordonnees.NB_COLONNES; col++) {
                plateauStr += " " + plateau[lig][col].couleur + plateau[lig][col].hauteur + " |";
            }
            plateauStr += "\n";
        }
        return plateauStr;
    }

    /**
     * L'adversaire joue : on récupère son action, met à jour le plateau, et
     * signale toute disqualification.
     *
     * @param plateau le plateau de jeu
     * @param couleurAdversaire couleur de l'adversaire
     * @return l'action choisie sous forme de chaîne
     */
    boolean adversaireJoue(Case[][] plateau, char couleurAdversaire) {
        boolean disqualification = false;
        System.out.println("Attente de réception action adversaire...");
        String actionAdversaire = grandOrdo.recevoirAction();
        System.out.println("Action adversaire reçue : " + actionAdversaire);
        if ("Z".equals(actionAdversaire)) {
            System.out.println("L'adversaire est disqualifié.");
            disqualification = true;
        } else {
            System.out.println("L'adversaire joue : "
                    + actionAdversaire + ".");
            mettreAJour(plateau, actionAdversaire, couleurAdversaire);
        }
        return disqualification;
    }

    /**
     * Calcule la couleur du prochain joueur.
     *
     * @param couleurCourante la couleur du joueur courant
     * @return la couleur du prochain joueur
     */
    static char suivant(char couleurCourante) {
        return couleurCourante == Case.CAR_NOIR
                ? Case.CAR_BLANC : Case.CAR_NOIR;
    }

    /**
     * Mettre à jour le plateau suite à une action, supposée valide.
     *
     * @param plateau le plateau
     * @param action l'action à appliquer
     * @param couleurCourante couleur du joueur courant
     */
    static void mettreAJour(Case[][] plateau, String action,
            char couleurCourante) {
        // vérification des arguments
        if (plateau == null || action == null || action.length() != 3) {
            return;
        }
        Coordonnees coord = Coordonnees.depuisCars(action.charAt(1), action.charAt(2));
        switch (action.charAt(0)) {
            case 'P':
                poser(coord, plateau, couleurCourante);
                break;
            case 'A':
                activer(coord, plateau, couleurCourante);
                break;
            case 'F':
                fusionner(coord, plateau, couleurCourante);
            default:
                System.out.println("Type d'action incorrect : " + action.charAt(0));
        }
    }

    /**
     * Poser un pion sur une case donnée (vide ou pas).
     *
     * @param coord coordonnées de la case
     * @param plateau le plateau de jeu
     * @param couleurCourante couleur du joueur courant
     */
    static void poser(Coordonnees coord, Case[][] plateau, char couleurCourante) {
        Case laCase = plateau[coord.ligne][coord.colonne];
        if (laCase.tourPresente()) {
            laCase.hauteur++;
        } else {
            laCase.couleur = couleurCourante;
            if (ennemiVoisine(coord, plateau, couleurCourante)) {
                laCase.hauteur = 2;
            } else {
                laCase.hauteur = 1;
            }
        }
    }

    /**
     * Activer une tour.
     *
     * @param coord coordonnées de la case où se situe la tour à activer
     * @param plateau le plateau de jeu
     * @param couleurCourante couleur du joueur courant
     */
    static void activer(Coordonnees coord, Case[][] plateau, char couleurCourante) {
        Case[] enemieLignesColonnes = enemiDansLigneEtColonne(coord, couleurCourante, plateau);
        for (Case tourADetruire : enemieLignesColonnes) {
            detruireTour(tourADetruire);
        }
        Case[] enemiAdjacents = casesAdjacentesActivation(coord, couleurCourante, plateau, 8);
        for (Case tourADetruire : enemiAdjacents) {
            detruireTour(tourADetruire);
        }
//        final int hauteurTourJoueur = plateau[coord.ligne][coord.colonne].hauteur;
//        List<Case> aDetruire
//                = porteeActivation(coord)
//                        .map(aPortee -> plateau[aPortee.ligne][aPortee.colonne])
//                        .filter(c -> c.tourPresente()) // une tour
//                        .filter(c -> c.couleur != couleurCourante) // ennemie
//                        .filter(c -> c.hauteur < hauteurTourJoueur) // plus basse
//                        .collect(Collectors.toList());
//        for (Case tourADetruire : aDetruire) {
//            detruireTour(tourADetruire);
//        }
    }

    /**
     * Fusionner une tour avec toutes ses voisines.
     *
     * @param coord coordonnées de la case où se situe la tour à fusionner
     * @param plateau le plateau de jeu
     * @param couleurCourante couleur du joueur courant
     */
    static void fusionner(Coordonnees coord, Case[][] plateau, char couleurCourante) {
        Case laCase = plateau[coord.ligne][coord.colonne];
        Case[] amisLignesColonnes = amisDansLigneEtColonne(coord, couleurCourante, plateau, 8);
        int pionsRecuperes = 0;
        for (Case tourADetruire : amisLignesColonnes) {
            pionsRecuperes += tourADetruire.hauteur;
            detruireTour(tourADetruire);
        }
        Case[] amisAdjacents = casesAdjacentesFusion(coord, couleurCourante, plateau, 8);
        for (Case tourADetruire : amisAdjacents) {
            pionsRecuperes += tourADetruire.hauteur;
            detruireTour(tourADetruire);
        }
        int nouvelleHauteur = laCase.hauteur + pionsRecuperes;
        if (nouvelleHauteur > HAUTEUR_MAX) {
            nouvelleHauteur = HAUTEUR_MAX;
        }
        laCase.hauteur = nouvelleHauteur;
    }

    /**
     * Détruire une tour.
     *
     * @param laCase la case dont on doit détruire la tour
     */
    static void detruireTour(Case laCase) {
        laCase.hauteur = 0;
        laCase.couleur = Case.CAR_VIDE;
    }

    /**
     * Indique si une case possède une case voisine avec une tour ennemie.
     *
     * @param coord la case dont on souhaite analyser les voisines
     * @param plateau le plateau courant
     * @param couleurCourante couleur du joueur courant
     * @return vrai ssi la case possède une voisine avec une tour ennemie
     */
    static boolean ennemiVoisine(Coordonnees coord, Case[][] plateau, char couleurCourante) {
        return voisines(coord)
                .map(v -> plateau[v.ligne][v.colonne])
                .filter(c -> c.tourPresente())
                .anyMatch(c -> c.couleur != couleurCourante);
    }

    /**
     * Les coordonnées des cases voisines dans le plateau.
     *
     * @param coord les coordonnées de la case d'origine
     * @return les coordonnées des cases voisines
     */
    static Stream<Coordonnees> voisines(final Coordonnees coord) {
        return IntStream.rangeClosed(-1, 1).boxed()
                .flatMap(l -> IntStream.rangeClosed(-1, 1)
                .filter(c -> !(l == 0 && c == 0))
                .mapToObj(c -> new Coordonnees(coord.ligne + l, coord.colonne + c)))
                .filter(v -> 0 <= v.ligne && v.ligne < Coordonnees.NB_LIGNES)
                .filter(v -> 0 <= v.colonne && v.colonne < Coordonnees.NB_COLONNES);
    }

    /**
     * Coordonnées des cases à portée d'activation.
     *
     * @param coord les coordonnées de la case activée
     * @return les coordonnées des cases à portée d'activation
     */
    static Stream<Coordonnees> porteeActivation(final Coordonnees coord) {
        return Stream.concat(voisines(coord),
                Stream.concat(memeLigne(coord), memeColonne(coord)));
    }

    /**
     * Cette fonction permet de déterminer combien de pions adverses sont sur la
     * même ligne et la même colonne.
     *
     * @param coord coordonnées de la case où se trouve la tour à vérifier.
     * @param couleur la couleur de la tour à vérifier (le joueur actif).
     * @param plateau le plateau de jeu
     * @param niveau le niveau du jeu.
     * @return le nombre d'adversaires présents sur la même ligne et sur la même
     * colonne que le joueur actif.
     */
    static Case[] amisDansLigneEtColonne(Coordonnees coord, char couleur, Case[][] plateau, int niveau) {
        Case[] amisDansLigneColonne = new Case[40];
        int nbAmis = 0;
        Coordonnees coordS = new Coordonnees(coord.ligne, coord.colonne);
        boolean caseVide;
        for (Direction d : Direction.cardinales1()) {
            coordS.ligne = positionSuivante(coord, d).ligne;
            coordS.colonne = positionSuivante(coord, d).colonne;
            caseVide = true;
            // On cherche le premier pion dans la direction d.
            while (estDansPlateau(coordS, Coordonnees.NB_LIGNES) && caseVide) {
                // Si on a trouvé un pion
                if (plateau[coordS.ligne][coordS.colonne].couleur != Case.CAR_VIDE) {
                    caseVide = false;
                    // Si la case est remplie par un pion ami
                    if (plateau[coordS.ligne][coordS.colonne].couleur == couleur) {
                        amisDansLigneColonne[nbAmis] = plateau[coordS.ligne][coordS.colonne];
                        nbAmis++;
                    }
                }
                coordS.ligne = positionSuivante(coordS, d).ligne;
                coordS.colonne = positionSuivante(coordS, d).colonne;
            }
        }
        Case[] tabFinal = new Case[nbAmis];
        for (int i = 0; i < nbAmis; i++) {
            tabFinal[i] = amisDansLigneColonne[i];
        }
        return tabFinal;
    }

    /**
     * Retourne le nombre d'amis adjacents (en comptant les pions qui forment
     * des tours.
     *
     * @param coord coordonnées de la case considérée
     * @param couleur la couleur du joueur actif
     * @param plateau le plateau de jeu
     * @param niveau le niveau du jeu
     * @return le nombre de pions amis adjacents.
     */
    static Case[] casesAdjacentesFusion(Coordonnees coord, char couleur, Case[][] plateau, int niveau) {
        Case[] amisAdjacents = new Case[8];
        int nbAmis = 0;
        for (Direction d : Direction.cardinales2()) {
            Coordonnees pionSuivant = suivante(coord, d);
            // Si il est dans le plateau
            if (estDansPlateau(pionSuivant, Coordonnees.NB_LIGNES)) {
                // Si c'est un pion ami
                if (plateau[pionSuivant.ligne][pionSuivant.colonne].couleur == couleur) {
                    amisAdjacents[nbAmis] = plateau[pionSuivant.ligne][pionSuivant.colonne];
                    nbAmis++;
                }
            }
        }
        Case[] tabFinal = new Case[nbAmis];
        for (int i = 0; i < nbAmis; i++) {
            tabFinal[i] = amisAdjacents[i];
        }
        return tabFinal;
    }

    /**
     * Retourne le nombre d'amis adjacents (en comptant les pions qui forment
     * des tours.
     *
     * @param coord coordonnées de la case considérée
     * @param couleur la couleur du joueur actif
     * @param plateau le plateau de jeu
     * @param niveau le niveau du jeu
     * @return le nombre de pions amis adjacents.
     */
    static Case[] casesAdjacentesActivation(Coordonnees coord, char couleur, Case[][] plateau, int niveau) {
        Case[] enemiAdjacents = new Case[8];
        int hauteurTour = plateau[coord.ligne][coord.colonne].hauteur;
        int nbAmis = 0;
        for (Direction d : Direction.cardinales2()) {
            Coordonnees pionSuivant = suivante(coord, d);
            // Si il est dans le plateau
            if (estDansPlateau(pionSuivant, Coordonnees.NB_LIGNES)) {
                // Si c'est un pion emeni
                if (plateau[pionSuivant.ligne][pionSuivant.colonne].couleur != couleur && plateau[pionSuivant.ligne][pionSuivant.colonne].tourPresente()) {
                    if (hauteurTour > plateau[pionSuivant.ligne][pionSuivant.colonne].hauteur) {
                        enemiAdjacents[nbAmis] = plateau[pionSuivant.ligne][pionSuivant.colonne];
                        nbAmis++;
                    }
                }
            }
        }
        Case[] tabFinal = new Case[nbAmis];
        for (int i = 0; i < nbAmis; i++) {
            tabFinal[i] = enemiAdjacents[i];
        }
        return tabFinal;
    }

    /**
     * Cette fonction permet de déterminer combien de pions adverses sont sur la
     * même ligne et la même colonne.
     *
     * @param coord coordonnées de la case où se trouve la tour à vérifier.
     * @param couleur la couleur de la tour à vérifier (le joueur actif).
     * @param plateau le plateau de jeu
     * @param niveau le niveau du jeu.
     * @return le nombre d'adversaires présents sur la même ligne et sur la même
     * colonne que le joueur actif.
     */
    static Case[] enemiDansLigneEtColonne(Coordonnees coord, char couleur, Case[][] plateau) {
        Case[] enemiDansLigneColonne = new Case[40];
        int hauteurTour = plateau[coord.ligne][coord.colonne].hauteur;
        int nbEnemi = 0;
        Coordonnees coordS = new Coordonnees(coord.ligne, coord.colonne);
        boolean caseVide;
        for (Direction d : Direction.cardinales1()) {
            coordS.ligne = positionSuivante(coord, d).ligne;
            coordS.colonne = positionSuivante(coord, d).colonne;
            caseVide = true;
            // On cherche le premier pion dans la direction d.
            while (estDansPlateau(coordS, Coordonnees.NB_LIGNES) && caseVide) {
                // Si on a trouvé un pion
                if (plateau[coordS.ligne][coordS.colonne].couleur != Case.CAR_VIDE) {
                    caseVide = false;
                    // Si la case est remplie par un pion ami
                    if (plateau[coordS.ligne][coordS.colonne].couleur != couleur && plateau[coordS.ligne][coordS.colonne].tourPresente()) {
                        if (hauteurTour > plateau[coordS.ligne][coordS.colonne].hauteur) {
                            enemiDansLigneColonne[nbEnemi] = plateau[coordS.ligne][coordS.colonne];
                            nbEnemi++;
                        }
                    }
                }
                coordS.ligne = positionSuivante(coordS, d).ligne;
                coordS.colonne = positionSuivante(coordS, d).colonne;
            }
        }
        Case[] tabFinal = new Case[nbEnemi];
        for (int i = 0; i < nbEnemi; i++) {
            tabFinal[i] = enemiDansLigneColonne[i];
        }
        return tabFinal;
    }

    /**
     * Les coordonnées des cases sur la même ligne (sans celles de la case
     * d'origine).
     *
     * @param coord les coordonnées de la case d'origine
     * @return les coordonnées des cases sur la même ligne
     */
    static Stream<Coordonnees> memeLigne(final Coordonnees coord) {
        return IntStream.rangeClosed(0, Coordonnees.NB_LIGNES - 1).boxed()
                .filter(col -> col != coord.colonne)
                .map(col -> new Coordonnees(coord.ligne, col));
    }

    /**
     * Les coordonnées des cases sur la même colonne (sans celles de la case
     * d'origine).
     *
     * @param coord les coordonnées de la case d'origine
     * @return les coordonnées des cases sur la même colonne
     */
    static Stream<Coordonnees> memeColonne(final Coordonnees coord) {
        return IntStream.rangeClosed(0, Coordonnees.NB_COLONNES - 1).boxed()
                .filter(lig -> lig != coord.ligne)
                .map(lig -> new Coordonnees(lig, coord.colonne));
    }

    /**
     * Programme principal. Il sera lancé automatiquement, ce n'est pas à vous
     * de le lancer.
     *
     * @param args Arguments.
     */
    public static void main(String[] args) {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSS");
        System.out.println("Démarrage le " + format.format(new Date()));
        System.out.flush();
        // « create » du protocole du grand ordonnateur.
        final String USAGE
                = System.lineSeparator()
                + "\tUsage : java " + IATowa.class.getName()
                + " <hôte> <port> <ordre>";
        if (args.length != 3) {
            System.out.println("Nombre de paramètres incorrect." + USAGE);
            System.out.flush();
            System.exit(1);
        }
        String hote = args[0];
        int port = -1;
        try {
            port = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            System.out.println("Le port doit être un entier." + USAGE);
            System.out.flush();
            System.exit(1);
        }
        int ordre = -1;
        try {
            ordre = Integer.parseInt(args[2]);
        } catch (NumberFormatException e) {
            System.out.println("L'ordre doit être un entier." + USAGE);
            System.out.flush();
            System.exit(1);
        }
        try {
            char couleurJoueur = (ordre == 1 ? 'N' : 'B');
            IATowa iaTowa = new IATowa(hote, port, couleurJoueur);
            iaTowa.connexion();
            iaTowa.toursDeJeu();
        } catch (IOException e) {
            System.out.println("Erreur à l'exécution du programme : \n" + e);
            System.out.flush();
            System.exit(1);
        }
    }
}
