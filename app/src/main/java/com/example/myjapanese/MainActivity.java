package com.example.myjapanese;

import android.annotation.SuppressLint;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private TextView txt1, txt2;
    private Button fetchButton;
    private ApiService apiService;
    private Button addButton;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txt1 = findViewById(R.id.txt1);
        txt2 = findViewById(R.id.txt2);
        fetchButton = findViewById(R.id.fetch_button);
        addButton = findViewById(R.id.add_btn);

        // Initialize Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.91.137:8000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);

        fetchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchItems();
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
//            public void onClick(View v) {
//                String itemName = txt1.getText().toString();
//                String dateAdded = "2024-08-19";//txt2.getText().toString();
//                if (!itemName.isEmpty() && !dateAdded.isEmpty()) {
//                    addItem(itemName, dateAdded, 1);
//                } else {
//                    Toast.makeText(MainActivity.this, "Please fill in both fields", Toast.LENGTH_SHORT).show();
//                }
//            }
            public void onClick(View v) {
                String itemName = txt1.getText().toString();
                int locationId = 1;  // Or get this from the user input
                if (!itemName.isEmpty()) {
                    addItem(itemName, locationId);
                } else {
                    Toast.makeText(MainActivity.this, "Please fill in the item name", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void addItem(String itemName, int itemLocationId) {
        Item item = new Item(itemName, itemLocationId);

        Call<Item> call = apiService.createItem(item);
        call.enqueue(new Callback<Item>() {
            @Override
            public void onResponse(Call<Item> call, Response<Item> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "Item added successfully!", Toast.LENGTH_SHORT).show();
                    txt1.setText("");
                } else {
                    try {
                        String errorBody = response.errorBody().string();
                        Toast.makeText(MainActivity.this, "Failed to add item: " + response.code() + " " + errorBody, Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(MainActivity.this, "Failed to add item and error body could not be parsed", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Item> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }





    private void fetchItems() {
        Call<List<Item>> call = apiService.getItems();
        call.enqueue(new Callback<List<Item>>() {
            @Override
            public void onResponse(Call<List<Item>> call, Response<List<Item>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Item> items = response.body();
                    // Assuming you want to display the first item
                    if (!items.isEmpty()) {
                        Item item = items.get(0);
                        txt1.setText(item.getItemName());
                        //txt2.setText(item.getItemLocation().getLocationName());
                    } else {
                        txt1.setText("No items found");
                        txt2.setText("");
                    }
                } else {
                    txt1.setText("Error");
                    txt2.setText("No data received");
                }
            }

            @Override
            public void onFailure(Call<List<Item>> call, Throwable t) {
                String a = t.getMessage();
                Toast.makeText(MainActivity.this, "ErrorX: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
