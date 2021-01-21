package com.jaysonjose.smartgreenhouse;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ControlFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ControlFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private TextView textView_light,text_view_progress_light,textView_off_light,text_view_progress_off_light;
    private ProgressBar progress_bar_light,progress_bar_off_light;
    Handler handler_light;
    int hours_light = 0,min_light=0,sec_light=0,time_light = 0,progress_light = 100;

    private TextView textView_fan,text_view_progress_fan,textView_off_fan,text_view_progress_off_fan;
    private ProgressBar progress_bar_fan,progress_bar_off_fan;
    Handler handler_fan;
    int hours_fan = 0,min_fan=0,sec_fan=0,time_fan = 0,progress_fan = 100;

    private TextView textView_pump,text_view_progress_pump,textView_off_pump,text_view_progress_off_pump;
    private ProgressBar progress_bar_pump,progress_bar_off_pump;
    Handler handler_pump;
    int hours_pump = 0,min_pump=0,sec_pump=0,time_pump = 0,progress_pump = 100;

    private DatabaseReference reference;
    String userId;
    FirebaseUser userLog;

    private Switch aSwitch;



    public ControlFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ControlFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ControlFragment newInstance(String param1, String param2) {
        ControlFragment fragment = new ControlFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);


        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_control, null);

        text_view_progress_light = view.findViewById(R.id.text_view_progress_light);
        textView_light = view.findViewById(R.id.textView_light);
        progress_bar_light = view.findViewById(R.id.progress_bar_light);

        //fake
        text_view_progress_off_light = view.findViewById(R.id.text_view_progress_off_light);
        textView_off_light = view.findViewById(R.id.textView_off_light);
        progress_bar_off_light = view.findViewById(R.id.progress_bar_off_light);
        text_view_progress_off_light.setText("OFF");

        text_view_progress_fan = view.findViewById(R.id.text_view_progress_fan);
        textView_fan = view.findViewById(R.id.textView_fan);
        progress_bar_fan = view.findViewById(R.id.progress_bar_fan);

        //fake
        text_view_progress_off_fan = view.findViewById(R.id.text_view_progress_off_fan);
        textView_off_fan = view.findViewById(R.id.textView_off_fan);
        progress_bar_off_fan = view.findViewById(R.id.progress_bar_off_fan);
        text_view_progress_off_fan.setText("OFF");

        text_view_progress_pump = view.findViewById(R.id.text_view_progress_pump);
        textView_pump = view.findViewById(R.id.textView_pump);
        progress_bar_pump= view.findViewById(R.id.progress_bar_pump);

        //fake
        text_view_progress_off_pump = view.findViewById(R.id.text_view_progress_off_pump);
        textView_off_pump = view.findViewById(R.id.textView_off_pump);
        progress_bar_off_pump = view.findViewById(R.id.progress_bar_off_pump);
        text_view_progress_off_pump.setText("OFF");


        userLog = FirebaseAuth.getInstance().getCurrentUser();
        userId = userLog.getUid();
        reference = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);





       // btn_light();
       // onBtn_light();

    //    offBtn_fan();
     //   onBtn_fan();

      //  offBtn_pump();
      //  onBtn_pump();

        reference.child("mode").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String mode = snapshot.getValue().toString();
                    if(mode.equals("manual")){

                        progress_bar_off_light.setClickable(true);
                        progress_bar_light.setClickable(true);
                        progress_bar_fan.setClickable(true);
                        progress_bar_off_fan.setClickable(true);
                        progress_bar_pump.setClickable(true);
                        progress_bar_off_pump.setClickable(true);

                    }else{
                        progress_bar_off_light.setClickable(false);
                        progress_bar_light.setClickable(false);
                        progress_bar_fan.setClickable(false);
                        progress_bar_off_fan.setClickable(false);
                        progress_bar_pump.setClickable(false);
                        progress_bar_off_pump.setClickable(false);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



       fan();
       sprinkler();
        lights();
        return view;
    }

    private void sprinkler() {
        reference.child("control").child("sprinkler").child("status").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String data1 = snapshot.getValue().toString();
                    if (data1.equals("OFF")){

                        text_view_progress_pump.setVisibility(View.GONE);
                        textView_pump.setVisibility(View.GONE);
                        progress_bar_pump.setVisibility(View.GONE);
                        text_view_progress_off_pump.setVisibility(View.VISIBLE);
                        textView_off_pump.setVisibility(View.VISIBLE);
                        progress_bar_off_pump.setVisibility(View.VISIBLE);
                        progress_bar_pump.setProgress(100);
                        progress_bar_off_pump.setProgress(100);

                    }else{
                        text_view_progress_off_pump.setVisibility(View.GONE);
                        textView_off_pump.setVisibility(View.GONE);
                        progress_bar_off_pump.setVisibility(View.GONE);
                        text_view_progress_pump.setVisibility(View.VISIBLE);
                        textView_pump.setVisibility(View.VISIBLE);
                        progress_bar_pump.setVisibility(View.VISIBLE);
                        progress_bar_pump.setProgress(100);
                        progress_bar_off_pump.setProgress(100);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        progress_bar_pump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                text_view_progress_pump.setVisibility(View.GONE);
                textView_pump.setVisibility(View.GONE);
                progress_bar_pump.setVisibility(View.GONE);

                text_view_progress_off_pump.setVisibility(View.VISIBLE);
                textView_off_pump.setVisibility(View.VISIBLE);
                progress_bar_off_pump.setVisibility(View.VISIBLE);
                reference.child("control").child("sprinkler").child("status").setValue("OFF");

               // progress_bar_pump.setProgress(100);
             //   progress_pump = 100;
             //   text_view_progress_off_pump.setText("OFF");
             //   handler_pump.removeCallbacksAndMessages(null);
            }
        });

        progress_bar_off_pump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text_view_progress_off_pump.setVisibility(View.GONE);
                textView_off_pump.setVisibility(View.GONE);
                progress_bar_off_pump.setVisibility(View.GONE);

                text_view_progress_pump.setVisibility(View.VISIBLE);
                textView_pump.setVisibility(View.VISIBLE);
                progress_bar_pump.setVisibility(View.VISIBLE);
                reference.child("control").child("sprinkler").child("status").setValue("ON");
                // num = Integer.parseInt(value.getText().toString());
                /*
                time_pump = 400;
                progress_pump = time_pump;
                progress_bar_pump.setMax(time_pump);

                handler_pump = new Handler();
                handler_pump.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        sec_pump = time_pump % 60;
                        hours_pump = time_pump / 60;
                        min_pump = hours_pump % 60;
                        hours_pump = hours_pump / 60;

                        progress_bar_pump.setProgress(progress_pump);

                        handler_pump.postDelayed(this,1000);
                        // text_view_progress.setText(String.valueOf(progress)+" °");
                        text_view_progress_pump.setText(hours_pump+":"+min_pump+":"+sec_pump);
                        time_pump--;
                        if(progress_pump <= 30){
                            text_view_progress_pump.setTextColor(Color.parseColor("#FA0707"));
                        }
                        if(progress_pump <= 0){
                            text_view_progress_pump.setTextColor(Color.parseColor("#1E1D1D"));
                            progress_bar_pump.setProgress(0);
                            text_view_progress_pump.setText("OFF");
                        }else{
                            progress_pump--;
                        }
                    }
                },1000);

*/
            }
        });

    }

    private void fan() {
        reference.child("control").child("fan").child("status").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String data2 = snapshot.getValue().toString();
                    if(data2.equals("OFF")){

                        text_view_progress_fan.setVisibility(View.GONE);
                        textView_fan.setVisibility(View.GONE);
                        progress_bar_fan.setVisibility(View.GONE);
                        text_view_progress_off_fan.setVisibility(View.VISIBLE);
                        textView_off_fan.setVisibility(View.VISIBLE);
                        progress_bar_off_fan.setVisibility(View.VISIBLE);
                        progress_bar_fan.setProgress(100);
                        progress_bar_off_fan.setProgress(100);

                    }else{
                        text_view_progress_off_fan.setVisibility(View.GONE);
                        textView_off_fan.setVisibility(View.GONE);
                        progress_bar_off_fan.setVisibility(View.GONE);
                        text_view_progress_fan.setVisibility(View.VISIBLE);
                        textView_fan.setVisibility(View.VISIBLE);
                        progress_bar_fan.setVisibility(View.VISIBLE);
                        progress_bar_fan.setProgress(100);
                        progress_bar_off_fan.setProgress(100);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        progress_bar_fan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                text_view_progress_fan.setVisibility(View.GONE);
                textView_fan.setVisibility(View.GONE);
                progress_bar_fan.setVisibility(View.GONE);

                text_view_progress_off_fan.setVisibility(View.VISIBLE);
                textView_off_fan.setVisibility(View.VISIBLE);
                progress_bar_off_fan.setVisibility(View.VISIBLE);
                reference.child("control").child("fan").child("status").setValue("OFF");

            }
        });

        progress_bar_off_fan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text_view_progress_off_fan.setVisibility(View.GONE);
                textView_off_fan.setVisibility(View.GONE);
                progress_bar_off_fan.setVisibility(View.GONE);

                text_view_progress_fan.setVisibility(View.VISIBLE);
                textView_fan.setVisibility(View.VISIBLE);
                progress_bar_fan.setVisibility(View.VISIBLE);
                reference.child("control").child("fan").child("status").setValue("ON");
                // num = Integer.parseInt(value.getText().toString());



            }
        });


    }

    private void lights() {
        reference.child("control").child("lights").child("status").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String data3 = snapshot.getValue().toString();
                    if (data3.equals("OFF")) {
                        text_view_progress_light.setVisibility(View.GONE);
                        textView_light.setVisibility(View.GONE);
                        progress_bar_light.setVisibility(View.GONE);
                        text_view_progress_off_light.setVisibility(View.VISIBLE);
                        textView_off_light.setVisibility(View.VISIBLE);
                        progress_bar_off_light.setVisibility(View.VISIBLE);
                        progress_bar_off_light.setProgress(100);
                        progress_bar_light.setProgress(100);


                    } else {
                        progress_bar_light.setProgress(100);
                        progress_bar_off_light.setProgress(100);
                       text_view_progress_off_light.setVisibility(View.GONE);
                       textView_off_light.setVisibility(View.GONE);
                       progress_bar_off_light.setVisibility(View.GONE);

                       text_view_progress_light.setVisibility(View.VISIBLE);
                        textView_light.setVisibility(View.VISIBLE);
                        progress_bar_light.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        progress_bar_light.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                text_view_progress_light.setVisibility(View.GONE);
                textView_light.setVisibility(View.GONE);
                progress_bar_light.setVisibility(View.GONE);

                text_view_progress_off_light.setVisibility(View.VISIBLE);
                textView_off_light.setVisibility(View.VISIBLE);
                progress_bar_off_light.setVisibility(View.VISIBLE);

                reference.child("control").child("lights").child("status").setValue("OFF");

            }
        });

        progress_bar_off_light.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text_view_progress_off_light.setVisibility(View.GONE);
                textView_off_light.setVisibility(View.GONE);
                progress_bar_off_light.setVisibility(View.GONE);

                text_view_progress_light.setVisibility(View.VISIBLE);
                textView_light.setVisibility(View.VISIBLE);
                progress_bar_light.setVisibility(View.VISIBLE);

                reference.child("control").child("lights").child("status").setValue("ON");

                // num = Integer.parseInt(value.getText().toString());


            }
        });

    }
}