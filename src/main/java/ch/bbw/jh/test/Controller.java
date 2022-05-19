package ch.bbw.jh.test;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;

import java.io.*;
import java.text.BreakIterator;
import java.util.Scanner;

/**
 * Controller
 * @author Jorin Heer, Aron Gassner
 * @version 2022
 */

public class Controller  {

    private Model myModel;
    private HammingCode hammingCode = new HammingCode();
    @FXML
    private TextArea input;
    @FXML
    private TextArea hammingInput;
    @FXML
    private TextField output;
    @FXML
    private TextArea hammingOutput;
    @FXML
    private TextField aenderungspos;
    @FXML
    private TextArea whatsgoingon;
    @FXML
    private CheckBox cbx;
    @FXML
    private Button auswahl1;
    @FXML
    private Button auswahl2;
    @FXML
    private Button auswahl3;
    @FXML
    private Button decode;

    public void setModel(Model myModel) {
        this.myModel = myModel;
        myModel.setInput("");
        myModel.setOutput("");
        myModel.setWhatsgoingon("");
        myModel.setHammingInput("");
        myModel.setHammingOutput("");

    }
    @FXML
    private void Initialize(){
        input.setText("");
        output.setText("");
        whatsgoingon.setText("");
    }


    /**
     * Teile dieses Codes wurden von https://www.javatpoint.com/crc-program-in-java übernommen.
     * Abfragedatum: 08.05.2022. https://www.javatpoint.com/crc-program-in-java Jahr 2020.
     */
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
    public void auswahl1()throws IOException  {
        myModel.setHammingInput("");
        myModel.setHammingOutput("");
        hammingCode.setFinalStr("");
        myModel.setHammingInput(hammingInput.getText());
        String s1 = myModel.getHammingInput();
        //Dateneingabe in Bytes umwandeln
        byte[] bytes = s1.getBytes("UTF-8");
        //Bytes als Binary String umwandeln
        String binaryString = convertToBinaryString(bytes);
        System.out.println("binaryString"+binaryString);
        hammingCode.autoHammingCode(binaryString);
        myModel.setHammingOutput(myModel.getHammingOutput()+"Ihre Eingabe: " + s1 + "\n");
        //Dateneingabe in Bytes umwandeln
        myModel.setHammingOutput(myModel.getHammingOutput()+"verwandelt zu binär: " + binaryString + "\n");
        myModel.setHammingOutput(myModel.getHammingOutput()+"Hamming Code: " + hammingCode.getFinalStr() + "\n");
        hammingOutput.setText(myModel.getHammingOutput() + "\n");
    }


    @FXML
    public void auswahl2()throws IOException  {
        myModel.setHammingInput("");
        myModel.setHammingOutput("");
        hammingCode.setFinalStr("");
        hammingCode.setN(15);
        hammingCode.setn(11);
        clickHamming();
    }

    @FXML
    public void auswahl3()throws IOException  {
        myModel.setHammingInput("");
        myModel.setHammingOutput("");
        hammingCode.setFinalStr("");
        hammingCode.setN(63);
        hammingCode.setn(57);
        clickHamming();
    }

    public void clickHamming()throws IOException  {
        myModel.setHammingInput(hammingInput.getText());
        String s1 = myModel.getHammingInput();
        myModel.setHammingOutput(myModel.getHammingOutput()+"Ihre Eingabe: " + s1 + "\n");
        // Dateneingabe in Bytes umwandeln
        byte[] bytes = s1.getBytes("UTF-8");
        // Bytes als Binary String umwandeln
        String binaryString = convertToBinaryString(bytes);
        hammingCode.hammingCode(binaryString);
        myModel.setHammingOutput(myModel.getHammingOutput()+"verwandelt zu binär: " + hammingCode.getNoParity() + "\n");
        myModel.setHammingOutput(myModel.getHammingOutput()+"Hamming Code: " + hammingCode.getFinalStr() + "\n");
        hammingOutput.setText(myModel.getHammingOutput() + "\n");
    }

    public void clickReceiveHamming() {
        myModel.setHammingInput(hammingInput.getText());
        String s1 = myModel.getHammingInput();
        int arr [] = new int[s1.length()];
        for (int i = 0; i < s1.length(); i++) {
            arr[i] = s1.charAt(i) - '0';
        }
        hammingCode.receiveData(arr, hammingCode.getR());
        myModel.setHammingOutput(myModel.getHammingOutput() + hammingCode.getAfter());
        hammingOutput.setText(myModel.getHammingOutput() + "\n");
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

