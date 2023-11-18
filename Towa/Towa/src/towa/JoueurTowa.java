package towa;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Joueur implémentant les actions possibles à partir d'un plateau, pour un
 * niveau donné.
 */
public class JoueurTowa implements IJoueurTowa {

    /**
     * Cette méthode renvoie, pour un plateau donné et un joueur donné, toutes
     * les actions possibles pour ce joueur.
     *
     * @param plateau le plateau considéré
     * @param couleurJoueur couleur du joueur
     * @param niveau le niveau de la partie à jouer
     * @return l'ensemble des actions possibles
     */
    @Override
    public String[] actionsPossibles(Case[][] plateau, char couleurJoueur, int niveau) {
        // afficher l'heure de lancement
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSS");
        System.out.println("actionsPossibles : lancement le " + format.format(new Date()));
        // se préparer à stocker les actions possibles
        ActionsPossibles actions = new ActionsPossibles();
        // on compte le nombre de pions sur le plateau avant action
        NbPions nbPions = nbPions(plateau);
        // pour chaque ligne
        for (int lig = 0; lig < Coordonnees.NB_LIGNES; lig++) {
            // pour chaque colonne
            for (int col = 0; col < Coordonnees.NB_COLONNES; col++) {
                Coordonnees coord = new Coordonnees(lig, col);
                // si la pose d'un pion de cette couleur est possible sur cette case
                if (posePossible(plateau, coord, couleurJoueur, niveau)) {
                    // on ajoute l'action dans les actions possibles
                    ajoutActionPose(coord, actions, nbPions, couleurJoueur);
                }
            }
        }
        System.out.println("actionsPossibles : fin");
        return actions.nettoyer();
    }

    /**
     * Indique s'il est possible de poser un pion sur une case pour ce plateau,
     * ce joueur, dans ce niveau.
     *
     * @param plateau le plateau
     * @param coord coordonnées de la case à considérer
     * @param couleur couleur du joueur
     * @return vrai ssi la pose d'un pion sur cette case est autorisée dans ce
     * niveau
     */
    boolean posePossible(Case[][] plateau, Coordonnees coord, char couleur, int niveau) {
        boolean estPossible = true;
        if (niveau >= 1) {
            if (coord.ligne < 0) {
                estPossible = false;
            } else if (coord.ligne >= Coordonnees.NB_LIGNES) {
                estPossible = false;
            } else if (coord.colonne < 0) {
                estPossible = false;
            } else if (coord.colonne >= Coordonnees.NB_COLONNES) {
                estPossible = false;
            }
        }
        if(niveau >= 2){
            if((plateau[coord.ligne][coord.colonne].couleur != couleur) && (plateau[coord.ligne][coord.colonne].couleur != Case.CAR_VIDE)){
                estPossible = false;
            }
        }
        return estPossible;
    }

    /**
     * Nombre de pions sur le plateau, de chaque couleur.
     *
     * @param plateau le plateau
     * @return le nombre de pions sur le plateau, de chaque couleur
     */
    static NbPions nbPions(Case[][] plateau) {
        int nbPionsNoirs = 0;
        int nbPionsBlancs = 0;
        for (int i = 0; i < plateau.length; i++) {
            for (int j = 0; j < plateau[i].length; j++) {
                if(plateau[i][j].couleur == Case.CAR_NOIR){
                    nbPionsNoirs += plateau[i][j].hauteur;
                }
                if(plateau[i][j].couleur == Case.CAR_BLANC){
                    nbPionsBlancs += plateau[i][j].hauteur;
                }
            }
        }
        return new NbPions(nbPionsNoirs, nbPionsBlancs);
    }

    /**
     * Ajout d'une action de pose dans l'ensemble des actions possibles.
     *
     * @param coord coordonnées de la case où poser un pion
     * @param actions l'ensemble des actions possibles (en construction)
     * @param nbPions le nombre de pions par couleur sur le plateau avant de
     * jouer l'action
     * @param couleur la couleur du pion à ajouter
     */
    void ajoutActionPose(Coordonnees coord, ActionsPossibles actions,
            NbPions nbPions, char couleur) {
        int pionsNoirAAjouter = 0;
        int pionsBlancAAjouter = 0;
        if(couleur == Case.CAR_NOIR){
            pionsNoirAAjouter = 1;
        }
        if(couleur == Case.CAR_BLANC){
            pionsBlancAAjouter = 1;
        }
        String action = "P" + coord.carLigne() + coord.carColonne() + ","
                + (nbPions.nbPionsNoirs + pionsNoirAAjouter) + ","
                + (nbPions.nbPionsBlancs+ pionsBlancAAjouter);
        actions.ajouterAction(action);
    }
}
