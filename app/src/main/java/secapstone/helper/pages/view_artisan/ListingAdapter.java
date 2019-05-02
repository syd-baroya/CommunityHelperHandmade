package secapstone.helper.pages.view_artisan;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import de.hdodenhof.circleimageview.CircleImageView;
import secapstone.helper.R;
import secapstone.helper.model.Artisan;
import secapstone.helper.model.Listing;

public class ListingAdapter extends FirestoreRecyclerAdapter<Listing, ListingAdapter.ListingHolder> {

    private Context context;
    private StorageReference storageRef = FirebaseStorage.getInstance().getReference();


    public ListingAdapter(@NonNull FirestoreRecyclerOptions<Listing> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull ListingHolder holder, int position, @NonNull Listing model) {
        holder.textViewDescription.setText(model.getDescription());
        holder.textViewTitle.setText(String.valueOf(model.getTitle()));

        final ListingHolder copy = holder;
        final Listing modelCopy = model;

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

    @NonNull
    @Override
    public ListingHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.listing_item,
                viewGroup, false);
        return new ListingHolder(v);
    }

    class ListingHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle;
        TextView textViewDescription;
        ImageView image;
        ConstraintLayout parent;

        public ListingHolder(@NonNull View itemView) {
            super(itemView);

            textViewTitle = itemView.findViewById(R.id.text_view_title);
            textViewDescription = itemView.findViewById(R.id.text_view_description);
            image = itemView.findViewById(R.id.image);
            parent = itemView.findViewById(R.id.listing_list_parent);
        }
    }
}
