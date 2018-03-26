package com.example.shreyash.myapplication;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

public class FragmentBoard extends Fragment  {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        View rootview= inflater.inflate(R.layout.fragment_board, container, false);
        TextView notice_text=(TextView)rootview.findViewById(R.id.text_notice);
        TextView amount_prev = (TextView) rootview.findViewById(R.id.text_bill_amount_prev);
        TextView amount_b = (TextView) rootview.findViewById(R.id.text_bill_amount_b);
        TextView amount_l = (TextView) rootview.findViewById(R.id.text_bill_amount_l);
        TextView amount_d = (TextView) rootview.findViewById(R.id.text_bill_amount_d);
        TextView amount_ex = (TextView) rootview.findViewById(R.id.text_bill_amount_ex);
        TextView amount_es = (TextView) rootview.findViewById(R.id.text_bill_amount_estimated);
        TextView menu_today = (TextView) rootview.findViewById(R.id.text_menu_today);
        TextView menu_tom = (TextView) rootview.findViewById(R.id.text_menu_tom);
        TextView menu_next = (TextView) rootview.findViewById(R.id.text_menu_next);

        //TODO: Set text of all amount textviews here
        amount_prev.setText("450");
        //TODO: Get data from firebase for notice board
        String notice = "1.Now Enjoy Extra Items on demand in your daily meals." + "\n\n"+
                "2.You can ask for Namkeen, Milk, Butter, Sweets and many more items." + "\n\n"+
                "3.With the motto to serve pure vegetarian food in the institute, we start with the registration procedure for another session.";
        notice_text.setText(notice);
        //TODO:get data from firebase for each day meal and use it below

    return rootview;
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Dashboard");
    }
}

