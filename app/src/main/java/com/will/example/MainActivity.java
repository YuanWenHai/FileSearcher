package com.will.example;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
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
        makeMockedData();
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
                FileSearcher fileSearcher = new FileSearcher(MainActivity.this);
                fileSearcher.showHidden(true).withKeyword("88").search(new FileSearcher.FileSearcherCallback() {
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
