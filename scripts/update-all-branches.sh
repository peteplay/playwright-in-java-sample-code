#!/bin/bash

# Check if commit hash is provided as an argument
if [ -z "$1" ]; then
  echo "Usage: $0 <commit-hash>"
  exit 1
fi

# Commit to cherry-pick (from the command-line argument)
COMMIT_HASH="$1"

# List of branches
BRANCHES=(
  "sample-code/end-to-end"
  "sample-code/module-10-assertions"
  "sample-code/module-11-waits"
  "sample-code/module-12-mocking-api-calls"
  "sample-code/module-12-api-interactions "
  "sample-code/module-13-page-objects"
  "sample-code/module-14-allure-reporting"
  "sample-code/module-14-organizing-your-tests"
  "sample-code/module-14-parallel-execution"
  "sample-code/module-15-parallel-execution"
  "sample-code/module-16-allure-reporting"
  "sample-code/module-17-cucumber"
  "sample-code/module-3-my-first-playwright-test"
  "sample-code/module-4-interacting-with-elements"
  "sample-code/module-5-refactoring"
  "sample-code/module-6-browser-options"
  "sample-code/module-7-browser-contexts"
  "sample-code/module-8-locators"
  "sample-code/module-9-forms"
  "sample-code/start-here"
)

# Iterate over each branch
for BRANCH in "${BRANCHES[@]}"; do
  echo "Processing branch: $BRANCH"

  # Checkout the branch
  git checkout "$BRANCH"
  if [ $? -ne 0 ]; then
    echo "Failed to checkout branch: $BRANCH"
    continue
  fi

  # Cherry-pick the commit
  git cherry-pick "$COMMIT_HASH"
  if [ $? -ne 0 ]; then
    echo "Merge conflict detected on branch: $BRANCH"
    echo "Please resolve the conflict, commit the changes, and type 'continue' to proceed, or 'abort' to skip this branch."
    
    while true; do
      read -p "Type 'continue' to proceed or 'abort' to skip this branch: " RESPONSE
      case $RESPONSE in
        continue)
          echo "Continuing after resolving the conflict..."
          break
          ;;
        abort)
          echo "Aborting cherry-pick on branch: $BRANCH"
          git cherry-pick --abort
          continue 2
          ;;
        *)
          echo "Invalid input. Please type 'continue' or 'abort'."
          ;;
      esac
    done
  fi

  # Commit the changes if no conflict or resolved
  git commit --allow-empty -m "Cherry-pick commit $COMMIT_HASH to update branch $BRANCH"
  if [ $? -ne 0 ]; then
    echo "Failed to commit changes on branch: $BRANCH"
    continue
  fi

  # Push the changes to origin
  git push origin "$BRANCH"
  if [ $? -ne 0 ]; then
    echo "Failed to push branch: $BRANCH"
    continue
  fi

  echo "Successfully processed branch: $BRANCH"
done

echo "All branches processed."
