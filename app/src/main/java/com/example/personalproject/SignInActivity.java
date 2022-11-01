package com.example.personalproject;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignInActivity extends AppCompatActivity {

    private EditText ID, password, confirmPwd, name, phoneNum, address;
    private Button registerBtn, quitBtn, apptitle, validateCheck;
    private RadioButton accept, decline;
    private boolean validate = false;
    private boolean passwordPattern;
    private AlertDialog dialog;
    public static final Pattern VALID_PASSWOLD_REGEX_ALPHA_NUM = Pattern.compile("^[a-zA-Z0-9!@.#$%^&*?_~]{8,16}$");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        //회원가입 정보
        ID = findViewById(R.id.typeID);
        password = findViewById(R.id.typePassword);
        confirmPwd = findViewById(R.id.confirmPassword);
        name = findViewById(R.id.typeName);
        phoneNum = findViewById(R.id.typePhoneNum);
        address = findViewById(R.id.typeAddress);

        //약관동의
        accept = findViewById(R.id.accept);
        decline = findViewById(R.id.decline);
        //홈 버튼
        apptitle = findViewById(R.id.appTitle);
        apptitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toMain = new Intent(SignInActivity.this, MainActivity.class);
                startActivity(toMain);
            }
        });
        //중복체크
        validateCheck = findViewById(R.id.checkValid);
        validateCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String UserID = ID.getText().toString();
                if (validate) {
                    return; //검증 완료
                }
                if (UserID.equals("")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SignInActivity.this);
                    dialog = builder.setMessage("아이디를 입력하세요.").setPositiveButton("확인", null).create();
                    dialog.show();
                    return;
                }
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            AlertDialog.Builder builder = new AlertDialog.Builder(SignInActivity.this);
                            if (success) {
                                dialog = builder.setMessage("사용할 수 있는 아이디입니다.").setPositiveButton("확인", null).create();
                                dialog.show();
                                ID.setEnabled(false); //아이디값 고정
                                validate = true; //검증 완료
                                validateCheck.setBackgroundColor(Color.GRAY);
                            }
                            else {
                                dialog = builder.setMessage("이미 존재하는 아이디입니다.").setNegativeButton("확인", null).create();
                                dialog.show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                ValidateRequest validateRequest = new ValidateRequest(UserID, responseListener);
                RequestQueue queue = Volley.newRequestQueue(SignInActivity.this);
                queue.add(validateRequest);
            }
        });
        //회원가입 버튼
        registerBtn = findViewById(R.id.registerID);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String UserID = ID.getText().toString();
                final String UserPwd = password.getText().toString();
                final String UserName = name.getText().toString();
                final String CheckPwd = confirmPwd.getText().toString();
                final String UserPhone = phoneNum.getText().toString();
                final String UserAdd = address.getText().toString();
                final Boolean acception = accept.isChecked();
                final Boolean deny = decline.isChecked();
                passwordPattern = validatePassword(UserPwd);
                //한 칸이라도 입력 안했을 경우
                if (UserID.equals("") || UserPwd.equals("") || UserName.equals("") || CheckPwd.equals("") || UserPhone.equals("") || UserAdd.equals("")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SignInActivity.this);
                    dialog = builder.setMessage("모두 입력해주세요.").setNegativeButton("확인", null).create();
                    dialog.show();
                    return;
                }
                if (!validate) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SignInActivity.this);
                    dialog = builder.setMessage("중복된 아이디가 있는지 확인하세요.").setNegativeButton("확인", null).create();
                    dialog.show();
                    return;
                }
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject( response );
                            boolean success = jsonObject.getBoolean( "success" );
                            //회원가입 성공시
                            if(UserPwd.equals(CheckPwd) && passwordPattern) {
                                if (success) {
                                    if(acception){
                                        Toast.makeText(getApplicationContext(), String.format("%s님 가입을 환영합니다.", UserName), Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                                        startActivity(intent);
                                    }
                                    else if(deny){
                                        Toast.makeText(getApplicationContext(), "이용약관에 동의해주세요.", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                    else{
                                        Toast.makeText(getApplicationContext(), "체크박스를 체크해주세요.", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                    //회원가입 실패시
                                } else {
                                    Toast.makeText(getApplicationContext(), "회원가입에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            } else {
                                if(!UserPwd.equals(CheckPwd) && passwordPattern){
                                    AlertDialog.Builder builder = new AlertDialog.Builder(SignInActivity.this);
                                    dialog = builder.setMessage("비밀번호가 동일하지 않습니다.").setNegativeButton("확인", null).create();
                                    dialog.show();
                                    return;
                                }
                                else if(UserPwd.equals(CheckPwd) && !passwordPattern){
                                    AlertDialog.Builder builder = new AlertDialog.Builder(SignInActivity.this);
                                    dialog = builder.setMessage("비밀번호의 형식이 맞지 않습니다.").setNegativeButton("확인", null).create();
                                    dialog.show();
                                    return;
                                }
                                else{
                                    AlertDialog.Builder builder = new AlertDialog.Builder(SignInActivity.this);
                                    dialog = builder.setMessage("비밀번호와 비밀번호 확인란을 형식에 맞춰 작성해주세요.").setNegativeButton("확인", null).create();
                                    dialog.show();
                                    return;
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                };
                //서버로 Volley를 이용해서 요청
                RegisterRequest registerRequest = new RegisterRequest( UserID, UserPwd, UserName, UserPhone, UserAdd, responseListener);
                RequestQueue queue = Volley.newRequestQueue( SignInActivity.this );
                queue.add( registerRequest );

            }
        });
        //취소버튼
        quitBtn = findViewById(R.id.quitRegister);
        quitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public static boolean validatePassword(String pwStr) {
        Matcher matcher = VALID_PASSWOLD_REGEX_ALPHA_NUM.matcher(pwStr);
        return matcher.matches();
    }
}

