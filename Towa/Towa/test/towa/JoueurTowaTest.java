package towa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 * Tests sur la classe JoueurTowa.
 */
public class JoueurTowaTest {

    /**
     * Test de la fonction actionsPossibles. Commentez les appels aux tests des
     * niveaux inférieurs, n'activez que le test du niveau à valider.
     */
    @Test
    public void testActionsPossibles() {
        // testActionsPossibles_niveau1();
        // testActionsPossibles_niveau2();
        // testActionsPossibles_niveau3();
        // testActionsPossibles_niveau4();
        // testActionsPossibles_niveau5();
        // testActionsPossibles_niveau6();
        // testActionsPossibles_niveau7();
        // testActionsPossibles_niveau8();
        // testActionsPossibles_niveau9();
        // testActionsPossibles_niveau10();
        // testActionsPossibles_niveau11();
        testActionsPossibles_niveau12();
    }

    /**
     * Test de la fonction actionsPossibles, au niveau 1.
     */
    public void testActionsPossibles_niveau1() {
        JoueurTowa joueur = new JoueurTowa();
        // un plateau sur lequel on veut tester actionsPossibles()
        Case[][] plateau = Utils.plateauDepuisTexte(PLATEAU_NIVEAU1);
        // on choisit la couleur du joueur
        char couleur = Case.CAR_NOIR;        // on choisit le niveau
        int niveau = 1;
        // on lance actionsPossibles
        String[] actionsPossiblesDepuisPlateau = joueur.actionsPossibles(plateau, couleur, niveau);
        ActionsPossibles actionsPossibles
                = new ActionsPossibles(actionsPossiblesDepuisPlateau);
        // on peut afficher toutes les actions possibles calculées :
        actionsPossibles.afficher();
        // on peut aussi tester si une action est dans les actions possibles :
        assertTrue(actionsPossibles.contient("PaB,1,0"));
        // on peut aussi tester si une action n'est pas dans les actions 
        // possibles :
        assertFalse(actionsPossibles.contient("PaQ,1,0"));
        assertFalse(actionsPossibles.contient("PaA,0,0"));
        // testons les 4 coins :
        assertTrue(actionsPossibles.contient("PaA,1,0"));
        assertTrue(actionsPossibles.contient("PpA,1,0"));
        assertTrue(actionsPossibles.contient("PaP,1,0"));
        assertTrue(actionsPossibles.contient("PpP,1,0"));
        // vérifions s'il y a le bon nombre d'actions possibles :
        assertEquals(Coordonnees.NB_LIGNES * Coordonnees.NB_COLONNES,
                actionsPossibles.nbActions);
    }

    /**
     * Test de la fonction actionsPossibles, au niveau 2.
     */
    public void testActionsPossibles_niveau2() {
        JoueurTowa joueur = new JoueurTowa();
        Case[][] plateau = Utils.plateauDepuisTexte(PLATEAU_NIVEAU2);
        // sur le plateau initial : 27 pions noirs et 20 pions blancs
        int niveau = 2;
        // 1 - joueur noir
        char couleur = Case.CAR_NOIR;
        // on lance actionsPossibles
        String[] actionsPossiblesDepuisPlateau = joueur.actionsPossibles(plateau, couleur, niveau);
        ActionsPossibles actionsPossibles
                = new ActionsPossibles(actionsPossiblesDepuisPlateau);
        // pose sur case vide : possible
        assertTrue(actionsPossibles.contient("PaB,28,20"));
        // pose sur case de même couleur : possible
        assertTrue(actionsPossibles.contient("PbA,28,20"));
        // pose sur case de couleur opposée : impossible
        assertFalse(actionsPossibles.contient("PaG,28,20"));
        // pose sur case de même couleur et hauteur > 1 : possible
        assertTrue(actionsPossibles.contient("PcK,28,20"));
        // 2 - joueur blanc
        couleur = Case.CAR_BLANC;
        // on lance actionsPossibles
        actionsPossiblesDepuisPlateau = joueur.actionsPossibles(plateau, couleur, niveau);
        actionsPossibles = new ActionsPossibles(actionsPossiblesDepuisPlateau);
        // pose sur case vide : possible
        assertTrue(actionsPossibles.contient("PaB,27,21"));
        // pose sur case de même couleur : possible
        assertTrue(actionsPossibles.contient("PaG,27,21"));
        // pose sur case de couleur opposée : impossible
        assertFalse(actionsPossibles.contient("PbA,27,21"));
        // pose sur case de même couleur et hauteur > 1 : possible
        assertTrue(actionsPossibles.contient("PlF,27,21"));
    }

