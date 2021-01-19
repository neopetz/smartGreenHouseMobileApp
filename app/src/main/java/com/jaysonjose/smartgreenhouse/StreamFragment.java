package com.jaysonjose.smartgreenhouse;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Switch;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StreamFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StreamFragment extends Fragment {


    String url = "http://192.168.254.125";
    String url2 = "http://www.google.com";
    private WebView visualStream;
    private Switch aSwitch;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public StreamFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StreamFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StreamFragment newInstance(String param1, String param2) {
        StreamFragment fragment = new StreamFragment();
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
        View view = inflater.inflate(R.layout.fragment_stream,null);

        visualStream = view.findViewById(R.id.visualStream);
        aSwitch = view.findViewById(R.id.switch1);

        visualStream.setWebViewClient(new WebViewClient());
        WebSettings webSettings = visualStream.getSettings();
        //webSettings.setBuiltInZoomControls(true);
        visualStream.setInitialScale(10);
        webSettings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
        webSettings.setUseWideViewPort(true);
        webSettings.setJavaScriptEnabled(true);
        visualStream.loadUrl(url);

        aSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(aSwitch.isChecked()){
                    visualStream.loadUrl(url2);
                    aSwitch.setText("Online");
                }else{
                    visualStream.loadUrl(url);
                    aSwitch.setText("Offline");
                }
            }
        });



        return view;
    }
}