package phinhph25802.example.appfastfood_banhang.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import java.util.ArrayList;


public class CategoryDetail extends AppCompatActivity {
    GridView gridView;
    EditText ed_search;
    TextView title;
    ImageView btnBack;
    List<Product> mProducts;
    Context context;
    CategoryDetailAdapter categoryDetailAdapter;
    String id, keyword;
    LinearLayout id_lineaerlayout;
    FirebaseDatabase database;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_detail);
        gridView = findViewById(R.id.grv_cat_detail);
        id_lineaerlayout = findViewById(R.id.no_search);
        btnBack = findViewById(R.id.btn_back_cate_detail);
        title = findViewById(R.id.title_cate_detail);
        ed_search = findViewById(R.id.ed_search_cate_detail);

        context = this;
        mProducts = new ArrayList<>();
        categoryDetailAdapter = new CategoryDetailAdapter(context);

        database = FirebaseDatabase.getInstance();

        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        String name = intent.getStringExtra("nameCat");

        title.setText(name);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        ed_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                keyword = charSequence.toString().toLowerCase();
//                Query query = myref.orderByChild("name").startAt(keyword).endAt(keyword + "\uf8ff");
                // Lấy kết quả gợi ý

            }

            @Override
            public void afterTextChanged(Editable editable) {
                Log.i("zzzzz", "afterTextChanged: " + editable.toString());
                mProducts.clear();
                DatabaseReference myref = database.getReference("products");
                myref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                for (DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()) {
                                    String idCat = dataSnapshot2.getKey();
                                    if (idCat.equals(id)) {
                                        if (dataSnapshot.child("tensp").getValue(String.class).contains(editable)) {
                                            Product product = new Product();
                                            product.setId(dataSnapshot.getKey());
                                            product.setTensp(dataSnapshot.child("tensp").getValue(String.class));
                                            product.setGiasp(dataSnapshot.child("giasp").getValue(Integer.class));
                                            product.setMota(dataSnapshot.child("mota").getValue(String.class));
                                            product.setImage(dataSnapshot.child("image").getValue(String.class));
                                            mProducts.add(product);
                                            id_lineaerlayout.setVisibility(View.INVISIBLE);
                                        } else if (mProducts.size() == 0) {
                                            id_lineaerlayout.setVisibility(View.VISIBLE);
                                        }
                                    }
                                }
                            }
                            categoryDetailAdapter.setData(mProducts);
                            gridView.setAdapter(categoryDetailAdapter);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.d("TAG", "onCancelled: " + error.toString());
                    }
                });
            }
        });
        getListProductbyCategory();
    }

    private void getListProductbyCategory() {
        DatabaseReference myref = database.getReference("products");
        myref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        for (DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()) {
                            String idCat = dataSnapshot2.getKey();
                            if (idCat.equals(id)) {
                                Product product = new Product();
                                product.setId(dataSnapshot.getKey());
                                product.setTensp(dataSnapshot.child("tensp").getValue(String.class));
                                product.setGiasp(dataSnapshot.child("giasp").getValue(Integer.class));
                                product.setMota(dataSnapshot.child("mota").getValue(String.class));
                                product.setImage(dataSnapshot.child("image").getValue(String.class));
                                mProducts.add(product);
                            }
                        }
                    }
                    categoryDetailAdapter.setData(mProducts);
                    gridView.setAdapter(categoryDetailAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("TAG", "onCancelled: " + error.toString());
            }
        });
    }

    private void searchProductbyCategory(String keyword) {
        DatabaseReference myref = database.getReference("products");
        myref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        for (DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()) {
                            String idCat = dataSnapshot2.getKey();
                            Product product1 = dataSnapshot2.getValue(Product.class);
                            if (product1.getTensp().contains(keyword)) {
                                Product product = new Product();
                                product.setId(dataSnapshot.getKey());
                                product.setTensp(dataSnapshot.child("tensp").getValue(String.class));
                                product.setGiasp(dataSnapshot.child("giasp").getValue(Integer.class));
                                product.setMota(dataSnapshot.child("mota").getValue(String.class));
                                product.setImage(dataSnapshot.child("image").getValue(String.class));
                                mProducts.add(product);
                            }
                        }
                    }
                    categoryDetailAdapter.setData(mProducts);
                    gridView.setAdapter(categoryDetailAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("TAG", "onCancelled: " + error.toString());
            }
        });
    }
}
