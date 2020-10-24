package io.infinitestrike.flatpixelgdx.ui;

import com.badlogic.gdx.math.Rectangle;

import io.infinitestrike.flatpixelgdx.core.Core;
import io.infinitestrike.flatpixelgdx.core.InputManager;
import io.infinitestrike.flatpixelgdx.entity.Entity;
import io.infinitestrike.flatpixelgdx.grafx.Color;
import io.infinitestrike.flatpixelgdx.grafx.Graphics;
import io.infinitestrike.flatpixelgdx.math.Vector;
import io.infinitestrike.flatpixelgdx.state.RenderState;

public class JoyStick extends UIEntity {

    private float xPos, yPos, size, nubScale;
    private float xAxis = 0, yAxis = 0, deadZone = 0.2f;
    private boolean isActive = false;
    private Vector.Vector2f defaultPosition = null;

    private InputManager.InputEventManager input = new InputManager.InputEventManager();

    public JoyStick(float x, float y, float size, float nubscale) {
        super(0,0,0,0);
        this.xPos = x;
        this.yPos = y;
        this.size = size;
        this.nubScale = nubscale;
        this.defaultPosition = new Vector.Vector2f(xPos - (size * nubScale) / 2, yPos - (size * nubScale) / 2);
    }

    @Override
    public void onEntityCreate(RenderState s) {

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
    public void onEntityUpdate(RenderState s, float deltatime) {
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

            if(Math.abs(xAxis) < this.deadZone) xAxis = 0;
            if(Math.abs(yAxis) < this.deadZone) yAxis = 0;
        }else{
            this.defaultPosition = new Vector.Vector2f(xPos - (size * nubScale) / 2, yPos - (size * nubScale) / 2);
            xAxis = 0;
            yAxis = 0;
        }
    }

    @Override
    public void onEntityRender(RenderState s, Graphics g) {
        g.setColor(Color.WHITE);
        g.drawEllipse((int) (xPos - (size / 2)), (int) (yPos - (size / 2)),(int) size,(int) size);
        g.fillEllipse((int)(this.defaultPosition.x), (int)(this.defaultPosition.y),Math.round(size * this.nubScale),
                Math.round(size * this.nubScale));
    }

    @Override
    public void onEntityDestroy(RenderState s) {
        input.dispose();
    }

    @Override
    public void onEntityCollide(Entity e) {

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
