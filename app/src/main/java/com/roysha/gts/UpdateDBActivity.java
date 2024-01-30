package com.roysha.gts;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.HashMap;

public class UpdateDBActivity extends AppCompatActivity {
    ArrayList<Question> QuestionsList = new ArrayList<>();
    Button buttonUpdateDb;
    Button buttonDone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_dbactivity);
        Question question;

        ListView simpleListView = findViewById(R.id.DBListView);

        QuestionsList = ApplicationData.getQuestionsList();
        ArrayList list=new ArrayList<>();

        for(int i=0;i<QuestionsList.size();i++){
            HashMap<String,String> item = new HashMap<String,String>();
            question = QuestionsList.get(i);
            item.put("line1", "#:" + String.valueOf(question.id));
            item.put("line2", "Q:" + question.Question);
            item.put("line3", "A1:" + question.A1);
            item.put("line4", "A2:" + question.A2);
            item.put("line5", "A3:" + question.A3);
            item.put("line6", "A4:" + question.A4);
            item.put("line7", "CA:" + String.valueOf(question.CorrectAnswer));

            list.add( item );
        }

        SimpleAdapter sa = new SimpleAdapter(getBaseContext(), list,
                R.layout.activity_questionlistview,
                new String[] { "line1","line2","line3","line4","line5","line6","line7"},
                new int[] {R.id.line_1, R.id.line_2, R.id.line_3 ,R.id.line_4 ,R.id.line_5 ,R.id.line_6 ,R.id.line_7});

        simpleListView.setAdapter(sa);


        TextInputEditText tId = findViewById(R.id.id);
        TextInputEditText tQ = findViewById(R.id.Question);
        TextInputEditText tA1 = findViewById(R.id.A1);
        TextInputEditText tA2 = findViewById(R.id.A2);
        TextInputEditText tA3 = findViewById(R.id.A3);
        TextInputEditText tA4 = findViewById(R.id.A4);
        TextInputEditText tCA = findViewById(R.id.CA);

        simpleListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView <?> parent, View view, int position, long id) {
                Question question;
                question = QuestionsList.get(position);
                tId.setText( String.valueOf(question.id));
                tQ.setText(question.Question);
                tA1.setText(question.A1);
                tA2.setText(question.A2);
                tA3.setText(question.A3);
                tA4.setText(question.A4);
                tCA.setText(String.valueOf(question.CorrectAnswer));

                Toast.makeText(UpdateDBActivity.this, QuestionsList.get(position).id+"", Toast.LENGTH_SHORT).show();

            }
        });

        Button buttonDone = findViewById(R.id.Done);


        buttonDone.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                    Intent intent = new Intent(buttonDone.getContext(), MainActivity.class);
                    startActivity(intent);
            }

        });

        Button buttonUpdateDb = findViewById(R.id.UpdateQ);
        buttonUpdateDb.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(buttonUpdateDb.getContext(), MainActivity.class);
                startActivity(intent);
            }

        });
    }
}