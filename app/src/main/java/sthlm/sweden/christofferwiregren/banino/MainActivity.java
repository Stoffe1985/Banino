package sthlm.sweden.christofferwiregren.banino;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(savedInstanceState == null){

            MainActivityFragment mainActivityFragment = new MainActivityFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.rootMain,mainActivityFragment).commit();
        }

    }





}


