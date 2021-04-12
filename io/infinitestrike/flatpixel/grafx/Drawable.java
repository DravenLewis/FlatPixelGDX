package io.infinitestrike.flatpixel.grafx;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public interface Drawable {
    public void draw(SpriteBatch b);
    public void drawFilled(ShapeRenderer r);
    public void drawOutline(ShapeRenderer r);
}
