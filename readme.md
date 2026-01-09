# vanillaJAVA

Run Selenium tests with the BrowserStack Java SDK as a Java agent.

## Setup
- Ensure dependencies are installed:
```bash
mvn -U clean install
```

## Run tests
```bash
export BROWSERSTACK_BUILD_RUN_IDENTIFIER=run1 && mvn clean test-compile exec:exec@run-bstackdemo exec:exec@run-google
```

   