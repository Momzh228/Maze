package edu.school21.maze.solver;

import edu.school21.maze.model.Maze;
import edu.school21.maze.model.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import lombok.Getter;

public class MazeSolver {
  private final Point start;
  private final Point end;
  private final int[][] maze;
  private final int[] rowDirection = {-1, 1, 0, 0};
  private final int[] colDirection = {0, 0, 1, -1};
  private final int rows;
  private final int cols;
  @Getter List<Point> pathList;

  public MazeSolver(Maze maze, Point start, Point end) {
    this.maze = maze.getMaze();
    this.start = start;
    this.end = end;
    this.rows = maze.getRows();
    this.cols = maze.getCols();
    pathList = new ArrayList<>();
  }

  public int solveMaze() {
    boolean[][] visited = new boolean[rows][cols];
    visited[start.getX()][start.getY()] = true;
    Queue<Point> queue = new LinkedList<>();
    Map<Point, Point> path = new HashMap<>();
    queue.add(new Point(start.getX(), start.getY()));
    while (!queue.isEmpty()) {
      Point curr = queue.poll();
      if (curr.equals(end)) {
        pathToList(path);
        return 0;
      }
      for (int i = 0; i < 4; i++) {
        int cRow = curr.getX();
        int cCol = curr.getY();
        int nRow = cRow + rowDirection[i];
        int nCol = cCol + colDirection[i];
        if (isValidMove(curr.getX(), curr.getY(), nRow, nCol, i) && !visited[nRow][nCol]) {
          visited[nRow][nCol] = true;
          Point nextPoint = new Point(nRow, nCol);
          queue.offer(nextPoint);
          path.put(nextPoint, curr);
        }
      }
    }
    return -1;
  }

  private boolean isValidMove(int cx, int cy, int nx, int ny, int dir) {
    if (nx < 0 || ny < 0 || nx >= rows || ny >= cols) return false;
    int currCell = maze[cx][cy];
    int newCell = maze[nx][ny];
    if (dir == 0 && (newCell == 1 || newCell == 3)) {
      return false;
    } else if (dir == 1 && (currCell == 1 || currCell == 3)) {
      return false;
    } else if (dir == 2 && (currCell == 2 || currCell == 3)) {
      return false;
    } else if (dir == 3 && (newCell == 2 || newCell == 3)) {
      return false;
    }
    return true;
  }

  private void pathToList(Map<Point, Point> path) {
    for (Point at = end; at != null; at = path.get(at)) {
      pathList.add(at);
    }
    Collections.reverse(pathList);
  }
}
