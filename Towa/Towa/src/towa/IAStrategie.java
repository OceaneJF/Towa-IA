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
    // 3- Si on peut détruire 4 pions ou plus (en une activation), les détruires
    // 4- Si on peut poser un pion "safe" à côté d'un adversaire, le faire (= commencer une nouvelle tour)
    // 5- Sinon chercher un endroit "safe" pour commencer une nouvelle tour

    // "safe" = tous les adversaires qui peuvent s'activer contre nous sont au max de même hauteur après pose.
    /**
     *
     * @param plateau
     * @param couleur
     * @param nbTourJeu
     * @return
     */
    static String principale(Case[][] plateau, char couleur, int nbTourJeu) {
        // Première règle
        String actionTemp = activerPourDetruireTourH3(plateau, couleur);
        if (actionTemp != null) {
            return actionTemp;
        }
        //Deuxième règle
        actionTemp = augmenterHauteurDe1(plateau, couleur);
        if (actionTemp != null) {
            return actionTemp;
        }
        // Troisième règle
        actionTemp = activerPourDegommerPlusDe4(plateau, couleur);
        if (actionTemp != null) {
            return actionTemp;
        }
        // Quatrième règle
        actionTemp = posePionSafeACoteAdversaire(plateau, couleur);
        if (actionTemp != null) {
            return actionTemp;
        }
        // Cinquième règle
        actionTemp = posePionSafe(plateau, couleur);
        if(actionTemp != null){
            return actionTemp;
        }
        // Par défault : leilleur action dans le tableau des actions possibles
        // on instancie votre implémentation
        JoueurTowa joueurTowa = new JoueurTowa();
        // choisir aléatoirement une action possible
        String[] actionsPossibles = ActionsPossibles.nettoyerTableau(
                joueurTowa.actionsPossibles(plateau, couleur, 8));
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
    static String posePionSafe(Case[][] plateau, char couleur) {
        int i = 0;
        int j = 0;
        int coordI = 0;
        int coordJ = 0;
        boolean tourTrouvee = false;
        String action = null;
        while (i < plateau.length && !tourTrouvee) {
            j=0;
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
        return action;
    }

    /**
     * Quatrième règle :
     *
     * @param plateau
     * @param couleur
     * @return
     */
    static String posePionSafeACoteAdversaire(Case[][] plateau, char couleur) {
        int i = 0;
        int j = 0;
        int coordI = 0;
        int coordJ = 0;
        boolean tourTrouvee = false;
        String action = null;
        while (i < plateau.length && !tourTrouvee) {
            j=0;
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
        return action;
    }

    static boolean poseEstBonifiee(Case[][] plateau, char couleur, Coordonnees coord) {
        for (Direction d : Direction.cardinales1()) {
            Coordonnees coordTemp = PionsAdverses.suivante(coord, d);
            if(PionsAdverses.estDansPlateau(coordTemp, Coordonnees.NB_LIGNES)){
                if (plateau[coordTemp.ligne][coordTemp.colonne].couleur != Case.CAR_VIDE && plateau[coordTemp.ligne][coordTemp.colonne].couleur != couleur) {
                    return true;
                }
            }
            
        }
        for (Direction d : Direction.cardinales2()) {
            Coordonnees coordTemp2 = PionsAdverses.suivante(coord, d);
            if(PionsAdverses.estDansPlateau(coordTemp2, Coordonnees.NB_LIGNES)){
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
            if(PionsAdverses.estDansPlateau(coordTemp, Coordonnees.NB_LIGNES)){
                if (plateau[coordTemp.ligne][coordTemp.colonne].couleur != Case.CAR_VIDE && plateau[coordTemp.ligne][coordTemp.colonne].couleur != couleur) {
                    if (plateau[coordTemp.ligne][coordTemp.colonne].hauteur >= tailleMax) {
                        return false;
                    }
                }
            }
            
        }
        for (Direction d : Direction.cardinales2()) {
            Coordonnees coordTemp2 = PionsAdverses.suivante(coord, d);
            if(PionsAdverses.estDansPlateau(coordTemp2, Coordonnees.NB_LIGNES)){
                if (plateau[coordTemp2.ligne][coordTemp2.colonne].couleur != Case.CAR_VIDE && plateau[coordTemp2.ligne][coordTemp2.colonne].couleur != couleur) {
                    if (plateau[coordTemp2.ligne][coordTemp2.colonne].hauteur >= tailleMax) {
                        return false;
                    }
                }
            }
            
        }
        boolean trouve = false;
        for (Direction d : Direction.cardinales1()) {
            Coordonnees coordTemp = new Coordonnees(coord.ligne, coord.colonne);
            while (PionsAdverses.estDansPlateau(coordTemp, Coordonnees.NB_LIGNES) && !trouve) {
                if (plateau[coordTemp.ligne][coordTemp.colonne].couleur == Case.CAR_VIDE) {
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
    static String activerPourDegommerPlusDe4(Case[][] plateau, char couleur) {
        int i = 0;
        int j = 0;
        int coordI = 0;
        int coordJ = 0;
        boolean tourTrouvee = false;
        String action = null;
        int nbAdversaires;
        while (i < plateau.length && !tourTrouvee) {
            j=0;
            while (j < plateau[i].length && !tourTrouvee) {
                nbAdversaires = 0;
                nbAdversaires += PionsAdverses.estDansLigneEtColonne(new Coordonnees(i, j), couleur, plateau, 8);
                nbAdversaires += PionsAdverses.casesAdjacentesActivation(new Coordonnees(i, j), couleur, plateau, 8);
                if (nbAdversaires >= 4) {
                    tourTrouvee = true;
                    coordI = i;
                    coordJ = j;
                }
                j++;
            }
            i++;
        }
        if (tourTrouvee) {
            action = "A" + ligneVersLettre(coordI) + colonneVersLettre(coordJ);
        }
        return action;
    }

    /**
     * Deuxième règle :
     *
     * @param plateau
     * @param couleur
     * @return
     */
    static String augmenterHauteurDe1(Case[][] plateau, char couleur) {
        int i = 0;
        int j = 0;
        int coordI = 0;
        int coordJ = 0;
        boolean tourTrouvee = false;
        String action = null;
        while (i < plateau.length && !tourTrouvee) {
            j=0;
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
        return action;
    }

    /**
     * Permière règle :
     *
     * @param plateau
     * @param couleur
     * @return
     */
    static String activerPourDetruireTourH3(Case[][] plateau, char couleur) {
        int i = 0;
        int j = 0;
        int l;
        boolean tourAActiverTrouvee = false;
        Case[] toursAdverses;
        String action = null;
        int coordI = 0;
        int coordJ = 0;
        while (i < plateau.length && !tourAActiverTrouvee) {
            j=0;
            while (j < plateau[i].length && !tourAActiverTrouvee) {
                // Si c'est une tour amie et qu'elle est de hauteur 4
                // (C'est intéressant de regarder si il n'y a pas une voisine / dans lignet colonne, qui est de hauteur 3 pour pouvoir la dégommer)
                if (plateau[i][j].couleur == couleur && plateau[i][j].hauteur == IATowa.HAUTEUR_MAX) {
                    toursAdverses = IATowa.adversaireDansLigneEtColonne(new Coordonnees(i, j), couleur, plateau, 8);
                    if (toursAdverses.length != 0) {
                        l = 0;
                        while (l < toursAdverses.length && toursAdverses[l] != null && !tourAActiverTrouvee) {
                            if (toursAdverses[l].hauteur == 3) {
                                tourAActiverTrouvee = true;
                                coordI = i;
                                coordJ = j;
                            }
                            l++;
                        }
                    }
                    toursAdverses = IATowa.casesAdjacentesActivation(new Coordonnees(i, j), couleur, plateau, 8);
                    if (toursAdverses.length != 0) {
                        l = 0;
                        while (l < toursAdverses.length && toursAdverses[l] != null && !tourAActiverTrouvee) {
                            if (toursAdverses[l].hauteur == 3) {
                                tourAActiverTrouvee = true;
                                coordI = i;
                                coordJ = j;
                            }
                            l++;
                        }
                    }
                }
                j++;
            }
            i++;
        }
        if (tourAActiverTrouvee) {
            action = "A" + ligneVersLettre(coordI) + colonneVersLettre(coordJ);
        }
        return action;
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

}
