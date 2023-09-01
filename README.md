# Nike Store Android Application README

This README file provides an overview of the Nike Store Android Application, outlining its key features, architecture, and the technologies used in its development.

## Table of Contents
- [Introduction](#introduction)
- [Features](#features)
- [Technologies Used](#technologies-used)
- [Getting Started](#getting-started)
- [Project Structure](#project-structure)
- [Images](#images)

## Introduction

The Nike Store Android Application is a native Kotlin application that allows users to explore and shop for Nike products. It utilizes modern Android development practices and various technologies to deliver a seamless shopping experience.

## Features

1. **Firebase Integration**: Firebase is used for authentication, real-time database, and cloud functions, ensuring a secure and dynamic user experience.

2. **Coroutines**: We leverage Kotlin Coroutines for managing asynchronous operations efficiently, making the application responsive and performant.

3. **SuspendCoroutine**: The `suspendCoroutine` function is used to convert callback-based APIs into coroutine-friendly ones, simplifying asynchronous code.

4. **Kotlin Flows**: Kotlin Flows are employed to handle streams of data in a reactive manner, providing a smooth and reactive UI.

5. **Clean Architecture**: The application follows Clean Architecture principles, ensuring separation of concerns, maintainability, and testability.

6. **Multi-Module Project**: The project is structured into multiple modules, promoting modularity, code reusability, and better organization.

7. **MVVM (Model-View-ViewModel)**: We adopt the MVVM architectural pattern to create a clear separation between UI, business logic, and data operations.

8. **Jetpack Compose**: The user interface is built using Jetpack Compose, enabling a declarative and modern approach to UI development.

9. **Kotlin**: The application is written entirely in Kotlin, benefiting from its concise syntax, null safety, and modern language features.

10. **Android DataStore**: DataStore is used for efficient data persistence, offering a convenient way to store and manage user settings and preferences.

11. **Room Database**: Room is employed as a local database solution to store and manage user-related data offline.

12. **Dagger Hilt** : To provide instances and creation of objects.

## Technologies Used

- Kotlin
- Android Jetpack (Compose, Navigation, ViewModel, LiveData)
- Firebase (Authentication, Realtime Database, Cloud Functions)
- Kotlin Coroutines
- Kotlin Flow
- Clean Architecture
- Multi-Module Project
- Android DataStore
- Room Database
- Dagger Hilt

## Getting Started

To get started with the Nike Store Android Application, follow these steps:

1. Clone this repository to your local machine:

   ```bash
   git clone https://github.com/AhmedMaherHosny/NikeStore
   ```

2. Open the project in Android Studio.

3. Build and run the application on an Android emulator or physical device.

## Project Structure

The project is structured into multiple modules for better organization and modularity:

- **app**: The main application module that contains the all dagger hilt dependency injection.
- **domain**: The domain module contains the core business logic and domain models.
- **data (remote - local)**: The data module handles data retrieval, storage, and interaction with external APIs and databases.
- **presentation**: This module is responsible for UI presentation logic and state management and viewmodels.
- **Core**: Contains shared utility code and resources used across the application.
- **ui**: Contains all composable function and screens.

## Images
![image Alt](https://github.com/AhmedMaherHosny/NikeStore/blob/master/imgs/1.jpeg)
![image Alt](https://github.com/AhmedMaherHosny/NikeStore/blob/master/imgs/2.jpeg)
![image Alt](https://github.com/AhmedMaherHosny/NikeStore/blob/master/imgs/3.jpeg)
![image Alt](https://github.com/AhmedMaherHosny/NikeStore/blob/master/imgs/4.jpeg)
![image Alt](https://github.com/AhmedMaherHosny/NikeStore/blob/master/imgs/5.jpeg)
![image Alt](https://github.com/AhmedMaherHosny/NikeStore/blob/master/imgs/6.jpeg)
