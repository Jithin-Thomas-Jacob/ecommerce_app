package com.example.ecommerceapp;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class activity_product extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    private List<Product> productList;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_product);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recyclerView = findViewById(R.id.viewProducts);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));

        productList = new ArrayList<>();
        productAdapter = new ProductAdapter(this, this.productList);
        recyclerView.setAdapter(productAdapter);

        db = FirebaseFirestore.getInstance();

        fetchProducts();
    }

    private void fetchProducts() {
        db.collection("products")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        productList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            List<Map<String, Object>> products = (List<Map<String, Object>>) document.get("products");
                            if (products != null) {
                                for (Map<String, Object> productMap : products) {
                                    String title = (String) productMap.get("title");
                                    String image = (String) productMap.get("image");


                                    Object priceObject = productMap.get("price");
                                    int price = 0;
                                    if (priceObject instanceof Long) {
                                        price = ((Long) priceObject).intValue();
                                    } else if (priceObject instanceof String) {
                                        try {
                                            price = Integer.parseInt((String) priceObject);
                                        } catch (NumberFormatException e) {
                                            Log.e("Firestore", "Invalid price format: " + priceObject);
                                        }
                                    }


                                    Product product = new Product(title, price, image);
                                    productList.add(product);
                                }
                            }
                        }
                        productAdapter.notifyDataSetChanged();
                    } else {
                        Log.e("Firestore", "Error fetching products: ", task.getException());
                    }
                });
    }


}

