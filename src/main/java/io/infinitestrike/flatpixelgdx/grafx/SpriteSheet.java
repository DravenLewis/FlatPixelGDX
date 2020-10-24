package io.infinitestrike.flatpixelgdx.grafx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;


// Can Maybe use code from FlatPixelMapEditor
public class SpriteSheet {

    private final int tileSizeX,tileSizeY;
    private final Texture texture;

    public SpriteSheet(String path, int tsx, int tsy){
        this.texture = new Texture(Gdx.files.internal(path));
        this.tileSizeX = tsx;
        this.tileSizeY = tsy;
    }

    public Image getTile(int tileX, int tileY){
        if(tileX * this.tileSizeX + tileSizeX > texture.getWidth()) return null;
        if(tileY * this.tileSizeY + tileSizeY > texture.getHeight()) return null;
        if(tileX < 0 || tileY < 0) return null;

        TextureRegion region = new TextureRegion(this.texture,tileX * tileSizeY, tileY * tileSizeY,tileSizeX,tileSizeY);
        return new Image(new com.badlogic.gdx.scenes.scene2d.ui.Image(region));
    }

    public int getHeight(){
        return texture.getHeight();
    }

    public int getWidth(){
        return texture.getWidth();
    }

    public int getHorizontalCellCount(){
        return this.getWidth() / this.tileSizeX;
    }

    public int getVerticalCellCount(){
        return this.getHeight() / this.tileSizeY;
    }

    public int getTileSizeX(){
        return this.tileSizeX;
    }

    public int getTileSizeY(){
        return this.tileSizeY;
    }
}
