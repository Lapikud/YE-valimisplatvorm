package backend.enums;

public enum Faculty {
    INFORMATION_TECHNOLOGIES('I'),
    ENGINEERING('E'),
    SCIENCE('L'),
    BUSINESS_AND_GOVERNANCE('M');

    private char value;

    private Faculty(char value) {
        this.value = value;
    }
}
