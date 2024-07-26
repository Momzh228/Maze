package edu.school21.maze.reader;

import edu.school21.maze.exception.InvalidMazeFormatException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import lombok.Getter;

@Getter
public class MazeReader {
  private boolean[][] rightWalls;
  private boolean[][] bottomWalls;
  private int rows;
  private int cols;

  public MazeReader(String path) throws InvalidMazeFormatException {
    parseMatrix(path);
  }

  public void parseMatrix(String path) throws InvalidMazeFormatException {
    try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
      String[] sizes = reader.readLine().trim().split(" +");
      if (sizes.length != 2)
        throw new InvalidMazeFormatException("Invalid size format. Expected two integers.");
      rows = parseInteger(sizes[0]);
      cols = parseInteger(sizes[1]);
      if ((rows <= 0 || cols <= 0) && (rows > 50 || cols > 50)) {
        throw new InvalidMazeFormatException("Invalid size values.");
      }
      rightWalls = new boolean[rows][cols];
      bottomWalls = new boolean[rows][cols];
      writeToMatrix(reader, rightWalls);
      reader.readLine();
      writeToMatrix(reader, bottomWalls);
    } catch (IOException e) {
      throw new InvalidMazeFormatException("IOException occurred: " + e.getMessage());
    }
  }

  private int parseInteger(String value) throws InvalidMazeFormatException {
    try {
      return Integer.parseInt(value);
    } catch (NumberFormatException e) {
      throw new InvalidMazeFormatException("Invalid integer value: " + value);
    }
  }

  private void writeToMatrix(BufferedReader reader, boolean[][] matrix)
      throws IOException, InvalidMazeFormatException {
    String line;
    for (int i = 0; i < rows; i++) {
      line = reader.readLine();
      if (line == null || line.trim().isEmpty()) {
        throw new InvalidMazeFormatException("Insufficient rows in the matrix.");
      }
      String[] values = line.trim().split(" +");
      if (values.length != cols) {
        throw new InvalidMazeFormatException(
            "Incorrect number of columns in row " + (i + 1) + ". Expected " + cols);
      }
      for (int j = 0; j < cols; j++) {
        matrix[i][j] = parseBoolean(values[j]);
      }
    }
  }

  private boolean parseBoolean(String value) throws InvalidMazeFormatException {
    if ("1".equals(value)) {
      return true;
    } else if ("0".equals(value)) {
      return false;
    } else {
      throw new InvalidMazeFormatException("Invalid matrix value: " + value);
    }
  }
}
