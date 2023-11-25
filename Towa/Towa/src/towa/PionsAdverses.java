/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package towa;

/**
 *
 * @author cbardot
 */
public class PionsAdverses {

    /**
     * Renvoie les coordonnées de la case suivante, en suivant une direction
     * donnée.
     *
     * @param d la direction à suivre
     * @return les coordonnées de la case suivante
     */
    static Coordonnees suivante(Coordonnees c, Direction d) {
        return new Coordonnees(c.ligne + Direction.mvtVertic(d),
                c.colonne + Direction.mvtHoriz(d));
    }

    /**
     * Indique si ces coordonnées sont dans le plateau.
     *
     * @param coord coordonnées à tester
     * @param taille taille du plateau (carré)
     * @return vrai ssi ces coordonnées sont dans le plateau
     */
    static boolean estDansPlateau(Coordonnees coord, int taille) {
        return (coord.ligne >= 0 && coord.ligne < taille && coord.colonne >= 0 && coord.colonne < taille);
    }

    /**
     * Retourne le nombre d'adversaire à côté du joueur.
     *
     * @param coord coordonnées de la case considérée
     * @param couleur la couleur du joueur actif
     * @param plateau le plateau de jeu
     * @param niveau le niveau du jeu
     * @return le nombre de tours adverses.
     */
    static int casesAdjacentesPose(Coordonnees coord, char couleur, Case[][] plateau, int niveau) {
        int nbAdversairesAdjacents = 0;
        for (Direction d : Direction.values()) {
            Coordonnees pionSuivant = suivante(coord, d);
            // Si il est dans le plateau
            if (estDansPlateau(pionSuivant, Coordonnees.NB_LIGNES)) {
                // Si c'est un adversaire
                if (plateau[pionSuivant.ligne][pionSuivant.colonne].couleur != couleur && plateau[pionSuivant.ligne][pionSuivant.colonne].couleur != Case.CAR_VIDE) {
                    // Si la case pour poser est vide
                    if (plateau[coord.ligne][coord.colonne].couleur == Case.CAR_VIDE) {
                        nbAdversairesAdjacents++;
                    }
                }
            }
        }
        return nbAdversairesAdjacents;
    }

    /**
     * Retourne le nombre d'adversaires (en comptant le nombre de pions sur
     * chaque tours) à côté du joueur si la hauteur est supérieur à celle du
     * joueur.
     *
     * @param coord coordonnées de la case considérée
     * @param couleur la couleur du joueur actif
     * @param plateau le plateau de jeu
     * @param niveau le niveau du jeu
     * @return le nombre de pions adverses.
     */
    static int casesAdjacentesActivation(Coordonnees coord, char couleur, Case[][] plateau, int niveau) {
        int hauteurTour = plateau[coord.ligne][coord.colonne].hauteur;
        int nbAdversairesAdjacents = 0;
        for (Direction d : Direction.cardinales()) {
            Coordonnees pionSuivant = suivante(coord, d);
            // Si il est dans le plateau
            if (estDansPlateau(pionSuivant, Coordonnees.NB_LIGNES)) {
                // Si c'est un adversaire
                if (plateau[pionSuivant.ligne][pionSuivant.colonne].couleur != couleur && plateau[pionSuivant.ligne][pionSuivant.colonne].couleur != Case.CAR_VIDE) {
                    // Si la hauteur de la tour est supérieur à celle de la tour adverse
                    if (niveau >= 6 && hauteurTour > plateau[pionSuivant.ligne][pionSuivant.colonne].hauteur) {
                        nbAdversairesAdjacents += plateau[pionSuivant.ligne][pionSuivant.colonne].hauteur;
                    }
                }

            }
        }
        return nbAdversairesAdjacents;
    }

