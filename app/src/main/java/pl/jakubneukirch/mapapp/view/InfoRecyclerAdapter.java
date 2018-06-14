package pl.jakubneukirch.mapapp.view;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.jakubneukirch.mapapp.R;
import pl.jakubneukirch.mapapp.common.PlacesUtils;
import pl.jakubneukirch.mapapp.data.model.api.PlaceDetails;

public class InfoRecyclerAdapter extends RecyclerView.Adapter<InfoRecyclerAdapter.ViewHolder> {

    private RecyclerView recyclerView = null;
    private List<PlaceDetails> list = new ArrayList<>();

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.infoImageView) ImageView imageView;
        @BindView(R.id.infoNameTextView) TextView nameTextView;
        @BindView(R.id.infoAddressTextView) TextView addressTextView;
        @BindView(R.id.infoPositionTextView) TextView positionTextView;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bind(String photoUrl, String name, String address, String positionText) {
            Picasso.get()
                    .load(photoUrl)
                    .error(R.drawable.baseline_broken_image_black_48)
                    .into(imageView);
            nameTextView.setText(name);
            addressTextView.setText(address);
            positionTextView.setText(positionText);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_info, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String url = "none";
        final String name;
        final String address;
        if (list.get(position).getResult() == null) {
            name = getString(R.string.no_name);
            address = getString(R.string.no_address);
        } else {
            if (list.get(position).getResult().getPhotos() != null &&
                    list.get(position).getResult().getPhotos().size() > 0
                    ) {
                url = list.get(position).getResult().getPhotos().get(0).getPhotoReference();
                url = PlacesUtils.getPhotoUrl(url);
            }
            name = list.get(position).getResult().getName();
            address = list.get(position).getResult().getAddress();
        }
        holder.bind(
                url,
                name,
                address,
                position == 0 ? getString(R.string.start_position) : getString(R.string.end_position)
        );

    }

    private String getString(int stringId) {
        if (recyclerView == null) {
            return "";
        }
        return recyclerView.getContext().getString(stringId);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public List<PlaceDetails> getList() {
        return list;
    }

    public void setList(List<PlaceDetails> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
        super.onAttachedToRecyclerView(recyclerView);
    }
}
