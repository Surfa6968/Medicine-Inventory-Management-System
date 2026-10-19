package com.example.mediremind;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.Locale;

public class AddMedicineActivity extends AppCompatActivity {

    private EditText etName, etQuantity, etPrice, etExpiry;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_medicine);

        databaseHelper = new DatabaseHelper(this);

        etName = findViewById(R.id.etName);
        etQuantity = findViewById(R.id.etQuantity);
        etPrice = findViewById(R.id.etPrice);
        etExpiry = findViewById(R.id.etExpiry);
        Button btnSave = findViewById(R.id.btnSave);

        etExpiry.setFocusable(false);
        etExpiry.setClickable(true);
        etExpiry.setOnClickListener(v -> showDatePicker());

        btnSave.setOnClickListener(v -> saveMedicine());
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();

        DatePickerDialog picker = new DatePickerDialog(
                this,
                (view, year, month, day) -> {
                    String date = String.format(
                            Locale.getDefault(),
                            "%04d-%02d-%02d",
                            year, month + 1, day
                    );
                    etExpiry.setText(date);
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );

        picker.show();
    }

    private void saveMedicine() {
        String name = etName.getText().toString().trim();
        String quantityText = etQuantity.getText().toString().trim();
        String priceText = etPrice.getText().toString().trim();
        String expiry = etExpiry.getText().toString().trim();

        if (name.isEmpty()) {
            etName.setError("Enter medicine name");
            etName.requestFocus();
            return;
        }

        if (quantityText.isEmpty()) {
            etQuantity.setError("Enter quantity");
            etQuantity.requestFocus();
            return;
        }

        if (priceText.isEmpty()) {
            etPrice.setError("Enter price");
            etPrice.requestFocus();
            return;
        }

        try {
            int quantity = Integer.parseInt(quantityText);
            double price = Double.parseDouble(priceText);

            if (quantity < 0) {
                etQuantity.setError("Quantity cannot be negative");
                return;
            }

            if (!Double.isFinite(price) || price < 0) {
                etPrice.setError("Enter a valid price");
                return;
            }

            Medicine medicine =
                    new Medicine(name, quantity, price, expiry);

            long result = databaseHelper.insertMedicine(medicine);

            if (result != -1) {
                Toast.makeText(
                        this,
                        "Medicine saved successfully",
                        Toast.LENGTH_SHORT
                ).show();
                finish();
            } else {
                Toast.makeText(
                        this,
                        "Unable to save medicine",
                        Toast.LENGTH_SHORT
                ).show();
            }

        } catch (NumberFormatException e) {
            Toast.makeText(
                    this,
                    "Enter a valid quantity and price",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }

    @Override
    protected void onDestroy() {
        databaseHelper.close();
        super.onDestroy();
    }
}