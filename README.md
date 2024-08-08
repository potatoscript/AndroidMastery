# AndroidMastery

Welcome to **AndroidMastery**, a comprehensive project designed to help you master Android App through hands-on learning and practical application. This repository serves as a learning path for developers of all skill levels to deepen their understanding of Android app, covering everything from basic concepts to advanced techniques.

## Table of Contents

- [Introduction](#introduction)
- [Connect Android to Django API](#connect-android-to-django-api)
- [Resolve Security Policies Issue](#resolve-security-policies-issue)

## Introduction
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

## Resolve Security Policies Issue
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
