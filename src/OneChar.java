public class OneChar implements Cloneable {
    private int ch;
    private int row;
    private int column;
    private boolean valid;

    public OneChar(int ch, int row, int column) {
        this.ch = ch;
        this.row = row;
        this.column = column;
    }

    public int getCh() {
        return ch;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }


    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        String s = "OneChar{" +
                "ch=" + ch +
                ", row=" + row +
                ", column=" + column +
                '}';
        return s;
    }
}
