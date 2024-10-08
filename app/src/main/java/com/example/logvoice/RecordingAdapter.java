package com.example.logvoice;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecordingAdapter extends RecyclerView.Adapter<RecordingAdapter.ViewHolder> {

    private List<Recording> recordings;

    public RecordingAdapter(List<Recording> recordings) {
        this.recordings = recordings;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recording, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Recording recording = recordings.get(position);
        holder.titleTextView.setText(recording.getTitle());
        holder.timestampTextView.setText(recording.getTimestamp());

        // Set onClickListener for the Open button
        holder.itemView.findViewById(R.id.open).setOnClickListener(v -> {
            Intent intent = new Intent(holder.itemView.getContext(), DisplayActivity.class);
            intent.putExtra("filePath", recording.getFilePath());
            intent.putExtra("title", recording.getTitle());
            intent.putExtra("timestamp", recording.getTimestamp());
            holder.itemView.getContext().startActivity(intent);
        });
    }


    @Override
    public int getItemCount() {
        return recordings.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView timestampTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.recording_title);
            timestampTextView = itemView.findViewById(R.id.recording_timestamp);
        }
    }
}