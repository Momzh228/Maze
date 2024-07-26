package edu.school21.maze.reader;

import edu.school21.maze.exception.InvalidCaveFormatException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class CaveReader {
  private final File file;

  public CaveReader(File file) {
    this.file = file;
  }

  public boolean[][] parseFile() throws InvalidCaveFormatException {
    try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
      String[] sizes = reader.readLine().trim().split(" +");
      if (sizes.length != 2)
        throw new InvalidCaveFormatException("Invalid size format. Expected two integers.");
      int rows = parseInteger(sizes[0]);
      int cols = parseInteger(sizes[1]);
      if ((rows <= 0 || cols <= 0) && (rows > 50 || cols > 50)) {
        throw new InvalidCaveFormatException("Invalid size values.");
      }
      boolean[][] cells = new boolean[rows][cols];
      for (int i = 0; i < rows; i++) {
        String line = reader.readLine();
        if (line == null || line.trim().isEmpty())
          throw new InvalidCaveFormatException("Insufficient rows in the matrix.");
        String[] values = line.trim().split(" +");
        if (values.length != cols)
          throw new InvalidCaveFormatException(
              "Incorrect number of columns in row " + (i + 1) + ". Expected " + cols);
        for (int j = 0; j < cols; j++) cells[i][j] = parseBoolean(values[j]);
      }
      return cells;
    } catch (IOException e) {
      throw new InvalidCaveFormatException("IOException occurred: " + e.getMessage());
    }
  }

  private int parseInteger(String value) throws InvalidCaveFormatException {
    try {
      return Integer.parseInt(value);
    } catch (NumberFormatException e) {
      throw new InvalidCaveFormatException("Invalid integer value: " + value);
    }
  }

  private boolean parseBoolean(String value) throws InvalidCaveFormatException {
    if ("1".equals(value)) {
      return true;
    } else if ("0".equals(value)) {
      return false;
    } else {
      throw new InvalidCaveFormatException("Invalid matrix value: " + value);
    }
  }
}
