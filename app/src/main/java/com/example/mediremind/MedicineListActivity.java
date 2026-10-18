package com.example.mediremind;

import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MedicineListActivity extends AppCompatActivity {

    LinearLayout listContainer;
    EditText etSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_list);

        listContainer = findViewById(R.id.listContainer);
        etSearch = findViewById(R.id.etSearch);

        display("");

        etSearch.addTextChangedListener(new android.text.TextWatcher() {
            public void beforeTextChanged(CharSequence s,int a,int b,int c){}
            public void onTextChanged(CharSequence s,int a,int b,int c){
                display(s.toString());
            }
            public void afterTextChanged(android.text.Editable s){}
        });
    }

    private void display(String keyword){

        listContainer.removeAllViews();

        for(Medicine medicine:MedicineStore.medicineList){

            if(!medicine.getName().toLowerCase().contains(keyword.toLowerCase()))
                continue;

            LinearLayout card=new LinearLayout(this);
            card.setOrientation(LinearLayout.VERTICAL);
            card.setPadding(25,25,25,25);

            TextView tv=new TextView(this);
            tv.setTypeface(null, Typeface.BOLD);
            tv.setText(
                    medicine.getName()+"\n"+
                            "Qty: "+medicine.getQuantity()+"\n"+
                            "Price: "+medicine.getPrice()+"\n"+
                            "Expiry: "+medicine.getExpiryDate()+"\n"+
                            "Total: "+medicine.getTotalValue()
            );

            Button update=new Button(this);
            update.setText("Update");

            Button delete=new Button(this);
            delete.setText("Delete");

            update.setOnClickListener(v->{
                medicine.setQuantity(medicine.getQuantity()+1);
                display(keyword);
            });

            delete.setOnClickListener(v->{
                MedicineStore.medicineList.remove(medicine);
                display(keyword);
            });

            card.addView(tv);
            card.addView(update);
            card.addView(delete);

            listContainer.addView(card);
        }
    }
}