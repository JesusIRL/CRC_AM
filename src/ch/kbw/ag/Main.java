package ch.kbw.ag;

public class Main {

    public static void main(String args[]) throws IOException {
        //Pfad zu Eingabefile
        String path = System.getProperty("user.dir")+"/src/ch/kbw/ag/input.txt";
        System.out.println(path);
        String s1 = readFile(path, "UTF-8");
        System.out.println("Orginal: "+s1);
        //Umwandlung des Strings in das Binärsystem
        byte[] bytes = s1.getBytes("UTF-8");
        //Binary Array zurrück zu String umwandeln
        String byteString = convertToBinaryString(bytes);

        System.out.println(byteString);

        // create scanner class object to take input from user
        // Scanner erstellt für Userinput
        Scanner scan = new Scanner(System.in);

        //Arraygroesse für die Nachricht
        int size= byteString.length();

        int data[] = new int[size];
        //Bytestring übergibt bitcode dem Dataarray
        for (int i = 0; i < size; i++){
            data[i] = byteString.charAt(i) - '0';
        }

        // Groesse des Divisor vom Benutzer erfassen
        System.out.println("Enter the size of the divisor array:");
        size = scan.nextInt();
        int divisor[] = new int[size];
        //Divisor bits vom benutzer erfassen
        System.out.println("Enter divisor bits in the array one by one: ");
        for(int i = 0 ; i < size ; i++) {
            System.out.println("Enter bit " + (size-i) + ":");
            divisor[i] = scan.nextInt();
        }
        // Den data Array mit dem Divisor Array dividieren und das Resultat im rem Array speichern
        int rem[] = divideDataWithDivisor(data, divisor);
        // Rest rem in der Konsole ausgeben
        for(int i = 0; i < rem.length-1; i++) {
            System.out.print(rem[i]);
        }

        //CRC Code Ausgeben
        System.out.println("\nGenerated CRC code is: ");

        for(int i = 0; i < data.length; i++) {
            System.out.print(data[i]);
        }
        for(int i = 0; i < rem.length-1; i++) {
            System.out.print(rem[i]);
        }
        System.out.println();

        // Die Nachricht, welche beim Empfaenger angkommt vom Benutzer in der Konsole einlesen
        int sentData[] = new int[data.length + rem.length - 1];
        System.out.println("Enter bits in the array which you want to send: ");
        for(int i = 0; i < sentData.length; i++) {
            System.out.println("Enter bit " +(sentData.length - 1)+ ":");
            sentData[i] = scan.nextInt();
        }
        receiveData(sentData, divisor);
    }



    static String convertToBinaryString(byte[] bytes) throws UnsupportedEncodingException {

        StringBuffer buffer = new StringBuffer();
        for (int b : bytes) {
            buffer.append(Integer.toBinaryString((b + 256) % 256));
            buffer.append("");
        }
        return buffer.toString();

    }
    static String readFile(String filePath, String encoding) throws IOException {
        File file = new File(filePath);
        StringBuffer buffer = new StringBuffer();
        try (InputStreamReader isr = new InputStreamReader(new FileInputStream(file), encoding)) {
            int data;
            while ((data = isr.read()) != -1) {
                buffer.append((char) data);
            }
        }
        return buffer.toString();
    }





    // CRC code mit dem CRC verfahren ausrechnen
    static int[] divideDataWithDivisor(int oldData[], int divisor[]) {
        // rem[] array deklariernen in dem der Rest gespeichert wird
        int rem[] = new int[divisor.length];
        int i;
        int data[] = new int[oldData.length + divisor.length];
        // mit System.arraycopy() daten in rem und data Array kopieren
        System.arraycopy(oldData, 0, data, 0, oldData.length);
        System.arraycopy(data, 0, rem, 0, divisor.length);
        // oldData iterieren und mit exor die bits und den Divisor verrechenen
        for(i = 0; i < oldData.length; i++) {
            System.out.println((i+1) + ".) First data bit is : "+ rem[0]);
            System.out.print("Remainder : ");
            if(rem[0] == 1) {
                // Mit exor Rest und Divisor verrechnen
                for(int j = 1; j < divisor.length; j++) {
                    rem[j-1] = exorOperation(rem[j], divisor[j]);
                    System.out.print(rem[j-1]);
                }
            }
            else {
                // We have to exor the remainder bits with 0
                // Die Rest bits mit 0 mit der exorOperation verrechnen
                for(int j = 1; j < divisor.length; j++) {
                    rem[j-1] = exorOperation(rem[j], 0);
                    System.out.print(rem[j-1]);
                }
            }
            // Der letzte bit des Restes wird vom data geholt
            rem[divisor.length-1] = data[i+divisor.length];
            System.out.println(rem[divisor.length-1]);
        }
        return rem;
    }
    // exorOperation() erstellen
    static int exorOperation(int x, int y) {
        // Exor
        if(x == y) {
            return 0;
        }
        return 1;
    }
    // method to print received data
    // erhaltene Daten Aus
    static void receiveData(int data[], int divisor[]) {

        int rem[] = divideDataWithDivisor(data, divisor);
        // Division is done
        for(int i = 0; i < rem.length; i++) {
            if(rem[i] != 0) {
                // if the remainder is not equal to zero, data is currupted
                System.out.println("Currupted data received...");
                return;
            }
        }
        System.out.println("Data received without any error.");
    }
}