    /**
     * Test de la fonction actionsPossibles, au niveau 3.
     */
    public void testActionsPossibles_niveau3() {
        JoueurTowa joueur = new JoueurTowa();
        Case[][] plateau = Utils.plateauDepuisTexte(PLATEAU_NIVEAU2);
        // sur le plateau initial : 27 pions noirs et 20 pions blancs
        int niveau = 3;
        // 1 - joueur noir
        char couleur = Case.CAR_NOIR;
        // on lance actionsPossibles
        String[] actionsPossiblesDepuisPlateau = joueur.actionsPossibles(plateau, couleur, niveau);
        ActionsPossibles actionsPossibles
                = new ActionsPossibles(actionsPossiblesDepuisPlateau);
        // pose sur case vide : possible
        assertTrue(actionsPossibles.contient("PaB,28,20"));
        // pose sur case avec un seul pion de même couleur : possible
        assertTrue(actionsPossibles.contient("PbA,28,20"));
        // pose sur case avec 5 pions de même couleur : impossible
        assertFalse(actionsPossibles.contient("PcK,28,20"));
        // 2 - joueur blanc
        couleur = Case.CAR_BLANC;
        // on lance actionsPossibles
        actionsPossiblesDepuisPlateau = joueur.actionsPossibles(plateau, couleur, niveau);
        actionsPossibles = new ActionsPossibles(actionsPossiblesDepuisPlateau);
        // pose sur case avec 3 pions de même couleur : possible
        assertTrue(actionsPossibles.contient("PmF,27,21"));
        // pose sur case avec 4 pions de même couleur : impossible
        assertFalse(actionsPossibles.contient("PlF,27,21"));
    }

    /**
     * Test de la fonction actionsPossibles, au niveau 4.
     */
    public void testActionsPossibles_niveau4() {
        JoueurTowa joueur = new JoueurTowa();
        Case[][] plateau = Utils.plateauDepuisTexte(PLATEAU_NIVEAU2);
        // sur le plateau initial : 27 pions noirs et 20 pions blancs
        int niveau = 4;
        // 1 - joueur noir
        char couleur = Case.CAR_NOIR;
        // on lance actionsPossibles
        String[] actionsPossiblesDepuisPlateau = joueur.actionsPossibles(plateau, couleur, niveau);
        ActionsPossibles actionsPossibles
                = new ActionsPossibles(actionsPossiblesDepuisPlateau);
        // activation sans effet, ne détruit pas les tours adverses de même hauteur : possible
        assertTrue(actionsPossibles.contient("AnG,27,20"));
        // activation d'une tour de la mauvaise couleur : impossible
        assertFalse(actionsPossibles.contient("AaG,27,20"));
        // 2 - joueur blanc
        couleur = Case.CAR_BLANC;
        // on lance actionsPossibles
        actionsPossiblesDepuisPlateau = joueur.actionsPossibles(plateau, couleur, niveau);
        actionsPossibles = new ActionsPossibles(actionsPossiblesDepuisPlateau);
        // activation d'une case vide : impossible
        assertFalse(actionsPossibles.contient("AaA,27,20"));
        // activation d'une tour sur le bord : possible
        assertTrue(actionsPossibles.contient("AaG,27,20"));
        // activation d'une tour contre une tour adverse de même hauteur sans effet : possible
        assertTrue(actionsPossibles.contient("ApG,27,20"));
        // destruction de tours adverse : possible
        assertTrue(actionsPossibles.contient("AmF,24,20"));
        assertTrue(actionsPossibles.contient("AlF,22,20"));
    }

    /**
     * Test de la fonction actionsPossibles, au niveau 5.
     */
    public void testActionsPossibles_niveau5() {
        JoueurTowa joueur = new JoueurTowa();
        Case[][] plateau = Utils.plateauDepuisTexte(PLATEAU_NIVEAU2);
        // sur le plateau initial : 27 pions noirs et 20 pions blancs
        int niveau = 5;
        // 1 - joueur noir
        char couleur = Case.CAR_NOIR;
        // on lance actionsPossibles
        String[] actionsPossiblesDepuisPlateau = joueur.actionsPossibles(plateau, couleur, niveau);
        ActionsPossibles actionsPossibles
                = new ActionsPossibles(actionsPossiblesDepuisPlateau);
        // pose à côté de 2 blancs : possible
        assertTrue(actionsPossibles.contient("PaH,29,20"));
        // pose à côté d'un blanc et d'un noir : possible
        assertTrue(actionsPossibles.contient("PkF,29,20"));
        // pose à côté d'un noir : possible
        assertTrue(actionsPossibles.contient("PiH,28,20"));
        // 2 - joueur blanc
        couleur = Case.CAR_BLANC;
        // on lance actionsPossibles
        actionsPossiblesDepuisPlateau = joueur.actionsPossibles(plateau, couleur, niveau);
        actionsPossibles = new ActionsPossibles(actionsPossiblesDepuisPlateau);
        // pose à côté de rien : possible
        assertTrue(actionsPossibles.contient("PfF,27,21"));
        // pose à côté d'un noir : possible
        assertTrue(actionsPossibles.contient("PhL,27,22"));
        // pose  à côté d'un noir mais sur un blanc : possible
        assertTrue(actionsPossibles.contient("PlG,27,21"));
    }

