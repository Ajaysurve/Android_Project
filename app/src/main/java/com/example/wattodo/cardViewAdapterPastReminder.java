package com.example.wattodo;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class cardViewAdapterPastReminder extends RecyclerView.Adapter<cardViewAdapterPastReminder.cardViewHolderPast> {

    public void setTaskListPast(List<TaskClass> taskListPast) {
        this.taskListPast = taskListPast;
        notifyDataSetChanged();
    }

    private List<TaskClass> taskListPast = new ArrayList<>();
    private cardViewAdapterPastReminder.OnItemClickListener mListenerPast;

    public interface OnItemClickListener {
        void onDeleteClick(int position);
    }
    public void setOnItemClickListener(cardViewAdapterPastReminder.OnItemClickListener onItemClickListener) {
        this.mListenerPast = onItemClickListener;
    }

    @NonNull
    @Override
    public cardViewHolderPast onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_past,parent,false);
        return new cardViewHolderPast(itemView,mListenerPast);
    }

    @Override
    public void onBindViewHolder(@NonNull cardViewHolderPast holder, int position) {
        TaskClass currentTask = taskListPast.get(position);
        holder.textViewTitle.setText(currentTask.getTitle());
        holder.textViewDate.setText(currentTask.getDate());
        holder.textViewTime.setText(currentTask.getTime());
    }

    @Override
    public int getItemCount() {
        return taskListPast.size();
    }

    public TaskClass getAndDeletePos(int position) {
        TaskClass taskClass = taskListPast.get(position);
        taskListPast.remove(position);
        return taskClass;
    }

    public static class cardViewHolderPast extends RecyclerView.ViewHolder {
       private TextView textViewTitle;
       private TextView textViewDate;
       private TextView textViewTime;
       private ImageView mDeleteImage;

       public cardViewHolderPast(@NonNull View itemView, final OnItemClickListener listener) {
           super(itemView);
           textViewTitle = itemView.findViewById(R.id.txt_view_title_past);
           textViewDate = itemView.findViewById(R.id.txt_view_date_past);
           textViewTime = itemView.findViewById(R.id.txt_view_time_past);
           mDeleteImage = itemView.findViewById((R.id.img_delete_past));

           mDeleteImage.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   if(listener!= null )
                   {   int pos = getAdapterPosition();
                       if(pos != RecyclerView.NO_POSITION)
                           listener.onDeleteClick(pos);
                   }
               }
           });
       }
   }
}
