package ch.kbw.ag;

import java.io.*;
import java.util.Scanner;

public class Main {

    public static void main(String args[]) throws IOException {
        //Pfad zur Eingabedatei für Dateneingabe
        String path = System.getProperty("user.dir") + "/src/ch/kbw/ag/input.txt";
        String s1 = readFile(path, "UTF-8");
        System.out.println("Orginal: " + s1);
        //Dateneingabe in Bytes umwandeln
        byte[] bytes = s1.getBytes("UTF-8");
        //Bytes als Binary String umwandeln
        String binaryString = convertToBinaryString(bytes);
        System.out.println(binaryString);

        //Daten Array erstellen mit Grösse des Binary String
        int size = binaryString.length();
        int daten[] = new int[size];
        //Binary String übergibt Bits dem Daten Array
        for (int i = 0; i < size; i++) {
            daten[i] = binaryString.charAt(i) - '0';
        }

        //Scanner erstellen, um die Eingaben vom Benutzer entgegenzunehmen
        Scanner scan = new Scanner(System.in);
        //Groesse des Divisors vom Benutzer erfassen
        System.out.println("Geben Sie die Anzahl Bits des Divisors an:");
        size = scan.nextInt();
        int divisor[] = new int[size];
        //Bits des Divisors vom Benutzer erfassen
        System.out.println("Geben Sie die Bits des Divisors nacheinander ein:");
        for (int i = 0; i < size; i++) {
            System.out.println("Enter bit " + (size - i) + ":");
            divisor[i] = scan.nextInt();
        }
        //Daten Array mit dem Divisor Array dividieren und das Resultat im Array speichern
        int res[] = dividiereDatenMitDivisor(daten, divisor);
        //Resultat in der Konsole ausgeben
        for (int i = 0; i < res.length - 1; i++) {
            System.out.print(res[i]);
        }

        //CRC Code ausgeben
        System.out.println("\nGenerierter CRC code ist: ");

        for (int i = 0; i < daten.length; i++) {
            System.out.print(daten[i]);
        }
        for (int i = 0; i < res.length - 1; i++) {
            System.out.print(res[i]);
        }
        System.out.println();
        System.out.println("Möchtest du das CRC korrekt beim empfänger ankommen lassen? [y/N]");
        String antwort = scan.next();
        System.out.println(antwort);
        if(antwort.equals("y")){
            int counter = 0;
            int gesendeteDaten[] = new int[daten.length + res.length - 1];
            for (int i = 0; i < daten.length; i++) {
                gesendeteDaten[counter] = daten[i];
                counter++;
            }
            for (int i = 0; i < res.length - 1; i++) {
                gesendeteDaten[counter] = res[i];
                counter++;
            }
            erhaltenDaten(gesendeteDaten, divisor);
        }else{
            // Die Nachricht, welche beim Empfaenger angkommt vom Benutzer in der Konsole einlesen
            int gesendeteDaten[] = new int[daten.length + res.length - 1];
            System.out.println("Geben Sie die Bits in das Feld ein, die Sie senden möchten: ");
            for (int i = 0; i < gesendeteDaten.length; i++) {
                System.out.println("Gib Bit ein " + (gesendeteDaten.length - 1) + ":");
                gesendeteDaten[i] = scan.nextInt();
            }
            erhaltenDaten(gesendeteDaten, divisor);
        }


    }

    //Bytes in Binary String umwandeln
    static String convertToBinaryString(byte[] bytes) throws UnsupportedEncodingException {

        StringBuffer buffer = new StringBuffer();
        for (int b : bytes) {
            buffer.append(Integer.toBinaryString((b + 256) % 256));
            buffer.append("");
        }
        return buffer.toString();

    }

    //Eingabedatei lesen
    static String readFile(String filePath, String encoding) throws IOException {
        File file = new File(filePath);
        StringBuffer buffer = new StringBuffer();
        try (InputStreamReader isr = new InputStreamReader(new FileInputStream(file), encoding)) {
            int daten;
            while ((daten = isr.read()) != -1) {
                buffer.append((char) daten);
            }
        }
        return buffer.toString();
    }


    // CRC code mit dem CRC verfahren ausrechnen
    static int[] dividiereDatenMitDivisor(int alteDaten[], int divisor[]) {
        // res[] array deklariernen in dem der Rest gespeichert wird
        int res[] = new int[divisor.length];
        int i;
        int daten[] = new int[alteDaten.length + divisor.length];
        // mit System.arraycopy() Daten in res und daten Array kopieren
        System.arraycopy(alteDaten, 0, daten, 0, alteDaten.length);
        System.arraycopy(daten, 0, res, 0, divisor.length);
        // alteDaten iterieren und mit exor die bits und den Divisor verrechenen
        for (i = 0; i < alteDaten.length; i++) {
            System.out.println((i + 1) + ".) Erstes Daten Bit ist : " + res[0]);
            System.out.print("Rest : ");
            if (res[0] == 1) {
                // Mit exor Rest und Divisor verrechnen
                for (int j = 1; j < divisor.length; j++) {
                    res[j - 1] = exorOperation(res[j], divisor[j]);
                    System.out.print(res[j - 1]);
                }
            } else {
                // Die Rest Bits mit 0 mit der exorOperation verrechnen
                for (int j = 1; j < divisor.length; j++) {
                    res[j - 1] = exorOperation(res[j], 0);
                    System.out.print(res[j - 1]);
                }
            }
            // Der letzte bit des Restes wird von Daten geholt
            res[divisor.length - 1] = daten[i + divisor.length];
            System.out.println(res[divisor.length - 1]);
        }
        return res;
    }

    //exorOperation() erstellen
    static int exorOperation(int x, int y) {
        //Exor
        if (x == y) {
            return 0;
        }
        return 1;
    }

    // Methode um erhaltene Daten auszugeben
    static void erhaltenDaten(int daten[], int divisor[]) {

        int res[] = dividiereDatenMitDivisor(daten, divisor);
        //Division ist fertig
        for (int i = 0; i < res.length; i++) {
            if (res[i] != 0) {
                System.out.println("Korrupte Daten bekommen");
                return;
            }
        }
        System.out.println("Daten bekommen ohne Fehler.");
    }
}
