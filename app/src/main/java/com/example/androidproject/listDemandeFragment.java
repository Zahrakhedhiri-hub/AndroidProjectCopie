package com.example.androidproject;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import static com.example.androidproject.Register.TAG;


public class listDemandeFragment extends Fragment implements demandeAdapter.onItemClickListner {
    private RecyclerView mRecyclerView;
    private demandeAdapter demandeAdapter;
    private FirebaseFirestore firestore;
    private List<UploadDemande> mUploads;
    private DocumentReference documentReference;
    private CollectionReference collectionReference;
    private FirebaseStorage firebaseStorage;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_list_demande, container, false);
        mRecyclerView = v.findViewById(R.id.recycler_demande);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        mUploads = new ArrayList<>();
        firestore = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        firestore.collection("demandes").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    for (QueryDocumentSnapshot document : task.getResult()) {
                        //Log.d(TAG, "list de don" + document.getId() + " => " + document.getData());


                        String id = document.getId();
                        //upload.setKey(id);


                        Object nom = document.getData().get("demande");
                        String a = nom.toString();
                        Object image = document.getData().get("image");
                        String b = image.toString();
                        Object user=document.getData().get("user");
                        String u=user.toString();
                        UploadDemande upload = new UploadDemande(a, b,u);
                        upload.setKey(id);
                        Log.d(TAG, "id image" + upload.getKey());

                        mUploads.add(upload);


                    }
                    demandeAdapter = new demandeAdapter(requireContext(), mUploads);
                    mRecyclerView.setAdapter(demandeAdapter);

                    demandeAdapter.setOnItemClickListner(listDemandeFragment.this);
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });

        return v;
    }


    @Override
    public void reserveD(int position) {

        UploadDemande selectedItem = mUploads.get(position);
        String selectedKey = selectedItem.getKey();
        //mUploads.remove(position);
        Log.d(TAG, "image choisie " + selectedKey);
        Log.d(TAG, "lien " + selectedItem.getImageUrl());
        StorageReference imageRef = firebaseStorage.getReferenceFromUrl(selectedItem.getImageUrl());
        imageRef.delete();
        firestore.collection("demandes").document(selectedKey).delete();

        Fragment fg=new listDemandeFragment();
        //dans le fragment on utilise get fragment manager et non pa get support fragment manager
        FragmentManager fm=getFragmentManager();
        FragmentTransaction ft=fm.beginTransaction();
        ft.replace(R.id.fragment_place,fg);
        ft.commit();

    }

    @Override
    public void onItemCall(int position) {
        UploadDemande selectedItem = mUploads.get(position);
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
