package com.example.chhots.ui.Dashboard.HistoryPackage;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chhots.R;
import com.example.chhots.bottom_navigation_fragments.Explore.See_Video;
import com.example.chhots.category_view.Courses.course_purchase_view;
import com.example.chhots.category_view.routine.routine_purchase;
import com.example.chhots.category_view.routine.routine_view;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {

    private static final String TAG = "HistoryAdapter";
    View BottomNavBar;
    private List<HistoryModel> historylist;
    private Context context;
    private View v;

    public HistoryAdapter(List<HistoryModel> historylist, Context context) {
        this.historylist = historylist;
        this.context = context;
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.raw_history_item, parent, false);
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_dashboard, parent, false);
        BottomNavBar = v.findViewById(R.id.bottom_navigation_dashboard);

        return new HistoryViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        holder.title.setText(historylist.get(position).getTitle());
        holder.dexription.setText(historylist.get(position).getDescription());
        holder.value = historylist.get(position).getId();
        holder.date.setText(historylist.get(position).getDate());
        holder.category = historylist.get(position).getCategory();
        holder.url = historylist.get(position).getUrl();
        if(holder.category.equals("Routine")||holder.category.equals("Course"))
        {
            holder.dexription.setVisibility(View.GONE);
        }
        holder.dexription.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                return true;
            }
        });
        Picasso.get().load(Uri.parse(historylist.get(position).getUrl())).into(holder.image);
    }


    @Override
    public int getItemCount() {
        return historylist.size();
    }


    public class HistoryViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {

        public TextView title, dexription, date;
        ImageView image;
        String value, category,url;
        private final MenuItem.OnMenuItemClickListener onDeleteMenu = new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case 1:
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                        databaseReference.child("History").child(value).removeValue();
                        break;
                }

                return true;
            }
        };


        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.history_username);
            dexription = itemView.findViewById(R.id.history_video_title);
            image = itemView.findViewById(R.id.history_image);
            date = itemView.findViewById(R.id.history_date);
            itemView.setOnCreateContextMenuListener(this);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    BottomNavBar.setVisibility(View.GONE);
                    if (category.equals("Explore")) {
                        Bundle bundle = new Bundle();
                        bundle.putString("videoId", value);
                        setFragment(new See_Video(), bundle);
                    } else if (category.equals("Routine")) {
                        Bundle bundle = new Bundle();
                        bundle.putString("category", "NotVideoView");
                        bundle.putString("instructorId",dexription.getText().toString());
                        bundle.putString("routineId", value);
                        setFragment(new routine_purchase(), bundle);
                    }
                    else if (category.equals("Course")) {
                        Bundle bundle = new Bundle();
                        bundle.putString("courseId", value);
                        bundle.putString("thumbnail",url);
                        bundle.putString("instructorId",dexription.getText().toString());
                        setFragment(new course_purchase_view(), bundle);
                    }

                }
            });
        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            MenuItem delete = contextMenu.add(Menu.NONE, 1, 1, "Delete");
            delete.setOnMenuItemClickListener(onDeleteMenu);
        }

        public void setFragment(Fragment fragment, Bundle bundle) {
            fragment.setArguments(bundle);
            FragmentTransaction fragmentTransaction = ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.dashboard_layout, fragment);
            fragmentTransaction.commit();

        }
    }


}
