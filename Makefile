JAVAC = javac
JAVA = java
SRC_DIR = src
BUILD_DIR = build
MAIN = app/Main

SOURCES := $(shell find $(SRC_DIR) -name "*.java")

all: $(BUILD_DIR)
	$(JAVAC) -d $(BUILD_DIR) $(SOURCES)

$(BUILD_DIR):
	mkdir -p $(BUILD_DIR)

run: all
	$(JAVA) -cp $(BUILD_DIR) $(MAIN)

clean:
	rm -rf $(BUILD_DIR)