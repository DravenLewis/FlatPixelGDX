package io.infinitestrike.flatpixel.grafx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

public class Font {
    private int fontSize = 12;
    private BitmapFont font = null;
    private boolean flipped =  false;

    private Font(BitmapFont font){
        this(font,false);
    }

    private Font(BitmapFont font, boolean flipped){
        this.font = font;
        this.flipped =flipped;
    }

    public Font(){
        this(12,false);
    }

    public Font(int size){
        this(size,false);
    }

    public Font(int size, boolean flipped){
        this.fontSize = size;
        this.flipped = flipped;
        this.font = Font.deriveBitMapFont(Font.getInternalFontHandle(),size,flipped);
    }

    public BitmapFont getInternalFont(){
        return this.font;
    }

    public float getFontSize(){
        return this.fontSize;
    }

    public static Font deriveFont(String path){
        return Font.deriveFont(path,12);
    }

    public static Font deriveFont(String path, int size){
        return deriveFont(path,size,false);
    }

    public static Font deriveFont(String path, int size, boolean flipped){
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(path));
        FreeTypeFontGenerator.FreeTypeFontParameter params = new FreeTypeFontGenerator.FreeTypeFontParameter();
        params.size = size;
        params.shadowColor = Color.CLEAR;
        params.flip = flipped;
        params.magFilter = Texture.TextureFilter.Nearest;
        params.minFilter = Texture.TextureFilter.Nearest;
        BitmapFont customFont = generator.generateFont(params);
        generator.dispose();
        Font f = new Font(customFont);
        return f;
    }

    public static BitmapFont deriveBitmapFont(String path){
        return Font.deriveBitmapFont(path,12);
    }

    public static BitmapFont deriveBitmapFont(String path, int size){
        return Font.deriveBitmapFont(path,size,false);
    }

    public static BitmapFont deriveBitmapFont(String path, int size, boolean flipped){
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(path));
        FreeTypeFontGenerator.FreeTypeFontParameter params = new FreeTypeFontGenerator.FreeTypeFontParameter();
        params.size = size;
        params.shadowColor = Color.CLEAR;
        params.flip = flipped;
        BitmapFont customFont = generator.generateFont(params);
        return customFont;
    }

    public static BitmapFont deriveBitMapFont(FileHandle handle, int size, boolean flipped){
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(handle);
        FreeTypeFontGenerator.FreeTypeFontParameter params = new FreeTypeFontGenerator.FreeTypeFontParameter();
        params.size = size;
        params.shadowColor = Color.CLEAR;
        params.flip = flipped;
        BitmapFont customFont = generator.generateFont(params);
        return customFont;
    }

    public static Font fromBitmapFont(BitmapFont f){
        return new Font(f);
    }

    public static FileHandle getInternalFontHandle(){
        FileHandle fontFile = Gdx.files.classpath("font.bin");
        return fontFile;
    }
}