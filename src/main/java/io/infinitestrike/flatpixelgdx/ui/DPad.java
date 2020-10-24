package io.infinitestrike.flatpixelgdx.ui;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.InputListener;

import java.util.ArrayList;
import java.util.HashMap;

import io.infinitestrike.flatpixelgdx.core.InputManager;
import io.infinitestrike.flatpixelgdx.core.LogBot;
import io.infinitestrike.flatpixelgdx.entity.Entity;
import io.infinitestrike.flatpixelgdx.grafx.Graphics;
import io.infinitestrike.flatpixelgdx.grafx.Image;
import io.infinitestrike.flatpixelgdx.state.RenderState;

import static io.infinitestrike.flatpixelgdx.core.InputManager.InputType.*;

public class DPad extends UIEntity {

    private Image imgDPadUp = new Image(0x000000,128,128),
    imgDPadDown = imgDPadUp,
    imgDPadLeft = imgDPadUp,
    imgDPadRight = imgDPadUp,
    imgDpadCenter = imgDPadUp;

    int xo = 32, yo = 32;

    private Rectangle upButtonBoundingBox = new Rectangle(64 * 2  + xo,64 * 4  + yo,128,128),
    downButtonBoundingBox = new Rectangle(64 * 2 + xo,64 * 0 + yo,128,128),
    leftButtonBoundingBox =  new Rectangle(64 * 0 + xo,64 * 2 + yo,128,128),
    rightButtonBoundingBox = new Rectangle(64 * 4 + xo,64 * 2 + yo,128,128),
    centerButtonBoundingBox = new Rectangle(64 * 2 + xo,64 * 2 + yo,128,128);

    private InputManager.InputEventManager m = new InputManager.InputEventManager();
    private ArrayList<DPadEvent> dPadListeners = new ArrayList<DPadEvent>();
    private boolean[] buttons = new boolean[5];

    public enum DPadDirection{
        UP,DOWN,LEFT,RIGHT,CENTER,SINGLE // DPadDirection will be used with other classes, so SINGLE
                                         // is just for one button press.
    }

    public DPad(){
        super(0,0,0,0);
    }

