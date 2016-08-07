package none.test;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PVector;
import android.view.MotionEvent;

/*
 * COLOUR CODES
 * 
 * new Color(48, 120, 245, 128) : Faded Navy Blue
 * 
 * new Color(128, 128, 128, 80) : Light Grey
 * 
 * 
 * MESH LIMITATIONS 
 * 
 * No more than 100,000 cells, otherwise too sluggish (FPS drops to 2).
 * 
 */

public class GoL extends PApplet {

	public void settings() {
		size(displayHeight, displayWidth, JAVA2D);
	}

	static int genStepFPS = 1;
	static float inc = (float) 0.025;
	static boolean runGen = true;
	MultiTouch[] mt = new MultiTouch[]{new MultiTouch(), new MultiTouch(), new MultiTouch()};
	int maxTouchEvents = 3;
	CellMesh GoL_Mesh;

	public void setup() {
		hint(DISABLE_TEXTURE_MIPMAPS);
		frameRate(60);
		background(0);
		GoL_Mesh = new CellMesh(this, false, new PVector(40, 40), new PVector(10, 10),
				new Color(128, 128, 128, 80), new PVector(20, 20), new PVector(40, 40));
		GoL_Mesh.createMesh(1);
		GoL_Mesh.togglePeriodicBoundaryConditions(this);
		GoL_Mesh.checkPeriodicBoundaryConditions(this);

//		DisplayableCell baseCell = GoL_Mesh.getFirst().getLowerRight().getLowerRight()
//								.getLowerRight().getLowerRight();
//
//		// BEACON
//		baseCell.getLowerRight().setState(true);
//		baseCell.getLowerRight().getRight().setState(true);
//		baseCell.getLowerRight().getBottom().setState(true);
//		baseCell.getLowerRight().getLowerRight().getLowerRight().getLowerRight().setState(true);
//		baseCell.getLowerRight().getLowerRight().getLowerRight().getLowerRight().getTop().setState(true);
//		baseCell.getLowerRight().getLowerRight().getLowerRight().getLowerRight().getLeft().setState(true);
//
//		// BLINKER
//		GoL_Mesh.getFirst().getBottom().setState(true);
//		GoL_Mesh.getFirst().getBottom().getRight().setState(true);
//		GoL_Mesh.getFirst().getBottom().getRight().getRight().setState(true);

        //GLIDER
        GoL_Mesh.getCell(2, 1).setState(true);
        GoL_Mesh.getCell(3, 2).setState(true);
        GoL_Mesh.getCell(3, 3).setState(true);
        GoL_Mesh.getCell(2, 3).setState(true);
        GoL_Mesh.getCell(1, 3).setState(true);

	}

	public void draw() {
        background(0);
		if (frameCount % genStepFPS == 0 && runGen) {
			GoL_Mesh.updateMesh();
			GoL_Mesh.displayMesh(this);
		}
		if (mousePressed) {
			for (int i=0; i < maxTouchEvents; i++) {
				if (mt[2].touched == false && mt[1].touched == true) {
					GoL_Mesh.setZoomLevel(GoL_Mesh.getZoomLevel() +
                            inc * dist(mt[0].motionX, mt[0].motionY, mt[1].motionX, mt[1].motionY));
					GoL_Mesh.setMeshLoc(new PVector(mt[0].motionX / 2 + mt[1].motionX / 2, mt[0].motionY / 2 + mt[1].motionY / 2));
				} else if (mt[2].touched == true && mt[1].touched == true) {
					GoL_Mesh.setZoomLevel(GoL_Mesh.getZoomLevel() -
                            inc * dist(mt[0].motionX, mt[0].motionY, mt[1].motionX, mt[1].motionY));
					GoL_Mesh.setMeshLoc(new PVector(mt[0].motionX / 2 + mt[1].motionX / 2, mt[0].motionY / 2 + mt[1].motionY / 2));
				} else {
					GoL_Mesh.setMeshLoc(new PVector(mouseX, mouseY));
				}
			}
		}
		textSize(28 * (float) (1920 * 1080) / (displayHeight * displayWidth));
		fill(255);
		text("FPS: " + frameRate, 0, 28 * (float) 1920 / displayHeight);
		text("GenStep: " + genStepFPS, (width - 200) * (float) 1080 / displayWidth,
                28 * (float) 1920 / displayHeight);
	}

	public void keyPressed() {
		if (key == ' ') {
			runGen = !runGen;
		}
		if (key == 's' || key == 'S') {
			GoL_Mesh.screenShot(this);
		}
		if (keyCode == UP) {
			genStepFPS++;
		} else if (genStepFPS > 1 && keyCode == DOWN) {
			genStepFPS--;
		}
	}

	public boolean surfaceTouchEvent(MotionEvent me) {
		int pointers = me.getPointerCount();
		for (int i=0; i < maxTouchEvents; i++) {
			mt[i].touched = false;
		}
		for (int i=0; i < maxTouchEvents; i++) {
			if (i < pointers) {
				mt[i].update(me, i);
			} else {
				mt[i].update();
			}
		}
		return super.surfaceTouchEvent(me);
	}
}
