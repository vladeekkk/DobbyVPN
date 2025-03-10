#!/bin/sh

if [ -z "$1" ]; then
    echo "Usage: $0 <submodule-directory>"
    exit 1
fi

SUBMODULE_DIR="$1"
PATCH_FILE="./patches/${SUBMODULE_DIR}.patch"

if [ ! -f "$PATCH_FILE" ]; then
    echo "Patch file $PATCH_FILE not found!"
    exit 1
fi

cd "$SUBMODULE_DIR" || { echo "Submodule directory $SUBMODULE_DIR not found"; exit 1; }

if ! git apply --check --allow-overlap "../$PATCH_FILE"; then
    echo "Cannot apply patch - conflicts detected!"
    exit 1
fi

echo "Applying patch..."
git apply --whitespace=nowarn --allow-overlap "../$PATCH_FILE"

if [ $? -eq 0 ]; then
    echo "Patch applied successfully!"
    echo "Don't forget to commit changes in parent repository if needed"
else
    echo "Failed to apply patch!"
    exit 1
fi