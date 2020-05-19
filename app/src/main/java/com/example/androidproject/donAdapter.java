package com.example.androidproject;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.Constraints;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.example.androidproject.Register.TAG;

public class donAdapter extends RecyclerView.Adapter<donAdapter.DonViewHolder> {
private Context mContext;
private List<Upload> mUploads;
private onItemClickListner mListner;


public donAdapter(Context context,List<Upload> uploads){
    mContext=context;
    mUploads=uploads;

}

    @NonNull
    @Override
    public DonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

View v = LayoutInflater.from(mContext).inflate(R.layout.don_item,parent,false);
        return new DonViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull DonViewHolder holder, final int position) {
Upload uploadCurrent=mUploads.get(position);
        Log.d(Constraints.TAG, "Url image  de don" +uploadCurrent.getImageUrl());
holder.textViewName.setText(uploadCurrent.getName());
        Picasso.get().load(uploadCurrent.getImageUrl()).fit().centerCrop().into(holder.imageView);
        holder.adresse.setText(uploadCurrent.getAdresse());

//holder.reserve.setOnClickListener(this);
    }

    @Override
    public int getItemCount() {
        return mUploads.size();

    }

public interface onItemClickListner{
    void onItemReserve(int position);
    void onItemCall(int position);
}
public void setOnItemClickListner(onItemClickListner listner){
    mListner=listner;

}

    public class DonViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
public TextView textViewName;
public ImageView imageView;
public Button reserve;
public TextView adresse;
        public DonViewHolder(@NonNull final View itemView) {
            super(itemView);
            textViewName=itemView.findViewById(R.id.nomD);
            imageView=itemView.findViewById(R.id.image_view_upload);
            reserve=itemView.findViewById(R.id.reserver);
            adresse=itemView.findViewById(R.id.adresse);
            reserve.setOnClickListener(this);
            ImageView appeler=itemView.findViewById(R.id.btnApl);
            appeler.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListner.onItemCall(getAdapterPosition());
                }
            });


        }


        @Override
        public void onClick(View v) {

            int position=getAdapterPosition();

            mListner.onItemReserve(position);
          //mListner.onItemCall(position);



        }
    }
}