    /**
     * Test de la fonction actionsPossibles, au niveau 6.
     */
    public void testActionsPossibles_niveau6() {
        JoueurTowa joueur = new JoueurTowa();
        Case[][] plateau = Utils.plateauDepuisTexte(PLATEAU_NIVEAU2);
        // sur le plateau initial : 27 pions noirs et 20 pions blancs
        int niveau = 6;
        // 1 - joueur noir
        char couleur = Case.CAR_NOIR;
        // on lance actionsPossibles
        String[] actionsPossiblesDepuisPlateau = joueur.actionsPossibles(plateau, couleur, niveau);
        ActionsPossibles actionsPossibles
                = new ActionsPossibles(actionsPossiblesDepuisPlateau);
        // activation avec un adversaire dans la ligne mais de même hauteur : possible
        assertTrue(actionsPossibles.contient("AbA,27,20"));
        // activation avec un adversaire de qui doit être effacé : possible
        assertTrue(actionsPossibles.contient("AkJ,27,19"));
        // activation avec plusieurs pions adverses dans la même colonne : possible
        assertTrue(actionsPossibles.contient("AnG,27,17"));
        // 2 - joueur blanc
        couleur = Case.CAR_BLANC;
        // on lance actionsPossibles
        actionsPossiblesDepuisPlateau = joueur.actionsPossibles(plateau, couleur, niveau);
        actionsPossibles = new ActionsPossibles(actionsPossiblesDepuisPlateau);
        // activation avec aucun pion dans la ligne ni dans la colonne : possible
        assertTrue(actionsPossibles.contient("AdI,27,20"));
        // activation avec des pions adverses adjacents et dans la même ligne : possible
        assertTrue(actionsPossibles.contient("AlF,21,20"));
        assertTrue(actionsPossibles.contient("PmB,27,21"));
        assertTrue(actionsPossibles.contient("PmB,27,21"));
    }

    /**
     * Test de la fonction actionsPossibles, au niveau 7.
     */
    public void testActionsPossibles_niveau7() {
        JoueurTowa joueur = new JoueurTowa();
        Case[][] plateau = Utils.plateauDepuisTexte(PLATEAU_NIVEAU2);
        // sur le plateau initial : 27 pions noirs et 20 pions blancs
        int niveau = 7;
        // 1 - joueur noir
        char couleur = Case.CAR_NOIR;
        // on lance actionsPossibles
        String[] actionsPossiblesDepuisPlateau = joueur.actionsPossibles(plateau, couleur, niveau);
        ActionsPossibles actionsPossibles
                = new ActionsPossibles(actionsPossiblesDepuisPlateau);
        // activation d'une tour avec un blanc adjacent, deux blancs dans la ligne et un blanc protégé : possible
        assertTrue(actionsPossibles.contient("AcK,27,17"));
        // activation avec un blanc sur la colonne et un noir qui protège un blanc : possible
        assertTrue(actionsPossibles.contient("AnG,27,19"));
        // activation avec plusieurs pions blancs protégés par pion blanc et pion noir : possible
        assertTrue(actionsPossibles.contient("AlE,27,17"));
        // 2 - joueur blanc
        couleur = Case.CAR_BLANC;
        // on lance actionsPossibles
        actionsPossiblesDepuisPlateau = joueur.actionsPossibles(plateau, couleur, niveau);
        actionsPossibles = new ActionsPossibles(actionsPossiblesDepuisPlateau);
        // activation avec aucun pion dans la ligne ni dans la colonne : possible
        assertTrue(actionsPossibles.contient("AdI,27,20"));
        // activation avec des pions adverses adjacents et dans la même ligne et un noir protégé par un blanc : possible
        assertTrue(actionsPossibles.contient("AlF,22,20"));
    }

