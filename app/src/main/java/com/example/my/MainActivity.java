package com.example.my;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    TextView Welocme;
    ArrayList <status> state = new ArrayList<>();
    AirplaneModeChangeReceiver airplaneModeChangeReceiver = new AirplaneModeChangeReceiver();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //create the adapter of recycler view
        setCardsText();

        CustomAdapter Adapter = new CustomAdapter(this,state);
        RecyclerView rv = findViewById(R.id.rv);
        GridLayoutManager grid = new GridLayoutManager(this,2);
        rv.setLayoutManager(grid);
        rv.setAdapter(Adapter);


        // Create a new JobInfo object that describes the job to be scheduled
        JobInfo.Builder builder = new JobInfo.Builder(1, new ComponentName(this, job_scadular.class));
        builder.setMinimumLatency(1000); // Start immediately
        builder.setOverrideDeadline(2000); // Complete within 2 seconds
        JobInfo jobInfo = builder.build();

        // Schedule the job with the JobScheduler
        JobScheduler jobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        jobScheduler.schedule(jobInfo);


        //go in welcome page
        Welocme = findViewById(R.id.welcome);
        Welocme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,welcomeActivity.class);
                startActivity(intent);
            }
        });


    }

    public void setCardsText(){
        String [] cardsText = getResources().getStringArray(R.array.cardNames);

        for(int i = 0; i < cardsText.length; i ++)
        {
            state.add(new status(cardsText[i]));
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED);
        registerReceiver(airplaneModeChangeReceiver, filter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(airplaneModeChangeReceiver);
    }

}