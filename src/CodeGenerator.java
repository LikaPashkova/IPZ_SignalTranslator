import java.io.*;
import java.util.ArrayList;

public class CodeGenerator {
    private static ArrayList<String> labelTable = new ArrayList<>();
    private static ArrayList<Boolean> labelUsedTable = new ArrayList<>();
    private static ArrayList<String> identifierTable = new ArrayList<>();
    private static ArrayList<Link> linkTable = new ArrayList<>();

    public static String generateCode(Node tree) {
        File f = new File("code.asm");
        String s = "";
        try (PrintWriter pw = new PrintWriter(f)) {
            s = generateProgram(tree.getChildAt(0));
            pw.print(s);
            for(int i = 0; i<labelTable.size(); i++){
                if(labelUsedTable.get(i)==false){
                    System.out.println("WARNING: Label '"+labelTable.get(i)+"' not used");
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return s;
    }

    private static String generateProgram(Node node) {
        String s = "";
        String procid = node.getChildAt(1).getChildAt(0).getChildAt(0).getValue();
        s = "?" + procid + " SEG\n" + generateBlock(node.getChildAt(3)) + "?" + procid + " ENDS";
        return s;
    }

    private static String generateBlock(Node node) {
        String s = "";
        s = '\n' + generateStatementList(node.getChildAt(1)) + '\n';
        return s;
    }

    private static String generateStatementList(Node node) {
        String s = "";
        if (!node.getChildAt(0).getValue().equals("empty")) {
            s = generateStatement(node.getChildAt(0)) + generateStatementList(node.getChildAt(1));
        }
        return s;
    }

    private static String generateStatement(Node node) {
        String s = "";
        Node child = node.getChildAt(0);
        if (child.getValue().equals("<unsigned-integer>")) {
            String l = child.getChildAt(0).getValue();
            boolean flag = false;
            for (String label : labelTable) {
                if (label.equals(l)) {
                    flag = true;
                }
            }
            if (flag) {
                Lexem lex = child.getLexem();
                s = "\nERROR: row:"+lex.getRow()+", column: "+lex.getColumn()+": Label '" + l + "' already used";
                System.out.println(s);
            } else {
                labelTable.add(l);
                labelUsedTable.add(false);
                s = l + ":" + generateStatement(node.getChildAt(2));
            }
        } else if (child.getValue().equals("<var-identifier>")) {
            String id = child.getChildAt(0).getChildAt(0).getValue();
            boolean flag = false;
            for (String i : identifierTable) {
                if (i.equals(id)) {
                    flag = true;
                }
            }
            if (flag == false) {
                identifierTable.add(id);
            }
            String numb = node.getChildAt(2).getChildAt(0).getValue();
            s = "MOV AX," + numb + "\nMOV ?" + id + ",AX\n";
        } else if (child.getValue().equals("<procedure-identifier>")) {
            String procid = child.getChildAt(0).getChildAt(0).getValue();
            child = node.getChildAt(1);
            if (!child.getChildAt(0).getValue().equals("empty")) {
                s = "MOV AX,?" + child.getChildAt(1).getChildAt(0).getChildAt(0).getValue() + "\n";
                s = s + "PUSH AX\n" + actarglist(child.getChildAt(2));
            }
            s = s + "CALL " + procid + '\n';
        } else if (child.getValue().equals("GOTO")) {
            String l = node.getChildAt(1).getChildAt(0).getValue();
            int index = -1;
            for (int i = 0; i<labelTable.size(); i++) {
                if (labelTable.get(i).equals(l)) {
                    index = i;
                }
            }
            if (index == -1) {
                Lexem lex = node.getChildAt(1).getLexem();
                System.out.println("ERROR: row:"+lex.getRow()+", column:"+lex.getColumn()+": Label '" + l + "' isn't declared");
            } else {
                s = "JMP " + l + '\n';
                labelUsedTable.set(index, true);
            }
        } else if (child.getValue().equals("LINK")) {
            String varid = node.getChildAt(1).getChildAt(0).getChildAt(0).getValue();
            String uint = node.getChildAt(3).getChildAt(0).getValue();
            int index = -1;
            for (int i = 0; i < linkTable.size(); i++) {
                if (linkTable.get(i).getVarid().equals(varid)) {
                    index = i;
                }
            }
            if (index == -1) {
                linkTable.add(new Link(varid, uint));
            } else {
                linkTable.get(index).setUint(uint);
            }
        } else if (child.getValue().equals("IN")) {
            String uint = node.getChildAt(1).getChildAt(0).getValue();
            int index = -1;
            for (int i = 0; i < linkTable.size(); i++) {
                if (linkTable.get(i).getUint().equals(uint)) {
                    index = i;
                }
            }
            if (index == -1) {
                Lexem lex = node.getChildAt(1).getLexem();
                System.out.println("ERROR: row:"+lex.getRow()+", column:"+lex.getColumn()+": Port '" + uint + "' not found in LinkTable for enable IN");
            } else {
                linkTable.get(index).enableIn();
            }
        } else if (child.getValue().equals("OUT")) {
            String uint = node.getChildAt(1).getChildAt(0).getValue();
            int index = -1;
            for (int i = 0; i < linkTable.size(); i++) {
                if (linkTable.get(i).getUint().equals(uint)) {
                    index = i;
                }
            }
            if (index == -1) {
                Lexem lex = node.getChildAt(1).getLexem();
                System.out.println("ERROR: row:"+lex.getRow()+", column:"+lex.getColumn()+":  Port '" + uint + "' not found in LinkTable for enable OUT");
            } else {
                linkTable.get(index).enableOut();
            }
        } else if (child.getValue().equals("RETURN")) {
            s = "RET\n";
        } else if (child.getValue().equals("($")){
            File file = new File(node.getChildAt(1).getChildAt(0).getChildAt(0).getValue()+".txt");
            try {
                BufferedReader  br = new BufferedReader(new FileReader(file));
                int ch = br.read();
                String str = "";
                while(ch!=-1){
                    str = str+(char)ch;
                    ch = br.read();
                }
                s = "\n"+str+"\n";
            }catch (FileNotFoundException e) {
                Lexem lex = node.getChildAt(1).getLexem();
                System.out.println("ERROR: row:"+lex.getRow()+", column:"+lex.getColumn()+":  File '"+node.getChildAt(1).getChildAt(0).getChildAt(0).getValue()+"' not found");
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return s;
    }

    private static String actarglist(Node node) {
        String s = "";
        if (!node.getChildAt(0).getValue().equals("empty")) {
            Node child = node.getChildAt(1);
            s = "MOV AX,?" + child.getChildAt(0).getChildAt(0).getValue() + "\nPUSH AX\n" + actarglist(node.getChildAt(2));
        }
        return s;
    }

    public static String outputTables() {
        String s = "";
        s = "LabelTable:\n";
        for (String l : labelTable) {
            s = s + "   " + l + '\n';
        }
        s = s + '\n';
        s = s + "IdentifierTable:\n";
        for (String id : identifierTable) {
            s = s + "   " + id + '\n';
        }
        s = s + '\n';
        s = s + "LinkTable:\n";
        s = s + String.format("%10s    %10s    %7s    %7s", "varid", "uint", "in", "out");
        s = s+'\n';
        for (Link link : linkTable) {
            s = s + link+'\n';
        }
        s = s + '\n';
        return s;
    }
}
