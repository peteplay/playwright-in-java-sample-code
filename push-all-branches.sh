#!/bin/bash

# List of branch names to process
BRANCHES=(
    "sample-code/start-here"
    "sample-code/module-3-my-first-playwright-test"
    "sample-code/module-4-interacting-with-elements"
    "sample-code/module-5-refactoring"
    "sample-code/module-6-browser-options"
    "sample-code/module-7-browser-contexts"
    "sample-code/module-8-locators"
    "sample-code/module-9-forms"
    "sample-code/module-10-assertions"
    "sample-code/module-11-waits"
    "sample-code/module-12-api-interactions"
    "sample-code/module-12-mocking-api-calls"
    "sample-code/module-13-page-objects"
    "sample-code/module-14-allure-reporting"
    "sample-code/module-14-organizing-your-tests"
    "sample-code/module-14-parallel-execution"
    "sample-code/module-15-parallel-execution"
    "sample-code/module-16-allure-reporting"
    "sample-code/module-17-cucumber"
)

# Iterate over each branch
for BRANCH_NAME in "${BRANCHES[@]}"; do
    echo "Processing branch $BRANCH_NAME..."

    # Check if the branch already exists locally
    if git branch --list | grep -q "$BRANCH_NAME"; then
        echo "Branch $BRANCH_NAME already exists locally. Checking it out..."
        git checkout "$BRANCH_NAME"
    else
        echo "Creating and checking out branch $BRANCH_NAME..."
        git checkout -b "$BRANCH_NAME"
    fi

    # Add and commit changes (if any)
    git add .
    git commit -m "Add or update code for $BRANCH_NAME" || echo "No changes to commit for $BRANCH_NAME"

    # Push the branch to the origin remote
    echo "Pushing branch $BRANCH_NAME to GitHub..."
    git push -u origin "$BRANCH_NAME"

    echo "Branch $BRANCH_NAME processed successfully."
done

echo "All branches have been processed!"
