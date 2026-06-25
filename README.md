# 🥒 Picklement

**The Ultimate Gherkin AST to Excel Element Generator.**

Picklement is a high-performance, enterprise-grade CLI tool built with **Java 21** that parses Cucumber Gherkin (`.feature`) files using Abstract Syntax Tree (AST) analysis and transforms them into beautifully structured, multi-column Excel reports (`.xlsx`) in milliseconds.

Stop manually copy-pasting BDD scenarios into Test Management Tools. Let Picklement do the heavy lifting with zero memory overhead.

![Java](https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apache-maven&logoColor=white)
![License](https://img.shields.io/badge/License-Apache_2.0-blue.svg?style=for-the-badge)

---

## 🚀 Key Features

- ⚡ **Blazing Fast AST Parsing**: Uses `io.cucumber:gherkin` to read the exact syntax tree, avoiding fragile Regex-based text parsing.
- 🧠 **Reflection-Based Mapping**: Custom `@GherkinMap` annotation automatically routes AST data to POJOs with **O(1) cached lookups**.
- 🛡️ **Zero Memory Leaks (O(1) Footprint)**: Powered by **FastExcel** (`cn.idev.excel`), utilizing streaming APIs to generate massive Excel files without triggering `OutOfMemoryError`.
- ⚙️ **Externalized Configuration**: Dynamic Tag Routing and Step Keywords managed via `picklement.config.json` (No recompilation needed when business rules change!).
- 🏗️ **Hexagonal Architecture**: Strictly decoupled Core domain. The business logic knows *nothing* about Cucumber or Excel libraries.

---
