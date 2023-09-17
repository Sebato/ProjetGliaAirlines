public abstract class PlaneElement {

    //l'inice de cet element
    public int num;

    //sa position dans l'avion (le bloc de sieges qui le precede)
    public int pos;

    public elemType type;
    public int getNum() {
        return num;
    }
    public int getPos() {
        return pos;
    }
    public elemType getType() {
        return type;
    }

    public PlaneElement(int num, int pos) {
        this.num = num;
        this.pos = pos;

    }
}
