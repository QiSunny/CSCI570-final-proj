import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Efficient_1473192762_5755414997_7894395015 {
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
        String[] res = divideConquerAlignment(s1,s2);
        long endMem = r.totalMemory() - r.freeMemory();
        long endTime = System.currentTimeMillis();

        for (String s : res) {
            System.out.println(s);
        }
        System.out.println((float) calcPenalty(res[0],res[1]));
        System.out.println((endTime - startTime)/1000.0);
        System.out.println((endMem-startMem)/1024.0);
    }

    public static String[] divideConquerAlignment(String fst,String snd) {
        int m = fst.length();
        int n = snd.length();
        if (m < 2 || n < 2) {
            return helper(fst, snd);
        }

        int[] firstHalf = spaceEfficientAlignment(fst, snd.substring(0, n / 2));
        String fstReverse = new StringBuilder(fst).reverse().toString();
        String sndReverse = new StringBuilder(snd.substring(n / 2)).reverse().toString();
        int[] secondHalf = spaceEfficientAlignment(fstReverse, sndReverse);
        int q = getIndex(firstHalf, secondHalf);

        String[] left = divideConquerAlignment(fst.substring(0, q), snd.substring(0, n / 2));
        String[] right = divideConquerAlignment(fst.substring(q, m), snd.substring(n / 2, n));
        String[] res = new String[2];
        res[0] = left[0] + right[0];
        res[1] = left[1] + right[1];


        return res;
    }

    /**
     * Calculate the index, q, minimizing f(q, n / 2) + g(q, n / 2)
     *
     * @param fstHalf the result that space efficient alignment returns
     * @param sndHalf the result that backward space efficient alignment returns
     * @return q
     */
    private static int getIndex(int[] fstHalf, int[] sndHalf) {
        int m = fstHalf.length;
        int n = sndHalf.length;
        if (m != n) {
            return -1;
        }

        int index = 0;
        int min = Integer.MAX_VALUE;
        for(int i = 0; i < m; i++) {
            if((fstHalf[i] + sndHalf[m - i - 1]) < min) {
                min = fstHalf[i] + sndHalf[m - i - 1];
                index = i;
            }
        }
        return index;
    }



    private static int calcPenalty(String s1, String s2){
        if(s1.length()!=s2.length()){
            return 0;
        }
        int result=0;
        for(int i=0; i<s1.length();i++){
            char cur1 = s1.charAt(i);
            char cur2 = s2.charAt(i);
            if(cur1=='_'&&cur2=='_'){
                result+=0;
            } else if(cur1=='_'||cur2=='_'){
                result+=delta;
            } else {
                result+=alpha(s1.charAt(i),s2.charAt(i));
            }
        }
        return result;
    }



    /**
     * Space efficient alignment
     *
     * @param fst first string
     * @param snd snd string
     * @return arr
     */
    private static int[] spaceEfficientAlignment(String fst, String snd) {
        int m = fst.length();
        int n = snd.length();
        int[] res = new int[m + 1];
        int[][] opt = new int[m + 1][2];

        for (int i = 0; i <= m; i++) {
            opt[i][0] = i * delta;
        }
        for (int j = 1; j <= n; j++) {
            opt[0][1] = j * delta;
            for (int i = 1; i <= m; i++) {
                int min = Math.min(opt[i][0] + delta, opt[i - 1][1] + delta);
                opt[i][1] = Math.min(min, opt[i - 1][0] + alpha(fst.charAt(i - 1), snd.charAt(j - 1)));
            }

            for (int i = 0; i <= m; i++) {
                opt[i][0] = opt[i][1];
                res[i] = opt[i][0];
            }
        }
        return res;
    }

    private static int alpha(char fst, char snd) {
        String combination = Character.toString(Character.toUpperCase(fst)) + Character.toUpperCase(snd);
        if (combination.compareTo("AA") == 0 || combination.compareTo("CC") == 0 ||
                combination.compareTo("GG") == 0 || combination.compareTo("TT") == 0) {
            return 0;
        } else if (combination.compareTo("AC") == 0 || combination.compareTo("CA") == 0 ||
                combination.compareTo("GT") == 0 || combination.compareTo("TG") == 0) {
            return 110;
        } else if (combination.compareTo("AG") == 0 || combination.compareTo("GA") == 0 ||
                combination.compareTo("CT") == 0 || combination.compareTo("TC") == 0) {
            return 48;
        } else if (combination.compareTo("AT") == 0 || combination.compareTo("TA") == 0) {
            return 94;
        } else if (combination.compareTo("GC") == 0 || combination.compareTo("CG") == 0) {
            return 118;
        }
        return -1;
    }

    public static String[] helper(String s1, String s2) {
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
