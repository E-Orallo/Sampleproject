# Implementation Plan - Core Infrastructure & Networking

Set up project dependencies for Jetpack Navigation 3, Compose Adaptive, and Retrofit. Define data models and implement the API service for fetching products from Fake Store API.

## User Review Required

> [!NOTE]
> I will be using Retrofit with Moshi for networking as they are already partially configured in the project.

## Proposed Changes

### Data Layer
I will create a `data` package to house models and networking logic.

#### [NEW] [Product.kt](file:///C:/Users/enric/AndroidStudioProjects/Sampleproject/app/src/main/java/simple/project/data/model/Product.kt)
- Defines `Product` and `Rating` data classes.
- Includes Moshi `@Json` annotations.

#### [NEW] [CartItem.kt](file:///C:/Users/enric/AndroidStudioProjects/Sampleproject/app/src/main/java/simple/project/data/model/CartItem.kt)
- Defines `CartItem` to represent items in the shopping cart.

#### [NEW] [Order.kt](file:///C:/Users/enric/AndroidStudioProjects/Sampleproject/app/src/main/java/simple/project/data/model/Order.kt)
- Defines `Order` to represent a completed purchase.

#### [NEW] [FakeStoreApi.kt](file:///C:/Users/enric/AndroidStudioProjects/Sampleproject/app/src/main/java/simple/project/data/api/FakeStoreApi.kt)
- Retrofit interface for [Fake Store API](https://fakestoreapi.com/).
- Methods: `getProducts()`, `getProduct(id)`.

#### [NEW] [NetworkModule.kt](file:///C:/Users/enric/AndroidStudioProjects/Sampleproject/app/src/main/java/simple/project/data/api/NetworkModule.kt)
- Singleton object to provide Retrofit instance and API service.

## Verification Plan

### Automated Tests
- [NEW] `FakeStoreApiTest.kt`: Unit test to verify API response parsing using a real or mocked network call.

### Manual Verification
- Update `MainActivity` temporarily to fetch and display the number of products fetched from the API.
