TARGET_PATH = Maze/target
BUILD_PATH = build/target
TRASH = Maze/dependency-reduced-pom.xml

all: uninstall install run

install:
	@mkdir build
	@cd Maze && mvn clean install
	@mv $(TARGET_PATH) build
	@make clean
	@cp dvi.md build

run:
	@java -jar $(BUILD_PATH)/maze.jar

uninstall:
	@rm -rf build

clean:
	@rm $(TRASH)

dvi:
	@open dvi.md

dist:
	@mkdir dist
	@cd Maze && mvn clean install
	@mv $(TARGET_PATH) dist
	@make clean
	@cp dvi.md dist

tests:
	@cd Maze && mvn clean test
