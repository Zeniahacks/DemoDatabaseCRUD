package sg.edu.rp.c346.id19023702.demodatabasecrud;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Button btnAdd, btnEdit, btnRetrieve;
    TextView tvDBContent;
    EditText etContent;
    ArrayList<Note> al;
    ListView lv;
    ArrayAdapter<Note> aa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAdd = findViewById(R.id.btnAdd);
        btnEdit = findViewById(R.id.btnEdit);
        btnRetrieve = findViewById(R.id.btnRetrieve);
        tvDBContent = findViewById(R.id.tvDBContnet);
        etContent = findViewById(R.id.etContent);
        lv = findViewById(R.id.lv);
        al = new ArrayList<Note>();
        aa = new ArrayAdapter<Note>(this, android.R.layout.simple_expandable_list_item_1, al);
        lv.setAdapter(aa);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Note target = al.get(position);
                Intent i = new Intent(MainActivity.this, EditActivity.class);
                i.putExtra("data", target);
                startActivity(i);
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String data = etContent.getText().toString();
                DBHelper dbHelper = new DBHelper(MainActivity.this);
                long inserted_id = dbHelper.insertNote(data);

                dbHelper.close();

                if (inserted_id != -1) {
                    Toast.makeText(MainActivity.this, "Insert successful", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnRetrieve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DBHelper dbHelper = new DBHelper(MainActivity.this);
                al.clear();
                // Section F: The edit text will act for filtering keyword.
                al.addAll(dbHelper.getAllNotes(etContent.getText().toString()));
                aa.notifyDataSetChanged();
                dbHelper.close();

                String text = "";
                for (int i = 0; i < al.size(); i++) {
                    Note tmp = al.get(i);
                    text += "ID: " + tmp.getId();
                    text += ", " + tmp.getNoteContent();
                    text += "\n";
                }
                tvDBContent.setText(text);
            }
        });
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Note target = al.get(0);
                Intent i = new Intent(MainActivity.this, EditActivity.class);
                i.putExtra("data", target);
                startActivity(i);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        retrieve();
    }

    public void retrieve() {
        tvDBContent = findViewById(R.id.tvDBContnet);
        DBHelper dbh = new DBHelper(MainActivity.this);
        al = new ArrayList<>();
        al.addAll(dbh.getAllNotes());
        dbh.close();

        String txt = "";
        for (Note i : al) {
            txt += "ID: " + i.getId() + ", " + i.getNoteContent() + "\n";
        }
        tvDBContent.setText(txt);
    }
}