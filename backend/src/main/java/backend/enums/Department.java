package backend.enums;

public enum Department {
    INFOTEHNOLOOGIA_TEADUSKOND('I'),
    INSENERITEADUSKOND('E'),
    LOODUSTEADUSKOND('L'),
    MAJANDUSTEADUSKOND('M');

    private char value;

    private Department(char value) {
        this.value = value;
    }
}
