package io.infinitestrike.flatpixel.entity;

import com.badlogic.gdx.math.*;

import java.util.HashMap;

import io.infinitestrike.flatpixel.grafx.Graphics;
import io.infinitestrike.flatpixel.grafx.View;
import io.infinitestrike.flatpixel.state.RenderState;
import io.infinitestrike.flatpixel.util.TiledMapObject;

public abstract class MapEntity extends Entity{

    private float offsetX = 0, offsetY = 0;
    private float scale = 1;
    private HashMap<String,Object> properties = null;

    public MapEntity(float x, float y) {
        super(x + 1, y + 1);
    }
    public MapEntity(float x, float y, float w, float h) {
        super(x + 1, y + 1, w, h);
    }
    public MapEntity() {
        super(0,0);
    }

    public void setOffsetX(float x){
        this.offsetX = x;
    }

    public void setOffsetY(float y){
        this.offsetY = y;
    }

    public float getOffsetX(){
        return this.offsetX;
    }

    public float getOffsetY(){
        return this.offsetY;
    }

    public void setLocalX(float x){
        this.x = x;
    }

    public void setLocalY(float y){
        this.y = y;
    }

    public float getLocalX(){
        return this.x;
    }

    public float getLocalY(){
        return this.y;
    }

    public float getX(){
        return this.x + offsetX;
    }

    public float getY(){
        return this.y + offsetY;
    }

    public float getLocalWidth(){
        return this.width;
    }

    public float getLocalHeight(){
        return this.height;
    }

    public float getScale(){
        return this.scale;
    }

    public void setScale(float scale){
        this.scale = scale;
    }

    public float getWidth(){
        return this.width * scale;
    }

    public float getHeight(){
        return this.height * scale;
    }

    public void setProperties(HashMap<String,Object> properties){
        this.properties = properties;
    }

    public Object getProperty(String name, Object fallback){
        if(this.properties != null){
            if(this.properties.containsKey(name)) return name;
            return fallback;
        }
        return fallback;
    }

    public <T> T getProperty(String name, T fallback, Class<T> clazz){
        if(this.properties != null){
            if(this.properties.containsKey(name)) return clazz.cast(this.properties.get(name));
            return fallback;
        }

        return fallback;
    }

    public boolean hasProperty(String name){
        if(this.properties != null){
            return this.properties.containsKey(name);
        }
        return false;
    }

    public HashMap<String, Object> getObjectProperties() {
        return this.properties;
    }

    @Override
    public Rectangle getBounds(){
        // will allow for map coords
        return new Rectangle(this.getX(),this.getY(),this.getWidth(),this.getHeight());
    }

    public abstract void onMapEntityLoaded(RenderState state);
}
