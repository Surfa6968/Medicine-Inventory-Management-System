package com.example.mediremind;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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

        btnSave.setOnClickListener(v -> {

            String name = etName.getText().toString().trim();
            String expiry = etExpiry.getText().toString().trim();

            if(name.isEmpty()){
                etName.setError("Enter name");
                return;
            }

            int quantity = Integer.parseInt(etQuantity.getText().toString());
            double price = Double.parseDouble(etPrice.getText().toString());

            Medicine medicine = new Medicine(name, quantity, price, expiry);

            MedicineStore.medicineList.add(medicine);

            Toast.makeText(this,"Medicine Added",Toast.LENGTH_SHORT).show();

            finish();
        });
    }
}