package com.example.logsignsql;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.logsignsql.databinding.ActivityEgzersizBinding;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class EgzersizList extends AppCompatActivity {

    ActivityEgzersizBinding EgzersizBinding;
    EgzersizDB EgzersizDB;

    private int playerId;
    private String playerName;
    private List<Egzersiz> EgzersizList;
    private EgzersizAdapter egzersizAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EgzersizBinding = ActivityEgzersizBinding.inflate(getLayoutInflater());
        setContentView(EgzersizBinding.getRoot());
        EgzersizDB = new EgzersizDB(this);

        ListView listView = findViewById(R.id.EgzersizList);

        // Retrieve player data from the database
        Intent intent = getIntent();
        playerId =intent.getIntExtra("playerId",0);
        playerName = intent.getStringExtra("playerName");
        System.out.println(playerName);
        System.out.println(playerId);
        EgzersizList = EgzersizDB.getEgzersizList(playerId);

        // Create and set the adapter
        egzersizAdapter = new EgzersizAdapter();
        listView.setAdapter(egzersizAdapter);

        TextView playerText = (TextView)findViewById(R.id.player_name);
        playerText.setText(playerName);

        // Listview item click
//        listView.setOnItemClickListener((parent, view, position, id) -> {
//            final String item = (String) parent.getItemAtPosition(position);
//            playerList.remove();
//            playerAdapter.notifyDataSetChanged();
//        });

        FloatingActionButton myFab = (FloatingActionButton) findViewById(R.id.egzersiz_fab);
        myFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(EgzersizBinding.egzersizFab.getContext());
                dialogBuilder.setTitle("Egzersiz");

                // Create the EditText for entering the name
                final EditText nameEditText = new EditText(EgzersizBinding.egzersizFab.getContext());
                nameEditText.setInputType(InputType.TYPE_CLASS_TEXT);
                dialogBuilder.setView(nameEditText);

                dialogBuilder.setPositiveButton("Ekle", (dialogInterface, i) -> {
                    String egzersiz = nameEditText.getText().toString();
                    System.out.println(playerId);
                    boolean inserted = EgzersizDB.insertData(playerId,egzersiz);
                    if (inserted) {
                        EgzersizList = EgzersizDB.getEgzersizList(playerId);
                        egzersizAdapter.notifyDataSetChanced();
                    }else{
                        System.out.println("Eklenmedi");
                    }
                });

                dialogBuilder.setNegativeButton("İptal", null);

                AlertDialog dialog = dialogBuilder.create();
                dialog.show();
            }
        });
    }
    private class EgzersizAdapter extends ArrayAdapter<Egzersiz> {

       EgzersizAdapter() {
             super(EgzersizList.this, R.layout.egzersiz_list_item, EgzersizList);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.egzersiz_list_item, parent, false);
            }

            Egzersiz egzersiz = getItem(position);

            TextView textViewName = convertView.findViewById(R.id.Egzersiz_name);
            if (egzersiz != null) {
                textViewName.setText(egzersiz.getName());
            }else{
                textViewName.setText("NULL");
            }

            return convertView;
        }

        @Override
        public int getCount() {
            return EgzersizList.size();
        }

        @Nullable
        @Override
        public Egzersiz getItem(int position) {
            return EgzersizList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public void notifyDataSetChanced() {
        }
    }

}
