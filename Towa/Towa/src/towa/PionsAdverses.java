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
     * Cette fonction renvoie la position suivante de la case à vérifier
     *
     * @param coord les coordonnées de la case d'avant
     * @param d la direnction à suivre
     * @return les coordonnées le la case suivante en suivant la direction d.
     */
    static Coordonnees positionSuivante(Coordonnees coord, Direction d) {
        Coordonnees coordS = new Coordonnees(coord.ligne, coord.colonne);
        switch (d) {
            case NORD:
                coordS.ligne -= 1;
                break;
            case SUD:
                coordS.ligne += 1;
                break;
            case EST:
                coordS.colonne += 1;
                break;
            case OUEST:
                coordS.colonne -= 1;
                break;
        }
        return coordS;
    }
    
    /**
     * Cette méthode indique la direction à suivre à partir d'un bord du plateau (qui se traduit par une autre direction)
     * @param d un des bords du plateau
     * @return la direction à suivre
     */
    static Direction DirectionASuivre(Direction d){
        switch (d) {
            case NORD:
                return Direction.SUD;
            case SUD:
                return Direction.NORD;
            case EST:
                return Direction.OUEST;
            default:
                return Direction.EST;
        }
    }
    
    /**
     * Cette méthode permet d'indiquer la direction pour parcourir toutes les lignes ou colonnes selon la diretion indiquée.
     * @param d la direction indiquée
     * @return la direction à parcourir
     */
    static Direction parcourirDirection(Direction d){
        switch (d) {
            case NORD:
            case SUD:
                return Direction.EST;
            case EST:
            default:
                return Direction.SUD;
        }
    }

    static Coordonnees initDepart(Direction d) {
        Coordonnees coord = new Coordonnees(0, 0);
        switch (d) {
            case SUD:
                coord.ligne = Coordonnees.NB_LIGNES;
                break;
            case OUEST:
                coord.colonne = Coordonnees.NB_COLONNES;
                break;
        }
        return coord;
    }
    
    static Coordonnees initEnFonctionDeLaDirection(Direction d, Coordonnees coord){
        switch(d){
            case NORD:
                coord.ligne = 0;
                break;
            case SUD:
                coord.ligne = Coordonnees.NB_LIGNES -1;
                break;
            case OUEST:
                coord.colonne = 0;
                break;
            case EST:
                coord.colonne = Coordonnees.NB_COLONNES -1;
                break;
        }
        return coord;
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
        for (Direction d : Direction.cardinales2()) {
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
     * Retourne le nombre d'amis adjacents (en comptant les pions qui forment
     * des tours.
     *
     * @param coord coordonnées de la case considérée
     * @param couleur la couleur du joueur actif
     * @param plateau le plateau de jeu
     * @param niveau le niveau du jeu
     * @return le nombre de pions amis adjacents.
     */
    static int casesAdjacentesFusion(Coordonnees coord, char couleur, Case[][] plateau, int niveau) {
        int nbAmisAdjacents = 0;
        for (Direction d : Direction.cardinales2()) {
            Coordonnees pionSuivant = suivante(coord, d);
            // Si il est dans le plateau
            if (estDansPlateau(pionSuivant, Coordonnees.NB_LIGNES)) {
                // Si c'est un pion ami
                if (plateau[pionSuivant.ligne][pionSuivant.colonne].couleur == couleur) {
                    nbAmisAdjacents += plateau[pionSuivant.ligne][pionSuivant.colonne].hauteur;
                }
            }
        }
        return nbAmisAdjacents;
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
    static int estDansLigneEtColonne(Coordonnees coord, char couleur, Case[][] plateau, int niveau) {
        int hauteurTour = plateau[coord.ligne][coord.colonne].hauteur;
        int nbAdversairesDansLigneColonne = 0;
        if (niveau == 6) {
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
        if (niveau >= 7) {
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
                        // Si la case est remplie par un pion adverse
                        if (plateau[coordS.ligne][coordS.colonne].couleur != couleur) {
                            // Et si la hauteur du pion adverse est inférieure à celle du pion activé
                            if (hauteurTour > plateau[coordS.ligne][coordS.colonne].hauteur) {
                                nbAdversairesDansLigneColonne += plateau[coordS.ligne][coordS.colonne].hauteur;
                            }
                        }
                    }
                    coordS.ligne = positionSuivante(coordS, d).ligne;
                    coordS.colonne = positionSuivante(coordS, d).colonne;
                }
            }
        }

        return nbAdversairesDansLigneColonne;
    }

    /**
     * Cette fonction permet de déterminer combien de pions amis sont sur la
     * même ligne et la même colonne.
     *
     * @param coord coordonnées de la case où se trouve la tour à vérifier.
     * @param couleur la couleur de la tour à vérifier (le joueur actif).
     * @param plateau le plateau de jeu
     * @param niveau le niveau du jeu.
     * @return le nombre d'adversaires présents sur la même ligne et sur la même
     * colonne que le joueur actif.
     */
    static int amisDansLigneEtColonne(Coordonnees coord, char couleur, Case[][] plateau, int niveau) {
        int nbAmisDansLigneColonne = 0;
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
                        nbAmisDansLigneColonne += plateau[coordS.ligne][coordS.colonne].hauteur;
                    }
                }
                coordS.ligne = positionSuivante(coordS, d).ligne;
                coordS.colonne = positionSuivante(coordS, d).colonne;
            }
        }
        return nbAmisDansLigneColonne;
    }
    
}
