# 📱 BMI Calculator

A minimalist Android app that calculates Body Mass Index (BMI) and classifies the result using the WHO adult categories. Built with Kotlin and Jetpack Compose.

## ✨ Features

- ⚡ **Instant BMI calculation** from weight (kg) and height (cm)
- 🏷️ **WHO classification**: Underweight, Healthy Weight, Overweight and Obesity
- 📊 **Visual result**: animated BMI value, color-coded category chip and a segmented scale with a position marker
- 🔢 **Forgiving input**: accepts both `.` and `,` as the decimal separator, and blocks invalid characters as you type
- ✅ **Input validation** with clear error feedback
- ⌨️ **Keyboard-friendly**: decimal keypad, Next/Done actions, and Done triggers the calculation
- 🌙 **Modern dark UI** with edge-to-edge layout that respects system bars and the keyboard

## 🧮 How it works

BMI is calculated as:

```
BMI = weight (kg) / height (m)²
```

The result is rounded to one decimal place and classified as follows:

| BMI (kg/m²)    | Category          |
|----------------|-------------------|
| Below 18.5     | 🔵 Underweight    |
| 18.5 – 24.9    | 🟢 Healthy Weight |
| 25.0 – 29.9    | 🟠 Overweight     |
| 30.0 and above | 🔴 Obesity        |

> ⚠️ **Disclaimer:** BMI is a general screening tool. It does not account for muscle mass, age, sex or body composition, and it is not a medical diagnosis. Consult a healthcare professional for personal health advice.

## 🛠️ Tech stack

- [Kotlin](https://kotlinlang.org/)
- [Jetpack Compose](https://developer.android.com/jetpack/compose) with [Material 3](https://m3.material.io/)
- Android Gradle Plugin 9.0.0 with Gradle Kotlin DSL (`.kts`)
- Gradle 9.1.0+, JDK 17 and `compileSdk` 36
- `androidx.core:core-ktx` 1.18.0 and `androidx.lifecycle` 2.10.0 (newer releases require AGP 9.1+ and `compileSdk` 37)

## 🚀 Getting started

### 📋 Prerequisites

- A recent version of [Android Studio](https://developer.android.com/studio) that supports AGP 9.0
- JDK 17 (bundled with Android Studio)
- Android SDK Platform 36, installed via **Tools → SDK Manager**

### ▶️ Run the app

1. Clone the repository:

   ```bash
   git clone https://github.com/guilhermeoliveirateo/BodyMassIndexCalculator
   ```

2. Open the project folder in Android Studio and wait for the Gradle sync to finish.
3. Select an emulator or a connected device and click **Run ▶**.

To build a debug APK from the command line:

```bash
./gradlew assembleDebug
```

On Windows, use `gradlew.bat assembleDebug`. The APK is generated in `app/build/outputs/apk/debug/`.

> 💡 **Tip:** keep the project outside synced folders such as OneDrive. Syncing can lock files in `app/build` and cause Gradle build errors.

## 🩺 Troubleshooting

| Problem | Fix |
|---------|-----|
| **"Incompatible version of the Android Gradle plugin"** | Your Android Studio is older than the AGP used by the project. Update Android Studio, or set `agp = "9.0.0"` in `gradle/libs.versions.toml`. |
| **Warning about `compileSdk` 37** | Set `compileSdk = 36` in `app/build.gradle.kts`, or update Android Studio and AGP. |
| **AAR metadata errors (dependency requires AGP 9.1+ / `compileSdk` 37)** | Keep `core-ktx` at 1.18.0 and `lifecycle` at 2.10.0 in `gradle/libs.versions.toml`, or update Android Studio and AGP together. |
| **"Unable to delete directory" in `app/build`** | Run `./gradlew --stop`, pause OneDrive, delete `app/build` and rebuild. Better: keep the project outside a synced folder. |
| **`Unresolved reference` on `android.os.*` in the IDE** | The Gradle sync failed. Check the **Build → Sync** tab, install the matching SDK platform and sync again. |

## 📂 Project structure

```
BodyMassIndexCalculator/
├── app/
│   └── src/main/java/br/com/BodyMassIndexCalculator/
│       ├── MainActivity.kt      # UI (Compose) and BMI logic
│       └── ui/theme/            # Theme files
├── gradle/                      # Gradle wrapper and version catalog
├── build.gradle.kts
├── settings.gradle.kts
└── gradle.properties
```

## 🎨 Design

The interface is dark and minimal: a single solid deep ink-blue background (no gradients) with one mint accent used for focus, the primary action and the "healthy" range. The BMI value uses a monospace font, and category colors (blue, mint, amber, red) are used consistently across the chip and the scale.