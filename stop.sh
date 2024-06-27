#!/bin/bash

# Find and kill the Java process running the application
PID=$(ps aux | grep '[j]ava -jar *.war' | awk '{print $2}')

if [ -n "$PID" ]; then
  echo "Stopping application with PID $PID"
  kill $PID
else
  echo "No running application found"
fi
