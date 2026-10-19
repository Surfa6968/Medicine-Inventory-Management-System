
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

    EditText etName, etQuantity, etPrice, etExpiry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_medicine);

        etName = findViewById(R.id.etName);
        etQuantity = findViewById(R.id.etQuantity);
        etPrice = findViewById(R.id.etPrice);
        etExpiry = findViewById(R.id.etExpiry);

        Button btnSave = findViewById(R.id.btnSave);

        // Open date picker when expiry date is selected
        etExpiry.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();

            DatePickerDialog picker = new DatePickerDialog(
                    AddMedicineActivity.this,
                    (view, year, month, day) -> {
                        String date = String.format(
                                Locale.US,
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
        });

        // Save medicine
        btnSave.setOnClickListener(v -> {

            String name = etName.getText().toString().trim();
            String quantityText =
                    etQuantity.getText().toString().trim();
            String priceText =
                    etPrice.getText().toString().trim();
            String expiry =
                    etExpiry.getText().toString().trim();

            // Validate medicine name
            if (name.isEmpty()) {
                etName.setError("Enter medicine name");
                etName.requestFocus();
                return;
            }

            // Validate quantity
            if (quantityText.isEmpty()) {
                etQuantity.setError("Enter quantity");
                etQuantity.requestFocus();
                return;
            }

            // Validate price
            if (priceText.isEmpty()) {
                etPrice.setError("Enter unit price");
                etPrice.requestFocus();
                return;
            }

            // Validate expiry date
            if (expiry.isEmpty()) {
                etExpiry.setError("Select expiry date");
                Toast.makeText(
                        this,
                        "Please select an expiry date",
                        Toast.LENGTH_SHORT
                ).show();
                return;
            }

            int quantity;
            double price;

            try {
                quantity = Integer.parseInt(quantityText);
                price = Double.parseDouble(priceText);
            } catch (NumberFormatException e) {
                Toast.makeText(
                        this,
                        "Enter valid quantity and price",
                        Toast.LENGTH_SHORT
                ).show();
                return;
            }

            if (quantity <= 0) {
                etQuantity.setError("Quantity must be greater than zero");
                etQuantity.requestFocus();
                return;
            }

            if (price < 0) {
                etPrice.setError("Price cannot be negative");
                etPrice.requestFocus();
                return;
            }

            // Create and store medicine
            Medicine medicine = new Medicine(
                    name,
                    quantity,
                    price,
                    expiry
            );

            MedicineStore.medicineList.add(medicine);

            Toast.makeText(
                    this,
                    "Medicine added successfully!",
                    Toast.LENGTH_SHORT
            ).show();

            finish();
        });
    }
}