    /**
     * Test de la fonction actionsPossibles, au niveau 8.
     */
    public void testActionsPossibles_niveau8() {
        JoueurTowa joueur = new JoueurTowa();
        Case[][] plateau = Utils.plateauDepuisTexte(PLATEAU_NIVEAU2);
        // sur le plateau initial : 27 pions noirs et 20 pions blancs
        int niveau = 8;
        // 1 - joueur noir
        char couleur = Case.CAR_NOIR;
        // on lance actionsPossibles
        String[] actionsPossiblesDepuisPlateau = joueur.actionsPossibles(plateau, couleur, niveau);
        ActionsPossibles actionsPossibles
                = new ActionsPossibles(actionsPossiblesDepuisPlateau);
        // fusion d'un pion avec perte d'un pion : possible
        assertTrue(actionsPossibles.contient("FmG,26,20"));
        // fusion d'une tour sans effet : possible
        assertTrue(actionsPossibles.contient("FbA,27,20"));
        // 2 - joueur blanc
        couleur = Case.CAR_BLANC;
        // on lance actionsPossibles
        actionsPossiblesDepuisPlateau = joueur.actionsPossibles(plateau, couleur, niveau);
        actionsPossibles = new ActionsPossibles(actionsPossiblesDepuisPlateau);
        // fusion sur une tour de hauteur 4 : possible
        assertTrue(actionsPossibles.contient("FlF,27,16"));
        // fusion de deux pions blancs : aucun changements sur le nombre de pions dans le plateau : possible
        assertTrue(actionsPossibles.contient("FcE,27,20"));
    }

    /**
     * Test de la fonction actionsPossibles, au niveau 9.
     */
    public void testActionsPossibles_niveau9() {
        JoueurTowa joueur = new JoueurTowa();
        Case[][] plateau = Utils.plateauDepuisTexte(PLATEAU_NIVEAU2);
        // sur le plateau initial : 27 pions noirs et 20 pions blancs
        int niveau = 9;
        // 1 - joueur noir
        char couleur = Case.CAR_NOIR;
        // on lance actionsPossibles
        String[] actionsPossiblesDepuisPlateau = joueur.actionsPossibles(plateau, couleur, niveau);
        ActionsPossibles actionsPossibles
                = new ActionsPossibles(actionsPossiblesDepuisPlateau);
        // action de chatons kamikazes dans la direction nord
        assertTrue(actionsPossibles.contient("CN,18,9"));
        // action de chatons kamikazes dans la direction sud
        assertTrue(actionsPossibles.contient("CS,19,15"));
        // 2 - joueur blanc
        couleur = Case.CAR_BLANC;
        // on lance actionsPossibles
        actionsPossiblesDepuisPlateau = joueur.actionsPossibles(plateau, couleur, niveau);
        actionsPossibles = new ActionsPossibles(actionsPossiblesDepuisPlateau);
        // action de chatons kamikazes dans la direction ouest
        assertTrue(actionsPossibles.contient("CO,17,13"));
        // action de chatons kamikazes dans la direction est
        assertTrue(actionsPossibles.contient("CE,21,11"));
    }

    /**
     * Test de la fonction actionsPossibles, au niveau 10.
     */
    public void testActionsPossibles_niveau10() {
        JoueurTowa joueur = new JoueurTowa();
        Case[][] plateau = Utils.plateauDepuisTexte(PLATEAU_NIVEAU10);
        // sur le plateau initial : 3 pions noirs et 20 pions blancs
        int niveau = 10;
        // 1 - joueur noir
        char couleur = Case.CAR_NOIR;
        // on lance actionsPossibles
        String[] actionsPossiblesDepuisPlateau = joueur.actionsPossibles(plateau, couleur, niveau);
        ActionsPossibles actionsPossibles
                = new ActionsPossibles(actionsPossiblesDepuisPlateau);
        // Action d'activation avec des tours blanches de niveau 4 mais de hauteur inférieurs
        assertTrue(actionsPossibles.contient("AbC,3,14"));
        // Action de pose sur une tour de niveau 4 mais de hauteur 2 : impossible
        assertFalse(actionsPossibles.contient("PbC,4,20"));
        // Action de pose sur une case vide d'altitude 3 et avec un pion adverse à côté : impossible
        assertFalse(actionsPossibles.contient("PgC,4,20"));
        // 2 - joueur blanc
        couleur = Case.CAR_BLANC;
        // on lance actionsPossibles
        actionsPossiblesDepuisPlateau = joueur.actionsPossibles(plateau, couleur, niveau);
        actionsPossibles = new ActionsPossibles(actionsPossiblesDepuisPlateau);
        // action de fusion sur un tour de niveau 3  et de hauteur 1 : perte de 2 pions
        assertTrue(actionsPossibles.contient("FaD,3,18"));
        // action d'activation d'une tour blanche avec une hauteur de 3 et qui détruit un noir
        assertTrue(actionsPossibles.contient("AbE,2,20"));
    }

