#!/bin/bash
cd /home/kavia/workspace/code-generation/email-suite-490/gmail_backend
./gradlew checkstyleMain
LINT_EXIT_CODE=$?
if [ $LINT_EXIT_CODE -ne 0 ]; then
   exit 1
fi

