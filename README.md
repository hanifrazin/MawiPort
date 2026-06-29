# <img src="logo.png" width="20" valign="middle" alt="MawiPort Logo"> MawiPort
**The Ultimate Gherkin to Excel Converter**

MawiPort is a simple and powerful command-line interface (CLI) tool that instantly converts your Cucumber Gherkin (`.feature`) files into beautifully formatted Excel (`.xlsx`) reports. It's designed for Quality Assurance teams, Business Analysts, and anyone who needs to bridge the gap between technical test scenarios and business-friendly documentation.

## ✨ Features
- **Effortless Conversion**: Turn any `.feature` file into an `.xlsx` spreadsheet with a single command.
- **Global Access**: After setup, you can run the `mawi` command from any directory in your terminal.
- **Cross-Platform**: Works seamlessly on Windows, macOS, and Linux.
- **Zero Dependencies (for Native version)**: The pre-built executable requires no other installations (not even Java!).
- **Easy to Share**: Once built, the entire tool can be zipped and shared with teammates.
- **Smart Tag Inheritance**: Feature-level tags are automatically inherited by all scenarios, with scenario-level tags overriding when needed.
- **Comprehensive Tag Support**: Supports priority, severity, type, and custom tags with intelligent categorization.

## 📋 Prerequisites
Your system requirements depend on which version of MawiPort you intend to use after building it.

- **For the Native Executable (`mawi.exe` or `mawi` binary)**
  - No dependencies required! It's a self-contained executable.
- **For the JAR / Script-based version (`mawi.jar`)**
  - You will need **Java 21 or higher** installed on your system.

To build the project, you will also need **Maven** and **Git**.

## 📦 Installation & Setup
Since the project does not yet have a public download page, you'll first need to build the release package from the source code. It's a simple two-step process.

### Step 1: Build the Release Package (Generate the Files)
First, we'll use Maven to compile the code and generate all the necessary files (the JAR, scripts, and installers).

1.  **Clone the repository** or ensure you are in the project's root directory.
    ```bash
    git clone https://github.com/your-username/MawiPort.git
    cd MawiPort
    ```
2.  **Run the Maven build command**:
    ```bash
    mvn clean package
    ```
3.  **Check the output**: Maven will automatically create a `target/release/` directory. This folder contains everything you need to run and install MawiPort.

### Step 2: Install Globally (The "Zero-Config" Magic)
Now that the release files are generated, you can install the `mawi` command to be accessible from anywhere on your system.

1.  **Navigate into the generated release directory**:
    ```bash
    cd target/release/
    ```
2.  **Run the installer for your Operating System**:
    -   **On Windows**: Double-click the `install.bat` file or run it from your command prompt.
    -   **On macOS / Linux**: Run the shell script.
        ```bash
        ./install.sh
        ```
3.  **Restart Your Terminal**: This is a crucial step! Close and reopen your Command Prompt, PowerShell, or Terminal window for the changes to take effect.
4.  **Verify the installation**: You're all set! Check that the command is working by typing:
    ```bash
    mawi
    ```

> 💡 **Pro Tip for Distribution:**
> Want to share this tool with your team without asking them to run a Maven build? Just **zip the entire `target/release/` folder** and send them the `.zip` file. They can extract it and run the `install.bat` or `install.sh` script directly.

### Alternative: Manual Setup (Advanced / Fallback)
If you prefer not to use the `install.sh` / `install.bat` script, or if it fails due to system permissions, you can manually register MawiPort to your system's PATH. This is exactly what the installer scripts do behind the scenes.

**For macOS / Linux Users:**
1. **Get your absolute path**: Navigate to your extracted `target/release` folder and type `pwd`. Copy the output (e.g., `/Users/username/IdeaProjects/MawiPort/target/release`).
2. **Open your shell configuration file** using the `nano` text editor:
   ```bash
   nano ~/.zshrc   # For macOS (Zsh)
   # OR
   nano ~/.bashrc  # For Linux (Bash)
   ```
3. **Add the path variable**: Scroll to the bottom of the file and add the following line (replace the example path with the one you copied in step 1):
   ```bash
   export PATH="$PATH:/Users/username/IdeaProjects/MawiPort/target/release"
   ```
   *Press `Ctrl+O` then `Enter` to save, and `Ctrl+X` to exit nano.*
4. **Apply the changes**: Refresh your current terminal session:
   ```bash
   source ~/.zshrc   # For Zsh
   # OR
   source ~/.bashrc  # For Bash
   ```
5. **Verify permissions**: Check if the scripts are executable:
   ```bash
   ls -l /Users/username/IdeaProjects/MawiPort/target/release/mawi
   ls -l /Users/username/IdeaProjects/MawiPort/target/release/install.sh
   ```
   If they lack execution permissions (indicated by `-rw-r--r--`), run:
   ```bash
   chmod +x /Users/username/IdeaProjects/MawiPort/target/release/mawi
   chmod +x /Users/username/IdeaProjects/MawiPort/target/release/install.sh
   ```

**For Windows Users:**
1. Open the Start Search, type in **"env"**, and select **"Edit the system environment variables"**.
2. Click the **"Environment Variables..."** button.
3. Under **"User variables"**, select the **"Path"** variable and click **"Edit..."**.
4. Click **"New"** and paste the absolute path to your `target\release` folder (e.g., `C:\Users\username\IdeaProjects\MawiPort\target\release`).
5. Click **"OK"** on all windows to save.
6. Restart your terminal (Command Prompt or PowerShell).

