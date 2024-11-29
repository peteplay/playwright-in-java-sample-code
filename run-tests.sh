#!/bin/bash

# Build Docker images
docker-compose build

# Start services and stop all when tests are complete
docker-compose up --abort-on-container-exit

# Capture the exit code of the tests service
EXIT_CODE=$(docker-compose ps -q tests | xargs docker inspect -f '{{ .State.ExitCode }}')

# Shutdown services
docker-compose down

# Exit with the tests service's exit code
exit $EXIT_CODE
