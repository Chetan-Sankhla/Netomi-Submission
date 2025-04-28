# 📱 **Chat Application**

A simple real-time chat application built using **Jetpack Compose**, **MVVM**, and **Clean Architecture**. This project uses **PieSocket** for WebSocket communication and supports offline message queuing with automatic retry.

---

### 🌟 **Features**

- Real-time chat with WebSocket (PieSocket)
- MVVM with Clean Architecture
- Offline message queuing with retry on reconnection
- Unread message count tracking
- Shared `ViewModel` between Chat List and Chat Detail screens
- Error handling (network/API)
- Connectivity status monitoring
- UI built using **Jetpack Compose**
- Material 3 design with theming
- WhatsApp-like chat list & detail screen

---

### 📸 **Screenshots**

| Chat List (Home) | Chat Detail |
| :--------------: | :---------: |
|  |


---

### 🛠️ **Tech Stack**

- **Kotlin**
- **Jetpack Compose**
- **MVVM**
- **Clean Architecture**
- **Hilt (Dependency Injection)**
- **PieSocket SDK** (WebSocket)
- **Coroutines / Flow**
- **Material 3**

---

### ⚙️ **Setup Instructions**

1. **Clone the Repository:**

```bash
git clone https://github.com/Chetan-Sankhla/Netomi-Submission.git
cd your-repo-name
```

2. **Add API keys:**

Create a `local.properties` file in the root directory with the following content:

```properties
API_KEY="your_api_key_here"
CLUSTER_ID="your_cluster_id_here"
```

> **Note:**  
> Ensure that `local.properties` is added to your `.gitignore` file to prevent sensitive data from being pushed.

3. **Sync the Project:**

```bash
./gradlew clean build
```

4. **Run the App:**

Run the app on your emulator or connected device through Android Studio or via:

```bash
./gradlew installDebug
```

---

### 📂 **Key Functionalities**

- **ChatHomeScreen**: Displays the list of chat rooms with unread message counts.
- **ChatDetailScreen**: Displays messages for a selected chat room with real-time updates.
- **SocketManager**: Handles WebSocket initialization, reconnection logic, and message sending/receiving.
- **NetworkObserver**: Monitors connectivity changes to retry queued messages when online.
- **Repository**: Acts as a single source of truth (SSOT) for chat rooms and message handling.

---

### 🙌 **Acknowledgments**

- [PieSocket](https://www.piesocket.com/)
- [Jetpack Compose](https://developer.android.com/jetpack/compose)
- [Hilt](https://developer.android.com/training/dependency-injection/hilt-android)

### ⚠️ **Disclaimer**

This project is for learning purposes only. Make sure to secure your API keys and sensitive data in production environments.
