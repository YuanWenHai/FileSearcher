package com.will.example;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.will.filesearcher.FileSearcher;

import java.io.File;
import java.util.List;

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
               /* String content = editText.getText().toString();
                if(content.replaceAll(" ","").isEmpty()){
                    Toast.makeText(MainActivity.this, "miss the keyword", Toast.LENGTH_SHORT).show();
                }else{

                }*/
                FileSearcher fileSearcher = new FileSearcher(Environment.getExternalStorageDirectory(),MainActivity.this);
                fileSearcher.showHidden(true).withExtension("txt").withSizeLimit(1024*1024*1024,-5).search(new FileSearcher.FileSearcherCallback() {
                    @Override
                    public void onSelect(List<File> files) {
                        Toast.makeText(MainActivity.this, files.size()+"", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });



    }
}
