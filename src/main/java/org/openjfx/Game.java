package org.openjfx;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class Game {
	private int height;
	private int width;
	private int mines;
	private Tile[][] board;
	private Tile firstPressedTile;
	private int difficulty;
	private List<Tile> listOfMines = new ArrayList<>();
	private Set<Tile> setOfTilesMadeVisible = new HashSet<>();
	private boolean gameOver = false;
	private boolean gameLost = false;
	private boolean gameWon = false;
	private final List<Tile> testBoard1 = new ArrayList<>(Arrays.asList(new Tile(1, 4, true), new Tile(0, 0, true),
			new Tile(1, 2, true), new Tile(3, 1, true), new Tile(7, 4, true), new Tile(6, 1, true),
			new Tile(3, 9, true), new Tile(3, 8, true), new Tile(9, 1, true), new Tile(2, 6, true)));
	private final List<Tile> testBoard2 = new ArrayList<>(
			Arrays.asList(new Tile(1, 0, true), new Tile(7, 8, true), new Tile(4, 1, true), new Tile(3, 1, true),
					new Tile(9, 2, true), new Tile(11, 9, true), new Tile(0, 0, true), new Tile(2, 6, true),
					new Tile(0, 10, true), new Tile(3, 9, true), new Tile(8, 0, true), new Tile(4, 7, true),
					new Tile(7, 1, true), new Tile(0, 3, true), new Tile(11, 2, true), new Tile(5, 8, true),
					new Tile(3, 4, true), new Tile(10, 6, true), new Tile(9, 10, true), new Tile(5, 6, true)));
	private final List<Tile> testBoard3 = new ArrayList<>(
			Arrays.asList(new Tile(8, 8, true), new Tile(13, 7, true), new Tile(1, 2, true), new Tile(0, 9, true),
					new Tile(7, 9, true), new Tile(9, 1, true), new Tile(13, 1, true), new Tile(11, 3, true),
					new Tile(5, 9, true), new Tile(10, 4, true), new Tile(0, 8, true), new Tile(3, 11, true),
					new Tile(8, 12, true), new Tile(2, 0, true), new Tile(10, 9, true), new Tile(0, 12, true),
					new Tile(7, 10, true), new Tile(7, 1, true), new Tile(12, 5, true), new Tile(9, 7, true),
					new Tile(11, 1, true), new Tile(11, 13, true), new Tile(9, 4, true), new Tile(5, 7, true),
					new Tile(6, 4, true), new Tile(13, 13, true), new Tile(9, 0, true), new Tile(13, 0, true),
					new Tile(8, 9, true), new Tile(12, 8, true), new Tile(1, 12, true), new Tile(7, 12, true),
					new Tile(11, 5, true), new Tile(12, 0, true), new Tile(10, 12, true)));
	private final List<Tile> testBoard4 = new ArrayList<>(
			Arrays.asList(new Tile(5, 7, true), new Tile(4, 6, true), new Tile(14, 0, true), new Tile(3, 11, true),
					new Tile(11, 5, true), new Tile(12, 10, true), new Tile(15, 15, true), new Tile(8, 0, true),
					new Tile(1, 3, true), new Tile(5, 4, true), new Tile(14, 13, true), new Tile(13, 11, true),
					new Tile(12, 12, true), new Tile(12, 0, true), new Tile(5, 8, true), new Tile(5, 5, true),
					new Tile(10, 15, true), new Tile(7, 14, true), new Tile(15, 14, true), new Tile(10, 11, true),
					new Tile(5, 12, true), new Tile(9, 11, true), new Tile(4, 2, true), new Tile(14, 5, true),
					new Tile(10, 1, true), new Tile(14, 7, true), new Tile(10, 6, true), new Tile(9, 8, true),
					new Tile(11, 3, true), new Tile(3, 7, true), new Tile(8, 1, true), new Tile(8, 15, true),
					new Tile(8, 11, true), new Tile(3, 4, true), new Tile(1, 9, true), new Tile(3, 0, true),
					new Tile(7, 12, true), new Tile(3, 15, true), new Tile(8, 4, true), new Tile(14, 14, true),
					new Tile(9, 14, true), new Tile(1, 10, true), new Tile(10, 5, true), new Tile(2, 1, true),
					new Tile(13, 6, true), new Tile(12, 15, true), new Tile(6, 9, true), new Tile(1, 6, true),
					new Tile(15, 9, true), new Tile(4, 4, true), new Tile(12, 14, true), new Tile(4, 9, true),
					new Tile(4, 10, true), new Tile(15, 1, true), new Tile(0, 13, true)));

	public Game(int difficulty) {
		if (difficulty < 1 || difficulty > 4)
			throw new IllegalArgumentException("Invalid difficulty. Difficulty must be between 1 and 4.");
		switch (difficulty) {
			case 1:
				height = 10;
				width = 10;
				mines = 10;
				break;
			case 2:
				height = 12;
				width = 12;
				mines = 20;
				break;
			case 3:
				height = 14;
				width = 14;
				mines = 35;
				break;
			case 4:
				height = 16;
				width = 16;
				mines = 55;
				break;
		}
		board = new Tile[height][width];
		this.difficulty = difficulty;
	}

	public void layOutMines() {
		while (listOfMines.size() < getMines()) {
			int num = ThreadLocalRandom.current().nextInt(0, getHeight());
			int num2 = ThreadLocalRandom.current().nextInt(0, getHeight());
			if (!(board[num][num2] instanceof Tile)) {
				board[num][num2] = new Tile(num, num2, true);
				listOfMines.add(getTile(num, num2));
			}
		}
	}

	public void setTiles() {
		for (int y = 0; y < getHeight(); y++) {
			for (int x = 0; x < getWidth(); x++) {
				if (!(board[y][x] instanceof Tile)) {
					board[y][x] = new Tile(y, x, false);
				}
			}
		}
	}

	public void setNeighborTilesToZero(int y, int x) {
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				if (isValidTile(y + i, x + j)) {
					board[y + i][x + j] = new Tile(y + i, x + j, false, 0);
				}
			}
		}
	}

	public int calculateValue(int y, int x) {
		int value = 0;
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				if (isValidTile(y + i, x + j)) {
					if (board[y + i][x + j].isBomb()) {
						value += 1;
					}
				}
			}
		}
		return value;
	}

	public void setValueOfTiles() {
		for (int y = 0; y < getHeight(); y++) {
			for (int x = 0; x < getWidth(); x++) {
				board[y][x].setValue(calculateValue(y, x));
			}
		}
	}

	public void makeNeighborTilesVisible(int y, int x) {
		if (getTile(y, x).getValue() == 0 && getTile(y, x).isVisible() == true)
			setOfTilesMadeVisible.add(getTile(y, x));
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				if (isValidTile(y + i, x + j)) {
					getTile(y + i, x + j).setVisibility(true);
					if (getTile(y + i, x + j).getValue() == 0
							&& !setOfTilesMadeVisible.contains(getTile(y + i, x + j))) {
						makeNeighborTilesVisible(y + i, x + j);
					}
					setOfTilesMadeVisible.add(getTile(y + i, x + j));
				}
			}
		}
	}

	public int sizeOfSetOfTilesMadeVisible() {
		return setOfTilesMadeVisible.size();
	}

	public void makeAllBombsVisible() {
		for (Tile bomb : getListOfMines()) {
			bomb.setVisibility(true);
		}
	}

	public List<Tile> getListOfMines() {
		List<Tile> copy = new ArrayList<>(listOfMines);
		return copy;
	}

	public boolean isValidTile(int y, int x) {
		if (y >= 0 && x >= 0 && y < getHeight() && x < getWidth()) {
			return true;
		}
		return false;
	}

	public void addTileToSetOfTilesMadeVisible(Tile tile) {
		if (!isValidTile(tile.getY(), tile.getX()))
			throw new IllegalArgumentException("An invalid tile was added to the 'setOfTilesMadeVisible'.");
		setOfTilesMadeVisible.add(tile);
	}

	public Tile getTile(int y, int x) {
		Tile copy = board[y][x];
		return copy;
	}

	public void setTile(int y, int x) {
		coordinateCheck(y, x);
		board[y][x] = new Tile(y, x, false);
	}

	public void setTile2(int y, int x, boolean bomb, boolean flagged, boolean visible, int value) {
		coordinateCheck(y, x);
		if (value < 0 || value > 9)
			throw new IllegalArgumentException("The value of the tile must be positive.");
		board[y][x] = new Tile(y, x, bomb, flagged, visible, value);
	}

	public int getMines() {
		return mines;
	}

	public Tile getFirstPressedTile() {
		Tile copy = firstPressedTile;
		return copy;
	}

	public void setFirstPressedTile(int y, int x) {
		coordinateCheck(y, x);
		firstPressedTile = new Tile(y, x, false, 0);
		board[y][x] = new Tile(y, x, false, 0);
		board[y][x].setVisibility(true);
	}

	public int getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(int difficulty) {
		if (difficulty < 1 || difficulty > 4)
			throw new IllegalArgumentException("Difficulty must be between 1 and 4.");
		this.difficulty = difficulty;
	}

	public int getHeight() {
		return height;
	}

	// Velger å ikke ha med set-metoder for height og width da Game-objektet kan
	// komme
	// i ugyldig tilstand. Hvis height og width ikke samsvarer med koordinatene til
	// brettet vil mange av metodene ikke fungere og spillet vil garantert krasje.

	// public void setHeight(int height) {
	// if (height < 0)
	// throw new IllegalArgumentException("Height must be positive.");
	// this.height = height;
	// }

	public int getWidth() {
		return width;
	}

	// public void setWidth(int width) {
	// if (width < 0)
	// throw new IllegalArgumentException("Width must be positive.");
	// this.width = width;
	// }

	public boolean isGameOver() {
		return (gameOver || (getHeight() * getWidth() == sizeOfSetOfTilesMadeVisible() + listOfMines.size()));
	}

	public void setGameOver(boolean gameOver) {
		if (!isGameOver()) {
			this.gameOver = gameOver;
		}
	}

	public boolean isGameLost() {
		return gameLost;
	}

	public void setGameLost(boolean gameLost) {
		if (!isGameLost()) {
			this.gameLost = gameLost;
		}
	}

	public boolean isGameWon() {
		return gameWon;
	}

	public void setGameWon(boolean gameWon) {
		if (!isGameWon()) {
			this.gameWon = gameWon;
		}
	}

	@Override
	public String toString() {
		String s = "";
		s += "Height: " + height + "\nMines: " + mines + "\n";
		for (int y = 0; y < getHeight(); y++) {
			for (int x = 0; x < getWidth(); x++) {
				if (getTile(y, x).isVisible()) {
					s += board[y][x].toString() + " ";
				} else {
					s += "+ ";
				}
			}
			s += "\n";
		}
		return s;
	}

	public void setListOfMinesAndLayOut(List<Tile> list) {
		this.listOfMines = list;
		for (Tile i : list) {
			board[i.getY()][i.getX()] = i;
		}
	}

	public Tile[][] getBoard() {
		Tile[][] copy = board;
		return copy;
	}

	public void updateListOfMines() {
		for (int y = 0; y < getHeight(); y++) {
			for (int x = 0; x < getWidth(); x++) {
				if (board[y][x].isBomb()) {
					listOfMines.add(getTile(y, x));
				}
			}
		}
	}
	// Lage til felles metode? updateListOfMines&SetOfTilesMadeVisible...

	public void updateSetOfTilesMadeVisible() {
		for (int y = 0; y < getHeight(); y++) {
			for (int x = 0; x < getWidth(); x++) {
				if (board[y][x].isVisible()) {
					setOfTilesMadeVisible.add(getTile(y, x));
				}
			}
		}
	}

	private void coordinateCheck(int y, int x) {
		if (!isValidTile(y, x))
			throw new IllegalArgumentException("The coordinates doesn't match with the coordinates of the board.");
	}

	public List<Tile> getTestBoard1() {
		List<Tile> copy = new ArrayList<>(testBoard1);
		return copy;
	}

	public List<Tile> getTestBoard2() {
		List<Tile> copy = new ArrayList<>(testBoard2);
		return copy;
	}

	public List<Tile> getTestBoard3() {
		List<Tile> copy = new ArrayList<>(testBoard3);
		return copy;
	}

	public List<Tile> getTestBoard4() {
		List<Tile> copy = new ArrayList<>(testBoard4);
		return copy;
	}

	public void makeAllTilesVisible() {
		for (int y = 0; y < getHeight(); y++) {
			for (int x = 0; x < getWidth(); x++) {
				board[y][x].setVisibility(true);
			}
		}
	}

	public static void main(String[] args) {
		// Game board = new Game(1);
		// System.out.println(board.getFirstPressedTile());
		// board.setFirstPressedTile(7, 6);
		// board.setNeighborTilesToZero(board.getFirstPressedTile().getY(),
		// board.getFirstPressedTile().getX());
		// board.layOutMines();
		// board.setTiles();
		// board.setValueOfTiles();
		// board.makeNeighborTilesVisible(board.getFirstPressedTile().getY(),
		// board.getFirstPressedTile().getX());
		// board.makeAllBombsVisible();
		// System.out.println(board.isGameOver());
		// System.out.println(board.toString());
		// System.out.println(board.listOfMinesToString());
		// System.out.println(board.sizeOfSetOfTilesMadeVisible());
		// System.out.println(board.isGameOver());
		Game game = new Game(1);
		game.setListOfMinesAndLayOut(game.getTestBoard1());
		game.setTiles();
		game.setValueOfTiles();
		game.makeAllTilesVisible();
		System.out.println(game.toString());
	}

}
