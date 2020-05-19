package com.example.androidproject;

import android.content.ClipData;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static androidx.constraintlayout.widget.Constraints.TAG;


public class reservationFragment extends Fragment implements ReservationAdapter.onItemClickListner{
    private RecyclerView mRecyclerView;
    private ReservationAdapter reservationAdapter;
    private FirebaseFirestore firestore;
    private List<Upload> mUploads;
    private DocumentReference documentReference;
    private CollectionReference collectionReference;
    private FirebaseStorage firebaseStorage;
    private FirebaseAuth fireAuth;
    String userId;
    String adresse;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_reservation, container, false);
        mRecyclerView = v.findViewById(R.id.recycler_reservation);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        mUploads = new ArrayList<>();
        firestore = FirebaseFirestore.getInstance();
        fireAuth=FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        userId=fireAuth.getCurrentUser().getUid();

        firestore.collection("reservation").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    for (QueryDocumentSnapshot document : task.getResult()) {



                        String id = document.getId();
                        //upload.setKey(id);

//imageUrl;
                        //preneur //user //name
                        Object name = document.getData().get("name");
                        String a = name.toString();
                        Object image = document.getData().get("imageUrl");
                        String b = image.toString();
                        Object user=document.getData().get("user");
                        String u=user.toString();
                        Object preneur=document.getData().get("preneur");
                        String d=preneur.toString();
                        Upload upload = new Upload(a, b,u,d);
                        upload.setKey(id);
                        if(d.equals(userId)){
                            Log.d(TAG, "Url de l image" +b);
                        mUploads.add(upload);
                        }

                    }
                    reservationAdapter = new ReservationAdapter(requireContext(),mUploads);
                    mRecyclerView.setAdapter(reservationAdapter);

                   reservationAdapter.setOnItemClickListner(reservationFragment.this);
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });


        return v;
    }


    @Override
    public void annule(int position) {
        final String userId=fireAuth.getCurrentUser().getUid();
        Upload selectedItem = mUploads.get(position);
        final String selectedKey = selectedItem.getKey();
        final String nom=selectedItem.getName();
        final  String imageUrl=selectedItem.getImageUrl();
        final String user=selectedItem.getUser();
        DocumentReference docUser =firestore.collection("users").document(user);
        docUser.addSnapshotListener(getActivity(), new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if(documentSnapshot.exists()){
                    adresse=documentSnapshot.getString("adresse");
                    Map<String,Object> don=new HashMap<>();
                    don.put("don",nom);
                    don.put("user",user);
                    don.put("image",imageUrl);
                    don.put("adresse",adresse);
                    String preneur=userId;
                    Log.d(TAG,"adresse"+adresse);
                    Log.d(TAG,"don"+nom);
                    Log.d(TAG,"adresse"+adresse);
                    Log.d(TAG,"image"+imageUrl);
                    DocumentReference documentReference=firestore.collection("dons").document();
                    documentReference.set(don);
                    //mUploads.remove(position);


                    firestore.collection("reservation").document(selectedKey).delete();

                    Fragment fg=new reservationFragment();
                    //dans le fragment on utilise get fragment manager et non pa get support fragment manager
                    FragmentManager fm=getFragmentManager();
                    FragmentTransaction ft=fm.beginTransaction();
                    ft.replace(R.id.fragment_place,fg);
                    ft.commit();

                }}});



    }

    @Override
    public void supprimer(int position) {
        Upload selectedItem=mUploads.get(position);
        String selectedKey = selectedItem.getKey();
        String imageUrl=selectedItem.getImageUrl();
        StorageReference imageRef = firebaseStorage.getReferenceFromUrl(imageUrl);
        imageRef.delete();
        firestore.collection("reservation").document(selectedKey).delete();
        Fragment fg=new reservationFragment();
        //dans le fragment on utilise get fragment manager et non pa get support fragment manager
        FragmentManager fm=getFragmentManager();
        FragmentTransaction ft=fm.beginTransaction();
        ft.replace(R.id.fragment_place,fg);
        ft.commit();
    }
}


