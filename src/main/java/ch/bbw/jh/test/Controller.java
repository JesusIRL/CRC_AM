package ch.bbw.jh.test;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import java.io.*;
import java.text.BreakIterator;
import java.util.Scanner;
/**
 * @author Jorin Heer, Aron Gassner
 * @version 2022
 */
public class Controller  {


    private Model myModel;
    @FXML
    private TextArea input;
    @FXML
    private TextField output;
    @FXML
    private TextField aenderungspos;
    @FXML
    private TextArea whatsgoingon;
    @FXML
    private CheckBox cbx;

    public void setModel(Model myModel) {
        this.myModel = myModel;
        myModel.setInput("");
        myModel.setOutput("");
        myModel.setWhatsgoingon("");
    }
    @FXML
    private void Initialize(){
        input.setText("");
        output.setText("");
        whatsgoingon.setText("");
    }

    @FXML
    public void clickCRC() throws IOException{
        try {
            myModel.setWhatsgoingon("");
            myModel.setInput(input.getText());
            String s1 = myModel.getInput();
            myModel.setWhatsgoingon(myModel.getWhatsgoingon()+"Original: "+ s1+"\n");
            //Dateneingabe in Bytes umwandeln
            byte[] bytes = s1.getBytes("UTF-8");
            //Bytes als Binary String umwandeln
            String binaryString = convertToBinaryString(bytes);
            myModel.setWhatsgoingon(myModel.getWhatsgoingon()+"Original: "+ binaryString+"\n");

            //Daten Array erstellen mit Grösse des Binary String
            int size = binaryString.length();
            int daten[] = new int[size];
            //Binary String übergibt Bits dem Daten Array
            for (int i = 0; i < size; i++) {
                daten[i] = binaryString.charAt(i) - '0';
            }
            size= 6;
            int divisor[] = {1,0,0,1,0,1};
            //Daten Array mit dem Divisor Array dividieren und das Resultat im Array speichern
            int res[] = dividiereDatenMitDivisor(daten, divisor);

            if(cbx.isSelected()){
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
                gesendeteDaten[daten.length-1] = (gesendeteDaten[daten.length-1]+1)%2;
                erhaltenDaten(gesendeteDaten, divisor);
            }
            whatsgoingon.setText(myModel.getWhatsgoingon());

        }
        catch (Exception e) {
            System.out.println("fehler");
        }
    }
    @FXML
    public void clickHamming()throws IOException  {
        myModel.setWhatsgoingon("");
        myModel.setInput(input.getText());
        String s1 = myModel.getInput();
        myModel.setWhatsgoingon(myModel.getWhatsgoingon()+"Original: "+ s1+"\n");
        //Dateneingabe in Bytes umwandeln
        byte[] bytes = s1.getBytes("UTF-8");
        //Bytes als Binary String umwandeln
        String binaryString = convertToBinaryString(bytes);
        myModel.setWhatsgoingon(myModel.getWhatsgoingon()+"Original: "+ binaryString+"\n");

        //Daten Array erstellen mit Grösse des Binary String
        int size = binaryString.length();
        // declare variables and array
        int hammingCodeSize, errorPosition;
        int hammingCode[];
        // create scanner class object to take input from user

        // initialize array
        int arr[] = new int[size];
        //Binary String übergibt Bits dem Daten Array
        for (int i = 0; i < size; i++) {
            arr[i] = binaryString.charAt(i) - '0';
        }

        // die Methode getHammingCode() aufrufen und ihren Rückgabewert im Array hammingCode speichern
        hammingCode = getHammingCode(arr);
        hammingCodeSize = hammingCode.length;

        errorPosition = Integer.parseInt( aenderungspos.getText());


        // Prüfen, ob die vom Benutzer eingegebene Position 0 ist oder nicht.
        if(errorPosition != 0) {
            // Bit der vom Benutzer eingegebenen Position ändern
            hammingCode[errorPosition - 1] = (hammingCode[errorPosition - 1] + 1) % 2;
        }
        // gesendete Daten an den Empfänger ausgeben
        receiveData(hammingCode, hammingCodeSize - arr.length);
        whatsgoingon.setText(myModel.getWhatsgoingon());
    }
    // Erstellung der Methode getHammingCode(), die den Hamming-Code für die zu sendenden Daten zurückgibt
    int[] getHammingCode(int data[]) {
        myModel.setWhatsgoingon("");
        // ein Array deklarieren, in dem der Hamming-Code für die Daten gespeichert wird
        int returnData[];
        int size;
        // Code, um die erforderliche Anzahl von Paritätsbits zu erhalten
        int i = 0, parityBits = 0 ,j = 0, k = 0;
        size = data.length;
        while(i < size) {
            // 2 hoch Paritätsbits müssen gleich der aktuellen Position sein (Anzahl der durchlaufenen Bits + Anzahl der Paritätsbits + 1).
            if(Math.pow(2, parityBits) == (i + parityBits + 1)) {
                parityBits++;
            }
            else {
                i++;
            }
        }

        // Die Größe der Rückgabedaten ist gleich der Grösse der Originaldaten + der Anzahl der Paritätsbits.
        returnData = new int[size + parityBits];

        // um einen nicht gesetzten Wert für das Paritätsbit anzuzeigen, wird das Array returnData mit '2' initialisiert

        for(i = 1; i <= returnData.length; i++) {
            // Bedingung, um die Position des Paritätsbits zu finden
            if(Math.pow(2, j) == i) {

                returnData[(i - 1)] = 2;
                j++;
            }
            else {
                returnData[(k + j)] = data[k++];
            }
        }
        // for-Schleife verwenden, um gerade Paritätsbits an den Paritätsbitpositionen zu setzen
        for(i = 0; i < parityBits; i++) {

            returnData[((int) Math.pow(2, i)) - 1] = getParityBit(returnData, i);
        }
        return returnData;
    }

