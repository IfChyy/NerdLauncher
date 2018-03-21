package com.bignerdranch.android.nerdlauncher;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Ivo Georgiev (IfChyy)
 * Singleto class
 * Create fragment abstract method giving a class the ability to call a fragment it wants to display
 * after that SingleFragmentAcitivty is invoked which creates a new fragment manager
 * gets our frame layout with fragment ( here we place the fragment we called)
 * and the beginstransaction ( eg. places our fragment class into fragmentContainer ( frame layout)
 */
public abstract class SingleFragmentActivity extends AppCompatActivity {
        protected abstract Fragment createFragment();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(setLayoutResId());

        //init fragment manager
        FragmentManager fm = getFragmentManager();
        //create the empty fragment container to host our fragment in
        Fragment fragment = fm.findFragmentById(R.id.fragmentContainer);

        //check if container is null
        if(fragment == null){
            //if yes create our new fragment passed by parent class/fragment
            fragment = createFragment();
            //add our fragment to our frame layout we spcified in our activity_fragment
            fm.beginTransaction().add(R.id.fragmentContainer, fragment).commit();
        }

    }
    //get the layout for the view we are hosting our fragment in
    private int setLayoutResId(){
        return R.layout.activity_fragment;
    }
}
