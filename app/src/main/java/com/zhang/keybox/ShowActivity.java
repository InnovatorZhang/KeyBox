package com.zhang.keybox;

import android.content.DialogInterface;
import android.content.Intent;
import android.preference.DialogPreference;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.UUID;

public class ShowActivity extends AppCompatActivity {

    public static final String EXTRA_ID = "com.zhang.keybox.id";

    Intent mIntent;

    UUID id;

    KeyBoxLab keyBoxLab;

    KeyBox mKeyBox;

    TextView mCountTextView;
    TextView mPasswordTextView;
    TextView mTimeTextView;
    TextView mRemarkTextView;
    CollapsingToolbarLayout mCollapsingToolbarLayout;
    FloatingActionButton mFloatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);

        mCountTextView = (TextView)findViewById(R.id.show_account);
        mPasswordTextView = (TextView)findViewById(R.id.show_password);
        mTimeTextView = (TextView)findViewById(R.id.show_time);
        mRemarkTextView = (TextView)findViewById(R.id.show_note);
        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.show_collapsingToolbar);
        mFloatingActionButton = (FloatingActionButton)findViewById(R.id.show_floating_action_button);

        Toolbar toolbar = (Toolbar)findViewById(R.id.show_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);//设置drawer图标

        mIntent = getIntent();
        id = (UUID)mIntent.getSerializableExtra(KeyBoxAdapter.EXTRA_ID);

        keyBoxLab = KeyBoxLab.getKeyBoxLab(ShowActivity.this);

        mKeyBox = keyBoxLab.getKeyBox(id);
        setText(mKeyBox);

        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ShowActivity.this,EditorActivity.class);
                intent.putExtra(EXTRA_ID,id);
                startActivityForResult(intent,1);
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.show_delete,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.show_delete:
                AlertDialog.Builder dialog = new AlertDialog.Builder(ShowActivity.this);
                 //设置对话框
                dialog.setTitle("删除");
                dialog.setMessage("确定要删除本条密钥？");
                dialog.setCancelable(false);//无法通过Back键取消
                dialog.setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        keyBoxLab.deleteKeyBox(id);
                        finish();
                    }
                });
                dialog.setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(ShowActivity.this,"已取消",Toast.LENGTH_SHORT).show();
                    }
                }
        );
                dialog.show();

                break;

            case android.R.id.home:
                finish();
                default:
                    finish();
        }
        return true;
    }

    private void setText(KeyBox keyBox){

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(keyBox.getDate());
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        String date = year + "年" + month + "月" + day + "日" + hour + "时" + minute +"分";

        mCountTextView.setText(keyBox.getCount());
        mPasswordTextView.setText(keyBox.getPassword());
        mTimeTextView.setText(date);
        mRemarkTextView.setText(keyBox.getRemark());
        mCollapsingToolbarLayout.setTitle(keyBox.getName());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 1:
                if(resultCode == RESULT_OK){
                    UUID id = (UUID)data.getSerializableExtra(EditorActivity.DATA_RETURN);
                }
        }
    }

    @Override
    protected void onRestart() {
        super.onStart();
        setText(KeyBoxLab.getKeyBoxLab(ShowActivity.this).getKeyBox(id));//刷新显示的数据
    }
}
