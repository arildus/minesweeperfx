package org.openjfx;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.TilePane;
import javafx.stage.FileChooser;

public class Controller {

	private Game game;
	private Tile firstPressedTile;
	private List<Button> buttonList = new ArrayList<>();
	private List<Tile> buttonTileList = new ArrayList<>();
	private IFileHandler saveHandler = new MSFileHandler();
	private FileChooser fileChooser = new FileChooser();
	@FXML
	TilePane board;
	@FXML
	Button easyButton, mediumButton, hardButton, expertButton, resetGameButton, saveButton, loadButton, browseButton;
	@FXML
	Label infoField, tilesMadeVisible, gameStatusLabel;
	@FXML
	TextField fileLocationField;

	@FXML
	public void setControls() {
		easyButton.setOnMouseClicked(event -> {
			initialize2(1);
			game.setDifficulty(1);
		});
		mediumButton.setOnMouseClicked(event -> {
			initialize2(2);
			game.setDifficulty(2);
		});
		hardButton.setOnMouseClicked(event -> {
			initialize2(3);
			game.setDifficulty(3);
		});
		expertButton.setOnMouseClicked(event -> {
			initialize2(4);
			game.setDifficulty(4);
		});
		resetGameButton.setOnMouseClicked(event -> {
			initialize2(game.getDifficulty());
		});
		saveButton.setOnMouseClicked(event -> {
			handleSaveAction();
		});
		loadButton.setOnMouseClicked(event -> {
			handleLoadAction();
		});
		browseButton.setOnMouseClicked(event -> {
			handleBrowseAction();
		});
	}

	@FXML
	public void initialize() {
		game = new Game(1);
		createDummyBoard();
		drawBoard();
		infoField.setText("Press a tile to generate a new, randomized board!");

		setControls();
	}

	public void initialize2(int difficulty) {
		game = new Game(difficulty);
		createDummyBoard();
		drawBoard();

		infoField.setText("Press a tile to generate a new, randomized board!");
	}

	public void initialize3() {
		game = new Game(game.getDifficulty());
		game.setFirstPressedTile(getFirstPressedTile().getY(), getFirstPressedTile().getX());
		game.setNeighborTilesToZero(getFirstPressedTile().getY(), getFirstPressedTile().getX());
		game.layOutMines();
		game.setTiles();
		game.setValueOfTiles();
		game.makeNeighborTilesVisible(getFirstPressedTile().getY(), getFirstPressedTile().getX());

		createRealBoard();
		drawBoard();
		displayVisibleTiles();
		displayNumberOfTilesMadeVisible();

		infoField.setText("Find all the tiles without bombs!");
	}

	public String getFileName() {
		String fileName = fileLocationField.getText();
		if (fileName.isBlank() || fileName.isEmpty()) {
			return "save_file";
		}
		return fileName;
	}

	public void handleLoadAction() {
		if (!getFileName().isBlank()) {
			try {
				game = saveHandler.load(getFileName());
				buttonList.clear();
				board.getChildren().clear();
				createRealBoard();
				drawBoard();
				displayVisibleTiles();
				displayNumberOfTilesMadeVisible();
				infoField.setText("Find all the tiles without bombs!");
				setControls();

			} catch (FileNotFoundException fnfe) {
				infoField.setText("This savefile does not exist. Try again.");
			} catch (NumberFormatException nfe) {
				infoField.setText("This file does not have the right format. Try again.");
			} catch (NoSuchElementException nsee) {
				infoField.setText("This save file is incomplete. Try again.");
			}
		}
	}

	public void handleSaveAction() {
		if (!getFileName().isBlank() && !game.isGameWon() && !game.isGameLost() && game.getFirstPressedTile() != null)
			try {
				saveHandler.save(getFileName(), game);
				infoField.setText("Game was saved succesfully.");
			} catch (FileNotFoundException e) {
				infoField.setText("This savefile does not exist. Try again.");
			}
	}

	public void handleBrowseAction() {
		saveHandler.ensureFolderAndFileExists();
		fileChooser.setInitialDirectory(saveHandler.getFolderPath().toFile());
		File selectedFile = fileChooser.showOpenDialog(fileLocationField.getScene().getWindow());
		if (selectedFile != null) {
			String fileName = selectedFile.getName();
			int pos = selectedFile.getName().lastIndexOf(".");
			if (pos > -1) {
				fileName = selectedFile.getName().substring(0, pos);
			}
			fileLocationField.setText(fileName);
		}
	}

	private void displayVisibleTiles() {
		for (Button button : buttonList) {
			int buttonY = buttonList.indexOf(button) / game.getHeight();
			int buttonX = buttonList.indexOf(button) % game.getWidth();
			if (game.getTile(buttonY, buttonX).isVisible() && !game.getTile(buttonY, buttonX).isFlagged()) {
				button.setText("" + game.getTile(buttonY, buttonX).getValue());
			} else if (game.getTile(buttonY, buttonX).isFlagged()) {
				button.setText("!!");
				button.setStyle("-fx-background-color: #F75353; ");
			}
		}
	}

