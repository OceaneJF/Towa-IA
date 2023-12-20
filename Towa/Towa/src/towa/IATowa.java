package towa;

import java.io.IOException;
import static java.lang.Integer.parseInt;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import static towa.PionsAdverses.estDansPlateau;
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
        int max = this.pionsGagnee(actionPossible[0]);
        String meillAction = "";

        for (int i = 0; i < actionPossible.length; i++) {
            String action = actionPossible[i];
            if (max <= evaluation(action, plateau)) {
                max = evaluation(action, plateau);
                meillAction = action;
            }

        }

        return meillAction;
    }

    int evaluation(String action, Case[][] plateau) {
        int score = 0;
        score += pionsGagnee(action);
        score += pionsTuable(action, plateau);
        return score;
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

    /**
     * /**
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
                break;
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
        final int hauteurTourJoueur = plateau[coord.ligne][coord.colonne].hauteur;
        List<Case> aDetruire
                = porteeActivation(coord)
                        .map(aPortee -> plateau[aPortee.ligne][aPortee.colonne])
                        .filter(c -> c.tourPresente()) // une tour
                        .filter(c -> c.couleur != couleurCourante) // ennemie
                        .filter(c -> c.hauteur < hauteurTourJoueur) // plus basse
                        .collect(Collectors.toList());
        for (Case tourADetruire : aDetruire) {
            detruireTour(tourADetruire);
        }
    }

    /**
     * Activer une tour.
     *
     * @param coord coordonnées de la case où se situe la tour à activer
     * @param plateau le plateau de jeu
     * @param couleurCourante couleur du joueur courant
     */
    static void fusionner(Coordonnees coord, Case[][] plateau, char couleurCourante) {
        Case tourJouer = plateau[coord.ligne][coord.colonne];
        List<Case> aFusionner
                = porteeActivation(coord)
                        .map(aPortee -> plateau[aPortee.ligne][aPortee.colonne])
                        .filter(c -> c.tourPresente()) // une tour
                        .filter(c -> c.couleur == couleurCourante) // ami
                        .collect(Collectors.toList());
        for (Case tourAFusionner : aFusionner) {
            fusionnerTour(tourAFusionner, tourJouer);
        }
    }

    /**
     * Détruire une tour.
     *
     * @param laCase la case dont on doit détruire la tour
     */
    static void fusionnerTour(Case laCase, Case joueur) {
        int hauteurAtteint = joueur.hauteur + laCase.hauteur;

        if (hauteurAtteint > 4) {
            joueur.hauteur = 4;
        } else {
            joueur.hauteur += laCase.hauteur;
        }
        laCase.hauteur = 0;
        laCase.couleur = Case.CAR_VIDE;
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
