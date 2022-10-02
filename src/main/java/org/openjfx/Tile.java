package org.openjfx;

public class Tile {
	private int x;
	private int y;
	private boolean bomb = false;
	private boolean visibility = false;
	private int value;
	private boolean flagged = false;

	public Tile(int y, int x, boolean bomb) {
		if (y < 0 || x < 0)
			throw new IllegalArgumentException("y and x must be positive integers.");
		this.x = x;
		this.y = y;
		this.bomb = bomb;
	}

	public Tile(int y, int x, boolean bomb, int value) {
		if (y < 0 || x < 0 || value < 0)
			throw new IllegalArgumentException("y, x and value must be positive integers.");
		this.x = x;
		this.y = y;
		this.bomb = bomb;
		this.value = value;
	}

	public Tile(int y, int x, boolean bomb, boolean flagged, boolean visibility, int value) {
		if (y < 0 || x < 0 || value < 0)
			throw new IllegalArgumentException("y, x and value must be positive integers.");
		this.y = y;
		this.x = x;
		this.bomb = bomb;
		this.flagged = flagged;
		this.visibility = visibility;
		this.value = value;
	}

	public boolean isBomb() {
		return bomb;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public boolean isVisible() {
		return visibility;
	}

	public void setVisibility(boolean visibility) {
		this.visibility = visibility;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		if (value < 0)
			throw new IllegalArgumentException("Value must be a positive integer.");
		this.value = value;
	}

	public boolean isFlagged() {
		return flagged;
	}

	public void changeFlagStatus() {
		flagged = !flagged;
	}

	@Override
	public String toString() {
		if (isBomb()) {
			return "x";
		}
		return "" + getValue();
	}

	public String toStringForFileHandling() {
		boolean b = isBomb();
		boolean f = isFlagged();
		boolean v = isVisible();
		return b + ":" + f + ":" + v + ":" + getValue();
	}

	public static void main(String[] args) {
		Tile t = new Tile(10, 5, true);
		System.out.println(t.toString());
	}

}
