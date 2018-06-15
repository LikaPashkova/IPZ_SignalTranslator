import java.util.ArrayList;

public class Tables {
    private static byte[] attributesTable = TableCreator.getAttributesTable();
    private static ArrayList<WordCode> delimitersTable = TableCreator.getDelimitersTable();
    private static ArrayList<WordCode> multidelimitersTable = TableCreator.getMultidelimitersTable();
    private static ArrayList<WordCode> keywordsTable = TableCreator.getKeywordsTable();
    private static ArrayList<WordCode> constantsTable = TableCreator.getConstantsTable();
    private static ArrayList<WordCode> identifiersTable = TableCreator.getIdentifiersTable();

    public static byte groupOfAttribute(int ch){
        return attributesTable[0+ch];
    }

    public static int codeOfDelimiter(char ch) {
        int index = -1;
        for (int i = 1; i < delimitersTable.size() && index == -1; i++) {
            if (delimitersTable.get(i).getValue().equals("" + ch)) {
                index = i;
            }
        }
        if(index != -1) {
            return delimitersTable.get(index).getCode();
        }else{
            return -1;
        }
    }

    public static int codeOfMultidelimiter(String s) {
        int index = -1;
        for(int i=1; i<multidelimitersTable.size()&&index==-1;i++){
            if(multidelimitersTable.get(i).getValue().equals(s)){
                index=i;
            }
        }
        if(index != -1) {
            return multidelimitersTable.get(index).getCode();
        }else{
            return -1;
        }
    }

    public static int codeOfKeyword(String s){
        int index = -1;
        for(int i=1; i<keywordsTable.size()&&index==-1;i++){
            if(keywordsTable.get(i).getValue().equals(s)){
                index=i;
            }
        }
        if(index!=-1) {
            return keywordsTable.get(index).getCode();
        }else{
            return -1;
        }
    }

    public static int codeOfConstant(String s){
        int index = -1;
        int i=1;
        while(i<constantsTable.size()&&index==-1){
            if(constantsTable.get(i).getValue().equals(s)){
                index=constantsTable.get(i).getCode();
            }
            i++;
        }
        if(index==-1){
            index = constantsTable.get(i-1).getCode()+1;
            constantsTable.add(new WordCode(s, index));
        }
        return index;
    }

    public static int codeOfIdentifier(String s){
        int index = -1;
        int i=1;
        while(i<identifiersTable.size()&&index==-1){
            if(identifiersTable.get(i).getValue().equals(s)){
                index=i+1000;
            }
            i++;
        }
        if(index==-1){
            index = identifiersTable.get(i-1).getCode()+1;
            identifiersTable.add(new WordCode(s, index));
        }
        return index;
    }

    public static String getWordWithCode(int code){
        if(code>=0) {
            if (code < 300){
                return delimitersTable.get(code+1).getValue();
            }else if (code < 400){
                return multidelimitersTable.get(code-300).getValue();
            }else if (code < 500){
                return keywordsTable.get(code-400).getValue();
            }else if (code < 1000){
                return constantsTable.get(code - 500).getValue();
            }else{
                return identifiersTable.get(code - 1000).getValue();
            }
        }else{
            return null;
        }
    }

    public static String outTables(){
        String s = "";
        s = "Delimiters Table:\n"+ delimitersTable.toString()+"\n";
        s = s+"\nMultidelimiters Table:\n"+multidelimitersTable.toString()+"\n";
        s = s+"\nKeywords Table:\n"+keywordsTable.toString()+"\n";
        s = s+"\nConstants Table:\n"+constantsTable.toString()+"\n";
        s = s+"\nIdentifiers Table:\n"+identifiersTable.toString()+"\n";
        return s;
    }

}
