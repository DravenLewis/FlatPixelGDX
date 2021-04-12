package io.infinitestrike.flatpixel.grafx;

public class Color {

    public static final Color BLACK = new Color(0x000000);
    public static final Color WHITE = new Color(0xFFFFFF);
    public static final Color RED = new Color(0xFF0000);
    public static final Color GREEN = new Color(0x00FF00);
    public static final Color BLUE = new Color(0x0000FF);
    public static final Color YELLOW = new Color(0xFFFF00);
    public static final Color CYAN = new Color(0x00FFFF);
    public static final Color MAGENTA = new Color(0xFF00FF);

    private float r = 0;
    private float g = 0;
    private float b = 0;
    private float a = 0;

    public Color(int color32){
        // 0xAARRGGBB

        this.a = 0xFF / (float) 0xFF; // Alpha = 1;
        this.r = (float)((color32 >> 16) & 0xFF) / (float) 0xFF;
        this.g = (float)((color32 >> 8) & 0xFF) / (float) 0xFF;
        this.b = (float)((color32) & 0xFF) / (float) 0xFF;
    }

    public Color(com.badlogic.gdx.graphics.Color c){
        this.a = c.a;
        this.r = c.r;
        this.g = c.g;
        this.b = c.b;
    }

    public Color(float r, float g, float b){
        this(r,g,b,1);
    }

    public Color(float r, float g, float b, float a){
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

    public com.badlogic.gdx.graphics.Color toGDXColor(){
        com.badlogic.gdx.graphics.Color gdxColor = new com.badlogic.gdx.graphics.Color();
        gdxColor.r = this.getRed();
        gdxColor.g = this.getGreen();
        gdxColor.b = this.getBlue();
        gdxColor.a = this.getAlpha();
        return gdxColor;
    }


    public static int[] intArrayFromByteArray(byte[] rawData){ // RGBA8888 Format, 8bpp.
        int[] finalArray = new int[rawData.length / 4];
        for(int i = 0; i < rawData.length; i+= 4){
            int color = rawData[i];
            color = (color << 8) + rawData[i + 1];
            color = (color << 8) + rawData[i + 2];
            color = (color << 8) + rawData[i + 3];
            finalArray[i / 4] = color;
        }

        return finalArray;
    }

    public int getRGBA(){
        int color = Math.round(this.a * 255);
        color = (color << 8) + (Math.round(this.b * 255));
        color = (color << 8) + (Math.round(this.g * 255));
        color = (color << 8) + (Math.round(this.r * 255));
        return color;
    };

    public float getRed(){return this.r;}
    public float getGreen(){return this.g;}
    public float getBlue(){return this.b;}
    public float getAlpha(){return this.a;}

    public void setRed(float r){this.r = r;}
    public void setGreen(float g){this.g = g;}
    public void setBlue(float b){this.b = b;}
    public void setAlpha(float a){this.a = a;}
}
