import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class LexicalAnalyzer {
    private static ArrayList<Lexem> lexemTable;
    public static ArrayList<Lexem> getLexemTable(File finput, File foutput) {
        lexemTable = new ArrayList<>();
        int err = 0;
        try(PrintWriter pw=new PrintWriter(foutput)) {
            FReader fr = new FReader(finput);
            OneChar och = fr.getOneChar();
            byte group;
            while (och != null) {
                group = Tables.groupOfAttribute(och.getCh());
                String s = "" + (char) och.getCh();
                switch (group) {
                    case 0: {
                        while (group == 0) {
                            och = fr.getOneChar();
                            if(och==null) {
                                break;
                            }
                            group = Tables.groupOfAttribute(och.getCh());
                        }
                        break;
                    }
                    case 1: {
                        OneChar oc = fr.getOneChar();
                        if(oc==null) {
                            int code = Tables.codeOfConstant(s);
                            Lexem l = new Lexem(code, och.getRow(), och.getColumn());
                            String str = String.format("%5d    %5d    %5d    %20s", code, och.getRow(), och.getColumn(), s);
                            pw.println(str);
                            lexemTable.add(l);
                            och = oc;
                            break;
                        }
                        group = Tables.groupOfAttribute(oc.getCh());
                        while (group == 1) {
                            s = s +(char) oc.getCh();
                            oc = fr.getOneChar();
                            if(oc==null) {
                                int code = Tables.codeOfConstant(s);
                                Lexem l = new Lexem(code, och.getRow(), och.getColumn());
                                String str = String.format("%5d    %5d    %5d    %20s", code, och.getRow(), och.getColumn(), s);
                                pw.println(str);
                                och = oc;
                                lexemTable.add(l);
                                break;
                            }
                            group = Tables.groupOfAttribute(oc.getCh());
                        }
                        if(och != null) {
                            if (group != 2) {
                                int code = Tables.codeOfConstant(s);
                                Lexem l = new Lexem(code, och.getRow(), och.getColumn());
                                String str = String.format("%5d    %5d    %5d    %20s", code, och.getRow(), och.getColumn(), s);
                                pw.println(str);
                                lexemTable.add(l);
                                och = oc;
                            } else {
                                int col = oc.getColumn();
                                while (group == 2 || group == 1) {
                                    s = s + (char) oc.getCh();
                                    oc = fr.getOneChar();
                                    group = Tables.groupOfAttribute(oc.getCh());
                                }
                                Lexem l = new Lexem(-1, och.getRow(), och.getColumn());
                                String str = String.format("%5d    %5d    %5d    %20s", -1, och.getRow(), och.getColumn(), s);
                                pw.println(str);
                                pw.println("ERROR: In constant  '" + s + "' on position: " + (col - och.getColumn()));
                                lexemTable.add(l);
                                och = oc;
                            }
                        }
                        break;
                    }
                    case 2: {
                        OneChar oc = fr.getOneChar();
                        if(oc==null) {
                            int code = Tables.codeOfConstant(s);
                            Lexem l = new Lexem(code, och.getRow(), och.getColumn());
                            String str = String.format("%5d    %5d    %5d    %20s", code, och.getRow(), och.getColumn(), s);
                            och = oc;
                            pw.println(str);
                            lexemTable.add(l);
                            break;
                        }
                        group = Tables.groupOfAttribute(oc.getCh());
                        while (group == 2 || group == 1) {
                            s = s +(char) oc.getCh();
                            oc = fr.getOneChar();
                            if(oc==null) {
                                int code = Tables.codeOfConstant(s);
                                Lexem l = new Lexem(code, och.getRow(), och.getColumn());
                                String str = String.format("%5d    %5d    %5d    %20s", code, och.getRow(), och.getColumn(), s);
                                lexemTable.add(l);
                                pw.println(str);
                                och = oc;
                                break;
                            }
                            group = Tables.groupOfAttribute(oc.getCh());
                        }
                        if(och == null){
                            break;
                        }
                        int code = Tables.codeOfKeyword(s);
                        if (code == -1) {
                            code = Tables.codeOfIdentifier(s);
                        }
                        Lexem l = new Lexem(code, och.getRow(), och.getColumn());
                        lexemTable.add(l);
                        String str = String.format("%5d    %5d    %5d    %20s", code, och.getRow(), och.getColumn(), s);
                        pw.println(str);
                        och = oc;
                        break;
                    }
                    case 3: {
                        int code = Tables.codeOfDelimiter((char) och.getCh());
                        Lexem l = new Lexem(code, och.getRow(), och.getColumn());
                        lexemTable.add(l);
                        String str = String.format("%5d    %5d    %5d    %20s",code, och.getRow(), och.getColumn(), s);
                        pw.println(str);
                        och = fr.getOneChar();
                        break;
                    }
                    case 4: {
                        if (och.getCh() == ':') {
                            OneChar oc = fr.getOneChar();
                            group = Tables.groupOfAttribute(oc.getCh());
                            if (oc.getCh() == '=') {
                                s = s + (char)oc.getCh();
                                int code = Tables.codeOfMultidelimiter(s);
                                Lexem l = new Lexem(code, och.getRow(), och.getColumn());
                                String str = String.format("%5d    %5d    %5d    %20s",code, och.getRow(), och.getColumn(), s);
                                pw.println(str);
                                och = fr.getOneChar();
                                group = Tables.groupOfAttribute(oc.getCh());
                                lexemTable.add(l);
                            } else {
                                int code = Tables.codeOfDelimiter((char) och.getCh());
                                Lexem l = new Lexem(code, och.getRow(), och.getColumn());
                                lexemTable.add(l);
                                String str = String.format("%5d    %5d    %5d    %20s",code, och.getRow(), och.getColumn(), s);
                                pw.println(str);
                                och = oc;
                            }
                        } else if (och.getCh() == '(') {
                            OneChar oc = fr.getOneChar();
                            group = Tables.groupOfAttribute(oc.getCh());
                            if (oc.getCh() == '$') {
                                s = s +(char) oc.getCh();
                                int code = Tables.codeOfMultidelimiter(s);
                                Lexem l = new Lexem(code, och.getRow(), och.getColumn());
                                lexemTable.add(l);
                                String str = String.format("%5d    %5d    %5d    %20s",code, och.getRow(), och.getColumn(), s);
                                pw.println(str);
                                och = fr.getOneChar();
                                group = Tables.groupOfAttribute(och.getCh());
                            } else if (oc.getCh() == '*') {
                                oc = fr.getOneChar();
                                while (oc != null) {
                                    if (oc.getCh() != '*') {
                                        oc = fr.getOneChar();
                                    } else {
                                        oc = fr.getOneChar();
                                        while (oc.getCh() == '*') {
                                            oc = fr.getOneChar();
                                        }
                                        if (oc.getCh() == ')') {
                                            och = fr.getOneChar();
//                                            group = Tables.groupOfAttribute(och.getCh());
                                            break;
                                        }
                                    }
                                }
                                if (oc == null) {
                                    Lexem l = new Lexem(-1, och.getRow(), och.getColumn());
                                    String str = String.format("%5d    %5d    %5d    %20s",-1, och.getRow(), och.getColumn(), s);
                                    lexemTable.add(l);
                                    pw.println(str);
                                    pw.println("ERROR: (*...*) isn't closed");
                                    och = oc;
                                }
                            } else {
                                int code = Tables.codeOfDelimiter((char) och.getCh());
                                Lexem l = new Lexem(code, och.getRow(), och.getColumn());
                                String str = String.format("%5d    %5d    %5d    %20s",code, och.getRow(), och.getColumn(), s);
                                lexemTable.add(l);
                                pw.println(str);
                                och = oc;
                            }
                        } else if (och.getCh() == '$') {
                            OneChar oc = fr.getOneChar();
                            if (oc.getCh() == ')') {
                                s = s +(char) oc.getCh();
                                int code = Tables.codeOfMultidelimiter(s);
                                Lexem l = new Lexem(code, och.getRow(), och.getColumn());
                                String str = String.format("%5d    %5d    %5d    %20s",code, och.getRow(), och.getColumn(), s);
                                pw.println(str);
                                lexemTable.add(l);
                                och = fr.getOneChar();
                            } else {
                                Lexem l = new Lexem(-1, och.getRow(), och.getColumn());
                                lexemTable.add(l);
                                String str = String.format("%5d    %5d    %5d    %20s",-1, och.getRow(), och.getColumn(), s);
                                pw.println(str);
                                pw.println("ERROR: Invalid character '"+s+"'");
                                err++;
                                och = oc;
                            }
                        }
                        break;
                    }
                    case 5: {
                        Lexem l = new Lexem(-1, och.getRow(), och.getColumn());
                        lexemTable.add(l);
                        String str = String.format("%5d    %5d    %5d    %20s",-1, och.getRow(), och.getColumn(), s);
                        pw.println(str);
                        pw.println("ERROR: Invalid character '"+s+"'");
                        err++;
                        och = fr.getOneChar();
                        break;
                    }
                }
            }
            if (err>0){
                return null;
            }
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }
        return lexemTable;
    }
}
