package com.tejashah.internal.definitions;

import org.magicwerk.brownies.collections.BigList;
import org.magicwerk.brownies.collections.primitive.ByteBigList;
import org.magicwerk.brownies.collections.primitive.IntBigList;

import com.tejashah.internal.utils.GeneralUtils;

/**
 * Represents the playing field in PAD
 * @author Tejas Shah
 *
 */
public class OrbMap {
	private byte[][] orbs;
	private int numRows, numCols;
	public IntBigList locations = new IntBigList();
	public BigList<Combo> combos = new BigList<>();
	public byte[] typesContained = {};
	
	public OrbMap(int rows, int cols) {
		orbs = new byte[numRows = rows][numCols = cols];
	}
	
	public OrbMap(OrbMap map) {
		this(map.numRows, map.numCols);
		
		locations = map.locations.copy();
		combos = map.combos.copy();
		
		for (int r = 0; r < map.numRows; r++) {
			for (int c = 0; c < map.numCols; c++) {
				orbs[r][c] = map.orbs[r][c];
			}
		}
	}
	
	public OrbMap(int rows, int cols, byte type) {
		this(rows, cols);
		for (int r = 0; r < numRows; r++) {
			for (int c = 0; c < numCols; c++) {
				orbs[r][c] = type;
			}
		}
	}
	
	public OrbMap(String str) {
		String[] rows = str.split("\n");
		int r = 0, c = 0;
		orbs = new byte[numRows = rows.length][numCols = rows[0].trim().split(" ").length];
		for (String row : rows) {
			String[] chars = row.trim().split(" ");
			for (String chAsStr : chars) {
				char ch = chAsStr.toUpperCase().toCharArray()[0];
				byte type = OrbType.UNKNOWN;
				switch (ch) {
					case 'D':
						type = OrbType.DARK;
						break;
					case 'F':
						type = OrbType.FIRE;
						break;
					case 'J':
						type = OrbType.JAMMER;
						break;
					case 'L':
						type = OrbType.LIGHT;
						break;
					case 'P':
						type = OrbType.POISON;
						break;
					case 'R':
						type = OrbType.RECOVER;
						break;
					case 'W':
						type = OrbType.WATER;
						break;
					case 'T':
						type = OrbType.WOOD;
						break;
					case ' ':
						type = OrbType.EMPTY;
						break;
					case 'X':
						type = OrbType.UNKNOWN;
						break;
				}
				setOrb(r, c, type);
				c++;
			}
			c = 0;
			r++;
		}
	}
	
	public byte getOrb(int r, int c) {
		return orbs[r][c];
	}
	
	public byte getOrb(int l) {
		return orbs[Location.getX(l)][Location.getY(l)];
	}
	
	public void setOrb(int r, int c, byte orb) {
		orbs[r][c] = orb;
	}
	
	public void setOrb(int l, byte orb) {
		orbs[Location.getX(l)][Location.getY(l)] = orb;
	}
	
	public int getRowCount() {
		return numRows;
	}
	
	public int getColumnCount() {
		return numCols;
	}
	
	private void setCol(int c, byte[] col) {
		for (int x = 0; x < numRows; x++) {
			orbs[x][c] = col[x];
		}
	}
	
	private byte[] getCol(int c) {
		byte[] col = new byte[numRows];
		for (int x = 0; x < numRows; x++) {
			col[x] = orbs[x][c];
		}
		
		return col;
	}
	
	public OrbMap swapAndCreate(int loc, int dir) {
		OrbMap newMap = new OrbMap(this);
		newMap.swapOrbsDir(loc, dir);
		return newMap;
	}
	
	public OrbMap updateCombos() {
		BigList<Combo> foundCombos;
		while (true) {
			foundCombos = findCombos();
			if (foundCombos.size() > 0) { //combos have been found
				clearCombos(foundCombos);
				skyfall();
			} else {
				break;
			}
		}
		
		return this;
	}
	
