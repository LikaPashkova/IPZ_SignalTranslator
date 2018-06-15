import java.util.ArrayList;

public class Node {
    private int code;
    private String value;
    private ArrayList<Node> childs;
    private Lexem lexem = null;

    public Node(int code, String value){
        this.code = code;
        this.value = value;
        this.childs = new ArrayList<>();
    }

    void addTerm(int code, String value){
        Node node = new Node(code, value);
        childs.add(node);
    }

    void addNotTerm(String value){
        Node node = new Node(-1, value);
        childs.add(node);
    }

    void addChild(Node node){
        childs.add(node);
    }

    int getCountOfChilds(){
        return childs.size();
    }

    Node getChildAt(int index){
        return childs.get(index);
    }

    public int getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }

    public String nodeToString(String tab){
        String s = this.value;
        if  (this.code>=0){
            s = code+" "+s;
        }
        s = tab+s;
        for(Node child:this.childs){
            s = s + '\n'+ child.nodeToString(tab+"...|");
        }
        return s;
    }

    public  Lexem getLexem(){
        return this.lexem;
    }

    public void addLexem(Lexem l){
        this.lexem = l;
    }
}
