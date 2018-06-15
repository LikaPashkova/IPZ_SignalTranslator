import java.io.File;
import java.util.ArrayList;

public class Translator {
    public static void main(String[] argz) {
        ArrayList<Lexem> lexems = LexicalAnalyzer.getLexemTable
                (new File("finputerror1.txt"), new File("foutputlexem.txt"));
        if (lexems == null) {
            System.out.println("Invalid characters in file.");
        }
//        System.out.println(Tables.outTables());
        Node tree = SyntacticalAnalyzer.getTree(lexems);
//        System.out.println(tree.nodeToString(""));
        String s = CodeGenerator.generateCode(tree);
//        System.out.println(s);
        System.out.println('\n'+CodeGenerator.outputTables());

//        System.out.println(lexems.toString());
    }
}
