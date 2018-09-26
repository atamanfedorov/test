package rus.myapplication;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

public class Navigator {

    private AppCompatActivity mAppCompatActivity;

    public Navigator(AppCompatActivity appCompatActivity)
    {
        mAppCompatActivity = appCompatActivity;
    }

    public void showFragment(Fragment fragment) {
        mAppCompatActivity.getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame, fragment)
                .addToBackStack(null)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit();
    }


}
