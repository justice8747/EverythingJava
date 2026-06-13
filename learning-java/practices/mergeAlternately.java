package practice;

public class mergeAlternately {
    public static String mergeAlternately(String word1, String word2) {
        StringBuffer str1 = new StringBuffer(word1);
        StringBuffer str2 = new StringBuffer(word2);
        StringBuffer newStr = new StringBuffer();
        for(int i =0; i<word1.length(); i++){

            newStr.append(str1.charAt(0));
            str1.replace(0,1, "");
            System.out.println(str1);
            if(!str2.isEmpty()){
                newStr.append(str2.charAt(0));
                str2.replace(0,1, "");
            }
            System.out.println(str2);
        }

        if(!str2.isEmpty()){
            newStr.append(str2);
        }

        return newStr.toString();

    }

    public static void main(String[] args) {
        System.out.println(mergeAlternately("abcuiu", "pqr"));
    }

}
