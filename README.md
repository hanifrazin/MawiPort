# 🦎 MawiPort
**The Ultimate Gherkin to Excel Converter**

MawiPort is a simple and powerful command-line interface (CLI) tool that instantly converts your Cucumber Gherkin (`.feature`) files into beautifully formatted Excel (`.xlsx`) reports. It's designed for Quality Assurance teams, Business Analysts, and anyone who needs to bridge the gap between technical test scenarios and business-friendly documentation.

## ✨ Features
- **Effortless Conversion**: Turn any `.feature` file into an `.xlsx` spreadsheet with a single command.
- **Global Access**: After setup, you can run the `mawi` command from any directory in your terminal.
- **Cross-Platform**: Works seamlessly on Windows, macOS, and Linux.
- **Zero Dependencies (for Native version)**: The pre-built executable requires no other installations (not even Java!).
- **Easy to Share**: Once built, the entire tool can be zipped and shared with teammates.

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
    mawi --version
    ```

> 💡 **Pro Tip for Distribution:**
> Want to share this tool with your team without asking them to run a Maven build? Just **zip the entire `target/release/` folder** and send them the `.zip` file. They can extract it and run the `install.bat` or `install.sh` script directly.

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
