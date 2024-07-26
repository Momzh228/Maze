package edu.school21.maze.generator;

import edu.school21.maze.model.Cave;
import java.util.Random;
import lombok.Setter;

public class CaveGenerator {

  @Setter int dieBound;
  @Setter int bornBound;
  Cave lastCave;
  Random random = new Random();

  public CaveGenerator() {
    this.bornBound = 0;
    this.dieBound = 0;
    lastCave = null;
  }

  public Cave generate(int width, int height) {
    boolean[][] cells = new boolean[height][width];
    for (int i = 0; i < height; ++i) {
      for (int j = 0; j < width; ++j) {
        cells[i][j] = random.nextBoolean();
      }
    }
    Cave result = new Cave(width, height);
    result.setCells(cells);
    lastCave = result;
    return result;
  }

  public void step() {
    if (lastCave == null) return;
    boolean[][] newCells = new boolean[lastCave.getHeight()][lastCave.getWidth()];
    for (int y = 0; y < lastCave.getHeight(); ++y) {
      for (int x = 0; x < lastCave.getWidth(); ++x) {
        newCells[y][x] = process(x, y);
      }
    }
    lastCave.setCells(newCells);
  }

  private boolean process(int x, int y) {
    int aliveCount = aliveAround(x, y);
    if (aliveCount >= bornBound) {
      return true;
    } else if (aliveCount < dieBound) {
      return false;
    } else {
      return lastCave.getCells()[y][x];
    }
  }

  private int aliveAround(int x, int y) {
    int result = 0;
    int[][] offsets = {
      {-1, -1}, {0, -1}, {1, -1}, {-1, 0}, {1, 0}, {-1, 1}, {0, 1}, {1, 1},
    };
    for (int[] offset : offsets) {
      if (isAlive(x + offset[0], y + offset[1])) ++result;
    }
    return result;
  }

  private boolean isAlive(int x, int y) {
    if (x < 0 || y < 0) return true;
    else if (x >= lastCave.getWidth() || y >= lastCave.getHeight()) return true;
    else return lastCave.getCells()[y][x];
  }
}
