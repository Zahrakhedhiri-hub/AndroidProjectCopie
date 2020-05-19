package com.example.androidproject;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class ReservationAdapter extends RecyclerView.Adapter<ReservationAdapter.ReservationViewHolder>{
    private Context mContext;
    private List<Upload> mUploads;
    private onItemClickListner mListner;


    public ReservationAdapter(Context context,List<Upload> uploads){
        mContext=context;
        mUploads=uploads;

    }

    @NonNull
    @Override
    public ReservationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(mContext).inflate(R.layout.item_reservation,parent,false);
        return new ReservationViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ReservationViewHolder holder, final int position) {
        Upload uploadCurrent=mUploads.get(position);
        holder.textViewName.setText(uploadCurrent.getName());

        Picasso.get().load(uploadCurrent.getImageUrl()).fit().centerCrop().into(holder.imageView);
        Log.d(TAG, "Url de Adapter" +uploadCurrent.getImageUrl());

//holder.reserve.setOnClickListener(this);
    }

    @Override
    public int getItemCount() {
        return mUploads.size();
    }

    public interface onItemClickListner{
        void annule(int position);
        void supprimer(int position);
    }
    public void setOnItemClickListner(onItemClickListner listner){
        mListner=listner;

    }

    public class ReservationViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView textViewName;
        public ImageView imageView;
        public Button annuler;
        public Button supprimer;
        public ReservationViewHolder(@NonNull final View itemView) {
            super(itemView);
            textViewName=itemView.findViewById(R.id.nomRes);
            imageView=(ImageView) itemView.findViewById(R.id.imageRes);
            annuler=itemView.findViewById(R.id.annuler);
            annuler.setOnClickListener(this);
             supprimer=itemView.findViewById(R.id.supprimer);
            supprimer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListner.supprimer(getAdapterPosition());
                }
            });


        }


        @Override
        public void onClick(View v) {

            int position=getAdapterPosition();

            mListner.annule(position);
            //mListner.onItemCall(position);



        }
    }
}
