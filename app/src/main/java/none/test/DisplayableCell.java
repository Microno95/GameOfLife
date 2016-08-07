package none.test;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PVector;

public class DisplayableCell extends Cell {

	private DisplayableCell left = null, right = null, top = null, bottom = null, upperLeft = null, lowerLeft = null,
			upperRight = null, lowerRight = null, allFather = null;
	private PVector position, dims;
	private Color strokeColour;

	public DisplayableCell(boolean state, DisplayableCell allFather_,
                           PVector position_, PVector dims_, Color stroke_) {
		super(state);
		position = position_;
		dims = dims_;
		strokeColour = stroke_;
		left = this;
		right = this;
		top = this;
		bottom = this;
		upperLeft = this;
		lowerLeft = this;
		upperRight = this;
		lowerRight = this;
		if (allFather_ != null) {
			allFather = allFather_;
		} else {
			allFather = this;
		}
	}

	public DisplayableCell display(PApplet screen) {
		if (getState()) {
			screen.fill(255);
		} else {
			screen.fill(0);
		}
		screen.stroke(strokeColour.getRGB());
		screen.strokeWeight(1);
		screen.rectMode(PApplet.CENTER);
		if (allFather == this) {
			screen.rect(position.x, position.y, dims.x, dims.y);
		} else {
			screen.rect(allFather.getAbsPosition().x + position.x, allFather.getAbsPosition().y + position.y, dims.x, dims.y);
		}
		return this;
	}

	public DisplayableCell display(PGraphics screen) {
        if (getState()) {
            screen.fill(255);
        } else {
            screen.fill(0);
        }
        screen.stroke(strokeColour.getRGB());
        screen.strokeWeight(1);
        screen.rectMode(PApplet.CENTER);
        if (allFather == this) {
            screen.rect(position.x, position.y, dims.x, dims.y);
        } else {
            screen.rect(allFather.getAbsPosition().x + position.x, allFather.getAbsPosition().y + position.y, dims.x, dims.y);
        }
        return this;
	}
	
	//nay nay
	public int neighbourCount() {
		int neigh = 0;
		
		if (left != this) {
			if (this.getLeft().getState() == true) {
				neigh++;
			}
		}
		
		if (right != this) {
			if (this.getRight().getState() == true) {
				neigh++;
			}
		}
		
		if (top != this) {
			if (this.getTop().getState() == true) {
				neigh++;
			}
		}
		
		if (bottom != this) {
			if (this.getBottom().getState() == true) {
				neigh++;
			}
		}
		
		if (upperLeft != this && upperLeft != top) {
			if (this.getUpperLeft().getState() == true) {
				neigh++;
			}
		}
		
		if (upperRight != this && upperRight != top) {
			if (this.getUpperRight().getState() == true) {
				neigh++;
			}
		}
		
		if (lowerLeft != this && lowerLeft != bottom) {
			if (this.getLowerLeft().getState() == true) {
				neigh++;
			}
		}
		
		if (lowerRight != this && lowerRight != bottom) {
			if (this.getLowerRight().getState() == true) {
				neigh++;
			}
		}
		
		return neigh;
	}
	
	public DisplayableCell getDir(int dir) {
		switch (dir) {
			case 0:
				return this;
			case 1:
				return getLeft();
			case 2:
				return getUpperLeft();
			case 3:
				return getTop();
			case 4:
				return getUpperRight();
			case 5:
				return getRight();
			case 6:
				return getLowerRight();
			case 7:
				return getBottom();
			case 8:
				return getLowerLeft();
			default:
				return this;
		}
	}

	// --------- Accessors / Getters -------- //

	public PVector getDims() {
		return dims;
	}
	
	public DisplayableCell getLeft() {
		return left;
	}

	public DisplayableCell getRight() {
		return right;
	}

	public DisplayableCell getTop() {
		return top;
	}

	public DisplayableCell getBottom() {
		return bottom;
	}

	public DisplayableCell getUpperLeft() {
		return upperLeft;
	}

	public DisplayableCell getUpperRight() {
		return upperRight;
	}

	public DisplayableCell getLowerLeft() {
		return lowerLeft;
	}

	public DisplayableCell getLowerRight() {
		return lowerRight;
	}

	public PVector getAbsPosition() {
		if (allFather != this) {
			return PVector.add(allFather.getAbsPosition(), position);
		} else {
			return position;
		}
	}
	
	public PVector getRelPosition() {
		if (allFather != this) {
			return position;
		} else {
			return new PVector(0, 0);
		}
	}

	public Color getStrokeColour() {
		return strokeColour;
	}

	// --------------------------------------- //

	// ---------- Mutators / Setters --------- //

	public void setDims(PVector dims) {
		this.dims = dims;
	}

	public DisplayableCell setState(boolean state) {
		super.setState(state);
		return this;
	}

	public DisplayableCell setLeft(DisplayableCell left_) {
		left = left_;
		return this;
	}

	public DisplayableCell setRight(DisplayableCell right_) {
		right = right_;
		return this;
	}

	public DisplayableCell setTop(DisplayableCell top_) {
		top = top_;
		return this;
	}

	public DisplayableCell setBottom(DisplayableCell bottom_) {
		bottom = bottom_;
		return this;
	}

	public DisplayableCell setUpperLeft(DisplayableCell ul) {
		upperLeft = ul;
		return this;
	}

	public DisplayableCell setLowerLeft(DisplayableCell ll) {
		lowerLeft = ll;
		return this;
	}

	public DisplayableCell setUpperRight(DisplayableCell ur) {
		upperRight = ur;
		return this;
	}

	public DisplayableCell setLowerRight(DisplayableCell lr) {
		lowerRight = lr;
		return this;
	}

	public void setAbsPosition(PVector position_) {
		if (allFather != this) {
			position = PVector.sub(position_, allFather.getAbsPosition());
		} else {
			position = position_;
		}
	}
	
	public void setRelPosition(PVector position_) {
		if (allFather != this) {
			position = position_;
		} else {
			position = PVector.add(position_, position);
		}
	}

	public void setStrokeColour(Color strokeColour) {
		this.strokeColour = strokeColour;
	}

	// --------------------------------------- //

}
