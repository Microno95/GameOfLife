package none.test;

import processing.core.PApplet;
import processing.core.PVector;
import processing.core.PGraphics;

/**
 * Created by ekin4 on 06/08/2016.
 */

public class CellMesh {

    private PVector meshDims = new PVector(1, 1);
    private PGraphics screen;
    private PApplet display;
    private DisplayableCell first, curRow, curCol;
    private float zoomLevel = 1;
    private boolean newStateAvailable = true, periodic = false;

    public CellMesh(PApplet display_, boolean state, PVector position_,
                    PVector dims_, Color stroke_, PVector meshDims_) {
        first = new DisplayableCell(display_, state, null, position_, dims_, stroke_);
        meshDims = meshDims_;
        display = display_;
        screen = display.createGraphics((int) meshDims_.x, (int) meshDims_.y);
    }

    public void createMesh(int sep) {
        if (sep < 0) {
            sep = 2;
        }
        curCol = first;
        for (int i = 1; i < (int) meshDims.x; i++) {
            curCol.setRight(
                    new DisplayableCell(first.getScreen(), (false), first, new PVector(first.getDims().x * sep * i, 0),
                            new PVector(first.getDims().x, first.getDims().y), first.getStrokeColour()));
            curCol.getRight().setLeft(curCol);
            curRow = curCol;
            for (int k = 1; k < (int) meshDims.y; k++) {
                curRow.setBottom(new DisplayableCell(first.getScreen(), (false), first,
                        new PVector(curCol.getRelPosition().x, first.getDims().y * sep * k),
                        new PVector(first.getDims().x, first.getDims().y), first.getStrokeColour()));
                curRow.getBottom().setTop(curRow);
                curRow = curRow.getBottom();
                curRow.setLeft(curRow.getTop().getLeft().getBottom());
                curRow.getLeft().setRight(curRow);
                curRow.setUpperLeft(curRow.getTop().getLeft());
                curRow.getUpperLeft().setLowerRight(curRow);
            }
            curCol = curCol.getRight();
        }
        curRow = curCol;
        for (int k = 1; k < (int) meshDims.y; k++) {
            curRow.setBottom(new DisplayableCell(first.getScreen(), (false), first,
                    new PVector(curCol.getRelPosition().x, first.getDims().y * sep * k),
                    new PVector(first.getDims().x, first.getDims().y), first.getStrokeColour()));
            curRow.getBottom().setTop(curRow);
            curRow = curRow.getBottom();
            curRow.setLeft(curRow.getTop().getLeft().getBottom());
            curRow.getLeft().setRight(curRow);
            curRow.setUpperLeft(curRow.getTop().getLeft());
            curRow.getUpperLeft().setLowerRight(curRow);
        }
        curCol = first;
        int timesCol = 0, timesRow = 0;
        while (timesCol < 2) {
            curRow = curCol;
            timesRow = 0;
            while (timesRow < 2) {
                curRow = curRow.getBottom();
                curRow.setUpperRight(curRow.getTop().getRight());
                curRow.getUpperRight().setLowerLeft(curRow);
                timesRow += (curRow == curRow.getBottom() ? 1 : 0);
            }
            curCol = curCol.getRight();
            timesCol += (curCol == curCol.getRight() ? 1 : 0);
        }
    }

    public void displayMesh() {
        if (newStateAvailable) {
            screen.beginDraw();
            curRow = first;
            if (meshDims.x * meshDims.y < 10000) {
                for (int i = 0; i < screen.width; i++) {
                    curCol = curRow;
                    for (int k = 0; k < screen.height; k++) {
                        curCol.updateState();
                        curCol.display();
                        curCol = curCol.getRight();
                    }
                    curRow = curRow.getBottom();
                }
            } else {
                screen.loadPixels();
                for (int i = 0; i < screen.width; i++) {
                    curCol = curRow;
                    for (int k = 0; k < screen.height; k++) {
                        curCol.updateState();
                        int val = (curCol.getState() ? 255 : 0);
                        screen.pixels[i + k * (int) meshDims.x] = new Color(val, val, val).getRGB();
                        curCol = curCol.getRight();
                    }
                    curRow = curRow.getBottom();
                }
                screen.updatePixels();
            }
            screen.endDraw();
            newStateAvailable = false;
        }
        display.image(screen, first.getAbsPosition().x, first.getAbsPosition().y,
                meshDims.x * zoomLevel,meshDims.y * zoomLevel);
    }

    public void updateMesh() {
        curRow = first;
        curCol = curRow;
        for (int i = 0; i < screen.width; i++) {
            curCol = curRow;
            for (int k = 0; k < screen.height; k++) {
                switch (curCol.neighbourCount()) {
                    case 0:
                        curCol.setNewLifeState(false);
                        break;
                    case 1:
                        curCol.setNewLifeState(false);
                        break;
                    case 2:
                        curCol.setNewLifeState(curCol.getState());
                        break;
                    case 3:
                        curCol.setNewLifeState(true);
                        break;
                    default:
                        curCol.setNewLifeState(false);
                        break;
                }
                curCol = curCol.getRight();
            }
            curRow = curRow.getBottom();
        }
        newStateAvailable = true;
    }

    public void screenShot() {
        screen.save(display.day() + "d_" + display.month() + "m_" + display.year()
                    + "yr_" + display.hour() + "h_" + display.minute() + "min.bmp");
    }

