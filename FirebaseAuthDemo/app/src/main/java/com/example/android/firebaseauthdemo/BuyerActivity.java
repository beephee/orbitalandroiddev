package com.example.android.firebaseauthdemo;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.R.attr.id;
import static android.R.attr.name;
import static android.media.CamcorderProfile.get;
import static com.example.android.firebaseauthdemo.R.id.dateValue;
import static com.example.android.firebaseauthdemo.R.id.editTextProductCoords;
import static com.example.android.firebaseauthdemo.R.id.editTextProductDate;
import static com.example.android.firebaseauthdemo.R.id.editTextProductLength;
import static com.example.android.firebaseauthdemo.R.id.editTextProductName;
import static com.example.android.firebaseauthdemo.R.id.editTextProductName;

public class BuyerActivity extends AppCompatActivity {

    DatabaseReference databaseProducts;
    ListView listViewProducts;
    List<Product> productList;
    String userEmail;
    private static final String TAG = "BuyerActivity";
    private TextView mDisplayDate;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    String date;
    String newDate;
    String getCoords;
    Spinner spinnerProductType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer);

        databaseProducts = FirebaseDatabase.getInstance().getReference("products");

        listViewProducts = (ListView) findViewById(R.id.listViewProducts);

        listViewProducts.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Product product = productList.get(i);
                showUpdateDeleteDialog(product.getProductId(), product.getProductBuyer(), product.getProductCourier(), product.getProductName(), product.getProductType(), product.getProductCoords(), product.getLength(), product.getWidth(), product.getHeight(), product.getWeight(), product.getPrice(), product.getDate());
                return true;
            }
        });

        productList = new ArrayList<>();

        //Grabs email string from previous activity
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            userEmail = extras.getString("email");
        }

    }

    @Override
    protected void onStart() {
        super.onStart();

        databaseProducts.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                productList.clear();

                for(DataSnapshot productSnapshot : dataSnapshot.getChildren()){
                    Product product = productSnapshot.getValue(Product.class);
                    //Filter results to show only products by the user
                    String email = product.getProductBuyer();
                    if(email.equals(userEmail)){
                        productList.add(product);
                    }
                }

                ProductListBuyer adapter = new ProductListBuyer(BuyerActivity.this, productList);
                listViewProducts.setAdapter(adapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private boolean updateProduct(String productId, String productBuyer, String productCourier, String productName, String productType, String productCoords, String length, String width, String height, String weight, String price, String date) {
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("products").child(productId);
        Product product = new Product(productId, productBuyer, productCourier, productName, productType, productCoords, length, width, height, weight, price, date);
        dR.setValue(product);
        Toast.makeText(getApplicationContext(), "Product Updated", Toast.LENGTH_LONG).show();
        return true;
    }

    private void showUpdateDeleteDialog(final String productId, final String productBuyer, final String productCourier, final String productName, final String productType, final String productCoords, final String length, final String width, final String height, final String weight, final String price, final String date) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.update_dialog, null);
        dialogBuilder.setView(dialogView);

        final EditText editTextProductName = (EditText) dialogView.findViewById(R.id.editTextProductName);
        editTextProductName.setText(productName);
        spinnerProductType = (Spinner) dialogView.findViewById(R.id.spinnerProductType);

        //final EditText editTextProductType = (EditText) dialogView.findViewById(R.id.editTextProductType);
        //editTextProductType.setText(productType);
        final EditText editTextProductLength = (EditText) dialogView.findViewById(R.id.editTextProductLength);
        editTextProductLength.setText(length);
        final EditText editTextProductWidth = (EditText) dialogView.findViewById(R.id.editTextProductWidth);
        editTextProductWidth.setText(width);
        final EditText editTextProductHeight = (EditText) dialogView.findViewById(R.id.editTextProductHeight);
        editTextProductHeight.setText(height);
        final EditText editTextProductWeight = (EditText) dialogView.findViewById(R.id.editTextProductWeight);
        editTextProductWeight.setText(weight);
        final EditText editTextProductPrice = (EditText) dialogView.findViewById(R.id.editTextProductPrice);
        editTextProductPrice.setText(price);
        final TextView showDatePicker = (TextView) dialogView.findViewById(R.id.editTextProductDate);
        final EditText editTextProductCoords = (EditText) dialogView.findViewById(R.id.editTextProductCoords);
        editTextProductCoords.setText(productCoords);
        final Button buttonUpdate = (Button) dialogView.findViewById(R.id.buttonUpdateProduct);
        final Button buttonDelete = (Button) dialogView.findViewById(R.id.buttonDeleteProduct);

        dialogBuilder.setTitle(productName);
        final AlertDialog b = dialogBuilder.create();
        b.show();

        showDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeDate();
                showDatePicker.setText(newDate);
            }
        });

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newName = editTextProductName.getText().toString();
                String newType = spinnerProductType.getSelectedItem().toString();
                String newLength = editTextProductLength.getText().toString();
                String newWidth = editTextProductWidth.getText().toString();
                String newHeight = editTextProductHeight.getText().toString();
                String newPrice = editTextProductPrice.getText().toString();
                String newWeight = editTextProductWeight.getText().toString();
                String newCoords = editTextProductCoords.getText().toString();
                if (!TextUtils.isEmpty(newName) && !TextUtils.isEmpty(newType) && !TextUtils.isEmpty(newLength) && !TextUtils.isEmpty(newWidth) && !TextUtils.isEmpty(newHeight) && !TextUtils.isEmpty(newWeight) && !TextUtils.isEmpty(newPrice) && !TextUtils.isEmpty(newDate) && !TextUtils.isEmpty(newCoords)) {
                    updateProduct(productId, productBuyer, productCourier, newName, newType, newCoords, newLength, newWidth, newHeight, newWeight, newPrice, newDate);
                    b.dismiss();
                }
                else{
                    Toast.makeText(getApplicationContext(), "Please ensure all fields are completed.", Toast.LENGTH_LONG).show();
                }
            }
        });

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteProduct(productId);
                b.dismiss();
            }
        });
    }

    private boolean deleteProduct(String productId) {
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("products").child(productId);
        dR.removeValue();
        Toast.makeText(getApplicationContext(), "Product Deleted", Toast.LENGTH_LONG).show();
        return true;
    }

    //Goto Courier Dashboard
    public void viewAddRequest(View view)
    {
        Intent intent = new Intent(this, AddRequestActivity.class);
        //Pass the email string to next activity
        intent.putExtra("email", userEmail);
        startActivity(intent);
    }

    public void goCourierPage(View view)
    {
        Intent intent = new Intent(this, CourierActivity.class);
        //Pass the email string to next activity
        intent.putExtra("email", userEmail);
        startActivity(intent);
    }

    // Update date
    private void changeDate() {
        final TextView showDatePicker = (TextView) findViewById(R.id.dateValue);
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(
                BuyerActivity.this,
                android.R.style.Theme_DeviceDefault_Light_Dialog,
                mDateSetListener,
                year,month,day);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        dialog.show();

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                Log.d(TAG, "onDateSet: dd/mm/yyyy: " + day + "/" + month + "/" + year);

                newDate = day + "/" + month + "/" + year;
            }
        };
    };

    // to fix
    private void chooseCoords (View view) {
    }

}

