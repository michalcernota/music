package cz.upce.music.entity;

public enum UserRoleEnum {
    User("USER"),
    Admin("ADMIN");

    private final String displayValue;

    UserRoleEnum(String displayValue) {
        this.displayValue = displayValue;
    }

    public String getDisplayValue() {
        return displayValue;
    }
}
