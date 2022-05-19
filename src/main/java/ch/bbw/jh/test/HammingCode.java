package ch.bbw.jh.test;

/**
 * HammingCode
 *
 * @author Jorin Heer, Aron Gassner
 * @version 2022
 */

public class HammingCode {

    private static int finalLoc;
    private static String after = "";
    private String finalStr = null;
    private int N;
    private int n;
    private int r;
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
        r = 1;

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

        r = N - n;
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

    static void receiveData(int data[], int parityBits) {

        // declare variable pow, which we use to get the correct bits to check for parity.
        int pow;
        int size = data.length;
        // declare parityArray to store the value of parity check
        int parityArray[] = new int[parityBits];
        // we use errorLoc string for storing the integer value of the error location.
        String errorLoc = new String();
        // use for loop to check the parities
        for(pow = 0; pow < parityBits; pow++) {
            // use for loop to extract the bit from 2^(power)
            for(int i = 0; i < size; i++) {
                int j = i + 1;
                // convert the value of j into binary
                String str = Integer.toBinaryString(j);
                // find bit by using str
                int bit = ((Integer.parseInt(str)) / ((int) Math.pow(10, pow))) % 10;
                if(bit == 1) {
                    if(data[i] == 1) {
                        parityArray[pow] = (parityArray[pow] + 1) % 2;
                    }
                }
            }
            errorLoc = parityArray[pow] + errorLoc;
        }
        // This gives us the parity check equation values.
        // Using these values, we will now check if there is a single bit error and then correct it.
        // errorLoc provides parity check eq. values which we use to check whether a single bit error is there or not
        // if present, we correct it
        finalLoc = Integer.parseInt(errorLoc, 2);
        // check whether the finalLoc value is 0 or not
        if(finalLoc != 0) {
            after = "Fehler wurde gefunden bei Position " + finalLoc + "." + "\n";
            data[finalLoc - 1] = (data[finalLoc - 1] + 1) % 2;
            after = after + "Nach dem Korrigieren des Fehlers ist das Codewort jetzt:" + "\n";
            for(int i = 0; i < size; i++) {
                after = after + data[size - i - 1] ;
            }
            after = after + "\n";
        }

        else {
            after = after + "Es wurde kein Fehler gefunden." + "\n";
        }
        // print the original data
        after = after + "Die Originalnachricht vom Sender:" + "\n";
        pow = parityBits - 1;
        for(int k = size; k > 0; k--) {
            if(Math.pow(2, pow) != k) {
                after = after + data[k - 1];
            }
            else {
                // decrement value of pow
                pow--;
            }
        }
        after = after + "\n";

    }

    public int getFinalLoc() {
        return finalLoc;
    }

    public void setN(int N) {
        this.N = N;
    }

    public void setn(int n) {
        this.n = n;
        System.out.println(n + "");
    }

    public int getR() {
        return r;
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

    public String getAfter() {
        return after;
    }
}
