package edu.school21.maze.generator;

import edu.school21.maze.model.Maze;
import java.util.ArrayList;
import java.util.Random;
import lombok.Getter;

public class EulerGenerator {
  private final Random random = new Random();
  @Getter private boolean[][] rightBorders;
  @Getter private boolean[][] bottomBorders;

  public Maze generate(int width, int height) {
    rightBorders = new boolean[height][width];
    bottomBorders = new boolean[height][width];

    int setIndex = 0; // Счетчик номеров множеств.
    ArrayList<Cell> line = new ArrayList<>(); // Текущая строка лабиринта.
    // Первично инициализируем все клетки новыми множествами.
    for (int i = 0; i < width; ++i) line.add(new Cell(setIndex++));

    // Создадим сверху-вниз все строки лабиринта.
    for (int lineIndex = 0; lineIndex < height; ++lineIndex) {
      // 1. Определяем правые границы для всех смежных клеток
      for (int i = 1; i < width; ++i) {
        if (line.get(i).setIndex
            == line.get(i - 1)
                .setIndex) { // Всегда ставим стену между клетками одного множества, чтобы избежать
          // циклов.
          line.get(i - 1).rightBorder = true;
        } else if (random
            .nextBoolean()) { // Случайно выбираем ставить стену или нет между клетками разных
          // множеств.
          // Если ставим, то они остаются в разных множествах и ничего не меняется.
          line.get(i - 1).rightBorder = true;
        } else {
          // Если не ставим, то объединяем множества
          int changingSet = line.get(i).setIndex;
          for (int j = 0; j < width; ++j) {
            // Все элементы правого множества становятся элементами левого
            if (line.get(j).setIndex == changingSet) {
              line.get(j).setIndex = line.get(i - 1).setIndex;
            }
          }
        }
      }

      // Ставим правую стену для последней клетки в строке
      line.get(width - 1).rightBorder = true;

      // 2. Устанавливаем нижние стены
      // Для множества клеток должна быть хотя бы одна без нижней границы,
      // чтобы избежать изолированных областей
      for (int i = 0; i < width; ++i) {
        int count = 0;
        for (int j = 0; j < width; ++j) {
          if (line.get(j).setIndex == line.get(i).setIndex) {
            ++count;
          }
        }
        if (random.nextBoolean() && count != 1) {
          line.get(i).bottomBorder = true;
        }
      }

      // 3. Убедимся что у множества есть хотя бы одна клетка без нижней стены
      if (lineIndex != height - 1) {
        for (int i = 0; i < width; ++i) {
          int countHole = 0;
          for (int j = 0; j < width; ++j) {
            if (line.get(i).setIndex == line.get(j).setIndex && !line.get(j).bottomBorder)
              ++countHole;
          }
          if (countHole == 0) {
            line.get(i).bottomBorder = false;
          }
        }
      } else {
        // Для последней строки:
        // I. всем клеткам зададим нижние стены
        line.forEach(e -> e.bottomBorder = true);
        // II. Важно! А также удалим правые стены между клетками разных множеств
        for (int i = 1; i < width; ++i) {
          if (line.get(i - 1).setIndex != line.get(i).setIndex) {
            line.get(i - 1).rightBorder = false;
          }
        }
      }

      // 4. Теперь строка имеет все стены, записываем ее в итоговую матрицу
      for (int i = 0; i < width; ++i) {
        if (line.get(i).rightBorder) rightBorders[lineIndex][i] = true;
        if (line.get(i).bottomBorder) bottomBorders[lineIndex][i] = true;
      }

      // 5. Подготовим новую строку
      for (Cell cell : line) {
        // Удалим все правые границы
        cell.rightBorder = false;
        // Если ячейка имеет нижнюю границу, то удалим ее из множества и поместим в новое
        if (cell.bottomBorder) {
          cell.setIndex = setIndex++;
        }
        // Удалим все нижние границы
        cell.bottomBorder = false;
      }
    }
    return new MazeGenerator(rightBorders, bottomBorders, height, width).createMaze();
  }

  private class Cell {
    public int setIndex;
    public boolean rightBorder;
    public boolean bottomBorder;

    public Cell(int setIndex) {
      this.setIndex = setIndex;
      rightBorder = false;
      bottomBorder = false;
    }
  }
}
