package io.infinitestrike.flatpixel.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Disposable;

import io.infinitestrike.flatpixel.core.InputManager;
import io.infinitestrike.flatpixel.core.LogBot;
import io.infinitestrike.flatpixel.grafx.Color;
import io.infinitestrike.flatpixel.grafx.Image;
import io.infinitestrike.flatpixel.grafx.View;
import io.infinitestrike.flatpixel.state.RenderState;

public class UILayer implements Disposable{

    public static final int MAX_UI_OBJECTS = 100;

    private View layerView;
    private UIObject[] objects = new UIObject[MAX_UI_OBJECTS];
    private RenderState parent = null;

    private Image renderedImage = null;

    public UILayer(){
        // we will create this at the initial scale
        // could create an update method to dispose the View and
        // make a new one with a new size.
        this.layerView = new View(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        this.renderedImage = this.render();
    }

    public void update(){
        for(int i = 0; i < objects.length; i++){
            UIObject obj = objects[i];
            if(obj != null){
                obj.tick(Gdx.graphics.getDeltaTime());
            }
        }

        this.renderedImage = this.render();
    }

    private Image render(){
        layerView.begin();
        layerView.getGraphics().clear(new Color(0,0,0,0));

        for(int i = 0; i < objects.length; i++){
            UIObject obj = objects[i];
            if(obj != null){
                obj.render(layerView.getGraphics());
            }
        }
        layerView.end();

        return layerView.getImage();
    }

    public Image getImage(){
        return this.renderedImage;
    }

    public void addUIObject(UIObject object){
        for(int i = 0; i < MAX_UI_OBJECTS; i++){
            if(objects[i] == null){
                objects[i] = object;
                object.objectID = i;
                object.onUICreate();
            }
        }
    }

    public void removeUIObject(UIObject object){
        int objectIndex = object.objectID;
        if(objectIndex >= 0 && objectIndex < MAX_UI_OBJECTS - 1){
            object.objectID = -1;
            object.dispose();
            objects[objectIndex] = null;
        }
    }

    @Override
    public void dispose(){
        this.layerView.dispose();
    }

    public RenderState getParent() {
        return parent;
    }

    public void setParent(RenderState parent) {
        this.parent = parent;
    }
}
