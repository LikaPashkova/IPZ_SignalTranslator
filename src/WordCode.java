public class WordCode {
    private String value;
    private int code;

    public WordCode(String value, int code) {
        this.value = value;
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "\nWordCode{" +
                "value='" + value + '\'' +
                ", code=" + code +
                '}';
    }
}

