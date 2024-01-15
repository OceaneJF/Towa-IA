/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package towa;

/**
 *
 * @author cbardot
 */
public class IAStrategie {

    // Ma strat :
    // 1- Si possible, détruire (en activant) une tour de hauteur 3
    // 2- Si on a une tour en cours, l'augmenter de 1
    // 3- Si on peut détruire 4 pions ou plus (en une activation), les détruire. !! A ne pas faire !!
    // 4- Si on peut poser un pion "safe" à côté d'un adversaire, le faire (= commencer une nouvelle tour). Faire de la pose intelligente !
    // 5- Sinon chercher un endroit "safe" pour commencer une nouvelle tour. Faire de la pose intelligente !
    //
    // "safe" = tous les adversaires qui peuvent s'activer contre nous sont au max de même hauteur après pose.
    //
    //
    /**
     *
     * @param plateau
     * @param couleur
     * @param nbTourJeu
     * @return
     */
    static String principale(Case[][] plateau, char couleur, int nbTourJeu) {
        System.out.println(nbTourJeu);
        JoueurTowa joueurTowa = new JoueurTowa();
        String[] actionsPossibles = ActionsPossibles.nettoyerTableau(
                joueurTowa.actionsPossibles(plateau, couleur, 8));
        String actionTemp;
        // Au début, je pose au milieu
        if (nbTourJeu == 1) {
            return "PhH";
        }
        // Feu d'artifice final : LE CLOU DU SPECTACLE !!!
        if (nbTourJeu > IATowa.NB_TOURS_JEU_MAX - 8) {
            // On active pour faire le max de dégats
            actionTemp = activerPourDegommerPlusDeN(plateau, couleur, actionsPossibles, 3);
            if (actionTemp != null) {
                return actionTemp;
            }
            // Mode pose qui peut !!! : on pose 2 pions pour avoir le max de pions posés.
//            actionTemp = posePionSafeACoteAdversaire(plateau, couleur, actionsPossibles);
//            if (actionTemp != null) {
//                return actionTemp;
//            }
        } else {
            // Première règle
            actionTemp = activerPourDetruireTourH3(plateau, couleur, actionsPossibles);
            if (actionTemp != null) {
                return actionTemp;
            }
            //Deuxième règle
            actionTemp = augmenterHauteurDe1(plateau, couleur, actionsPossibles);
            if (actionTemp != null) {
                return actionTemp;
            }

            // Troisième règle : remplacer par le feu d'artifice final pour plus d'efficacité
//        actionTemp = activerPourDegommerPlusDeN(plateau, couleur, actionsPossibles,4);
//        if (actionTemp != null) {
//            return actionTemp;
//        }
        }
        // troisième règle
//        actionTemp = posePionSafeACoteAdversaire(plateau, couleur, actionsPossibles);
        actionTemp = poseIntelligente(plateau, couleur, actionsPossibles, true);
        if (actionTemp != null) {
            return actionTemp;
        }
        // Quatrième règle
//        actionTemp = posePionSafe(plateau, couleur, actionsPossibles);
        actionTemp = poseIntelligente(plateau, couleur, actionsPossibles, false);
        if (actionTemp != null) {
            return actionTemp;
        }

        actionTemp = posePionSafeACoteAdversaire(plateau, couleur, actionsPossibles);
        if (actionTemp != null) {
            return actionTemp;
        }
        actionTemp = posePionSafe(plateau, couleur, actionsPossibles);
        if (actionTemp != null) {
            return actionTemp;
        }
        // Par défault : meilleur action dans le tableau des actions possibles
        // on instancie votre implémentation

        // choisir aléatoirement une action possible
        String actionJouee = null;
        if (actionsPossibles.length > 0) {

            actionJouee = IAMeilleurActionTab.meilleurActionDansTab(actionsPossibles, couleur);
        }

        return actionJouee;
    }

    /**
     * Cinquième règle
     *
     * @param plateau
     * @param couleur
     * @return
     */
    static String posePionSafe(Case[][] plateau, char couleur, String[] actionsPossibles) {
        int i = 0;
        int j = 0;
        int coordI = 0;
        int coordJ = 0;
        boolean tourTrouvee = false;
        String action = null;
        while (i < plateau.length && !tourTrouvee) {
            j = 0;
            while (j < plateau[i].length && !tourTrouvee) {
                if (plateau[i][j].couleur == Case.CAR_VIDE) {
                    if (estSafe(plateau, couleur, new Coordonnees(i, j), 2)) {
                        tourTrouvee = true;
                        coordJ = j;
                        coordI = i;
                    }
                }

                j++;
            }
            i++;
        }
        if (tourTrouvee) {
            action = "P" + ligneVersLettre(coordI) + colonneVersLettre(coordJ);
        }
        if (action != null) {
            if (estUneActionPossible(action, actionsPossibles)) {
                return action;
            }
            return null;
        }

        return null;
    }

