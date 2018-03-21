package com.bignerdranch.android.nerdlauncher;

import android.app.Fragment;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Ivo Georgiev(IfChyy)
 * NerdLauncherFragment hosting our layout for the recyclerview
 * displaying all the apps in the launcher
 */

public class NerdLauncherFragment extends Fragment {
    private static final String TAG = "NerdLauncherFragment";


    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        //init the view holding the layout of the fragment with recycler view
        View view = inflater.inflate(R.layout.fragment_nerd_launcher, container, false);

        //init the recycler view
        recyclerView = view.findViewById(R.id.fragment_nerd_launcher_recycler_view);
        //set recyclerviews linear layout manager
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        setupAdapter();
        return view;
    }


    //call the fragment from another activity to launch it
    public static NerdLauncherFragment newInstance() {
        return new NerdLauncherFragment();
    }

    public void setupAdapter(){
        //start an intent loocking for apps which have a MAIN ACTIVITY/INTNET
        Intent startupIntent = new Intent(Intent.ACTION_MAIN);
        startupIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        //go through all the packages and find the apps wchi can be launched and add them to a list
        PackageManager pm = getActivity().getPackageManager();
        List<ResolveInfo> activities = pm.queryIntentActivities(startupIntent, 0);

        //sort the objects returned by the list alphabeticaly
        Collections.sort(activities, new Comparator<ResolveInfo>() {
            @Override
            public int compare(ResolveInfo a, ResolveInfo b) {
                PackageManager pm = getActivity().getPackageManager();
                return String.CASE_INSENSITIVE_ORDER.compare(
                        a.loadLabel(pm).toString(),
                        b.loadIcon(pm).toString());
            }
        });


        Log.d(TAG, "found : " + activities.size());
        ActivityAdapter adapter = new ActivityAdapter(activities);
        recyclerView.setAdapter(adapter);
    }


    //----------------------CLASS VIEWHOLDER
    // used for holding each individual item and its properties in our recyclerview
    private class ActivityHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private ResolveInfo resolveInfo;
        private TextView nameTextView;

        //init the textview which is going to hold the name of our app to lauchn
        public ActivityHolder(View itemView) {
            super(itemView);
            nameTextView = (TextView) itemView.findViewById(R.id.appName);
        }

        public void bindActivity(ResolveInfo resolveInfo){
            //init the resolveInfo instance
            this.resolveInfo = resolveInfo;
            //init the package manager to get the label of the app
            PackageManager pm = getActivity().getPackageManager();
            //app name string holding each app's name
            String appName = resolveInfo.loadLabel(pm).toString();
            //set the text view text with the app's name
            nameTextView.setText(appName);
            nameTextView.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            //get package name of desired clicked app and its class name
            //from resolveinfo's activity info
            ActivityInfo activityInfo = resolveInfo.activityInfo;

            //create an explicit intent pointing to our new app's main
            Intent in = new Intent(Intent.ACTION_MAIN)
                    .setClassName(activityInfo.applicationInfo.packageName,
                            activityInfo.name)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            startActivity(in);

        }
    }

    //----------------------ADAPTER CLASS FOR THE RECYCLER VIEW TO POPULATE OUR LIST
    private class ActivityAdapter extends RecyclerView.Adapter<ActivityHolder> {
        private final List<ResolveInfo> activities;

        private TextView nameTextView;
        //init the adapter with the list of activities
        private ActivityAdapter(List<ResolveInfo> activities) {
            this.activities = activities;
        }
        //create the view which represents each item in our list
        //here it is just a simplelistitem from android
        @NonNull
        @Override
        public ActivityHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
           LayoutInflater inflater = LayoutInflater.from(getActivity());
           View view = inflater.inflate(R.layout.list_item_app, parent, false);

           return new ActivityHolder(view);

        }
        //bind the infromation for each position in our holder
        @Override
        public void onBindViewHolder(@NonNull ActivityHolder holder, int position) {
            ResolveInfo resolveInfo = activities.get(position);
            holder.bindActivity(resolveInfo);
        }
        //get the number of items in our list
        @Override
        public int getItemCount() {
            return activities.size();
        }
    }




}
