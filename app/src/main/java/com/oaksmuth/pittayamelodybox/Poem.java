package com.oaksmuth.pittayamelodybox;

/**
 * Created by Elm on 12/15/2015.
 */
public class Poem {
    private String[] string = new String[6];

    public Poem(String[] string){
        this.string = string;
    }

    @Override
    public String toString() {
        return string[0] + " " + string[1] + " " + string[2] + " " + string[3] + " " + string[4] + " " + string[5];
    }

    public String enteredString() {
        return string[0] + "\n\n" + string[1] + "\n\n" + string[2] + "\n\n" + string[3] + "\n\n" + string[4] + "\n\n" + string[5];
    }
}
