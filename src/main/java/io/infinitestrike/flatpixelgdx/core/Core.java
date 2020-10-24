package io.infinitestrike.flatpixelgdx.core;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import io.infinitestrike.flatpixelgdx.math.Vector;

public class Core {

    public enum Type {
        type_int, type_char, type_string, type_short, type_long, type_byte, type_object, type_float, type_null
    }

    public static <T> T choose(T... args) {
        T[] objects = args;
        // do length -1 if array overflow
        return objects[new Random().nextInt(objects.length)];
    }

    public static Object[] getArrayFromArrayList(ArrayList<Object> obj) {
        Object[] objects = new Object[obj.size()];
        for (int i = 0; i < obj.size(); i++) {
            objects[i] = obj.get(i);
        }
        return objects;
    }

    public static ArrayList<Object> getArrayListFromArray(Object[] obj) {
        ArrayList<Object> objects = new ArrayList<Object>();
        for (Object o : obj) {
            objects.add(o);
        }
        return objects;
    }

    public static Type typeof(int i) {
        return Type.type_int;
    }

    public static Type typeof(char i) {
        return Type.type_char;
    }

    public static Type typeof(String i) {
        return Type.type_string;
    }

    public static Type typeof(short i) {
        return Type.type_short;
    }

    public static Type typeof(long i) {
        return Type.type_long;
    }

    public static Type typeof(byte i) {
        return Type.type_byte;
    }

    public static Type typeof(Object i) {
        return Type.type_object;
    }

    public static Type typeof(float i) {
        return Type.type_float;
    }

    public static String[] sortStringEvalLTG(int resolution, String... dat) {
        for (int pass = 0; pass < resolution; pass++) {
            for (int i = 0; i < dat.length - 1; i++) {
                int index_i = Integer.parseInt(dat[i]);
                int index_nxt = Integer.parseInt(dat[i + 1]);

                // Greatest to least
                if (index_nxt > index_i) {
                    dat[i + 1] = index_i + "";
                    dat[i] = index_nxt + "";
                }
            }
        }
        return dat;
    }

    public static String[] sortStringEvalGTL(int resolution, String... dat) {
        for (int pass = 0; pass < resolution; pass++) {
            for (int i = 0; i < dat.length - 1; i++) {
                int index_i = Integer.parseInt(dat[i]);
                int index_nxt = Integer.parseInt(dat[i + 1]);

                // Greatest to least
                if (index_nxt > index_i) {
                    dat[i + 1] = index_i + "";
                    dat[i] = index_nxt + "";
                }
            }
        }
        return dat;
    }

    public static String[] sortStringLengthGTL(int resolution, String... dat) {
        for (int pass = 0; pass < resolution; pass++) {
            for (int i = 0; i < dat.length - 1; i++) {
                String index_i = dat[i];
                String index_nxt = dat[i + 1];

                // Greatest to least
                if (index_nxt.length() > index_i.length()) {
                    dat[i + 1] = index_i;
                    dat[i] = index_nxt;
                }
            }
        }
        return dat;
    }

    public static String[] sortStringLengthLTG(int resolution, String... dat) {
        for (int pass = 0; pass < resolution; pass++) {
            for (int i = 0; i < dat.length - 1; i++) {
                String index_i = dat[i];
                String index_nxt = dat[i + 1];

                // Greatest to least
                if (index_nxt.length() < index_i.length()) {
                    dat[i + 1] = index_i;
                    dat[i] = index_nxt;
                }
            }
        }
        return dat;
    }

    public static Integer[] sortIntGTL(int resolution, Integer... dat) {
        for (int pass = 0; pass < resolution; pass++) {
            for (int i = 0; i < dat.length - 1; i++) {
                int index_i = dat[i];
                int index_nxt = dat[i + 1];

                // Greatest to least
                if (index_nxt > index_i) {
                    dat[i + 1] = index_i;
                    dat[i] = index_nxt;
                }
            }
        }
        return dat;
    }

    public static Integer[] sortIntLTG(int resolution, Integer... dat) {
        for (int pass = 0; pass < resolution; pass++) {
            for (int i = 0; i < dat.length - 1; i++) {
                int index_i = dat[i];
                int index_nxt = dat[i + 1];

                // Greatest to least
                if (index_nxt < index_i) {
                    dat[i + 1] = index_i;
                    dat[i] = index_nxt;
                }
            }
        }
        return dat;
    }

    public static void printHashMapData(HashMap map) {
        Object[] keys = map.keySet().toArray();
        Object[] values = map.values().toArray();
        if (keys.length == values.length) {
            for (int i = 0; i < keys.length; i++) {
                LogBot.log(LogBot.Status.INFO,"[HashMapData] Key: %s : Value: %s",keys[i],values[i]);
            }
        } else {
            LogBot.log(LogBot.Status.WARNING,"[HashMapData] Incomplete HashMap, Values don't match!");
        }
    }

    public static final boolean complexInstanceOf(Object e, ArrayList<Class<?>> classObject) {
        for (Class<?> c : classObject) {
            if (c == e.getClass() || e.getClass().isAssignableFrom(c)) {
                return true;
            }
        }
        return false;
    }

    public static final boolean hasInterface(Object e, ArrayList<Class<?>> objs) {
        Class<?>[] interfaces = e.getClass().getInterfaces();
        for (Class<?> sources : objs) {
            for (Class<?> interf : interfaces) {
                if (sources.getClass() == interf.getClass()) {
                    return true;
                }
            }
        }
        return false;
    }

