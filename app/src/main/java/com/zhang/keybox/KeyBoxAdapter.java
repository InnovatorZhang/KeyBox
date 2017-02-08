package com.zhang.keybox;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by 张 on 2017/1/29.
 */

public class KeyBoxAdapter extends RecyclerView.Adapter<KeyBoxAdapter.ViewHolder> {

    public static final String EXTRA_ID = "com.zhang.keybox.id";

    private List<KeyBox> mKeyBoxes;

    KeyBox keyBox;

    Context mContext;

    public void setContext(Context context){
        mContext = context;
    }

    public KeyBoxAdapter(List<KeyBox> keyBoxes) {
        mKeyBoxes = keyBoxes;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);//读取设置中的密码状态

        keyBox = mKeyBoxes.get(position);

        holder.nameTextView.setText(keyBox.getName());
        holder.countTextView.setText(keyBox.getCount());

        if(prefs.getBoolean("IsChecked",false)){  //若设置中为勾选状态，则设置密码不可见
            holder.secretTextView.setText("**********");
        }else {
            holder.secretTextView.setText(keyBox.getPassword());
        }
    }

    @Override
    public int getItemCount() {
        return mKeyBoxes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView nameTextView;
        TextView countTextView;
        TextView secretTextView;
        ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);

            nameTextView = (TextView) itemView.findViewById(R.id.item_key_name);
            countTextView = (TextView) itemView.findViewById(R.id.item_key_count);
            secretTextView = (TextView) itemView.findViewById(R.id.item_key_secret);
            imageView = (ImageView) itemView.findViewById(R.id.item_copy);

            /*为粘贴设置点击事件*/
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    /*获取粘贴板*/
                    final ClipboardManager cm = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);

                    //设置对话框
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setTitle("选择要复制的选项");
                    String[] item = {"复制账号","复制密码"};

                    /*设置item的点击事件*/
                    builder.setItems(item, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if(i == 0){
                                //设置复制的内容
                                cm.setText(mKeyBoxes.get(getLayoutPosition()).getCount());
                            }else{
                                cm.setText(mKeyBoxes.get(getLayoutPosition()).getPassword());
                            }
                            Toast.makeText(mContext,"复制成功",Toast.LENGTH_SHORT).show();
                        }
                    });

                    builder.show();
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    KeyBox keyBox1 = mKeyBoxes.get(getLayoutPosition());//获取当前点击的item
                    Intent intent = new Intent(mContext,ShowActivity.class);
                    intent.putExtra(EXTRA_ID,keyBox1.getId());
                    mContext.startActivity(intent);
                }
            });


        }
    }

    public void setKeyBoxes(List<KeyBox> keyBoxes){
        mKeyBoxes = keyBoxes;
    }


}
