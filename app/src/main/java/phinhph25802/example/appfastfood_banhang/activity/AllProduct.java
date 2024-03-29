package phinhph25802.example.appfastfood_banhang.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;


import java.util.ArrayList;
import java.util.List;



public class AllProduct extends AppCompatActivity {
    GridView grv;
    EditText edSearch;
    ImageView btnBack;
    LinearLayout ll_nopro;
    CategoryDetailAdapter adapter;
    List<Product> list;
    String idCat;
    Context context;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_product);
        btnBack = findViewById(R.id.btn_back_all_product);
        grv = findViewById(R.id.grv_all_detail);
        edSearch = findViewById(R.id.ed_search_all_product);
        ll_nopro = findViewById(R.id.no_product);
        context = this;
        list = new ArrayList<>();
        adapter = new CategoryDetailAdapter(context);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        getListProduct();

        edSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myref = database.getReference("products");
                myref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        list.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Product product = new Product();
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                for (DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()) {
                                    idCat = dataSnapshot2.getKey();
                                }
                            }
                            if (dataSnapshot.child("tensp").getValue(String.class).contains(editable)) {
                                product.setId(dataSnapshot.getKey());
                                product.setId_theloai(idCat);
                                product.setTensp(dataSnapshot.child("tensp").getValue(String.class));
                                product.setGiasp(dataSnapshot.child("giasp").getValue(Integer.class));
                                product.setMota(dataSnapshot.child("mota").getValue(String.class));
                                product.setImage(dataSnapshot.child("image").getValue(String.class));
                                list.add(product);
                                adapter.setData(list);
                                grv.setAdapter(adapter);
                                ll_nopro.setVisibility(View.INVISIBLE);
                            } else if (list.size() == 0) {
                                ll_nopro.setVisibility(View.VISIBLE);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.i("listsp", "onCancelled: " + error.toString());
                    }
                });
            }
        });
    }

    private void getListProduct() {
        list.clear();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myref = database.getReference("products");
        myref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
             list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Product product = new Product();
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        for (DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()) {
                            String idCat = dataSnapshot2.getKey();
                            product.setId_theloai(idCat);
                        }
                    }
                    product.setId(dataSnapshot.getKey());
                    product.setTensp(dataSnapshot.child("tensp").getValue(String.class));
                    product.setGiasp(dataSnapshot.child("giasp").getValue(Integer.class));
                    product.setMota(dataSnapshot.child("mota").getValue(String.class));
                    product.setImage(dataSnapshot.child("image").getValue(String.class));

                    list.add(product);
                    Log.i("sai", "onDataChange: "+list.size());
                }

                grv.setAdapter(adapter);
                adapter.setData(list);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i("listsp", "onCancelled: " + error.toString());
            }
        });
    }
}