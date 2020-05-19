package com.example.androidproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {
    public static final String TAG = "TAG";
    EditText fullName,email,password,phone;
Button registerBtn,buttonMap;
TextView loginLink;
FirebaseAuth fAuth;
FirebaseFirestore fstore;
String userId;
Intent intent;
TextView adresse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    fullName=findViewById(R.id.fullName);
    email=findViewById(R.id.email);
    password=findViewById(R.id.password);
    phone=findViewById(R.id.phoneNumber);
    registerBtn=findViewById(R.id.registerBtn);
    loginLink=findViewById(R.id.loginLink);
        adresse=(TextView) findViewById(R.id.adresse);
        buttonMap=findViewById(R.id.map);
        buttonMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                intent = new Intent(Register.this, GoogleMap.class);
                startActivityForResult(intent,1);
            }
        });
//instanciation de la BD
    fAuth=FirebaseAuth.getInstance();
        fstore=FirebaseFirestore.getInstance();

        if (fAuth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
        }

    registerBtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //trim pr enlever les espace au debut w a la fin de stream
            final String mEmail=email.getText().toString().trim();
            final String mPassword=password.getText().toString().trim();
            final String adr=adresse.getText().toString();
final String mPhone=phone.getText().toString();
final String mFullName=fullName.getText().toString();
            if(TextUtils.isEmpty(mFullName)){fullName.setError("Veuillez saisir votre Nom");return;}
            if(TextUtils.isEmpty(mEmail)){
                email.setError("Veuillez saisir votre adresse mail");
                return;
            }

            if(TextUtils.isEmpty(mPassword)){
                password.setError("Veuillez saisir votre mot de passe");
                return;
            }

            if (mPassword.length() < 6) {
                password.setError("Le mot de passe doit être fourni d'au moins 6 lettres");
                return;
            }
            ///Register the User in fireBase
if(TextUtils.isEmpty(mPhone)){phone.setError(("Veuillez saisir le numero de votre télephone"));return;}
if(mPhone.length() !=8){phone.setError(("Le numero de télephone doit être fournie de 8 chiffres"));return;}
if(adr.equals("")){adresse.setError("Veuillez saisir votre adresse");return;}
            fAuth.createUserWithEmailAndPassword(mEmail,mPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    { Toast.makeText(Register.this,"User created", Toast.LENGTH_SHORT).show();
                    userId=fAuth.getCurrentUser().getUid();
                        DocumentReference documentReference=fstore.collection("users").document(userId);
                        Map<String,Object> user=new HashMap<>();
                        user.put("fullName",mFullName);
                        user.put("email",mEmail);
                        user.put("password",mPassword);
                        user.put("phone",mPhone);
                        user.put("adresse",adr);
                        documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                //ctrl+alt+c: lajout automatique de la variable sera fait
                                Log.d(TAG, "onSuccess: user Profile is created for "+ userId);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(TAG, "onFailure: " + e.toString());
                            }
                        });
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                    }
                    else {
                        Toast.makeText(Register.this,"Error"+ task.getException().getMessage(),Toast.LENGTH_SHORT).show();}
                }
            });

        }
    });


loginLink.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        startActivity(new Intent(getApplicationContext(),login.class));

    }
});

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode==RESULT_OK ){
            String result=data.getStringExtra("reponse");

            adresse.setText(result);
        }

    }
}
