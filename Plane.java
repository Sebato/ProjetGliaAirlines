import java.util.ArrayList;
import java.util.Arrays;
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
    //avion modélisé textuellement par un tableau de taille m+1 (m blocs de sièges + le 0 pour la cabine de pilotage)
    //les séparateurs seront insérés dans le tableau et représentés par leur numéro
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
    private final PlaneElement[] elements;

    public Plane(int n, int m, int[] exits) {
        this.n = n;
        this.m = m;
        this.exits = exits;
        this.elements = new PlaneElement[m+1]; //SI OUT OF BOUNDS, REGARDE ICI MON BRO

        //A SECURISER (exits vide ou length > m etc...)
        for (int i = 0 ; i < exits.length ; i++) {
            this.elements[exits[i]] = new Exit(i, exits[i]);
        }
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
    public PlaneElement[] getElements() {
        return elements;
    }


    //OTHER ACCESSORS

    public Divider getDiv(int num) {
        if (num > this.n || num < 0) return null;
        for (PlaneElement e : this.elements) {
            if (e != null && e.getType() == elemType.DIVIDER && e.getNum() == num) return (Divider) e;
        }
        return null;
    }
    public Divider[] getAllDivs() {
        Divider[] divs = new Divider[this.n];
        int i = 0;
        for (PlaneElement e : this.elements) {
            if (e != null && e.getType() == elemType.DIVIDER) {
                divs[i] = (Divider) e;
                i++;
            }
        }
        return divs;
    }
    public Exit getExit(int num) {
        if (num > this.exits.length || num < 0) return null;
        for (PlaneElement e : this.elements) {
            if (e != null && e.getType() == elemType.EXIT && e.getNum() == num) return (Exit) e;
        }
        return null;
    }
    public void placeDiv(int n, int i) {
        //Desormais inutile, à supprimer
        this.elements[i] = new Divider(n, i);
    }


    //METHODES UTILES
    public int getDist(Divider d1, Divider d2) {
        return Math.abs(d1.getPos() - d2.getPos());
    }
    public void display(){

        ArrayList<String> s1 = new ArrayList<String>();

        for (int i = 0; i < this.m; i++) {
            s1.add("| ");
        }
        s1.add("|");
        for ( int i : this.exits) {
            s1.set(i, "|#");
        }

        ArrayList<String> s2 = new ArrayList<String>(s1);

        for ( Divider d : this.getAllDivs()) {
            s2.set(d.getPos(), "|"+d.getNum());
        }

        System.out.println("Instance : n = " + this.n + ", m = " + this.m + ", exits = " + Arrays.toString(this.exits));
        System.out.println("\n avant resolution : ");
        for (String s : s1) {
            System.out.print(s);
        }
        System.out.println("\n après resolution : ");
        for (String s : s2) {
            System.out.print(s);
        }
        System.out.println();
    }

    public void debug(){

        ArrayList<String> deb = new ArrayList<String>();

        for (int i = 0; i < this.m; i++) {
            deb.add("|_");
        }
        deb.add("|");
        for ( int i : this.exits) {
            deb.set(i, "|#");
        }
        for ( Divider d : this.getAllDivs()) {
            if (d != null) deb.set(d.getPos(), "|"+d.getNum());
        }

        System.out.print("debug : ");
        for (String s : deb) {
            System.out.print(s);
        }
        System.out.println();
    }

    //CONTRAINTES
    public boolean DistisDiff() {
        //on va calculer le nombre de blocs de sièges entre chaque couple de séparateurs
        //et vérifier que ces distances sont toutes différentes

        System.out.println("\n ---------------\n  Testing for distinct distances\n");
        ArrayList<Divider> dividers = new ArrayList<Divider>();
        List<Integer> distances = new ArrayList<Integer>();

        //on récupère tous les séparateurs
        for (PlaneElement e : this.elements) {
            if (e != null && e.getType() == elemType.DIVIDER) dividers.add((Divider) e);
        }

        //on affiche les séparateurs et leurs positions
        StringBuilder s = new StringBuilder(), s2 = new StringBuilder();
        for (Divider d : dividers) {
            s.append(d.getNum()).append(" ");
        }
        for (Divider d : dividers) {
            s2.append(d.getPos()).append(" ");
        }
        System.out.println("dividers : "+ s);
        System.out.println("position : "+ s2+"\n");

        for (int i = 0; i < dividers.size(); i++) {
            for (int j = i+1; j < dividers.size(); j++) {
                System.out.print("div" + dividers.get(i).getNum() +
                              " ; div" + dividers.get(j).getNum());
                Divider d1 = dividers.get(i);
                Divider d2 = dividers.get(j);

                int dist = getDist(d1, d2);
                System.out.println(" dist = " + dist);
                //si la distance est déjà présente dans la liste, on retourne false
                if (distances.contains(dist)) {
                    System.out.println("distance égale trouvée");
                    return false;
                }
                //sinon on l'ajoute à la liste
                distances.add(dist);
            }
        }
        System.out.println("distances : " + distances.toString());
        return true;
    }
    public boolean firstClassIsTwoBlocks() {
        //on vérifie que la classe à l'avant de l'avion dispose d'au moins deux blocs
        //si un le premier séparateur est placé au niveau de la cabine de pilotage, on vérifie que sa distance avec le suivant est supérieure à 1
        //sinon on vérifie qu'il est positionné à au moins 2

        System.out.println("\n ---------------\n  Testing for first Class Is Two Blocks wide");

        Divider d = getDiv(0);
        Divider d1 = getDiv(1);

        if (d != null && d1 != null) {
            if (d.getPos() == 0) {
                {
                    int dist = getDist(d, d1);
                    System.out.println("dist = " + dist);
                    return dist >= 2;
                }
            } else return d.getPos() >= 2;
        }

        System.out.println("test ok");
        return true;
    }
    public boolean atLeastOneExitFree() {
        //on vérifie qu'au moins une sortie de secours est libre
        ArrayList<Integer> tmp = new ArrayList<Integer>();
        ArrayList<Integer> divs = new ArrayList<Integer>();

        System.out.println("\n ---------------\n  Testing for at Least One Exit Free\n");

        for (int e : this.exits) {
            tmp.add(e);
        }

        for (PlaneElement p : this.elements) {
            if (p != null && p.getType() == elemType.DIVIDER) {
                Divider d = (Divider) p;
                divs.add(d.getPos());
            }
        }
        for (Integer d : divs) {
            if (tmp.contains(d)) {
                tmp.remove((Integer) d);
            }
        }
        System.out.println("exits free : " + tmp.toString());
        return !tmp.isEmpty();
    }

    //TEST DE FIN
    public boolean allDivsPlaced() {
        //on vérifie que tous les séparateurs ont été placés
        int cpt = 0;
        for (PlaneElement e : this.elements) {
            if (e != null && e.getType() == elemType.DIVIDER) {
                cpt++;
            }
        }
        return cpt == this.n;
    }

    //RUN

    public static void dividers(int nbDivs, int nbBlocks, int[] exits) {

        //initialisation de l'avion
        Plane plane = new Plane(nbDivs, nbBlocks, exits);

        //liste des séparateurs et des sorties de secours
        ArrayList<Divider> dividers = new ArrayList<Divider>();
        //ArrayList<Exit> exits = new ArrayList<Exit>();

        //recherche des solutions
        int i = 0;
        int n = 0;

        System.out.println("n = " + n + ", i = " + i);
        plane.debug();

        while( n < plane.m && n > -1 && !plane.allDivsPlaced() ) {

            System.out.print("etat actuel de la recherche : ");
            plane.debug();
            System.out.println(" début de boucle n = " + n + ", i = " + i);

            //si la postion est hors de la taille de l'avion, la combinaison n'est pas bonne
            if (i > plane.m) {

                System.out.println("##################\nOUT OF BOUNDS : (" + i + " >=  " + plane.m + ")");

                //on se place sur le diviseur n-1
                n--;
                i = dividers.get(n).getPos();

                //on supprime ce diviseur pour éviter un doublon
                dividers.remove(n);
                plane.elements[i] = null;

                //on place le curseur i sur l'espace suivant
                i++;

                //et on reprend la recherche
                System.out.println("reprise à n = " + n + ", i = " + i+"\n##################");

                //mais si on est encore en dehors de l'avion, on recule encore plus
            }
            else{
                plane.placeDiv(n, i);
                dividers.add((Divider) plane.elements[i]);

                i = dividers.get(n).getPos() + 1;
                //Overkill ? => checker si juste i++ fait pareil

                System.out.println("div placé : n = " + n + ", i++ = " + i);
                plane.debug();

                if (plane.DistisDiff() && plane.firstClassIsTwoBlocks() && plane.atLeastOneExitFree()) {
                    n++;
                    System.out.println("tests ok\n");
                } else {
                    System.out.println("tests pas ok\n");
                    plane.elements[dividers.get(n).getPos()] = null;
                    dividers.remove(n);
                }
            }


        }
        if (n == -1) {
            System.out.println("Aucune solution");
        } else {
            System.out.println("Solution trouvée");
            plane.display();
        }

    }
    public static void dividers(Instance inst) {
        dividers(inst.nb_dividers, inst.capacity, inst.exits);
    }

    public static void main(String[] args) {
        //int[] exits = {3};
        dividers(Instance.inst5);
    }

}