	public void displayNumberOfTilesMadeVisible() {
		tilesMadeVisible.setText(
				"" + game.sizeOfSetOfTilesMadeVisible() + "/" + (game.getHeight() * game.getWidth() - game.getMines()));
	}

	public void resetGraphics() {
		buttonList.clear();
		board.getChildren().clear();
		gameStatusLabel.setVisible(false);
		tilesMadeVisible.setText("0");
	}

	public void createDummyBoard() {
		resetGraphics();
		for (int y = 0; y < game.getHeight(); y++) {
			for (int x = 0; x < game.getHeight(); x++) {
				Button button = new Button();
				button.setMinSize((1.0 * board.getMaxHeight() / game.getHeight()) - 1,
						1.0 * board.getMaxHeight() / game.getHeight() - 1);
				buttonList.add(button);
				button.setOnMouseClicked(event -> {
					int buttonY = buttonList.indexOf(button) / game.getHeight();
					int buttonX = buttonList.indexOf(button) % game.getWidth();
					Tile firstButton = new Tile(buttonY, buttonX, false);
					setFirstPressedTile(firstButton.getY(), firstButton.getX());
					initialize3();
				});
			}
		}
	}

	public void createRealBoard() {
		resetGraphics();
		for (int y = 0; y < game.getHeight(); y++) {
			for (int x = 0; x < game.getHeight(); x++) {
				Button button = new Button();
				button.setMinSize((1.0 * board.getMaxHeight() / game.getHeight()) - 1,
						1.0 * board.getMaxHeight() / game.getHeight() - 1);
				buttonList.add(button);
				int buttonY = buttonList.indexOf(button) / game.getHeight();
				int buttonX = buttonList.indexOf(button) % game.getWidth();
				Tile buttonTile = game.getTile(buttonY, buttonX);
				buttonTileList.add(buttonTile);
				button.setOnMouseClicked(event -> {
					if (event.getButton() == MouseButton.PRIMARY) {
						if (!game.isGameWon() && !game.isGameLost()) {
							if (!buttonTile.isVisible() && !buttonTile.isFlagged()) {
								if (!buttonTile.isBomb())
									game.addTileToSetOfTilesMadeVisible(buttonTile);
								if (!game.isGameOver()) {
									if (buttonTile.getValue() == 0 && !buttonTile.isBomb()) {
										game.makeNeighborTilesVisible(buttonTile.getY(), buttonTile.getX());
										displayVisibleTiles();
										if (game.isGameOver()) {
											infoField.setText("Game won!");
											gameStatusLabel.setText("Game won!");
											gameStatusLabel.setStyle("-fx-background-color: #61DD69; ");
											gameStatusLabel.setVisible(true);
											game.setGameWon(true);
										}
									} else if (!buttonTile.isBomb()) {
										buttonTile.setVisibility(true);
										button.setText("" + buttonTile.getValue());
									} else if (buttonTile.isBomb()) {
										game.setGameOver(true);
										button.setText("X");
										showAllBombs();
										infoField.setText("Game over!");
										gameStatusLabel.setText("Game over!");
										gameStatusLabel.setStyle("-fx-background-color: #F75353; ");
										gameStatusLabel.setVisible(true);
										game.setGameLost(true);
									}
								} else {
									buttonTile.setVisibility(true);
									button.setText("" + buttonTile.getValue());
									infoField.setText("Game won!");
									gameStatusLabel.setText("Game won!");
									gameStatusLabel.setStyle("-fx-background-color: #61DD69; ");
									gameStatusLabel.setVisible(true);
									game.setGameWon(true);
								}
							}
						}
					} else if (event.getButton() == MouseButton.SECONDARY) {
						if (!game.isGameWon() && !game.isGameLost() && !buttonTile.isVisible()) {
							buttonTile.changeFlagStatus();
							if (buttonTile.isFlagged()) {
								button.setText("!!");
								button.setStyle("-fx-background-color: #F75353; ");
							} else {
								button.setText("");
								button.setStyle(null);
							}
						}
					}
					displayNumberOfTilesMadeVisible();
				});
			}
		}
	}

	@FXML
	public void drawBoard() {
		for (int i = 0; i < buttonList.size(); i++) {
			board.getChildren().add(buttonList.get(i));
		}
	}

	private void showAllBombs() {
		game.makeAllBombsVisible();
		for (Tile bomb : game.getListOfMines()) {
			buttonList.get(bomb.getY() * game.getHeight() + bomb.getX()).setText("X");
		}
	}

	public Tile getFirstPressedTile() {
		Tile copy = firstPressedTile;
		return copy;
	}

	public void setFirstPressedTile(int y, int x) {
		this.firstPressedTile = new Tile(y, x, false);
	}

}