    /**
     * Test de la fonction actionsPossibles, au niveau 11.
     */
    public void testActionsPossibles_niveau11() {
        JoueurTowa joueur = new JoueurTowa();
        Case[][] plateau = Utils.plateauDepuisTexte(PLATEAU_NIVEAU11);
        // sur le plateau initial : 9 pions noirs et 23 pions blancs
        int niveau = 11;
        // 1 - joueur noir
        char couleur = Case.CAR_NOIR;
        // on lance actionsPossibles
        String[] actionsPossiblesDepuisPlateau = joueur.actionsPossibles(plateau, couleur, niveau);
        ActionsPossibles actionsPossibles
                = new ActionsPossibles(actionsPossiblesDepuisPlateau);
        // on peut afficher toutes les actions possibles calculées :
        actionsPossibles.afficher();
        // Action de magie sur sur un niveau trop haut : impossible
        assertFalse(actionsPossibles.contient("MfG,9,23"));
        // Action de magie qui déplace le pion noir sur un pion de la même couleur : impossible
        assertFalse(actionsPossibles.contient("MhG,9,23"));
        // Action de magie qui déplace le pion noir sur un pion adverse : impossible
        assertFalse(actionsPossibles.contient("MhH,9,23"));
        // 2 - joueur blanc
        couleur = Case.CAR_BLANC;
        // on lance actionsPossibles
        actionsPossiblesDepuisPlateau = joueur.actionsPossibles(plateau, couleur, niveau);
        actionsPossibles = new ActionsPossibles(actionsPossiblesDepuisPlateau);
        // action de magie qui déplace le pion blanc par symétrie centrale : possible
        assertTrue(actionsPossibles.contient("MfC,9,23"));
        // action de magie qui déplace un pion blanc à côté d'un pion noir : pas de changements de hauteur du pion : possible
        assertTrue(actionsPossibles.contient("MhI,9,23"));
        // action de magie qui déplace un pion blanc sur un niveau, sans dépasser la hauteur maximale : possible
        assertTrue(actionsPossibles.contient("MeG,9,23"));
    }

    /**
     * Test de la fonction actionsPossibles, au niveau 12.
     */
    public void testActionsPossibles_niveau12() {
        JoueurTowa joueur = new JoueurTowa();
        Case[][] plateau = Utils.plateauDepuisTexte(PLATEAU_NIVEAU12);
        // sur le plateau initial : 36 pions noirs et 23 pions blancs
        int niveau = 12;
        // 1 - joueur noir
        char couleur = Case.CAR_NOIR;
        // on lance actionsPossibles
        String[] actionsPossiblesDepuisPlateau = joueur.actionsPossibles(plateau, couleur, niveau);
        ActionsPossibles actionsPossibles
                = new ActionsPossibles(actionsPossiblesDepuisPlateau);
        // on peut afficher toutes les actions possibles calculées :
        actionsPossibles.afficher();
        // Action de pose sur un case d'Eau : impossible
        assertFalse(actionsPossibles.contient("PgD,38,23"));
        // Action de pose sur une case Terre : possible
        assertTrue(actionsPossibles.contient("PhG,38,23"));
        // 2 - joueur blanc
        couleur = Case.CAR_BLANC;
        // on lance actionsPossibles
        actionsPossiblesDepuisPlateau = joueur.actionsPossibles(plateau, couleur, niveau);
        actionsPossibles = new ActionsPossibles(actionsPossiblesDepuisPlateau);
        // action de magie qui déplace le pion blanc sur une case d'Eau : impossible
        assertFalse(actionsPossibles.contient("MiI,36,23"));
        // action de magie qui déplace un pion blanc sur une case Terre vide : possible
        assertTrue(actionsPossibles.contient("McC,36,23"));
    }

