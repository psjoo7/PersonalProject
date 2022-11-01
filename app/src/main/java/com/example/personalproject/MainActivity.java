package com.example.personalproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity {

    private EditText ID, password;
    private Button signInBtn, loginBtn, directBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        ID = findViewById(R.id.typeID);
        password = findViewById(R.id.typePassword);
        signInBtn = findViewById(R.id.signInBtn);
        loginBtn = findViewById(R.id.loginBtn);
        directBtn = findViewById(R.id.directBtn);
        //회원가입버튼
        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SignInActivity.class);
                startActivity(intent);
            }
        });
        //상품창으로 바로 가기
        directBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, GoodsActivity.class);
                intent.putExtra("isMember", false);
                startActivity(intent);
            }
        });
        //로그인버튼
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String UserID = ID.getText().toString();
                String UserPwd = password.getText().toString();

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject( response );
                            boolean success = jsonObject.getBoolean( "success" );

                            if(success) {//로그인 성공시

                                String UserID = jsonObject.getString( "UserID" );
                                String UserPwd = jsonObject.getString( "UserPwd" );
                                String UserName = jsonObject.getString( "UserName" );
                                String UserPhone = jsonObject.getString("UserPhone");
                                String UserAdd = jsonObject.getString("UserAdd");

                                Toast.makeText( getApplicationContext(), String.format("%s님 환영합니다.", UserName), Toast.LENGTH_SHORT ).show();
                                Intent intent = new Intent( MainActivity.this, GoodsActivity.class );

                                intent.putExtra("isMember", true);
                                intent.putExtra( "UserID", UserID );
                                intent.putExtra( "UserPwd", UserPwd );
                                intent.putExtra( "UserName", UserName );
                                intent.putExtra("UserPhone", UserPhone);
                                intent.putExtra("UserAdd", UserAdd);

                                startActivity( intent );

                            } else {//로그인 실패시
                                Toast.makeText( getApplicationContext(), "로그인에 실패하셨습니다.", Toast.LENGTH_SHORT ).show();
                                return;
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                LoginRequest loginRequest = new LoginRequest( UserID, UserPwd, responseListener );
                RequestQueue queue = Volley.newRequestQueue( MainActivity.this );
                queue.add( loginRequest );
            }
        });
    }
}