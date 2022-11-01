package com.example.personalproject;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class GoodsActivity extends AppCompatActivity {
    private Button register, apptitle;
    private Boolean isMember;
    private String ID, Pwd, name, phoneNum, address;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods);
        //타이틀바 제거
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        //홈 버튼
        apptitle = findViewById(R.id.appTitle);
        apptitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toMain = new Intent(GoodsActivity.this, MainActivity.class);
                startActivity(toMain);
            }
        });
        //회원체크
        register = findViewById(R.id.registerIDFromGoods);
        Intent getLoginIntent = getIntent();

        isMember = getLoginIntent.getBooleanExtra("isMember", true);
        ID = getLoginIntent.getStringExtra("UserID");
        Pwd = getLoginIntent.getStringExtra("UserPwd");
        name = getLoginIntent.getStringExtra("UserName");
        phoneNum = getLoginIntent.getStringExtra("UserPhone");
        address = getLoginIntent.getStringExtra("UserAdd");

        register.setOnClickListener(new View.OnClickListener() {
            AlertDialog.Builder builder = new AlertDialog.Builder(GoodsActivity.this);
            @Override
            public void onClick(View view) {
                if(isMember == true){
                    dialog = builder.setTitle("회원 정보").setMessage("ID : " + ID + "\n" +
                            "Password : " + Pwd + "\n" +
                            "Name : " + name + "\n" +
                            "Phone Number : " + phoneNum + "\n" +
                            "User Address : " + address).setNegativeButton("확인", null).create();
                    dialog.show();
                    return;
                }
                else{
                    builder.setTitle("회원이 아닙니다!").setMessage("귀하는 회원이 아닙니다.\n회원가입 하시겠습니까?");
                    builder.setPositiveButton("네", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent toRegister = new Intent(GoodsActivity.this, SignInActivity.class);
                            startActivity(toRegister);
                        }
                    });
                    builder.setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(getApplicationContext(),"Try again!", Toast.LENGTH_LONG).show();
                        }
                    });
                    dialog = builder.create();
                    dialog.show();
                    return;
                }
            }
        });
    }
}