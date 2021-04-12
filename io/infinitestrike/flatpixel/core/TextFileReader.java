package io.infinitestrike.flatpixel.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class TextFileReader {
    public static String readFile(String s){
        return Gdx.files.internal(s).readString();
    }
}
