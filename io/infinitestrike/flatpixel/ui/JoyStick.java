package io.infinitestrike.flatpixel.ui;

import com.badlogic.gdx.math.Rectangle;

import io.infinitestrike.flatpixel.core.Core;
import io.infinitestrike.flatpixel.core.InputManager;
import io.infinitestrike.flatpixel.entity.Entity;
import io.infinitestrike.flatpixel.grafx.Color;
import io.infinitestrike.flatpixel.grafx.Graphics;
import io.infinitestrike.flatpixel.grafx.Image;
import io.infinitestrike.flatpixel.math.Vector;
import io.infinitestrike.flatpixel.state.RenderState;


public class JoyStick extends UIObject {

    public static final float DEADZONE_NONE = -255;

    private float xPos, yPos, size, nubScale;
    private float xAxis = 0, yAxis = 0, deadZone = 0.2f;
    private boolean isActive = false;
    private Vector.Vector2f defaultPosition = null;
    private Image nubImage = null, backgroundImage = null;

    private InputManager.InputEventManager input = new InputManager.InputEventManager();

    public enum JoyStickDirection{
        UP, DOWN, LEFT, RIGHT, NEUTRAL
    }

    public JoyStick(float x, float y, float size, float nubscale) {
        this.xPos = x;
        this.yPos = y;
        this.size = size;
        this.nubScale = nubscale;
        this.defaultPosition = new Vector.Vector2f(xPos - (size * nubScale) / 2, yPos - (size * nubScale) / 2);
    }

    @Override
    public void onUICreate() {

        input.define("active", new InputManager.InputCondition() {
            @Override
            public boolean testCondition(InputManager m, InputManager.InputType t) {
                if(t == InputManager.InputType.TOUCH_DOWN || t == InputManager.InputType.TOUCH_DRAG) {
                    for (InputManager.Pointer p : m.getPointers()) {
                        if (new Rectangle(xPos - (size / 2), yPos - (size / 2), size, size).contains(p.x, p.y)) {
                            return true;
                        }
                    }
                }
                return false;
            }
        });

        input.define("inactive", new InputManager.InputCondition() {
            @Override
            public boolean testCondition(InputManager m, InputManager.InputType t) {
                if(isActive && t == InputManager.InputType.TOUCH_DRAG){
                    return false;
                }
                if(t == InputManager.InputType.TOUCH_UP){
                    if(m.getPointers().size() == 0) return true;
                    for(InputManager.Pointer p : m.getPointers()){
                        if (new Rectangle(xPos - (size / 2), yPos - (size / 2), size, size).contains(p.x, p.y)) {
                            return false;
                        }
                    }
                }
                return true;
            }
        });

        input.bind(new InputManager.InputEvent("active") {
            @Override
            public void onEvent(InputManager m) {
                isActive = true;
            }
        });

        input.bind(new InputManager.InputEvent("inactive") {
            @Override
            public void onEvent(InputManager m) {
                if(isActive){
                    isActive = false;
                }
            }
        });
    }

    @Override
    public void onUIUpdate(float deltatime) {
        InputManager manager = InputManager.getInstance();
        //InputManager.Pointer point = manager.getFirstAvailablePointer();
        InputManager.Pointer point = manager.getClosestPointer(this.xPos,this.yPos,-1);
        if(this.isActive){
            if(point != null){
                if (getDistance(point.x,point.y) <= this.size / 2) {
                    this.defaultPosition = new Vector.Vector2f(point.x- (size * nubScale) / 2,
                            point.y - (size * nubScale) / 2);
                } else {
                    Vector.Vector2f max = getMaximumBounds(point.x,point.y);
                    this.defaultPosition = new Vector.Vector2f(max.x - (size * nubScale) / 2, max.y - (size * nubScale) / 2);
                }

                Vector.Vector2f move = calculateMove(point.x, point.y);
                xAxis = move.x;
                yAxis = move.y;
            }else{
                this.defaultPosition = new Vector.Vector2f(xPos - (size * nubScale) / 2, yPos - (size * nubScale) / 2);
                xAxis = 0;
                yAxis = 0;
            }
            if(this.deadZone != JoyStick.DEADZONE_NONE) {
                if (Math.abs(xAxis) < this.deadZone) xAxis = 0;
                if (Math.abs(yAxis) < this.deadZone) yAxis = 0;
            }
        }else{
            this.defaultPosition = new Vector.Vector2f(xPos - (size * nubScale) / 2, yPos - (size * nubScale) / 2);
            xAxis = 0;
            yAxis = 0;
        }
    }

