package com.example.androidproject;

import android.content.Context;
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

public class demandeAdapter extends RecyclerView.Adapter<demandeAdapter.DemandeViewHolder> {
private Context mContext;
private List<UploadDemande> mUploads;
private onItemClickListner mListner;


public demandeAdapter(Context context,List<UploadDemande> uploads){
        mContext=context;
        mUploads=uploads;

        }

@NonNull
@Override
public DemandeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(mContext).inflate(R.layout.demande_item,parent,false);
        return new DemandeViewHolder(v);
        }

@Override
public void onBindViewHolder(@NonNull DemandeViewHolder holder, final int position) {
    UploadDemande uploadCurrent=mUploads.get(position);
        holder.textViewName.setText(uploadCurrent.getName());
        Picasso.get().load(uploadCurrent.getImageUrl()).fit().centerCrop().into(holder.imageView);

//holder.reserve.setOnClickListener(this);
        }

@Override
public int getItemCount() {
        return mUploads.size();
        }

public interface onItemClickListner{
    void reserveD(int position);
    void onItemCall(int position);
}
    public void setOnItemClickListner(onItemClickListner listner){
        mListner=listner;

    }

public class DemandeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView textViewName;
    public ImageView imageView;
    public Button reserve;
    public DemandeViewHolder(@NonNull final View itemView) {
        super(itemView);
        textViewName=itemView.findViewById(R.id.nomDemande);
        imageView=(ImageView) itemView.findViewById(R.id.image_view_upload);
        reserve=itemView.findViewById(R.id.ajouter);
        reserve.setOnClickListener(this);
        ImageView appeler=itemView.findViewById(R.id.btnAplD);
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

        mListner.reserveD(position);
        //mListner.onItemCall(position);



    }
}
}