    public DisplayableCell getCell(int i, int k) {
        /*
        * i is the Column and k is the row;
        * */
        DisplayableCell current = first;
        for (int m = 0; m < i && m < meshDims.x; m++) {
            current = current.getRight();
        }
        for (int n = 0; n < k && n < meshDims.y; n++) {
            current = current.getBottom();
        }
        return current;
    }

    public void togglePeriodicBoundaryConditions() {
        curCol = getCell(0, 0);
        curRow = getCell(0, (int) meshDims.y);
        for (int i = 0; i < meshDims.x; i++) {
            if (!periodic) {
                curCol.setTop(curRow);
                curRow.setBottom(curCol);
            } else {
                curCol.setTop(curCol);
                curRow.setBottom(curRow);
            }
            curCol = curCol.getRight();
            curRow = curRow.getRight();
        }
        curCol = getCell(0, 0);
        curRow = getCell((int) meshDims.x, 0);
        for (int i = 0; i < meshDims.y; i++) {
            if (!periodic) {
                curCol.setLeft(curRow);
                curRow.setRight(curCol);
            } else {
                curCol.setLeft(curCol);
                curRow.setRight(curRow);
            }
            curCol = curCol.getBottom();
            curRow = curRow.getBottom();
        }
        curCol = getCell(0, 0);
        curRow = getCell(0, (int) meshDims.y);
        for (int i = 0; i < meshDims.x; i++) {
            if (!periodic) {
                curCol.setUpperLeft(curRow.getLeft());
                curCol.setUpperRight(curRow.getRight());
                curRow.setLowerLeft(curCol.getLeft());
                curRow.setLowerRight(curCol.getRight());
            } else {
                curCol.setUpperLeft(curCol);
                curCol.setUpperRight(curCol);
                curRow.setLowerLeft(curRow);
                curRow.setLowerRight(curRow);
            }
            curCol = curCol.getRight();
            curRow = curRow.getRight();
        }
        curCol = getCell(0, 0);
        curRow = getCell((int) meshDims.x, 0);
        for (int i = 0; i < meshDims.y; i++) {
            if (!periodic) {
                curCol.setUpperLeft(curRow.getTop());
                curCol.setLowerLeft(curRow.getBottom());
                curRow.setUpperRight(curCol.getTop());
                curRow.setLowerRight(curCol.getBottom());
            } else {
                curCol.setUpperLeft(curCol);
                curCol.setLowerLeft(curCol);
                curRow.setUpperRight(curRow);
                curRow.setLowerRight(curRow);
            }
            curCol = curCol.getBottom();
            curRow = curRow.getBottom();
        }
        periodic = !periodic;
    }

    public void checkPeriodicBoundaryConditions() {
        curCol = getCell(0, 0);
        curRow = getCell(0, (int) meshDims.y);
        for (int i = 0; i < meshDims.x; i++) {
            display.println("Top Check", curCol.getTop() == curRow,
                    "Bottom Check", curCol == curRow.getBottom());
            curCol = curCol.getRight();
            curRow = curRow.getRight();
        }
        curCol = getCell(0, 0);
        curRow = getCell((int) meshDims.x, 0);
        for (int i = 0; i < meshDims.y; i++) {
            display.println("Left Check", curCol.getLeft() == curRow,
                    "Right Check", curCol == curRow.getRight());
            curCol = curCol.getBottom();
            curRow = curRow.getBottom();
        }
        curCol = getCell(0, 0);
        curRow = getCell(0, (int) meshDims.y);
        for (int i = 0; i < meshDims.x; i++) {
            display.println("UpperLeft Check", curCol.getUpperLeft() == curRow.getLeft(),
                    "UpperRight Check", curCol.getUpperRight() == curRow.getRight(),
                    "LowerLeft Check", curCol.getLeft() == curRow.getLowerLeft(),
                    "LowerRight Check", curCol.getRight() == curRow.getLowerRight());
            curCol = curCol.getRight();
            curRow = curRow.getRight();
        }
        curCol = getCell(0, 0);
        curRow = getCell((int) meshDims.x, 0);
        for (int i = 0; i < meshDims.y; i++) {
            display.println("UpperLeft Check", curCol.getUpperLeft() == curRow.getTop(),
                    "UpperRight Check", curCol.getLowerLeft() == curRow.getBottom(),
                    "LowerLeft Check", curCol.getTop() == curRow.getUpperRight(),
                    "LowerRight Check", curCol.getBottom() == curRow.getLowerRight());
            curCol = curCol.getBottom();
            curRow = curRow.getBottom();
        }
        periodic = !periodic;
    }


    public DisplayableCell getFirst() {
        return first;
    }

    public void setFirst(DisplayableCell first) {
        this.first = first;
    }

    public PVector getMeshDims() {
        return meshDims;
    }

    public void setMeshDims(PVector meshDims) {
        this.meshDims = meshDims;
    }

    public PGraphics getScreen() {
        return screen;
    }

    public void setScreen(PGraphics screen) {
        this.screen = screen;
    }

    public PApplet getDisplay() {
        return display;
    }

    public void setDisplay(PApplet display) {
        this.display = display;
    }

    public DisplayableCell getCurRow() {
        return curRow;
    }

    public void setCurRow(DisplayableCell curRow) {
        this.curRow = curRow;
    }

    public DisplayableCell getCurCol() {
        return curCol;
    }

    public void setCurCol(DisplayableCell curCol) {
        this.curCol = curCol;
    }

    public float getZoomLevel() {
        return zoomLevel;
    }

    public void setZoomLevel(float zoomLevel) {
        this.zoomLevel = zoomLevel;
    }
}
