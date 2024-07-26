package edu.school21.maze.model;

import lombok.Getter;

@Getter
public class Maze {
  private int[][] maze;
  private int rows;
  private int cols;

  public Maze(int[][] maze, int rows, int cols) {
    this.maze = maze;
    this.rows = rows;
    this.cols = cols;
  }
}
