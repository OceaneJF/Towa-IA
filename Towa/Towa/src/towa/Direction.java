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
    
//    NORD,
//    SUD,
//    EST,
//    OUEST,
    NORD_EST,
    NORD_OUEST,
    SUD_EST,
    SUD_OUEST;

    /**
     * Renvoie les quatre directions cardinales dans un tableau.
     *
     * @return tableau contenant les quatre directions cardinales.
     */
    static Direction[] cardinales() {
        Direction[] directions = {NORD_EST, SUD_EST, NORD_OUEST, SUD_OUEST};
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
            case NORD_EST:
            case SUD_EST:
                dh = 1;
                break;
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
            case NORD_EST:
            case NORD_OUEST:
                dv = -1;
                break;
            case SUD_EST:
            case SUD_OUEST:
                dv = 1;
                break;
        }
        return dv;
    }
}
