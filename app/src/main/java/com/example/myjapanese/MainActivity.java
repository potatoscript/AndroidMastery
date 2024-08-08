package com.example.myjapanese;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

    private TextView txt1, txt2;
    private Button fetchButton;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txt1 = findViewById(R.id.txt1);
        txt2 = findViewById(R.id.txt2);
        fetchButton = findViewById(R.id.fetch_button);

        // Initialize Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.213.137:8000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);

        fetchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchItems();
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
                        txt2.setText(item.getItemLocation().getLocationName());
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
