package com.inhatc.listapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Consumer;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

public class ListViewAdapter extends ListAdapter<ListViewAdapterData, ListViewAdapter.ListItemViewHolder> {

    private Consumer<ListViewAdapterData> onClickListener;


    protected ListViewAdapter() {
        super(new DiffUtil.ItemCallback<ListViewAdapterData>() {
            @Override
            public boolean areItemsTheSame(@NonNull ListViewAdapterData oldItem, @NonNull ListViewAdapterData newItem) {
                return oldItem.getId() == newItem.getId();
            }

            @Override
            public boolean areContentsTheSame(@NonNull ListViewAdapterData oldItem, @NonNull ListViewAdapterData newItem) {
                return oldItem.getContent().equals(newItem.getContent()) &&
                        oldItem.getKeyword().equals(newItem.getKeyword()) &&
                        oldItem.getTitle().equals(newItem.getTitle()) &&
                        oldItem.getPlace().equals(newItem.getPlace()) &&
                        oldItem.getImg().equals(newItem.getImg());
            }

            @Nullable
            @Override
            public Object getChangePayload(@NonNull ListViewAdapterData oldItem, @NonNull ListViewAdapterData newItem) {
                return new Object();
            }
        });
    }

    public void setOnClickListener(Consumer<ListViewAdapterData> onClickListener) {
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public ListItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ListItemViewHolder(inflater.inflate(R.layout.item_listview, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ListItemViewHolder holder, int position) {
        ListViewAdapterData data = getItem(position);

        holder.tvTitle.setText(data.getTitle());
        holder.tvContent.setText(data.getContent());
        holder.tvKeyword.setText(data.getKeyword());

        holder.itemView.setOnClickListener(v -> {
            if (onClickListener != null) {
                onClickListener.accept(data);
            }
        });
    }


    static class ListItemViewHolder extends RecyclerView.ViewHolder {

        TextView tvId;
        TextView tvTitle;
        TextView tvContent;
        TextView tvKeyword;

        public ListItemViewHolder(@NonNull View itemView) {
            super(itemView);

            tvId = (TextView) itemView.findViewById(R.id.item_id);
            tvTitle = (TextView) itemView.findViewById(R.id.item_title);
            tvContent = (TextView) itemView.findViewById(R.id.item_content);
            tvKeyword = (TextView) itemView.findViewById(R.id.item_keyword);
        }
    }
}
