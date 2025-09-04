package net.nillus.ion.Core;

public enum LogType {
    Debug(0),
    Information(1),
    Warning(2),
    Error(3);

    private int value;
    LogType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
