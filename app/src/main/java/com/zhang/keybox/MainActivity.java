package com.zhang.keybox;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.zhang.keybox.drawer.ExportActivity;
import com.zhang.keybox.drawer.ImportActivity;
import com.zhang.keybox.drawer.ReplaceActivity;
import com.zhang.keybox.drawer.SettingActivity;
import com.zhang.keybox.drawer.SourceActivity;

import java.util.List;


public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;

    Toolbar toolbar;

    NavigationView navView;


    RecyclerView recyclerView;

    List<KeyBox> mKeyBoxes;

    KeyBoxAdapter boxAdapter;

    KeyBoxLab keyBoxLab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


          toolbar = (Toolbar)findViewById(R.id.main_toolbar);
          setSupportActionBar(toolbar);
          ActionBar actionBar = getSupportActionBar();
          FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.main_floatingActionButton);

          mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
          navView = (NavigationView)findViewById(R.id.nav_view);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,EditorActivity.class);
                startActivity(intent);
            }
        });

        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);//设置drawer图标
            actionBar.setHomeAsUpIndicator(R.drawable.ic_home);
        }

        navView.setCheckedItem(R.id.nav_replace);
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.nav_setting:
                        Intent settingIntent = new Intent(MainActivity.this, SettingActivity.class);
                        startActivity(settingIntent);
                        break;
                    case R.id.nav_source:
                        Intent sourceIntent = new Intent(MainActivity.this, SourceActivity.class);
                        startActivity(sourceIntent);
                        break;
                    case R.id.nav_replace:
                        Intent replaceIntent = new Intent(MainActivity.this, ReplaceActivity.class);
                        startActivity(replaceIntent);
                        break;
                    case R.id.nav_import:
                        Intent importIntent = new Intent(MainActivity.this, ImportActivity.class);
                        startActivity(importIntent);
                        break;
                    case R.id.nav_export:
                        Intent exportIntent = new Intent(MainActivity.this, ExportActivity.class);
                        startActivity(exportIntent);
                        break;
                }
               mDrawerLayout.closeDrawers();
                return true;
            }
        });




        recyclerView = (RecyclerView) findViewById(R.id.main_recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        keyBoxLab = KeyBoxLab.getKeyBoxLab(MainActivity.this);

        updateRC();

    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {

            super.onBackPressed();

        }
    }

    private void updateRC() {
        if (boxAdapter == null) {
            mKeyBoxes = keyBoxLab.getKeyBoxes();
            boxAdapter = new KeyBoxAdapter(mKeyBoxes);
            boxAdapter.setContext(MainActivity.this);
            recyclerView.setAdapter(boxAdapter);
        }else{
            mKeyBoxes = keyBoxLab.getKeyBoxes();
            boxAdapter.setKeyBoxes(mKeyBoxes);
            boxAdapter.notifyDataSetChanged();
        }
    }


    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.toolbar,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.mainActivity_search:
                Toast.makeText(this,"you",Toast.LENGTH_SHORT).show();
                break;
            case R.id.mainActivity_color:
                Toast.makeText(this,"you",Toast.LENGTH_SHORT).show();
                break;

            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            default:
        }
        return true;
    }

    @Override
    protected void onRestart() {
        super.onStart();
        updateRC();//刷新adapter
    }


}
