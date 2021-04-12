package io.infinitestrike.flatpixel.grafx;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.TextureData;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

public class Graphics {

    private ShapeRenderer surfaceRenderer;
    private SpriteBatch surfaceBetaBatch;
    private View surfaceView;

    // properties
    private Color drawColor = Color.BLACK;
    private Font drawFont = new Font(24,true);

    public static Graphics getGraphics(View v){
        Graphics g = new Graphics();
        g.surfaceView = v;
        g.surfaceBetaBatch = v.getBetaBatch();
        g.surfaceRenderer = v.getRenderer();
        g.init();
        return g;
    }

    public View getSurfaceView(){
        return this.surfaceView;
    }

    private void init(){
        Gdx.gl.glEnable(Gdx.gl.GL_BLEND);
        Gdx.gl.glBlendFunc(Gdx.gl.GL_SRC_ALPHA, Gdx.gl.GL_ONE_MINUS_SRC_ALPHA);
    }

    public void dispose(){
        Gdx.gl.glDisable(Gdx.gl.GL_BLEND);
    }

    //==============================================================================================

    public Font getFont(){
        return this.drawFont;
    }

    public void setFont(Font f){
        this.drawFont = f;
    }

    public Color getColor(){
        return this.drawColor;
    }

    public void setColor(Color c){
        this.drawColor = c;
    }

    public void clear(Color c){
        Gdx.gl.glClearColor(c.getRed(),c.getGreen(),c.getBlue(),c.getAlpha());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
    }

    public void setView(Rectangle rectangle){
        Gdx.gl.glViewport(
                (int)rectangle.x,
                (int)rectangle.y,
                (int)rectangle.getWidth(),
                (int)rectangle.getHeight()
        );
    }

    public void clear(){
        this.clear(Color.BLACK);
    }

    public void drawRect(Rectangle rectangle){
        this.surfaceRenderer.begin(ShapeRenderer.ShapeType.Line);
        this.surfaceRenderer.setColor(this.getColor().toGDXColor());
        this.surfaceRenderer.rect(rectangle.x,rectangle.y,rectangle.width,rectangle.height);
        this.surfaceRenderer.end();
    }

    public void drawRect(float x, float y, float w, float h){
        this.surfaceRenderer.begin(ShapeRenderer.ShapeType.Line);
        this.surfaceRenderer.setColor(this.getColor().toGDXColor());
        this.surfaceRenderer.rect(x,y,w,h);
        this.surfaceRenderer.end();
    }

    public void fillRect(Rectangle rectangle){
        this.surfaceRenderer.begin(ShapeRenderer.ShapeType.Filled);
        this.surfaceRenderer.setColor(this.getColor().toGDXColor());
        this.surfaceRenderer.rect(rectangle.x,rectangle.y,rectangle.width,rectangle.height);
        this.surfaceRenderer.end();
    }

    public void fillRect(float x, float y, float w, float h){
        this.surfaceRenderer.begin(ShapeRenderer.ShapeType.Filled);
        this.surfaceRenderer.setColor(this.getColor().toGDXColor());
        this.surfaceRenderer.rect(x,y,w,h);
        this.surfaceRenderer.end();
    }

    public void drawLine(float x1, float y1, float x2, float y2){
        this.surfaceRenderer.begin(ShapeRenderer.ShapeType.Line);
        this.surfaceRenderer.setColor(this.getColor().toGDXColor());
        this.surfaceRenderer.line(x1,y1,x2,y2);
        this.surfaceRenderer.end();
    }

    public void drawString(String text, float x, float y){
        if(this.drawFont != null){
            this.surfaceBetaBatch.begin();
            this.drawFont.getInternalFont().setColor(this.getColor().toGDXColor());
            this.drawFont.getInternalFont().draw(this.surfaceBetaBatch,text,x,y);
            this.surfaceBetaBatch.end();
        }
    }

    public void drawImage(Image img, float x, float y, float w, float h){
        float ilx = img.getImageLocationX();
        float ily = img.getImageLocationY();
        float iw = img.getImageWidth();
        float ih = img.getImageHeight();

        img.setBounds((int)x,(int)y,(int)w,(int)h);
        this.surfaceBetaBatch.begin();
        img.draw(surfaceBetaBatch);
        this.surfaceBetaBatch.end();

        img.setBounds((int)ilx,(int)ily,(int)iw,(int)ih);
    }

    public void drawImage(Image img, float x, float y){
        float ilx = img.getImageLocationX();
        float ily = img.getImageLocationY();

        img.setLocation((int)x,(int)y);
        this.surfaceBetaBatch.begin();
        img.draw(surfaceBetaBatch);
        this.surfaceBetaBatch.end();

        img.setLocation((int)ilx,(int)ily);
    }

    public void drawImage(Image img, float x, float y, float scale){
        float ilx = img.getImageLocationX();
        float ily = img.getImageLocationY();
        float scalex = img.getScaleX();
        float scaley = img.getScaleY();

        img.setLocation((int)x,(int)y);
        img.setScaleX(scale * scalex);
        img.setScaleY(scale * scaley);

        this.surfaceBetaBatch.begin();
        img.draw(surfaceBetaBatch);
        this.surfaceBetaBatch.end();

        img.setLocation((int)ilx,(int)ily);
        img.setScaleX(scalex);
        img.setScaleY(scaley);
    }

    public void draw(Drawable d){
        this.surfaceBetaBatch.begin();
        d.draw(this.surfaceBetaBatch);
        this.surfaceBetaBatch.end();
        this.surfaceRenderer.begin(ShapeRenderer.ShapeType.Filled);
        d.drawFilled(this.surfaceRenderer);
        this.surfaceRenderer.end();
        this.surfaceRenderer.begin(ShapeRenderer.ShapeType.Line);
        d.drawOutline(this.surfaceRenderer);
        this.surfaceRenderer.end();
    }

    public void fillEllipse(int x, int y, int w, int h){
        this.surfaceRenderer.setColor(this.getColor().toGDXColor());
        this.surfaceRenderer.begin(ShapeRenderer.ShapeType.Filled);
        this.surfaceRenderer.ellipse(x,y,w,h);
        this.surfaceRenderer.end();
    }

    public void drawEllipse(int x, int y, int w, int h){
        this.surfaceRenderer.setColor(this.getColor().toGDXColor());
        this.surfaceRenderer.begin(ShapeRenderer.ShapeType.Line);
        this.surfaceRenderer.ellipse(x,y,w,h);
        this.surfaceRenderer.end();
    }

    public void drawPixel(int x, int y){
        this.surfaceRenderer.begin(ShapeRenderer.ShapeType.Filled);
        this.surfaceRenderer.setColor(this.getColor().toGDXColor());
        this.surfaceRenderer.rect(x,y,1,1);
        this.surfaceRenderer.end();
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

    public static void copyTexture(Texture src, Texture dest){
        if(src.getWidth() != dest.getWidth() || src.getHeight() != dest.getHeight()){
            throw new IllegalArgumentException("Textures are not of the same size.");
        }
        TextureData srcData = src.getTextureData();
        dest.load(srcData);
    }

    public static Texture duplicateTexture(Texture src){
        Texture out = new Texture(src.getWidth(),src.getHeight(), Pixmap.Format.RGBA4444);
        out.load(src.getTextureData());
        return out;
    }

}
