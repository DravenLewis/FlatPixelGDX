package io.infinitestrike.flatpixel.physics;

import java.util.ArrayList;

import io.infinitestrike.flatpixel.core.Core;
import io.infinitestrike.flatpixel.entity.Entity;
import io.infinitestrike.flatpixel.entity.EntityManager;
import io.infinitestrike.flatpixel.math.Vector;


public class RayCast {

	public static boolean FLAG_BREAK_ON_HIT = false;
	public static boolean FLAG_INCLUDE_MAX_LENGTH = false;
	public static ArrayList<Class<?>> excludeList = new ArrayList<Class<?>>();
	
	public static ArrayList<RayCastHit> CastRay(EntityManager mgr, Vector.Vector2f location, float direction, float length, float step){
		ArrayList<RayCastHit> hitList = new ArrayList<RayCastHit>();
		Vector.Vector2f vec = Core.MathFunctions.getPolarAngles(direction);
		boolean hit = false;
		
		for(float i = 0; i < length; i+= step) {
			
			// current location
			Vector.Vector2f locVec = new Vector.Vector2f(location.x + vec.x * i, location.y + vec.y * i);
			for(Entity e : mgr.getEntities()) {
				if(e.getBounds().contains(locVec.x, locVec.y) && !HitsContainsEntity(hitList, e)) {
					
					if(Core.complexInstanceOf(e, RayCast.excludeList)) {
						continue;
					}
					
					RayCastHit h = new RayCastHit();
					h.hitEntity = e;
					h.hitVector = locVec;
					h.length = i;
					hitList.add(h);
					hit = true;
					if(FLAG_BREAK_ON_HIT) break;
				}
			}
			if(FLAG_BREAK_ON_HIT && hit) {
				break;
			}
		}
		
		if(FLAG_INCLUDE_MAX_LENGTH) {
			RayCastHit lastLocation = new RayCastHit();
			lastLocation.hitVector = new Vector.Vector2f(location.x + vec.x * length, location.y + vec.y * length);
			lastLocation.length = length;
			hitList.add(lastLocation);
		}
		
		return hitList;
	}
	
	public static Vector.Vector2f getMaxRayPosition(Vector.Vector2f location, float direction, float length) {
		Vector.Vector2f vec = Core.MathFunctions.getPolarAngles(direction);
		return new Vector.Vector2f(location.x + vec.x * length, location.y + vec.y * length);
	}
	
	public static class RayCastHit{
		public Vector.Vector2f hitVector;
		public Entity hitEntity; // Entities Are Game Objects
		public float length;
	}
	
	public static void cleanup() {
		RayCast.FLAG_BREAK_ON_HIT = false;
		RayCast.FLAG_INCLUDE_MAX_LENGTH = false;
		RayCast.excludeList = new ArrayList<Class<?>>();
	}
	
	private static boolean HitsContainsEntity(ArrayList<RayCastHit> hits, Entity e) {
		for(RayCastHit h : hits) {
			if(h.hitEntity == e) return true;
		}
		return false;
	}
}
