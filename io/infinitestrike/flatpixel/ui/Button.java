package io.infinitestrike.flatpixel.ui;

import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;

import io.infinitestrike.flatpixel.core.InputManager;
import io.infinitestrike.flatpixel.entity.Entity;
import io.infinitestrike.flatpixel.event.ActionListener;
import io.infinitestrike.flatpixel.event.Event;
import io.infinitestrike.flatpixel.grafx.Graphics;
import io.infinitestrike.flatpixel.grafx.Image;
import io.infinitestrike.flatpixel.state.RenderState;

import static io.infinitestrike.flatpixel.core.InputManager.InputType.TOUCH_DOWN;
import static io.infinitestrike.flatpixel.core.InputManager.InputType.TOUCH_DRAG;
import static io.infinitestrike.flatpixel.core.InputManager.InputType.TOUCH_UP;

public class Button{

    private Rectangle buttonBoundingBox = null;
    private Image buttonImage = null;
    private String actionName = "";
    private InputManager.InputEventManager eventManager = new InputManager.InputEventManager();
    private ArrayList<ActionListener> actionListeners = new ArrayList<ActionListener>();
    private boolean isPressed =  false;

    public Button(Image icon, float x, float y, float w, float h) {
        this.buttonImage = (icon == null) ? new Image(0x000000,(int)w,(int)h) : icon;
        this.buttonBoundingBox = new Rectangle(x,y,w,h);
    }

    public Button(Image icon, float x, float y){
        this(icon,x,y,128,128);
    }

    public Button (float x, float y){
        this(new Image(0x000000,128,128),x,y,128,128);
    }

    public void onUICreate(RenderState s) {

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
                    a.onEvent(new Event(Button.this,1,null));
                }
                isPressed = true;
            }
        });

        eventManager.bind(new InputManager.InputEvent("up") {
            @Override
            public void onEvent(InputManager m) {
                for(ActionListener a : actionListeners){
                    if(getButtonPressedState()){
                        a.onEvent(new Event(Button.this,0,null));
                    }
                }
                isPressed = false;
            }
        });
    }

    public void onUIUpdate(RenderState s, float deltatime) {

    }

    public void onUIRender(RenderState s, Graphics g) {
        g.drawImage(
                this.buttonImage,
                this.buttonBoundingBox.x,
                this.buttonBoundingBox.y,
                this.buttonBoundingBox.width,
                this.buttonBoundingBox.height
        );
    }

    public void onUIDestroy(RenderState s) {
        this.eventManager.dispose();
    }


    public boolean getButtonPressedState(){
        return this.isPressed;
    }

    public void addActionListener(ActionListener e){this.actionListeners.add(e);}
    public void removeActionListenerListener(ActionListener e){this.actionListeners.remove(e);}

}
