package org.openjfx;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

public class MSFileHandler implements IFileHandler {

	public final static String DEFAULT_SAVE = "save_file";

	public Path getFolderPath() {
		return Path.of(System.getProperty("user.dir"), "saves");
	}

	public Path getFilePath(String fileName) {
		return getFolderPath().resolve(fileName + ".txt");
	}

	public void ensureFolderAndFileExists() {
		if (!getFolderPath().toFile().exists()) {
			try {
				Files.createDirectories(getFolderPath());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		if (!getFilePath(DEFAULT_SAVE).toFile().exists()) {
			try {
				Files.createFile(getFilePath(DEFAULT_SAVE));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void save(String fileName, Game game) throws FileNotFoundException {
		ensureFolderAndFileExists();
		try (PrintWriter writer = new PrintWriter(getFilePath(fileName).toFile())) {
			writer.println(game.getDifficulty());
			writer.print(game.getFirstPressedTile().getY() + "," + game.getFirstPressedTile().getX());
			for (int y = 0; y < game.getHeight(); y++) {
				writer.println();
				for (int x = 0; x < game.getWidth(); x++) {
					writer.print(game.getTile(y, x).toStringForFileHandling());
					writer.print(",");
				}
			}
			writer.println();
		}
	}

	@Override
	public Game load(String fileName) throws FileNotFoundException {
		ensureFolderAndFileExists();
		Scanner scanner = new Scanner(getFilePath(fileName).toFile());
		String line = scanner.nextLine();
		String line2 = scanner.nextLine();
		String[] firstTile = line2.split(",");
		int rowNumber = 0;
		Game game = new Game(Integer.valueOf(line));
		game.setFirstPressedTile(Integer.parseInt(firstTile[0]), Integer.parseInt(firstTile[1]));
		while (scanner.hasNextLine()) {
			String row = scanner.nextLine();
			String[] firstRow = row.split(",");
			for (int x = 0; x < game.getWidth(); x++) {
				String[] tile = firstRow[x].split(":");
				game.setTile2(rowNumber, x, Boolean.parseBoolean(tile[0]), Boolean.parseBoolean(tile[1]),
						Boolean.parseBoolean(tile[2]), Integer.parseInt(tile[3]));
			}
			rowNumber++;
		}
		game.updateListOfMines();
		game.updateSetOfTilesMadeVisible();
		return game;
	}

	public static void main(String[] args) throws FileNotFoundException {
		System.out.println(Path.of(System.getProperty("user.home"), "git", "tdt4100-prosjekt-aris", "project", "src",
				"main", "java", "saves"));
		Game game = new Game(2);
		game.setListOfMinesAndLayOut(game.getTestBoard2());
		game.setTiles();
		game.setValueOfTiles();
		game.setFirstPressedTile(4, 4);
		game.makeNeighborTilesVisible(4, 4);
		System.out.println(game.toString());
		IFileHandler handler = new MSFileHandler();
		handler.save("test", game);
		Game loadedGame = handler.load("test");
		System.out.println(loadedGame.toString());
	}

}
