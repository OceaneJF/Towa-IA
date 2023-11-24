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
                    ajoutActionPose(coord, actions, nbPions, couleurJoueur, niveau, plateau);
                }
                // si l'activation d'une tour de cette couleur est possible sur cette case
                if (activationPossible(plateau, coord, couleurJoueur, niveau)) {
                    // on ajoute l'action dans les actions possibles
                    ajoutActionActivation(coord, actions, nbPions, couleurJoueur, plateau, niveau);
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
        if (niveau >= 2) {
            if ((plateau[coord.ligne][coord.colonne].couleur != couleur) && (plateau[coord.ligne][coord.colonne].couleur != Case.CAR_VIDE)) {
                estPossible = false;
            }
        }
        if (niveau >= 3) {
            if (plateau[coord.ligne][coord.colonne].hauteur >= 4) {
                estPossible = false;
            }
        }
        return estPossible;
    }

    /**
     * Indique s'il est possible d'activer une tour sur une case pour ce
     * plateau, ce joueur, dans ce niveau.
     *
     * @param plateau le plateau
     * @param coord coordonnées de la case à considérer
     * @param couleur couleur du joueur
     * @return vrai ssi l'activation d'une tour sur cette case est autorisée
     * dans ce niveau
     */
    boolean activationPossible(Case[][] plateau, Coordonnees coord, char couleur, int niveau) {
        boolean estPossible = true;
        if (niveau < 3) {
            estPossible = false;
        }
        if (niveau >= 3) {
            // Il faut qu'il y ait une tour de la couleur du joueur.
            if (!plateau[coord.ligne][coord.colonne].tourPresente()) {
                estPossible = false;
            } else if (plateau[coord.ligne][coord.colonne].couleur != couleur) {
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
                if (plateau[i][j].couleur == Case.CAR_NOIR) {
                    nbPionsNoirs += plateau[i][j].hauteur;
                }
                if (plateau[i][j].couleur == Case.CAR_BLANC) {
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
            NbPions nbPions, char couleur, int niveau, Case[][] plateau) {
        int pionsAAjouter = 0;
        if (niveau >= 0) {
            pionsAAjouter = 1;
        }
        if (niveau >= 5) {
            if (casesAdjacentes(coord, couleur, plateau, 1, niveau) >= 1) {
                pionsAAjouter = 2;
            }
        }
        int pionsNoirAAjouter = 0;
        int pionsBlancAAjouter = 0;
        if (couleur == Case.CAR_NOIR) {
            pionsNoirAAjouter = pionsAAjouter;
        }
        if (couleur == Case.CAR_BLANC) {
            pionsBlancAAjouter = pionsAAjouter;
        }
        String action = "P" + coord.carLigne() + coord.carColonne() + ","
                + (nbPions.nbPionsNoirs + pionsNoirAAjouter) + ","
                + (nbPions.nbPionsBlancs + pionsBlancAAjouter);
        actions.ajouterAction(action);
    }

    /**
     * Ajout d'une action d'activation dans l'ensemble des actions possibles.
     *
     * @param coord coordonnées de la case où se trouve la tour à activer
     * @param actions l'ensemble des actions possibles (en construction)
     * @param nbPions le nombre de pions par couleur sur le plateau avant de
     * jouer l'action
     * @param couleur la couleur de la tour à activer (le joueur actif)
     * @param plateau le plateau de jeu
     */
    void ajoutActionActivation(Coordonnees coord, ActionsPossibles actions,
            NbPions nbPions, char couleur, Case[][] plateau, int niveau) {
        int nbAdversairesAdjacent = casesAdjacentes(coord, couleur, plateau, 0, niveau);
        nbAdversairesAdjacent += estDansLigneEtColonne(coord, couleur, plateau, niveau);
        // Construction de l'action-meusure d'activation.
        int pionsNoirAEnlever = 0;
        int pionsBlancAEnlever = 0;
        if (couleur == Case.CAR_BLANC) {
            pionsNoirAEnlever = nbAdversairesAdjacent;
        }
        if (couleur == Case.CAR_NOIR) {
            pionsBlancAEnlever = nbAdversairesAdjacent;
        }
        String action = "A" + coord.carLigne() + coord.carColonne() + ","
                + (nbPions.nbPionsNoirs - pionsNoirAEnlever) + ","
                + (nbPions.nbPionsBlancs - pionsBlancAEnlever);
        actions.ajouterAction(action);
    }

    /**
     * Cette fonction permet de déterminer combien de pions adverses sont
     * adjacents au pion du joueur.
     *
     * @param coord coordonnées de la case où se trouve la tour à vérifier.
     * @param couleur la couleur de la tour à vérifier (le joueur actif)
     * @param plateau le plateau de jeu
     * @param appel permet d'appeler la fonction de plusieurs manières différentes.
     * @param niveau le niveau du jeu.
     * @return le nombre d'adversaires adjacents.
     */
    static int casesAdjacentes(Coordonnees coord, char couleur, Case[][] plateau, int appel, int niveau) {
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
                if (plateau[i][j].couleur != couleur && plateau[i][j].couleur != Case.CAR_VIDE) {
                    // Si la fonction est appelée depuis la fonction d'activation
                    if (appel == 0) {
                        if (niveau >= 6 &&
                                plateau[i][j] != plateau[coord.ligne][colonneMin] &&
                                plateau[i][j] != plateau[coord.ligne][colonneMax] &&
                                plateau[i][j] != plateau[ligneMin][coord.colonne] &&
                                plateau[i][j] != plateau[ligneMax][coord.colonne] &&
                                hauteurTour > plateau[i][j].hauteur){
                            nbAdversairesAdjacent += plateau[i][j].hauteur;
                        }
                        else if (niveau < 6 && hauteurTour > plateau[i][j].hauteur) {
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
    }

    /**
     * Cette fonction permet de déterminer combien de pions adverses sont sur la même ligne et la m^me colonne.
     * @param coord coordonnées de la case où se trouve la tour à vérifier.
     * @param couleur la couleur de la tour à vérifier (le joueur actif).
     * @param plateau le plateau de jeu
     * @param niveau le niveau du jeu.
     * @return le nombre d'adversaires présents sur la même ligne et sur la même colonne que le joueur actif.
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
