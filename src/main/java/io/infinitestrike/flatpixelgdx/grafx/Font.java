package io.infinitestrike.flatpixelgdx.grafx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

public class Font {
    private int fontSize = 12;
    private BitmapFont font = null;

    private Font(BitmapFont font){
        this.font = font;
    }

    public Font(){
        this(12);
    }

    public Font(int size){
        this.fontSize = size;
        this.font = Font.deriveBitmapFont("fonts/arial.ttf",size);
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
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(path));
        FreeTypeFontGenerator.FreeTypeFontParameter params = new FreeTypeFontGenerator.FreeTypeFontParameter();
        params.size = size;
        params.shadowColor = Color.CLEAR;
        BitmapFont customFont = generator.generateFont(params);
        generator.dispose();
        Font f = new Font(customFont);
        return f;
    }

    public static BitmapFont deriveBitmapFont(String path){
        return Font.deriveBitmapFont(path,12);
    }

    public static BitmapFont deriveBitmapFont(String path, int size){
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(path));
        FreeTypeFontGenerator.FreeTypeFontParameter params = new FreeTypeFontGenerator.FreeTypeFontParameter();
        params.size = size;
        params.shadowColor = Color.CLEAR;
        BitmapFont customFont = generator.generateFont(params);
        return customFont;
    }

    public static Font fromBitmapFont(BitmapFont f){
        return new Font(f);
    }
}
