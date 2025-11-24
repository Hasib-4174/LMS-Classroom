#!/bin/bash

PROJECT_ROOT="$(pwd)"
MAIN_CLASS="com.lms.App"
SRC_DIR="$PROJECT_ROOT/src/main/java"
BIN_DIR="$PROJECT_ROOT/bin"

echo "=== LMS-Classroom Build & Run Script ==="

# Create bin directory
mkdir -p "$BIN_DIR"

echo "[BUILD] Compiling Java sources..."
find "$SRC_DIR" -name "*.java" > sources.txt
javac -d "$BIN_DIR" @sources.txt

if [ $? -ne 0 ]; then
    echo "[ERROR] Compilation failed."
    rm sources.txt
    exit 1
fi

rm sources.txt

echo "[RUN] Running project..."
java -cp "$BIN_DIR" "$MAIN_CLASS"

exit 0