    /**
     * Quatrième règle :
     *
     * @param plateau
     * @param couleur
     * @return
     */
    static String posePionSafeACoteAdversaire(Case[][] plateau, char couleur, String[] actionsPossibles) {
        int i = 0;
        int j = 0;
        int coordI = 0;
        int coordJ = 0;
        boolean tourTrouvee = false;
        String action = null;
        while (i < plateau.length && !tourTrouvee) {
            j = 0;
            while (j < plateau[i].length && !tourTrouvee) {
                if (plateau[i][j].couleur == Case.CAR_VIDE) {
                    if (poseEstBonifiee(plateau, couleur, new Coordonnees(i, j))) {
                        if (estSafe(plateau, couleur, new Coordonnees(i, j), 3)) {

                            tourTrouvee = true;
                            coordJ = j;
                            coordI = i;
                        }
                    }
                }
                j++;
            }
            i++;
        }
        if (tourTrouvee) {
            action = "P" + ligneVersLettre(coordI) + colonneVersLettre(coordJ);
        }
        if (action != null) {
            if (estUneActionPossible(action, actionsPossibles)) {
                return action;
            }
            return null;
        }

        return null;
    }

    static boolean poseEstBonifiee(Case[][] plateau, char couleur, Coordonnees coord) {
        for (Direction d : Direction.cardinales1()) {
            Coordonnees coordTemp = PionsAdverses.suivante(coord, d);
            if (PionsAdverses.estDansPlateau(coordTemp, Coordonnees.NB_LIGNES)) {
                if (plateau[coordTemp.ligne][coordTemp.colonne].couleur != Case.CAR_VIDE && plateau[coordTemp.ligne][coordTemp.colonne].couleur != couleur) {
                    return true;
                }
            }

        }
        for (Direction d : Direction.cardinales2()) {
            Coordonnees coordTemp2 = PionsAdverses.suivante(coord, d);
            if (PionsAdverses.estDansPlateau(coordTemp2, Coordonnees.NB_LIGNES)) {
                if (plateau[coordTemp2.ligne][coordTemp2.colonne].couleur != Case.CAR_VIDE && plateau[coordTemp2.ligne][coordTemp2.colonne].couleur != couleur) {
                    return true;
                }
            }

        }
        return false;
    }

