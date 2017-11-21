package com.tejashah.internal;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.magicwerk.brownies.collections.BigList;

import com.tejashah.internal.definitions.Location;
import com.tejashah.internal.definitions.OrbMap;
import com.tejashah.internal.definitions.SolverParameters;

/**
 * A brute-force based path finder that will try to find to recursively find the best moves to execute
 * @author Tejas Shah
 * @version 7/21/15
 */
public class PADSolver {
	private ExecutorService executor;
	
	private Callback<OrbMap> callback;
	private BigList<OrbMap> mapList;
	private BigList<Runnable> runnables;
	
	private int maxThreads;
	
	public PADSolver(OrbMap map, final int maxMoves, SolverParameters params) {
		maxThreads = map.getRowCount() * map.getColumnCount();
		
		runnables = new BigList<>();
		runnables.ensureCapacity(maxThreads);
		
		mapList = new BigList<>();
		mapList.ensureCapacity(maxThreads);
		
		executor = Executors.newFixedThreadPool(maxThreads);
		
		//prep threads
		for (int r = 0; r < map.getRowCount(); r++) {
			for (int c = 0; c < map.getColumnCount(); c++) {
				final MapSolver tmpSolver = new MapSolver(map,
						Location.compress(r, c),
						(obj) -> {
							if (obj != null)
								mapList.add(obj);
						}, params);
				
				runnables.add(() -> {
					tmpSolver.startComboCalculations(maxMoves);
				});
			}
		}
	}
	
	public void solveMap(Callback<OrbMap> callback) {
		setCallback(callback);
		activateThreads();
	}
	
	public void setCallback(Callback<OrbMap> callback) {
		this.callback = callback;
	}
	
	public void activateThreads() {
		for (Runnable runnable : runnables) {
			executor.execute(runnable);
		}
		executor.shutdown();
		while (!executor.isTerminated());
		
		OrbMap target = mapList.get(0);
		int combos = -1;
		for (OrbMap map : mapList) {
			if (map.combos.size() > combos) {
				target = map;
			}
		}
		
		callback.onFinish(target);
	}
}