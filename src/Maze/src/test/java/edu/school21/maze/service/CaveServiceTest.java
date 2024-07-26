package edu.school21.maze.service;

import static org.junit.jupiter.api.Assertions.*;

import edu.school21.maze.exception.InvalidCaveFormatException;
import java.io.File;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class CaveServiceTest {

  private static CaveService caveService;

  @BeforeAll
  public static void setUp() {
    caveService = new CaveService();
  }

  @Test
  public void testInitializeCaveFromFile() throws InvalidCaveFormatException {
    String path = getClass().getClassLoader().getResource("cave.txt").getPath();
    boolean[][] expectedCells = {
      {false, true, false, true},
      {true, false, false, true},
      {false, true, false, false},
      {false, false, true, true}
    };
    boolean[][] resultCells = caveService.initializeCaveFromFile(new File(path));
    assertArrayEquals(expectedCells, resultCells);
  }

  @Test
  public void testGenerateCave() {
    int bornBound = 4;
    int dieBound = 3;
    int height = 4;
    int width = 4;

    boolean[][] resultCells = caveService.generateCave(bornBound, dieBound, height, width);
    assertNotNull(resultCells);
    assertEquals(width, resultCells.length);
    assertEquals(height, resultCells[0].length);
  }

  @Test
  public void testIncorrectCave() {
    String path = getClass().getClassLoader().getResource("incorrect_cave.txt").getPath();
    assertThrows(
        InvalidCaveFormatException.class,
        () -> {
          caveService.initializeCaveFromFile(new File(path));
        });
  }

  @Test
  public void testNextStep() {
    boolean[][] resultCells = caveService.nextStep();
    assertNotNull(resultCells);
  }
}
