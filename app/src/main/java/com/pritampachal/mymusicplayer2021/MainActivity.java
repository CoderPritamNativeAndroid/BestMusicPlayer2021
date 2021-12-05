package com.pritampachal.mymusicplayer2021;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private TextView textViewTopBar,textViewAllSongs;
    private ListView listView;
    private ArrayList<File> arrayList1;
    private ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textViewTopBar=findViewById(R.id.textViewTopBar);
        textViewAllSongs=findViewById(R.id.textViewAllSongs);
        listView=findViewById(R.id.listView);
        textViewTopBar.setSelected(true);
        textViewTopBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,MainActivity3.class);
                startActivity(intent);
            }
        });
        //start, for Storage-Permission
        Dexter.withContext(MainActivity.this).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                // arrayList1=new ArrayList<>();
                arrayList1=fetchAllSongs(Environment.getExternalStorageDirectory());
                String[] arrStr=new String[arrayList1.size()];
                for(int i=0;i<arrayList1.size();i++) {
                    arrStr[i]=arrayList1.get(i).getName().replace(".mp3","");
                }
                textViewAllSongs.setText("All Songs ("+arrayList1.size()+")");
                arrayAdapter=new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1,arrStr);
                listView.setAdapter(arrayAdapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Intent intent=new Intent(MainActivity.this,MainActivity2.class);
                        String currentSong=listView.getItemAtPosition(i).toString();
                        intent.putExtra("allSongs",arrayList1);
                        intent.putExtra("currentSong",currentSong);
                        intent.putExtra("currentSongPosition",i);
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                String[] myMess=new String[] {"","","\nPlease Give Me STORAGE-PERMISSION\n\nSTORAGE-PERMISSION Required\n\nI Cannot WORK without STORAGE-PERMISSION\n",""};
                ArrayAdapter<String> arrayAdapter1=new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1,myMess);
                listView.setAdapter(arrayAdapter1);
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                permissionToken.continuePermissionRequest();
            }
        }).check();
        //end, for Storage-Permission
    }
    //start, read all .mp3 files from storage
    public ArrayList<File> fetchAllSongs(File file) {
        ArrayList<File> arrayList=new ArrayList<>();
        File[] arrFile=file.listFiles();
        if(arrFile!=null) {
            for(File file1:arrFile) {
                if(!file1.isHidden() && file1.isDirectory()) {
                    arrayList.addAll(fetchAllSongs(file1));
                } else {
                    if(file1.getName().endsWith(".mp3") && !file1.getName().startsWith(".")) {
                        arrayList.add(file1);
                    }
                }
            }
        }
        return arrayList;
    }
    //end, read all .mp3 files from storage
}
