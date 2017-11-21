package com.tejashah.internal.utils;

public class GCAnalyzer {
	public static void analyze(String data) {
		String[] lines = data.split("\r\n");
		double sec = 0;
		for (String line : lines) {
			String[] parts = line.trim().split(",");
			String strSec = parts[1].trim().replace("secs]", "").trim();
			sec += Double.parseDouble(strSec);
		}
		
		System.out.println("total time used for GC: " + sec*1000 + " millisecs");
		System.exit(0);
	}
}
