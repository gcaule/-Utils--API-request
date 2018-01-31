package fr.wildcodeschool.wcstravelcheckpoint5;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AmadeusAPI.getTravels(this, new AmadeusAPI.AmadeusAPIResponse() {
            @Override
            public void onSuccess(List<TravelModel> travels) {
                for (TravelModel travel : travels) {
                    Log.d(TAG, "onSuccess: " + travel.getPrice());
                }
            }

            @Override
            public void onError(String error) {
                // TODO : afficher un message d'erreur à l'utilisateur
            }
        });
    }
}
