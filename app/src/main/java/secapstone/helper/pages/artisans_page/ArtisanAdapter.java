package secapstone.helper.pages.artisans_page;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import android.content.Intent;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.Context;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import de.hdodenhof.circleimageview.CircleImageView;
import secapstone.helper.model.Artisan;
import secapstone.helper.R;
import secapstone.helper.pages.view_artisan.ViewArtisanActivity;

public class ArtisanAdapter extends FirestoreRecyclerAdapter<Artisan, ArtisanAdapter.ArtisanHolder> {

    private Context context;
    private StorageReference storageRef = FirebaseStorage.getInstance().getReference();
    private CollectionReference artisansRef;
    private ArtisansFragment frag;


    public ArtisanAdapter(@NonNull FirestoreRecyclerOptions<Artisan> options, Context context, CollectionReference artisansRef, ArtisansFragment frag) {
        super(options);
        this.context = context;
        this.artisansRef = artisansRef;
        this.frag = frag;
    }

    @Override
    protected void onBindViewHolder(@NonNull ArtisanHolder holder, int position, @NonNull Artisan model) {
        holder.textViewDescription.setText(model.getDescription());
        holder.textViewName.setText(String.valueOf(model.getName()));

        Log.d("TAG", ""+ position);

        final ArtisanHolder copy = holder;
        final Artisan modelCopy = model;

        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onTileClick(modelCopy);
            }
        });
        holder.parent.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view){
                frag.showDeleteArtisanModel(modelCopy);
                return true;
            }
        });

        if(model.getPictureURL()!= null) {
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
        } else {
            copy.image.setImageResource(R.drawable.icon_empty_person);
        }

    }

    private void onTileClick(Artisan model) {
        Intent intent = new Intent(context, ViewArtisanActivity.class);
        intent.putExtra("url", model.getPictureURL());
        intent.putExtra("name", model.getName());
        intent.putExtra("description", model.getDescription());
        intent.putExtra("phone", model.getPhoneNumber());
        intent.putExtra("address", model.getAddress());
        intent.putExtra("id", model.getID());
        intent.putExtra("moneyOwed", model.getMoneyOwedFromCommunityLeader());
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
