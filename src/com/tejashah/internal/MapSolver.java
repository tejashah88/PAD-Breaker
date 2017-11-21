package com.tejashah.internal;

import com.tejashah.internal.definitions.Direction;
import com.tejashah.internal.definitions.Location;
import com.tejashah.internal.definitions.OrbMap;
import com.tejashah.internal.definitions.SolverParameters;
import com.tejashah.internal.processing.OrbMapProcessor;

/**
 * @author Tejas Shah
 * @version Aug 8, 2015
 */
public class MapSolver {
	private int startLoc;
	private int maxMoves;
	private OrbMap map;
	private OrbMap targetMap;
	private Callback<OrbMap> callback;
	private OrbMapProcessor processor;
	
	public MapSolver(OrbMap map, int loc, Callback<OrbMap> cback, SolverParameters params) {
		this.map = map;
		startLoc = loc;
		callback = cback;
		processor = new OrbMapProcessor(params);
	}

	public void startComboCalculations(int maxMoves) {
		this.maxMoves = maxMoves;
		
		targetMap = evaluateMaps(map, startLoc);
		callback.onFinish(targetMap);
	}
	
	public OrbMap evaluateMaps(OrbMap map, int loc) {
		return evaluateMaps(map, loc, Direction.UNKNOWN, 0);
	}
	
	public OrbMap evaluateMaps(OrbMap map, int loc, int exclude, int currentStackCount) {
		currentStackCount++;
		//System.out.println(processor.size());
		
		if (processor.checkMap(map.updateCombos()))
			return map;
		
		for (int dir : map.getPossibleDirections(loc)) {
			//check for invalid/excluded directions
			if (dir == exclude || dir == Direction.UNKNOWN || currentStackCount > maxMoves)
				continue;
			
			evaluateMaps(map.swapAndCreate(loc, dir),
					Location.getFrom(loc, dir),
					Direction.OPPOSITE_VALUES[dir-1],
					currentStackCount);
		}
		
		return null;
	}
}