package com.tejashah.test;

import java.util.Scanner;

import com.tejashah.internal.PADSolver;
import com.tejashah.internal.definitions.OrbMap;
import com.tejashah.internal.definitions.SolverParameters;

public class Main {
	static Scanner input = new Scanner(System.in);
	
	public static void pauseUntilEnter() {
		System.out.print("Press enter to continue...");
		input.nextLine();
	}
	
	public static void main(String[] args) {
		OrbMap map;
		String mapConfig =  "R W T F R D \n" + 
							"R W T F W T \n" + 
							"L W W R T L \n" + 
							"L W W D R W \n" + 
							"T W W D W T ";
		
		String mapConfig2 = "R W T F R D \n" + 
							"L W W W W T \n" + 
							"L W W R T W \n" + 
							"L W F D R W \n" + 
							"T T W W W W ";
		
		String mapConfig3 = "R W T F R D \n" + 
							"L W D D D T \n" + 
							"L W W F R W \n" + 
							"L W F F R W \n" + 
							"T T W W W W ";
		
		String mapConfigAlgorBreaker =  "F T L D R R \r\n" + 
										"T L F D W W \r\n" + 
										"T T T T L F \r\n" + 
										"T T W W T D \r\n" + 
										"D W D F F R ";
		/*
		 * -XX:+AggressiveOpts -XX:+UseFastAccessorMethods -XX:+OptimizeStringConcat
		 */
		map = new OrbMap(mapConfig3);
		
		map.printMap();
		//boolean testingSingleThread = false;
		
		int maxMoves = 100;
		
		final long initTime = System.nanoTime();
		
		final SolverParameters params = SolverParameters.createFromDefault();
		
		PADSolver solver = new PADSolver(map, maxMoves, params);
		solver.solveMap((obj) -> {
			System.out.println("combos = " + obj.combos.size());
			long finalTime = System.nanoTime() - initTime;
			System.out.println(finalTime / 1000000D);
		});
		
		/*if (testingSingleThread) {
			MapSolver solver = new MapSolver(map, Location.compress(3, 3), new Callback<OrbMap>() {
				@Override
				public void onFinish(OrbMap obj) {
					obj.printMap();
					long finalTime = System.nanoTime() - initTime;
					System.out.println(finalTime / 1000000D);
				}
			}, params);
			
			solver.startComboCalculations(maxMoves);
		} else {
			PADSolver solver = new PADSolver(map, maxMoves, params);
			solver.solveMap((obj) -> {
				System.out.println("combos = " + obj.combos.size());
				long finalTime = System.nanoTime() - initTime;
				System.out.println(finalTime / 1000000D);
			});
		}*/
		
		//pauseUntilEnter();
	}
}