public class Link {
    private String varid;
    private String uint;
    private boolean in;
    private boolean out;

    public Link(String varid, String uint, boolean in, boolean out) {
        this.varid = varid;
        this.uint = uint;
        this.in = in;
        this.out = out;
    }

    public void setVarid(String varid) {
        this.varid = varid;
    }

    public void setUint(String uint) {
        this.uint = uint;
    }

    public String getVarid() {

        return varid;
    }

    public String getUint() {
        return uint;
    }

    public boolean isIn() {
        return in;
    }

    public boolean isOut() {
        return out;
    }

    public Link(String varid, String uint) {
        this.varid = varid;
        this.uint = uint;
        this.in = true;
        this.out = true;

    }

    public void enableIn(){
        this.in = true;
        this.out = false;
    }

    public void enableOut(){
        this.in = false;
        this.out = true;
    }

    @Override
    public String toString() {
        String str = String.format("%10s    %10s    %7s    %7s", varid, uint, in, out);
        return  str;
    }
}