	public BigList<Combo> findCombos() {
		BigList<Combo> rawCombos = new BigList<>();
		//find the combos
		
		byte currentType, type;
		IntBigList list = new IntBigList();
		
		int r, c;
		
		for (r = 0; r < numRows; r++) {
			//scan each orb type
			currentType = OrbType.UNKNOWN; //keeps track of current type
			
			//go through each orb in the row
			for (c = 0; c < numCols; c++) {
				type = getOrb(r, c);
				if (currentType != type) { //detected type change
					//time to check if we have enough orbs to make a combo
					if (list.size() > 2) {
						//TODO: changed here
						rawCombos.add(new Combo(currentType, list));
						//rawCombos.add(new Combo(currentType, list.toArray()));
					}
					
					//reset type
					currentType = type;
					list.clear();
				}
				
				list.add(Location.compress(r, c));
			}
			
			//the list may still have a pending combo to be processed
			if (list.size() > 2) {
				//TODO: changed here
				rawCombos.add(new Combo(currentType, list));
				//rawCombos.add(new Combo(currentType, list.toArray()));
			}
			
			//clear list of old locs
			list.clear();
		}
		
		//scan for vertical
		for (c = 0; c < getColumnCount(); c++) {
			//scan each orb type
			currentType = OrbType.UNKNOWN; //keeps track of current type
			
			//go through each orb in the col
			for (r = 0; r < getRowCount(); r++) {
				type = getOrb(r,c);
				if (currentType != type) { //detected type change
					//time to check if we have enough orbs to make a combo
					if (list.size() > 2) {
						//TODO: changed here
						rawCombos.add(new Combo(currentType, list));
						//rawCombos.add(new Combo(currentType, list.toArray()));
					}
					
					//reset type
					currentType = type;
					list.clear();
				}
				
				list.add(Location.compress(r, c));
			}
			
			//the list may still have a pending combo to be processed
			if (list.size() > 2) {
				//TODO: changed here
				rawCombos.add(new Combo(currentType, list));
				//rawCombos.add(new Combo(currentType, list.toArray()));
			}
			
			//clear list of old locs
			list.clear();
		}
		
		//check if any similar combos are intersecting each other or are next to each other
		BigList<BigList<Combo>> sortedCombos = sortCombos(rawCombos, true);
		
		BigList<Combo> finalizedCombos = new BigList<>();
		
		Combo c1, c2;
		int x, y;
		for (BigList<Combo> comboList : sortedCombos) { //go thru each combo type
			//obtain indexies
			for (x = 0; x < comboList.size(); x++) {
				for (y = 0; y < comboList.size(); y++) {// if using "y < comboList.size()", we'd scan already scanned combos
					if (x == y) //refering to same exact combo
						continue;
					c1 = comboList.get(x);
					c2 = comboList.get(y);
					
					//Combo.withinProximity(c1, c2);
					if (Combo.withinProximity(c1, c2)) { //if combos are within proximity
						//calls++;
						comboList.set(x, Combo.mergeCombos(c1, c2));
						comboList.remove(y);
						x = y = 0; //reset indexes to check for the updated combos
					}
				}
			}
		}
		
		for (BigList<Combo> comboList : sortedCombos) {
			finalizedCombos.addAll(comboList);
		}
		
		finalizedCombos = GeneralUtils.removeNullValues(finalizedCombos);
		removeAllDuplicates(finalizedCombos);
		
		return finalizedCombos;
	}
	
	public void removeAllDuplicates(BigList<Combo> list) {
		BigList<Combo> copy = new BigList<>();
		
		for (int i = 0; i < list.size(); i++) {
			if (!copy.contains(list.get(i))) {
				copy.add(list.get(i));
			}
		}
		
		list = copy;
	}
	
	public void clearCombos(BigList<Combo> list) {
		for (Combo combo : list) {
			combos.add(combo);
			for (int loc : combo.locs.toArray()) {
				setOrb(loc, OrbType.EMPTY); //the EMPTY orbs will be cleared during skyfall
			}
		}
	}
	
	public void skyfall() {
		byte[] col, newCol;
		for (int c = 0; c < numCols; c++) {
			col = getCol(c);
			newCol = new byte[col.length];
			int index = col.length-1;
			for (int i = col.length-1; i >= 0; i--) {
				if (col[i] != OrbType.EMPTY) {
					newCol[index--] = col[i];
				}
			}
			
			setCol(c, newCol);
		}
	}
	
