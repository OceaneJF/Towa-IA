/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package towa;

import static java.lang.Integer.parseInt;

/**
 *
 * @author cbardot
 */
public class Oceane {
    char couleur;
    
    Oceane(char uneCouleur){
        couleur = uneCouleur;
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


}
