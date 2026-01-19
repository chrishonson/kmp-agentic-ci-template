# KMP Agentic CI Template

A **Kotlin Multiplatform** mobile app template designed for use with autonomous AI coding agents.

This project serves as a real-world test bed with:
- ✅ Strict MVI Architecture
- ✅ Build verification (Gradle + Detekt)
- ✅ Unit tests
- ✅ UI tests (Android instrumented)
- ✅ CI pipeline (GitHub Actions)
- ✅ Branch protection

---

## 🤖 Use with Night Shift Agent

This template is designed to work with [Night Shift Agent](https://github.com/chrishonson/night-shift-agent)—an autonomous coding assistant.

```bash
# Clone both repos
git clone https://github.com/chrishonson/night-shift-agent.git
git clone https://github.com/chrishonson/kmp-agentic-ci-template.git

# Setup agent
cd night-shift-agent
python -m venv .venv
source .venv/bin/activate
pip install -r requirements.txt

# Create tasks in mobile project
echo "Add a logout button to the home screen" > ../kmp-agentic-ci-template/tasks.txt

# Run agent against mobile project
python agent_gemini.py --project-dir ../kmp-agentic-ci-template
```

---

## 📁 Project Structure

```
.
├── composeApp/              # KMP shared code
│   ├── commonMain/          # Shared business logic & UI
│   ├── androidMain/         # Android-specific code
│   └── iosMain/             # iOS-specific code
│
├── iosApp/                  # iOS native entry point
│
├── .github/
│   └── workflows/
│       └── pr-gateway.yml   # CI pipeline
│
├── docs/
│   ├── ARCHITECTURE.md      # MVI pattern documentation
│   └── RUNNER_SETUP.md      # Self-hosted runner guide
│
├── tasks.txt                # Agent tasks (gitignored)
└── .env                     # Agent config (gitignored)
```

---

## 🏗️ Architecture

This project follows **MVI (Model-View-Intent)** architecture. See [docs/ARCHITECTURE.md](./docs/ARCHITECTURE.md) for details.

### Core Components
- **State**: Immutable data class (`AppState`)
- **Intent**: Sealed interface for user actions (`AppIntent`)
- **Store**: ViewModel that processes intents (`AppStore`)
- **UI**: Pure Compose functions

---

## 🔧 Build & Run

### Android
```bash
./gradlew :composeApp:assembleDebug
```
Or use Android Studio's run configuration.

### iOS
Open `iosApp/` in Xcode and run.

---

## ✅ CI Pipeline

The GitHub Actions workflow (`.github/workflows/pr-gateway.yml`) runs two jobs on every PR:

1. **quality_gate** (GitHub-hosted)
   - Detekt static analysis
   - Build APK
   - Unit tests

2. **ui_verification** (Self-hosted Mac)
   - Android UI tests on emulator

See [docs/RUNNER_SETUP.md](./docs/RUNNER_SETUP.md) for self-hosted runner setup.

---

## 📚 Documentation

| Document | Description |
|----------|-------------|
| [Architecture](./docs/ARCHITECTURE.md) | MVI pattern and conventions |
| [Runner Setup](./docs/RUNNER_SETUP.md) | Self-hosted GitHub Actions runner |
| [Night Shift Agent](https://github.com/chrishonson/night-shift-agent) | Autonomous coding agent |

---

## 🧪 Why This Template?

This template demonstrates **agentic CI/CD**—using AI agents to:
1. Receive high-level tasks
2. Autonomously implement code
3. Verify through existing guardrails
4. Submit PRs for human review

The strict architecture and CI requirements force the agent to write production-quality code or fail trying.

---

*Built with [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html).*