package com.zhang.keybox;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class EditorActivity extends AppCompatActivity {

    public static final String DATA_RETURN = "com.zhang.keybox.return";

    EditText editTextName;
    EditText editTextCount;
    EditText editTextSecret;
    EditText editTextRemark;
    KeyBoxLab mKeyBoxLab;

    Toolbar mToolbar;
    UUID uuid;
    KeyBox keyBox;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        mKeyBoxLab = KeyBoxLab.getKeyBoxLab(EditorActivity.this);
        editTextName = (EditText)findViewById(R.id.editor_name);
        editTextCount = (EditText)findViewById(R.id.editor_count);
        editTextSecret = (EditText)findViewById(R.id.editor_secret);
        editTextRemark = (EditText)findViewById(R.id.editor_remark);
        mToolbar = (Toolbar)findViewById(R.id.editor_toolbar);

        mToolbar.setTitle("编辑");
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

     /*从showActivity过来的数据，将其设置到editText上*/
        Intent intent1 = getIntent();
        uuid = (UUID)intent1.getSerializableExtra(ShowActivity.EXTRA_ID);
        if(uuid != null){
            keyBox = mKeyBoxLab.getKeyBox(uuid);
            editTextName.setText(keyBox.getName());
            editTextCount.setText(keyBox.getCount());
            editTextSecret.setText(keyBox.getPassword());
            editTextRemark.setText(keyBox.getRemark());
        }
    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.editor_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.editor_ok:
                if(uuid == null) {
                    //如果是从MainActivity启动的话，直接添加信息
                    setInfo();
                }else{
                    //如果是从ShowActivity启动的话，就返回更新的信息
                    keyBox = getupdateKeyBox(keyBox);
                    mKeyBoxLab.updateKeyBox(keyBox);
                    Intent intent2 = new Intent();
                    intent2.putExtra(DATA_RETURN,uuid);
                    setResult(RESULT_OK,intent2);
                    finish();
                }
                break;
            case android.R.id.home:
                finish();
                break;
            default:
        }
        return true;
    }

    private void setInfo(){
        String name = editTextName.getText().toString();
        String count = editTextCount.getText().toString();
        String secret = editTextSecret.getText().toString();
        String remark = editTextRemark.getText().toString();

        if(name.length() == 0 || count.length() == 0 || secret.length() == 0){
            Toast.makeText(EditorActivity.this,"名称，账户和密码不能为空",Toast.LENGTH_SHORT).show();
        }else {
            KeyBox keyBox2 = new KeyBox();

            keyBox2.setName(name);
            keyBox2.setCount(count);
            keyBox2.setPassword(secret);
            keyBox2.setRemark(remark);
            keyBox2.setDate(new Date());

            mKeyBoxLab.addKeyBox(keyBox2);


            finish();
        }
    }

    private KeyBox getupdateKeyBox(KeyBox keyBox1){

        String name = editTextName.getText().toString();
        String count = editTextCount.getText().toString();
        String secret = editTextSecret.getText().toString();
        String remark = editTextRemark.getText().toString();

        keyBox1.setName(name);
        keyBox1.setCount(count);
        keyBox1.setPassword(secret);
        keyBox1.setRemark(remark);
        keyBox1.setDate(new Date());
        return keyBox1;
    }

}
