package com.example.myjapanese;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
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

    private Spinner locationSpinner;
    private List<Location> locationsList;
    private LocationAdapter locationAdapter;

    private TextView selectedDateText;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        selectedDateText = findViewById(R.id.selected_date_text);

        selectedDateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });



        locationSpinner = findViewById(R.id.location_spinner);
        locationsList = new ArrayList<>();
//        locationAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new ArrayList<>());
//        locationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        locationSpinner.setAdapter(locationAdapter);

        locationAdapter = new LocationAdapter(this, android.R.layout.simple_spinner_item, locationsList);
        locationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        locationSpinner.setAdapter(locationAdapter);


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
                int selectedPosition = locationSpinner.getSelectedItemPosition();
                Location selectedLocation = locationAdapter.getItem(selectedPosition);
                int selectedLocationId = selectedLocation.getId();

                int locationId = selectedLocationId;  // Or get this from the user input
                if (!itemName.isEmpty()) {
                    addItem(itemName, locationId);
                } else {
                    Toast.makeText(MainActivity.this, "Please fill in the item name", Toast.LENGTH_SHORT).show();
                }
            }
        });

        fetchLocations();
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

    //    private void fetchLocations() {
    //        Call<List<Location>> call = apiService.getLocations();
    //        call.enqueue(new Callback<List<Location>>() {
    //            @Override
    //            public void onResponse(Call<List<Location>> call, Response<List<Location>> response) {
    //                if (response.isSuccessful() && response.body() != null) {
    //                    locationsList = response.body();
    //                    List<String> locationNames = new ArrayList<>();
    //
    //                    for (Location location : locationsList) {
    //                        locationNames.add(location.getLocationName());
    //                    }
    //
    //                    // Update Spinner with location names
    //                    locationAdapter.clear();
    //                    locationAdapter.addAll(locationNames);
    //                    locationAdapter.notifyDataSetChanged();
    //                } else {
    //                    Toast.makeText(MainActivity.this, "Failed to load locations", Toast.LENGTH_SHORT).show();
    //                }
    //            }
    //
    //            @Override
    //            public void onFailure(Call<List<Location>> call, Throwable t) {
    //                Toast.makeText(MainActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
    //            }
    //        });
    //    }
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
                        String selectedDate = dayOfMonth + "-" + (month + 1) + "-" + year;
                        selectedDateText.setText(selectedDate);
                    }
                },
                year, month, day);

        datePickerDialog.show();
    }

}
