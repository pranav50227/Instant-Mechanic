# 🚗 InstantMechanic

An Android application that enables users to discover nearby mechanics, request roadside services, manage bookings, and communicate with garages through a modern and intuitive user interface.

---

# 📹 Demo Video

<div align="center">
  <video src="media/instantmechanix.mp4" width="320" height="640" controls></video>
</div>

---

# 📱 Application Screens

| Splash | Login |
|---------|-------|
| ![](app/src/main/res/drawable/splash.png) | ![](app/src/main/res/drawable/login.png) |

| Signup | Home |
|---------|------|
| ![](app/src/main/res/drawable/signup.png) | ![](app/src/main/res/drawable/home.png) |

| Garage Details | Request Service |
|----------------|----------------|
| ![](app/src/main/res/drawable/requestbooking.png) | ![](app/src/main/res/drawable/bookservice.png) |

| Bookings | Booking Details |
|-----------|----------------|
| ![](app/src/main/res/drawable/upcoming.png) | ![](app/src/main/res/drawable/bookingdetail.png) |

| Messages | Profile |
|-----------|----------|
| ![](app/src/main/res/drawable/message.png) | ![](app/src/main/res/drawable/profile.png) |

---

# 📖 About the Project

InstantMechanic is a modern Android application developed using **Kotlin** and **MVVM Architecture**. The application allows users to browse nearby mechanics, explore garage details, request vehicle servicing, manage service bookings, and communicate with mechanics from a unified interface.

The project follows Android development best practices with proper separation of concerns using MVVM, Repository Pattern, Hilt Dependency Injection, StateFlow, and Material Design components.

---

# ✨ Features

## Authentication

- Splash Screen
- Login
- Signup
- Session Management

---

## Home

- View nearby mechanics
- Search mechanics
- Filter mechanics
- Garage ratings
- Distance
- Available services
- Open / Closed status

---

## Garage Details

- Garage information
- Rating
- Address
- Working Hours
- Contact Number
- Services Offered
- Request Service

---

## Request Service

- Vehicle Number
- Service Selection
- Appointment Date
- Appointment Time
- Problem Description
- Form Validation

---

## Bookings

- Upcoming Bookings
- Booking History
- Booking Status
- Cancel Booking
- Reschedule Booking

---

## Booking Details

- Booking Summary
- Garage Details
- Vehicle Details
- Appointment Details
- Estimated Cost
- Problem Description

---

## Messages

- Conversation List
- Latest Messages
- Unread Indicators

---

## Profile

- User Information
- Vehicles
- Settings
- Logout

---

# 🏗️ Project Architecture

The application follows **MVVM (Model–View–ViewModel)** architecture.

```
UI
│
▼
ViewModel
│
▼
Repository
│
├────────── Remote Data Source
│
└────────── Local Data Source
│
▼
Repository
│
▼
ViewModel
│
▼
UIState
│
▼
UI Rendering
```

---

## Why MVVM?

The MVVM architecture separates responsibilities into independent layers.

### UI Layer

Responsible for

- Rendering data
- User interaction
- Observing UIState

The UI never directly communicates with APIs or databases.

---

### ViewModel

Responsible for

- Managing UI State
- Handling User Events
- Calling Repository Methods
- Processing Business Logic
- Surviving Configuration Changes

---

### Repository

Acts as the **Single Source of Truth**.

Responsible for

- API Calls
- Local Storage
- Data Mapping
- Returning Results

---

### Data Layer

Responsible for

- Retrofit
- Mock Data
- Future Database Support

---

# 📂 Project Structure

```
com.instantmechanic

├── data
│   ├── api
│   ├── local
│   ├── model
│   ├── repository
│   └── mock
│
├── di
│
├── ui
│   ├── splash
│   ├── login
│   ├── signup
│   ├── home
│   ├── mechanic
│   ├── request
│   ├── booking
│   ├── messages
│   └── profile
│
├── viewmodel
│
├── navigation
│
├── utils
│
└── InstantMechanicApplication
```

---

# 🧱 Technology Stack

| Category | Technology |
|-----------|------------|
| Language | Kotlin |
| Architecture | MVVM |
| Dependency Injection | Hilt |
| Networking | Retrofit |
| Asynchronous Programming | Kotlin Coroutines |
| State Management | StateFlow |
| Image Loading | Coil |
| UI | XML / Material Design 3 |
| Navigation | Navigation Component |
| IDE | Android Studio |

