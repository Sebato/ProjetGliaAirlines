public abstract class PlaneElement {

    //l'inice de cet element
    public int num;

    //le numéro du bloc de sièges qui le précède
    public int pos;

    public elemType type;

    public int getNum() {
        return num;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public elemType getType() {
        return type;
    }

    public PlaneElement(int num, int pos) {
        this.num = num;
        this.pos = pos;

    }
}
