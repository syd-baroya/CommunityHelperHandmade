package secapstone.helper;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.Context;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.saga.communityhelperhandmade.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class ArtisanAdapter extends FirestoreRecyclerAdapter<Artisan, ArtisanAdapter.ArtisanHolder> {

    private Context context;
    private StorageReference storageRef = FirebaseStorage.getInstance().getReference();

    public ArtisanAdapter(@NonNull FirestoreRecyclerOptions<Artisan> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull ArtisanHolder holder, int position, @NonNull Artisan model) {
        holder.textViewDescription.setText(model.getDescription());
        holder.textViewName.setText(String.valueOf(model.getName()));

        final ArtisanHolder copy = holder;
        final Artisan modelCopy = model;

        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onTileClick(modelCopy);
            }
        });

        if(model.getPictureURL()!=null) {
            storageRef.child(model.getPictureURL()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Glide.with(context)
                            .asBitmap()
                            .load(uri)
                            .into(copy.image);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                }
            });
        }

    }

    private void onTileClick(Artisan model) {
        Intent intent = new Intent(context, ViewArtisanActivity.class);
        intent.putExtra("url", model.getPictureURL());
        intent.putExtra("name", model.getName());
        intent.putExtra("description", model.getDescription());
        context.startActivity(intent);
    }



    @NonNull
    @Override
    public ArtisanHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.artisan_item,
                viewGroup, false);
        return new ArtisanHolder(v);
    }

    class ArtisanHolder extends RecyclerView.ViewHolder {
        TextView textViewName;
        TextView textViewDescription;
        CircleImageView image;
        ConstraintLayout parent;

        public ArtisanHolder(@NonNull View itemView) {
            super(itemView);

            textViewName = itemView.findViewById(R.id.text_view_name);
            textViewDescription = itemView.findViewById(R.id.text_view_description);
            image = itemView.findViewById(R.id.image);
            parent = itemView.findViewById(R.id.artisan_list_parent);
        }
    }
}
