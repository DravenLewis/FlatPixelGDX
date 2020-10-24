package io.infinitestrike.flatpixelgdx.math;

public abstract class Vector {

    protected int size = 0;

    public static class Vector2i extends Vector{
        public int x = 0;
        public int y = 0;

        public static final Vector2i IDENTITY = new Vector2i(1,1);
        public static final Vector2i ZERO = new Vector2i(0,0);

        public Vector2i(int x, int y){
            this.size = 2;
            this.x = x;
            this.y = y;
        }

        public  Vector2i(){};
    }

    public static class Vector2f extends Vector{
        public float x = 0;
        public float y = 0;

        public static final Vector2f IDENTITY = new Vector2f(1,1);
        public static final Vector2f ZERO = new Vector2f(0,0);

        public Vector2f(float x, float y){
            this.size = 2;
            this.x = x;
            this.y = y;
        }

        public  Vector2f(){};
    }

    public static class Vector3i extends Vector{
        public int x = 0;
        public int y = 0;
        public int z = 0;

        public static final Vector3i IDENTITY = new Vector3i(1,1,1);
        public static final Vector3i ZERO = new Vector3i(0,0,0);

        public Vector3i(int x, int y, int z){
            this.size = 3;
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public  Vector3i(){};
    }

    public static class Vector3f extends Vector{
        public float x = 0;
        public float y = 0;
        public float z = 0;

        public static final Vector3f IDENTITY = new Vector3f(1,1,1);
        public static final Vector3f ZERO = new Vector3f(0,0,0);

        public Vector3f(float x, float y, float z){
            this.size = 3;
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public  Vector3f(){};
    }

    public int getSize(){
        return this.size;
    }

    public static <T extends Vector> int sizeOf(T v){
        return v.size;
    }
}