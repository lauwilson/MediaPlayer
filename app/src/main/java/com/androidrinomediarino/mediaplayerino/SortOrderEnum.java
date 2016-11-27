package com.androidrinomediarino.mediaplayerino;

public enum SortOrderEnum {
    ASC(0),
    DESC(1);

    private int value;

    private SortOrderEnum(int value){
        this.value = value;
    }

    public int getValue(){
        return value;
    }
}
