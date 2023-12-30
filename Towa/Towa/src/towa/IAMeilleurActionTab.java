/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package towa;

import static towa.IATowa.SEPARATEUR;

/**
 *
 * @author cbardot
 */
public class IAMeilleurActionTab {
    
    /**
     * Cette fonction renvoie l'action qui a la plus grande différence de pions en fonction de la couleur
     * @param actionsPossibles le tableau de toutes les actions possibles
     * @return la meilleure action
     */
    static String meilleurActionDansTab(String[] actionsPossibles, char couleur) {
        int nbPionsNoirs;
        int nbPionsBlancs;
        int meilleurCombot = 0;
        for (int i = 0; i < actionsPossibles.length; i++) {
            nbPionsNoirs = nbPionsNoirs(actionsPossibles[i]);
            nbPionsBlancs = nbPionsBlancs(actionsPossibles[i]);
            if (couleur == Case.CAR_NOIR) {
                if (nbPionsNoirs - nbPionsBlancs > nbPionsNoirs(actionsPossibles[meilleurCombot]) - nbPionsBlancs(actionsPossibles[meilleurCombot])) {
                    meilleurCombot = i;
                }
            }
            else{
                if (nbPionsBlancs - nbPionsNoirs > nbPionsBlancs(actionsPossibles[meilleurCombot]) - nbPionsNoirs(actionsPossibles[meilleurCombot])) {
                    meilleurCombot = i;
                }
            }
        }
        return ActionsPossibles.enleverVitalites(actionsPossibles[meilleurCombot]);
    }

    static int nbPionsNoirs(String action) {
        int indexSeparateur = action.indexOf(SEPARATEUR);
        int indexDeuxiemeSep = action.indexOf(SEPARATEUR, indexSeparateur + 1);
        return Integer.parseInt(action.substring(indexSeparateur+1, indexDeuxiemeSep));
    }
    
    static int nbPionsBlancs(String action) {
        int indexSeparateur = action.indexOf(SEPARATEUR);
        int indexDeuxiemeSep = action.indexOf(SEPARATEUR, indexSeparateur + 1);
        return Integer.parseInt(action.substring(indexDeuxiemeSep+1));
    }
}
