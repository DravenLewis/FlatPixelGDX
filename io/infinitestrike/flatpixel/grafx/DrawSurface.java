package io.infinitestrike.flatpixel.grafx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.*;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.*;

public class DrawSurface extends Rectangle implements Disposable {

    private static int activeDrawSurfaces = 0;

    private final SpriteBatch betaBatch;
    private final ShapeRenderer renderer;
    private final FrameBuffer buffer;
    private int width, height;

    public DrawSurface(int width, int height){
        super(0,0,width,height);
        this.buffer = new FrameBuffer(Pixmap.Format.RGBA8888,width,height,true);
        this.betaBatch = new SpriteBatch();
        this.renderer = new ShapeRenderer();
        Matrix4 mat4 = new Matrix4();
        mat4.setToOrtho2D(0,0,width,height);
        betaBatch.setProjectionMatrix(mat4);
        betaBatch.enableBlending();
        renderer.setProjectionMatrix(mat4);
        activeDrawSurfaces++;
    }

    public void begin(){
        this.buffer.begin();
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
    }

    public void end(){
        this.buffer.end();
    }

    public void dispose(){
        this.renderer.dispose();
        this.betaBatch.dispose();
        this.buffer.dispose();
        activeDrawSurfaces--;
    }


    public ShapeRenderer getRenderer(){
        return this.renderer;
    }

    public SpriteBatch getBetaBatch(){
        return this.betaBatch;
    }

    public Image getImage(){
        Texture t = this.buffer.getColorBufferTexture();
        if(!t.getTextureData().isPrepared()){
            t.getTextureData().prepare();
        }
        t.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

        Image img = new Image(t);

        return img.getFlippedCopy(false,true);
    }

    public Rectangle getBounds(){
        return this;
    }

    public int getBufferWidth(){
        return this.width;
    }

    public int getBufferHeight(){
        return this.height;
    }

    public static int getActiveDrawSurfaceCount(){
        return DrawSurface.activeDrawSurfaces;
    }
}
