package com.divesh.gymfl;

import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class RoutinePlanner extends AppCompatActivity {
    private Spinner WorkoutOptions;
    private Spinner WorkoutOptions2;
    private Spinner WorkoutOptions3;
    private Spinner WorkoutOptions4;
    private Spinner WorkoutOptions5;
    private Spinner WorkoutOptions6;
    private Spinner WorkoutOptions7;
    private Button updater;
    String[] Schedule={"Rest","Rest","Rest","Rest","Rest","Rest","Rest"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routine_planner);
        updater= (Button) findViewById(R.id.update);
        WorkoutOptions = (Spinner) findViewById(R.id.routine_spinner);
        WorkoutOptions2 = (Spinner) findViewById(R.id.routine_spinner2);
        WorkoutOptions3 = (Spinner) findViewById(R.id.routine_spinner3);
        WorkoutOptions4 = (Spinner) findViewById(R.id.routine_spinner4);
        WorkoutOptions5 = (Spinner) findViewById(R.id.routine_spinner5);
        WorkoutOptions6 = (Spinner) findViewById(R.id.routine_spinner6);
        WorkoutOptions7 = (Spinner) findViewById(R.id.routine_spinner7);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.workoutOptions, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        WorkoutOptions.setAdapter(adapter);
        WorkoutOptions2.setAdapter(adapter);
        WorkoutOptions3.setAdapter(adapter);
        WorkoutOptions4.setAdapter(adapter);
        WorkoutOptions5.setAdapter(adapter);
        WorkoutOptions6.setAdapter(adapter);
        WorkoutOptions7.setAdapter(adapter);

        //when update button is clicked
        View.OnClickListener Listener = new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Schedule[0] = WorkoutOptions.getSelectedItem().toString();
                Schedule[1] = WorkoutOptions2.getSelectedItem().toString();
                Schedule[2] = WorkoutOptions3.getSelectedItem().toString();
                Schedule[3] = WorkoutOptions4.getSelectedItem().toString();
                Schedule[4] = WorkoutOptions5.getSelectedItem().toString();
                Schedule[5] = WorkoutOptions6.getSelectedItem().toString();
                Schedule[6] = WorkoutOptions7.getSelectedItem().toString();
                updateScheduler();
            }
        };
        updater.setOnClickListener(Listener);
    }

    private void updateScheduler() {
        Intent intent = new Intent(this, MainActivity.class);
        Resources resources = getResources();
        String[] key = resources.getStringArray(R.array.key_schedule);
        Bundle bundle = new Bundle();
        bundle.putStringArray("MyArray", Schedule);
        intent.putExtras(bundle);
        startActivity(intent);
    }

}