    @Test
    public void testNbPions() {
        // plateau1 : 0 noir, 0 blanc
        Case[][] plateau1 = Utils.plateauDepuisTexte(PLATEAU_NIVEAU1);
        assertEquals(0, JoueurTowa.nbPions(plateau1).nbPionsNoirs);
        assertEquals(0, JoueurTowa.nbPions(plateau1).nbPionsBlancs);
        // plateau2 : 27 noirs, 20 blancs
        Case[][] plateau2 = Utils.plateauDepuisTexte(PLATEAU_NIVEAU2);
        assertEquals(27, JoueurTowa.nbPions(plateau2).nbPionsNoirs);
        assertEquals(20, JoueurTowa.nbPions(plateau2).nbPionsBlancs);
    }

    @Test
    public void testPosePossible() {
        JoueurTowa joueur = new JoueurTowa();
        Case[][] plateau = Utils.plateauDepuisTexte(PLATEAU_NIVEAU2);
        // sur le plateau initial : 27 pions noirs et 20 pions blancs
        int niveau = 2;
        // 1 - joueur noir
        char couleur = Case.CAR_NOIR;
        // pose sur case vide : possible
        assertTrue(joueur.posePossible(plateau, new Coordonnees(0, 1), couleur, niveau));
        // pose sur case de même couleur : possible
        assertTrue(joueur.posePossible(plateau, new Coordonnees(1, 0), couleur, niveau));
        // pose sur case de couleur opposée : impossible
        assertFalse(joueur.posePossible(plateau, new Coordonnees(0, 6), couleur, niveau));
        // pose sur case de même couleur et hauteur > 1 : possible
        assertTrue(joueur.posePossible(plateau, new Coordonnees(2, 10), couleur, niveau));
        // 2 - joueur blanc
        couleur = Case.CAR_BLANC;
        // pose sur case vide : possible
        assertTrue(joueur.posePossible(plateau, new Coordonnees(0, 1), couleur, niveau));
        // pose sur case de même couleur : possible
        assertTrue(joueur.posePossible(plateau, new Coordonnees(0, 6), couleur, niveau));
        // pose sur case de couleur opposée : impossible
        assertFalse(joueur.posePossible(plateau, new Coordonnees(1, 0), couleur, niveau));
        // pose sur case de même couleur et hauteur > 1 : possible
        assertTrue(joueur.posePossible(plateau, new Coordonnees(11, 5), couleur, niveau));
    }

    /**
     * Test de la fonction ajoutActionPose.
     */
    @Test
    public void testAjoutActionPose() {
        JoueurTowa joueur = new JoueurTowa();
        ActionsPossibles actions = new ActionsPossibles();
        int niveau = 3;
        Case[][] plateau = Utils.plateauDepuisTexte(PLATEAU_NIVEAU2);
        NbPions nbPions = new NbPions(0, 0);
        // pour l'instant pas d'action possible
        assertEquals(0, actions.nbActions);
        // on crée le tableau d'actions et on en ajoute une
        joueur.ajoutActionPose(Coordonnees.depuisCars('f', 'D'), actions,
                nbPions, Case.CAR_NOIR, niveau, plateau);
        // l'action est devenue possible
        assertTrue(actions.contient("PfD,1,0"));
        // une action possible mais qui n'a pas encore été ajoutée
        assertFalse(actions.contient("PbH,1,0"));
        // pour l'instant une seule action possible
        assertEquals(1, actions.nbActions);
        // ajout d'une deuxième action possible
        joueur.ajoutActionPose(Coordonnees.depuisCars('b', 'H'), actions,
                nbPions, Case.CAR_NOIR, niveau, plateau);
        // l'action a bien été ajoutée
        assertTrue(actions.contient("PbH,1,0"));
        // désormais, deux actions possibles
        assertEquals(2, actions.nbActions);
    }

    /**
     * Un plateau de base, sous forme de chaîne. Pour construire une telle
     * chaîne depuis votre sortie.log, déclarez simplement : final String
     * MON_PLATEAU = ""; puis copiez le plateau depuis votre sortie.log, et
     * collez-le entre les guillemets. Puis Alt+Shift+f pour mettre en forme.
     */
    final String PLATEAU_NIVEAU1
            = "   A   B   C   D   E   F   G   H   I   J   K   L   M   N   O   P \n"
            + " +---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+\n"
            + "a|   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |\n"
            + " +---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+\n"
            + "b|   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |\n"
            + " +---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+\n"
            + "c|   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |\n"
            + " +---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+\n"
            + "d|   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |\n"
            + " +---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+\n"
            + "e|   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |\n"
            + " +---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+\n"
            + "f|   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |\n"
            + " +---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+\n"
            + "g|   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |\n"
            + " +---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+\n"
            + "h|   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |\n"
            + " +---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+\n"
            + "i|   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |\n"
            + " +---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+\n"
            + "j|   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |\n"
            + " +---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+\n"
            + "k|   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |\n"
            + " +---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+\n"
            + "l|   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |\n"
            + " +---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+\n"
            + "m|   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |\n"
            + " +---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+\n"
            + "n|   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |\n"
            + " +---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+\n"
            + "o|   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |\n"
            + " +---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+\n"
            + "p|   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |\n"
            + " +---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+";

