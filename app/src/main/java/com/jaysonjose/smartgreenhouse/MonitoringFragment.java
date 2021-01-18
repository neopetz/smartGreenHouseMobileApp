package com.jaysonjose.smartgreenhouse;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
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
 * Use the {@link MonitoringFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MonitoringFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    int progressTemp = 0;
    private TextView text_view_progress_temp;
    private ProgressBar progress_bar_temp;
    Handler handlerTemp;
    float numTemp = 0;
    private EditText value;

    int progressHumid= 0;
    private TextView text_view_progress_humid;
    private ProgressBar progress_bar_humid;
    Handler handlerHumid;
    float numHumid = 0;
    private EditText value_humid;

    int progressSoil = 0;
    private TextView text_view_progress_soil;
    private ProgressBar progress_bar_soil;
    Handler handlerSoil;
    float numSoil = 0;
    private EditText value_soil;

    int progressHeat = 0;
    private TextView text_view_progress_heat;
    private ProgressBar progress_bar_heat;
    Handler handlerHeat;
    float numHeat = 0;
    private EditText value_heat;
    private DatabaseReference reference;
    String userId;
    FirebaseUser userLog;

    public MonitoringFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MonitoringFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MonitoringFragment newInstance(String param1, String param2) {
        MonitoringFragment fragment = new MonitoringFragment();
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
        View view = inflater.inflate(R.layout.fragment_monitoring, null);

        text_view_progress_temp =view.findViewById(R.id.text_view_progress_temp);
        progress_bar_temp = view.findViewById(R.id.progress_bar_temp);
        value = view.findViewById(R.id.value);

        text_view_progress_humid=view.findViewById(R.id.text_view_progress_humid);
        progress_bar_humid = view.findViewById(R.id.progress_bar_humid);

        text_view_progress_soil=view.findViewById(R.id.text_view_progress_soil);
        progress_bar_soil = view.findViewById(R.id.progress_bar_soil);

        text_view_progress_heat=view.findViewById(R.id.text_view_progress_heat);
        progress_bar_heat = view.findViewById(R.id.progress_bar_heat);

        userLog = FirebaseAuth.getInstance().getCurrentUser();
        userId = userLog.getUid();

        reference = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);

        updateProgressBarTemp();
        updateProgressBarHumid();
        updateProgressBarSoil();
        updateProgressBarHeat();

        return  view;
    }

    private void updateProgressBarHeat() {
        handlerHeat = new Handler();
        handlerHeat.postDelayed(new Runnable() {
            @SuppressLint({"SetTextI18n", "ResourceAsColor"})
            @Override
            public void run() {

                reference.child("monitoring").child("heat").addValueEventListener(new ValueEventListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            numHeat = Float.parseFloat(snapshot.getValue().toString());
                            //tempData.setText(data +" °C");
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

               // numHeat = Float.parseFloat(value.getText().toString());
                 // numHeat = 36;
                if (progressHeat<32){
                    text_view_progress_heat.setTextColor(Color.parseColor("#03FF0F"));
                }
                else if(progressHeat<36){
                    text_view_progress_heat.setTextColor(Color.parseColor("#F8F805"));
                }
                else{
                    text_view_progress_heat.setTextColor(Color.parseColor("#FA0707"));
                }
                if(progress_bar_heat.getProgress()<=numHeat){
                    if (progressHeat>=100){
                        progressHeat = 99;
                    }

                    progress_bar_heat.setProgress(progressHeat+2);

                    handlerHeat.postDelayed(this,50);
                    progressHeat++;
                    text_view_progress_heat.setText(String.valueOf(progressHeat)+" °");

                }else{
                    progress_bar_heat.setProgress(progressHeat);

                    handlerHeat.postDelayed(this,50);
                    text_view_progress_heat.setText(String.valueOf(progressHeat)+" °");
                    if(progressHeat<=0){
                        progressHeat=0;
                    }else{
                        progressHeat--;
                    }


                }
            }
        },50);
    }

    private void updateProgressBarSoil() {

        handlerSoil = new Handler();
        handlerSoil.postDelayed(new Runnable() {
            @SuppressLint({"SetTextI18n", "ResourceAsColor"})
            @Override
            public void run() {
                //numSoil = Float.parseFloat(value.getText().toString());
                reference.child("monitoring").child("soil").addValueEventListener(new ValueEventListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            numSoil = Float.parseFloat(snapshot.getValue().toString());
                            //tempData.setText(data +" °C");
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                 //numSoil = 18;
                if (progressSoil<32){
                    text_view_progress_soil.setTextColor(Color.parseColor("#03FF0F"));
                }
                else if(progressSoil<36){
                    text_view_progress_soil.setTextColor(Color.parseColor("#F8F805"));
                }
                else{
                    text_view_progress_soil.setTextColor(Color.parseColor("#FA0707"));
                }
                if(progress_bar_soil.getProgress()<=numSoil){
                    if (progressSoil>=100){
                        progressSoil = 99;
                    }

                    progress_bar_soil.setProgress(progressSoil+2);

                    handlerSoil.postDelayed(this,50);
                    progressSoil++;
                    text_view_progress_soil.setText(String.valueOf(progressSoil)+" °");

                }else{
                    progress_bar_soil.setProgress(progressSoil);

                    handlerSoil.postDelayed(this,50);
                    text_view_progress_soil.setText(String.valueOf(progressSoil)+" °");
                    if(progressSoil<=0){
                        progressSoil=0;
                    }else{
                        progressSoil--;
                    }


                }
            }
        },50);


    }

    public void updateProgressBarTemp() {
        handlerTemp = new Handler();
        handlerTemp.postDelayed(new Runnable() {
            @SuppressLint({"SetTextI18n", "ResourceAsColor"})
            @Override
            public void run() {
                //****************************************************

                //***************************************************
                //numTemp = Float.parseFloat(value.getText().toString());

                reference.child("monitoring").child("temperature").addValueEventListener(new ValueEventListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            numTemp = Float.parseFloat(snapshot.getValue().toString());
                            //tempData.setText(data +" °C");
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
               // numTemp = 28;
                if (progressTemp<32){
                    text_view_progress_temp.setTextColor(Color.parseColor("#03FF0F"));
                }
                else if(progressTemp<36){
                    text_view_progress_temp.setTextColor(Color.parseColor("#F8F805"));
                }
                else{
                    text_view_progress_temp.setTextColor(Color.parseColor("#FA0707"));
                }
                if(progress_bar_temp.getProgress()<=numTemp){
                    if (progressTemp>=100){
                        progressTemp = 99;
                    }

                    progress_bar_temp.setProgress(progressTemp+2);

                    handlerTemp.postDelayed(this,50);
                    progressTemp++;
                    text_view_progress_temp.setText(String.valueOf(progressTemp)+" °");

                }else{
                    progress_bar_temp.setProgress(progressTemp);

                    handlerTemp.postDelayed(this,50);
                    text_view_progress_temp.setText(String.valueOf(progressTemp)+" °");
                    if(progressTemp<=0){
                        progressTemp=0;
                    }else{
                        progressTemp--;
                    }


                }
            }
        },50);
    }

    private void updateProgressBarHumid() {
        handlerHumid = new Handler();
        handlerHumid.postDelayed(new Runnable() {
            @SuppressLint({"SetTextI18n", "ResourceAsColor"})
            @Override
            public void run() {
               //numHumid = Float.parseFloat(value.getText().toString());

                reference.child("monitoring").child("humidity").addValueEventListener(new ValueEventListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            numHumid = Float.parseFloat(snapshot.getValue().toString());
                            //tempData.setText(data +" °C");
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                //numHumid = 74;
                if (progressHumid<32){
                    text_view_progress_humid.setTextColor(Color.parseColor("#03FF0F"));
                }
                else if(progressHumid<36){
                    text_view_progress_humid.setTextColor(Color.parseColor("#F8F805"));
                }
                else{
                    text_view_progress_humid.setTextColor(Color.parseColor("#FA0707"));
                }
                if(progress_bar_humid.getProgress()<=numHumid){
                    if (progressHumid>=100){
                        progressHumid = 99;
                    }

                    progress_bar_humid.setProgress(progressHumid+2);

                    handlerTemp.postDelayed(this,50);
                    progressHumid++;
                    text_view_progress_humid.setText(String.valueOf(progressHumid)+" °");

                }else{
                    progress_bar_humid.setProgress(progressHumid);

                    handlerTemp.postDelayed(this,50);
                    text_view_progress_humid.setText(String.valueOf(progressHumid)+" °");
                    if(progressHumid<=0){
                        progressHumid=0;
                    }else{
                        progressHumid--;
                    }


                }
            }
        },50);
    }

}