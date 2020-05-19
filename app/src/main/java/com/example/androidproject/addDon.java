package com.example.androidproject;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.storage.UploadTask;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;


import java.util.HashMap;
import java.util.Map;

import static com.example.androidproject.Register.TAG;


public class addDon extends Fragment {
EditText nomProd;
Button addD;
ImageView image;
FirebaseFirestore fstore;
Button addImage;
FirebaseAuth fAuth;
String userId;
StorageReference storageReference;
String adresse;
    String url;
    Uri imageUri;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_add_don, container, false);
        nomProd=v.findViewById(R.id.nomProd);
        image=v.findViewById(R.id.imageProd);
        addImage=v.findViewById(R.id.addImage);
        //fAuth=FirebaseAuth.getInstance();
        addD=v.findViewById(R.id.addDon);
        fstore=FirebaseFirestore.getInstance();

        storageReference= FirebaseStorage.getInstance().getReference("images");
addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openGallery=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(openGallery,1000);            }
        });
addD.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        fAuth=FirebaseAuth.getInstance();
        userId=fAuth.getCurrentUser().getUid();
        Log.d(TAG, "***************id**************** "+userId);
        if(imageUri==null){Toast.makeText(getActivity(),"Vous devez choisir une image",Toast.LENGTH_LONG).show();}
        else if(nomProd.getText().toString().equals("")){Toast.makeText(getActivity(),"Vous devez indiquer le nom du produit",Toast.LENGTH_LONG).show();}
        else {
        StorageReference fileRef=storageReference.child(System.currentTimeMillis()+".jpg");

         fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                //String nom= nomProd.getText().toString().trim();
                ////////////////a voir//////////////
                  // String url=taskSnapshot.getStorage().getDownloadUrl().toString();
                taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess( final Uri uri) {
                       final String nom= nomProd.getText().toString().trim();
                        // Got the download URL for 'users/me/profile.png' in uri
                      DocumentReference docUser =fstore.collection("users").document(userId);
                      docUser.addSnapshotListener(getActivity(), new EventListener<DocumentSnapshot>() {
                          @Override
                          public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                              if(documentSnapshot.exists()){
                                 adresse=documentSnapshot.getString("adresse");

                                  DocumentReference documentReference=fstore.collection("dons").document();
                                  Map<String,Object> don=new HashMap<>();
                                  don.put("don",nom);
                                  don.put("user",userId);
                                  don.put("image",uri.toString());
                                  Log.d("tag","!!!!!!!!!!!!!!!!!!!!adresse!!!!!!!!!!!!!!!!"+adresse);
                                  don.put("adresse",adresse);

                                  documentReference.set(don);
                                  /////Move to listFragment
                                  Fragment fg=new listDonFragment();
                                  //dans le fragment on utilise get fragment manager et non pa get support fragment manager
                                  FragmentManager fm=getFragmentManager();
                                  FragmentTransaction ft=fm.beginTransaction();
                                  ft.replace(R.id.fragment_place,fg);
                                  ft.commit();
                              }else {
                                  Log.d("tag", "onEvent: Document do not exists");
                              }
                          }
                      });


                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle any errors
                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
             @Override
             public void onFailure(@NonNull Exception e) {
                 Toast.makeText(getActivity(),"erreur"+e.getMessage(),Toast.LENGTH_LONG).show();
             }
         });

    }
}});

        return v;

    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1000){
            if (resultCode == Activity.RESULT_OK){
                 imageUri=data.getData();
                //image.setImageURI(imageUri);

               Picasso.get().load(imageUri).into(image);

            }

        }
    }

    private void saveImage(Uri imageUri) {
        StorageReference fileRef=storageReference.child("don.jpg");
        fileRef.putFile(imageUri);






    }
}
