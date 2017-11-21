package com.tejashah.internal.definitions;

/**
 * @author Tejas Shah
 * @version Sep 13, 2015
 */
public class SolverParameters {
	public byte[] typesRequired = new byte[0];
	public int numTypesToSatisfy = 0;
	public int minCombos = 0;
	
	public static SolverParameters createFrom(byte[] typesRequired) {
		return createFrom(0, typesRequired, typesRequired.length);
	}
	
	public static SolverParameters createFrom(byte[] typesRequired, int numTypesToSatisfy) {
		return createFrom(0, typesRequired, numTypesToSatisfy);
	}
	
	public static SolverParameters createFrom(int numCombos) {
		return createFrom(numCombos, new byte[0], 0);
	}
	
	public static SolverParameters createFrom(int numCombos, byte[] typesRequired) {
		return createFrom(numCombos, typesRequired, typesRequired.length);
	}
	
	public static SolverParameters createFrom(int numCombos, byte[] typesRequired, int numTypesToSatisfy) {
		SolverParameters params = new SolverParameters();
		params.minCombos = numCombos;
		params.typesRequired = typesRequired;
		params.numTypesToSatisfy = numTypesToSatisfy;
		
		return params;
	}
	
	public static SolverParameters createFromDefault() {
		return new SolverParameters();
	}
}