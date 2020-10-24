package io.infinitestrike.flatpixelgdx.grafx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;

import java.nio.ByteBuffer;

import io.infinitestrike.flatpixelgdx.core.LogBot;
import io.infinitestrike.flatpixelgdx.math.Vector;

public class Image {

    public enum DrawMode{
        CENTERED,TOP_LEFT,BOTTOM_LEFT,TOP_RIGHT,BOTTOM_RIGHT;
    }

    private com.badlogic.gdx.scenes.scene2d.ui.Image rootImage;
    private float imageWidth = 0, imageHeight = 0;
    private float offsetX = 0, offsetY = 0; // used as a drawing offset
    private float xscale = 1, yscale = 1;
    private float locX = 0, locY = 0;

    private DrawMode currentDrawMode = DrawMode.BOTTOM_LEFT;

    public  Image(com.badlogic.gdx.scenes.scene2d.ui.Image img){
        this.rootImage = img;
        this.imageWidth = img.getWidth();
        this.imageHeight = img.getHeight();
        this.MakeDrawModeChanges();
    }

    public Image(String path){
        this.rootImage = Image.loadImage(path);
        this.imageWidth = this.rootImage.getWidth();
        this.imageHeight = this.rootImage.getHeight();
        this.MakeDrawModeChanges();
    }

    public Image (String path, float width, float height){
        this.rootImage = Image.loadImage(path);
        this.imageWidth = width;
        this.imageHeight = height;
        this.MakeDrawModeChanges();
    }

    public Image (int color, int width, int height){
        Pixmap p = new Pixmap(width,height, Pixmap.Format.RGBA8888);
        p.setColor(new Color(color).toGDXColor());
        p.fillRectangle(0,0,width,height);
        this.rootImage = Image.loadImage(new Texture(p));
        this.imageHeight = height;
        this.imageWidth = width;
        this.MakeDrawModeChanges();
        p.dispose();
    }

    public float getImageWidth(){
        return this.imageWidth;
    }

    public float getImageHeight(){
        return this.imageHeight;
    }

    public void setImageWidth(float width){
        this.imageWidth = width;
    }

    public void setImageHeight(float height){
        this.imageHeight = height;
    }

    public com.badlogic.gdx.scenes.scene2d.ui.Image getRootImage(){
        return this.rootImage;
    }

    public Image getSubImage(int x, int y, int w, int h){
        FrameBuffer fbo = new FrameBuffer(Pixmap.Format.RGBA8888,(int)this.getImageWidth(),(int)this.getImageHeight(),true);
        SpriteBatch batch = new SpriteBatch();
        Matrix4 matrix = new Matrix4();
        matrix.setToOrtho2D(0, 0, this.getImageWidth(),this.getImageHeight());
        batch.setProjectionMatrix(matrix);
        fbo.begin();
        batch.begin();
        this.rootImage.draw(batch,1);
        batch.end();

        Pixmap map = Graphics.flipPixmap(ScreenUtils.getFrameBufferPixmap(0,0,fbo.getWidth(),fbo.getHeight()),false,true);
        fbo.end();
        Pixmap rmap = new Pixmap(w,h, Pixmap.Format.RGBA8888);

        for(int yi = y; yi < y + h; yi++){
            for(int xi = x; xi < x + w; xi++){
                int p = map.getPixel(xi,yi);
                rmap.drawPixel((xi - x),(yi - y),p);
            }
        }

        Texture t = new Texture(rmap);

        map.dispose();
        rmap.dispose();

        return new Image(Image.loadImage(t));
    }

    public void setImageSize(float x, float y){
        this.setImageWidth(x);
        this.setImageHeight(y);
    }

    public void setDrawMode(DrawMode mode){
        this.currentDrawMode = mode;
        this.MakeDrawModeChanges();
    }

    public DrawMode getDrawMode(){
        return this.currentDrawMode;
    }

    public void setLocation(float x, float y){
        this.locX = x;
        this.locY = y;
    }

    public void setScale(float x, float y){
        this.xscale = x;
        this.yscale = y;
    }

    public void setScale(float s){
        this.xscale = s;
        this.yscale = s;
    }

    public Vector.Vector2f getScale(){
        return new Vector.Vector2f(this.xscale,this.yscale);
    }

    public Vector.Vector2f getLocation(){
        return new Vector.Vector2f(this.locX,this.locY);
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

    public void draw(SpriteBatch b){
        this.rootImage.setPosition(this.locX + this.offsetX, this.locY + this.offsetY);
        this.rootImage.setScale(this.xscale,this.yscale);
        this.rootImage.setSize(this.imageWidth,this.imageHeight);
        this.rootImage.draw(b,1);
    }

    public static final com.badlogic.gdx.scenes.scene2d.ui.Image loadImage(String path){
        return new com.badlogic.gdx.scenes.scene2d.ui.Image(new Texture(Gdx.files.internal(path)));
    }

    public static final com.badlogic.gdx.scenes.scene2d.ui.Image loadImage(Texture t){
        return new com.badlogic.gdx.scenes.scene2d.ui.Image(t);
    }

    public static final com.badlogic.gdx.scenes.scene2d.ui.Image loadImage(TextureRegion r){
        com.badlogic.gdx.scenes.scene2d.ui.Image i = new com.badlogic.gdx.scenes.scene2d.ui.Image();
        TextureRegionDrawable d = new TextureRegionDrawable(r);
        i.setDrawable(d);
        return i;
    };
}
