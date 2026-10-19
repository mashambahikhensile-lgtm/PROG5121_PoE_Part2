# PROG5121 PoE — Part 2: Sending Messages

This project extends the Part 1 QuickChat registration and login application. It is a console-based Java application built in NetBeans with Maven and JUnit 5.

## Features
- Registration checks for username, password and South African international cellphone format.
- Login is allowed only when the registration details pass validation.
- QuickChat menu with Send Messages, Show recently sent messages, and Quit options.
- Ten-digit message IDs, message numbering, message hash generation and message detail display.
- Send, disregard and store message choices.
- Stored messages are written to `messages.json` in the application's working directory.
- JUnit tests for IDs, recipient validation, message hashes, message length, send/disregard/store actions and JSON output.
- GitHub Actions runs the Maven tests on every push and pull request, regardless of feature-branch name.

## Run the project
Open the folder in NetBeans as a Maven project, then run `PROG5121_PoE_Part1.java`.

To run tests, use the NetBeans Test action or run `mvn test` from the project folder.

## GitHub feature branch
Keep `main` as the stable branch. Create a feature branch before making changes, for example:

```bash
git checkout -b Part2-Message-Feature
```

Commit and push the feature branch; GitHub Actions will run the tests automatically.

## Changelog
- Extended the Part 1 console application with the QuickChat menu and message-entry loop.
- Added the `Message` class, message ID generation, message hash creation and message actions.
- Added JSON message storage.
- Prevented login when registration validation fails, following Part 1 lecturer feedback.
- Added tests for message validation, exact example hash, additional hashes in a loop, message actions, JSON output, registration failures and login gating.
- Corrected message-length test feedback to include the exact number of excess characters.
- Made the GitHub Actions workflow run for all branches and pull requests.

## References
- Stack Overflow, “Validate South Africa cell phone number,” https://stackoverflow.com/questions/4058001/validate-south-africa-cell-phone-number (accessed 17 October 2026).
- The prescribed Programming 1A module material, PROG5121, Part 2 — Sending Messages, 2026.
