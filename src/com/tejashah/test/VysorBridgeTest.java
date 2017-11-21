package com.tejashah.test;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import com.tejashah.internal.PADSolver;
import com.tejashah.internal.definitions.OrbMap;
import com.tejashah.internal.definitions.SolverParameters;

public class VysorBridgeTest {
	//public static ArrayList<Point> dimensions = new ArrayList<>();
	public static ArrayList<Integer> pointsX = new ArrayList<>();
	public static ArrayList<Integer> pointsY = new ArrayList<>();
	
	public static void initialize() {
		Logger logger = Logger
				.getLogger(GlobalScreen.class.getPackage().getName());
		logger.setLevel(Level.WARNING);
		
		// Don't forget to disable the parent handlers.
		logger.setUseParentHandlers(false);
		
		try {
			GlobalScreen.registerNativeHook();
		} catch (NativeHookException ex) {
			System.err.println(
					"Error while initializing native hook: " + ex.getMessage());
		}
		
		NativeKeyListener keyListener = new NativeKeyListener() {
			@Override
			public void nativeKeyReleased(NativeKeyEvent krEvent) {
				if (krEvent.getKeyCode() == 41) {
					Point p = MouseInfo.getPointerInfo().getLocation();
					pointsX.add(p.x);
					pointsY.add(p.y);
					System.out.println(pointsX);
					System.out.println(pointsY);
				}
			}

			@Override public void nativeKeyPressed(NativeKeyEvent arg0) {}
			@Override public void nativeKeyTyped(NativeKeyEvent arg0) {}
		};
		
		GlobalScreen.addNativeKeyListener(keyListener);
	}
	
	public static void destroySelf() {
		try {
			GlobalScreen.unregisterNativeHook();
		} catch (NativeHookException ex) {
			System.err.println(
					"Error while destroying native hook: " + ex.getMessage());
		}
	}
	
	public static String getOrbColors(int xmin, int ymin, int xmax, int ymax) {
		System.out.println("Click on the first orb!");
		try {
			Robot r = new Robot();
			BufferedImage imgBoard = r.createScreenCapture(new Rectangle(xmin, ymin, xmax-xmin, ymax-ymin));
			
			Color[][] colors = new Color[6][5];
			
			for (int x = xmin, X = 0; x <= xmax; x += (xmax-xmin)/5, X++) {
				for (int y = ymin, Y = 0; y <= ymax; y += (ymax-ymin)/4, Y++) {
					colors[X][Y] = new Color(imgBoard.getRGB(x-xmin, y-ymin));
				}
			}
			
			System.out.println(colors.length);
			
			
			try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            }
			
		} catch (AWTException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void main(String[] args) {
		initialize();
		Object object = new Object();
		while (true) {
			synchronized(object) {
				if (pointsX.size() >= 2) {
					break;
				}
			}
		}
		
		getOrbColors(pointsX.get(0), pointsY.get(0), pointsX.get(1), pointsY.get(1));
		
		OrbMap map = null;		
		
		final SolverParameters params = SolverParameters.createFromDefault();
		int maxMoves = 100;
		
		PADSolver solver = new PADSolver(map, maxMoves, params);
		solver.solveMap((obj) -> {
			System.out.println("combos = " + obj.combos.size());
			
			destroySelf();
			System.exit(0);
		});
	}
}
