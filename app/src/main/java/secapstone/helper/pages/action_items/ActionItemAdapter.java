package secapstone.helper.pages.action_items;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import secapstone.helper.model.ActionItem;
import secapstone.helper.R;

public class ActionItemAdapter extends FirestoreRecyclerAdapter<ActionItem, ActionItemAdapter.ActionItemHolder> {

    private Context context;
    private StorageReference storageRef = FirebaseStorage.getInstance().getReference();

    public ActionItemAdapter(@NonNull FirestoreRecyclerOptions<ActionItem> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull ActionItemHolder holder, int position, @NonNull ActionItem model) {
        holder.textViewActionItem.setText(model.getAction());
        holder.textViewDate.setText(model.getDate());

        final ActionItemHolder copy = holder;
        final ActionItem modelCopy = model;

        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Do Something
            }
        });

    }

    @NonNull
    @Override
    public ActionItemHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.action_item,
                viewGroup, false);
        return new ActionItemHolder(v);
    }

    class ActionItemHolder extends RecyclerView.ViewHolder {
        TextView textViewActionItem;
        TextView textViewDate;
        ConstraintLayout parent;

        public ActionItemHolder(@NonNull View itemView) {
            super(itemView);

            textViewActionItem = itemView.findViewById(R.id.action_item_text);
            textViewDate = itemView.findViewById(R.id.due_date_text);
            parent = itemView.findViewById(R.id.action_item_parent);
        }
    }
}
