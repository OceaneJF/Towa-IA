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
                // si l'action de fusion d'une tour de cette couleur est possible sur cette case
                if (fusionPossible(plateau, coord, couleurJoueur, niveau)) {
                    // on ajoute l'action dans les actions possibles
                    ajoutActionFusion(coord, actions, nbPions, couleurJoueur, plateau, niveau);
                }
                // si l'action de magie d'une tour de cette couleur est possible sur cette case
                if (magiePossible(plateau, coord, couleurJoueur, niveau)) {
                    // on ajoute l'action dans les actions possibles
                    ajoutActionMagie(coord, actions, nbPions);
                }
            }
        }
        for (Direction d : Direction.cardinales1()) {
            ajoutActionChatonsKamikazes(d, actions, nbPions, plateau, niveau);
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
     * @param niveau le niveau du jeu
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
        if (niveau >= 10) {
            if (plateau[coord.ligne][coord.colonne].altitude + plateau[coord.ligne][coord.colonne].hauteur >= 4) {
                estPossible = false;
            }
            // Si il y a des adversaires à côté et que le niveau de la tour est >= à 3 alors la pose n'est pas possible.
            if (PionsAdverses.casesAdjacentesPose(coord, couleur, plateau, niveau) >= 1 && plateau[coord.ligne][coord.colonne].altitude + plateau[coord.ligne][coord.colonne].hauteur >= 3) {
                estPossible = false;
            }
        }
        if (niveau >= 12) {
            if (plateau[coord.ligne][coord.colonne].nature != Case.CAR_TERRE) {
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
     * @param niveau le niveau du jeu
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
     * Indique s'il est possible d'actionner la fusion d'une une tour sur une
     * case pour ce plateau, ce joueur, dans ce niveau.
     *
     * @param plateau le plateau
     * @param coord coordonnées de la case à considérer
     * @param couleur couleur du joueur
     * @param niveau le niveau du jeu
     * @return vrai ssi la fusion d'une tour sur cette case est autorisée dans
     * ce niveau
     */
    boolean fusionPossible(Case[][] plateau, Coordonnees coord, char couleur, int niveau) {
        boolean estPossible = true;
        if (niveau < 8) {
            estPossible = false;
        }
        if (niveau >= 8) {
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
     * Indique s'il est possible d'actionner la magie d'une une tour sur une
     * case pour ce plateau, ce joueur, dans ce niveau.
     *
     * @param plateau le plateau
     * @param coord coordonnées de la case à considérer
     * @param couleur la couleur du joueur
     * @param niveau le niveau du jeu
     * @return vrai ssi la magie d'une tour sur cette case est autorisée dans ce
     * niveau
     */
    boolean magiePossible(Case[][] plateau, Coordonnees coord, char couleur, int niveau) {
        boolean estPossible = true;
        if (niveau < 11) {
            estPossible = false;
        }
        if (niveau >= 11) {
            // Il faut qu'il y ait une tour de la couleur du joueur.
            if (!plateau[coord.ligne][coord.colonne].tourPresente()) {
                estPossible = false;
            } else if (plateau[coord.ligne][coord.colonne].couleur != couleur) {
                estPossible = false;
            }
            // Si il n'y a pas de tour dans la case symétrique
            if (plateau[(Coordonnees.NB_LIGNES - 1) - coord.ligne][(Coordonnees.NB_COLONNES - 1) - coord.colonne].tourPresente()) {
                estPossible = false;
            }
            // Vérification du niveau
            if (plateau[(Coordonnees.NB_LIGNES - 1) - coord.ligne][(Coordonnees.NB_COLONNES - 1) - coord.colonne].altitude + plateau[coord.ligne][coord.colonne].hauteur > 4) {
                estPossible = false;
            }
            if (plateau[(Coordonnees.NB_LIGNES - 1) - coord.ligne][(Coordonnees.NB_COLONNES - 1) - coord.colonne].nature != Case.CAR_TERRE) {
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
     * @param niveau le niveau du jeu
     * @param plateau le plateau du jeu
     */
    void ajoutActionPose(Coordonnees coord, ActionsPossibles actions,
            NbPions nbPions, char couleur, int niveau, Case[][] plateau) {
        int pionsAAjouter = 0;
        if (niveau >= 0) {
            pionsAAjouter = 1;
        }
        if (niveau >= 5) {
            if (PionsAdverses.casesAdjacentesPose(coord, couleur, plateau, niveau) >= 1) {
                pionsAAjouter = 2;
            }
        }
        if (niveau >= 13) {
            if (plateauCouvert(plateau, coord, couleur)) {
                pionsAAjouter = 4 - plateau[coord.ligne][coord.colonne].altitude;
            }
        }
        int[] pionsASuppr = {0, 0};
        if (niveau >= 14) {
            if(plateau[coord.ligne][coord.colonne].couleur == Case.CAR_VIDE){
                // si l'action de révolte des poneys sur une tour de cette couleur est possible sur cette case
            pionsASuppr = poneysPossible(plateau, coord, couleur, niveau);
            }
            
        }
        int pionsNoirAAjouter = 0;
        int pionsBlancAAjouter = 0;
        if (couleur == Case.CAR_NOIR) {
            pionsNoirAAjouter = pionsAAjouter;
            if (pionsASuppr[0] != 0) {
                pionsNoirAAjouter -= (pionsASuppr[0] + pionsAAjouter);
                pionsBlancAAjouter -= pionsASuppr[1];
            }
        }
        if (couleur == Case.CAR_BLANC) {
            pionsBlancAAjouter = pionsAAjouter;
            if (pionsASuppr[1] != 0) {
                pionsNoirAAjouter -= pionsASuppr[0];
                pionsBlancAAjouter -= (pionsASuppr[1] + pionsAAjouter);
            }
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
     * @param niveau le niveau du jeu
     */
    void ajoutActionActivation(Coordonnees coord, ActionsPossibles actions,
            NbPions nbPions, char couleur, Case[][] plateau, int niveau) {
        int nbAdversairesAdjacent = PionsAdverses.casesAdjacentesActivation(coord, couleur, plateau, niveau);
        nbAdversairesAdjacent += PionsAdverses.estDansLigneEtColonne(coord, couleur, plateau, niveau);
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
     * Ajout d'une action de fusion dans l'ensemble des actions possibles.
     *
     * @param coord coordonnées de la case où se trouve la tour à fusionner
     * @param actions l'ensemble des actions possibles (en construction)
     * @param nbPions le nombre de pions par couleur sur le plateau avant de
     * jouer l'action
     * @param couleur la couleur de la tour à fusionner (le joueur actif)
     * @param plateau le plateau de jeu
     * @param niveau le niveau du jeu
     */
    void ajoutActionFusion(Coordonnees coord, ActionsPossibles actions,
            NbPions nbPions, char couleur, Case[][] plateau, int niveau) {
        int hauteurTour = plateau[coord.ligne][coord.colonne].hauteur;
        int niveauTour = plateau[coord.ligne][coord.colonne].altitude + hauteurTour;
        int nbAmisAdjacent = PionsAdverses.casesAdjacentesFusion(coord, couleur, plateau, niveau);
        nbAmisAdjacent += PionsAdverses.amisDansLigneEtColonne(coord, couleur, plateau, niveau);
        int nbPionsPerdus = 0;
        if (nbAmisAdjacent + niveauTour > 4) {
            nbPionsPerdus = nbAmisAdjacent + niveauTour - 4;
        }
        // Construction de l'action-meusure de fusion.
        int pionsNoirAEnlever = 0;
        int pionsBlancAEnlever = 0;
        if (couleur == Case.CAR_NOIR) {
            pionsNoirAEnlever = nbPionsPerdus;
        }
        if (couleur == Case.CAR_BLANC) {
            pionsBlancAEnlever = nbPionsPerdus;
        }
        String action = "F" + coord.carLigne() + coord.carColonne() + ","
                + (nbPions.nbPionsNoirs - pionsNoirAEnlever) + ","
                + (nbPions.nbPionsBlancs - pionsBlancAEnlever);
        actions.ajouterAction(action);
    }

    /**
     * Ajout d'une action de chatons kamikazes dans l'ensemble des actions
     * possibles.
     *
     * @param d la direction de départ des chatons kamikazes
     * @param actions l'ensemble des actions possibles (en construction)
     * @param nbPions e nombre de pions par couleur sur le plateau avant de
     * jouer l'action
     * @param plateau le plateau de jeu
     * @param niveau le niveau du jeu
     */
    void ajoutActionChatonsKamikazes(Direction d, ActionsPossibles actions,
            NbPions nbPions, Case[][] plateau, int niveau) {
        int pionsNoirAEnlever = 0;
        int pionsBlancAEnlever = 0;
        int j;
        Direction dASuivre = PionsAdverses.DirectionASuivre(d);
        Direction dAParcourir = PionsAdverses.parcourirDirection(d);
        Coordonnees coord = PionsAdverses.initDepart(d);
        boolean estUnPion;
        for (int i = 0; i < plateau.length; i++) {
            estUnPion = false;
            j = 0;
            coord = PionsAdverses.initEnCours(d, coord);
            while (!estUnPion && j < plateau.length) {
                if (plateau[coord.ligne][coord.colonne].couleur != Case.CAR_VIDE) {
                    estUnPion = true;
                    if (plateau[coord.ligne][coord.colonne].couleur == Case.CAR_BLANC) {
                        pionsBlancAEnlever += plateau[coord.ligne][coord.colonne].hauteur;
                    }
                    if (plateau[coord.ligne][coord.colonne].couleur == Case.CAR_NOIR) {
                        pionsNoirAEnlever += plateau[coord.ligne][coord.colonne].hauteur;
                    }
                }
                j++;
                coord = PionsAdverses.positionSuivante(coord, dASuivre);
            }
            coord = PionsAdverses.positionSuivante(coord, dAParcourir);
        }
        String action = "C" + d.premiereLettre() + ","
                + (nbPions.nbPionsNoirs - pionsNoirAEnlever) + ","
                + (nbPions.nbPionsBlancs - pionsBlancAEnlever);
        actions.ajouterAction(action);
    }

    /**
     * Cette fonction permet de retourner les coordonnées tels quels car la
     * magie de fait que changer le pion de place et ne modifie pas son nombre
     * de pions.
     *
     * @param coord coordonnées de la case où se trouve la tour à fusionner
     * @param actions l'ensemble des actions possibles (en construction)
     * @param nbPions le nombre de pions par couleur sur le plateau avant de
     * jouer l'action
     */
    void ajoutActionMagie(Coordonnees coord, ActionsPossibles actions,
            NbPions nbPions) {
        String action = "M" + coord.carLigne() + coord.carColonne() + ","
                + (nbPions.nbPionsNoirs) + ","
                + (nbPions.nbPionsBlancs);
        actions.ajouterAction(action);
    }

    /**
     * Cette fonction retourne vrai si le plateau est couvert, faux sinon
     *
     * @param plateau le plateau de jeu
     * @param coord les coordonnées du pion qui est jouée
     * @param couleur la couleur du joueur
     * @return vrai si le plateau est couvert
     */
    boolean plateauCouvert(Case[][] plateau, Coordonnees coord, char couleur) {
        int j = 0;
        int i = 0;
        boolean noirDansLigne;
        boolean blancDansLigne;
        boolean plateauCouvert = true;
        while (i < plateau.length && plateauCouvert) {
            noirDansLigne = false;
            blancDansLigne = false;
            j = 0;
            while (j < plateau.length && (!noirDansLigne || !blancDansLigne)) {
                if (plateau[i][j].couleur == Case.CAR_NOIR) {
                    noirDansLigne = true;
                } else if (plateau[i][j].couleur == Case.CAR_BLANC) {
                    blancDansLigne = true;
                }
                if (i == coord.ligne && j == coord.colonne) {
                    if (couleur == Case.CAR_BLANC) {
                        blancDansLigne = true;
                    } else if (couleur == Case.CAR_NOIR) {
                        noirDansLigne = true;
                    }
                }
                j++;
            }
            if (!noirDansLigne || !blancDansLigne) {
                plateauCouvert = false;
            }
            i++;
        }
        j = 0;
        i = 0;
        while (j < plateau.length && plateauCouvert) {
            noirDansLigne = false;
            blancDansLigne = false;
            i = 0;
            while (i < plateau.length && (!noirDansLigne || !blancDansLigne)) {
                if (plateau[i][j].couleur == Case.CAR_NOIR) {
                    noirDansLigne = true;
                } else if (plateau[i][j].couleur == Case.CAR_BLANC) {
                    blancDansLigne = true;
                }
                if (i == coord.ligne && j == coord.colonne) {
                    if (couleur == Case.CAR_BLANC) {
                        blancDansLigne = true;
                    } else if (couleur == Case.CAR_NOIR) {
                        noirDansLigne = true;
                    }
                }
                i++;
            }
            if (!noirDansLigne || !blancDansLigne) {
                plateauCouvert = false;
            }
            j++;
        }
        return plateauCouvert;
    }

    /**
     * Cette fonction retourne le nombre de pions noir à enlever et le nombre de
     * pions blancs à enlever
     *
     * @param plateau le plateau de jeu
     * @param coord les coordonnées du pion à vérifier
     * @param couleur la couleur du joueur
     * @param niveau le niveau de jeu
     * @return un tableau de deux entiers : nombre de pions blanc et nombre de
     * pions noir
     */
    static int[] poneysPossible(Case[][] plateau, Coordonnees coord, char couleur, int niveau) {
        Coordonnees[] coordASupprimer = new Coordonnees[Coordonnees.NB_LIGNES * 2];
        int nbCoordDansTab = 0;
        // Un tableau de deux coordonnées : les pions qui va falloir enlever
        Coordonnees[] coordTrouvees;
        boolean pionPresent;
        int nbCoordTemp;
        Coordonnees coordLigne = new Coordonnees(coord.ligne, coord.colonne);
        for (Direction d : Direction.cardinalesLigne()) {
            pionPresent = false;
            nbCoordTemp = nbCoordDansTab;
            coordLigne.ligne = PionsAdverses.positionSuivante(coord, d).ligne;
            coordLigne.colonne = PionsAdverses.positionSuivante(coord, d).colonne;
            // Tant que je ne croise pas de pions sur le plateau
            while (PionsAdverses.estDansPlateau(coordLigne, Coordonnees.NB_LIGNES) && !pionPresent) {
                if (plateau[coordLigne.ligne][coordLigne.colonne].couleur != Case.CAR_VIDE) {
                    pionPresent = true;
                } else {
                    coordLigne.ligne = PionsAdverses.positionSuivante(coordLigne, d).ligne;
                    coordLigne.colonne = PionsAdverses.positionSuivante(coordLigne, d).colonne;
                }
            }
            if (pionPresent) {
                // Si on a trouvé un signal entre deux pions de même couleur
                if (plateau[coordLigne.ligne][coordLigne.colonne].couleur == couleur) {
                    Coordonnees coordDebut = new Coordonnees(coord.ligne, coord.colonne);
                    // On parcours les cases entre les deux émetteurs de signaux
                    for (int j = 0; j < valAbsolue(coord.colonne - coordLigne.colonne)-1; j++) {
                        coordDebut.colonne = PionsAdverses.positionSuivante(coordDebut, d).colonne;
                        // On regarde toutes les colonnes entre les deux pions.
                        coordTrouvees = signalTransverseLigne(plateau, coordDebut);
                        // Si on a trouvé un croisement de signaux alors on mémorise les coordonnées trouvées dans un tableau avec tous les pions qui emmetent un signal.
                        if (coordTrouvees[0] != null) {
                            if (nbCoordDansTab == nbCoordTemp) {
                                coordASupprimer[nbCoordDansTab] =  new Coordonnees(coordLigne.ligne, coordLigne.colonne);
                                nbCoordDansTab += 1;
                            }
                            coordASupprimer[nbCoordDansTab] = new Coordonnees(coordTrouvees[0].ligne, coordTrouvees[0].colonne);
                            coordASupprimer[nbCoordDansTab + 1] = new Coordonnees(coordTrouvees[1].ligne, coordTrouvees[1].colonne);
                            nbCoordDansTab += 2;
                        }
                    }
                }
            }
        }

        nbCoordTemp = nbCoordDansTab;
        Coordonnees coordColonne = new Coordonnees(coord.ligne, coord.colonne);
        for (Direction d : Direction.cardinalesColonne()) {
            pionPresent = false;
            coordColonne.ligne = PionsAdverses.positionSuivante(coord, d).ligne;
            coordColonne.colonne = PionsAdverses.positionSuivante(coord, d).colonne;
            // Tant que je ne croise pas de pions sur le plateau
            while (PionsAdverses.estDansPlateau(coordColonne, Coordonnees.NB_COLONNES) && !pionPresent) {
                if (plateau[coordColonne.ligne][coordColonne.colonne].couleur != Case.CAR_VIDE) {
                    pionPresent = true;
                } else {
                    coordColonne.ligne = PionsAdverses.positionSuivante(coordColonne, d).ligne;
                    coordColonne.colonne = PionsAdverses.positionSuivante(coordColonne, d).colonne;
                }
            }
            if (pionPresent) {
                // Si on a trouvé un signal entre deux pions de même couleur
                if (plateau[coordColonne.ligne][coordColonne.colonne].couleur == couleur) {
                    Coordonnees coordDebut = new Coordonnees(coord.ligne, coord.colonne);
                    // On parcours les cases entre les deux émetteurs de signaux
                    for (int j = 0; j < valAbsolue(coord.ligne - coordColonne.ligne)-1; j++) {
                        coordDebut.ligne = PionsAdverses.positionSuivante(coordDebut, d).ligne;
                        // On regarde toutes les lignes entre les deux pions.
                        coordTrouvees = signalTransverseColonne(plateau, coordDebut);
                        // Si on a trouvé un croisement de signaux alors on mémorise les coordonnées trouvées dans un tableau avec tous les pions qui emmetent un signal.
                        if (coordTrouvees[0] != null) {
                            if (nbCoordDansTab == nbCoordTemp) {
                                coordASupprimer[nbCoordDansTab] =  new Coordonnees(coordColonne.ligne, coordColonne.colonne);
                                nbCoordDansTab += 1;
                            }
                            coordASupprimer[nbCoordDansTab] = new Coordonnees(coordTrouvees[0].ligne, coordTrouvees[0].colonne);
                            coordASupprimer[nbCoordDansTab + 1] = new Coordonnees(coordTrouvees[1].ligne, coordTrouvees[1].colonne);
                            nbCoordDansTab += 2;
                        }
                    }

                }
            }
        }

        int[] tabPionsASupprimer = new int[2];
        int nbPionsNoirASupprimer = 0;
        int nbPionsBlancsASupprimer = 0;
        for (int i = 0; i < nbCoordDansTab; i++) {
            // Si la couleur du pion est noir alors on supprime ça hauteur
            if (plateau[coordASupprimer[i].ligne][coordASupprimer[i].colonne].couleur == Case.CAR_NOIR) {
                nbPionsNoirASupprimer += plateau[coordASupprimer[i].ligne][coordASupprimer[i].colonne].hauteur;
            } // Pareil avec la couleur blanche
            else if (plateau[coordASupprimer[i].ligne][coordASupprimer[i].colonne].couleur == Case.CAR_BLANC) {
                nbPionsBlancsASupprimer += plateau[coordASupprimer[i].ligne][coordASupprimer[i].colonne].hauteur;
            }
        }
        // On retourne le nombre de pions noir et blanc à supprimer
        tabPionsASupprimer[0] = nbPionsNoirASupprimer;
        tabPionsASupprimer[1] = nbPionsBlancsASupprimer;
        return tabPionsASupprimer;
    }

    /**
     * Cette fonction rentourne un tableau de deux coordonnées si il y a un
     * croisement de signal et un tableau de deux cases vides sinon
     *
     * @param plateau le plateau de jeu
     * @param coordAVerifier les coordonnées de la case à vérifier
     * @return un tableau de deux coordonnées cui correspondent aux pions à
     * enlever
     */
    static Coordonnees[] signalTransverseLigne(Case[][] plateau, Coordonnees coordAVerifier) {
        Coordonnees[] coordTrouvees = new Coordonnees[2];
        boolean pionPresent;
        Coordonnees coordTemp1 = new Coordonnees(-1, -1);
        Coordonnees coordTemp2 = new Coordonnees(-1, -1);
        Coordonnees coordColonne = new Coordonnees(coordAVerifier.ligne, coordAVerifier.colonne);
        for (Direction d : Direction.cardinalesColonne()) {
            pionPresent = false;
            coordColonne.ligne = PionsAdverses.positionSuivante(coordColonne, d).ligne;
            while (PionsAdverses.estDansPlateau(coordColonne, Coordonnees.NB_COLONNES) && !pionPresent) {
                // Si il y a un pion dans la colonne et dans la direction
                if (plateau[coordColonne.ligne][coordColonne.colonne].couleur != Case.CAR_VIDE) {
                    pionPresent = true;
                    if (d == Direction.NORD) {
                        coordTemp1.ligne = coordColonne.ligne;
                        coordTemp1.colonne = coordColonne.colonne;
                    }
                    else{
                        coordTemp2.ligne = coordColonne.ligne;
                        coordTemp2.colonne = coordColonne.colonne;
                    }
                } else {
                    coordColonne.ligne = PionsAdverses.positionSuivante(coordColonne, d).ligne;
                }
            }
            // Si on a trouvé deux pions
            if (coordTemp2.ligne != -1 && coordTemp1.ligne != -1 && pionPresent) {
                // Si ces deux pions sont de la même couleur
                if (plateau[coordTemp1.ligne][coordTemp1.colonne].couleur == plateau[coordTemp2.ligne][coordTemp2.colonne].couleur) {
                    coordTrouvees[0] = new Coordonnees(coordTemp1.ligne, coordTemp1.colonne);
                    coordTrouvees[1] = new Coordonnees(coordTemp2.ligne, coordTemp2.colonne);
                }
            }
        }
        return coordTrouvees;
    }

    /**
     * Cette fonction rentourne un tableau de deux coordonnées si il y a un
     * croisement de signal et un tableau de deux cases vides sinon
     *
     * @param plateau le plateau de jeu
     * @param coordAVerifier les coordonnées de la case à vérifier
     * @return un tableau de deux coordonnées cui correspondent aux pions à
     * enlever
     */
    static Coordonnees[] signalTransverseColonne(Case[][] plateau, Coordonnees coordAVerifier) {
        Coordonnees[] coordTrouvees = new Coordonnees[2];
        boolean pionPresent;
        Coordonnees coordTemp1 = new Coordonnees(-1, -1);
        Coordonnees coordTemp2 = new Coordonnees(-1, -1);
        Coordonnees coordLigne = new Coordonnees(coordAVerifier.ligne, coordAVerifier.colonne);
        for (Direction d : Direction.cardinalesLigne()) {
            pionPresent = false;
            coordLigne.colonne = PionsAdverses.positionSuivante(coordLigne, d).colonne;
            while (PionsAdverses.estDansPlateau(coordLigne, Coordonnees.NB_LIGNES) && !pionPresent) {
                // Si il y a un pion dans la ligne et dans la direction
                if (plateau[coordLigne.ligne][coordLigne.colonne].couleur != Case.CAR_VIDE) {
                    pionPresent = true;
                    if (d == Direction.OUEST) {
                        coordTemp1.ligne = coordLigne.ligne;
                        coordTemp1.colonne = coordLigne.colonne;
                    }
                    else{
                        coordTemp2.ligne = coordLigne.ligne;
                        coordTemp2.colonne = coordLigne.colonne;
                    }
                } else {
                    coordLigne.colonne = PionsAdverses.positionSuivante(coordLigne, d).colonne;
                }
            }
            // Si on a trouvé deux pions
            if (coordTemp1.ligne != -1 && coordTemp2.ligne != -1 && pionPresent) {
                // Si ces deux pions sont de la même couleur
                if (plateau[coordTemp1.ligne][coordTemp1.colonne].couleur == plateau[coordLigne.ligne][coordLigne.colonne].couleur) {
                    coordTrouvees[0] = new Coordonnees(coordTemp1.ligne, coordTemp1.colonne);
                    coordTrouvees[1] = new Coordonnees(coordTemp2.ligne, coordTemp2.colonne);
                }
            }
        }
        return coordTrouvees;
    }

    /**
     * Cette fonction permet de retourner les coordonnées tels quels car la
     * magie de fait que changer le pion de place et ne modifie pas son nombre
     * de pions.
     *
     * @param coord coordonnées de la case où se trouve la tour à fusionner
     * @param actions l'ensemble des actions possibles (en construction)
     * @param nbPions le nombre de pions par couleur sur le plateau avant de
     * jouer l'action
     * @param pionsASuppr le nombre de pions blanc et de pions noir a supprimer
     */
    void ajoutActionPoneys(Coordonnees coord, ActionsPossibles actions,
            NbPions nbPions, int[] pionsASuppr) {
        String action = "M" + coord.carLigne() + coord.carColonne() + ","
                + (nbPions.nbPionsNoirs - pionsASuppr[0]) + ","
                + (nbPions.nbPionsBlancs - pionsASuppr[1]);
        actions.ajouterAction(action);
    }

    /**
     * Cette fonction renvoie la valeur absolue d'un nombre passé en paramètre
     *
     * @param nb le nombre à tester
     * @return la valeur absolue
     */
    static int valAbsolue(int nb) {
        if (nb > 0) {
            return nb;
        } else {
            return -nb;
        }
    }
}
