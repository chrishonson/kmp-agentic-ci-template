# 🌙 Night Shift Agent

> **An AI-powered autonomous coding assistant built for the unique constraints of mobile development.**

Most AI coding agents run in the cloud or sandboxed environments. **Night Shift Agent runs locally on your machine**—because mobile development demands it.

Mobile apps require platform SDKs (Android SDK, Xcode), emulators, proprietary build systems (Gradle, CocoaPods), and hardware-specific testing that simply can't run in a generic cloud container. This agent is designed to work *with* your local development environment, not around it.

---

## ⚡ Quick Start

```bash
# 1. Create your task list
echo "Add a dark mode toggle to the settings screen" > tasks.txt

# 2. Activate the Python environment
source .venv/bin/activate

# 3. Run the agent
python agent_gemini.py
```

The agent will:
1. Check for existing open PRs (and reuse the branch if one exists)
2. Create a feature branch (or continue on existing one)
3. Write the code to complete your task
4. Verify the build passes
5. Open a Pull Request (or update the existing one)
6. Monitor CI and attempt fixes if needed

📖 **Full documentation**: [docs/AGENT_README.md](./docs/AGENT_README.md)

---

## 🤖 How the Agent Works

```
┌─────────────────────────────────────────────────────────────────┐
│                     🌙 Night Shift Agent                        │
│                                                                 │
│   tasks.txt ──▶ Gemini AI ──▶ Code Changes ──▶ PR + CI Monitor  │
│                     │                                           │
│              ┌──────┴──────┐                                    │
│              │   4 Tools   │                                    │
│              ├─────────────┤                                    │
│              │ read_file   │                                    │
│              │ write_file  │                                    │
│              │ list_files  │                                    │
│              │ run_shell   │                                    │
│              └─────────────┘                                    │
└─────────────────────────────────────────────────────────────────┘
```

The agent operates in a loop:
1. **Check** for existing open PRs from previous runs
2. **Read** the next task from `tasks.txt`
3. **Understand** the codebase using `list_files` and `read_file`
4. **Write** code changes with `write_file`
5. **Verify** the build with `./gradlew assembleDebug detekt`
6. **Commit** when verification passes
7. **Repeat** until all tasks are complete
8. **Push** and create/update Pull Request
9. **Monitor** CI, auto-fixing failures when possible

### PR Continuity

If you stop the agent and restart it later, it will:
- **Find your existing open PR** from a `nightshift/` branch
- **Check out that branch** instead of creating a new one
- **Continue working** on the same PR
- **Read any feedback comments** you left on the PR

This means you can reject changes, leave comments, and restart—the agent will try again on the same PR.

---

## 📁 Repository Structure

```
.
├── agent_gemini.py          # 🌙 The autonomous agent (start here!)
├── tasks.txt                # Your task list for the agent
├── .env                     # API keys and configuration
│
├── docs/
│   ├── AGENT_README.md      # 📖 Full agent documentation
│   ├── ARCHITECTURE.md      # MVI architecture guide
│   └── RUNNER_SETUP.md      # Self-hosted runner setup
│
├── scripts/
│   ├── check_models.py      # List available Gemini models
│   └── setup-protection.sh  # Configure GitHub branch protection
│
├── composeApp/              # 📱 KMP shared code (agent test bed)
│   ├── commonMain/          # Shared business logic & UI
│   ├── androidMain/         # Android-specific code
│   └── iosMain/             # iOS-specific code
│
└── iosApp/                  # 📱 iOS native entry point
```

---

## 📱 The Test Application

The mobile app is a **Virtual Card** example built with:
- **Kotlin Multiplatform** (Android + iOS)
- **Compose Multiplatform** for shared UI
- **MVI Architecture** (Model-View-Intent)

It provides a realistic codebase for the agent to work with, including:
- ✅ Build verification (Gradle)
- ✅ Static analysis (Detekt)
- ✅ Unit tests
- ✅ UI tests (Android instrumented)
- ✅ CI pipeline (GitHub Actions)

### Build Commands

```bash
# Android
./gradlew :composeApp:assembleDebug

# iOS
# Open iosApp/ in Xcode
```

---

## 🔧 Setup

### Why Local?

Unlike web development, mobile development has hard dependencies on local tooling:

| Requirement | Why It Can't Be Cloud-Only |
|-------------|----------------------------|
| Android SDK | Proprietary, large (10+ GB), version-specific |
| Gradle | Long build times, local caching critical |
| Android Emulator | Requires hardware virtualization (KVM/HAXM) |
| Xcode | macOS-only, required for iOS builds |
| iOS Simulator | macOS-only, tied to Xcode version |

The agent runs on your development machine where these tools are already configured.

### Prerequisites
- **macOS** (required for iOS development and Android emulator)
- Android Studio with SDK and emulator configured
- Xcode (for iOS builds)
- Python 3.9+ with virtual environment
- [Gemini CLI](https://ai.google.dev/gemini-api/docs/cli) installed and authenticated
- GitHub account with repository access

### Environment Configuration

Create a `.env` file:
```bash
GH_BOT_TOKEN=ghp_xxxxxxxxxxxxxxxxxxxx  # GitHub PAT for the agent
BOT_USERNAME=agentnightshift           # Git commit author
AGENT_MODEL=gemini-3-flash-preview     # Gemini model to use
```

See [docs/AGENT_README.md](./docs/AGENT_README.md) for complete setup instructions including:
- GitHub bot account setup
- Branch protection configuration
- Self-hosted runner for UI tests

---

## 📚 Documentation

| Document | Description |
|----------|-------------|
| [**Agent README**](./docs/AGENT_README.md) | Complete agent setup and usage guide |
| [**Architecture**](./docs/ARCHITECTURE.md) | MVI pattern and code organization |
| [**Runner Setup**](./docs/RUNNER_SETUP.md) | Self-hosted GitHub Actions runner |

---

## 🧪 Why This Exists

This project explores **agentic CI/CD**—the idea that an AI agent can:
1. Receive high-level tasks
2. Autonomously implement them
3. Verify correctness through existing guardrails (builds, tests, linters)
4. Submit changes for human review via pull requests
5. **Iterate on feedback** by continuing work on rejected PRs

The mobile app provides a constrained, real-world environment where the agent must:
- Follow architectural patterns (MVI)
- Pass static analysis (Detekt)
- Maintain test coverage
- Work within protected file boundaries

---

*Built with [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html) and [Gemini AI](https://ai.google.dev/).*