package pl.jakubneukirch.mapapp.saved;

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
import pl.jakubneukirch.mapapp.common.CalendarUtils;
import pl.jakubneukirch.mapapp.data.model.dto.RouteDto;

public class SavedRecyclerAdapter extends RecyclerView.Adapter<SavedRecyclerAdapter.ViewHolder> {

    private List<RouteDto> list = new ArrayList<>();

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.mapImageView) ImageView mapImageView;
        @BindView(R.id.dateTextView) TextView dateTextView;
        @BindView(R.id.pointsTextView) TextView pointsTextView;


        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bind(String mapImageUrl, String date, int points){
            Picasso.get()
                    .load(mapImageUrl)
                    .into(mapImageView);
            dateTextView.setText(date);
            pointsTextView.setText(itemView.getContext().getString(R.string.points, points));
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.saved_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String date;
        try{
            date = CalendarUtils.getStringDate(list.get(position).getTimestamp());
        }catch (Exception ex){
            date = "";
        }
        holder.bind(
                list.get(position).getMapImageUrl(),
                date,
                list.get(position).getLocations().size()
        );
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public List<RouteDto> getList() {
        return list;
    }

    public void setList(List<RouteDto> list) {
        this.list = list;
    }
}
