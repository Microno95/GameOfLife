package none.test;

public class Cell {
	private boolean lifeState = false, newLifeState = false, newStateAvailable = false;
	
	public Cell(boolean state) {
		lifeState = state;
		newLifeState = state;
	}
	
	public Cell updateState() {
		if (newStateAvailable) {
			lifeState = newLifeState;
			newStateAvailable = false;
		}
		return this;
	}
	
	// ---------- Mutators / Setters ---------- //
	
	public Cell setState(boolean state) {
		lifeState = state;
		return this;
	}
	
	public Cell setNewLifeState(boolean newLifeState_) {
		newLifeState = newLifeState_;
		newStateAvailable = true;
		return this;
	}
	
	// ----------------------------------------- //
	
	// ---------- Accessors / Getters ---------- //
		
	public boolean getState() {
		return lifeState;
	}
	
	public boolean getNewLifeState() {
		return newLifeState;
	}
	
	// ----------------------------------------- //
}
