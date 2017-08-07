package shenancalhar.getbrains;

/**
 * Created by Shen an Calhar on 07.08.2017.
 */
public class arduinoMsgValue {
    private char flag;
    private int reading;

    public arduinoMsgValue(char flag, int reading) {
        this.flag = flag;
        this.reading = reading;
    }

    public int getReading() {
        return reading;
    }

    public char getFlag() {
        return flag;
    }
}
