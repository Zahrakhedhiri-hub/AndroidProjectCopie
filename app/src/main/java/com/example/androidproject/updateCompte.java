package com.example.androidproject;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

import static android.app.Activity.RESULT_OK;


public class updateCompte extends Fragment {
    EditText name,phoneNumber,mail,password;
    TextView adresse;
    Button map,update;
FirebaseAuth fireAuth;
FirebaseFirestore fireStore;
Intent intent;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
     final View v = inflater.inflate(R.layout.fragment_update_compte, container, false);
        fireAuth=FirebaseAuth.getInstance();
        fireStore=FirebaseFirestore.getInstance();


name=v.findViewById(R.id.uFullName);
adresse=v.findViewById(R.id.uAdresse);
phoneNumber=v.findViewById(R.id.uPhoneNumber);
mail=v.findViewById(R.id.uEmail);
password=v.findViewById(R.id.uPassword);
map=v.findViewById(R.id.uMap);
update=v.findViewById(R.id.updateBtn);
update.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
      String id=fireAuth.getCurrentUser().getUid();
      final FirebaseUser currentUser=fireAuth.getCurrentUser();



String nom=name.getText().toString();
String adr=adresse.getText().toString();
String numtel=phoneNumber.getText().toString();
final String pwd=password.getText().toString();
final String email=mail.getText().toString();

if(TextUtils.isEmpty(nom)){
    name.setError("veuillez renseigner le champs nom");
    return;
}
        if(TextUtils.isEmpty(adr)){
            adresse.setError("veuillez renseigner le champs  adresse");
            return;
        }
        if(TextUtils.isEmpty(pwd)){
            password.setError("veuillez renseigner le champs de passe");
            return;
        }
        if(pwd.length()<6){
            password.setError("la mot de passe doit être composé d'au moins 6 lettres");
            return;
        }
        if(TextUtils.isEmpty(email)){
            mail.setError("veuillez renseigner le champs adresse mail");
            return;
        }
        if(TextUtils.isEmpty(numtel)){
            phoneNumber.setError("veuillez renseigner le champs numero de telephone");
            return;
        }
            Map<String, Object> user = new HashMap<>();

        user.put("fullName",nom);
        user.put("adresse",adr);
        user.put("phone",numtel);
        user.put("email",email);
        user.put("password",pwd);
        user.put("adresse",adr);
       /* AuthCredential provider= EmailAuthProvider.getCredential(email,pwd);
        currentUser.reauthenticate(provider).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){

                }else {*/

               /* }
            }
        });*/
DocumentReference docUser=fireStore.collection("users").document(id);
docUser.set(user);
        currentUser.updateEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){Log.d("!!!!emaillll!"," Modified: ");
                    currentUser.updatePassword(pwd).addOnCompleteListener(new OnCompleteListener<Void>() {

                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){

                                FirebaseAuth.getInstance().signOut();
                                startActivity(new Intent(getActivity(),login.class));

                            }
                            else { Toast.makeText(getActivity(),"Erreur "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();}
                        }
                    });}
                else {Log.d("myLog","!!!!!!!!!!!!!!!erreur"+task.getException().getMessage());}
            }
        });







    }
});
map.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        intent = new Intent(getActivity(), GoogleMap.class);
        startActivityForResult(intent,1);
    }
});

String userId=fireAuth.getCurrentUser().getUid();

        DocumentReference user=fireStore.collection("users").document(userId);
        user.addSnapshotListener(getActivity(), new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                String nom=documentSnapshot.getString("fullName");
                String adr=documentSnapshot.getString("adresse");
                String numPhone=documentSnapshot.getString("phone");
                String email=documentSnapshot.getString("email");
                String pwd=documentSnapshot.getString("password");
            name.setText(nom);
            adresse.setText(adr);
            phoneNumber.setText(numPhone);
            mail.setText(email);
            password.setText(pwd);


            }


        });





    return v; }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @androidx.annotation.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode==RESULT_OK ){
            String result=data.getStringExtra("reponse");

            adresse.setText(result);
        }

    }
}

