package edu.school21.maze.service;

import edu.school21.maze.exception.InvalidMazeFormatException;
import edu.school21.maze.generator.EulerGenerator;
import edu.school21.maze.generator.MazeGenerator;
import edu.school21.maze.model.Maze;
import edu.school21.maze.model.Point;
import edu.school21.maze.reader.MazeReader;
import edu.school21.maze.solver.MazeSolver;
import edu.school21.maze.writer.MazeWriter;
import java.util.List;

public class MazeService {
  private MazeSolver mazeSolver;
  private Maze maze;
  private EulerGenerator eulerGenerator;

  public void initializeMazeFromFile(String path) throws InvalidMazeFormatException {
    MazeReader file = new MazeReader(path);
    maze = new MazeGenerator(file).createMaze();
  }

  public void initializeMaze(int rows, int cols) {
    eulerGenerator = new EulerGenerator();
    maze = eulerGenerator.generate(cols, rows);
  }

  public void writeGeneratedMazeToFile() {
    new MazeWriter(eulerGenerator.getRightBorders(), eulerGenerator.getBottomBorders())
        .writeGeneratedMazeToFile();
  }

  public int solveMaze(int x1, int y1, int x2, int y2) {
    mazeSolver = new MazeSolver(maze, new Point(x1, y1), new Point(x2, y2));
    return mazeSolver.solveMaze();
  }

  public int[][] getMaze() {
    return maze.getMaze();
  }

  public List<Point> getSolution() {
    return mazeSolver.getPathList();
  }
}
