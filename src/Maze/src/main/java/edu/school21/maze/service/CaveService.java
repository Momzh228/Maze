package edu.school21.maze.service;

import edu.school21.maze.exception.InvalidCaveFormatException;
import edu.school21.maze.generator.CaveGenerator;
import edu.school21.maze.model.Cave;
import edu.school21.maze.reader.CaveReader;
import java.io.File;

public class CaveService {

  private final CaveGenerator generator;
  private Cave cave;

  public CaveService() {
    this.generator = new CaveGenerator();
  }

  public boolean[][] initializeCaveFromFile(File file) throws InvalidCaveFormatException {
    CaveReader caveReader = new CaveReader(file);
    return caveReader.parseFile();
  }

  public boolean[][] generateCave(int bornBound, int dieBound, int height, int width) {
    generator.setBornBound(bornBound);
    generator.setDieBound(dieBound);
    cave = generator.generate(width, height);
    return cave.getCells();
  }

  public boolean[][] nextStep() {
    generator.step();
    return cave.getCells();
  }
}