    /**
     * Cette fonction permet de déterminer combien de pions adverses sont
     * adjacents au pion du joueur.
     *
     * @param coord coordonnées de la case où se trouve la tour à vérifier.
     * @param couleur la couleur de la tour à vérifier (le joueur actif)
     * @param plateau le plateau de jeu
     * @param appel permet d'appeler la fonction de plusieurs manières
     * différentes.
     * @param niveau le niveau du jeu.
     * @return le nombre d'adversaires adjacents.
     */
    /*static int casesAdjacentes(Coordonnees coord, char couleur, Case[][] plateau, int appel, int niveau) {
        // Détermination des cases adjacentes à la tour activée.
        int hauteurTour = plateau[coord.ligne][coord.colonne].hauteur;
        int ligneMin = coord.ligne - 1;
        if (ligneMin < 0) {
            ligneMin = 0;
        }
        int ligneMax = coord.ligne + 1;
        if (ligneMax > Coordonnees.NB_LIGNES - 1) {
            ligneMax = Coordonnees.NB_LIGNES - 1;
        }
        int colonneMin = coord.colonne - 1;
        if (colonneMin < 0) {
            colonneMin = 0;
        }
        int colonneMax = coord.colonne + 1;
        if (colonneMax > Coordonnees.NB_COLONNES - 1) {
            colonneMax = Coordonnees.NB_COLONNES - 1;
        }
        // Calcul du nombre de pions enlevés.
        int nbAdversairesAdjacent = 0;
        for (int i = ligneMin; i <= ligneMax; i++) {
            for (int j = colonneMin; j <= colonneMax; j++) {
                // Si c'est un adversaire
                if (plateau[i][j].couleur != couleur && plateau[i][j].couleur != Case.CAR_VIDE) {
                    // Si la fonction est appelée depuis la fonction d'activation
                    if (appel == 0) {
                        if (niveau >= 6
                                && plateau[i][j] != plateau[coord.ligne][colonneMin]
                                && plateau[i][j] != plateau[coord.ligne][colonneMax]
                                && plateau[i][j] != plateau[ligneMin][coord.colonne]
                                && plateau[i][j] != plateau[ligneMax][coord.colonne]
                                && hauteurTour > plateau[i][j].hauteur) {
                            nbAdversairesAdjacent += plateau[i][j].hauteur;
                        } else if (niveau < 6 && hauteurTour > plateau[i][j].hauteur) {
                            nbAdversairesAdjacent += plateau[i][j].hauteur;
                        }
                    }
                    // Si la fonction est appelée depuis la fonction de pose d'un pion
                    if (appel == 1 && plateau[coord.ligne][coord.colonne].couleur == Case.CAR_VIDE) {
                        nbAdversairesAdjacent++;
                    }
                }
            }
        }
        return nbAdversairesAdjacent;
    }*/
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
    static int estDansLigneEtColonne(Coordonnees coord, char couleur, Case[][] plateau, int niveau) {
        int hauteurTour = plateau[coord.ligne][coord.colonne].hauteur;
        int nbAdversairesDansLigneColonne = 0;
        if (niveau >= 6) {
            // On parcours la ligne où se trouve le pion à activer
            for (int j = 0; j < plateau[coord.ligne].length; j++) {
                //On test si le pion est un adversaire
                if (plateau[coord.ligne][j].couleur != couleur && plateau[coord.ligne][j].couleur != Case.CAR_VIDE) {
                    // On test si la hauteur de l'adversaire est plus petite que la tour du joueur.
                    if (hauteurTour > plateau[coord.ligne][j].hauteur) {
                        nbAdversairesDansLigneColonne += plateau[coord.ligne][j].hauteur;
                    }
                }
            }
            for (int i = 0; i < plateau.length; i++) {
                //On test si le pion est un adversaire
                if (plateau[i][coord.colonne].couleur != couleur && plateau[i][coord.colonne].couleur != Case.CAR_VIDE) {
                    // On test si la hauteur de l'adversaire est plus petite que la tour du joueur.
                    if (hauteurTour > plateau[i][coord.colonne].hauteur) {
                        nbAdversairesDansLigneColonne += plateau[i][coord.colonne].hauteur;
                    }
                }
            }
        }

        return nbAdversairesDansLigneColonne;
    }

}