    // erstellen der Methode getParityBit(), die das Paritätsbit auf der Grundlage der Potenz zurückgibt
    static int getParityBit(int returnData[], int pow) {
        int parityBit = 0;
        int size = returnData.length;

        for(int i = 0; i < size; i++) {

            // prüfen, ob returnData[i] einen nicht gesetzten Wert enthält oder nicht
            if(returnData[i] != 2) {

                // wenn nicht, speichern wir den Index in k, indem wir seinen Wert um 1 erhöhen

                int k = (i + 1);

                // den Wert von k in binäre Werte umwandeln
                String str = Integer.toBinaryString(k);

                // Nun, wenn das Bit an der 2^(Potenz)-Stelle des Binärwertes von index 1 ist, prüfen wir den an dieser
                // Stelle gespeicherten Wert. Wenn der Wert 1 oder 0 ist, berechnen wir den Paritätswert.


                int temp = ((Integer.parseInt(str)) / ((int) Math.pow(10, pow))) % 10;
                if(temp == 1) {
                    if(returnData[i] == 1) {
                        parityBit = (parityBit + 1) % 2;
                    }
                }
            }
        }
        return parityBit;
    }

    // Methode receiveData() zur Erkennung von Fehlern in den empfangenen Daten erstellen
    void receiveData(int data[], int parityBits) {

        // Variable pow deklarieren, die wir verwenden, um die richtigen Bits für die Paritätsprüfung zu erhalten
        int pow;
        int size = data.length;
        // parityArray deklarieren, um den Wert der Paritätsprüfung zu speichern
        int parityArray[] = new int[parityBits];
        // Wir verwenden errorLoc string für die Speicherung des Integer-Wertes der Fehlerstelle.
        String errorLoc = new String();
        // for-Schleife zur Überprüfung der Paritäten verwenden
        for(pow = 0; pow < parityBits; pow++) {
            // for-Schleife verwenden, um das Bit aus 2^(Potenz) zu extrahieren
            for(int i = 0; i < size; i++) {
                int j = i + 1;
                // Werte von j in das Binärformat konvertieren
                String str = Integer.toBinaryString(j);
                // Bit mit Hilfe von str finden
                int bit = ((Integer.parseInt(str)) / ((int) Math.pow(10, pow))) % 10;
                if(bit == 1) {
                    if(data[i] == 1) {
                        parityArray[pow] = (parityArray[pow] + 1) % 2;
                    }
                }
            }
            errorLoc = parityArray[pow] + errorLoc;
        }
        // So erhalten wir die Werte der Paritätsprüfungsgleichung.
        // Anhand dieser Werte prüfen wir nun, ob ein einzelner Bitfehler vorliegt, und korrigieren ihn dann.
        // errorLoc liefert Paritätsprüfungswerte, die wir verwenden, um zu prüfen, ob ein einzelner Bitfehler vorliegt oder nicht
        // falls vorhanden, korrigieren wir sie
        int finalLoc = Integer.parseInt(errorLoc, 2);
        // prüfen, ob der finalLoc-Wert 0 ist oder nicht
        if(finalLoc != 0) {
            myModel.setWhatsgoingon(myModel.getWhatsgoingon()+"Der Fehler wurde an folgender Stelle gefunden " + finalLoc + "."+"\n");
            data[finalLoc - 1] = (data[finalLoc - 1] + 1) % 2;
            myModel.setWhatsgoingon(myModel.getWhatsgoingon()+"Nach Behebung des Fehlers lautet der Code:"+"\n");
            for(int i = 0; i < size; i++) {
                myModel.setWhatsgoingon(myModel.getWhatsgoingon()+data[size - i - 1]);
            }
            myModel.setWhatsgoingon(myModel.getWhatsgoingon()+"\n");
            output.setText("Fehler in den bekommenen Daten");
        }
        else {
            output.setText("Keine Fehler in den bekommen Daten");
        }
        // Originaldaten drucken
        myModel.setWhatsgoingon(myModel.getWhatsgoingon()+"Die vom Absender gesendeten Daten:"+"\n");
        pow = parityBits - 1;
        for(int k = size; k > 0; k--) {
            if(Math.pow(2, pow) != k) {
                myModel.setWhatsgoingon(myModel.getWhatsgoingon()+data[k - 1]);
            }
            else {
                // Wertes von pow verringern
                pow--;
            }
        }
        myModel.setWhatsgoingon(myModel.getWhatsgoingon()+"\n");
    }
    static String convertToBinaryString(byte[] bytes) throws UnsupportedEncodingException {

        StringBuffer buffer = new StringBuffer();
        for (int b : bytes) {
            buffer.append(Integer.toBinaryString((b + 256) % 256));
            buffer.append("");
        }
        return buffer.toString();

    }

