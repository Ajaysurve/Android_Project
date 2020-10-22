package com.example.wattodo;

import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

public class cardViewAdapter extends RecyclerView.Adapter<cardViewAdapter.cardViewHolder> {

    private List<TaskClass> taskList = new ArrayList<>();
    private OnItemClickListener mListener;
    public static final String TAG = cardViewHolder.class.getSimpleName();

    public TaskClass getAndDeletePos(int position) {
        TaskClass taskClass = taskList.get(position);
        taskList.remove(position);
        return taskClass;
    }

    public interface OnItemClickListener{
        void onDeleteClick(int position);
        void onCheckBoxClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mListener = onItemClickListener;
    }

    @NonNull
    @Override
    public cardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview,parent,false);
        return new cardViewHolder(itemView,mListener);
    }

    public void setTaskList(List<TaskClass> taskList)  {
        this.taskList = taskList;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull cardViewHolder holder, int position) {
        TaskClass currentTask = taskList.get(position);
        holder.textViewTitle.setText(currentTask.getTitle());
        holder.textViewDate.setText(currentTask.getDate());
        holder.textViewTime.setText(currentTask.getTime());
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public static class cardViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewTitle;
        private TextView textViewDate;
        private TextView textViewTime;
        private ImageView mDeleteImage;
        private CheckBox checkBox;

        public cardViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.txt_view_title);
            textViewDate = itemView.findViewById(R.id.txt_view_date);
            textViewTime = itemView.findViewById(R.id.txt_view_time);
            mDeleteImage = itemView.findViewById(R.id.img_delete);
            checkBox = itemView.findViewById(R.id.checkBox);

            mDeleteImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(listener!= null )
                         {   int pos = getAdapterPosition();
                               Log.d(TAG, "inside mDeleteImage - Position "+pos);
                              if(pos != RecyclerView.NO_POSITION)
                                   listener.onDeleteClick(pos);
                         }
                    }
             });
            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener!= null )
                    {   int pos = getAdapterPosition();
                        Log.d(TAG, "inside Checkbox - Position "+pos);
                        if(pos != RecyclerView.NO_POSITION)
                            listener.onCheckBoxClick(pos);
                    }
                }
            });

        }
    }
}
