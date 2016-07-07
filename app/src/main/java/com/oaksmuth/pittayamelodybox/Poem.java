package com.oaksmuth.pittayamelodybox;

/**
 * Created by Elm on 12/15/2015.
 * Edited 7/4/2016 : Add string[6] for English Compatibility
 * Edited 7/7/2016 : Change name to english
 */
public class Poem {
    private String[] string = new String[6];
    private String english;
    public Poem(String[] string,String english){
        this.string = string;
        this.english = english;
        if(this.english.startsWith("\"") && this.english.endsWith("\"")) {
            this.english = this.english.substring(1, this.english.length() - 1);
        }
    }

    public String toString() {
        return string[0] + " " + string[1] + " " + string[2] + " " + string[3] + " " + string[4] + " " + string[5];
    }

    public String EnglishString() {
        return english;
    }

    public String AllString() {
        return string[0] + " " + string[1] + " " + string[2] + " " + string[3] + " " + string[4] + " " + string[5] + " " + english;
    }

    public String enteredString() {
        return string[0] + "\n" + string[1] + "\n" + string[2] + "\n" + string[3] + "\n" + string[4] + "\n" + string[5] + "\n\n" + english;
    }
}
