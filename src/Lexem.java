public class Lexem {
    private int code;
    private int row;
    private int column;

    public Lexem(int code, int row, int column) {
        this.code = code;
        this.row = row;
        this.column = column;
    }

    public int getCode() {
        return code;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    @Override
    public String toString() {
        return "\nLexem{" +
                "code=" + code +
                ", row=" + row +
                ", column=" + column +
                '}';
    }
}
