# Project Architecture: MVI (Model-View-Intent) with Action Creators

This project follows a strict MVI architecture with a unidirectional data flow:

```
UI → Intent → ActionCreator → Action → Reducer → State → UI
```

## Core Components

### 1. State (Model)
- **Definition**: An immutable data class representing the single source of truth for the screen.
- **Naming**: `[ScreenName]State` (e.g., `StartupState`).
- **Usage**: Must be a `data class`. All properties must be immutable (`val`).

### 2. Intent
- **Definition**: A sealed interface representing all possible **user-facing** actions or events from the UI.
- **Naming**: `[ScreenName]Intent` (e.g., `StartupIntent`).
- **Usage**: Use `data object` for no-arg intents and `data class` for intents with parameters.
- **Rule**: Intents describe **what the user did**, not what should happen. They carry no business logic.

### 3. Action
- **Definition**: A sealed interface representing **internal** state-change instructions processed by the Reducer.
- **Naming**: `[ScreenName]Action` (e.g., `StartupAction`).
- **Usage**: Actions are the output of Action Creators. They describe **what happened** (e.g., loading started, data loaded, error occurred).
- **Rule**: Actions must contain all the data the Reducer needs. The Reducer should never need to fetch or compute additional data.

### 4. Reducer
- **Definition**: A **pure function** that takes the current State and an Action, and returns a new State.
- **Naming**: `[screenName]Reducer` (e.g., `startupReducer`).
- **Signature**: `fun reducer(state: State, action: Action): State`
- **Rules**:
    - **No side effects**. No network calls, no logging, no I/O. Pure input → output.
    - **No coroutines**. Reducers are synchronous.
    - Must be exhaustive over all Action types (`when` expression).
- **Location**: Defined as a top-level function in the Contract file alongside State, Intent, and Action.

### 5. Action Creator
- **Definition**: A class that handles **side effects** (network calls, delays, database access) and translates Intents into Actions dispatched to the Reducer.
- **Naming**: `[ScreenName]ActionCreator` (e.g., `StartupActionCreator`).
- **Responsibilities**:
    - Receive an Intent and a `dispatch: (Action) -> Unit` callback.
    - Perform async work (if needed) and dispatch the appropriate Actions.
    - Handle errors and dispatch error Actions (never throw to the Store).
    - Always rethrow `CancellationException` to preserve coroutine cancellation.
- **Rules**:
    - Use `suspend fun handleIntent(intent, dispatch)` when async work is involved.
    - Use regular `fun handleIntent(intent, dispatch)` when all operations are synchronous.
    - Dependencies (services, repositories) are injected via constructor.

### 6. Store (ViewModel)
- **Definition**: A thin `ViewModel` that wires the Intent → ActionCreator → Reducer → State pipeline.
- **Naming**: `[ScreenName]Store` (e.g., `StartupStore`).
- **Responsibilities**:
    - Expose `val state: StateFlow<State>`.
    - Expose `fun dispatch(intent: Intent)` for the UI.
    - Delegate to the ActionCreator and apply results through the Reducer.
- **Rules**:
    - **No business logic**. The Store is glue only.
    - **No direct state mutations**. All state changes go through the Reducer.
    - Use `viewModelScope.launch` to call suspend ActionCreator methods.

### 7. UI (View)
- **Definition**: Jetpack Compose functions.
- **Container**: The "Screen" composable (e.g., `StartupScreen`) holds the Store, collects state, and dispatches intents.
- **Component**: The "Content" composable (e.g., `StartupContent`) must be **Pure**. It takes raw data (Strings, Ints, Booleans) as arguments, NOT the State object or the Store.

## File Organization

Each feature has the following files:

| File | Contains |
|------|----------|
| `[Feature]Contract.kt` | Intent, Action, State, Reducer |
| `[Feature]ActionCreator.kt` | ActionCreator class |
| `[Feature]Store.kt` | Store (ViewModel) |
| `[Feature]Screen.kt` | UI composables |

## Coroutine Exception Handling

Action Creators that perform async work **must** follow this pattern:

```kotlin
try {
    // async work
    dispatch(SomeAction.Success(result))
} catch (e: CancellationException) {
    throw e  // Never swallow — preserves coroutine cancellation
} catch (e: Exception) {
    dispatch(SomeAction.Error(e.message ?: "Unknown error"))
}
```

## Testing Guidelines

### Reducer Tests (Pure, no coroutine infrastructure)
Test the Reducer as a pure function. No test dispatchers, no `runTest`, no mocks needed:
```kotlin
@Test
fun loadingStartedSetsIsLoading() {
    val state = StartupState()
    val result = startupReducer(state, StartupAction.LoadingStarted)
    assertTrue(result.isLoading)
}
```

### Action Creator Tests (Lightweight, mock dispatch)
Test the ActionCreator by injecting a mock service and capturing dispatched Actions:
```kotlin
@Test
fun initializeDispatchesLoadingThenSuccess() = runTest {
    val actions = mutableListOf<StartupAction>()
    val creator = StartupActionCreator(FakePostService())
    creator.handleIntent(StartupIntent.Initialize) { actions.add(it) }
    assertEquals(StartupAction.LoadingStarted, actions[0])
    assertIs<StartupAction.LoadingSucceeded>(actions[1])
}
```

### Integration Tests (Store end-to-end)
Test the full pipeline using `kotlinx-coroutines-test`:
```kotlin
@Test
fun testInitializeIntent() = runTest {
    val store = StartupStore(StartupActionCreator(mockPostService))
    store.dispatch(StartupIntent.Initialize)
    advanceUntilIdle()
    assertTrue(store.state.value.isCompleted)
}
```

### UI Tests
Use Compose test rules to test the integration of the UI with raw data inputs.