    @Override
    public void onEntityCreate(RenderState s) {
        m.define("up", new InputManager.InputCondition() {
            @Override
            public boolean testCondition(InputManager m, InputManager.InputType t) {
                if(m.getPointers().size() > 0){
                    for(InputManager.Pointer p : m.getPointers()){
                        if(upButtonBoundingBox.contains(p.x,p.y) && (t == TOUCH_DOWN || t == TOUCH_DRAG)){
                            return true;
                        }
                    }
                }
                return false;
            }
        });
        m.define("down", new InputManager.InputCondition() {
            @Override
            public boolean testCondition(InputManager m, InputManager.InputType t) {
                if(m.getPointers().size() > 0){
                    for(InputManager.Pointer p : m.getPointers()){
                        if(downButtonBoundingBox.contains(p.x,p.y) && (t == TOUCH_DOWN || t == TOUCH_DRAG)){
                            return true;
                        }
                    }
                }
                return false;
            }
        });
        m.define("left", new InputManager.InputCondition() {
            @Override
            public boolean testCondition(InputManager m, InputManager.InputType t) {
                if(m.getPointers().size() > 0){
                    for(InputManager.Pointer p : m.getPointers()){
                        if(leftButtonBoundingBox.contains(p.x,p.y) && (t == TOUCH_DOWN || t == TOUCH_DRAG)){
                            return true;
                        }
                    }
                }
                return false;
            }
        });
        m.define("right", new InputManager.InputCondition() {
            @Override
            public boolean testCondition(InputManager m, InputManager.InputType t) {
                if(m.getPointers().size() > 0){
                    for(InputManager.Pointer p : m.getPointers()){
                        if(rightButtonBoundingBox.contains(p.x,p.y) && (t == TOUCH_DOWN || t == TOUCH_DRAG)){
                            return true;
                        }
                    }
                }
                return false;
            }
        });
        m.define("center", new InputManager.InputCondition() {
            @Override
            public boolean testCondition(InputManager m, InputManager.InputType t) {
                if(m.getPointers().size() > 0){
                    for(InputManager.Pointer p : m.getPointers()){
                        if(centerButtonBoundingBox.contains(p.x,p.y) && (t == TOUCH_DOWN || t == TOUCH_DRAG)){
                            return true;
                        }
                    }
                }
                return false;
            }
        });
        // Release
        m.define("up-release", new InputManager.InputCondition() {
            @Override
            public boolean testCondition(InputManager m, InputManager.InputType t) {
                if(t == TOUCH_UP || t == TOUCH_DRAG){
                    for(InputManager.Pointer p : m.getPointers()){
                        if(upButtonBoundingBox.contains(p.x,p.y)){
                            return false;
                        }
                    }
                    return true;
                }
                return false;
            }
        });
        m.define("down-release", new InputManager.InputCondition() {
            @Override
            public boolean testCondition(InputManager m, InputManager.InputType t) {
                if(t == TOUCH_UP || t == TOUCH_DRAG){
                    for(InputManager.Pointer p : m.getPointers()){
                        if(downButtonBoundingBox.contains(p.x,p.y)){
                            return false;
                        }
                    }
                    return true;
                }
                return false;
            }
        });
        m.define("left-release", new InputManager.InputCondition() {
            @Override
            public boolean testCondition(InputManager m, InputManager.InputType t) {
                if(t == TOUCH_UP || t == TOUCH_DRAG){
                    for(InputManager.Pointer p : m.getPointers()){
                        if(leftButtonBoundingBox.contains(p.x,p.y)){
                            return false;
                        }
                    }
                    return true;
                }
                return false;
            }
        });
        m.define("right-release", new InputManager.InputCondition() {
            @Override
            public boolean testCondition(InputManager m, InputManager.InputType t) {
                if(t == TOUCH_UP || t == TOUCH_DRAG){
                    for(InputManager.Pointer p : m.getPointers()){
                        if(rightButtonBoundingBox.contains(p.x,p.y)){
                            return false;
                        }
                    }
                    return true;
                }
                return false;
            }
        });
        m.define("center-release", new InputManager.InputCondition() {
            @Override
            public boolean testCondition(InputManager m, InputManager.InputType t) {
                if(t == TOUCH_UP || t == TOUCH_DRAG){
                    for(InputManager.Pointer p : m.getPointers()){
                        if(centerButtonBoundingBox.contains(p.x,p.y)){
                            return false;
                        }
                    }
                    return true;
                }
                return false;
            }
        });
        // ===================================================

        m.bind(new InputManager.InputEvent("up") {
            @Override
            public void onEvent(InputManager m) {
                for(DPadEvent e : dPadListeners){
                    e.onDPadPress(DPadDirection.UP, true);
                }
                buttons[DPadDirection.UP.ordinal()] = true;
            }
        });

        m.bind(new InputManager.InputEvent("down") {
            @Override
            public void onEvent(InputManager m) {
                for(DPadEvent e : dPadListeners){
                    e.onDPadPress(DPadDirection.DOWN, true);
                }
                buttons[DPadDirection.DOWN.ordinal()] = true;
            }
        });

        m.bind(new InputManager.InputEvent("left") {
            @Override
            public void onEvent(InputManager m) {
                for(DPadEvent e : dPadListeners){
                    e.onDPadPress(DPadDirection.LEFT, true);
                }
                buttons[DPadDirection.LEFT.ordinal()] = true;
            }
        });

        m.bind(new InputManager.InputEvent("right") {
            @Override
            public void onEvent(InputManager m) {
                for(DPadEvent e : dPadListeners){
                    e.onDPadPress(DPadDirection.RIGHT,true);
                }
                buttons[DPadDirection.RIGHT.ordinal()] = true;
            }
        });

        m.bind(new InputManager.InputEvent("center") {
            @Override
            public void onEvent(InputManager m) {
                for(DPadEvent e : dPadListeners){
                    e.onDPadPress(DPadDirection.CENTER,true);
                }
                buttons[DPadDirection.CENTER.ordinal()] = true;
            }
        });

        m.bind(new InputManager.InputEvent("up-release") {
            @Override
            public void onEvent(InputManager m) {
                for(DPadEvent e : dPadListeners){
                    if(buttons[DPadDirection.UP.ordinal()]) {
                        e.onDPadPress(DPadDirection.UP, false);
                    }
                }
                buttons[DPadDirection.UP.ordinal()] = false;
            }
        });

        m.bind(new InputManager.InputEvent("down-release") {
            @Override
            public void onEvent(InputManager m) {
                for(DPadEvent e : dPadListeners){
                    if(buttons[DPadDirection.DOWN.ordinal()]) {
                        e.onDPadPress(DPadDirection.DOWN, false);
                    }
                }
                buttons[DPadDirection.DOWN.ordinal()] = false;
            }
        });

        m.bind(new InputManager.InputEvent("left-release") {
            @Override
            public void onEvent(InputManager m) {
                for(DPadEvent e : dPadListeners){
                    if(buttons[DPadDirection.LEFT.ordinal()]) {
                        e.onDPadPress(DPadDirection.LEFT, false);
                    }
                }
                buttons[DPadDirection.LEFT.ordinal()] = false;
            }
        });

        m.bind(new InputManager.InputEvent("right-release") {
            @Override
            public void onEvent(InputManager m) {
                for(DPadEvent e : dPadListeners){
                    if(buttons[DPadDirection.RIGHT.ordinal()]) {
                        e.onDPadPress(DPadDirection.RIGHT, false);
                    }
                }
                buttons[DPadDirection.RIGHT.ordinal()] = false;
            }
        });

        m.bind(new InputManager.InputEvent("center-release") {
            @Override
            public void onEvent(InputManager m) {
                for(DPadEvent e : dPadListeners){
                    if(buttons[DPadDirection.CENTER.ordinal()]) {
                        e.onDPadPress(DPadDirection.CENTER, false);
                    }
                }
                buttons[DPadDirection.CENTER.ordinal()] = false;
            }
        });
    }

