package com.zhang.keybox.drawer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.zhang.keybox.R;
import com.zhang.keybox.drawer.set.AboutActivity;
import com.zhang.keybox.drawer.set.HelpActivity;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener {

    Boolean switchIsChecked = false;

    Switch passwordSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        Toolbar toolbar = (Toolbar)findViewById(R.id.drawer_setting_toolbar);
        toolbar.setTitle("设置");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        passwordSwitch = (Switch) findViewById(R.id.drawer_setting_passwordSwitch);
        passwordSwitch.setOnClickListener(this);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        /*保存勾选状态*/
        if(prefs.getBoolean("IsChecked",false)){
            switchIsChecked = prefs.getBoolean("IsChecked",false);
            passwordSwitch.setChecked(true);
        }

        FrameLayout helpFrameLayout = (FrameLayout) findViewById(R.id.drawer_setting_help);
        helpFrameLayout.setOnClickListener(this);

        FrameLayout aboutFrameLayout = (FrameLayout) findViewById(R.id.drawer_setting_about);
        aboutFrameLayout.setOnClickListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
              finish();
            default:
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.drawer_setting_help:
                Intent helpIntent = new Intent(SettingActivity.this, HelpActivity.class);
                startActivity(helpIntent);
                break;
            case R.id.drawer_setting_about:
                Intent aboutIntent = new Intent(SettingActivity.this, AboutActivity.class);
                startActivity(aboutIntent);
                break;
            case R.id.drawer_setting_passwordSwitch:
                switchIsChecked = !switchIsChecked;
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(SettingActivity.this).edit();
                editor.putBoolean("IsChecked",switchIsChecked);
                editor.apply();
                break;

        }
    }
}
