package io.infinitestrike.flatpixelgdx.grafx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;


import java.nio.ByteBuffer;

import io.infinitestrike.flatpixelgdx.math.FPMath;

public class Graphics {
    private int sizeX = 0, sizey = 0;
    private View attatchedView = null;
    private Color backgroundColor = Color.BLACK;
    private Color foregroundColor = Color.WHITE;
    private Font f = new Font();
    private ShapeRenderer shapeRenderer = null;
    private SpriteBatch spriteBatch = null;


    public static Graphics getGraphics(View v){
        Graphics g = new Graphics();
        g.attatchedView = v;
        g.spriteBatch = new SpriteBatch();
        g.shapeRenderer = new ShapeRenderer();
        return g;
    }

    public void dispose(){
        this.attatchedView = null;
        this.spriteBatch.dispose();
        this.shapeRenderer.dispose();
    }


    public View getView(){
        return this.attatchedView;
    }

    public Font getFont() {
        return f;
    }

    public void setFont(Font f) {
        this.f = f;
    }

    public SpriteBatch getDrawBatch(){
        return this.spriteBatch;
    }

    public ShapeRenderer getShapeRenderer(){
        return this.shapeRenderer;
    }

    // GRAPHICS ROUTINES
    // =============================================================================================

    public void setColor(Color c){
        this.backgroundColor = c;
    }

    public Color getColor(){
        return this.backgroundColor;
    }

    public void clear(){
        this.clear(Color.BLACK);
    }

    public void clear(Color c){
        Gdx.gl.glClearColor(c.getRed(),c.getGreen(),c.getBlue(),c.getAlpha());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); // Clear the actual buffer, not just draw over it

        /*
        this.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        this.shapeRenderer.setColor(this.getColor().toGDXColor());
        this.shapeRenderer.rect(0,0,this.getView().getWidth(),this.getView().getHeight());
        this.shapeRenderer.end();
         */
    }

    public void drawRect(int x, int y, int w, int h){
        this.shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        this.shapeRenderer.setColor(this.getColor().toGDXColor());
        this.shapeRenderer.rect(x,y,w,h);
        this.shapeRenderer.end();
    }

    public void fillRect(int x, int y, int w, int h){
        this.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        this.shapeRenderer.setColor(this.getColor().toGDXColor());
        this.shapeRenderer.rect(x,y,w,h);
        this.shapeRenderer.end();
    }

    public void drawText(String text, int x, int y){
        Color c = new Color(this.getFont().getInternalFont().getColor());
        this.getDrawBatch().begin();
        this.getFont().getInternalFont().setColor(this.getColor().toGDXColor());
        this.getFont().getInternalFont().draw(this.spriteBatch,text,x,y);
        this.getFont().getInternalFont().setColor(c.toGDXColor());
        this.getDrawBatch().end();
    }

    public void fillEllipse(int x, int y, int w, int h){
        this.getShapeRenderer().setColor(this.getColor().toGDXColor());
        this.getShapeRenderer().begin(ShapeRenderer.ShapeType.Filled);
        this.getShapeRenderer().ellipse(x,y,w,h);
        this.getShapeRenderer().end();
    }

    public void drawEllipse(int x, int y, int w, int h){
        this.getShapeRenderer().setColor(this.getColor().toGDXColor());
        this.getShapeRenderer().begin(ShapeRenderer.ShapeType.Line);
        this.getShapeRenderer().ellipse(x,y,w,h);
        this.getShapeRenderer().end();
    }

    public void drawImage(Image img, int x, int y, float scale){
        this.drawImage(img,x,y,img.getImageWidth() * scale,img.getImageHeight() * scale,Color.WHITE);
    }

    public void drawImage(Image img, int x, int y){
        this.drawImage(img,x,y,img.getImageWidth(),img.getImageHeight(),Color.WHITE);
    }

    public void drawImage(Image img, int x, int y, float width, float height, Color c){
        img.setLocation(x,y);
        img.setImageSize(width,height);
        // TODO SUPPORT TINTING
        this.getDrawBatch().begin();
        img.draw(this.getDrawBatch());
        this.getDrawBatch().end();
    }

    public void drawImage(Image img, int x, int y, float width, float height){
        img.setLocation(x,y);
        img.setImageSize(width,height);
        this.getDrawBatch().begin();
        img.draw(this.getDrawBatch());
        this.getDrawBatch().end();
    }

    public void drawLine(int x1, int y1, int x2, int y2){
        this.getShapeRenderer().begin(ShapeRenderer.ShapeType.Line);
        this.getShapeRenderer().setColor(this.getColor().toGDXColor());
        this.getShapeRenderer().line(x1,y1,x2,y2);
        this.getShapeRenderer().end();
    }

    public void drawTriangle(int[] points, int x, int y){
        if(points.length != 6) return;
        this.getShapeRenderer().begin(ShapeRenderer.ShapeType.Line);
        this.getShapeRenderer().setColor(this.getColor().toGDXColor());
        this.getShapeRenderer().triangle(
                points[0], points[1],
                points[2], points[3],
                points[4], points[5]
        );
        this.getShapeRenderer().end();
    }

    public void fillTriangle(int[] points, int x, int y){
        if(points.length != 6) return;
        this.getShapeRenderer().begin(ShapeRenderer.ShapeType.Filled);
        this.getShapeRenderer().setColor(this.getColor().toGDXColor());
        this.getShapeRenderer().triangle(
                x + points[0], y + points[1],
                x + points[2], y + points[3],
                x + points[4], y + points[5]
        );
        this.getShapeRenderer().end();
    }

    public void drawPixel(int x, int y){
        this.getShapeRenderer().begin(ShapeRenderer.ShapeType.Filled);
        this.getShapeRenderer().setColor(this.getColor().toGDXColor());
        this.getShapeRenderer().rect(x,y,1,1);
        this.getShapeRenderer().end();
    }

    // Source - https://stackoverflow.com/questions/12548532/how-to-flip-a-pixmap-to-draw-to-a-texture-in-libgdx
    public static Pixmap flipPixmap(Pixmap src, boolean flipx, boolean flipy) {
        final int width = src.getWidth();
        final int height = src.getHeight();
        Pixmap flipped = new Pixmap(width, height, src.getFormat());

        if(!flipx && !flipy){
            return src;
        }

        if(flipx) {
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    flipped.drawPixel(x, y, src.getPixel(width - x - 1, y));
                }
            }
        }
        if(flipy){
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    flipped.drawPixel(x, y, src.getPixel( x, height - y - 1));
                }
            }
        }
        return flipped;
    }
}