## 🚀 Quick Start & Usage
Using MawiPort is incredibly straightforward. The basic command requires an input file (`-i`) and an output file (`-o`).

```bash
# Basic command structure
mawi -i <path/to/input.feature> -o <path/to/output.xlsx>
```

### Examples:

**1. Converting a file in the same directory:**
```bash
mawi -i login-scenario.feature -o test-case-report.xlsx
```

**2. Specifying paths in different folders:**
```bash
mawi -i ./gherkin-files/checkout.feature -o ./excel-reports/TC_Checkout.xlsx
```

## 🏷️ Tag Inheritance System
MawiPort features a powerful tag inheritance system that makes test organization effortless:

### How It Works
- **Feature-Level Tags**: Tags placed on the Feature line are inherited by all scenarios
- **Scenario-Level Overrides**: Scenarios can override specific tags while inheriting others
- **Smart Categorization**: Tags are automatically categorized into Priority, Severity, Type, and Custom tags

### Examples

**Basic Inheritance**
```gherkin
@P1 @S2 @WEB @Smoke
Feature: Login Module
  # All scenarios inherit: P1 priority, S2 severity, WEB type, Smoke tag

  Scenario: Successful login
    Given the user is on the login page
    When the user enters valid credentials
    Then the dashboard should be displayed
    # Result: Priority=P1, Severity=S2, Type=WEB, TAG_1=Smoke
```

**Partial Override**
```gherkin
@P1 @S2 @WEB @Smoke
Feature: Login Module

  @P0 @Regression  # Overrides priority to P0, adds Regression tag
  Scenario: Critical login test
    Given the system is under heavy load
    When the user attempts to login
    Then the login should still succeed
    # Result: Priority=P0 (overridden), Severity=S2 (inherited)
    #         Type=WEB (inherited), TAG_1=Regression, TAG_2=P0
```

**Type Override Only**
```gherkin
@P1 @S2 @WEB @Smoke
Feature: Login Module

  @API  # Only overrides the type
  Scenario: API login test
    Given the API endpoint is available
    When a POST request is sent
    Then the response should be 200
    # Result: Priority=P1 (inherited), Severity=S2 (inherited)
    #         Type=API (overridden), TAG_1=Smoke (inherited)
```

### Supported Tag Categories

**Priority Tags**: P0, P1, P2, P3, HIGH, MEDIUM, LOW
**Severity Tags**: S1, S2, S3, S4, CRITICAL, MAJOR, MODERATE, LOW  
**Type Tags**: API, WEB, MOBILE, UI, BACKEND
**Custom Tags**: Any other tags become TAG_1, TAG_2, etc.

### Configuration
Customize tag categories in `mawiport.config.json`:
```json
{
  "tagRouting": {
    "typeKeywords": ["API", "WEB", "MOBILE", "UI", "BACKEND"],
    "priorityKeywords": ["P0", "P1", "P2", "P3", "HIGH", "MEDIUM", "LOW"],
    "severityKeywords": ["S1", "S2", "S3", "S4", "CRITICAL", "MAJOR", "MODERATE", "LOW"]
  }
}
```

## 🛠️ Troubleshooting & FAQ

### 🏗️ Build & Project Issues (For Developers)
If you encounter problems during the `mvn clean package` step or the `target/release/` folder is not generated, check these common issues:

**Problem 1: `mvn: command not found`**
- **Cause:** Apache Maven is not installed or not added to your system's PATH.
- **Solution:** Download and install Maven from [maven.apache.org](https://maven.apache.org/), or use a package manager like `brew install maven` (macOS) or `choco install maven` (Windows).

**Problem 2: `java: command not found` or `Unsupported class file major version`**
- **Cause:** Java is not installed, or you are using an older version (Java 8, 11, or 17). MawiPort requires **Java 21 or higher**.
- **Solution:** 
  - Check your version: `java --version`
  - If it's below 21, download JDK 21 from [Adoptium](https://adoptium.net/) or install via `brew install openjdk@21`.
  - Ensure your `JAVA_HOME` environment variable points to the JDK 21 installation.

**Problem 3: `target/release/` folder is missing after `mvn clean package`**
- **Cause:** The Maven build failed silently, or the `maven-antrun-plugin` did not execute properly.
- **Solution:** 
  - Scroll up in your terminal and look for `[ERROR]` messages.
  - Ensure your `pom.xml` contains the `maven-antrun-plugin` configuration.
  - Try running `mvn clean package -X` (debug mode) to see detailed logs.

**Problem 4: `Compilation failure: invalid target release: 21`**
- **Cause:** Your IDE or terminal is using a different JDK version than the one required by the project.
- **Solution:** In IntelliJ, go to `File > Project Structure > Project SDK` and ensure it is set to JDK 21. Also check `Settings > Build > Compiler > Java Compiler` to ensure the target bytecode version is 21.

---

### 🍎 For macOS / Linux Users
**Problem:** You get the error `zsh: command not found: mawi` or `bash: ./mawi: Permission denied` even though you ran `install.sh` or are inside the release folder.

**Solution:** You need to grant execution permissions to the scripts. Run this command in your terminal:
```bash
chmod +x mawi
chmod +x install.sh
