import java.util.ArrayList;
import java.util.List;

public class Plane {

    //L’objectif est de positionner n séparateurs dans un avion ayant une capacité de m blocs de sièges

    // n = nombre de séparateurs
    // m = nombre de blocs de sièges
    // exits = tableau des positions des sorties de secours

        //Contraintes :
    //Il est impératif de ne pas installer de séparateurs au niveau des sorties de secours (au moins une sortie)
    //il est essentiel que la classe à l’avant de l'avion dispose d'au moins deux blocs
    //il est nécessaire de garantir que les distances entre les séparateurs sont différente

        //Visualisation :
    //avion modélisé textuellement par un tableau de taille m+1 (m blocs de sièges + 0 pour la cabine de pilotage)
    //les séparateurs seront insérés dans le tableau et représentés par des * (étoiles)
    //les sorties de secours seront insérés dans le tableau et représentées par des # (dièses)

    //Exemple : n = 3, m = 5, exits = 1
    //avant division :
    // 0 1 2 3 4 5
    //après division :
    // 0 1 * 2 # 3 * 4 5

    //Exemple Exo : n = 5, m = 11, exits = 1
    //avant division :
    // 0 1 2 3 4 5 6 7 8 9 10 11
    //après division :
    // 0 * 1 2 * # 3 4 5 6 7 * 8 * 9 10 11 *

    private final int n;
    private final int m;
    private final int[] exits;
    private final ArrayList<Character> planeTxt;
    private final ArrayList<PlaneElement> elements;

    public Plane(int n, int m, int[] exits){
        this.n = n;
        this.m = m;
        this.exits = exits;
        this.planeTxt = new ArrayList<Character>();
        this.elements = new ArrayList<PlaneElement>();
    }

    public Plane(Instance I){
        this.n = I.nb_dividers;
        this.m = I.capacity;
        this.exits = I.exits;
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
    public int[] getExits() {
        return exits;
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
        if (num > this.exits.length || num < 0) return null;
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

    public boolean atLeastOneExitFree(){
        //on vérifie qu'au moins une sortie de secours est libre

        for (PlaneElement e : this.elements) {
            if (e.getType() == elemType.EXIT) {
                Exit exit = (Exit) e;
                if ( exit.isFree() ) return true;
            }
        }
        return false;
    }

    public boolean allDivsPlaced(){
        //on vérifie que tous les séparateurs ont été placés

        for (int i = 0; i < this.n; i++) {
            Divider d = getDividerByNum(i);
            if (d.getPos() == -1 ) return false;
        }
        return true;
    }

    //RUN

    public static void dividers(int n, int m, int[] exits) {

        //initialisation de l'avion
        Plane plane = new Plane(n, m, exits);
        ArrayList<Divider> dividers = new ArrayList<Divider>();

        //nombre de possibilités de positionnement des séparateurs
        //a verifier
        int nbPossibilities = (int) Math.pow(m, n);

        //initialisation des éléments

        for (int i = 0; i < exits.length; i++) {
            plane.elements.add(new Exit(i, exits[i]));
        }

        for (int i = 0; i < n; i++) {
            plane.elements.add(new Divider(i, -1));
            dividers.add(plane.getDividerByNum(i));
        }

        //recherche de la position de chaque élément
        //on teste toutes les positions possibles pour chaque élément jusqu'à trouver une solution valide

        while( nbPossibilities > 0 ) {
            for (Divider d : dividers){
                if (d.getPos() == -1) {
                    d.setPos(0);
                    while (d.getPos() < m) {
                        if (plane.allDivsPlaced()) {
                            if (plane.DistisDiff() && plane.firstClassIsTwoBlocks() && plane.atLeastOneExitFree()) {
                                System.out.println("Solution trouvée :");
                                System.out.println(plane.toString());
                                return;
                            }
                        }
                        d.setPos(d.getPos() + 1);
                    }
                    d.setPos(-1);
                    break;
                }

            }
            nbPossibilities--;
        }
    }

    public static void main(String[] args) {
        int[] exits = {3};
        dividers(5, 11, exits);
    }

}
