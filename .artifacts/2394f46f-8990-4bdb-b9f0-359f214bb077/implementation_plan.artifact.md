# Product Discovery and Details Implementation Plan

Implement the Product Discovery (browsing/search) and Product Details screens using Jetpack Compose, Navigation 3, and Compose Material Adaptive.

## Proposed Changes

### Data & Logic

#### [NEW] [CartManager.kt](file:///C:/Users/enric/AndroidStudioProjects/Sampleproject/app/src/main/java/simple/project/data/cart/CartManager.kt)
A simple shared manager to handle "Add to Cart" logic and maintain the cart state.

### UI & Navigation

#### [NEW] [Routes.kt](file:///C:/Users/enric/AndroidStudioProjects/Sampleproject/app/src/main/java/simple/project/ui/navigation/Routes.kt)
Define serializable routes for Navigation 3.
- `ProductDiscovery`
- `ProductDetails(productId: Int)`

#### [NEW] [ProductDiscoveryViewModel.kt](file:///C:/Users/enric/AndroidStudioProjects/Sampleproject/app/src/main/java/simple/project/ui/discovery/ProductDiscoveryViewModel.kt)
ViewModel for the Product Discovery screen. Handles fetching products, searching, and filtering.

#### [NEW] [ProductDetailsViewModel.kt](file:///C:/Users/enric/AndroidStudioProjects/Sampleproject/app/src/main/java/simple/project/ui/details/ProductDetailsViewModel.kt)
ViewModel for the Product Details screen. Handles fetching a specific product and adding it to the cart.

#### [NEW] [ProductDiscoveryScreen.kt](file:///C:/Users/enric/AndroidStudioProjects/Sampleproject/app/src/main/java/simple/project/ui/discovery/ProductDiscoveryScreen.kt)
Product list UI with search and category filtering.

#### [NEW] [ProductDetailsScreen.kt](file:///C:/Users/enric/AndroidStudioProjects/Sampleproject/app/src/main/java/simple/project/ui/details/ProductDetailsScreen.kt)
Product details UI with "Add to Cart" button.

#### [MODIFY] [MainActivity.kt](file:///C:/Users/enric/AndroidStudioProjects/Sampleproject/app/src/main/java/simple/project/MainActivity.kt)
Integrate the adaptive scaffold and Navigation 3 to host the discovery and details screens.

## Verification Plan

### Automated Tests
- Unit tests for `CartManager`.
- Unit tests for `ProductDiscoveryViewModel`.

### Manual Verification
- Run the app and verify product list displays data.
- Test search and category filtering.
- Navigate to details screen and verify data.
- Test "Add to Cart" button.
- Verify adaptive layout on phone (single pane) and tablet/emulator (dual pane).