	public BigList<BigList<Combo>> sortCombos(BigList<Combo> combos, boolean recordTypesFound) {
		BigList<BigList<Combo>> sortedList = new BigList<>();
		BigList<Combo> list;
		ByteBigList typesFound = new ByteBigList();
		typesFound.addArray(typesContained);
		
		for (byte type : OrbType.ALL_LEGAL_TYPES) {
			list = new BigList<>();
			
			for (int i = 0; i < combos.size(); i++) {
				if (combos.get(i).type == type) {
					if (recordTypesFound) {
						typesFound.addIfAbsent(type);
					}
					list.add(combos.remove(i--));
				}
			}
			
			sortedList.add(list);
		}
		
		if (recordTypesFound) {
			typesContained = typesFound.toArray();
		}
		
		return sortedList;
	}
	
	public int[] getPossibleDirections(int loc) {
		int[] arr = new int[4];
		int index = 0;
		for (int adjLoc : Location.getAdjacentLocations(loc)) {
			if (locationExists(adjLoc)) {
				arr[index] = Direction.VALUES[index];
			}
			index++;
		}
		
		return arr;
	}
	
	private boolean locationExists(int loc) {
		int x = Location.getX(loc);
		int y = Location.getY(loc);
		return x >= 0 && x < numRows && y >= 0 && y < numCols;
	}
	
	private void swapOrbsDir(int oldLoc, int dir) {
		int newLoc = Location.getFrom(oldLoc, dir);
		if (locationExists(newLoc)) {
			//swap orb locations
			byte tmpOrb = getOrb(oldLoc);
			setOrb(oldLoc, getOrb(newLoc));
			setOrb(newLoc, tmpOrb);
			
			//record move
			locations.add(newLoc);
		}
	}
	
	public void generateMap() {
		for (int r = 0; r < numRows; r++) {
			for (int c = 0; c < numCols; c++) {
				byte type;
				do {
					type = OrbType.randomLegalType();
				} while (type == OrbType.POISON || type == OrbType.JAMMER);
				
				setOrb(r, c, type);
			}
		}
	}
	
	/**
	 * Used for debugging purposes only!!!
	 */
	public void printMap() {
		System.out.println("OrbMap Layout: ");
		for (int r = 0; r < numRows; r++) {
			for (int c = 0; c < numCols; c++) {
				char ch = '?';
				switch (getOrb(r, c)) {
					case OrbType.DARK:
						ch = 'D';
						break;
					case OrbType.FIRE:
						ch = 'F';
						break;
					case OrbType.JAMMER:
						ch = 'J';
						break;
					case OrbType.LIGHT:
						ch = 'L';
						break;
					case OrbType.POISON:
						ch = 'P';
						break;
					case OrbType.RECOVER:
						ch = 'R';
						break;
					case OrbType.WATER:
						ch = 'W';
						break;
					case OrbType.WOOD:
						ch = 'T'; //tree
						break;
					case OrbType.EMPTY:
						ch = 'E';
						break;
					case OrbType.UNKNOWN:
						ch = 'X';
						break;
				}
				System.out.print(ch + " ");
			}
			System.out.println();
		}
		System.out.println();
	}
	
	@Override
	public String toString() {
		String mapLayout = "";
		
		for (int r = 0; r < numRows; r++) {
			for (int c = 0; c < numCols; c++) {
				char ch = '?';
				switch (getOrb(r, c)) {
					case OrbType.DARK:
						ch = 'D';
						break;
					case OrbType.FIRE:
						ch = 'F';
						break;
					case OrbType.JAMMER:
						ch = 'J';
						break;
					case OrbType.LIGHT:
						ch = 'L';
						break;
					case OrbType.POISON:
						ch = 'P';
						break;
					case OrbType.RECOVER:
						ch = 'R';
						break;
					case OrbType.WATER:
						ch = 'W';
						break;
					case OrbType.WOOD:
						ch = 'T'; //tree
						break;
					case OrbType.EMPTY:
						ch = 'E';
						break;
					case OrbType.UNKNOWN:
						ch = 'X';
						break;
				}
				
				mapLayout += (ch + " ");
			}
			mapLayout += "\n";
		}

		return mapLayout.trim();
	}
}
