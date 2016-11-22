package com.androidrinomediarino.mediaplayerino;

public enum SortByEnum {
    SONGNAME(3),
    ARTISTNAME(4),
    ALBUMNAME(5);

    private int value;

    private SortByEnum(int value){
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static SortByEnum fromInt(int number) {
        for (SortByEnum value : SortByEnum.values()) {
            if (value.getValue() == number) {
                return value;
            }
        }
        return null;
    }
}
