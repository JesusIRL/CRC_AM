package ch.bbw.jh.test;

/**
 * HammingCode
 *
 * @author Jorin Heer, Aron Gassner
 * @version 2022
 */

public class HammingCode {

    private String finalStr = null;
    private int N;
    private int n;
    private String noParity;

    /**
     * calculating value of redundant bits
     *
     * Teile dieses Codes wurden von https://www.geeksforgeeks.org/hamming-code-implementation-in-java/ übernommen
     * und angepasst. Abfragedatum: 08.05.2022. https://www.geeksforgeeks.org/hamming-code-implementation-in-java/ Jahr 2020.
     */
    static int[] calculation(int[] ar, int r) {
        System.out.print("ar: ");
        for (int i = 0; i < ar.length; i++) {
            System.out.print(ar[i]);
        }
        System.out.println();
        for (int i = 0; i < r; i++) {
            int x = (int) Math.pow(2, i);
            for (int j = 1; j < ar.length; j++) {
                if (((j >> i) & 1) == 1) {
                    if (x != j) {
                        ar[x] = ar[x] ^ ar[j];
                    }
                }
            }
            System.out.println("r" + x + " = "
                    + ar[x]);
        }
        return ar;
    }

    /**
     * Teile dieses Codes wurden von https://www.geeksforgeeks.org/hamming-code-implementation-in-java/ übernommen
     * und angepasst. Abfragedatum: 08.05.2022. https://www.geeksforgeeks.org/hamming-code-implementation-in-java/ Jahr 2020.
     */
    static int[] generateCode(String str, int n, int r) {
        int[] ar = new int[r + n + 1];
        int j = 0;
        for (int i = 1; i < ar.length; i++) {
            if ((Math.ceil(Math.log(i) / Math.log(2))
                    - Math.floor(Math.log(i) / Math.log(2)))
                    == 0) {

                // if i == 2^n for n in (0, 1, 2, .....)
                // then ar[i]=0
                // codeword[i] = 0 ----
                // redundant bits are initialized
                // with value 0
                ar[i] = 0;
            } else {

                // codeword[i] = dataword[j]
                ar[i] = (int) (str.charAt(j) - '0');
                j++;
            }
        }
        return ar;
    }

    /**
     * Teile dieses Codes wurden von https://www.geeksforgeeks.org/hamming-code-implementation-in-java/ übernommen
     * und angepasst. Abfragedatum: 08.05.2022. https://www.geeksforgeeks.org/hamming-code-implementation-in-java/ Jahr 2020.
     */
    public void autoHammingCode(String str) {

        // input message
        int N = str.length();
        int r = 1;

        while (Math.pow(2, r) < (N + r + 1)) {
            // r is number of redundant bits
            r++;
        }
        int[] ar = generateCode(str, N, r);

        System.out.println("Generated hamming code ");
        ar = calculation(ar, r);
        for (int i = 1; i < ar.length; i++) {
            if (finalStr == null) {
                finalStr = "" + ar[i];
            } else {
                finalStr = finalStr + ar[i];
            }
        }
    }

    /**
     * Teile dieses Codes wurden von https://www.geeksforgeeks.org/hamming-code-implementation-in-java/ übernommen
     * und angepasst. Abfragedatum: 08.05.2022. https://www.geeksforgeeks.org/hamming-code-implementation-in-java/ Jahr 2020.
     */
    public void hammingCode(String str) {

        int r = N - n;
        int O;

        if (str.length() > str.length() / n * n) {
            O = str.length() / n + 1;
            int j = str.length();
            for (int i = 0; i < str.length() - str.length() / n * n; i++) {
                // fügt Nullen hinten hinzu
                str = str + "0";
            }
        } else {
            O = str.length() / n;
        }
        noParity = str;
        System.out.println("noParity: " + str);
        System.out.println(O);
        String[] strAr = new String[O];
        for (int i = 0; i < O; i++) {
            for (int j = 0; j < n; j++) {
                if (strAr[i] == null) {
                    strAr[i] = Character.toString(str.charAt(i * n + j));
                } else {
                    strAr[i] = strAr[i] + Character.toString(str.charAt(i * n + j));
                }
            }
        }
        for (int i = 0; i < O; i++) {
            System.out.println("strAr[" + i + "]:" + strAr[i]);
        }
        int[][] ar = new int[O][];
        for (int i = 0; i < O; i++) {
            ar[i] = generateCode(strAr[i], n, r);
            System.out.println();
            System.out.println("Generated hamming code ");
            ar[i] = calculation(ar[i], r);
        }

        System.out.println(ar.length);
        for (int i = 0; i < ar.length; i++) {
            for (int j = 1; j < ar[i].length; j++) {
                if (finalStr == null) {
                    finalStr = "" + ar[i][j];
                } else {
                    finalStr = finalStr + ar[i][j];
                }
            }
        }
        System.out.println(finalStr);
    }

    public void setN(int N) {
        this.N = N;
    }

    public void setn(int n) {
        this.n = n;
        System.out.println(n + "");
    }

    public void setFinalStr(String finalStr) {
        this.finalStr = finalStr;
    }

    public String getFinalStr() {
        return finalStr;
    }

    public String getNoParity() {
        return noParity;
    }
}
