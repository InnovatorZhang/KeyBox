package com.zhang.keybox;

import android.content.Intent;
import android.speech.RecognizerIntent;
import android.support.annotation.IdRes;
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
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.zhang.keybox.drawer.ExportActivity;
import com.zhang.keybox.drawer.ImportActivity;
import com.zhang.keybox.drawer.ReplaceActivity;
import com.zhang.keybox.drawer.SettingActivity;
import com.zhang.keybox.drawer.SourceActivity;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;

    Toolbar toolbar;

    NavigationView navView;


    RecyclerView recyclerView;

    List<KeyBox> mKeyBoxes;

    KeyBoxAdapter boxAdapter;

    KeyBoxLab keyBoxLab;

    MaterialSearchView searchView;//开源库，让搜索更美观

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
          searchView = (MaterialSearchView) findViewById(R.id.main_search_view);

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

        /*给搜索按钮添加监听事件*/
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {

            @Override

            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override

            public boolean onQueryTextChange(String newText) {

                /*根据用户输入的内容来搜索相应的内容*/

                List keyBoxes = new ArrayList<>();
                for(KeyBox keyBox : mKeyBoxes){
                    int length =  newText.length();
                    if(keyBox.getName().length() >= length) {
                        if (keyBox.getName().substring(0, length).equals(newText)) {
                            keyBoxes.add(keyBox);
                        }
                    }
                }

                if(keyBoxes.size() == 0){
                    updateRC();

                }else {
                    boxAdapter.setKeyBoxes(keyBoxes);
                    boxAdapter.notifyDataSetChanged();
                }
                return false;
            }

        });


        recyclerView = (RecyclerView) findViewById(R.id.main_recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        keyBoxLab = KeyBoxLab.getKeyBoxLab(MainActivity.this);

        updateRC();

    }

    /*覆盖回退按钮，如果在另外界面来判断是否该退出界面，若在drawer中则关闭drawer，
    在搜索中则关闭搜索框*/

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            if (searchView.isSearchOpen()) {
                searchView.closeSearch();
            } else {
                super.onBackPressed();
            }

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

  /*创建菜单项*/
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.toolbar,menu);

        /*添加搜索*/
        MenuItem item = menu.findItem(R.id.mainActivity_search);
        searchView.setMenuItem(item);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.mainActivity_color:
                Toast.makeText(this,"还弄不来",Toast.LENGTH_SHORT).show();
                break;

            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            default:
        }
        return true;
    }

    /*开源库MaterialSearchView要用来处理相应的地方*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == MaterialSearchView.REQUEST_VOICE && resultCode == RESULT_OK) {
            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (matches != null && matches.size() > 0) {
                String searchWrd = matches.get(0);
                if (!TextUtils.isEmpty(searchWrd)) {
                    searchView.setQuery(searchWrd, false);
                }

            }

            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onRestart() {
        super.onStart();
        updateRC();//刷新adapter
    }


}
