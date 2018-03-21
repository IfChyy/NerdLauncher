package com.bignerdranch.android.nerdlauncher;

import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class NerdLauncherActivity extends SingleFragmentActivity {

    //init our nerd launcher fragment and display it in our activity
    @Override
    protected Fragment createFragment() {
        return NerdLauncherFragment.newInstance();
    }

}
