package com.example.mediremind;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Button btnAdd, btnView;
    private TextView txtTotalMedicines, txtLowStock;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseHelper = new DatabaseHelper(this);

        btnAdd = findViewById(R.id.btnAddMedicine);
        btnView = findViewById(R.id.btnViewMedicines);
        txtTotalMedicines = findViewById(R.id.txtTotalMedicines);
        txtLowStock = findViewById(R.id.txtLowStock);

        btnAdd.setOnClickListener(v ->
                startActivity(new Intent(
                        MainActivity.this,
                        AddMedicineActivity.class
                ))
        );

        btnView.setOnClickListener(v ->
                startActivity(new Intent(
                        MainActivity.this,
                        MedicineListActivity.class
                ))
        );
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshDashboard();
    }

    private void refreshDashboard() {
        txtTotalMedicines.setText(
                String.valueOf(databaseHelper.getMedicineCount())
        );

        txtLowStock.setText(
                String.valueOf(databaseHelper.getLowStockCount())
        );
    }

    @Override
    protected void onDestroy() {
        databaseHelper.close();
        super.onDestroy();
    }
}