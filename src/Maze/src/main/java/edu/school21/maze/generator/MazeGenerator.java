package edu.school21.maze.generator;

import edu.school21.maze.model.Maze;
import edu.school21.maze.reader.MazeReader;

public class MazeGenerator {
  private final boolean[][] rightWalls;
  private final boolean[][] bottomWalls;
  private final int rows;
  private final int cols;

  public MazeGenerator(MazeReader mazeReader) {
    this.rightWalls = mazeReader.getRightWalls();
    this.bottomWalls = mazeReader.getBottomWalls();
    this.rows = mazeReader.getRows();
    this.cols = mazeReader.getCols();
  }

  public MazeGenerator(boolean[][] rightWalls, boolean[][] bottomWalls, int rows, int cols) {
    this.rightWalls = rightWalls;
    this.bottomWalls = bottomWalls;
    this.rows = rows;
    this.cols = cols;
  }

  public Maze createMaze() {
    int[][] maze = new int[rows][cols];
    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < cols; j++) {
        if (rightWalls[i][j] && bottomWalls[i][j]) {
          maze[i][j] = 3;
        } else if (rightWalls[i][j] && !bottomWalls[i][j]) {
          maze[i][j] = 2;
        } else if (!rightWalls[i][j] && bottomWalls[i][j]) {
          maze[i][j] = 1;
        }
      }
    }
    return new Maze(maze, rows, cols);
  }
}
