import java.util.ArrayList;
import java.util.List;

public class Plane {

    //L’objectif est de positionner n séparateurs dans un avion ayant une capacité de m blocs de sièges

    // n = nombre de séparateurs
    // m = nombre de blocs de sièges
    // nbexits = nombre de sorties de secours

        //Contraintes :
    //Il est impératif de ne pas installer de séparateurs au niveau des sorties de secours (au moins une sortie)
    //il est essentiel que la classe à l’avant de l'avion dispose d'au moins deux blocs
    //il est nécessaire de garantir que les distances entre les séparateurs sont différente

        //Visualisation :
    //avion modélisé textuellement par un tableau de taille m+1 (m blocs de sièges + 0 pour la cabine de pilotage)
    //les séparateurs seront insérés dans le tableau et représentés par des * (étoiles)
    //les sorties de secours seront insérés dans le tableau et représentées par des # (dièses)

    //Exemple : n = 3, m = 5, nbexits = 1
    //avant division :
    // 0 1 2 3 4 5
    //après division :
    // 0 1 * 2 # 3 * 4 5

    //Exemple Exo : n = 5, m = 11, nbexits = 1
    //avant division :
    // 0 1 2 3 4 5 6 7 8 9 10 11
    //après division :
    // 0 * 1 2 * # 3 4 5 6 7 * 8 * 9 10 11 *

    private final int n;
    private final int m;
    private final int nbExits;
    private final ArrayList<Character> planeTxt;
    private final ArrayList<PlaneElement> elements;

    public Plane(int n, int m, int nbexits){
        this.n = n;
        this.m = m;
        this.nbExits = nbexits;
        this.planeTxt = new ArrayList<Character>();
        this.elements = new ArrayList<PlaneElement>();
    }

    //GETTERS SETTERS
    public int getN() {
        return n;
    }
    public int getM() {
        return m;
    }
    public int getNbExits() {
        return nbExits;
    }
    public ArrayList<Character> getPlaneTxt() {
        return planeTxt;
    }
    public ArrayList<PlaneElement> getElements() {
        return elements;
    }
    public PlaneElement getElementByPos(int pos) {
        if (pos > this.m || pos < 0) return null;
        for (PlaneElement e : this.elements) {
            if (e.getPos() == pos) return e;
        }
        return null;
    }
    public Divider getDividerByNum(int num) {
        if (num > this.n || num < 0) return null;
        for (PlaneElement e : this.elements) {
            if (e.getType() == elemType.DIVIDER && e.getNum() == num) return (Divider) e;
        }
        return null;
    }
    public Exit getExitByNum(int num) {
        if (num > this.nbExits || num < 0) return null;
        for (PlaneElement e : this.elements) {
            if (e.getType() == elemType.EXIT && e.getNum() == num) return (Exit) e;
        }
        return null;
    }
    public int getExitPos(int numExit) {
        Exit e = getExitByNum(numExit);
        if (e != null) return e.getPos();
        else return -1;
    }

    //METHODES

    public int getDist(Divider d1, Divider d2) {
        return Math.abs(d1.getPos() - d2.getPos());
    }
    public boolean DistisDiff() {
        //on va calculer le nombre de blocs de sièges entre chaque couple de séparateurs
        //et vérifier que ces distances sont toutes différentes

        ArrayList<Divider> dividers = new ArrayList<Divider>();
        List<Integer> distances = new ArrayList<Integer>();

        for (PlaneElement e : this.elements) {
            if (e.getType() == elemType.DIVIDER) dividers.add((Divider) e);
        }
        for (Divider d1 : dividers) {
            for (Divider d2 : dividers) {
                if (d1 != d2) {
                    int dist = getDist(d1, d2);
                    if (distances.contains(dist)) return false;
                    distances.add(dist);
                }
            }
        }
        return true;
    }

    public boolean firstClassIsTwoBlocks(){
        //on vérifie que la classe à l'avant de l'avion dispose d'au moins deux blocs
        //si un le premier séparateur est placé au niveau de la cabine de pilotage, on vérifie que sa distance avec le suivant est supérieure à 1
        //sinon on vérifie que sa position est supérieure à 1

        Divider d = getDividerByNum(0);
        Divider d2 = getDividerByNum(1);
        if (d != null) {
            if (d.getPos() == 0) {
                if (getDist(d, d2) < 2 ) return false;
            }
            else if (d.getPos() < 2) return false;
        }
        return true;
    }

    public boolean exitsAreNotDividers() {
        //on vérifie que les sorties de secours ne sont pas placées au niveau des séparateurs

        for (PlaneElement e : this.elements) {
            if (e.getType() == elemType.EXIT) {
                for (int i = 0; i < this.n; i++) {
                    Divider d = getDividerByNum(i);
                    if (d != null) {
                        if (d.getPos() == e.getPos()) return false;
                    }
                }
            }
        }
        return true;
    }

    //RUN

    public static void dividers(int n, int m, int exits) {

        //initialisation de l'avion
        Plane plane = new Plane(n, m, exits);

        //initialisation des éléments

        for (int i = 0; i < n; i++) {
            plane.elements.add(new Divider(i, -1));
        }

        for (int i = 0; i < exits; i++) {
            plane.elements.add(new Exit(i, -1));
        }

        //recherche de la position de chaque élément
        //on teste toutes les positions possibles pour chaque élément jusqu'à trouver une solution valide

    }

}
