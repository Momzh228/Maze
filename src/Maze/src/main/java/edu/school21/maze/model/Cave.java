package edu.school21.maze.model;

import lombok.Getter;
import lombok.Setter;

@Getter
public class Cave {
  private final int width;
  private final int height;
  @Setter private boolean[][] cells;

  public Cave(int width, int height) throws IllegalArgumentException {
    this.height = height;
    this.width = width;
    cells = new boolean[height][width];
  }
}
