package com.oaksmuth.pittayamelodybox;

import android.content.Context;
import android.content.res.AssetManager;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by noraw on 11/12/2558.
 */
public class PoemProvider {
    public static ArrayList<Poem> poems = new ArrayList<Poem>();
    public PoemProvider(Context context) throws IOException {
        Scanner in;
        AssetManager am = context.getAssets();
        InputStream inputStream = am.open("Melody.dat");
        in = new Scanner(inputStream,"utf-8").useDelimiter("\\t|\\n");
        for (int i = 0; i < 2000; i++) {
            String[] tempStringList = new String[7];
            for(int j = 0; j < 6; j++)
            {
                tempStringList[j] = in.next().trim();
            }
            String eng = in.next().trim();
            poems.add(new Poem(tempStringList,eng));
        }
        in.close();
        inputStream.close();
    }

}
