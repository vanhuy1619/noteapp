package com.example.appnote;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Signup extends AppCompatActivity
{
    EditText emailsignup,passwordsignup,usernamesignup,repasswordsignup;
    private MaterialButton btnSignUp;
    private TextView haveAccount;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logup);

        usernamesignup=findViewById(R.id.usernamesignup);
        emailsignup=findViewById(R.id.emailusersignup);
        passwordsignup=findViewById(R.id.passworduser);
        repasswordsignup=findViewById(R.id.repassworduser);

        btnSignUp=findViewById(R.id.signbtn);
        haveAccount=findViewById(R.id.haveaccount);
        firebaseAuth=FirebaseAuth.getInstance();
        haveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),Login.class);
                startActivity(intent);
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username=usernamesignup.getText().toString();
                String email=emailsignup.getText().toString();
                String pass=passwordsignup.getText().toString();
                String repass=repasswordsignup.getText().toString();
                if(username.isEmpty() || email.isEmpty() || pass.isEmpty() || repass.isEmpty())
                {
                    Toast.makeText(getApplicationContext(),"Vui l??ng nh???p ?????y ????? th??ng tin",Toast.LENGTH_SHORT).show();
                }
                else if (email.isEmpty() || !email.contains("@"))
                {
                    Toast.makeText(getApplicationContext(),"Email kh??ng h???p l???",Toast.LENGTH_SHORT).show();
                }
                else if (!repass.equals(pass)){
                    Toast.makeText(Signup.this, "M???t kh???u kh??ng tr??ng nhau", Toast.LENGTH_SHORT).show();
                }
                else if (pass.length() < 5){
                    Toast.makeText(Signup.this, "M???t kh???u qu?? ng???n (< 5 k?? t???)", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    firebaseAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful())
                            {
                                Toast.makeText(Signup.this, "????ng k?? t??i kho???n th??nh c??ng", Toast.LENGTH_SHORT).show();
                                sendEmail();
                            }
                            else
                            {
                                Toast.makeText(Signup.this, "????ng k?? t??i kho???n th???t b???i", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }

    public void sendEmail()
    {
        FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
        if(firebaseUser!=null)
        {
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(Signup.this, "Vui l??ng ki???m tra email ????? k??ch ho???t t??i kho???n", Toast.LENGTH_SHORT).show();
                    firebaseAuth.signOut();
                    finish();
                    startActivity(new Intent(getApplicationContext(),Login.class));
                }
            });
        }
        else
        {
            Toast.makeText(this, "Vui l??ng ki???m tra l???i", Toast.LENGTH_SHORT).show();
        }
    }
}
