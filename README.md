# Astra Android App

Astra is an Android application built using the MVVM (Model-View-ViewModel) architecture. The app demonstrates CRUD (Create, Read, Update, Delete) operations on a list of posts fetched from an API. It uses Dagger 2 for Dependency Injection, Retrofit for network communication, and various other libraries to enhance functionality and user experience.

## Project Structure

The project follows a clean and modular approach:

- **UI Layer**: Contains Activities and Adapters. Activities handle the user interface logic, displaying data and responding to user actions. The main activity is `PostDetailsActivity`.
- **ViewModel Layer**: Contains ViewModels, which serve as a communication bridge between the UI and the data layers. The ViewModels fetch data from the repository, manage UI-related data, and provide it to the Activities.
- **Data Layer**: Consists of Repositories and Models. The Repositories handle data operations and make API calls using Retrofit, while Models represent the data structure.
- **Dependency Injection**: Dagger 2 is used to inject dependencies, making the code more modular, testable, and easy to maintain.

## Why We Use MVVM

MVVM (Model-View-ViewModel) architecture separates the development of the UI from the business logic, which provides several benefits:

1. **Separation of Concerns**: MVVM separates UI logic from business logic, making the app modular and easy to manage.
2. **Maintainability**: It allows for easier code maintenance and testing because the components are loosely coupled.
3. **Reusability**: ViewModels can be reused across different Activities or Fragments, reducing code duplication.
4. **Better Lifecycle Management**: ViewModels are lifecycle-aware, which means they survive configuration changes (like screen rotation), and handle UI-related data more effectively.

## External Packages and Why We Use Them

### 1. Dagger 2
- **Why**: Dagger 2 is used for Dependency Injection (DI). It allows you to manage dependencies across the app in a modular and scalable way, making the code more testable and maintainable.

### 2. AndroidX Lifecycle
- **Why**: Lifecycle components like ViewModel and LiveData help manage UI-related data lifecycle-consciously, reducing the chances of memory leaks and improving performance.

### 3. Retrofit
- **Why**: Retrofit simplifies making HTTP requests to REST APIs. The `converter-gson` helps in parsing JSON responses into Kotlin objects easily.

### 4. Coroutines
- **Why**: Coroutines are used for performing tasks asynchronously (like network requests) without blocking the main thread, improving app responsiveness.

### 5. OkHttp
- **Why**: OkHttp's logging interceptor helps in debugging network requests by logging request and response details, making it easier to track errors.

### 6. Glide
- **Why**: Glide is an image loading library that helps load images from URLs into ImageViews efficiently, with caching and placeholder support.

### 7. Lottie
- **Why**: Lottie allows you to add smooth and attractive animations using JSON files. It makes it easy to integrate animations without creating them manually.

### 8. CircleImageView
- **Why**: CircleImageView simplifies creating circular profile pictures or avatars, providing an easy way to implement rounded images.

### 9. AndroidX Libraries
- **Why**: These libraries are essential for building modern Android apps. `AppCompat` provides backward compatibility, and `ConstraintLayout` helps in building flexible and complex UI layouts.

## Conclusion

This README provides a basic understanding of how the Astra app is structured and why we chose to use specific libraries and architecture. By following MVVM and using these external packages, the app is efficient, maintainable, and scalable for future enhancements.

---
