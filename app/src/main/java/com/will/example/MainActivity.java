package com.will.example;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.will.filesearcher.file_searcher.FileSearcherActivity;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE = 888;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText editText = (EditText) findViewById(R.id.example_edit);
        Button button = (Button) findViewById(R.id.example_confirm);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = editText.getText().toString();
                if(content.replaceAll(" ","").isEmpty()){
                    Toast.makeText(MainActivity.this, "miss the keyword", Toast.LENGTH_SHORT).show();
                }else{
                    Intent intent = new Intent(MainActivity.this, FileSearcherActivity.class);
                    intent.putExtra("keyword",content);
                    //intent.putExtra("theme",R.style.SearchTheme);
                    intent.putExtra("min",50*1024);
                    startActivityForResult(intent,REQUEST_CODE);
                }
            }
        });



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_CODE && resultCode ==FileSearcherActivity.OK && data != null){
            ArrayList<File> list = ( ArrayList<File>) data.getSerializableExtra("data");
            Toast.makeText(this,"you selected"+list.size()+"items",Toast.LENGTH_SHORT).show();
        }
    }
}
