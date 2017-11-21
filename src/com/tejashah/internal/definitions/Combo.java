package com.tejashah.internal.definitions;

import org.magicwerk.brownies.collections.primitive.IntBigList;

public class Combo {
	public IntBigList locs;
	public byte type;
	
	public Combo(byte type, IntBigList list) {
		locs = list.copy();
		this.type = type;
	}
	
	public static boolean withinProximity(Combo c1, Combo c2) {
		if (c1.type != c2.type) //a location object can't have 2 different types
			return false;
		
		//check for shared locations
		IntBigList locs1 = c1.locs;
		IntBigList locs2 = c2.locs;
		IntBigList commonLocs = findSimilarObjects(locs1, locs2);
		
		if (commonLocs.size() > 0) //returns true if there is at least 1 object in the commonLocs list
			return true;
		
		//check for shared adjacent locations
		for (int loc1 : locs1.toArray()) { //take locs from combo 1
			//check if each adjacent location is part of combo 2
			for (int adjLoc1 : Location.getAdjacentLocations(loc1)) {
				if (locs2.contains(adjLoc1)) //the are next to each other
					return true;
			}
		}
		
		//we dont have to go through all the location objects in loc2
		//since that was already checked
		
		return false;
	}
	
	public static Combo mergeCombos(Combo c1, Combo c2) {
		IntBigList uniqueOrbs = new IntBigList();
		
		//since c1 has unique locations already, no need to check of duplicates for c1.locs
		
		uniqueOrbs.addAll(c1.locs);
		
		for (int loc : c2.locs.toArray()) {
			if (!uniqueOrbs.contains(loc))
				uniqueOrbs.add(loc);
		}
		
		//TODO: changed here
		return new Combo(c1.type, uniqueOrbs);
		//return new Combo(c1.type, uniqueOrbs.toArray());
		//return new Combo(c1.type, uniqueOrbs.getDistinct().toArray());
	}
	
	private static IntBigList findSimilarObjects(IntBigList list1, IntBigList list2) {
		IntBigList similarList = new IntBigList();
		
		for (int loc1 : list1.toArray()) {
			for (int loc2 : list2.toArray()) {
				if (loc1 == loc2) {
					similarList.add(loc1);
				}
			}
		}
		
		return similarList;
	}
	
	@Override
	public boolean equals(Object obj) {
		//System.out.println("called");
		if (obj instanceof Combo) {
			Combo other = (Combo) obj;
			if (type != other.type)
				return false;
			if (locs.size() != other.locs.size())
				return false;
			return !locs.retainAll(other.locs);
		}
		
		return false;
	}
	
	@Override
	public String toString() {
		//return locs.size() + " " + type;
		return locs.size() + "-orb " + type + " combo @ " + locs;// + " / orbs = " + orbs;
	}
}