    @Override
    public void onUIRender(Graphics g) {

        g.setColor(Color.WHITE);
        if(this.backgroundImage != null){
            g.drawImage(this.backgroundImage,(int)(xPos - (size/2)),(int)(yPos - (size/2)),(int)size,(int)size);
        }else {
            g.drawEllipse((int) (xPos - (size / 2)), (int) (yPos - (size / 2)), (int) size, (int) size);
        }

        if(this.nubImage != null) {
            g.drawImage(this.nubImage,(int) this.defaultPosition.x, (int) this.defaultPosition.y,Math.round(size * nubScale),
                    Math.round(size * this.nubScale));
        }else{
            g.fillEllipse((int) (this.defaultPosition.x), (int) (this.defaultPosition.y), Math.round(size * this.nubScale),
                    Math.round(size * this.nubScale));
        }
    }

    @Override
    public void onUIDestroy() {
        input.dispose();
    }

    public JoyStickDirection getCardinalDirection(){
        if(this.getXAxis() == 0 && this.getYAxis() == 0){
            return JoyStickDirection.NEUTRAL;
        }else{
            InputManager.Pointer p = InputManager.getInstance().getClosestPointer(this.xPos,this.yPos,-1);
            if(p != null){
                float angle  = this.getAngle(p.x,p.y);
                if(angle < 0) angle += 360;
                if(angle < 45 || angle > 315) return JoyStickDirection.RIGHT;
                if(angle > 45 && angle < 135) return JoyStickDirection.UP;
                if(angle > 135 && angle < 225) return JoyStickDirection.LEFT;
                if(angle > 225 && angle < 315) return JoyStickDirection.DOWN;
            }
        }
        return  JoyStickDirection.NEUTRAL;
    }

    public float getXAxis(){
        return this.xAxis;
    }

    public float getYAxis(){
        return this.yAxis;
    }

    public float getDeadZone(){
        return this.deadZone;
    }

    public void setDeadZone(float f){
        this.deadZone = Core.MathFunctions.fclamp(f,-1,1);
    }

    public void setJoystickImage(Image nubImage, Image backgroundImage){
        this.nubImage = nubImage;
        this.backgroundImage = backgroundImage;
    }

    private float getAngle(float x, float y) {
        return (float) Math.toDegrees(Math.atan2(this.yPos - y, x - this.xPos));
    }

    public Vector.Vector2f getMaximumBounds(float x, float y) {
        Vector.Vector2f maximumPoint = getLocation(x,y);
        return new Vector.Vector2f((xPos + maximumPoint.x * (this.size / 2)), (yPos + maximumPoint.y * (this.size / 2)));
    }

    public Vector.Vector2f calculateMove(float x, float y) {
        Vector.Vector2f location = getLocation(x,y);
        float distance = getDistance(x,y) / (this.size / 2);
        return new Vector.Vector2f(
                Core.MathFunctions.fclamp((float) Math.floor((location.x * distance) * 100) / 100, -1, 1),
                Core.MathFunctions.fclamp((float) Math.floor((location.y * distance) * 100) / 100, -1, 1));
    }

    private Vector.Vector2f getLocation(float x, float y) {
        double angle = getAngle(x,y);
        return new Vector.Vector2f((float) Math.cos(Math.toRadians(angle)), (float) -Math.sin(Math.toRadians(angle)));
    }

    private float getDistance(float x, float y) {
        return (float) Math.sqrt(Math.pow((this.xPos - x), 2) + Math.pow((this.yPos - y), 2));
    }
}
