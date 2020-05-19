package com.example.androidproject;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.example.androidproject.Register.TAG;


public class listDonFragment extends Fragment implements donAdapter.onItemClickListner {
    private RecyclerView mRecyclerView;
    private donAdapter donAdapter;
    private FirebaseFirestore firestore;
    private List<Upload> mUploads;
    private DocumentReference documentReference;
    private CollectionReference collectionReference;
    private FirebaseStorage firebaseStorage;
    private FirebaseAuth fireAuth;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_list_don, container, false);
        mRecyclerView = v.findViewById(R.id.recycler_don);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        mUploads = new ArrayList<>();
        firestore = FirebaseFirestore.getInstance();
fireAuth=FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        firestore.collection("dons").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d(TAG, "list de don" + document.getId() + " => " + document.getData());


                        String id = document.getId();
                        //upload.setKey(id);


                        Object nom = document.getData().get("don");
                        String a = nom.toString();
                        Object image = document.getData().get("image");
                        String b = image.toString();
                        Object user=document.getData().get("user");
                        String u=user.toString();
                        Object adr=document.getData().get("adresse");
                        String d=adr.toString();
                       Upload upload = new Upload(a, b,u,d);
                        upload.setKey(id);
                        Log.d(TAG, "id image" + upload.getKey());
                        mUploads.add(upload);


                    }
                    donAdapter = new donAdapter(requireContext(), mUploads);
                    mRecyclerView.setAdapter(donAdapter);

                    donAdapter.setOnItemClickListner(listDonFragment.this);
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });


        return v;
    }


    @Override
    public void onItemReserve(int position) {
String userId=fireAuth.getCurrentUser().getUid();
        Upload selectedItem = mUploads.get(position);
        String selectedKey = selectedItem.getKey();
        String nom=selectedItem.getName();
        String imageUrl=selectedItem.getImageUrl();
        String user=selectedItem.getUser();
        String preneur=userId;
        Reservation reservation=new Reservation(nom,imageUrl,user,preneur);
        Log.d(TAG, "userIdAuth " + userId);
        Log.d(TAG, "AncienUser " + user);
        if(userId.equals(user)){
            Toast.makeText(getActivity(), "Vous ne pouvez pa reserver un produit dont vous êtes proprietaire", Toast.LENGTH_SHORT).show();
        }else{
        DocumentReference documentReference=firestore.collection("reservation").document();
        documentReference.set(reservation);
        //mUploads.remove(position);
        Log.d(TAG, "image choisie " + selectedKey);
        Log.d(TAG, "lien " + selectedItem.getImageUrl());
        //StorageReference imageRef = firebaseStorage.getReferenceFromUrl(selectedItem.getImageUrl());
        //imageRef.delete();
        firestore.collection("dons").document(selectedKey).delete();

        Fragment fg=new listDonFragment();
        //dans le fragment on utilise get fragment manager et non pa get support fragment manager
        FragmentManager fm=getFragmentManager();
        FragmentTransaction ft=fm.beginTransaction();
        ft.replace(R.id.fragment_place,fg);
        ft.commit();}

    }

    @Override
    public void onItemCall(int position) {
        Upload selectedItem = mUploads.get(position);
        String selectedKey = selectedItem.getKey();
        String phoneNumber;
String userId=selectedItem.getUser();
        Log.d(TAG, "id " + selectedItem.getUser());
        DocumentReference docRef = firestore.collection("users").document(userId);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                     String  phoneNumber=document.getData().get("phone").toString();
                        Log.d(TAG, "Numero de telephone " + phoneNumber);
                        Intent callIntent = new Intent(Intent.ACTION_DIAL);
                        callIntent.setData(Uri.parse("tel:"+phoneNumber));
                        startActivity(callIntent);
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
        //Log.d(TAG, "Userrrrrr " + user);


    }
}
