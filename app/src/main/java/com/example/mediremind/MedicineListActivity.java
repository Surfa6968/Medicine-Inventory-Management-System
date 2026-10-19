package com.example.mediremind;

import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Locale;

public class MedicineListActivity extends AppCompatActivity {

    private EditText etSearch;
    private LinearLayout listContainer;
    private DatabaseHelper databaseHelper;

    private final int green = Color.rgb(35, 130, 90);
    private final int dark = Color.rgb(40, 55, 50);
    private final int grey = Color.rgb(105, 120, 115);
    private final int paleGreen = Color.rgb(235, 247, 240);
    private final int red = Color.rgb(190, 55, 55);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_list);

        databaseHelper = new DatabaseHelper(this);

        etSearch = findViewById(R.id.etSearch);
        listContainer = findViewById(R.id.listContainer);

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(
                    CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(
                    CharSequence s, int start, int before, int count) {
                display(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        display("");
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (etSearch != null) {
            display(etSearch.getText().toString());
        }
    }

    private void display(String keyword) {
        listContainer.removeAllViews();

        ArrayList<Medicine> medicines =
                databaseHelper.getMedicines(keyword);

        if (medicines.isEmpty()) {
            TextView empty = new TextView(this);
            empty.setText(keyword == null || keyword.trim().isEmpty()
                    ? "No medicines added yet."
                    : "No matching medicines found.");
            empty.setTextColor(grey);
            empty.setTextSize(16);
            empty.setGravity(Gravity.CENTER);
            empty.setPadding(dp(16), dp(28), dp(16), dp(28));
            listContainer.addView(empty);
            return;
        }

        for (Medicine medicine : medicines) {
            addMedicineCard(medicine, keyword);
        }
    }

    private void addMedicineCard(Medicine medicine, String keyword) {
        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setPadding(dp(16), dp(14), dp(16), dp(14));
        card.setBackground(background(
                Color.WHITE,
                medicine.getQuantity() <= 10 ? red : Color.rgb(220, 230, 224),
                16
        ));

        LinearLayout.LayoutParams cardParams =
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );
        cardParams.bottomMargin = dp(12);
        listContainer.addView(card, cardParams);

        TextView name = new TextView(this);
        name.setText(medicine.getName());
        name.setTextSize(19);
        name.setTypeface(null, Typeface.BOLD);
        name.setTextColor(dark);
        card.addView(name);

        addDetail(card, "Quantity: " + medicine.getQuantity(),
                medicine.getQuantity() <= 10 ? red : dark);

        addDetail(card, String.format(
                Locale.getDefault(), "Price: LKR %.2f", medicine.getPrice()
        ), dark);

        addDetail(card, "Expiry date: " +
                (medicine.getExpiryDate() == null ||
                        medicine.getExpiryDate().trim().isEmpty()
                        ? "Not provided" : medicine.getExpiryDate()), dark);

        if (medicine.getQuantity() <= 10) {
            TextView warning = new TextView(this);
            warning.setText("⚠ Low stock — please check inventory");
            warning.setTextColor(red);
            warning.setTextSize(13);
            warning.setPadding(0, dp(6), 0, dp(6));
            card.addView(warning);
        }

        LinearLayout actions = new LinearLayout(this);
        actions.setOrientation(LinearLayout.HORIZONTAL);
        actions.setGravity(Gravity.END);
        LinearLayout.LayoutParams actionParams =
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );
        actionParams.topMargin = dp(10);
        card.addView(actions, actionParams);

        Button update = new Button(this);
        update.setText("Update");
        update.setTextColor(Color.WHITE);
        update.setTextSize(13);
        update.setTransformationMethod(null);
        update.setBackground(background(green, green, 10));
        actions.addView(update);

        Button delete = new Button(this);
        delete.setText("Delete");
        delete.setTextColor(Color.WHITE);
        delete.setTextSize(13);
        delete.setTransformationMethod(null);
        delete.setBackground(background(red, red, 10));

        LinearLayout.LayoutParams deleteParams =
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );
        deleteParams.leftMargin = dp(8);
        actions.addView(delete, deleteParams);

        update.setOnClickListener(v -> showUpdateDialog(medicine, keyword));
        delete.setOnClickListener(v -> confirmDelete(medicine, keyword));
    }

    private void addDetail(LinearLayout card, String text, int color) {
        TextView detail = new TextView(this);
        detail.setText(text);
        detail.setTextColor(color);
        detail.setTextSize(14);
        detail.setPadding(0, dp(5), 0, 0);
        card.addView(detail);
    }

    private void showUpdateDialog(Medicine medicine, String keyword) {
        EditText input = new EditText(this);
        input.setInputType(
                android.text.InputType.TYPE_CLASS_NUMBER
        );
        input.setHint("Enter new quantity");
        input.setText(String.valueOf(medicine.getQuantity()));
        input.setSelectAllOnFocus(true);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Update Quantity")
                .setMessage("Enter the new quantity for " + medicine.getName())
                .setView(input)
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Save", null)
                .create();

        dialog.setOnShowListener(ignored ->
                dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                        .setOnClickListener(v -> {
                            String value = input.getText().toString().trim();

                            if (value.isEmpty()) {
                                input.setError("Enter a quantity");
                                return;
                            }

                            try {
                                int quantity = Integer.parseInt(value);

                                if (quantity < 0) {
                                    input.setError(
                                            "Quantity cannot be negative"
                                    );
                                    return;
                                }

                                int result = databaseHelper.updateQuantity(
                                        medicine.getId(), quantity
                                );

                                if (result > 0) {
                                    Toast.makeText(
                                            this,
                                            "Quantity updated",
                                            Toast.LENGTH_SHORT
                                    ).show();
                                    dialog.dismiss();
                                    display(keyword);
                                } else {
                                    Toast.makeText(
                                            this,
                                            "Could not update medicine",
                                            Toast.LENGTH_SHORT
                                    ).show();
                                }

                            } catch (NumberFormatException e) {
                                input.setError("Enter a valid quantity");
                            }
                        })
        );

        dialog.show();
    }

    private void confirmDelete(Medicine medicine, String keyword) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Medicine")
                .setMessage("Delete " + medicine.getName() + "?")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Delete", (dialog, which) -> {
                    int result = databaseHelper.deleteMedicine(
                            medicine.getId()
                    );

                    if (result > 0) {
                        Toast.makeText(
                                this,
                                "Medicine deleted",
                                Toast.LENGTH_SHORT
                        ).show();
                        display(keyword);
                    } else {
                        Toast.makeText(
                                this,
                                "Could not delete medicine",
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                })
                .show();
    }

    private GradientDrawable background(
            int color, int strokeColor, int radiusDp) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(color);
        drawable.setCornerRadius(dp(radiusDp));
        drawable.setStroke(dp(1), strokeColor);
        return drawable;
    }

    private int dp(float value) {
        return (int) (value * getResources()
                .getDisplayMetrics().density + 0.5f);
    }

    @Override
    protected void onDestroy() {
        databaseHelper.close();
        super.onDestroy();
    }
}