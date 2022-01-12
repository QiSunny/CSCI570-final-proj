import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Basic_1473192762_5755414997_7894395015 {
    final static int delta = 30;
    static int pGap = 30;
    static int pAC = 110;
    static int pAG = 48;
    static int pAT = 94;
    static int pCG = 118;
    static int pCT = 48;
    static int pGT = 110;


    public static void main(String[] args){

        Runtime r = Runtime.getRuntime();
        Scanner in = new Scanner(System.in);
        if(args.length>0){
            File inFile = new File(args[0]);
            try{
                in = new Scanner(inFile);
            } catch (FileNotFoundException e){
                in = new Scanner(System.in);
            }
        }



        StringGenerator generator = new StringGenerator(in);
        String s1 = generator.getString1();
        String s2 = generator.getString2();


        r.gc();
        long startMem = r.totalMemory() - r.freeMemory();
        long startTime = System.currentTimeMillis();
        dp(s1, s2);
        long endMem = r.totalMemory() - r.freeMemory();
        long endTime = System.currentTimeMillis();
        System.out.println((endTime - startTime)/1000.0);
        System.out.println((endMem-startMem)/1024.0);
    }


    public static String[] dp(String s1, String s2) {
        int len1 = s1.length();
        int len2 = s2.length();

        int[][] dp = new int[len1 +len2 + 1][len1 +len2 + 1];

        for(int i = 0; i <= len1 + len2; i++) {
            dp[i][0] = i * pGap;
            dp[0][i] = i * pGap;
        }

        for(int i = 1; i <= len1; i++) {
            for(int j = 1; j <= len2; j++) {
                if(s1.charAt(i - 1) == s2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                }else {
                    fillDP(dp, s1, i, s2, j);
                }
            }
        }


        int len = len1 + len2;
        int i = len1;
        int j = len2;
        int pos1 = len;
        int pos2 = len;

        char[] newS1 = new char[len + 1];
        char[] newS2 = new char[len + 1];
        while(i != 0 && j != 0) {
            if(s1.charAt(i - 1) == s2.charAt(j - 1)) {
                newS1[pos1--] = s1.charAt(i - 1);
                newS2[pos2--] = s2.charAt(j - 1);
                i--;
                j--;
            }else if(dp[i - 1][j] + pGap == dp[i][j]) {
                newS1[pos1--] = s1.charAt(i - 1);
                newS2[pos2--] = '_';
                i--;
            }else if(dp[i][j - 1] + pGap == dp[i][j]) {
                newS1[pos1--] = '_';
                newS2[pos2--] = s2.charAt(j - 1);
                j--;
            }else {
                newS1[pos1--] = s1.charAt(i - 1);
                newS2[pos2--] = s2.charAt(j - 1);
                i--;
                j--;
            }
        }
        while(pos1 > 0) {
            if(i > 0) {
                newS1[pos1--] = s1.charAt(--i);
            }else {
                newS1[pos1--] = '_';
            }
        }
        while(pos2 > 0) {
            if(j > 0) {
                newS2[pos2--] = s2.charAt(--j);
            }else {
                newS2[pos2--] = '_';
            }
        }

        int ind = 1;
        for(int t = len; t >= 1; t--) {
            if(newS2[t] == '_' && newS1[t] == '_') {
                ind = t + 1;
                break;
            }
        }

        String res1 = new String(newS1, ind, newS1.length - ind);
        String res2 = new String(newS2, ind, newS2.length - ind);
        System.out.println(res1);
        System.out.println(res2);
        System.out.println((float) dp[len1][len2]);
        String[] res = new String[2];
        res[0] = res1;
        res[1] = res2;
        return res;
    }

    private static void fillDP(int[][] dp, String s1, int i, String s2, int j) {
        if((s1.charAt(i - 1) == 'A' && s2.charAt(j - 1) == 'C') || (s1.charAt(i - 1) == 'C' && s2.charAt(j - 1) == 'A')) {
            dp[i][j] = Math.min(Math.min(dp[i - 1][j - 1] + pAC ,
                            dp[i - 1][j] + pGap) ,
                    dp[i][j - 1] + pGap );
        }else if((s1.charAt(i - 1) == 'A' && s2.charAt(j - 1) == 'G') || (s1.charAt(i - 1) == 'G' && s2.charAt(j - 1) == 'A')) {
            dp[i][j] = Math.min(Math.min(dp[i - 1][j - 1] + pAG ,
                            dp[i - 1][j] + pGap) ,
                    dp[i][j - 1] + pGap );
        }else if((s1.charAt(i - 1) == 'A' && s2.charAt(j - 1) == 'T') || (s1.charAt(i - 1) == 'T' && s2.charAt(j - 1) == 'A')) {
            dp[i][j] = Math.min(Math.min(dp[i - 1][j - 1] + pAT ,
                            dp[i - 1][j] + pGap) ,
                    dp[i][j - 1] + pGap );
        }else if((s1.charAt(i - 1) == 'C' && s2.charAt(j - 1) == 'G') || (s1.charAt(i - 1) == 'G' && s2.charAt(j - 1) == 'C')) {
            dp[i][j] = Math.min(Math.min(dp[i - 1][j - 1] + pCG ,
                            dp[i - 1][j] + pGap) ,
                    dp[i][j - 1] + pGap );
        }else if((s1.charAt(i - 1) == 'C' && s2.charAt(j - 1) == 'T') || (s1.charAt(i - 1) == 'T' && s2.charAt(j - 1) == 'C')) {
            dp[i][j] = Math.min(Math.min(dp[i - 1][j - 1] + pCT ,
                            dp[i - 1][j] + pGap) ,
                    dp[i][j - 1] + pGap );
        }else if((s1.charAt(i - 1) == 'G' && s2.charAt(j - 1) == 'T') || (s1.charAt(i - 1) == 'T' && s2.charAt(j - 1) == 'G')) {
            dp[i][j] = Math.min(Math.min(dp[i - 1][j - 1] + pGT ,
                            dp[i - 1][j] + pGap) ,
                    dp[i][j - 1] + pGap );
        }
    }

    public static class StringGenerator {
        private Scanner in;
        private String string1;
        private String string2;
        StringGenerator(Scanner in){
            this.in = in;
            generate();
        }

        public void generate(){
            if(in.hasNext()){
                string1 = in.next();
            }
            while(in.hasNextInt()){
                int offset = in.nextInt();
                string1 = add(string1,offset+1);
            }
            if(in.hasNext()){
                string2 = in.next();
            }
            while(in.hasNextInt()){
                int offset = in.nextInt();
                string2 = add(string2,offset+1);
            }
        }

        private String add(String s, int index){
            StringBuilder sb = new StringBuilder(s);
            sb.insert(index,s);
            return sb.toString();
        }

        public String getString1(){
            return string1;
        }

        public String getString2(){
            return string2;
        }
    }



}
