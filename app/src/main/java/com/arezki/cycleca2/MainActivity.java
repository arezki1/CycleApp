package com.arezki.cycleca2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    private static final String TAG = "MainActivity";
    double Latitude;
    double Longitude;
    DatabaseReference myRef = database.getReference("coordinates");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //getting the coordinates values from firebase

        DatabaseReference database1 = FirebaseDatabase.getInstance().getReference();

        database1.child("coordinates").addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Object values = snapshot.getValue(Object.class);

                    //get the latitude values here to instantly be displayed in the mobile app
                    try{
                        JSONObject obj = new JSONObject(values.toString());
                        JSONObject snippet = obj.getJSONObject("data");

                        String latitude= snippet.getString("latitude");
                        Latitude=Double.parseDouble(latitude);

                        String longitude= snippet.getString("longitude");
                        Longitude=Double.parseDouble(longitude);

                        TextView tv = (TextView) findViewById(R.id.textView);
                        if (tv != null)
                        {tv.setText(Latitude +" °C");}
                    } catch (JSONException e) {
                        Log.e("MYAPP", "unexpected JSON exception", e);
                        // Do something to recover ... or kill the app.
                        throw new RuntimeException(e);
                    }


                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "Failed to read value.", databaseError.toException());
                throw new RuntimeException();
            }
        });


        // Locate the button in activity_main.xml
       Button button = (Button) findViewById(R.id.button);

        //start the mapas activity on clicke here
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MainActivity.this,
                        MapsMarkerActivity.class);


                Bundle b = new Bundle();
                b.putDouble("latitude", Latitude);
                b.putDouble("longitude", Longitude);
                myIntent.putExtras(b);

                startActivity(myIntent);
            }
        });



    }
}
