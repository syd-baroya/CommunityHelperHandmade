package secapstone.helper;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import android.net.Uri;
import android.support.annotation.NonNull;
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
    protected void onBindViewHolder(@NonNull final ArtisanHolder holder, int position, @NonNull Artisan model) {
        holder.textViewPhoneNumber.setText(model.getPhoneNumber());
        holder.textViewDescription.setText(model.getDescription());
        holder.textViewPictureURL.setText(String.valueOf(model.getPictureURL()));
        holder.textViewName.setText(String.valueOf(model.getName()));

        storageRef.child(model.getPictureURL()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context)
                        .asBitmap()
                        .load(uri)
                        .into(holder.image);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });


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
        TextView textViewPhoneNumber;
        TextView textViewPictureURL;
        CircleImageView image;

        public ArtisanHolder(@NonNull View itemView) {
            super(itemView);

            textViewName = itemView.findViewById(R.id.text_view_name);
            textViewDescription = itemView.findViewById(R.id.text_view_description);
            textViewPhoneNumber = itemView.findViewById(R.id.text_view_phonenumber);
            textViewPictureURL = itemView.findViewById(R.id.text_view_pictureurl);
            image = itemView.findViewById(R.id.image);
        }
    }
}
