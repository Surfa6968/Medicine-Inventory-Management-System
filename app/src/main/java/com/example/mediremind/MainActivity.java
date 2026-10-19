
package com.example.mediremind;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    Button btnAdd, btnView;
    TextView txtTotalMedicines, txtLowStock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(
                findViewById(R.id.main), (v, insets) -> {

                    Insets systemBars = insets.getInsets(
                            WindowInsetsCompat.Type.systemBars()
                    );

                    v.setPadding(
                            systemBars.left,
                            systemBars.top,
                            systemBars.right,
                            systemBars.bottom
                    );

                    return insets;
                }
        );

        // Buttons
        btnAdd = findViewById(R.id.btnAddMedicine);
        btnView = findViewById(R.id.btnViewMedicines);

        // Dashboard statistics
        txtTotalMedicines =
                findViewById(R.id.txtTotalMedicines);

        txtLowStock =
                findViewById(R.id.txtLowStock);

        // Open Add Medicine page
        btnAdd.setOnClickListener(v -> {
            startActivity(
                    new Intent(MainActivity.this,
                            AddMedicineActivity.class)
            );
        });

        // Open Medicine Inventory page
        btnView.setOnClickListener(v -> {
            startActivity(
                    new Intent(MainActivity.this,
                            MedicineListActivity.class)
            );
        });

        // Temporary values until connected to your database
        txtTotalMedicines.setText("0");
        txtLowStock.setText("0");
    }
}
