package com.tejashah.internal.processing;

import java.util.ArrayList;

import com.tejashah.internal.definitions.OrbMap;
import com.tejashah.internal.definitions.OrbType;
import com.tejashah.internal.definitions.SolverParameters;

public class OrbMapProcessor {
	//private static final int DEFAULT_SIZE = 10000;
	private int minCombos = 0, numTypesToSatisfy = 0;
	private byte[] typesRequired = OrbType.ALL_LEGAL_TYPES.clone();
	private boolean checkingMinCombos = false, checkTypesRequired = false; 
	
	public OrbMapProcessor(SolverParameters params) {
		//this();
		this.minCombos = params.minCombos;
		this.checkingMinCombos = this.minCombos > 0;
		
		this.typesRequired = params.typesRequired.clone();
		this.numTypesToSatisfy = params.numTypesToSatisfy;
		this.checkTypesRequired = this.numTypesToSatisfy > 0;
	}
	
	private OrbMapProcessor(OrbMapProcessor list) {
		//mapList = list.mapList.copy();
		this.minCombos = list.minCombos;
		this.typesRequired = list.typesRequired.clone();
		this.numTypesToSatisfy = list.numTypesToSatisfy;
	}
	
	public boolean checkMap(OrbMap map) {
		//try { Thread.sleep(1000); } catch (InterruptedException ex) { ex.printStackTrace(); }
		
		//System.out.println("initializing checks...");
		
		//check if min combos needed is satisfied
		if (checkingMinCombos) {
			if (map.combos.size() >= minCombos);
				//pass
			else
				return false;
		}
		
		//System.out.println("passed 1st check");

		//check number of types of orbs cleared in the map
		if (checkTypesRequired) {
			if (map.typesContained.length >= numTypesToSatisfy);
				//pass
			else
				return false;
		}
		
		//System.out.println("passed 2nd check");
		//System.out.println("types required size = " + typesRequired.length);
		//now to check actual orb types satisfied
		ArrayList<Byte> localTypesContained = new ArrayList<>();
		for (byte type : map.typesContained) {
			localTypesContained.add(type);
		}
		
		ArrayList<Byte> localTypesRequired = new ArrayList<>();
		for (byte type : typesRequired) {
			localTypesRequired.add(type);
		}
		
		//now for actual check
		
		//first, remove types not required
		for (int i = 0; i < localTypesContained.size(); i++) {
			byte typeContained = localTypesContained.get(i);
			if (!localTypesContained.contains(typeContained)) {
				localTypesContained.remove(i--);
			}
		}
		
		if (localTypesContained.size() < localTypesRequired.size()) {
			// failed
			System.out.println("alert!!");
			return false;
		}
		return true;
	}
	
	public synchronized OrbMapProcessor copy() {
		return new OrbMapProcessor(this);
	}
}