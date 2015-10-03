package com.swimo.sharemoment;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.parse.ParseUser;
import com.swimo.sharemoment.animation.GuillotineAnimation;

//import butterknife.ButterKnife;
//import butterknife.InjectView;

public class MainActivity extends AppCompatActivity {
    private static final long RIPPLE_DURATION = 500;


   /* @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.root)
    FrameLayout root;
    @InjectView(R.id.content2)
    View contentHamburger;*/

LinearLayout Pub,prof,fav,out,ll1,ll2,ll3;
    Toolbar toolbar;
    View guillotineMenu;
    FrameLayout root;
    View contentHamburger;
    boolean x=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       // ButterKnife.inject(this);
        ll1=(LinearLayout)findViewById(R.id.ll1);
        ll2=(LinearLayout)findViewById(R.id.ll2);
        ll3=(LinearLayout)findViewById(R.id.ll3);
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        root=(FrameLayout)findViewById(R.id.root);
        contentHamburger=(View)findViewById(R.id.content2);

        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle(null);
        }

       guillotineMenu = LayoutInflater.from(this).inflate(R.layout.guillotine, null);
        root.addView(guillotineMenu);

         Pub=(LinearLayout)guillotineMenu.findViewById(R.id.feed_group);
        prof=(LinearLayout)guillotineMenu.findViewById(R.id.profile_group);
        fav=(LinearLayout)guillotineMenu.findViewById(R.id.activity_group);
        out=(LinearLayout)guillotineMenu.findViewById(R.id.settings_group);


        Pub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                ll1.setVisibility(View.VISIBLE);

                ll2.setVisibility(View.GONE);

                ll3.setVisibility(View.GONE);
                if(!x) {
                    x=true;
                    new LongOperation().execute("1");
                }


            }
        });
        prof.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll1.setVisibility(View.GONE);

                ll2.setVisibility(View.VISIBLE);

                ll3.setVisibility(View.GONE);

                new LongOperation().execute("2");

            }
        });
        fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll1.setVisibility(View.GONE);

                ll2.setVisibility(View.GONE);

                ll3.setVisibility(View.VISIBLE);
                new LongOperation().execute("3");

            }
        });





        out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              /*  ParseUser.logOut();
                startActivity(new Intent(getApplicationContext(),Accueil.class));*/
            }
        });



    }
    private class LongOperation extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String etat=params[0];

            Log.e("hhhh",etat);

            return etat;
        }

        @Override
        protected void onPostExecute(String result) {

            switch(result){
                case "1":
                    Log.e("etat",result);

                    new GuillotineAnimation.GuillotineBuilder(guillotineMenu, guillotineMenu.findViewById(R.id.feed_group), contentHamburger)
                            .setStartDelay(250)
                            .setActionBarViewForAnimation(toolbar)
                            .build();


                    break;
                case "2":
                    Log.e("etat2",result);

                    new GuillotineAnimation.GuillotineBuilder(guillotineMenu, guillotineMenu.findViewById(R.id.profile_group), contentHamburger)
                            .setStartDelay(250)
                            .setActionBarViewForAnimation(toolbar)
                            .build();


                    break;
                case "3":
                    Log.e("etat3",result);

                    new GuillotineAnimation.GuillotineBuilder(guillotineMenu, guillotineMenu.findViewById(R.id.activity_group), contentHamburger)
                            .setStartDelay(250)
                            .setActionBarViewForAnimation(toolbar)
                            .build();
                    break;
            }




        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}
    }

}
