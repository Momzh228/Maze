package edu.school21.maze.service;

import static org.junit.jupiter.api.Assertions.*;

import edu.school21.maze.exception.InvalidMazeFormatException;
import edu.school21.maze.model.Point;
import edu.school21.maze.writer.MazeWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class MazeServiceTest {

  private static MazeService mazeService;

  @BeforeAll
  public static void setUp() {
    mazeService = new MazeService();
  }

  @Test
  public void testInitializeMazeFromPath() {
    String path = getClass().getClassLoader().getResource("maze.txt").getPath();
    int rows = 4;
    int cols = 4;
    try {
      mazeService.initializeMazeFromFile(path);
    } catch (InvalidMazeFormatException e) {
      throw new RuntimeException(e);
    }
    int[][] maze = mazeService.getMaze();
    assertNotNull(mazeService.getMaze());
    assertEquals(rows, maze.length);
    assertEquals(cols, maze[0].length);
  }

  @Test
  public void testInitializeMaze() {
    int rows = 10;
    int cols = 10;
    mazeService.initializeMaze(rows, cols);
    int[][] maze = mazeService.getMaze();
    assertNotNull(maze);
    assertEquals(rows, maze.length);
    assertEquals(cols, maze[0].length);
  }

  @Test
  public void testSolveMaze() {
    String path = getClass().getClassLoader().getResource("maze.txt").getPath();
    try {
      mazeService.initializeMazeFromFile(path);
    } catch (InvalidMazeFormatException e) {
      throw new RuntimeException(e);
    }
    int startX = 0;
    int startY = 0;
    int endX = 3;
    int endY = 3;

    int result = mazeService.solveMaze(startX, startY, endX, endY);
    assertEquals(0, result);
    List<Point> solution = mazeService.getSolution();
    assertNotNull(solution);
    assertFalse(solution.isEmpty());
  }

  @Test
  public void testSolveUnsolvableMaze() {
    String path = getClass().getClassLoader().getResource("unsolvable_maze.txt").getPath();
    try {
      mazeService.initializeMazeFromFile(path);
    } catch (InvalidMazeFormatException e) {
      throw new RuntimeException(e);
    }

    int startX = 1;
    int startY = 0;
    int endX = 3;
    int endY = 3;

    int result = mazeService.solveMaze(startX, startY, endX, endY);

    assertEquals(-1, result);
    List<Point> solution = mazeService.getSolution();
    assertTrue(solution.isEmpty());
  }

  @Test
  public void testIncorrectMaze() {
    String path = getClass().getClassLoader().getResource("incorrect_maze.txt").getPath();
    assertThrows(
        InvalidMazeFormatException.class,
        () -> {
          mazeService.initializeMazeFromFile(path);
        });
  }

  @Test
  public void testWriteGeneratedMazeToFile() {
    boolean[][] rightWalls = {
      {false, false, false, true},
      {true, false, true, true},
      {false, true, false, true},
      {false, false, false, true}
    };
    boolean[][] bottomWalls = {
      {true, false, true, false},
      {false, false, true, false},
      {true, true, false, true},
      {true, true, true, true}
    };
    String fileName = "generated_maze.txt";
    MazeWriter writer = new MazeWriter(rightWalls, bottomWalls);
    writer.writeGeneratedMazeToFile();
    String projectRoot = System.getProperty("user.dir");
    String filePath = projectRoot + java.io.File.separator + fileName;
    File file = new File(filePath);

    assertTrue(file.exists());

    try {
      String content = new String(Files.readAllBytes(Paths.get(filePath)));
      String expectedContent =
          "4 4\n"
              + "0 0 0 1 \n"
              + "1 0 1 1 \n"
              + "0 1 0 1 \n"
              + "0 0 0 1 \n"
              + "\n"
              + "1 0 1 0 \n"
              + "0 0 1 0 \n"
              + "1 1 0 1 \n"
              + "1 1 1 1 \n";
      assertEquals(content, expectedContent);
    } catch (IOException e) {
      fail("Failed to read the generated file.");
    }
  }
}
