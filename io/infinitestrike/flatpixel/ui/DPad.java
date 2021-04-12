package io.infinitestrike.flatpixel.ui;

import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;

import io.infinitestrike.flatpixel.core.InputManager;
import io.infinitestrike.flatpixel.entity.Entity;
import io.infinitestrike.flatpixel.grafx.Graphics;
import io.infinitestrike.flatpixel.grafx.Image;
import io.infinitestrike.flatpixel.math.Vector;
import io.infinitestrike.flatpixel.state.RenderState;

import static io.infinitestrike.flatpixel.core.InputManager.InputType.TOUCH_DOWN;
import static io.infinitestrike.flatpixel.core.InputManager.InputType.TOUCH_DRAG;
import static io.infinitestrike.flatpixel.core.InputManager.InputType.TOUCH_UP;


public class DPad extends UIObject {

    private Image imgDPadUp = new Image(0x000000,128,128),
    imgDPadDown = imgDPadUp,
    imgDPadLeft = imgDPadUp,
    imgDPadRight = imgDPadUp,
    imgDpadCenter = imgDPadUp;

    private int xo = 32, yo = 32;

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

    public DPad(){};

    public void setOffset(int x, int y){
        this.xo = x;
        this.yo = y;
        // recalculate boundingboxes
        upButtonBoundingBox = new Rectangle(64 * 2  + xo,64 * 4  + yo,128,128);
        downButtonBoundingBox = new Rectangle(64 * 2 + xo,64 * 0 + yo,128,128);
        leftButtonBoundingBox =  new Rectangle(64 * 0 + xo,64 * 2 + yo,128,128);
        rightButtonBoundingBox = new Rectangle(64 * 4 + xo,64 * 2 + yo,128,128);
        centerButtonBoundingBox = new Rectangle(64 * 2 + xo,64 * 2 + yo,128,128);
    }

    public Vector.Vector2i getOffset(){
        return new Vector.Vector2i(xo,yo);
    }

    @Override
    public void onUICreate() {
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
    public void onUIUpdate(float deltatime) {

    }

    @Override
    public void onUIRender(Graphics g) {
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
    public void onUIDestroy() {
        this.m.dispose();
    }


    public void setButtonImages(Image up, Image down, Image left, Image right, Image center){
        this.imgDPadUp = (up != null) ? up : new Image(0x00000000,128,128);
        this.imgDPadDown = (down != null) ? down : new Image(0x00000000,128,128);
        this.imgDPadLeft = (left != null) ? left : new Image(0x00000000,128,128);
        this.imgDPadRight = (right != null) ? right : new Image(0x00000000,128,128);
        this.imgDpadCenter = (center != null) ? center : new Image(0x00000000,128,128);
    }

    public void addDPadListener(DPadEvent e){this.dPadListeners.add(e);}
    public void removeDPadListener(DPadEvent e){this.dPadListeners.remove(e);}

    public interface DPadEvent{void onDPadPress(DPadDirection d, boolean down);}
}