    public boolean getButton(int index){
        return this.buttons[index];
    }

    public boolean[] getButtons(){
        return this.buttons;
    }

    @Override
    public void onEntityUpdate(RenderState s, float deltatime) {

    }

    @Override
    public void onEntityRender(RenderState s, Graphics g) {
        g.drawImage( // UP Button
                this.imgDPadUp,
                (int) this.upButtonBoundingBox.x,
                (int) this.upButtonBoundingBox.y,
                1
        );
        g.drawImage( // DOWN Button
                this.imgDPadDown,
                (int) this.downButtonBoundingBox.x,
                (int) this.downButtonBoundingBox.y,
                1
        );
        g.drawImage( // LEFT Button
                this.imgDPadLeft,
                (int) this.leftButtonBoundingBox.x,
                (int) this.leftButtonBoundingBox.y,
                1
        );
        g.drawImage( // RIGHT Button
                this.imgDPadRight,
                (int) this.rightButtonBoundingBox.x,
                (int) this.rightButtonBoundingBox.y,
                1
        );
        g.drawImage( // CENTER Button
                this.imgDpadCenter,
                (int) this.centerButtonBoundingBox.x,
                (int) this.centerButtonBoundingBox.y,
                1
        );
    }

    @Override
    public void onEntityDestroy(RenderState s) {
        this.m.dispose();
    }

    @Override
    public void onEntityCollide(Entity e) {

    }

    public void addDPadListener(DPadEvent e){this.dPadListeners.add(e);}
    public void removeDPadListener(DPadEvent e){this.dPadListeners.remove(e);}

    public interface DPadEvent{void onDPadPress(DPadDirection d, boolean down);}
}
