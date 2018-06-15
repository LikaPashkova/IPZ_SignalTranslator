import java.io.*;

public class FReader {
    private File file;
    private int row;
    private int column;
    private OneChar buffer;
    private BufferedReader br;

    public FReader(File file) {
        this.file = file;
        this.row = 1;
        this.column = 1;
        this.buffer = null;
        try {
            br = new BufferedReader(new FileReader(this.file));
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void pushChar(OneChar ch) {
        this.buffer = ch;
    }

    public OneChar popChar() {
        try {
            if (buffer != null) {
                OneChar ch = (OneChar) buffer.clone();
                return ch;
            } else {
                return null;
            }
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }

    public OneChar getOneChar() {
        try{
            int ch = br.read();
            if (ch != -1) {
                OneChar oc = new OneChar(ch, row, column++);
                if(ch==10){
                    row++;
                    column=1;
                }
                return oc;
            }else{
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    @Override
    protected void finalize() throws Throwable {
        br.close();
        super.finalize();
    }
}
