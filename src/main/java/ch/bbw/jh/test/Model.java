package ch.bbw.jh.test;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

/**
 * Model
 * @author Jorin Heer, Aron Gassner
 * @version 2022
 */

public class Model {

    private String input;
    private String hammingInput;
    private String output;
    private String hammingOutput;
    private String whatsgoingon;

    public Model() {
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public String getHammingInput() {
        return hammingInput;
    }

    public void setHammingInput(String hammingInput) {
        this.hammingInput = hammingInput;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public String getHammingOutput() {
        return hammingOutput;
    }

    public void setHammingOutput(String hammingOutput) {
        this.hammingOutput = hammingOutput;
    }

    public String getWhatsgoingon() {
        return whatsgoingon;
    }

    public void setWhatsgoingon(String whatsgoingon) {
        this.whatsgoingon = whatsgoingon;
    }
}
