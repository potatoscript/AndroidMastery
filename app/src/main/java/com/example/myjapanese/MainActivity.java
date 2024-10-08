package com.example.myjapanese;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private TableLayout itemTable;

    private ApiService apiService;
    private Button addButton, reloadButton;

    private Spinner locationSpinner;
    private List<Location> locationsList;
    private LocationAdapter locationAdapter;

    private TextView selectedDateText, itemNameText;

    private int selectedItemId = -1; // Variable to store the ID of the item to edit

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        itemTable = findViewById(R.id.item_table);  // Make sure item_table exists in your layout XML

        itemNameText = findViewById(R.id.item_name_text);
        selectedDateText = findViewById(R.id.date_added_text);

        selectedDateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        locationSpinner = findViewById(R.id.item_location_id_text);
        locationsList = new ArrayList<>();
        locationAdapter = new LocationAdapter(this, android.R.layout.simple_spinner_item, locationsList);
        locationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        locationSpinner.setAdapter(locationAdapter);

        addButton = findViewById(R.id.save_btn);
        reloadButton = findViewById(R.id.reload_btn);

        // Initialize Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.196.137:8000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);

        reloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearInputFields();
                fetchItems();
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String itemName = itemNameText.getText().toString();
                int selectedPosition = locationSpinner.getSelectedItemPosition();
                Location selectedLocation = locationAdapter.getItem(selectedPosition);
                int selectedLocationId = selectedLocation.getId();
                String selectedDate = selectedDateText.getText().toString();

                if (selectedItemId == -1) {
                    // Adding new item
                    if (!itemName.isEmpty()) {
                        addItem(itemName, selectedDate, selectedLocationId);
                    } else {
                        Toast.makeText(MainActivity.this, "Please fill in the item name", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Updating existing item
                    if (!itemName.isEmpty()) {
                        updateItem(selectedItemId, itemName, selectedDate, selectedLocationId);
                    } else {
                        Toast.makeText(MainActivity.this, "Please fill in the item name", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        fetchLocations();

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
                            selectedItemId = item.getId();  // Set the selected item ID for editing

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

    private void deleteItem(int itemId, final TableRow tableRow) {
        Call<Void> call = apiService.deleteItem(itemId);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    //itemTable.removeView(tableRow);
                    fetchItems();  // Refresh the table to display the newly added item
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

        // Get today's date
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String todayDate = dateFormat.format(calendar.getTime());

        // Set today's date to selectedDateText
        selectedDateText.setText(todayDate);

        locationSpinner.setSelection(0);  // Reset spinner to the first item
    }


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
                        String selectedDate = year + "-" + (month + 1) + "-" + dayOfMonth;
                        selectedDateText.setText(selectedDate);
                    }
                },
                year, month, day);

        datePickerDialog.show();
    }

}
