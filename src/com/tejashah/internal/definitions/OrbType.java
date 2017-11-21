package com.tejashah.internal.definitions;

import java.util.Random;

/**
 * Represents the types of orbs present in PAD
 * @author Tejas Shah
 * @version 7/21/15
 */
public class OrbType {
	public static final byte UNKNOWN 	= 0;
	public static final byte FIRE 		= 1;
	public static final byte WATER 		= 2;
	public static final byte WOOD 		= 3;
	public static final byte LIGHT 		= 4;
	public static final byte DARK 		= 5;
	public static final byte RECOVER 	= 6;
	public static final byte POISON 	= 7;
	public static final byte JAMMER		= 8;
	public static final byte EMPTY 		= 9;
	public static final byte[] ALL_LEGAL_TYPES 		= new byte[] {FIRE, WATER, WOOD, LIGHT, DARK, RECOVER, POISON, JAMMER};
	public static final byte[] ALL_HEALTHY_TYPES 	= new byte[] {FIRE, WATER, WOOD, LIGHT, DARK, RECOVER};
	public static final byte[] ALL_ATTACK_TYPES 	= new byte[] {FIRE, WATER, WOOD, LIGHT, DARK};
	
	public static byte randomLegalType() {
		return ALL_LEGAL_TYPES[new Random().nextInt(ALL_LEGAL_TYPES.length)];
	}
	
	public static byte[] getConfig(String leaderName) {
		switch(leaderName.toUpperCase()) {
			case "DURGA":
				return new byte [] {FIRE, WATER, WOOD, DARK, RECOVER};
			default:
				return new byte[0];
		}
	}
	
	public static byte[] getDurga() {
		return new byte [] {FIRE, WATER, WOOD, DARK, RECOVER};
	}
}