#!/bin/sh

if [ -z "$1" ]; then
    echo "Usage: $0 <submodule-directory>"
    exit 1
fi

SUBMODULE_DIR="$1"
PATCH_FILE="./patches/${SUBMODULE_DIR}.patch"

cd "$SUBMODULE_DIR" || { echo "Submodule directory $SUBMODULE_DIR not found"; exit 1; }

git add .
git diff HEAD > "../${PATCH_FILE}"
git reset .