    // Eingabedatei lesen
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
    int[] dividiereDatenMitDivisor(int alteDaten[], int divisor[]) {
        // res[] array deklariernen in dem der Rest gespeichert wird
        int res[] = new int[divisor.length];
        int i;
        int daten[] = new int[alteDaten.length + divisor.length];
        // mit System.arraycopy() Daten in res und daten Array kopieren
        System.arraycopy(alteDaten, 0, daten, 0, alteDaten.length);
        System.arraycopy(daten, 0, res, 0, divisor.length);
        // alteDaten iterieren und mit exor die bits und den Divisor verrechenen
        for (i = 0; i < alteDaten.length; i++) {
            myModel.setWhatsgoingon(myModel.getWhatsgoingon()+ (i + 1) + ".) Erstes Daten Bit ist : " + res[0]+"\n");
            myModel.setWhatsgoingon(myModel.getWhatsgoingon()+"Rest : "+"\n");
            if (res[0] == 1) {
                // Mit exor Rest und Divisor verrechnen
                for (int j = 1; j < divisor.length; j++) {
                    res[j - 1] = exorOperation(res[j], divisor[j]);
                    myModel.setWhatsgoingon(myModel.getWhatsgoingon()+res[j - 1]+"\n");
                }
            } else {
                // Die Rest Bits mit 0 mit der exorOperation verrechnen
                for (int j = 1; j < divisor.length; j++) {
                    res[j - 1] = exorOperation(res[j], 0);
                    myModel.setWhatsgoingon(myModel.getWhatsgoingon()+res[j - 1]+"\n");
                }
            }
            // Der letzte bit des Restes wird von Daten geholt
            res[divisor.length - 1] = daten[i + divisor.length];
            myModel.setWhatsgoingon(myModel.getWhatsgoingon()+res[divisor.length - 1]+"\n");
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
    void erhaltenDaten(int daten[], int divisor[]) {

        int res[] = dividiereDatenMitDivisor(daten, divisor);
        //Division ist fertig
        for (int i = 0; i < res.length; i++) {
            if (res[i] != 0) {
                output.setText("Korrupte Daten bekommen");
                return;
            }
        }
        output.setText("Daten bekommen ohne Fehler.");
    }
}

