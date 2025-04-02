package com.JH.JhOnlineJudge.domain.Image;

public enum ImageFrom {
    PRODUCT("Product"),
    INQUIRY("Inquiry"),
    REVIEW("Review");

    private final String dir;

    ImageFrom(String dir){
        this.dir = dir;
    }

    public String getDir() {
        return dir;
    }

}
