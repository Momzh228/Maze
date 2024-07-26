package edu.school21.maze.controller;

import edu.school21.maze.exception.InvalidCaveFormatException;
import edu.school21.maze.exception.InvalidMazeFormatException;
import edu.school21.maze.model.Point;
import edu.school21.maze.service.CaveService;
import edu.school21.maze.service.MazeService;
import java.io.File;
import java.util.List;
import java.util.Objects;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.SpinnerValueFactory.IntegerSpinnerValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class MazeView extends Application {

  private final MazeService mazeService = new MazeService();
  private final double WINDOW_WIDTH = 500;
  private final double WINDOW_HEIGHT = 500;
  private final CaveService caveService = new CaveService();
  @FXML private Button fileChooserMaze;
  @FXML private Canvas window;
  @FXML private Spinner<Integer> spinnerBornBound;
  @FXML private Spinner<Integer> spinnerCols;
  @FXML private Spinner<Integer> spinnerColsCave;
  @FXML private Spinner<Integer> spinnerDieBound;
  @FXML private Spinner<Integer> spinnerRows;
  @FXML private Spinner<Integer> spinnerRowsCave;
  @FXML private Button stepCave;
  @FXML private Button startTimer;
  @FXML private Spinner<Integer> timerForStep;
  @FXML private Button fileChooserCave;
  @FXML private Button stopTimer;
  @FXML private Button generateMaze;
  @FXML private Button generateCave;
  @FXML private Button saveMaze;
  private boolean isRun = false;
  private Stage mainStage;
  private GraphicsContext gc;
  private int[][] mazeMatrix;
  private boolean[][] caveMatrix;
  private int startX;
  private int startY;
  private int finishX;
  private int finishY;

  @FXML
  public void initialize() {
    gc = window.getGraphicsContext2D();
    spinnerRows.setValueFactory(setValueFactoryForSpinnerRowsAndCols());
    spinnerCols.setValueFactory(setValueFactoryForSpinnerRowsAndCols());
    spinnerRowsCave.setValueFactory(setValueFactoryForSpinnerRowsAndCols());
    spinnerColsCave.setValueFactory(setValueFactoryForSpinnerRowsAndCols());
    spinnerDieBound.setValueFactory(setValueFactoryForSpinnerBounds());
    spinnerBornBound.setValueFactory(setValueFactoryForSpinnerBounds());
    timerForStep.setValueFactory(new IntegerSpinnerValueFactory(1, 1000000, 1000, 100));
    spinnerRows
        .valueProperty()
        .addListener((observable, oldValue, newValue) -> generateRandomMaze());
    spinnerCols
        .valueProperty()
        .addListener((observable, oldValue, newValue) -> generateRandomMaze());
    spinnerColsCave
        .valueProperty()
        .addListener((observable, oldValue, newValue) -> generateRandomCave());
    spinnerRowsCave
        .valueProperty()
        .addListener((observable, oldValue, newValue) -> generateRandomCave());
    spinnerBornBound
        .valueProperty()
        .addListener((observable, oldValue, newValue) -> generateRandomCave());
    spinnerDieBound
        .valueProperty()
        .addListener((observable, oldValue, newValue) -> generateRandomCave());
    fileChooserMaze.setOnAction(this::setFileChooserMaze);
    window.setOnMouseClicked(this::handleClick);
    stepCave.setOnMouseClicked(this::handleClickStep);
    startTimer.setOnMouseClicked(this::handleClickRun);
    fileChooserCave.setOnAction(this::setFileChooserCave);
    stopTimer.setOnMouseClicked(this::handleClickStop);
    generateMaze.setOnAction(
        event -> {
          generateRandomMaze();
        });
    generateCave.setOnAction(
        event -> {
          generateRandomCave();
        });
    saveMaze.setOnMouseClicked(this::saveMazetoFile);
    generateRandomMaze();
    stopTimer.setDisable(true);
  }

  private void saveMazetoFile(MouseEvent mouseEvent) {
    mazeService.writeGeneratedMazeToFile();
  }

  private void handleClickRun(MouseEvent mouseEvent) {
    stopTimer.setDisable(false);
    startTimer.setDisable(true);
    long time = timerForStep.getValue();
    isRun = true;
    new Thread(
            () -> {
              if (caveMatrix == null) generateRandomCave();
              while (isRun) {
                Platform.runLater(this::nextStep);
                try {
                  Thread.sleep(time);
                } catch (InterruptedException e) {
                  System.err.println(e.getMessage());
                }
              }
            })
        .start();
  }

  private void handleClickStop(MouseEvent mouseEvent) {
    stopTimer.setDisable(true);
    startTimer.setDisable(false);
    isRun = false;
  }

  private void handleClickStep(MouseEvent mouseEvent) {
    nextStep();
  }

  private void nextStep() {
    if (caveMatrix != null) {
      clearWindow();
      caveMatrix = caveService.nextStep();
      Platform.runLater(this::printCave);
    } else {
      generateRandomCave();
    }
  }

  private IntegerSpinnerValueFactory setValueFactoryForSpinnerRowsAndCols() {
    return new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 50, 20);
  }

  private IntegerSpinnerValueFactory setValueFactoryForSpinnerBounds() {
    return new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 7, 3);
  }

  @Override
  public void start(Stage primaryStage) throws Exception {
    this.mainStage = primaryStage;
    Parent view =
        FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/maze_view.fxml")));
    Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/dota.png")));
    primaryStage.getIcons().add(icon);
    primaryStage.setTitle("Maze");
    primaryStage.setScene(new Scene(view));
    primaryStage.setResizable(false);
    primaryStage.show();
  }

  private void generateRandomMaze() {
    handleClickStop(null);
    mazeService.initializeMaze(spinnerRows.getValue(), spinnerCols.getValue());
    mazeMatrix = mazeService.getMaze();
    printMaze();
  }

  private void generateRandomCave() {
    handleClickStop(null);
    clearWindow();
    int bornBound = spinnerBornBound.getValue();
    int dieBound = spinnerDieBound.getValue();
    int rows = spinnerRowsCave.getValue();
    int cols = spinnerColsCave.getValue();
    caveMatrix = caveService.generateCave(bornBound, dieBound, rows, cols);
    printCave();
  }

  private void printCave() {
    clearWindow();
    double lineWidth = WINDOW_WIDTH / caveMatrix[0].length;
    double lineHeight = WINDOW_HEIGHT / caveMatrix.length;
    for (int row = 0; row < caveMatrix.length; ++row) {
      for (int col = 0; col < caveMatrix[0].length; ++col) {
        if (caveMatrix[row][col]) {
          gc.fillRect(col * lineWidth, row * lineHeight, lineWidth, lineHeight);
        }
      }
    }
  }

  private void clearWindow() {
    gc.clearRect(0, 0, window.getWidth(), window.getHeight());
  }

  private void printMaze() {
    clearWindow();
    double lineWidth = WINDOW_WIDTH / mazeMatrix[0].length;
    double lineHeight = WINDOW_HEIGHT / mazeMatrix.length;
    gc.setStroke(Color.BLACK);
    gc.setLineWidth(2);
    for (int row = 0; row < mazeMatrix.length; ++row) {
      for (int col = 0; col < mazeMatrix[0].length; ++col) {
        double x = col * lineWidth;
        double y = row * lineHeight;
        switch (mazeMatrix[row][col]) {
          case 1:
            gc.strokeLine(x, y + lineHeight, x + lineWidth, y + lineHeight);
            break;
          case 2:
            gc.strokeLine(x + lineWidth, y, x + lineWidth, y + lineHeight);
            break;
          case 3:
            gc.strokeLine(x, y + lineHeight, x + lineWidth, y + lineHeight);
            gc.strokeLine(x + lineWidth, y, x + lineWidth, y + lineHeight);
            break;
        }
      }
    }
  }

  private void handleClick(MouseEvent mouseEvent) {
    handleClickStop(null);
    isRun = false;
    printMaze();
    gc.setStroke(Color.RED);
    gc.setLineWidth(2);
    double lineWidth = WINDOW_WIDTH / mazeMatrix[0].length;
    double lineHeight = WINDOW_HEIGHT / mazeMatrix.length;
    processCoordinates(mouseEvent, lineWidth, lineHeight);
    int res = mazeService.solveMaze(startY, startX, finishY, finishX);
    if (res == -1) {
      showErrorMessage();
    }
    List<Point> pathList = mazeService.getSolution();
    for (int i = 0; i < pathList.size() - 1; ++i) {
      Point current = pathList.get(i);
      Point next = pathList.get(i + 1);
      double currentX = current.getY() * lineWidth + lineWidth / 2;
      double currentY = current.getX() * lineHeight + lineHeight / 2;
      double nextX = next.getY() * lineWidth + lineWidth / 2;
      double nextY = next.getX() * lineHeight + lineHeight / 2;
      gc.strokeLine(currentX, currentY, nextX, nextY);
    }
  }

  private void processCoordinates(MouseEvent mouseEvent, double lineWidth, double lineHeight) {
    if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
      double X = mouseEvent.getX();
      double Y = mouseEvent.getY();
      startX = (int) Math.floor(X / lineWidth);
      startY = (int) Math.floor(Y / lineHeight);
    } else if (mouseEvent.getButton().equals(MouseButton.SECONDARY)) {
      double X = mouseEvent.getX();
      double Y = mouseEvent.getY();
      finishX = (int) Math.floor(X / lineWidth);
      finishY = (int) Math.floor(Y / lineHeight);
    }
  }

  private void setFileChooserMaze(ActionEvent actionEvent) {
    handleClickStop(null);
    try {
      File selectedFile = selectFile();
      if (selectedFile != null) {
        mazeService.initializeMazeFromFile(selectedFile.getAbsolutePath());
        mazeMatrix = mazeService.getMaze();
        printMaze();
      } else {
        errorSelectedFile();
      }
    } catch (InvalidMazeFormatException e) {
      errorIllegalFile();
    }
  }

  private void setFileChooserCave(ActionEvent actionEvent) {
    handleClickStop(null);
    try {
      File selectedFile = selectFile();
      if (selectedFile != null) {
        caveMatrix = caveService.initializeCaveFromFile(selectedFile);
        printCave();
      } else {
        errorSelectedFile();
      }
    } catch (InvalidCaveFormatException e) {
      errorIllegalFile();
    }
  }

  private File selectFile() {
    FileChooser fc = new FileChooser();
    fc.setTitle("Open Resource File");
    fc.getExtensionFilters().addAll(new ExtensionFilter("Text Files", "*.txt"));
    return fc.showOpenDialog(mainStage);
  }

  private void errorSelectedFile() {
    Alert alert = new Alert(AlertType.ERROR, "No file selected");
    alert.setTitle("Input fields empty");
    alert.setContentText("No file selected");
    alert.showAndWait();
  }

  private void errorIllegalFile() {
    Alert alert = new Alert(AlertType.ERROR, "No file selected");
    alert.setTitle("Illegal File Selected");
    alert.setContentText("Illegal File Selected");
    alert.showAndWait();
  }

  private void showErrorMessage() {
    Alert alert = new Alert(AlertType.ERROR);
    alert.setTitle("Ошибка");
    alert.setHeaderText("Не удалось найти путь в лабиринте");
    alert.setContentText("Пожалуйста, укажите другие начальную и конечную точки лабиринта.");
    alert.showAndWait();
  }
}
