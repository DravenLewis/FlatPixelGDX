package io.infinitestrike.flatpixel.grafx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import io.infinitestrike.flatpixel.core.*;

public class Image {

    public enum DrawMode{
        CENTERED,TOP_LEFT,BOTTOM_LEFT,TOP_RIGHT,BOTTOM_RIGHT;
    }

    private final Texture imageData;
    private final TextureRegion imageDataPart;

    private int imageWidth = 0, imageHeight = 0;
    private int imageLocationX = 0;
    private int imageLocationY = 0;
    private float scaleX = 1, scaleY = 1;
    private int offsetX = 0, offsetY = 0;
    private float alpha = 1;

    private DrawMode currentDrawMode = DrawMode.BOTTOM_LEFT;

    public Image(Image img){
        this.imageData = img.imageData;
        this.imageDataPart = img.imageDataPart;
        this.imageWidth = img.getImageWidth();
        this.imageHeight = img.getImageHeight();
        this.imageLocationX = img.getImageLocationX();
        this.imageLocationY = img.getImageLocationY();
        this.offsetY = img.offsetY;
        this.offsetX = img.offsetX;
        this.scaleX = img.scaleX;
        this.scaleY = img.scaleY;
        this.currentDrawMode = img.currentDrawMode;
        this.MakeDrawModeChanges();
    }

    public Image(String path){
        this.imageData = new Texture(Gdx.files.internal(path));
        this.imageDataPart = null;
        this.imageWidth = imageData.getWidth();
        this.imageHeight = imageData.getHeight();
        this.MakeDrawModeChanges();
    }

    public Image(String path, int w, int h){
        this.imageData = new Texture(Gdx.files.internal(path));
        this.imageDataPart = null;
        this.imageWidth = w;
        this.imageHeight = h;
        this.MakeDrawModeChanges();
    }

    public Image(Texture data){
        this.imageData = data;
        this.imageDataPart = null;
        this.imageWidth = data.getWidth();
        this.imageHeight = data.getHeight();
        this.MakeDrawModeChanges();
    }

    public Image(TextureRegion r){
        this.imageData = null;
        this.imageDataPart = r;
        this.imageWidth = r.getRegionWidth();
        this.imageHeight = r.getRegionHeight();
        this.MakeDrawModeChanges();
    }

    public Image(int c, int w, int h){
        Pixmap p = new Pixmap(w,h, Pixmap.Format.RGBA8888);
        p.setColor(new Color(c).toGDXColor());
        p.fillRectangle(0,0,w,h);
        this.imageData = new Texture(p);
        this.imageDataPart = null;
        this.imageWidth = w;
        this.imageHeight = h;
        this.MakeDrawModeChanges();
    }

    private void MakeDrawModeChanges(){
        switch (this.currentDrawMode){
            case CENTERED:
                this.offsetX = -this.imageWidth / 2;
                this.offsetY = -this.imageHeight / 2;
                break;
            case TOP_LEFT:
                this.offsetX = 0;
                this.offsetY = -this.imageHeight;
                break;
            case TOP_RIGHT:
                this.offsetX = -this.imageWidth;
                this.offsetY = -this.imageHeight;
                break;
            case BOTTOM_LEFT:
                this.offsetX = 0;
                this.offsetY = 0;
                break;
            case BOTTOM_RIGHT:
                this.offsetX = -this.imageWidth;
                this.offsetY = 0;
                break;
            default:
                this.offsetY = 0;
                this.offsetX = 0;
        }
    }

    public void setDrawMode(DrawMode mode){
        this.currentDrawMode = mode;
        this.MakeDrawModeChanges();
    }

    public DrawMode getDrawMode(){
        return this.currentDrawMode;
    }

    public Texture getTexture(){
        return this.imageData;
    }

    public TextureRegion getTextureRegion(){
        return  this.imageDataPart;
    }