---

# 🧩 Dependency Injection

The application uses **Hilt** for Dependency Injection.

Dependency Graph

```
Application

↓

Hilt

↓

Network Module

↓

Retrofit

↓

ApiService

↓

Repositories

↓

ViewModels

↓

Activities / Fragments
```

Hilt automatically provides

- Retrofit
- ApiService
- Repositories
- SharedPreferences
- ViewModels

without manual object creation.

---

# 📡 Data Flow

```
User Interaction

↓

Activity / Fragment

↓

ViewModel

↓

Repository

↓

API / Mock Data

↓

Repository

↓

ViewModel

↓

UiState

↓

UI
```

---

# 🎯 UI State Management

Every screen follows a unified UI state.

```
UiState

├── Idle

├── Loading

├── Success

├── Error

└── Empty
```

Each ViewModel exposes StateFlow of UiState.

The UI observes the state and renders the appropriate screen.

---

# 📱 Application Screens

## Splash

- Application Branding
- Session Validation
- Navigation

---

## Login

- Email Authentication
- Password Validation

---

## Signup

- User Registration
- Form Validation

---

## Home

- Mechanics List
- Search
- Filter

---

## Garage Details

- Mechanic Details
- Available Services
- Working Hours

---

## Request Service

- Booking Form
- Vehicle Details
- Service Selection

---

## My Bookings

- Booking History
- Upcoming Services

---

## Booking Details

- Service Information
- Appointment Details

---

## Messages

- Chat List
- Latest Conversation

---

## Profile

- User Information
- Vehicles
- Settings

---

# 📦 Data Models

The application contains the following models.

- User
- Mechanic
- Service
- Vehicle
- WorkingHours
- Booking
- ServiceRequest
- Chat
- Message
- Notification
- PaymentMethod

---

# 📡 API / Data Details

The application currently uses **Mock Data**.

The repository layer is structured so that Retrofit APIs can replace mock data without changing the ViewModels.

### Planned Endpoints

```
GET

/mechanics
```

```
GET

/mechanics/{id}
```

```
POST

/request
```

```
GET

/bookings
```

```
GET

/bookings/{id}
```

```
GET

/chats
```

```
GET

/messages
```

```
GET

/profile
```

---

# 🧪 Repository Structure

```
AuthRepository

↓

Login

Signup

Logout

Session
```

```
MechanicRepository

↓

Mechanics

Search

Filters

Garage Details
```

```
BookingRepository

↓

Bookings

Booking Details

Create Booking

Cancel Booking

Reschedule
```

```
ChatRepository

↓

Chats

Messages

Send Message
```

```
ProfileRepository

↓

Profile

Settings

Logout
```

```
VehicleRepository

↓

Vehicles

Update Vehicle
```

---

# 🚀 Running the Project

### Clone the Repository

```bash
git clone YOUR_REPOSITORY_URL
```

---

### Open

Android Studio

---

### Sync

Gradle

---

### Run

Select an Emulator or Physical Device.

Click **Run**.

---

# 📌 Assumptions

- Authentication is simulated.
- Mechanics are loaded from mock data.
- Bookings are simulated.
- Messaging uses sample conversations.
- Static map preview is displayed.
- Payment functionality is not implemented.
- Notifications are represented as mock objects.
- One user can own multiple vehicles.
- Search and filtering are performed on mock data.
- The repository layer is prepared for future REST API integration.

---

# 🌟 Additional Features

- Material Design 3 UI
- Hilt Dependency Injection
- MVVM Architecture
- Repository Pattern
- StateFlow State Management
- Search Mechanics
- Filter Mechanics
- Form Validation
- Modular Package Structure
- Reusable Components
- Loading State Handling
- Error State Handling
- Empty State Handling
- Scalable Architecture
- Production-Oriented Folder Structure

---

# 🔮 Future Improvements

- Firebase Authentication
- Google Maps Integration
- Room Database
- Offline Caching
- Push Notifications
- Online Payments
- WorkManager Background Sync
- Pagination
- Unit Testing
- Dark Theme
- Multi-language Support

---

# 👨‍💻 Developed By

**Your Name**

Android Developer

GitHub: https://github.com/pranav50227

LinkedIn: https://linkedin.com/in/pranav50227

---

# 📄 License

This project has been developed as part of the **Android Development Internship Assignment** for evaluation purposes.