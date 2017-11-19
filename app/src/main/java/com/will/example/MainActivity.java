package com.will.example;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.will.filesearcher.FileSearcher;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE = 888;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //makeMockedData();
        final EditText keywordEdit = findViewById(R.id.example_keyword);
        final EditText extensionEdit = findViewById(R.id.example_extension);
        final EditText minSizeEdit = findViewById(R.id.example_min);
        final EditText maxSizeEdit = findViewById(R.id.example_max);
        final CheckBox showHiddenCheckBox = findViewById(R.id.example_show_hidden);
        Button button = (Button) findViewById(R.id.example_confirm);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = keywordEdit.getText().toString().replaceAll(" ","");
                String extension = extensionEdit.getText().toString();
                long minSize = Long.valueOf(minSizeEdit.getText().toString().isEmpty() ? "0" : minSizeEdit.getText().toString());
                long maxSize = Long.valueOf(maxSizeEdit.getText().toString().isEmpty() ? "-1" : maxSizeEdit.getText().toString());
                boolean showHidden = showHiddenCheckBox.isChecked();

                FileSearcher fileSearcher = new FileSearcher(MainActivity.this);
                fileSearcher
                        .withKeyword(content)
                        .withExtension(extension)
                        .withSizeLimit(minSize,maxSize)
                        .showHidden(showHidden)
                        .search(new FileSearcher.FileSearcherCallback() {
                    @Override
                    public void onSelect(List<File> files) {
                        Toast.makeText(MainActivity.this, files.size()+"", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
    private void makeMockedData(){

        final File cacheDir = getExternalCacheDir();
        if(cacheDir != null){
            if(!cacheDir.exists()){
                cacheDir.mkdirs();
            }
            if(cacheDir.listFiles().length > 0){
                return;
            }
            final ProgressDialog p = new ProgressDialog(this);
            p.show();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try{
                        for(int i=0;i<200;i++){
                            File dir = new File(cacheDir.getPath()+File.separator+i);
                            dir.mkdir();
                            for(int a=0;a<200;a++){
                                File file = new File(dir.getPath()+File.separator+i+"-"+a);
                                file.createNewFile();
                            }
                        }

                    }catch (IOException i){
                        i.printStackTrace();
                    }finally {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                p.cancel();
                            }
                        });
                    }
                }
            }).start();
        }
    }
}
