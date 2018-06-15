import java.util.ArrayList;

public class TableCreator {

    public static byte[] getAttributesTable() {
        //0 - whitespaces
        //1 - const
        //2 - identificator
        //3 - delimiters
        //4 - multidelimiters
        //5 - unacceptable
        byte[] attributes = new byte[128];
        for (int i = 0; i < 8; i++) {
            attributes[i] = 5;
        }
        attributes[8] = 0;
        attributes[9] = 0;
        attributes[10] = 0;
        attributes[11] = 5;
        attributes[12] = 5;
        attributes[13] = 0;
        for (int i = 14; i < 32; i++) {
            attributes[i] = 5;
        }
        attributes[32] = 0;//' '
        attributes[33] = 5;//'!'
        attributes[34] = 5;//'"'
        attributes[35] = 5;//'#'
        attributes[36] = 4;//'$'
        attributes[37] = 5;//%
        attributes[38] = 5;//&
        attributes[39] = 5;//'
        attributes[40] = 4;//(
        attributes[41] = 3;//)
        attributes[42] = 5;//*
        attributes[43] = 5;//+
        attributes[44] = 3;//,
        attributes[45] = 5;//-
        attributes[46] = 3;//.
        attributes[47] = 5;///
        attributes[48] = 1;//0
        attributes[49] = 1;//1
        attributes[50] = 1;//2
        attributes[51] = 1;//3
        attributes[52] = 1;//4
        attributes[53] = 1;//5
        attributes[54] = 1;//6
        attributes[55] = 1;//7
        attributes[56] = 1;//8
        attributes[57] = 1;//9
        attributes[58] = 4;//:
        attributes[59] = 3;//;
        attributes[60] = 5;//<
        attributes[61] = 5;//=
        attributes[62] = 5;//>
        attributes[63] = 5;//?
        attributes[64] = 5;//@
        attributes[65] = 2;//A
        attributes[66] = 2;//B
        attributes[67] = 2;//C
        attributes[68] = 2;//D
        attributes[69] = 2;//E
        attributes[70] = 2;//F
        attributes[71] = 2;//G
        attributes[72] = 2;//H
        attributes[73] = 2;//I
        attributes[74] = 2;//J
        attributes[75] = 2;//K
        attributes[76] = 2;//L
        attributes[77] = 2;//M
        attributes[78] = 2;//N
        attributes[79] = 2;//O
        attributes[80] = 2;//P
        attributes[81] = 2;//Q
        attributes[82] = 2;//R
        attributes[83] = 2;//S
        attributes[84] = 2;//T
        attributes[85] = 2;//U
        attributes[86] = 2;//V
        attributes[87] = 2;//W
        attributes[88] = 2;//X
        attributes[89] = 2;//Y
        attributes[90] = 2;//Z
        attributes[91] = 5;//[
        attributes[92] = 5;//\
        attributes[93] = 5;//]
        attributes[94] = 5;//^
        attributes[95] = 5;//_
        attributes[96] = 5;//`
        attributes[97] = 5;//a
        attributes[98] = 5;//b
        attributes[99] = 5;//c
        attributes[100] = 5;//d
        attributes[101] = 5;//e
        attributes[102] = 5;//f
        attributes[103] = 5;//g
        attributes[104] = 5;//h
        attributes[105] = 5;//i
        attributes[106] = 5;//j
        attributes[107] = 5;//k
        attributes[108] = 5;//l
        attributes[109] = 5;//m
        attributes[110] = 5;//n
        attributes[111] = 5;//o
        attributes[112] = 5;//p
        attributes[113] = 5;//q
        attributes[114] = 5;//r
        attributes[115] = 5;//s
        attributes[116] = 5;//t
        attributes[117] = 5;//u
        attributes[118] = 5;//v
        attributes[119] = 5;//w
        attributes[120] = 5;//q
        attributes[121] = 5;//y
        attributes[122] = 5;//z
        attributes[123] = 5;//{
        attributes[124] = 5;//|
        attributes[125] = 5;//}
        attributes[126] = 5;//~
        attributes[127] = 5;//DEL
        return attributes;
    }


    public static ArrayList<WordCode> getDelimitersTable() {
        ArrayList<WordCode> delimitersTable = new ArrayList<>();
        delimitersTable.add(new WordCode(null, -1));
        delimitersTable.add(new WordCode(":", 0));
        delimitersTable.add(new WordCode(";", 1));
        delimitersTable.add(new WordCode(",", 2));
        delimitersTable.add(new WordCode("(", 3));
        delimitersTable.add(new WordCode(")", 4));
        delimitersTable.add(new WordCode(".", 5));
        return delimitersTable;
    }

    public static ArrayList<WordCode> getMultidelimitersTable() {
        ArrayList<WordCode> multidelimetersTable = new ArrayList<>();
        multidelimetersTable.add(new WordCode(null, 300));
        multidelimetersTable.add(new WordCode(":=", 301));
        multidelimetersTable.add(new WordCode("($", 302));
        multidelimetersTable.add(new WordCode("$)", 303));
        return multidelimetersTable;
    }

    public static ArrayList<WordCode> getKeywordsTable() {
        ArrayList<WordCode> keywordsTable = new ArrayList<>();
        keywordsTable.add(new WordCode(null, 400));
        keywordsTable.add(new WordCode("PROGRAM", 401));
        keywordsTable.add(new WordCode("BEGIN", 402));
        keywordsTable.add(new WordCode("GOTO", 403));
        keywordsTable.add(new WordCode("LINK", 404));
        keywordsTable.add(new WordCode("IN", 405));
        keywordsTable.add(new WordCode("OUT", 406));
        keywordsTable.add(new WordCode("RETURN", 407));
        keywordsTable.add(new WordCode("END", 408));
        keywordsTable.add(new WordCode(":=", 409));
        keywordsTable.add(new WordCode("($", 410));
        return keywordsTable;
    }

    public static ArrayList<WordCode> getConstantsTable() {
        ArrayList<WordCode> constantsTable = new ArrayList<>();
        constantsTable.add(new WordCode(null, 500));
        return constantsTable;
    }

    public static ArrayList<WordCode> getIdentifiersTable() {
        ArrayList<WordCode> identifiersTable = new ArrayList<>();
        identifiersTable.add(new WordCode(null, 1000));
        return identifiersTable;
    }

}