    static boolean estSafe(Case[][] plateau, char couleur, Coordonnees coord, int tailleMax) {
        for (Direction d : Direction.cardinales1()) {
            Coordonnees coordTemp = PionsAdverses.suivante(coord, d);
            if (PionsAdverses.estDansPlateau(coordTemp, Coordonnees.NB_LIGNES)) {
                if (plateau[coordTemp.ligne][coordTemp.colonne].couleur != Case.CAR_VIDE && plateau[coordTemp.ligne][coordTemp.colonne].couleur != couleur) {
                    if (plateau[coordTemp.ligne][coordTemp.colonne].hauteur >= tailleMax) {
                        return false;
                    }
                }
            }

        }
        for (Direction d : Direction.cardinales2()) {
            Coordonnees coordTemp2 = PionsAdverses.suivante(coord, d);
            if (PionsAdverses.estDansPlateau(coordTemp2, Coordonnees.NB_LIGNES)) {
                if (plateau[coordTemp2.ligne][coordTemp2.colonne].couleur != Case.CAR_VIDE && plateau[coordTemp2.ligne][coordTemp2.colonne].couleur != couleur) {
                    if (plateau[coordTemp2.ligne][coordTemp2.colonne].hauteur >= tailleMax) {
                        return false;
                    }
                }
            }

        }
        boolean trouve;
        for (Direction d : Direction.cardinales1()) {
            trouve = false;
            Coordonnees coordTemp = new Coordonnees(coord.ligne, coord.colonne);
            while (PionsAdverses.estDansPlateau(coordTemp, Coordonnees.NB_LIGNES) && !trouve) {
                if (plateau[coordTemp.ligne][coordTemp.colonne].couleur != Case.CAR_VIDE) {
                    trouve = true;
                } else {
                    coordTemp = PionsAdverses.suivante(coordTemp, d);
                }
            }
            if (trouve) {
                if (plateau[coordTemp.ligne][coordTemp.colonne].couleur != couleur && plateau[coordTemp.ligne][coordTemp.colonne].hauteur >= tailleMax) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Troisème règle :
     *
     * @param plateau
     * @param couleur
     * @return
     */
    static String activerPourDegommerPlusDeN(Case[][] plateau, char couleur, String[] actionsPossibles, int degatsMin) {
        int coordI = 0;
        int coordJ = 0;
        int degats = 0;
        int degatsMax = 0;
        String action = null;
        for (int i = 0; i < plateau.length; i++) {
            for (int j = 0; j < plateau[i].length; j++) {
                if (plateau[i][j].couleur == couleur) {
                    degats = 0;
                    degats += PionsAdverses.estDansLigneEtColonne(new Coordonnees(i, j), couleur, plateau, 8);
                    degats += PionsAdverses.casesAdjacentesActivation(new Coordonnees(i, j), couleur, plateau, 8);
                    if (degats >= degatsMin && degats > degatsMax) {
                        degatsMax = degats;
                        coordI = i;
                        coordJ = j;
                    }
                }

            }
        }
        if (degatsMax > 0) {
            action = "A" + ligneVersLettre(coordI) + colonneVersLettre(coordJ);
        }
        if (action != null) {
            if (estUneActionPossible(action, actionsPossibles)) {
                return action;
            }
            return null;
        }

        return null;
    }

    /**
     * Deuxième règle :
     *
     * @param plateau
     * @param couleur
     * @return
     */
    static String augmenterHauteurDe1(Case[][] plateau, char couleur, String[] actionsPossibles) {
        int i = 0;
        int j = 0;
        int coordI = 0;
        int coordJ = 0;
        boolean tourTrouvee = false;
        String action = null;
        while (i < plateau.length && !tourTrouvee) {
            j = 0;
            while (j < plateau[i].length && !tourTrouvee) {
                // Si je suis une tour amie qui n'est pas encore à 4 de hauteur, alors il faut la monter (pour la protéger).
                if (plateau[i][j].couleur == couleur && plateau[i][j].hauteur < IATowa.HAUTEUR_MAX) {
                    tourTrouvee = true;
                    coordI = i;
                    coordJ = j;
                }
                j++;
            }
            i++;
        }
        if (tourTrouvee) {
            action = "P" + ligneVersLettre(coordI) + colonneVersLettre(coordJ);
        }
        if (action != null) {
            if (estUneActionPossible(action, actionsPossibles)) {
                return action;
            }
            return null;
        }

        return null;
    }

    /**
     * Permière règle :
     *
     * @param plateau
     * @param couleur
     * @return
     */
    static String activerPourDetruireTourH3(Case[][] plateau, char couleur, String[] actionsPossibles) {
        boolean tourAActiverTrouvee = false;
        Case[] toursAdverses;
        String action = null;
        int coordI = 0;
        int coordJ = 0;
        int degats = 0;
        int degatsMax = 0;
        for (int i = 0; i < plateau.length; i++) {
            for (int j = 0; j < plateau[i].length; j++) {
                degats = 0;
                tourAActiverTrouvee = false;
                // Si c'est une tour amie et qu'elle est de hauteur 4
                // (C'est intéressant de regarder si il n'y a pas une voisine / dans lignet colonne, qui est de hauteur 3 pour pouvoir la dégommer)
                if (plateau[i][j].couleur == couleur && plateau[i][j].hauteur == IATowa.HAUTEUR_MAX) {
                    toursAdverses = IATowa.adversaireDansLigneEtColonne(new Coordonnees(i, j), couleur, plateau, 8);
                    if (toursAdverses.length != 0) {
                        for (int l = 0; l < toursAdverses.length; l++) {
                            degats += toursAdverses[l].hauteur;
                            if (toursAdverses[l].hauteur == 3) {
                                tourAActiverTrouvee = true;
                            }
                        }
                    }
                    toursAdverses = IATowa.casesAdjacentesActivation(new Coordonnees(i, j), couleur, plateau, 8);
                    if (toursAdverses.length != 0) {
                        for (int l = 0; l < toursAdverses.length; l++) {
                            degats += toursAdverses[l].hauteur;
                            if (toursAdverses[l].hauteur == 3) {
                                tourAActiverTrouvee = true;
                            }
                        }
                    }
                    if (tourAActiverTrouvee && degats > degatsMax) {
                        degatsMax = degats;
                        coordI = i;
                        coordJ = j;
                    }
                }
            }
        }

        if (degatsMax > 0) {
            action = "A" + ligneVersLettre(coordI) + colonneVersLettre(coordJ);
        }
        if (action != null) {
            if (estUneActionPossible(action, actionsPossibles)) {
                return action;
            }
            return null;
        }

        return null;

    }

    /**
     *
     * @param coordLigne
     * @return
     */
    static String ligneVersLettre(int coordLigne) {
        String[] alpha = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p"};
        return alpha[coordLigne];
    }

    /**
     *
     * @param coordColonne
     * @return
     */
    static String colonneVersLettre(int coordColonne) {
        String[] alpha = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P"};
        return alpha[coordColonne];
    }

    /**
     *
     * @param action
     * @param actionsPossibles
     * @return
     */
    static boolean estUneActionPossible(String action, String[] actionsPossibles) {
        boolean actionTrouvee = false;
        if (action == null) {
            return false;
        }
        for (int i = 0; i < actionsPossibles.length && !actionTrouvee; i++) {
            if (action.equals(ActionsPossibles.enleverVitalites(actionsPossibles[i]))) {
                actionTrouvee = true;
            }
        }
        return actionTrouvee;
    }

    /**
     *
     * @param plateau
     * @param couleur
     * @param coord
     * @return le nombre de tour en emprise avec seulement une seule tour ami et
     * pas d'autre. Donc que l'adversaire n'est pas déjà en emprise avec moi.
     */
    static int nbToursEnEmprise(Case[][] plateau, char couleur, Coordonnees coord) {
        int nbToursEnEmprise = 0;
        Coordonnees[] ciblesAVerifier = Direction.pionsCibles(plateau, coord);
        for (int i = 0; i < ciblesAVerifier.length; i++) {
            Coordonnees coordTemp = ciblesAVerifier[i];
            if (plateau[coordTemp.ligne][coordTemp.colonne].couleur != couleur) {
                Coordonnees[] ciblesDeLEnnemi = Direction.pionsCibles(plateau, coordTemp);
                boolean coequipierTrouve = false;
                for (int j = 0; j < ciblesDeLEnnemi.length && !coequipierTrouve; j++) {
                    Coordonnees coordTemp2 = ciblesDeLEnnemi[j];
                    if (plateau[coordTemp2.ligne][coordTemp2.colonne].couleur == couleur) {
                        coequipierTrouve = true;
                    }
                }
                if (!coequipierTrouve) {
                    nbToursEnEmprise++;
                }
            }
        }

        return nbToursEnEmprise;
    }

    /**
     * Quatrième règle :
     *
     * @param plateau
     * @param couleur
     * @return
     */
    static String poseIntelligente(Case[][] plateau, char couleur, String[] actionsPossibles, boolean poseBonifiee) {
        int coordI = 0;
        int coordJ = 0;
        int nbMaxEmprise = 0;
        String action = null;
        for (int i = 0; i < plateau.length; i++) {
            for (int j = 0; j < plateau[i].length; j++) {
                if (plateau[i][j].couleur == Case.CAR_VIDE) {
                    if (poseBonifiee && poseEstBonifiee(plateau, couleur, new Coordonnees(i, j)) || !poseBonifiee && !poseEstBonifiee(plateau, couleur, new Coordonnees(i, j))) {
                        int tailleMax = 2;
                        if (poseBonifiee) {
                            tailleMax = 3;
                        }
                        if (estSafe(plateau, couleur, new Coordonnees(i, j), tailleMax)) {
                            int nbEmprise = nbToursEnEmprise(plateau, couleur, new Coordonnees(i, j));
                            if (nbEmprise > nbMaxEmprise) {
                                coordI = i;
                                coordJ = j;
                                nbMaxEmprise = nbEmprise;
                            }
                        }
                    }
                }
            }
        }

        if (nbMaxEmprise > 0) {
            action = "P" + ligneVersLettre(coordI) + colonneVersLettre(coordJ);
        }
        if (action != null) {
            if (estUneActionPossible(action, actionsPossibles)) {
                return action;
            }
            return null;
        }

        return null;
    }

}
