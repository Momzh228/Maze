# Документация проекта Maze

## Введение
Программа Maze предназначена для генерации и отображения идеальных лабиринтов и пещер. Проект реализован на языке Java с графическим пользовательским интерфейсом (GUI) и поддерживает загрузку, генерацию и решение лабиринтов, а также генерацию пещер с использованием клеточных автоматов.

## Структура проекта
- **Исходный код**: Все исходные файлы находятся в папке `src`.
- **Сборка и установка**: Для сборки используется Makefile с целями: `all`, `install`, `uninstall`, `clean`, `dvi`, `dist`, `tests`. Установка производится в указанный пользователем каталог.

## Часть 1: Реализация программы Maze
### Требования
- **Язык разработки**: Java.
- **Стиль кода**: Google Style.
- **Максимальный размер лабиринта**: 50x50.
- **Поле отображения**: 500x500 пикселей.
- **Толщина стен**: 2 пикселя.
- **Размер ячеек**: Вычисляется автоматически для заполнения всего поля.

### Графический интерфейс
- GUI построен на любой выбранной библиотеке для Java.
- Предусмотрена кнопка для загрузки лабиринта из файла.

## Часть 2: Генерация идеального лабиринта
### Алгоритм генерации
- **Алгоритм Эллера**: Используется для генерации идеального лабиринта.
- **Критерии**: Лабиринт должен быть связанным (без изолированных областей и петель).

### Функции
- **Ввод размеров**: Пользователь указывает количество строк и столбцов.
- **Сохранение лабиринта**: Сгенерированный лабиринт сохраняется в файл.
- **Отображение**: Лабиринт отображается в GUI.

### Тестирование
- Полное покрытие unit-тестами модуля генерации.

## Часть 3: Решение лабиринта
### Функции
- **Задание точек**: Пользователь указывает начальную и конечную точки.
- **Отображение маршрута**: Решение отображается линией толщиной 2 пикселя, проходящей через середины ячеек.
- **Цвет линии**: Отличается от цветов стен и поля.

### Тестирование
- Полное покрытие unit-тестами модуля решения.

## Часть 4: Генерация пещер
### Параметры генерации
- **Файл с пещерой**: Пользователь загружает файл.
- **Максимальный размер**: 50x50.
- **Поле отображения**: 500x500 пикселей.
- **Параметры клеточного автомата**: Пользователь задает пределы «рождения» и «смерти» клетки, а также шанс начальной инициализации.

### Отображение
- **Режимы отрисовки**:
    - Пошаговая итерация по кнопке.
    - Автоматическая отрисовка с заданной частотой.

### Тестирование
- Полное покрытие unit-тестами модуля генерации пещер.

## Заключение
Проект Maze предоставляет функционал для создания, отображения и решения лабиринтов, а также генерации пещер. Реализованная программа включает графический интерфейс для удобного взаимодействия с пользователем и поддерживает все необходимые требования, включая масштабируемость и тестируемость кода.
