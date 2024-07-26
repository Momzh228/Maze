package edu.school21.maze.writer;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class MazeWriter {
  private final boolean[][] rightWalls;
  private final boolean[][] bottomWalls;

  public MazeWriter(boolean[][] rightWalls, boolean[][] bottomWalls) {
    this.rightWalls = rightWalls;
    this.bottomWalls = bottomWalls;
  }

  public void writeGeneratedMazeToFile() {
    String fileName = "generated_maze.txt";
    String projectRoot = System.getProperty("user.dir");
    String filePath = projectRoot + java.io.File.separator + fileName;
    int rows = rightWalls.length;
    int cols = rightWalls[0].length;
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
      writer.write(rows + " " + cols + "\n");
      writeWalls(writer, rightWalls);
      writer.newLine();
      writeWalls(writer, bottomWalls);
    } catch (IOException e) {
      throw new RuntimeException("Failed to write maze to file: " + filePath, e);
    }
  }

  private void writeWalls(BufferedWriter writer, boolean[][] walls) throws IOException {
    int rows = walls.length;
    int cols = walls[0].length;

    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < cols; j++) {
        writer.write((walls[i][j] ? "1" : "0") + " ");
      }
      writer.write("\n");
    }
  }
}
