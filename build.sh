#!/bin/bash

echo "=== LMS-Classroom Build & Run Script ==="

# -----------------------------
# Project Paths
# -----------------------------
PROJECT_ROOT="$(pwd)"
SRC_DIR="$PROJECT_ROOT/src"
BIN_DIR="$PROJECT_ROOT/bin"
MAIN_CLASS="app.App"

# -----------------------------
# Prepare directories
# -----------------------------
echo "[INFO] Creating bin directory..."
mkdir -p "$BIN_DIR"

# -----------------------------
# Build Java sources
# -----------------------------
echo "[BUILD] Compiling Java files..."
find "$SRC_DIR" -name "*.java" > sources.txt

if ! javac -d "$BIN_DIR" @sources.txt; then
    echo "[ERROR] Compilation failed."
    rm sources.txt
    exit 1
fi

rm sources.txt
echo "[SUCCESS] Compilation complete."

# -----------------------------
# Copy resources
# -----------------------------
if [ -d "$SRC_DIR/resources" ]; then
    echo "[INFO] Copying resources..."
    cp -r "$SRC_DIR/resources" "$BIN_DIR"/
    echo "[SUCCESS] Resources copied."
else
    echo "[INFO] No resources folder found. Skipping."
fi

# -----------------------------
# Run Project
# -----------------------------
echo "[RUN] Starting application..."
java -cp "$BIN_DIR" "$MAIN_CLASS"
EXIT_CODE=$?

echo ""
echo "=== Program exited with code: $EXIT_CODE ==="
exit $EXIT_CODE

