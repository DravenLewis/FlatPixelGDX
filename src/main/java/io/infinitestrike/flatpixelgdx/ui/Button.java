package io.infinitestrike.flatpixelgdx.ui;

import com.badlogic.gdx.math.Rectangle;


import java.util.ArrayList;

import io.infinitestrike.flatpixelgdx.core.InputManager;
import io.infinitestrike.flatpixelgdx.entity.Entity;
import io.infinitestrike.flatpixelgdx.event.ActionEvent;
import io.infinitestrike.flatpixelgdx.event.ActionListener;
import io.infinitestrike.flatpixelgdx.grafx.Graphics;
import io.infinitestrike.flatpixelgdx.grafx.Image;
import io.infinitestrike.flatpixelgdx.state.RenderState;

import static io.infinitestrike.flatpixelgdx.core.InputManager.InputType.*;

public class Button extends UIEntity {

    private Rectangle buttonBoundingBox = null;
    private Image buttonImage = null;
    private String actionName = "";
    private InputManager.InputEventManager eventManager = new InputManager.InputEventManager();
    private ArrayList<ActionListener> actionListeners = new ArrayList<ActionListener>();
    private boolean isPressed =  false;

    public Button(Image icon, float x, float y, float w, float h) {
        super(x, y, w, h);
        this.buttonImage = icon;
        this.buttonBoundingBox = new Rectangle(x,y,w,h);
    }

    public Button(Image icon, float x, float y){
        this(icon,x,y,128,128);
    }

    public Button (float x, float y){
        this(new Image(0x000000,128,128),x,y,128,128);
    }

    @Override
    public void onEntityCreate(RenderState s) {

        eventManager.FLAG_VERBOSE_OUTPUT = false;

        eventManager.define("down", new InputManager.InputCondition() {
            @Override
            public boolean testCondition(InputManager m, InputManager.InputType t) {
                if(m.getPointers().size() >= 1){
                    for(InputManager.Pointer p : m.getPointers()){
                        if(Button.this.buttonBoundingBox.contains(p.x,p.y) && (t == TOUCH_DOWN || t == TOUCH_DRAG)){
                            return true;
                        }
                    }
                }
                return false;
            }
        });
        eventManager.define("up", new InputManager.InputCondition() {
            @Override
            public boolean testCondition(InputManager m, InputManager.InputType t) {
                if(t == TOUCH_UP || t == TOUCH_DRAG){
                    for(InputManager.Pointer p : m.getPointers()){
                        if(Button.this.buttonBoundingBox.contains(p.x,p.y)){
                            return false;
                        }
                    }
                    return true;
                }
                return false;
            }
        });

        eventManager.bind(new InputManager.InputEvent("down") {
            @Override
            public void onEvent(InputManager m) {
                for(ActionListener a : actionListeners){
                    a.actionPerformed(new ActionEvent(Button.this,1,""));
                }
                isPressed = true;
            }
        });

        eventManager.bind(new InputManager.InputEvent("up") {
            @Override
            public void onEvent(InputManager m) {
                for(ActionListener a : actionListeners){
                    if(getButtonPressedState()){
                        a.actionPerformed(new ActionEvent(Button.this,0,""));
                    }
                }
                isPressed = false;
            }
        });
    }

    @Override
    public void onEntityUpdate(RenderState s, float deltatime) {

    }

    @Override
    public void onEntityRender(RenderState s, Graphics g) {
        g.drawImage(this.buttonImage,(int)this.getX(),(int)this.getY(),this.getWidth(),this.getHeight());
    }

    @Override
    public void onEntityDestroy(RenderState s) {
        this.eventManager.dispose();
    }

    @Override
    public void onEntityCollide(Entity e) {

    }

    public boolean getButtonPressedState(){
        return this.isPressed;
    }

    public void addActionListener(ActionListener e){this.actionListeners.add(e);}
    public void removeActionListenerListener(ActionListener e){this.actionListeners.remove(e);}

}
