package phinhph25802.example.appfastfood_banhang.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;



import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Chat extends AppCompatActivity {
    ImageView btnBack;
    ListView lv;
    List<Chats> listChat;
    FirebaseDatabase database;
    Context context;
    SwipeRefreshLayout swipeRefreshLayout;
    ChatAdapter adapter;
    String nameU, idChat;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        btnBack = findViewById(R.id.btn_back_chat);
        lv = findViewById(R.id.lv_chat);
        swipeRefreshLayout = findViewById(R.id.refresh_chat);
        context = this;

        database = FirebaseDatabase.getInstance();
        listChat = new ArrayList<>();
        adapter = new ChatAdapter(getApplicationContext(),listChat);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String idU = user.getUid();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        getListChat(idU);
    }

    private void getListChat(String idU){
        DatabaseReference reference = database.getReference("chats");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    DatabaseReference reference1 = dataSnapshot.child("id_user").getRef();
                    reference1.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot dataSnapshot1 : snapshot.getChildren()){
                                if(dataSnapshot1.getKey().equals(idU)){
                                    Chats chats = new Chats();
                                    chats.setId(dataSnapshot.getKey());
                                    listChat.add(chats);
                                }
                            }
                            lv.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}