    final String PLATEAU_NIVEAU2
            = "   A   B   C   D   E   F   G   H   I   J   K   L   M   N   O   P \n"
            + " +---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+\n"
            + "a|   |   |   |   |   |   |B1 |   |   |   |   |   |   |   |   |   |\n"
            + " +---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+\n"
            + "b|N1 |   |   |   |   |   |   |B1 |   |   |   |B1 |   |   |   |   |\n"
            + " +---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+\n"
            + "c|   |   |B1 |   |B1 |   |   |   |   |   |N5 |B1 |   |   |   |   |\n"
            + " +---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+\n"
            + "d|   |   |   |   |   |   |   |   |B1 |   |   |   |   |   |   |   |\n"
            + " +---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+\n"
            + "e|B1 |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |\n"
            + " +---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+\n"
            + "f|   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |\n"
            + " +---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+\n"
            + "g|   |   |B1 |   |   |   |   |   |   |N1 |   |   |   |   |   |   |\n"
            + " +---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+\n"
            + "h|   |   |   |   |   |   |   |   |   |   |N1 |   |   |   |   |   |\n"
            + " +---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+\n"
            + "i|   |N1 |N1 |   |   |   |   |   |   |   |   |   |N1 |   |   |   |\n"
            + " +---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+\n"
            + "j|   |   |   |   |   |   |   |N1 |   |   |   |B1 |   |   |   |   |\n"
            + " +---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+\n"
            + "k|   |   |   |   |N1 |   |   |   |   |N2 |   |   |   |   |B1 |   |\n"
            + " +---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+\n"
            + "l|   |   |   |   |N3 |B4 |B1 |   |   |   |   |   |   |   |   |   |\n"
            + " +---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+\n"
            + "m|   |   |   |   |B1 |B2 |N1 |   |   |N1 |   |   |   |   |   |   |\n"
            + " +---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+\n"
            + "n|   |   |   |   |N1 |N1 |N2 |   |   |N1 |   |   |   |   |   |   |\n"
            + " +---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+\n"
            + "o|   |N1 |   |   |   |   |   |N1 |   |   |   |   |   |   |   |   |\n"
            + " +---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+\n"
            + "p|   |   |   |   |   |   |B1 |   |   |   |   |   |   |   |   |   |\n"
            + " +---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+\n";

    final String PLATEAU_NIVEAU10
            = "   A   B   C   D   E   F   G   H   I   J   K   L   M   N   O   P \n"
            + " +---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+\n"
            + "a|   |   |   |B12|   |   |   |   |   |   |   |   |   |   |   |   |\n"
            + " +---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+\n"
            + "b|B3 |   |N22|N11|B3 |   |   |   |   |   |   |   |   |   |   |   |\n"
            + " +---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+\n"
            + "c|   |B31|  4|B4 |   |   |  2|   |   |   |   |   |   |   |   |   |\n"
            + " +---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+\n"
            + "d|   |   |B21|  2|   |   |   |   |   |   |   |   |   |   |   |   |\n"
            + " +---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+\n"
            + "e|B1 |   |   |  4|   |   |   |   |   |   |   |   |   |   |   |   |\n"
            + " +---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+\n"
            + "f|   |   |B3 |   |   |  3|   |   |   |   |   |   |   |   |   |   |\n"
            + " +---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+\n"
            + "g|   |   |  3|   |   |   |   |   |   |   |   |   |   |   |   |   |\n"
            + " +---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+\n"
            + "h|   |  4|   |   |   |   |   |   |   |   |   |   |   |   |   |   |\n"
            + " +---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+\n"
            + "i|   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |\n"
            + " +---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+\n"
            + "j|   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |\n"
            + " +---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+\n"
            + "k|   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |\n"
            + " +---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+\n"
            + "l|   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |\n"
            + " +---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+\n"
            + "m|   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |\n"
            + " +---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+\n"
            + "n|   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |\n"
            + " +---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+\n"
            + "o|   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |\n"
            + " +---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+\n"
            + "p|   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |\n"
            + " +---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+\n";

