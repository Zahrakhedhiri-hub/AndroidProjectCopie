package com.example.androidproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import static com.example.androidproject.Register.TAG;


public class AddDelandeFragment extends Fragment {
    private FirebaseAuth firebaseAuth;
    private FirebaseStorage firebaseStorage;
    private FirebaseFirestore firestore;
    StorageReference storageReference;
    String userId;
    Uri imageUri;
    ImageView image;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_add_delande, container, false);

        //////recuperer les composants//////
        final EditText nomDemande = v.findViewById(R.id.nomDemande);
        image = v.findViewById(R.id.imageDemande);
        Button addDemande = v.findViewById(R.id.addDemande);
        Button addImage = v.findViewById(R.id.addImageDemande);
        /////????????????????//////////
        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference("images");


        //////////////l'ajou d'une image////////
        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(openGallery, 100);
            }
        });
////////////ajouter une demande//////
        addDemande.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageUri == null) {
                    if (imageUri == null) {
                        Toast.makeText(getActivity(), "Vous devez choisir une image", Toast.LENGTH_LONG).show();
                    }

                } else if (nomDemande.getText().toString().equals("")) {

                        Toast.makeText(getActivity(), "Vous devez saisir le nom de produit", Toast.LENGTH_LONG).show();

                } else {

                    userId = firebaseAuth.getCurrentUser().getUid();
                    StorageReference fileRef = storageReference.child(System.currentTimeMillis() + ".jpg");

                    fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                            taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String nom = nomDemande.getText().toString().trim();
                                    // Got the download URL for 'users/me/profile.png' in uri

                                    DocumentReference documentReference = firestore.collection("demandes").document();
                                    Map<String, Object> don = new HashMap<>();
                                    don.put("demande", nom);
                                    don.put("user", userId);
                                    don.put("image", uri.toString());

                                    documentReference.set(don);
                                    /////Move to listFragment
                                    Fragment fg = new listDemandeFragment();
                                    //dans le fragment on utilise get fragment manager et non pa get support fragment manager
                                    FragmentManager fm = getFragmentManager();
                                    FragmentTransaction ft = fm.beginTransaction();
                                    ft.replace(R.id.fragment_place, fg);
                                    ft.commit();
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
                            Toast.makeText(getActivity(), "erreur" + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });

                }
            }
        });


        //final step
        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            imageUri = data.getData();
            Picasso.get().load(imageUri).into(image);
        }

    }
}
