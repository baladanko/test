package com.herokuapp.myapplication.activity;


import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.herokuapp.myapplication.R;
import com.herokuapp.myapplication.entity.User;

/* Окно с дополнительной информацией о пользователе */
public class UserInfoActivity extends Activity implements OnClickListener {

    User user;
    TextView tvName,tvSurname,tvInfo,tvCreated_at;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_info);
        user = (User) getIntent().getSerializableExtra(MainActivity.FIELD_USER);
        Button btnClose = (Button) findViewById(R.id.btnClose);
        tvName = (TextView) findViewById(R.id.tvName);
        tvSurname = (TextView) findViewById(R.id.tvSurname);
        tvInfo = (TextView) findViewById(R.id.tvInfo);
        tvCreated_at = (TextView) findViewById(R.id.tvCreated_at);
        tvName.setText(user.getName());
        tvSurname.setText(user.getSurname());
        tvInfo.setText(user.getInfo());
        tvCreated_at.setText(user.getCreated_at());
        btnClose.setOnClickListener(this);
    }






    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnClose:
                this.finish();
                break;
        }
    }


}