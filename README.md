# AndroidMastery

Welcome to **AndroidMastery**, a comprehensive project designed to help you master Android App through hands-on learning and practical application. This repository serves as a learning path for developers of all skill levels to deepen their understanding of Android app, covering everything from basic concepts to advanced techniques.

## Table of Contents

- [Introduction](#introduction)
- [Connect Android to Django API](#connect-android-to-django-api)
- [DatePickerDialog](#datepickerdialog)
- [Spinner](#spinner)
- [Table (READ)](#table)
- [Table (UPDATE)](#update-data)
- [Table (DELETE)](#delete-data)
- [Resolve Security Policies Issue](#resolve-security-policies-issue)
- [Resolve Response Error Issue](#resolve-response-error-issue)
- [JSON Object Error Issue](#json-object-error-issue)
- [Communication not permitted Error Issue](#communication-not-permitted-error-issue)

## Introduction
[Table of Contents](#table-of-contents)<br>
Android is a mobile operating system developed by Google. It is based on a modified version of the Linux kernel and other open-source software. Android is designed primarily for touchscreen mobile devices such as smartphones and tablets. It has a large user base and offers a rich application ecosystem.

#### Getting Started with Android Development

1. **Set Up Your Development Environment**
   - **Android Studio**: The official integrated development environment (IDE) for Android development. It includes tools for designing, coding, testing, and debugging Android applications.
   - **Java/Kotlin**: Android apps are primarily written in Java or Kotlin. Kotlin is now the preferred language for Android development due to its modern syntax and features.

2. **Understanding Android Components**
   - **Activities**: Represent a single screen with a user interface. Each activity is an entry point for interacting with the user.
   - **Fragments**: Modular sections of an activity, which allow for more flexible UI designs and reuse.
   - **Services**: Background components that perform long-running operations without a user interface.
   - **Broadcast Receivers**: Components that respond to broadcast messages from other apps or the system.
   - **Content Providers**: Manage access to a structured set of data, usually stored in a database.

3. **Creating Your First Android App**
   - **Project Structure**: Understanding the Android project structure is crucial. Key directories include `src/main/java` (for source code), `res` (for resources like layouts, images, and strings), and `AndroidManifest.xml` (for app configuration).
   - **Layout Design**: Use XML to design your app's user interface. Android Studio provides a visual editor to simplify layout creation.
   - **Activity Lifecycle**: Understanding the lifecycle of an activity (from creation to destruction) helps in managing resources efficiently.

4. **Building the User Interface**
   - **XML Layouts**: Define your UI using XML. Common layouts include `LinearLayout`, `RelativeLayout`, and `ConstraintLayout`.
   - **Widgets**: Basic UI components like buttons, text fields, and images. Customize these using properties and styles.
   - **RecyclerView**: A powerful tool for displaying large sets of data in a scrollable list.

5. **Handling User Input**
   - **Event Listeners**: Handle user interactions like clicks, touches, and gestures through event listeners.
   - **Intents**: Use intents to navigate between activities, pass data, or start services.

6. **Data Storage and Persistence**
   - **SharedPreferences**: Store simple key-value pairs.
   - **SQLite**: Use SQLite databases for more complex data storage needs.
   - **Room**: An abstraction layer over SQLite to simplify database operations.

7. **Networking and APIs**
   - **HTTP Libraries**: Use libraries like Retrofit or Volley to make network requests.
   - **RESTful APIs**: Interact with web services using RESTful APIs to fetch and send data.

8. **Debugging and Testing**
   - **Logcat**: Use Logcat to view debug messages and diagnose issues.
   - **Unit Testing**: Write unit tests for your code using JUnit.
   - **UI Testing**: Use tools like Espresso for automated UI testing.

9. **Publishing Your App**
   - **Google Play Store**: The primary platform for distributing Android apps. Follow the guidelines for app submission and publishing.
   - **App Monetization**: Explore various monetization strategies such as in-app purchases, ads, and subscriptions.

### Conclusion
Android application development offers a wide range of opportunities and challenges. By understanding the core components, tools, and best practices, developers can create robust, user-friendly applications that leverage the full potential of the Android platform. Whether you're building your first app or looking to refine your skills, the Android development ecosystem provides the resources and support needed to succeed.

## Connect Android to Django API
[Table of Contents](#table-of-contents)<br>
To connect your Android app to your Django API, you'll typically use HTTP requests to communicate with the Django backend. The process involves making GET, POST, PUT, or DELETE requests from your Android app to the endpoints defined in your Django API. Here’s a step-by-step guide on how to achieve this:

### 1. Set Up Your Django API
Ensure your Django API is up and running. Use Django REST framework to create endpoints. Here’s a simple example:

**Install Django REST framework:**
```bash
pip install djangorestframework
```

**Add it to your Django project:**
```python
# settings.py
INSTALLED_APPS = [
    ...
    'rest_framework',
]

# urls.py
from django.urls import path, include
from rest_framework import routers
from . import views

router = routers.DefaultRouter()
router.register(r'items', views.ItemViewSet)

urlpatterns = [
    path('', include(router.urls)),
]

# views.py
from rest_framework import viewsets
from .models import Item
from .serializers import ItemSerializer

class ItemViewSet(viewsets.ModelViewSet):
    queryset = Item.objects.all()
    serializer_class = ItemSerializer

# serializers.py
from rest_framework import serializers
from .models import Item

class ItemSerializer(serializers.ModelSerializer):
    class Meta:
        model = Item
        fields = '__all__'

# models.py
from django.db import models

class Item(models.Model):
    name = models.CharField(max_length=100)
    description = models.TextField()

    def __str__(self):
        return self.name
```

### 2. Enable CORS in Django
Ensure your Django API can accept requests from your Android app by enabling CORS (Cross-Origin Resource Sharing):

**Install django-cors-headers:**
```bash
pip install django-cors-headers
```

**Add it to your Django project:**
```python
# settings.py
INSTALLED_APPS = [
    ...
    'corsheaders',
    'rest_framework',
]

MIDDLEWARE = [
    ...
    'corsheaders.middleware.CorsMiddleware',
    'django.middleware.common.CommonMiddleware',
    ...
]

CORS_ALLOW_ALL_ORIGINS = True
```

### 3. Make HTTP Requests from Android
Use libraries like Retrofit, OkHttp, or the built-in HttpURLConnection to make HTTP requests from your Android app. Here’s an example using Retrofit:

**Add Retrofit dependencies:**
```groovy
// build.gradle (Module: app)
dependencies {
    implementation 'com.squareup.retrofit2:retrofit:2.9.0'
    implementation 'com.squareup.retrofit2:converter-gson:2.9.0'
}
```

**Create the Retrofit client:**
```java
// ApiClient.java
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static final String BASE_URL = "http://your-django-api-url/";
    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
```

**Define the API endpoints:**
```java
// ApiService.java
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Body;

public interface ApiService {
    @GET("items/")
    Call<List<Item>> getItems();

    @POST("items/")
    Call<Item> createItem(@Body Item item);
}

// Item.java
public class Item {
    private int id;
    private String name;
    private String description;

    // getters and setters
}
```

**Make network requests:**
```java
// MainActivity.java
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        apiService = ApiClient.getClient().create(ApiService.class);

        // Fetch items
        fetchItems();

        // Create an item
        createItem(new Item("New Item", "Description of new item"));
    }

    private void fetchItems() {
        Call<List<Item>> call = apiService.getItems();
        call.enqueue(new Callback<List<Item>>() {
            @Override
            public void onResponse(Call<List<Item>> call, Response<List<Item>> response) {
                if (response.isSuccessful()) {
                    List<Item> items = response.body();
                    // Handle the fetched items
                } else {
                    Log.e("MainActivity", "Error: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Item>> call, Throwable t) {
                Log.e("MainActivity", "Error: " + t.getMessage());
            }
        });
    }

    private void createItem(Item item) {
        Call<Item> call = apiService.createItem(item);
        call.enqueue(new Callback<Item>() {
            @Override
            public void onResponse(Call<Item> call, Response<Item> response) {
                if (response.isSuccessful()) {
                    Item createdItem = response.body();
                    // Handle the created item
                } else {
                    Log.e("MainActivity", "Error: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Item> call, Throwable t) {
                Log.e("MainActivity", "Error: " + t.getMessage());
            }
        });
    }
}
```

## DatePickerDialog
[Table of Contents](#table-of-contents)<br>

To create a calendar input in an Android application, you can use a `DatePickerDialog`. 

This dialog allows users to pick a date using a calendar interface. 

### Step 1: Add a Button or TextView to Trigger the DatePickerDialog

First, you'll need a UI element like a `Button` or `TextView` that the user can click to open the `DatePickerDialog`.

```xml
<!-- activity_main.xml -->
<Button
    android:id="@+id/select_date_button"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:text="Select Date" />

<TextView
    android:id="@+id/selected_date_text"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:text="Selected Date" />
```

### Step 2: Implement the `DatePickerDialog` in Your Activity

In your `MainActivity` (or any other activity), create and display the `DatePickerDialog` when the user clicks the button.

```java
// MainActivity.java
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private Button selectDateButton;
    private TextView selectedDateText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        selectDateButton = findViewById(R.id.select_date_button);
        selectedDateText = findViewById(R.id.selected_date_text);

        selectDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
    }

    private void showDatePickerDialog() {
        // Get the current date
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and show it
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                MainActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        // Note: month is 0-based, so we add 1
                        String selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                        selectedDateText.setText(selectedDate);
                    }
                },
                year, month, day);

        datePickerDialog.show();
    }
}
```

### Step 3: Customize the DatePickerDialog (Optional)

You can customize the `DatePickerDialog` by setting min/max dates or formatting the selected date.

#### Setting Minimum and Maximum Dates

You can restrict the date selection to a specific range:

```java
datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());  // Set min date to current date
calendar.add(Calendar.YEAR, 1);  // Add 1 year to the current date
datePickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());  // Set max date to 1 year from today
```

#### Formatting the Date

If you want to display the date in a specific format:

```java
// In the onDateSet method
SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
String selectedDate = dateFormat.format(calendar.getTime());
selectedDateText.setText(selectedDate);
```

## Spinner
[Table of Contents](#table-of-contents)<br>

### Purpose
The `fetchLocations()` method is designed to retrieve a list of locations from a remote server using Retrofit, then update a `Spinner` in your Android app with the names of these locations.

### Code Breakdown

```java
private void fetchLocations() {
    // Step 1: Create a Retrofit call to fetch the list of locations
    Call<List<Location>> call = apiService.getLocations();
    
    // Step 2: Enqueue the call to execute it asynchronously
    call.enqueue(new Callback<List<Location>>() {
        // Step 3: Handle the response from the server
        @Override
        public void onResponse(Call<List<Location>> call, Response<List<Location>> response) {
            // Check if the response was successful and contains a body
            if (response.isSuccessful() && response.body() != null) {
                // Step 4: Store the list of locations from the response
                locationsList = response.body();
                List<String> locationNames = new ArrayList<>();

                // Step 5: Extract the name of each location and add it to the locationNames list
                for (Location location : locationsList) {
                    locationNames.add(location.getLocationName());
                }

                // Step 6: Update the Spinner with the retrieved location names
                locationAdapter.clear(); // Clear any previous data in the adapter
                locationAdapter.addAll(locationNames); // Add the new location names
                locationAdapter.notifyDataSetChanged(); // Notify the Spinner that the data has changed
            } else {
                // Handle the case where the response was not successful or the body is null
                Toast.makeText(MainActivity.this, "Failed to load locations", Toast.LENGTH_SHORT).show();
            }
        }

        // Step 7: Handle any errors that occur during the network request
        @Override
        public void onFailure(Call<List<Location>> call, Throwable t) {
            // Display an error message to the user
            Toast.makeText(MainActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
        }
    });
}
```

### Detailed Explanation

1. **Creating the Retrofit Call**:
   ```java
   Call<List<Location>> call = apiService.getLocations();
   ```
   - This line creates a Retrofit `Call` object that is set up to send an HTTP GET request to the server endpoint defined in `apiService.getLocations()`. This endpoint is expected to return a list of `Location` objects.

2. **Executing the Call Asynchronously**:
   ```java
   call.enqueue(new Callback<List<Location>>() {
   ```
   - `enqueue` is used to send the request asynchronously, meaning it runs in the background without blocking the main thread (which is important for smooth UI performance). 

3. **Handling the Response**:
   ```java
   @Override
   public void onResponse(Call<List<Location>> call, Response<List<Location>> response) {
       if (response.isSuccessful() && response.body() != null) {
           // Handle successful response
       } else {
           // Handle unsuccessful response
       }
   }
   ```
   - `onResponse` is called when the server responds. The `Response` object contains the server's response, including the HTTP status code and the body of the response.
   - `response.isSuccessful()` checks if the HTTP request was successful (status code 200-299).
   - `response.body()` retrieves the actual data returned by the server (in this case, a list of `Location` objects).

4. **Updating the UI**:
   ```java
   locationsList = response.body();
   List<String> locationNames = new ArrayList<>();

   for (Location location : locationsList) {
       locationNames.add(location.getLocationName());
   }
   ```
   - The list of `Location` objects is stored in `locationsList`.
   - A new list `locationNames` is created to hold just the names of these locations.
   - A `for` loop iterates through each `Location` object in `locationsList` and extracts the `locationName`, adding it to `locationNames`.

5. **Updating the Spinner**:
   ```java
   locationAdapter.clear();
   locationAdapter.addAll(locationNames);
   locationAdapter.notifyDataSetChanged();
   ```
   - `locationAdapter.clear()` clears any existing data in the `ArrayAdapter` connected to the `Spinner`.
   - `locationAdapter.addAll(locationNames)` adds the new list of location names to the adapter.
   - `locationAdapter.notifyDataSetChanged()` notifies the `Spinner` that its data has changed, causing it to refresh and display the new locations.

6. **Handling Errors**:
   ```java
   @Override
   public void onFailure(Call<List<Location>> call, Throwable t) {
       Toast.makeText(MainActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
   }
   ```
   - `onFailure` is called if the network request fails (e.g., due to no internet connection or server issues). The `Throwable` object `t` contains information about the failure.
   - A `Toast` is displayed to inform the user of the error.

### Summary
This method is responsible for fetching location data from a server, processing that data, and updating a `Spinner` widget with the location names. It ensures that the network request is performed asynchronously, and it handles both successful and unsuccessful responses gracefully, providing feedback to the user if something goes wrong.

To make the `Spinner` behave like an HTML `<select>` element (where it displays the `locationName` but holds the corresponding `id` as the value), you can follow these steps:

### 1. Modify the `Location` Class
Ensure that your `Location` class has `id` and `locationName` attributes, which it already does based on your provided class.

### 2. Create a Custom ArrayAdapter
To display the `locationName` in the `Spinner` while holding onto the corresponding `id`, you can create a custom `ArrayAdapter`. This adapter will be used to display the names in the `Spinner`, but you can access the `id` when needed.

Here's how you can implement it:

#### 2.1. Create the Custom Adapter

```java
import android.content.Context;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class LocationAdapter extends ArrayAdapter<Location> {

    public LocationAdapter(@NonNull Context context, int resource, @NonNull List<Location> locations) {
        super(context, resource, locations);
    }

    @NonNull
    @Override
    public Location getItem(int position) {
        return super.getItem(position);
    }

    @Nullable
    @Override
    public String getItemAsString(int position) {
        return getItem(position).getLocationName();
    }
}
```

#### 2.2. Use the Custom Adapter in Your Activity

Update your `MainActivity` to use the custom adapter and fetch the selected `id` from the `Spinner`.

```java
locationAdapter = new LocationAdapter(this, android.R.layout.simple_spinner_item, locationsList);
locationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
locationSpinner.setAdapter(locationAdapter);
```

#### 3. Fetching the Selected ID

To get the `id` of the selected item from the `Spinner`, you can do the following:

```java
int selectedPosition = locationSpinner.getSelectedItemPosition();
Location selectedLocation = locationAdapter.getItem(selectedPosition);
int selectedLocationId = selectedLocation.getId();
```

#### 4. Putting It All Together

Here’s a summary of how it would work in your `MainActivity`:

```java
public class MainActivity extends AppCompatActivity {

    private Spinner locationSpinner;
    private List<Location> locationsList;
    private LocationAdapter locationAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationSpinner = findViewById(R.id.location_spinner);
        locationsList = new ArrayList<>();

        // Initialize custom adapter
        locationAdapter = new LocationAdapter(this, android.R.layout.simple_spinner_item, locationsList);
        locationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        locationSpinner.setAdapter(locationAdapter);

        fetchLocations();

        // Add button click listener
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String itemName = txt1.getText().toString();

                // Get the selected location ID
                int selectedPosition = locationSpinner.getSelectedItemPosition();
                Location selectedLocation = locationAdapter.getItem(selectedPosition);
                int selectedLocationId = selectedLocation.getId();

                if (!itemName.isEmpty()) {
                    addItem(itemName, selectedLocationId);
                } else {
                    Toast.makeText(MainActivity.this, "Please fill in the item name", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    
    private void fetchLocations() {
        // Code to fetch locations from the API
    }

    private void addItem(String itemName, int locationId) {
        // Code to add item to the server
    }
}
```

In the `fetchLocations()` method, we fetch the list of locations from the API and populate the `Spinner` with the `locationName` while keeping the corresponding `id` values associated with each name. Here's how you can implement this method in conjunction with the custom adapter:

### Updated `fetchLocations()` Method

```java
private void fetchLocations() {
    Call<List<Location>> call = apiService.getLocations();
    call.enqueue(new Callback<List<Location>>() {
        @Override
        public void onResponse(Call<List<Location>> call, Response<List<Location>> response) {
            if (response.isSuccessful() && response.body() != null) {
                locationsList.clear();
                locationsList.addAll(response.body());

                // Notify the adapter that the data has changed
                locationAdapter.notifyDataSetChanged();
            } else {
                Toast.makeText(MainActivity.this, "Failed to load locations", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onFailure(Call<List<Location>> call, Throwable t) {
            Toast.makeText(MainActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
        }
    });
}
```

### Explanation

1. **Call to API**: 
    - The method starts by making a call to the API using `apiService.getLocations()`.

2. **Enqueueing the Call**:
    - The `enqueue` method is used to perform the network operation asynchronously, meaning it won’t block the main UI thread.

3. **Handling the Response**:
    - **Success (`onResponse`)**:
        - If the API call is successful (`response.isSuccessful()`), and the response body is not null, we clear the current `locationsList` and populate it with the new data fetched from the server.
        - The adapter is then notified that the data has changed with `locationAdapter.notifyDataSetChanged()`. This triggers the `Spinner` to refresh its list of items.
    - **Failure (`onFailure`)**:
        - If the API call fails, a `Toast` message is displayed with the error message.

4. **UI Updates**:
    - After the locations are fetched and the adapter is updated, the `Spinner` will automatically reflect the updated list of locations, displaying the `locationName` values to the user.

### How It Works

- **Fetching Data**: The `fetchLocations()` method fetches location data from the API and populates the `locationsList` with `Location` objects.
- **Updating the Spinner**: The custom `LocationAdapter` is notified of the changes in the dataset via `notifyDataSetChanged()`, which updates the `Spinner` to display the new list of `locationName` values.
- **Selecting and Using Values**: When the user selects an item from the `Spinner`, you can retrieve the associated `id` from the selected `Location` object.


To make the `Spinner` display the `locationName` instead of the object reference, you need to override the `toString()` method in your `Location` class. Here's how to do it:

### Update the `Location` Class

Add the `toString()` method to your `Location` class so that it returns the `locationName`.

```java
public class Location {

    @SerializedName("id")
    private int id;

    @SerializedName("locationName")
    private String locationName;

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    @Override
    public String toString() {
        return locationName;  // This will be displayed in the Spinner
    }
}
```

With this setup, the `Spinner` will show the `locationName` to the user while keeping track of the `id` values, allowing you to use the `id` when needed, such as when adding a new item.


### Summary

- **Custom Adapter**: We created a custom `ArrayAdapter` (`LocationAdapter`) to hold the `Location` objects.
- **Spinner Setup**: We set up the `Spinner` to use the custom adapter.
- **Retrieving Selected ID**: We retrieved the selected `Location` object from the `Spinner` and extracted its `id` for use when adding the item.

With this setup, your `Spinner` will display `locationName` values to the user, but you'll have access to the corresponding `id` values when you need to use them (like when submitting the form).


## Table
[Table of Contents](#table-of-contents)<br>

Sure! Below is the full working code for creating a table that populates `itemName`, `date_added`, and `itemLocation_id` in an Android app using `androidx.constraintlayout.widget.ConstraintLayout` and `TableLayout`.

### 1. XML Layout File (`activity_main.xml`)

This XML layout defines a `ConstraintLayout` with a `TableLayout` inside it. The `TableLayout` will display the items in a tabular format.

```xml
<!-- res/layout/activity_main.xml -->
<androidx.constraintlayout.widget.ConstraintLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:padding="16dp">

    <TableLayout
        android:id="@+id/item_table"
        android:layout_width="0dp"
        android:layout_height="wrap_content"
        android:padding="16dp"
        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintEnd_toEndOf="parent">

        <!-- Table Header -->
        <TableRow>
            <TextView
                android:text="Item Name"
                android:padding="8dp"
                android:textStyle="bold"/>
            <TextView
                android:text="Date Added"
                android:padding="8dp"
                android:textStyle="bold"/>
            <TextView
                android:text="Location ID"
                android:padding="8dp"
                android:textStyle="bold"/>
        </TableRow>

        <!-- Data rows will be added programmatically -->
        
    </TableLayout>

</androidx.constraintlayout.widget.ConstraintLayout>
```

### 2. Java Code for `MainActivity`

This Java class populates the table with data from the `Item` class. It dynamically creates `TableRow` elements and populates them with `TextView` elements that display `itemName`, `date_added`, and `itemLocation_id`.

```java
// MainActivity.java
package com.example.myjapanese;

import android.os.Bundle;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TableLayout itemTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        itemTable = findViewById(R.id.item_table);

        // Fetch items (replace this with your actual data fetching method)
        List<Item> items = fetchItems();

        // Populate the table with items
        populateTable(items);
    }

    private void populateTable(List<Item> items) {
        for (Item item : items) {
            TableRow tableRow = new TableRow(this);

            TextView itemNameView = new TextView(this);
            itemNameView.setText(item.getItemName());
            itemNameView.setPadding(8, 8, 8, 8);

            TextView dateAddedView = new TextView(this);
            dateAddedView.setText(item.getDateAdded()); // Assuming dateAdded is a String
            dateAddedView.setPadding(8, 8, 8, 8);

            TextView itemLocationIdView = new TextView(this);
            itemLocationIdView.setText(String.valueOf(item.getItemLocationId()));
            itemLocationIdView.setPadding(8, 8, 8, 8);

            tableRow.addView(itemNameView);
            tableRow.addView(dateAddedView);
            tableRow.addView(itemLocationIdView);

            itemTable.addView(tableRow);
        }
    }

    // Dummy method to fetch items (replace this with your actual data fetching method)
    private List<Item> fetchItems() {
        List<Item> items = new ArrayList<>();
        items.add(new Item("Item 1", "2024-08-20", 1));
        items.add(new Item("Item 2", "2024-08-21", 2));
        return items;
    }
}
```

### 3. `Item` Class

This class represents an `Item` object with `itemName`, `date_added`, and `itemLocation_id` properties.

```java
// Item.java
package com.example.myjapanese;

public class Item {

    private String itemName;
    private String dateAdded;
    private int itemLocationId;

    public Item(String itemName, String dateAdded, int itemLocationId) {
        this.itemName = itemName;
        this.dateAdded = dateAdded;
        this.itemLocationId = itemLocationId;
    }

    public String getItemName() {
        return itemName;
    }

    public String getDateAdded() {
        return dateAdded;
    }

    public int getItemLocationId() {
        return itemLocationId;
    }
}
```

### Updated `MainActivity.java` to retrieve data from a database using Retrofit

```java
// MainActivity.java
package com.example.myjapanese;

import android.os.Bundle;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private TableLayout itemTable;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        itemTable = findViewById(R.id.item_table);

        // Initialize Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://your-api-base-url.com/")  // Replace with your actual base URL
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);

        // Fetch items from the database
        fetchItems();
    }

    private void fetchItems() {
        Call<List<Item>> call = apiService.getItems();
        call.enqueue(new Callback<List<Item>>() {
            @Override
            public void onResponse(Call<List<Item>> call, Response<List<Item>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Item> items = response.body();
                    populateTable(items);
                } else {
                    Toast.makeText(MainActivity.this, "Failed to load items", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Item>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void populateTable(List<Item> items) {
        for (Item item : items) {
            TableRow tableRow = new TableRow(this);

            TextView itemNameView = new TextView(this);
            itemNameView.setText(item.getItemName());
            itemNameView.setPadding(8, 8, 8, 8);

            TextView dateAddedView = new TextView(this);
            dateAddedView.setText(item.getDateAdded()); // Assuming dateAdded is a String
            dateAddedView.setPadding(8, 8, 8, 8);

            TextView itemLocationIdView = new TextView(this);
            itemLocationIdView.setText(String.valueOf(item.getItemLocationId()));
            itemLocationIdView.setPadding(8, 8, 8, 8);

            tableRow.addView(itemNameView);
            tableRow.addView(dateAddedView);
            tableRow.addView(itemLocationIdView);

            itemTable.addView(tableRow);
        }
    }
}
```
### API Endpoints

Ensure that your API endpoints are properly configured and that your server returns the expected JSON format for `Item` objects. The `Item` class should have the appropriate fields and getters.

### **Modify the `populateTable` Method to Handle Click Events:**

You will need to set an `OnClickListener` for each `TableRow` when you populate the table. When a row is clicked, the values of that row will be used to update the `TextView` and `Spinner`.

Here’s how you can modify the `populateTable` method:

```java
private void populateTable(final List<Item> items) {
    runOnUiThread(new Runnable() {
        @Override
        public void run() {
            // Preserve the header by removing only the rows after the header
            int childCount = itemTable.getChildCount();
            if (childCount > 1) {  // Assuming the first row is the header
                itemTable.removeViews(1, childCount - 1);
            }

            // Define the colors for even and odd rows
            int evenRowColor = getResources().getColor(android.R.color.darker_gray);  // Example color for even rows
            int oddRowColor = getResources().getColor(android.R.color.white);         // Example color for odd rows

            for (int i = 0; i < items.size(); i++) {
                Item item = items.get(i);
                TableRow tableRow = new TableRow(MainActivity.this);

                TextView itemNameView = new TextView(MainActivity.this);
                itemNameView.setText(item.getItemName());
                itemNameView.setPadding(8, 8, 8, 8);

                TextView dateAddedView = new TextView(MainActivity.this);
                dateAddedView.setText(item.getDateAdded());
                dateAddedView.setPadding(8, 8, 8, 8);

                TextView itemLocationIdView = new TextView(MainActivity.this);
                itemLocationIdView.setText(String.valueOf(item.getItemLocationId()));
                itemLocationIdView.setPadding(8, 8, 8, 8);

                tableRow.addView(itemNameView);
                tableRow.addView(dateAddedView);
                tableRow.addView(itemLocationIdView);

                // Set the background color of the row based on its position (even or odd)
                if (i % 2 == 0) {
                    tableRow.setBackgroundColor(evenRowColor);  // Even row color
                } else {
                    tableRow.setBackgroundColor(oddRowColor);   // Odd row color
                }

                // Add OnClickListener to each row
                tableRow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Set the selected values to the TextViews and Spinner
                        selectedDateText.setText(item.getDateAdded());
                        txt1.setText(item.getItemName());

                        // Find the location in the spinner by its ID
                        int locationPosition = -1;
                        for (int i = 0; i < locationAdapter.getCount(); i++) {
                            if (locationAdapter.getItem(i).getId() == item.getItemLocationId()) {
                                locationPosition = i;
                                break;
                            }
                        }

                        if (locationPosition >= 0) {
                            locationSpinner.setSelection(locationPosition);
                        }
                    }
                });

                itemTable.addView(tableRow);
            }
        }
    });
}


```

### **Explanation:**
- **`TableRow ClickListener:`** Each `TableRow` is given an `OnClickListener`. When the row is clicked, it retrieves the `item` associated with that row.
- **`Update TextViews and Spinner:`** The `TextView` for the date (`selectedDateText`) and the item name (`txt1`) are updated with the values from the selected `item`. The `Spinner` (`locationSpinner`) is updated by finding the position of the `itemLocationId` in the adapter and setting the `Spinner` to that position.

### **Ensuring Data Binding:**
Ensure that the `Spinner` is properly populated with locations before you try to set its selection. You should call `fetchLocations()` before populating the table to ensure that the `locationSpinner` has all the necessary data.

### **UI Thread Consideration:**
Since you're using `runOnUiThread` in `populateTable`, the UI updates are guaranteed to run on the main thread, ensuring that the `TextViews` and `Spinner` are updated safely.

## UPDATE DATA
[Table of Contents](#table-of-contents)<br>

Include an ID in your `Item` class and handle the item editing in your `MainActivity`:

### Updated `Item` Class

Add an `id` field to the `Item` class and include the corresponding getter and setter methods:

```java
// Item.java
package com.example.myjapanese;

public class Item {

    private int id; // Add this field
    private String itemName;
    private String date_added;
    private int itemLocation;

    // Constructors
    public Item() {
    }

    public Item(int id, String itemName, String date_added, int itemLocation) {
        this.id = id;
        this.itemName = itemName;
        this.date_added = date_added;
        this.itemLocation = itemLocation;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getDateAdded() {
        return date_added;
    }

    public void setDateAdded(String date_added) {
        this.date_added = date_added;
    }

    public int getItemLocationId() {
        return itemLocation;
    }

    public void setItemLocationId(int itemLocation) {
        this.itemLocation = itemLocation;
    }
}
```

### Updated `ApiService` Interface

Add a method for updating items:

```java
// ApiService.java
package com.example.myjapanese;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Body;

public interface ApiService {

    @GET("potato_posts/")
    Call<List<PotatoPost>> getPotatoPosts();

    @GET("location/")
    Call<List<Location>> getLocations();

    @GET("item/")
    Call<List<Item>> getItems();

    @POST("item/")
    Call<Item> createItem(@Body Item item);

    @PUT("item/{id}/")
    Call<Item> updateItem(@Path("id") int id, @Body Item item);
}
```

### `updateItem` Method
This method is used to update an existing item in your backend.

```java
// Method to update an existing item in the database
private void updateItem(int itemId, String itemName, String dateAdded, int itemLocationId) {
    Item updatedItem = new Item(itemId, itemName, dateAdded, itemLocationId);

    Call<Item> call = apiService.updateItem(itemId, updatedItem);
    call.enqueue(new Callback<Item>() {
        @Override
        public void onResponse(Call<Item> call, Response<Item> response) {
            if (response.isSuccessful() && response.body() != null) {
                fetchItems();  // Refresh the table to display the updated item
                Toast.makeText(MainActivity.this, "Item updated successfully", Toast.LENGTH_SHORT).show();
                selectedItemId = -1;  // Reset to add mode
                clearInputFields();  // Clear input fields after update
            } else {
                Toast.makeText(MainActivity.this, "Failed to update item", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onFailure(Call<Item> call, Throwable t) {
            Toast.makeText(MainActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
        }
    });
}
```

### `clearInputFields` Method
This helper method clears the input fields after an item is added or updated.

```java
// Method to clear input fields after adding or updating an item
private void clearInputFields() {
    itemNameText.setText("");
    selectedDateText.setText("");
    locationSpinner.setSelection(0);  // Reset spinner to the first item
}
```

### `ApiService` Interface Updates
Make sure your `ApiService` interface is updated to include the `updateItem` method:

```java
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiService {
    // Other methods...

    @PUT("item/{id}/")
    Call<Item> updateItem(@Path("id") int id, @Body Item item);
}
```

### `addItem` and `updateItem` Method
```java
// MainActivity.java

    // Method to add a new item to the database
    private void addItem(String itemName, String dateAdded, int itemLocationId) {
        Item item = new Item(0, itemName, dateAdded, itemLocationId);

        Call<Item> call = apiService.createItem(item);
        call.enqueue(new Callback<Item>() {
            @Override
            public void onResponse(Call<Item> call, Response<Item> response) {
                if (response.isSuccessful() && response.body() != null) {
                    fetchItems();  // Refresh the table to display the newly added item
                    Toast.makeText(MainActivity.this, "Item added successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Failed to add item", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Item> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Method to update an existing item in the database
    private void updateItem(int itemId, String itemName, String dateAdded, int itemLocationId) {
        Item updatedItem = new Item(itemId, itemName, dateAdded, itemLocationId);

        Call<Item> call = apiService.updateItem(itemId, updatedItem);
        call.enqueue(new Callback<Item>() {
            @Override
            public void onResponse(Call<Item> call, Response<Item> response) {
                if (response.isSuccessful() && response.body() != null) {
                    fetchItems();  // Refresh the table to display the updated item
                    Toast.makeText(MainActivity.this, "Item updated successfully", Toast.LENGTH_SHORT).show();
                    selectedItemId = -1;  // Reset to add mode
                    clearInputFields();  // Clear input fields after update
                } else {
                    Toast.makeText(MainActivity.this, "Failed to update item", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Item> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Method to clear input fields after adding or updating an item
    private void clearInputFields() {
        itemNameText.setText("");
        selectedDateText.setText("");
        locationSpinner.setSelection(0);  // Reset spinner to the first item
    }

}
```
### Update Item Method in Activity ###

In your `MainActivity`, create a method to handle the update process:

```java
private void updateItem(int id, String itemName, String dateAdded, int itemLocationId) {
    Item item = new Item(id, itemName, dateAdded, itemLocationId);

    Call<Item> call = apiService.updateItem(id, item);
    call.enqueue(new Callback<Item>() {
        @Override
        public void onResponse(Call<Item> call, Response<Item> response) {
            if (response.isSuccessful()) {
                Toast.makeText(MainActivity.this, "Item updated successfully!", Toast.LENGTH_SHORT).show();
                fetchItems();  // Refresh the table to show updated data
            } else {
                try {
                    String errorBody = response.errorBody().string();
                    Toast.makeText(MainActivity.this, "Failed to update item: " + response.code() + " " + errorBody, Toast.LENGTH_LONG).show();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "Failed to update item and error body could not be parsed", Toast.LENGTH_LONG).show();
                }
            }
        }

        @Override
        public void onFailure(Call<Item> call, Throwable t) {
            Toast.makeText(MainActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
        }
    });
}
```

### Trigger Update on Row Click ###

Modify the row click logic in your table to allow editing:

```java
tableRow.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        // Set the selected values to the TextViews and Spinner
        selectedDateText.setText(item.getDateAdded());
        itemNameText.setText(item.getItemName());

        // Find the location in the spinner by its ID
        int locationPosition = -1;
        for (int i = 0; i < locationAdapter.getCount(); i++) {
            if (locationAdapter.getItem(i).getId() == item.getItemLocationId()) {
                locationPosition = i;
                break;
            }
        }

        if (locationPosition >= 0) {
            locationSpinner.setSelection(locationPosition);
        }

        // Example of how to use updateItem
        updateItem(item.getId(), item.getItemName(), item.getDateAdded(), item.getItemLocationId());
    }
});
```

### Ensure `id` Field in `ItemSerializer`
First, make sure that the `id` field is explicitly included in the `ItemSerializer` so that it gets passed to your Android app:

```python
class ItemSerializer(serializers.ModelSerializer):
    itemLocation = serializers.PrimaryKeyRelatedField(
        queryset=Location.objects.all())

    class Meta:
        model = Item
        fields = ['id', 'itemName', 'date_added', 'itemLocation']  # Include 'id' field
```

### Ensure Backend Supports PUT Method
Lastly, make sure your Django view supports the `PUT` method for updating items. If you're using `ListCreateAPIView`, you might want to extend `RetrieveUpdateDestroyAPIView` for individual item updates:

```python
from rest_framework import generics

class ItemDetailUpdateDelete(generics.RetrieveUpdateDestroyAPIView):
    queryset = Item.objects.all()
    serializer_class = ItemSerializer
```

Add the corresponding URL pattern in your `urls.py`:

```python
from django.urls import path
from .views import ItemListCreate, ItemDetailUpdateDelete

urlpatterns = [
    path('items/', ItemListCreate.as_view(), name='item-list-create'),
    path('items/<int:pk>/', ItemDetailUpdateDelete.as_view(), name='item-detail-update-delete'),
]
```

## Delete Data
[Table of Contents](#table-of-contents)<br>

To add a delete button to each row in the table and enable it to delete the corresponding item from the backend, follow these steps:

### 1. Update `populateTable` Method

Add a `Button` for deletion to each `TableRow` and handle its click event. Modify the `populateTable` method in your `MainActivity` to include a delete button in each row and handle the deletion.

Here’s how you can update your `populateTable` method:

```java
private void populateTable(final List<Item> items) {
    runOnUiThread(new Runnable() {
        @Override
        public void run() {
            // Preserve the header by removing only the rows after the header
            int childCount = itemTable.getChildCount();
            if (childCount > 1) {  // Assuming the first row is the header
                itemTable.removeViews(1, childCount - 1);
            }

            // Define the colors for even and odd rows
            int evenRowColor = getResources().getColor(android.R.color.darker_gray);  // Example color for even rows
            int oddRowColor = getResources().getColor(android.R.color.white);         // Example color for odd rows

            for (int i = 0; i < items.size(); i++) {
                final Item item = items.get(i);
                TableRow tableRow = new TableRow(MainActivity.this);

                TextView itemNameView = new TextView(MainActivity.this);
                itemNameView.setText(item.getItemName());
                itemNameView.setPadding(8, 8, 8, 8);

                TextView dateAddedView = new TextView(MainActivity.this);
                dateAddedView.setText(item.getDateAdded());
                dateAddedView.setPadding(8, 8, 8, 8);

                TextView itemLocationIdView = new TextView(MainActivity.this);
                itemLocationIdView.setText(String.valueOf(item.getItemLocationId()));
                itemLocationIdView.setPadding(8, 8, 8, 8);

                Button deleteButton = new Button(MainActivity.this);
                deleteButton.setText("Delete");
                deleteButton.setPadding(8, 8, 8, 8);

                // Set the background color of the row based on its position (even or odd)
                if (i % 2 == 0) {
                    tableRow.setBackgroundColor(evenRowColor);  // Even row color
                } else {
                    tableRow.setBackgroundColor(oddRowColor);   // Odd row color
                }

                // Add views to the row
                tableRow.addView(itemNameView);
                tableRow.addView(dateAddedView);
                tableRow.addView(itemLocationIdView);
                tableRow.addView(deleteButton);

                // Add OnClickListener to each row
                tableRow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Set the selected values to the TextViews and Spinner
                        selectedDateText.setText(item.getDateAdded());
                        itemNameText.setText(item.getItemName());

                        // Find the location in the spinner by its ID
                        int locationPosition = -1;
                        for (int i = 0; i < locationAdapter.getCount(); i++) {
                            if (locationAdapter.getItem(i).getId() == item.getItemLocationId()) {
                                locationPosition = i;
                                break;
                            }
                        }

                        if (locationPosition >= 0) {
                            locationSpinner.setSelection(locationPosition);
                        }
                    }
                });

                // Handle delete button click
                deleteButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showDeleteConfirmationDialog(item.getId(), tableRow);
                        }
                });

                itemTable.addView(tableRow);
            }
        }
    });
}
```
To add a confirmation dialog before deleting an item, you can use an `AlertDialog` in Android. This dialog will ask the user to confirm their action before proceeding with the deletion. Here's how you can integrate this into your existing code:

### 1. Modify `populateTable` Method

Update the `populateTable` method to show a confirmation dialog when the delete button is clicked:

```java
private void populateTable(final List<Item> items) {
    runOnUiThread(new Runnable() {
        @Override
        public void run() {
            // Preserve the header by removing only the rows after the header
            int childCount = itemTable.getChildCount();
            if (childCount > 1) {  // Assuming the first row is the header
                itemTable.removeViews(1, childCount - 1);
            }

            // Define the colors for even and odd rows
            int evenRowColor = getResources().getColor(android.R.color.darker_gray);  // Example color for even rows
            int oddRowColor = getResources().getColor(android.R.color.white);         // Example color for odd rows

            for (int i = 0; i < items.size(); i++) {
                final Item item = items.get(i);
                TableRow tableRow = new TableRow(MainActivity.this);

                TextView itemNameView = new TextView(MainActivity.this);
                itemNameView.setText(item.getItemName());
                itemNameView.setPadding(8, 8, 8, 8);

                TextView dateAddedView = new TextView(MainActivity.this);
                dateAddedView.setText(item.getDateAdded());
                dateAddedView.setPadding(8, 8, 8, 8);

                TextView itemLocationIdView = new TextView(MainActivity.this);
                itemLocationIdView.setText(String.valueOf(item.getItemLocationId()));
                itemLocationIdView.setPadding(8, 8, 8, 8);

                Button deleteButton = new Button(MainActivity.this);
                deleteButton.setText("Delete");
                deleteButton.setPadding(8, 8, 8, 8);

                // Set the background color of the row based on its position (even or odd)
                if (i % 2 == 0) {
                    tableRow.setBackgroundColor(evenRowColor);  // Even row color
                } else {
                    tableRow.setBackgroundColor(oddRowColor);   // Odd row color
                }

                // Add views to the row
                tableRow.addView(itemNameView);
                tableRow.addView(dateAddedView);
                tableRow.addView(itemLocationIdView);
                tableRow.addView(deleteButton);

                // Add OnClickListener to each row
                tableRow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Set the selected values to the TextViews and Spinner
                        selectedDateText.setText(item.getDateAdded());
                        itemNameText.setText(item.getItemName());

                        // Find the location in the spinner by its ID
                        int locationPosition = -1;
                        for (int i = 0; i < locationAdapter.getCount(); i++) {
                            if (locationAdapter.getItem(i).getId() == item.getItemLocationId()) {
                                locationPosition = i;
                                break;
                            }
                        }

                        if (locationPosition >= 0) {
                            locationSpinner.setSelection(locationPosition);
                        }
                    }
                });

                // Handle delete button click
                deleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showDeleteConfirmationDialog(item.getId(), tableRow);
                    }
                });

                itemTable.addView(tableRow);
            }
        }
    });
}
```

### 2. Implement `showDeleteConfirmationDialog` Method

Add a method to show a confirmation dialog before deleting an item. This method will use `AlertDialog` to prompt the user:

```java
private void showDeleteConfirmationDialog(final int itemId, final TableRow tableRow) {
    new AlertDialog.Builder(this)
            .setTitle("Delete Item")
            .setMessage("Are you sure you want to delete this item?")
            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    deleteItem(itemId, tableRow);
                }
            })
            .setNegativeButton("No", null)
            .show();
}
```

### 3. Implement `deleteItem` Method

Add a method to handle the deletion of the item. This method will make a DELETE request to your API and remove the row from the table if the deletion is successful.

```java
private void deleteItem(int itemId, final TableRow tableRow) {
    Call<Void> call = apiService.deleteItem(itemId);
    call.enqueue(new Callback<Void>() {
        @Override
        public void onResponse(Call<Void> call, Response<Void> response) {
            if (response.isSuccessful()) {
                itemTable.removeView(tableRow);
                Toast.makeText(MainActivity.this, "Item deleted successfully!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "Failed to delete item", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onFailure(Call<Void> call, Throwable t) {
            Toast.makeText(MainActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
        }
    });
}
```

### 4. Update `ApiService` Interface

Ensure that your `ApiService` interface includes a method for deleting an item:

```java
@DELETE("item/{id}/")
Call<Void> deleteItem(@Path("id") int id);
```



## Resolve Security Policies Issue
[Table of Contents](#table-of-contents)<br>
The error `CLEARTEXT communication to [my localhost IP] not permitted by network security policy` indicates that your app is trying to make an insecure (non-HTTPS) network request, which is blocked by default due to security policies.

To resolve this, you need to allow cleartext traffic for your development purposes. Here's how you can do it:

### 1. Update `AndroidManifest.xml`

You need to create or update your `network_security_config.xml` and reference it in your `AndroidManifest.xml`.

#### `AndroidManifest.xml`

```xml
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="com.example.myjapanese">

    <application
        android:networkSecurityConfig="@xml/network_security_config"
        ... >
        ...
    </application>

    <uses-permission android:name="android.permission.INTERNET" />
</manifest>
```

### 2. Create `network_security_config.xml`

Create a new XML resource file under `res/xml` directory named `network_security_config.xml` with the following content:

#### `res/xml/network_security_config.xml`

```xml
<?xml version="1.0" encoding="utf-8"?>
<network-security-config>
    <domain-config cleartextTrafficPermitted="true">
        <domain includeSubdomains="true">[YOUR_LOCAL_IP]</domain>
    </domain-config>
</network-security-config>
```

### Check Android Manifest Permissions

Ensure your `AndroidManifest.xml` includes the following permission:

```xml
<uses-permission android:name="android.permission.INTERNET" />
```

### Start Django Server

Run the Django server on all interfaces to make it accessible from your Android device or emulator:

```sh
python manage.py runserver 0.0.0.0:8000
```

### Update Django `settings.py`

Ensure `ALLOWED_HOSTS` includes the IP address of your local machine:

```python
ALLOWED_HOSTS = ['10.0.2.2', 'localhost', '127.0.0.1', '<YOUR_LOCAL_IP>']
```

### Validate Network Access

Ensure your development machine and Android device/emulator are on the same network and can communicate with each other.

### Test Connectivity

1. **Ping the Server**: From your Android device/emulator, try to ping the local machine's IP address to ensure connectivity.
2. **Use a Browser**: Open a web browser on your Android device/emulator and navigate to `http://<YOUR_LOCAL_IP>:8000/` to verify the Django server is reachable.

### Restart Your Application

After making these changes, rebuild and restart your application. Your app should now be able to make cleartext (HTTP) requests to your local Django server.

## Resolve Response Error Issue
[Table of Contents](#table-of-contents)<br>
The Potential issues:

### 1. **URL Mismatch in `ApiService` Interface**
   - The endpoint URLs in your `ApiService` might not match the ones defined in your Django `urls.py`. 
   - For example:
     - In Django, the URL for `Item` is `/item`, but in `ApiService`, it's `/item/`.
     - In Django, the URL for `Location` is `/location`, but in `ApiService`, it's `/locations/`.

   **Correction:**
   - Update your `ApiService` to match the exact endpoints defined in your Django `urls.py`:
     ```java
     public interface ApiService {
         @GET("potatoposts/")
         Call<List<PotatoPost>> getPotatoPosts();

         @GET("location/")
         Call<List<Location>> getLocations();

         @GET("item/")
         Call<List<Item>> getItems();
     }
     ```

### 2. **Case Sensitivity in URL Paths**
   - Ensure that the URL paths are exactly as defined, considering case sensitivity. For example, if your Django URLs are defined as `/location/`, the `ApiService` should also have `/location/` (not `/locations/`).

### 3. **Serialized Names in Android Models**
   - Ensure that the serialized names in your Android model classes match the field names returned by your Django API.

   **Item Class in Android:**
   - Your `Item` class is already using `@SerializedName` annotations correctly. Just ensure that the fields returned by the Django API match these annotations exactly.

### 4. **API Base URL in `ApiClient`**
   - Verify that the `BASE_URL` in your `ApiClient` class correctly matches the IP address and port where your Django server is running. 
   - Ensure that this IP address is accessible from your Android device.

### 5. **Check Django Server Response**
   - Use tools like Postman or your web browser to check if the Django server returns the expected data when hitting the `/item/` endpoint. This will help verify that the server is working correctly.

### 6. **Cleartext Communication on Android**
   - Ensure that your Android app is configured to allow cleartext communication if you’re using HTTP instead of HTTPS.
   - Add the following to your `AndroidManifest.xml` inside the `<application>` tag:
     ```xml
     <application
         ...
         android:usesCleartextTraffic="true">
         ...
     </application>
     ```

### 7. **Error Handling and Debugging**
   - Add detailed logging in the `onResponse` and `onFailure` methods of your Retrofit call to understand what's going wrong:
     ```java
     public void onResponse(Call<List<Item>> call, Response<List<Item>> response) {
         if (response.isSuccessful() && response.body() != null) {
             List<Item> items = response.body();
             if (!items.isEmpty()) {
                 Item item = items.get(0);
                 txt1.setText(item.getItemName());
                 txt2.setText(item.getItemLocation().getLocationName());
             } else {
                 txt1.setText("No items found");
                 txt2.setText("");
             }
         } else {
             Log.e("API Error", "Code: " + response.code());
             Log.e("API Error", "Message: " + response.message());
             txt1.setText("Error");
             txt2.setText("No data received");
         }
     }

     public void onFailure(Call<List<Item>> call, Throwable t) {
         Log.e("API Failure", t.getMessage());
         txt1.setText("Error");
         txt2.setText("No data received");
     }
     ```

### 8. **Check for API Response Format**
   - Make sure that the response JSON structure matches what Retrofit is expecting based on your model class.

### 9. **Network Security Configuration**
   - If you encounter the `CLEARTEXT communication not permitted by network security policy` error again, revisit the `Network Security Configuration` to ensure proper setup. This can be particularly important if your server is accessible via HTTP and not HTTPS.

### 10. **Test with a Simple API Call**
   - Start by testing with a simpler API call to see if the communication between your Android app and the Django API works. For example, try fetching a single `Location` or a single `Item` first.

## JSON Object Error Issue
[Table of Contents](#table-of-contents)<br>
The error `java.lang.IllegalStateException: Expected BEGIN_OBJECT but was NUMBER at line 1 column 69 path $[0].itemLocation` indicates that your app is expecting a JSON object for the `itemLocation` field, but it's receiving a number instead. This typically happens when the JSON structure returned by the server does not match the structure expected by your Android model classes.

### Steps to Resolve the Error:

1. **Verify the JSON Structure Returned by the API:**
   - Check the actual JSON response from your Django server when you hit the `/item/` endpoint. Specifically, look at the `itemLocation` field.
   - If `itemLocation` is represented as an integer (likely a foreign key ID) instead of a JSON object, that would cause this error.

   **Example of JSON that might be causing the issue:**
   ```json
   [
       {
           "itemName": "Item1",
           "date_added": "2024-08-09",
           "itemLocation": 1  // This is a number, not an object
       }
   ]
   ```

   **Expected JSON format for your current Android model:**
   ```json
   [
       {
           "itemName": "Item1",
           "date_added": "2024-08-09",
           "itemLocation": {
               "locationName": "Location1"
           }
       }
   ]
   ```

2. **Adjust Django API Serialization:**
   - If `itemLocation` is currently serialized as a number (e.g., just the ID of the `Location`), you'll need to adjust the Django serialization to include the full `Location` object.

   **Django Serializer Example:**
   ```python
   from rest_framework import serializers
   from .models import Item, Location

   class LocationSerializer(serializers.ModelSerializer):
       class Meta:
           model = Location
           fields = ['locationName']  # Include the necessary fields

   class ItemSerializer(serializers.ModelSerializer):
       itemLocation = LocationSerializer()  # Serialize the related Location object

       class Meta:
           model = Item
           fields = ['itemName', 'date_added', 'itemLocation']
   ```

   - Use this serializer in your Django views to ensure that the `itemLocation` field is serialized as a nested object rather than just an ID.

3. **Alternative: Adjust Android Model to Match the API Response:**
   - If you prefer to keep `itemLocation` as just an ID in the JSON response, you'll need to adjust your Android model accordingly:

   **Updated `Item` Class in Android:**
   ```java
   public class Item {

       @SerializedName("itemName")
       private String itemName;

       @SerializedName("date_added")
       private Date dateAdded;

       @SerializedName("itemLocation")
       private int itemLocationId;  // Use an integer instead of Location object

       // Getters and setters
       public String getItemName() {
           return itemName;
       }

       public void setItemName(String itemName) {
           this.itemName = itemName;
       }

       public Date getDateAdded() {
           return dateAdded;
       }

       public void setDateAdded(Date dateAdded) {
           this.dateAdded = dateAdded;
       }

       public int getItemLocationId() {
           return itemLocationId;
       }

       public void setItemLocationId(int itemLocationId) {
           this.itemLocationId = itemLocationId;
       }
   }
   ```

   **Note:** With this approach, you'd lose the nested `Location` data unless you make another API call to fetch it.

### Recommendation:

The preferred approach is to update your Django API serialization to return the full `Location` object for `itemLocation`, matching your current Android model structure. This gives you more flexibility and ensures that the data is ready for use in the Android app without requiring additional API calls.

## Communication not permitted Error Issue
[Table of Contents](#table-of-contents)<br>

The error `CLEARTEXT communication to [my localhost IP] not permitted by network security policy` indicates that your app is trying to make an insecure (non-HTTPS) network request, which is blocked by default due to security policies.

To resolve this, you need to allow cleartext traffic for your development purposes. Here's how you can do it:

### 1. Update `AndroidManifest.xml`

You need to create or update your `network_security_config.xml` and reference it in your `AndroidManifest.xml`.

#### `AndroidManifest.xml`

```xml
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="com.example.myjapanese">

    <application
        android:networkSecurityConfig="@xml/network_security_config"
        ... >
        ...
    </application>

    <uses-permission android:name="android.permission.INTERNET" />
</manifest>
```

### 2. Create `network_security_config.xml`

Create a new XML resource file under `res/xml` directory named `network_security_config.xml` with the following content:

#### `res/xml/network_security_config.xml`

```xml
<?xml version="1.0" encoding="utf-8"?>
<network-security-config>
    <domain-config cleartextTrafficPermitted="true">
        <domain includeSubdomains="true">[YOUR_LOCAL_IP]</domain>
    </domain-config>
</network-security-config>
```

Replace `[YOUR_LOCAL_IP]` with your actual local IP address (e.g., `192.168.1.100`).

### Full Example

#### `AndroidManifest.xml`

```xml
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="com.example.myjapanese">

    <application
        android:networkSecurityConfig="@xml/network_security_config"
        android:allowBackup="true"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:roundIcon="@mipmap/ic_launcher_round"
        android:supportsRtl="true"
        android:theme="@style/AppTheme">
        <activity android:name=".MainActivity">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />

                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
    </application>

    <uses-permission android:name="android.permission.INTERNET" />
</manifest>
```

#### `res/xml/network_security_config.xml`

```xml
<?xml version="1.0" encoding="utf-8"?>
<network-security-config>
    <domain-config cleartextTrafficPermitted="true">
        <domain includeSubdomains="true">192.168.1.100</domain> <!-- Replace with your local IP -->
    </domain-config>
</network-security-config>
```

### Restart Your Application

After making these changes, rebuild and restart your application. Your app should now be able to make cleartext (HTTP) requests to your local Django server.

### Note

These changes should be used only for development purposes. For production, always use HTTPS to ensure secure communication between your app and your server.
