package com.tejashah.internal.definitions;

/**
 * Represents a 2D point on a non-fractional board
 * @author Tejas Shah
 * @version 7/21/15
 */
public class Location {
	private static final int LIMIT = 1000;
	
	/**
	 * WARNING: x or y cannot be greater than LIMIT-1 (999)
	 */
	public static int compress(int x, int y) {
		return x * LIMIT + y;
	}
	
	public static int getX(int loc) {
		return loc / LIMIT;
	}
	
	public static int getY(int loc) {
		return loc % LIMIT;
	}
	
	public static int getFrom(int loc, int dir) {
		int x = loc / LIMIT;
		int y = loc % LIMIT;
		switch (dir) {
			case Direction.UP:
				return compress(x - 1, y);
			case Direction.RIGHT:
				return compress(x, y + 1);
			case Direction.DOWN:
				return compress(x + 1, y);
			case Direction.LEFT:
				return compress(x, y - 1);
			default:
				return 0;
		}
	}
	
	public static int[] getAdjacentLocations(int loc) {
		int x = loc / LIMIT;
		int y = loc % LIMIT;
		return new int[] {compress(x - 1, y),compress(x, y + 1),compress(x + 1, y),compress(x, y - 1)};
	}
}