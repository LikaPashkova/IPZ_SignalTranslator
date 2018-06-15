import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class SyntacticalAnalyzer {
    private static int index;
    private static ArrayList<Lexem> lexems;

    public static Node getTree(ArrayList<Lexem> lex) {
        lexems = lex;
        index = 0;
        if (lexems.size() > 0) {
            Node node = signalprogram();
            if (node != null) {
                if (lexems.size() > index) {
                    System.out.println("ERROR: Charachters was found after end of program.");
                    System.exit(1);
                    return null;
                }
                File f = new File("tree.txt");
                try (PrintWriter pw = new PrintWriter(f)) {
                    pw.print(node.nodeToString(""));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                return node;
            }
        } else {
            System.out.println("ERROR: The file is empty.");
        }
        System.exit(1);
        return null;
    }

    private static Node signalprogram() {
        Node buff = program();
        if (buff != null) {
            Node node = new Node(-1, "<signal-program>");
            node.addChild(buff);
            return node;
        }
        System.out.println("ERROR: 'program' expected but " + Tables.getWordWithCode(lexems.get(index).getCode()) + " found in line:" + lexems.get(index).getRow() + ", column:" + lexems.get(index).getColumn());
        return null;
    }

    private static Node program() {
        Lexem l1 = lexems.get(index);
        int code1 = l1.getCode();
        String s1 = Tables.getWordWithCode(code1);
        if (s1.equals("PROGRAM")) {
            index++;
            Node buff1 = procid();
            if (buff1 != null) {
                index++;
                Lexem l2 = lexems.get(index);
                int code2 = l2.getCode();
                String s2 = Tables.getWordWithCode(code2);
                if (s2.equals(";")) {
                    index++;
                    Node buff2 = block();
                    if (buff2 != null) {
                        if(index+1<lexems.size()) {
                        index++;
                            Lexem l3 = lexems.get(index);
                            int code3 = l3.getCode();
                            String s3 = Tables.getWordWithCode(code3);
                            if (s3.equals(".")) {
                                index++;
                                Node node = new Node(-1, "<program>");
                                node.addChild(new Node(code1, s1));
                                node.getChildAt(0).addLexem(l1);
                                node.addChild(buff1);
                                node.addChild(new Node(code2, s2));
                                node.getChildAt(2).addLexem(l2);
                                node.addChild(buff2);
                                node.addChild(new Node(code3, s3));
                                node.getChildAt(4).addLexem(l3);
                                return node;
                            }
                        }
                        System.out.println("ERROR: '.' expected but " + Tables.getWordWithCode(lexems.get(index).getCode()) + " found in line:" + lexems.get(index).getRow() + ", column:" + lexems.get(index).getColumn());
                        return null;
                    }
                    System.out.println("ERROR: 'block' expected but " + Tables.getWordWithCode(lexems.get(index).getCode()) + " found in line:" + lexems.get(index).getRow() + ", column:" + lexems.get(index).getColumn());
                    return null;
                }
                System.out.println("ERROR: ';' expected but " + s2 + " found in line:" + lexems.get(index).getRow() + ", column:" + lexems.get(index).getColumn());
                return null;
            }
            System.out.println("ERROR: 'procedure-identifier' expected but " + Tables.getWordWithCode(lexems.get(index).getCode()) + " found in line:" + lexems.get(index).getRow() + ", column:" + lexems.get(index).getColumn());
            return null;
        }
        System.out.println("ERROR: 'PROGRAM' expected but " + s1 + " found in line:" + lexems.get(index).getRow() + ", column:" + lexems.get(index).getColumn());
        return null;
    }

    private static Node block() {
        Lexem l1 = lexems.get(index);
        int code1 = l1.getCode();
        String s1 = Tables.getWordWithCode(code1);
        if (s1.equals("BEGIN")) {
            index++;
            Node buff1 = statementlist();
            if (buff1 != null) {
                Lexem l2 = lexems.get(index);
                int code2 = l2.getCode();
                String s2 = Tables.getWordWithCode(code2);
                if (s2.equals("END")) {
                    Node node = new Node(-1, "<block>");
                    node.addChild(new Node(code1, s1));
                    node.getChildAt(0).addLexem(l1);
                    node.addChild(buff1);
                    node.addChild(new Node(code2, s2));
                    node.getChildAt(2).addLexem(l2);
                    return node;
                } else {
                    System.out.println("ERROR: 'END' expected but " + s2 + " found in line:" + lexems.get(index).getRow() + ", column:" + lexems.get(index).getColumn());
                    return null;
                }
            } else {
                System.out.println("ERROR: 'statement-list' expected but " + Tables.getWordWithCode(lexems.get(index).getCode()) + " found in line:" + lexems.get(index).getRow() + ", column:" + lexems.get(index).getColumn());
                return null;
            }
        } else {
            System.out.println("ERROR: 'BEGIN' expected but " + s1 + " found in line:" + lexems.get(index).getRow() + ", column:" + lexems.get(index).getColumn());
            return null;
        }

    }

    private static Node statementlist() {
        Node buff1 = statement();
        if (buff1 != null) {
            Node buff2 = statementlist();
            if (buff2 != null) {
                Node node = new Node(-1, "<statement-list>");
                node.addChild(buff1);
                node.addChild(buff2);
                return node;
            } else {
                System.out.println("ERROR: 'statement-list' expected but " + Tables.getWordWithCode(lexems.get(index).getCode()) + " found in line:" + lexems.get(index).getRow() + ", column:" + lexems.get(index).getColumn());
                return null;
            }
        }
        buff1 = new Node(-1, "<statement-list>");
        buff1.addChild(new Node(-2, "empty"));
        return buff1;
    }

    private static Node statement() {
        if (index+1 < lexems.size()) {
            Node buff1 = varid();
            if (buff1 != null) {
                index++;
                Lexem l1 = lexems.get(index);
                int code1 = l1.getCode();
                String s1 = Tables.getWordWithCode(code1);
                if (s1.equals(":=")) {
                    index++;
                    Node buff2 = uint();
                    if (buff2 != null) {
                        index++;
                        Lexem l2 = lexems.get(index);
                        int code2 = l2.getCode();
                        String s2 = Tables.getWordWithCode(code2);
                        if (s2.equals(";")) {
                            index++;
                            Node node = new Node(-1, "<statement>");
                            node.addChild(buff1);
                            node.addChild(new Node(code1, s1));
                            node.getChildAt(1).addLexem(l1);
                            node.addChild(buff2);
                            node.addChild(new Node(code2, s2));
                            node.getChildAt(3).addLexem(l2);
                            return node;
                        } else {
                            System.out.println("ERROR: ';' expected but " + s2 + " found in line:" + lexems.get(index).getRow() + ", column:" + lexems.get(index).getColumn());
                            return null;
                        }
                    } else {
                        System.out.println("ERROR: 'unsigned-integer' expected but " + Tables.getWordWithCode(lexems.get(index).getCode()) + " found in line:" + lexems.get(index).getRow() + ", column:" + lexems.get(index).getColumn());
                        return null;
                    }
                } else {
                    index--;
                    buff1 = procid();
                    index++;
                    l1 = lexems.get(index);
                    code1 = l1.getCode();
                    s1 = Tables.getWordWithCode(code1);
                    Node buff2 = actarg();
                    if (buff2 != null) {
                        Lexem l2 = lexems.get(index);
                        int code2 = l2.getCode();
                        String s2 = Tables.getWordWithCode(code2);
                        if (s2.equals(";")) {
                            index++;
                            Node node = new Node(-1, "<statement>");
                            node.addChild(buff1);
                            node.addChild(buff2);
                            node.addChild(new Node(code1, s2));
                            node.getChildAt(2).addLexem(l2);
                            return node;
                        } else {
                            System.out.println("ERROR: ';' expected but " + s2 + " found in line:" + lexems.get(index).getRow() + ", column:" + lexems.get(index).getColumn());
                            return null;
                        }
                    }
                    System.out.println("ERROR: ':=' or 'actual-argument' expected but " + s1 + " found in line:" + lexems.get(index).getRow() + ", column:" + lexems.get(index).getColumn());
                    return null;
                }
            }
            buff1 = uint();
            if (buff1 != null) {
                index++;
                Lexem l1 = lexems.get(index);
                int code1 = l1.getCode();
                String s1 = Tables.getWordWithCode(code1);
                if (s1.equals(":")) {
                    index++;
                    Node buff2 = statement();
                    if (buff2 != null) {
                        Node node = new Node(-1, "<statement>");
                        node.addChild(buff1);
                        node.addChild(new Node(code1, s1));
                        node.getChildAt(1).addLexem(l1);
                        node.addChild(buff2);
                        return node;
                    } else {
                        System.out.println("ERROR: 'statement' expected but " + Tables.getWordWithCode(lexems.get(index).getCode()) + " found in line:" + lexems.get(index).getRow() + ", column:" + lexems.get(index).getColumn());
                        return null;
                    }
                }else{
                    System.out.println("ERROR: ':' expected but " + s1 + " found in line:" + lexems.get(index).getRow() + ", column:" + lexems.get(index).getColumn());
                    return null;
                }
            }
            Lexem l1 = lexems.get(index);
            int code1 = l1.getCode();
            String s1 = Tables.getWordWithCode(code1);
            if (s1.equals("GOTO") || s1.equals("IN") || s1.equals("OUT")) {
                index++;
                Node buff = uint();
                if (buff != null) {
                    index++;
                    Lexem l2 = lexems.get(index);
                    int code2 = l2.getCode();
                    String s2 = Tables.getWordWithCode(code2);
                    if (s2.equals(";")) {
                        index++;
                        Node node = new Node(-1, "<statement>");
                        node.addChild(new Node(code1, s1));
                        node.getChildAt(0).addLexem(l1);
                        node.addChild(buff);
                        node.addChild(new Node(code2, s2));
                        node.getChildAt(2).addLexem(l2);
                        return node;
                    } else {
                        System.out.println("ERROR: ';' expected but " + s2 + " found in line:" + lexems.get(index).getRow() + ", column:" + lexems.get(index).getColumn());
                        return null;
                    }
                } else {
                    System.out.println("ERROR: 'unsigned-integer' expected but " + Tables.getWordWithCode(lexems.get(index).getCode()) + " found in line:" + lexems.get(index).getRow() + ", column:" + lexems.get(index).getColumn());
                    return null;
                }
            } else if (s1.equals("LINK")) {
                index++;
                Node buff2 = varid();
                if (buff2 != null) {
                    index++;
                    Lexem l2 = lexems.get(index);
                    int code2 = l2.getCode();
                    String s2 = Tables.getWordWithCode(code2);
                    if (s2.equals(",")) {
                        index++;
                        Node buff3 = uint();
                        if (buff3 != null) {
                            index++;
                            Lexem l3 = lexems.get(index);
                            int code3 = l3.getCode();
                            String s3 = Tables.getWordWithCode(code3);
                            if (s3.equals(";")) {
                                index++;
                                Node node = new Node(-1, "<statement>");
                                node.addChild(new Node(code1, s1));
                                node.getChildAt(0).addLexem(l1);
                                node.addChild(buff2);
                                node.addChild(new Node(code2, s2));
                                node.getChildAt(2).addLexem(l2);
                                node.addChild(buff3);
                                node.addChild(new Node(code3, s3));
                                node.getChildAt(4).addLexem(l3);
                                return node;
                            } else {
                                System.out.println("ERROR: ';' expected but " + s3 + " found in line:" + lexems.get(index).getRow() + ", column:" + lexems.get(index).getColumn());
                                return null;
                            }
                        } else {
                            System.out.println("ERROR: 'unsigned-integer' expected but " + Tables.getWordWithCode(lexems.get(index).getCode()) + " found in line:" + lexems.get(index).getRow() + ", column:" + lexems.get(index).getColumn());
                            return null;
                        }
                    } else {
                        System.out.println("ERROR: ',' expected but " + s2 + " found in line:" + lexems.get(index).getRow() + ", column:" + lexems.get(index).getColumn());
                        return null;
                    }
                } else {
                    System.out.println("ERROR: 'var-identifier' expected but " + Tables.getWordWithCode(lexems.get(index).getCode()) + " found in line:" + lexems.get(index).getRow() + ", column:" + lexems.get(index).getColumn());
                    return null;
                }
            } else if (s1.equals("RETURN")) {
                index++;
                Lexem l2 = lexems.get(index);
                int code2 = lexems.get(index).getCode();
                String s2 = Tables.getWordWithCode(code2);
                if (s2.equals(";")) {
                    index++;
                    Node node = new Node(-1, "<statement>");
                    node.addChild(new Node(code1, s1));
                    node.getChildAt(0).addLexem(l1);
                    node.addChild(new Node(code2, s2));
                    node.getChildAt(1).addLexem(l2);
                    return node;
                } else {
                    System.out.println("ERROR: ';' expected but " + s2 + " found in line:" + lexems.get(index).getRow() + ", column:" + lexems.get(index).getColumn());
                    return null;
                }
            } else if (s1.equals(";")) {
                index++;
                Node node = new Node(-1, "<statement>");
                node.addChild(new Node(code1, s1));
                node.getChildAt(0).addLexem(l1);
                return node;
            } else if (s1.equals("($")) {
                index++;
                Node buff2 = asmf();
                if (buff2 != null) {
                    index++;
                    Lexem l2 = lexems.get(index);
                    int code2 = lexems.get(index).getCode();
                    String s2 = Tables.getWordWithCode(code2);
                    if (s2.equals("$)")) {
                        Node node = new Node(-1, "<statement>");
                        index++;
                        node.addChild(new Node(code1, s1));
                        node.getChildAt(0).addLexem(l1);
                        node.addChild(buff2);
                        node.addChild(new Node(code2, s2));
                        node.getChildAt(2).addLexem(l2);
                        return node;
                    } else {
                        System.out.println("ERROR: '$)' expected but " + s2 + " found in line:" + lexems.get(index).getRow() + ", column:" + lexems.get(index).getColumn());
                        return null;
                    }
                } else {
                    System.out.println("ERROR: 'asm-file' expected but " + Tables.getWordWithCode(lexems.get(index).getCode()) + " found in line:" + lexems.get(index).getRow() + ", column:" + lexems.get(index).getColumn());
                    return null;
                }
            }
        }
        return null;
    }

    private static Node actarg() {
        Lexem l1 = lexems.get(index);
        int code1 = l1.getCode();
        String s1 = Tables.getWordWithCode(code1);
        if (s1.equals("(")) {
            index++;
            Node buff1 = varid();
            int c2 = lexems.get(index).getCode();
            String str2 = Tables.getWordWithCode(c2);
            if (buff1 != null) {
                index++;
                Node buff2 = actargl();
                if (buff2 != null) {
                    Lexem l2 = lexems.get(index);
                    int code2 = l2.getCode();
                    String s2 = Tables.getWordWithCode(code2);
                    if (s2.equals(")")) {
                        Node node = new Node(-1, "<actual-argument>");
                        node.addChild(new Node(code1, s1));
                        node.getChildAt(0).addLexem(l1);
                        node.addChild(buff1);
                        node.addChild(buff2);
                        node.addChild(new Node(code2, s2));
                        node.getChildAt(3).addLexem(l2);
                        index++;
                        return node;
                    }
                    System.out.println("ERROR: ')' expected but " + s2 + " found in line:" + lexems.get(index).getRow() + ", column:" + lexems.get(index).getColumn());
                    return null;
                }
                System.out.println("ERROR: 'actual-argument-list' expected but " + str2 + " found in line:" + lexems.get(index).getRow() + ", column:" + lexems.get(index).getColumn());
                return null;
            }
            System.out.println("ERROR: 'var-identifier' expected but " + str2 + " found in line:" + lexems.get(index).getRow() + ", column:" + lexems.get(index).getColumn());
            return null;
        }
        Node node = new Node(-1, "<actual-argument>");
        node.addChild(new Node(-2, "empty"));
        return node;
    }

    private static Node actargl() {
        Lexem l1 = lexems.get(index);
        int code1 = l1.getCode();
        String s1 = Tables.getWordWithCode(code1);
        if (s1.equals(",")) {
            index++;
            Node buff1 = varid();
            if (buff1 != null) {
                index++;
                Node buff2 = actargl();
                if (buff2 != null) {
                    Node node = new Node(-1, "<actual-argument-list>");
                    node.addChild(new Node(code1, s1));
                    node.getChildAt(0).addLexem(l1);
                    node.addChild(buff1);
                    node.addChild(buff2);
                    return node;
                }
                int c2 = lexems.get(index).getCode();
                String str2 = Tables.getWordWithCode(c2);
                System.out.println("ERROR: 'actual-argument-list' expected but " + str2 + " found in line:" + lexems.get(index).getRow() + ", column:" + lexems.get(index).getColumn());
                return null;
            }
            int c1 = lexems.get(index).getCode();
            String str1 = Tables.getWordWithCode(c1);
            System.out.println("ERROR: 'var-identifier' expected but " + str1 + " found in line:" + lexems.get(index).getRow() + ", column:" + lexems.get(index).getColumn());
            return null;
        }
        Node node = new Node(-1, "<actual-argument-list>");
        node.addChild(new Node(-2, "empty"));
        return node;
    }

    private static Node varid() {
        Node node = id();
        if (node != null) {
            Node buff = new Node(-1, "<var-identifier>");
            buff.addChild(node);
            return buff;
        }
        return null;
    }

    private static Node procid() {
        Node node = id();
        if (node != null) {
            Node buff = new Node(-1, "<procedure-identifier>");
            buff.addChild(node);
            return buff;
        }
        return null;
    }

    private static Node asmf() {
        Node node = id();
        if (node != null) {
            Node buff = new Node(-1, "<asm-file>");
            buff.addChild(node);
            return buff;
        }
        return null;
    }

    private static Node id() {
        Node node;
        Lexem l = lexems.get(index);
        int code = l.getCode();
        if (code >= 1000) {
            node = new Node(-1, "<identifier>");
            node.addTerm(code, Tables.getWordWithCode(code));
            node.addLexem(l);
            return node;
        } else {
            return null;
        }
    }

    private static Node uint() {
        Node node;
        Lexem l = lexems.get(index);
        int code = l.getCode();
        if (code >= 500 && code < 1000) {
            node = new Node(-1, "<unsigned-integer>");
            node.addTerm(code, Tables.getWordWithCode(code));
            node.addLexem(l);
            return node;
        } else {
            return null;
        }
    }
}
