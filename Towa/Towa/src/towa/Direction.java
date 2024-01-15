/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package towa;

/**
 *
 * @author cbardot
 */
enum Direction {
    
    NORD,
    SUD,
    EST,
    OUEST,
    NORD_EST,
    NORD_OUEST,
    SUD_EST,
    SUD_OUEST;

    
    /**
     * Renvoie les quatre directions cardinales dans un tableau.
     *
     * @return tableau contenant les quatre directions cardinales.
     */
    static Direction[] cardinales1() {
        Direction[] directions = {NORD, SUD, EST, OUEST};
        return directions;
    }
    
    /**
     * Renvoie les quatre directions cardinales dans un tableau.
     *
     * @return tableau contenant les quatre directions cardinales.
     */
    static Direction[] cardinales2() {
        Direction[] directions = {NORD_EST,NORD_OUEST,SUD_EST,SUD_OUEST};
        return directions;
    }

    /**
     * Renvoie le nombre de cases parcourues horizontalement lorsqu'on suit
     * cette direction (0 pour Nord et Sud, -1 pour Ouest, 1 pour Est).
     *
     * @param dir la direction à considérer
     * @return nombre de cases horizontales de cette direction
     */
    static int mvtHoriz(Direction dir) {
        int dh = -2;
        switch (dir) {
            case NORD:
            case SUD:
                dh = 0;
                break;
            case EST:
            case NORD_EST:
            case SUD_EST:
                dh = 1;
                break;
            case OUEST:
            case NORD_OUEST:
            case SUD_OUEST:
                dh = -1;
                break;
        }
        return dh;
    }

    /**
     * Renvoie le nombre de cases parcourues verticalement lorsqu'on suit cette
     * direction (0 pour Est et Ouest, -1 pour Nord, 1 pour Sud).
     *
     * @param dir la direction d'origine
     * @return nombre de cases verticales de cette direction
     */
    static int mvtVertic(Direction dir) {
        int dv = -2;
        switch (dir) {
            case EST:
            case OUEST:
                dv = 0;
                break;
            case NORD:
            case NORD_EST:
            case NORD_OUEST:
                dv = -1;
                break;
            case SUD:
            case SUD_EST:
            case SUD_OUEST:
                dv = 1;
                break;
        }
        return dv;
    }
    
    
    static Coordonnees[] pionsAdjacents(Case[][] plateau, Coordonnees coord){
        Coordonnees[] casesTrouvees = new Coordonnees[8];
        int nbCases = 0;
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                if(i != 0 || j != 0){
                    Coordonnees coordTemp = new Coordonnees(coord.ligne + i, coord.colonne + j);
                    if(PionsAdverses.estDansPlateau(coordTemp, Coordonnees.NB_COLONNES)){
                        if(plateau[coordTemp.ligne][coordTemp.colonne].couleur != Case.CAR_VIDE){
                            casesTrouvees[nbCases] = new Coordonnees(coordTemp.ligne, coordTemp.colonne);
                            nbCases ++;
                        }
                    }
                }
            }
        }
        Coordonnees[] tabFinal = new Coordonnees[nbCases];
        for (int i = 0; i < nbCases; i++) {
            tabFinal[i] = casesTrouvees[i];
        }
        return tabFinal;
    }
    
    static Coordonnees[] pionsCibles(Case[][] plateau, Coordonnees coord){
        Coordonnees[] pionsTrouves = new Coordonnees[8];
        int nbPions = 0;
        for(Direction d : cardinales1()){
            boolean trouve = false;
            Coordonnees coordTemp = new Coordonnees(coord.ligne, coord.colonne);
            coordTemp = PionsAdverses.suivante(coordTemp, d);
            while(PionsAdverses.estDansPlateau(coordTemp, Coordonnees.NB_COLONNES) && !trouve){
                if(plateau[coordTemp.ligne][coordTemp.colonne].couleur != Case.CAR_VIDE){
                    trouve = true;
                    pionsTrouves[nbPions] = new Coordonnees(coordTemp.ligne, coordTemp.colonne);
                    nbPions ++;
                }
                coordTemp = PionsAdverses.suivante(coordTemp, d);
            }
        }
        Coordonnees[] casesAdjacentes = pionsAdjacents(plateau, coord);
        for (int i = 0; i < casesAdjacentes.length; i++) {
            boolean trouve = false;
            for (int j = 0; j < nbPions && !trouve; j++) {
                if(casesAdjacentes[i].ligne == pionsTrouves[j].ligne && casesAdjacentes[i].colonne == pionsTrouves[j].colonne){
                    trouve = true;
                }
            }
            if(!trouve){
                pionsTrouves[nbPions] = casesAdjacentes[i];
                nbPions ++;
            }
        }
        
        Coordonnees[] tabFinal = new Coordonnees[nbPions];
        for (int i = 0; i < nbPions; i++) {
            tabFinal[i] = pionsTrouves[i];
        }
        return tabFinal;
    }
}
