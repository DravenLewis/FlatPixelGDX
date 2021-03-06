package io.infinitestrike.flatpixelgdx.math;

import java.util.Random;

public class Noise {
    public static double[][] getNoiseMap(int width, int height, double scale){
        SimplexNoise.restorePermuation();
        double[][] map = new double[width][height];
        for(int y = 0; y < height; y++){
            for(int x = 0; x < width; x++){
                map[y][x] = (SimplexNoise.noise(y * scale, x * scale) + 1) / 2 * 255;
            }
        }
        return map;
    }

    public static double[][] getNoiseMap(int width, int height, double scale, long seed){
        SimplexNoise.regeneratePermutation(seed);
        double[][] map = new double[width][height];
        for(int y = 0; y < height; y++){
            for(int x = 0; x < width; x++){
                map[y][x] = (SimplexNoise.noise(y * scale, x * scale) + 1) / 2 * 255;
            }
        }
        return map;
    }

    public static double[][] getWhiteNoiseMap(int width, int height, long seed){
      double[][] map = new double[width][height];
      Random rand = new Random();
      rand.setSeed(seed);
      for(int y = 0; y < height; y++){
          for(int x = 0; x < width; x++){
              map[x][y] = (double)(rand.nextInt(255)) / (double) 255;
          }
      }
      return map;
    };
}
