package com.example.ruchit.punjabdadhaba;

import android.graphics.Typeface;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class Main2Activity extends AppCompatActivity {

    ImageView filter;
    fragmetfilter mPasswordFragment = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        FragmentManager fm = getSupportFragmentManager();
        TableListFragment mMenuFragment = (TableListFragment) fm.findFragmentById(R.id.main_frame);
        if (mMenuFragment == null) {
            mMenuFragment = new TableListFragment();
            fm.beginTransaction().add(R.id.main_frame, mMenuFragment).commit();
        }
        setupToolbar();
    }
    protected void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setIcon(R.drawable.ic_search_white_24dp);
        // toolbar.setNavigationIcon(R.drawable.menu);
      /*  toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mDrawer.toggleMenu();

            }

        });*/
    //setContentView(R.layout.view_feed_toolbar);
      TextView tv=findViewById(R.id.title);
        Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/bebasneue_bold.OTF");
        tv.setTypeface(custom_font);
        filter=findViewById(R.id.mfilter);
        filter.setVisibility(View.INVISIBLE);
        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                if (mPasswordFragment==null) {
                    mPasswordFragment = new fragmetfilter();
                    transaction.add(R.id.main_frame, mPasswordFragment);
                }
                else if (mPasswordFragment!=null){
                    transaction.remove(mPasswordFragment);
                    mPasswordFragment=null;
                }
                transaction.commit();
            }
        });



    }
}