    public Image getSubImage(int x, int y, int w, int h){
        if(this.imageData != null) {
            if (!this.imageData.getTextureData().isPrepared()) {
                this.imageData.getTextureData().prepare();
            }
            TextureRegion r = new TextureRegion(this.imageData, x, y, w, h);
            r.flip(false, true);

            return new Image(r);
        }
        if(this.imageDataPart != null){

            Texture t = this.imageDataPart.getTexture();

            if (!t.getTextureData().isPrepared()) {
                t.getTextureData().prepare();
            }
            TextureRegion r = new TextureRegion(t, x, y, w, h);
            r.flip(false, true);

            return new Image(r);
        }
        return null;
    }

    public Image getSubImage(int x, int y, int w, int h, boolean flipx, boolean flipy){
        if(this.imageData != null) {
            if (!this.imageData.getTextureData().isPrepared()) {
                this.imageData.getTextureData().prepare();
            }
            TextureRegion r = new TextureRegion(this.imageData, x, y, w, h);
            r.flip(flipx, flipy);

            return new Image(r);
        }
        if(this.imageDataPart != null){

            Texture t = this.imageDataPart.getTexture();

            if (!t.getTextureData().isPrepared()) {
                t.getTextureData().prepare();
            }
            TextureRegion r = new TextureRegion(t, x, y, w, h);
            r.flip(flipx, flipy);

            return new Image(r);
        }
        return null;
    }

    public final void draw(SpriteBatch b){
        Color original = new Color(b.getColor());
        Color alpha = new Color(b.getColor());
        alpha.setAlpha(this.alpha);
        b.setColor(alpha.toGDXColor());
        if(this.imageData != null) {
            b.draw(this.imageData,
                    this.imageLocationX + this.offsetX,
                    this.imageLocationY + this.offsetY,
                    this.imageWidth * scaleX,
                    this.imageHeight * scaleY);
        }else if(this.imageDataPart != null){
            b.draw(this.imageDataPart,
                    this.imageLocationX + this.offsetX,
                    this.imageLocationY + this.offsetY,
                    this.imageWidth * scaleX,
                    this.imageHeight * scaleY);
        }
        b.setColor(original.toGDXColor()); // reset the color
    }

    public void setImageWidth(int width){
        this.imageWidth = width;
    }

    public void setImageHeight(int height){
        this.imageHeight = height;
    }

    public int getImageWidth(){
        return this.imageWidth;
    }

    public int getImageHeight(){
        return this.imageHeight;
    }

    public float getImageScaledWidth(){
        return this.imageWidth * this.scaleX;
    }

    public float getImageScaledHeight(){
        return this.imageWidth * this.scaleY;
    }

    public int getImageLocationX(){
        return this.imageLocationX;
    }

    public void setImageLocationX(int x){
        this.imageLocationX = x;
    }

    public void setAlpha(float alpha){
        this.alpha = Core.MathFunctions.fclamp(alpha,0,1);
    }

    public float getAlpha(){
        return this.alpha;
    }

    public int getImageLocationY() {
        return imageLocationY;
    }

    public void setImageLocationY(int imageLocationY) {
        this.imageLocationY = imageLocationY;
    }

    public Image getFlippedCopy(boolean flipx, boolean flipy){
        return this.getSubImage(0,0,this.getImageWidth(),this.getImageHeight(),flipx,flipy);
    };

    public void setLocation(int x, int y){
        this.imageLocationX = x;
        this.imageLocationY = y;
    }

    public Image getTintedCopy(Color c){
        Sprite s = new Sprite((this.getTexture() != null) ? this.getTexture() : this.getTextureRegion().getTexture());
        s.setColor(c.toGDXColor());
        return new Image(s);
    };

    public float getScaleX() {
        return scaleX;
    }

    public void setScaleX(float scaleX) {
        this.scaleX = scaleX;
    }

    public float getScaleY() {
        return scaleY;
    }

    public void setScaleY(float scaleY) {
        this.scaleY = scaleY;
    }

    public void setBounds(int x, int y, int w, int h){
        this.imageLocationX = x;
        this.imageLocationY = y;
        this.imageWidth = w;
        this.imageHeight = h;
    }

    public void dispose(){
        if(this.imageData != null) this.imageData.dispose();
        if(this.imageDataPart != null) this.imageDataPart.getTexture().dispose();
    }
}
