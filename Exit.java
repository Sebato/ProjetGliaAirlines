public class Exit extends PlaneElement {

    private boolean free;

    public boolean isFree() {
        return free;
    }

    public void setFree(boolean free) {
        this.free = free;
    }

    public Exit(int num, int pos) {
        super(num, pos);
        this.free = true;
        this.type = elemType.EXIT;
    }


}