    final String PLATEAU_NIVEAU11
            = "   A   B   C   D   E   F   G   H   I   J   K   L   M   N   O   P \n"
            + " +---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+\n"
            + "a|   |   |   |B12|   |   |   |   |   |   |   |   |   |   |   |   |\n"
            + " +---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+\n"
            + "b|B3 |   |N22|N11|B3 |   |   |   |   |   |   |   |   |   |   |   |\n"
            + " +---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+\n"
            + "c|   |B31|  4|B4 |   |   |  2|   |   |   |   |   |   |   |   |   |\n"
            + " +---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+\n"
            + "d|   |   |B21|  2|   |   |   |   |   |   |   |   |   |   |   |   |\n"
            + " +---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+\n"
            + "e|B1 |   |   |  4|   |   |B1 |   |   |   |   |   |   |   |   |   |\n"
            + " +---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+\n"
            + "f|   |   |B3 |   |   |  3|N2 |   |   |   |   |   |   |   |   |   |\n"
            + " +---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+\n"
            + "g|   |   |  3|   |   |   |   |   |   |   |   |   |   |   |   |   |\n"
            + " +---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+\n"
            + "h|   |  4|   |   |   |   |N1 |N1 |B1 |   |   |   |   |   |   |   |\n"
            + " +---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+\n"
            + "i|   |   |   |   |   |   |   |   |B1 |N2 |   |   |   |   |   |   |\n"
            + " +---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+\n"
            + "j|   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |\n"
            + " +---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+\n"
            + "k|   |   |   |   |   |   |   |   |   |  4|   |   |   |   |   |   |\n"
            + " +---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+\n"
            + "l|   |   |   |   |   |   |   |   |   |  1|   |   |   |   |   |   |\n"
            + " +---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+\n"
            + "m|   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |\n"
            + " +---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+\n"
            + "n|   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |\n"
            + " +---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+\n"
            + "o|   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |\n"
            + " +---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+\n"
            + "p|   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |\n"
            + " +---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+\n";

    final String PLATEAU_NIVEAU12
            = "   A   B   C   D   E   F   G   H   I   J   K   L   M   N   O   P \n"
            + " +---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+\n"
            + "a|   |   |   |   |   |N4 |B1 |   |   |   |   |   |   |   |   |   |\n"
            + " +---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+\n"
            + "b|N1 |   |   |   |   |   |   |B1 |   |   |   |B1 |   |   |   |   |\n"
            + " +---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+\n"
            + "c|   |   |B1 |   |B1 |   |   |   |   |   |N1 |B1 |   |   |   |   |\n"
            + " +---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+\n"
            + "d|   |   |   |   |   |   |   |   |B1 |   |   |   |   |   |   |   |\n"
            + " +---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+\n"
            + "e|B1 |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |\n"
            + " +---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+\n"
            + "f|   |   |   |   |   |N2 |   |B11|   |   |   |   |   |   |   |   |\n"
            + " +---+---+---+E--+---+---+---+---+---+---+---+---+---+---+---+---+\n"
            + "g|   |   |B2 |   |   |   |   |  2|   |N1 |   |   |   |   |   |   |\n"
            + " +---+---+---+---+---+---+---+E--+---+---+---+---+---+---+---+---+\n"
            + "h|   |   |   |   |   |B4 |   |  3|  3|   |N1 |   |   |   |   |   |\n"
            + " +---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+\n"
            + "i|   |N1 |N1 |   |   |   |   |B22|B1 |   |   |   |N1 |   |   |   |\n"
            + " +---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+\n"
            + "j|   |   |   |   |   |   |N1 |   |N2 |   |   |B1 |   |   |   |   |\n"
            + " +---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+\n"
            + "k|   |   |   |   |N1 |   |   |   |   |N2 |   |   |   |   |B1 |   |\n"
            + " +---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+\n"
            + "l|N1 |N4 |   |   |N3 |   |B1 |   |   |   |   |   |   |   |   |N1 |\n"
            + " +---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+\n"
            + "m|   |   |   |   |B1 |   |   |   |   |N1 |   |   |   |   |   |   |\n"
            + " +---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+\n"
            + "n|   |   |   |   |N1 |N1 |N2 |   |   |N1 |   |   |   |   |   |   |\n"
            + " +---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+\n"
            + "o|   |N1 |   |   |   |   |   |N1 |   |   |   |   |   |   |   |   |\n"
            + " +---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+\n"
            + "p|   |   |   |   |   |   |B1 |   |   |   |   |   |   |   |   |   |\n"
            + " +---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+";

}
