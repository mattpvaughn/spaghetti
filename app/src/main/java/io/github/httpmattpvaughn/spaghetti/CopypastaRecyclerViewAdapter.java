package io.github.httpmattpvaughn.spaghetti;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import io.github.httpmattpvaughn.spaghetti.data.Copypasta;

/**
 * Created by Matt Vaughn: http://mattpvaughn.github.io/
 */
public class CopypastaRecyclerViewAdapter extends RecyclerView.Adapter<CopypastaRecyclerViewAdapter.CopypastaViewHolder> {

    private List<Copypasta> copypastas = new ArrayList<>();
    private HomeContract.CopypastaClickListener clickListener;

    public CopypastaRecyclerViewAdapter(HomeContract.CopypastaClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public CopypastaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row;
        row = LayoutInflater.from(parent.getContext()).inflate(R.layout.copypasta_row, parent, false);
        return new CopypastaViewHolder(row, clickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CopypastaViewHolder holder, int position) {
        holder.setItem(copypastas.get(position));
    }

    @Override
    public long getItemId(int position) {
        return copypastas.get(position).id;
    }

    @Override
    public int getItemCount() {
        return copypastas.size();
    }

    public void setData(List<Copypasta> copypastas) {
        this.copypastas = copypastas;
    }

    public static class CopypastaViewHolder extends RecyclerView.ViewHolder {
        public View item;
        public TextView emoji;
        public TextView copypasta;
        public HomeContract.CopypastaClickListener clickListener;

        public CopypastaViewHolder(View itemView, HomeContract.CopypastaClickListener clickListener) {
            super(itemView);
            this.item = itemView;
            this.emoji = itemView.findViewById(R.id.emoji);
            this.copypasta = itemView.findViewById(R.id.copypasta);
            this.clickListener = clickListener;
        }

        public void setItem(Copypasta item) {
            this.emoji.setText(item.emoji);
            if (item.content != null) {
                this.copypasta.setText(item.content.substring(0, Math.min(item.content.length(), 300)));
            } else {
                this.copypasta.setText(null);
            }
            this.item.setOnClickListener(v -> clickListener.onRowClick(item));
            this.item.setOnLongClickListener(v -> clickListener.onRowLongClick(item));
        }
    }
}