    public static final boolean hasInterface(Object e, Class<?>[] objs) {
        Class<?>[] interfaces = e.getClass().getInterfaces();
        for (Class<?> sources : objs) {
            for (Class<?> interf : interfaces) {
                if (sources.getClass() == interf.getClass()) {
                    return true;
                }
            }
        }
        return false;
    }

    public static <T> void ArrayListCopy(ArrayList<T> source, ArrayList<T> target){
        for(T t : source) {
            target.add(t);
        }
    }

    public static class MathFunctions {
        public static Vector.Vector2f getPolarAngles(float angle) {
            float x = (float) Math.cos((angle / 180) * 3.14159);
            float y = (float) Math.sin((angle / 180) * 3.14159);

            return new Vector.Vector2f(x, y);
        }

        public static float getDistance(Vector.Vector2f v, Vector.Vector2f v2) {

            float x = v2.x - v.x;
            float y = v2.y - v.y;

            return (float) Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
        }

        public static float getDistance(float x, float y, float xx, float yy) {
            float xxx = xx - x;
            float yyy = yy - y;

            return (float) Math.sqrt(Math.pow(xxx, 2) + Math.pow(yyy, 2));
        }

        public static double randomRange(double firstBound, double lastBound) {
            return ThreadLocalRandom.current().nextDouble(firstBound, lastBound + 1);
        }

        /**
         * Easy way to calculate a point bound to a grid that can move.
         *
         * @Deprecated - Can give incorrect pixel offsets if the grid is not the same size. Use @see getCell()
         *
         * @param x  - x pos of input point
         * @param y  - y pos of input point
         * @param gx - grid size x
         * @param gy - grid size y
         * @param bx - bound size x (the width of the grid from its max value to min
         *           value) eg. -8 to 8 bx = 16
         * @param by - bound size y (the height of the grid from its max value to min
         *           value) eg.-10 to 10 = 20
         * @param xo - the x offset of the grid (for a movable grid)
         * @param yo - the y offset of the grid (for a movable grid)
         * @return a vector containing the 2 points.
         */
        @Deprecated
        public static Vector.Vector2f getPointBoundToGrid(float x, float y, float gx, float gy, float bx, float by, float xo,
                                                          float yo) {
            float fx = xo;
            float fy = yo;

            float xnn = (float) (gx * Math.floor(((x - fx) / gx)));
            float ynn = (float) (gy * Math.floor(((y - fy) / gy)));

            float xn = (xnn + fx) % bx;
            float yn = (ynn + fy) % by;

            return new Vector.Vector2f(xn, yn);
        }

        /**
         * Easy way to find a grid space cell index on a grid in screen space
         *
         * @param fieldOffset The Offset of the Grid Area
         * @param screenSpacePoint The Screen Space Point we want to find the grid index of (e.g. a mouse pointer on a grid of cells)
         * @param gridPixelSize The Size, in pixels, of the grid in screen space.
         * @param cellSize The Size of the square cells in pixels.
         *
         * @return a Vector2 containing the grid space X,Y projection position of the supplied point.
         */
        public Vector.Vector2f getCell(Vector.Vector2f fieldOffset, Vector.Vector2f screenSpacePoint, Vector.Vector2i gridPixelSize, int cellSize) {

            // Bind the Position Relative to the Grid
            float boundOffsetX = screenSpacePoint.x - fieldOffset.x;
            float boundOffsetY = screenSpacePoint.y - fieldOffset.y;

            // Scale the Relative position to an offset in cells space
            // X Cell Location = floor(X Relative offset * (Horizontal Cell Count) / Number of pixels the grid is wide)
            // Y Cell Location = floot(Y Relative offset * (Vertical Cell Count) / Number of pixels the grid is high)

            float fieldQuotientX = (float) Math.floor(boundOffsetX * ((gridPixelSize.x/cellSize) / gridPixelSize.x));
            float fieldQuotientY = (float) Math.floor(boundOffsetY * ((gridPixelSize.y/cellSize) / gridPixelSize.y));

            // Clamp the output to only be valid to the grid, as the bound offset is still in screen space,
            // therefore you can techically have a cell at (-1,-1) or (x + 1, y + 1), where x and y are the
            // max values the grid has. Prevents IndexOutOfBoundsException.

            float gridBoundX = fclamp(fieldQuotientX, 0, (gridPixelSize.x / cellSize) - 1);
            float gridBoundY = fclamp(fieldQuotientY, 0, (gridPixelSize.y / cellSize) - 1);


            // Return the clamped orderd pair
            return new Vector.Vector2f(gridBoundX,gridBoundY);
        }

        public static float getAngleFromPoints(Vector.Vector2f source, Vector.Vector2f target) {
            float angle = (float) Math.toDegrees(Math.atan2(target.y - source.y, target.x - source.x));
            return angle;
        }

        public static float fclamp(float i, float min, float max) {
            return Math.max(min, Math.min(max, i));
        }

        public static int iclamp(int i, int min, int max) {
            return Math.max(min, Math.min(max, i));
        }

        public static int get2DXFromIndex(int id, int width) {
            return id % width;
        }

        public static int get2DYFromIndex(int id, int width) {
            return id / width;
        }
    }
}
