package com.jain.shreyash.myapplication;

/**
 * Created by Shreyash on 17-02-2018.
 */

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import org.apache.commons.net.ntp.NTPUDPClient;
import org.apache.commons.net.ntp.TimeInfo;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;
import com.jain.shreyash.myapplication.R;
import com.tomer.fadingtextview.FadingTextView;

import java.text.ParseException;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class FragmentCancel extends Fragment {
    private TextView displayDate;

    List<CancelListItem> cancelList;

    //the recyclerview
    RecyclerView recyclerView;
    long tdiff;
    // flag for Internet connection status
    Boolean isInternetPresent = false;

    // Connection detector class
    ConnectionDetector cd;

    private DatePickerDialog.OnDateSetListener mDateSetListener;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootview = inflater.inflate(R.layout.fragment_cancel, container, false);
        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        recyclerView = (RecyclerView) rootview.findViewById(R.id.recyclerViewcCancel);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        List<CancelDetails> cancelDetailsArrayTemp = new ArrayList<>();
        String filename = "CancelData";
        try {

            //FileInputStream inStream = new FileInputStream(filename);
            FileInputStream inStream = getActivity().openFileInput(filename);
            ObjectInputStream objectInStream = new ObjectInputStream(inStream);
            int count = objectInStream.readInt();// Get the number of cancel requests
            for (int c=0; c < count; c++)
                cancelDetailsArrayTemp.add((CancelDetails) objectInStream.readObject());
            objectInStream.close();
        }
        catch (Exception e) {
            Toast.makeText(getContext(), "ohhh", Toast.LENGTH_SHORT).show();
            Log.e("reading error",""+e);
            e.printStackTrace();
        }

        cancelList=new ArrayList<>();
        Collections.reverse(cancelDetailsArrayTemp);
        for(int i = 0; i< cancelDetailsArrayTemp.size();i++) {


            String status = cancelDetailsArrayTemp.get(i).Acceptance;
            if(status.equals("-1")) status = "pending";
            else if(status.equals("1")) status = "accepted";
            else if(status.equals("0")) status = "rejected";
            boolean b = cancelDetailsArrayTemp.get(i).b.equals("1");
            boolean l = cancelDetailsArrayTemp.get(i).l.equals("1");
            boolean d = cancelDetailsArrayTemp.get(i).d.equals("1");

            Calendar calc = Calendar.getInstance();

            calc.add(Calendar.DATE, -2);

            Date today = calc.getTime();




            String sdate= cancelDetailsArrayTemp.get(i).request_date.substring(0, cancelDetailsArrayTemp.get(i).request_date.length()-11);

            if (status=="pending"){
                try {
                    Date predate=new SimpleDateFormat("yyyy/MM/dd").parse(sdate);
                    if (predate.before(today)){
                        cancelList.add(

                                new CancelListItem("Cancelled",cancelDetailsArrayTemp.get(i).date , sdate, b, l, d, R.color.thirdpage,false)
                        );

                    }
                    else {
                        cancelList.add(

                                new CancelListItem(status,cancelDetailsArrayTemp.get(i).date , sdate, b, l, d, R.color.bg_screen1,false)
                        );
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }

           }
            else if (status=="accepted")
                cancelList.add(

                        new CancelListItem(status,cancelDetailsArrayTemp.get(i).date , sdate, b, l, d, R.color.thirdpage,false)
                );
            else
                cancelList.add(

                        new CancelListItem(status,cancelDetailsArrayTemp.get(i).date , sdate, b, l, d, R.color.thirdpage,false)
                );
        }
        CancellationAdapter adapter = new CancellationAdapter(getContext(), cancelList);
        recyclerView.setAdapter(adapter);





       // checkTimeServer();



        String[] meals = new String[]{
                "Breakfast",
                "Lunch",
                "Dinner",

        };
        final boolean[] checkedmeals = new boolean[]{
                false, // Red
                false, // Green
                false, // Blue


        };


        final List<String> mealsList = Arrays.asList(meals);
        com.applandeo.materialcalendarview.CalendarView calendarView = (com.applandeo.materialcalendarview.CalendarView) rootview.findViewById(R.id.calendarView);


        Calendar max = Calendar.getInstance();
        max.add(Calendar.DAY_OF_MONTH, 15);


        calendarView.setMaximumDate(max);

        String[] texts = {"Click a date ","To cancel your meals"};
        FadingTextView FTV = (FadingTextView) rootview.findViewById(R.id.fadingTextView);
        FTV.setTexts(texts);
        final boolean[] correct_date = new boolean[1];



        List<EventDay> cancel_events = new ArrayList<>();



        List<CancelDetails> cancelDetailsArrayTemper = new ArrayList<>();

        try {

            //FileInputStream inStream = new FileInputStream(filename);
            FileInputStream inStream = getActivity().openFileInput(filename);
            ObjectInputStream objectInStream = new ObjectInputStream(inStream);
            int count = objectInStream.readInt();// Get the number of cancel requests
            for (int c=0; c < count; c++)
                cancelDetailsArrayTemper.add((CancelDetails) objectInStream.readObject());
            objectInStream.close();
        }
        catch (Exception e) {

            Log.e("reading error",""+e);
            e.printStackTrace();
        }
        List<CheckCancelDate> cancel_dates_checker= new ArrayList<>();


        for(int i = 0; i< cancelDetailsArrayTemper.size();i++) {


            String sdate= cancelDetailsArrayTemper.get(i).request_date.substring(0, cancelDetailsArrayTemper.get(i).request_date.length()-12);
            SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
            try {
                Date pdate = format.parse(sdate);
                Calendar event_day=toCalendar(pdate);
                String status = cancelDetailsArrayTemper.get(i).Acceptance;


                boolean b = cancelDetailsArrayTemper.get(i).b.equals("1");
                boolean l = cancelDetailsArrayTemper.get(i).l.equals("1");
                boolean d = cancelDetailsArrayTemper.get(i).d.equals("1");
                if(status.equals("1")) {
                    if (b && !l && !d) {
                        cancel_events.add(new EventDay(event_day, R.drawable.b));

                    } else if (!b && l && !d) {
                        cancel_events.add(new EventDay(event_day, R.drawable.l));
                    } else if (!b && !l && d) {
                        cancel_events.add(new EventDay(event_day, R.drawable.d));
                    } else if (b && l && !d) {
                        cancel_events.add(new EventDay(event_day, R.drawable.bl));
                    } else if (b && !l && d) {
                        cancel_events.add(new EventDay(event_day, R.drawable.bd));
                    } else if (!b && l && d) {
                        cancel_events.add(new EventDay(event_day, R.drawable.ld));
                    } else if (b && l && d) {
                        cancel_events.add(new EventDay(event_day, R.drawable.bld));
                    }
                }


                int acceptance=Integer.valueOf(cancelDetailsArrayTemper.get(i).Acceptance);
                cancel_dates_checker.add(new CheckCancelDate(pdate,acceptance,b,l,d));
            } catch (ParseException e) {
                e.printStackTrace();
            }





        }
        calendarView.setEvents(cancel_events);

        calendarView.setOnDayClickListener(new OnDayClickListener() {
            @Override
            public void onDayClick(EventDay eventDay) {
                Calendar clickedDayCalendar = eventDay.getCalendar();
                Date clickeddate=clickedDayCalendar.getTime();
                Date today = new Date();
                Date fdate = new Date();
                Calendar c = Calendar.getInstance();
                c.setTime(fdate);
                c.add(Calendar.DATE, 15);
                fdate = c.getTime();
                correct_date[0] =isTimeAutomatic(getContext());
                cd = new ConnectionDetector(getActivity().getApplicationContext());
                isInternetPresent = cd.isConnectingToInternet();
                if(!isInternetPresent){
                    Toast.makeText(getActivity(), "Please connect to internet", Toast.LENGTH_LONG).show();
                }
                else if(!correct_date[0]){
                    Toast.makeText(getActivity(), "Your time is not correct, Please set time to automatic", Toast.LENGTH_LONG).show();
                }

                else if (!today.after(clickeddate) && !fdate.before(clickeddate) ){
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Select meals to cancel").setMultiChoiceItems(R.array.mealsCancel,
                        null, new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                //TODO:get values and store in a variable


                                checkedmeals[which] = isChecked;

                                String currentItem = mealsList.get(which);

                                // Notify the current action


                            }
                        })
                        .setPositiveButton("PROCEED", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int sum=0;
                                if (checkedmeals[0]) sum+=1;if (checkedmeals[1]) sum+=3;if (checkedmeals[2]) sum+=5;
                                int coinside=0;
                                for(int i = 0; i< cancel_dates_checker.size();i++){
                                    if ((clickeddate.compareTo(cancel_dates_checker.get(i).for_date)==0)){
                                        if(checkedmeals[0]==true && cancel_dates_checker.get(i).ck_breakfast==true){
                                            coinside=1;
                                            break;
                                        }
                                        if(checkedmeals[1]==true && cancel_dates_checker.get(i).ck_lunch==true){
                                            coinside=1;
                                            break;
                                        }
                                        if(checkedmeals[2]==true && cancel_dates_checker.get(i).ck_dinner==true){
                                            coinside=1;
                                            break;
                                        }
                                    }

                                }
                                checkedmeals[0]=false;
                                checkedmeals[1]=false;
                                checkedmeals[2]=false;
                                if (sum>0 && coinside==0){


                                    Intent intent = new Intent(getActivity(), ConfirmCancel.class);
                                    intent.putExtra("Cancel_diets", sum);
                                    intent.putExtra("date", clickeddate.getTime());
                                    sum=0;coinside=0;
                                    startActivity(intent);
                                }

                                else {
                                    if (sum==0)
                                    Toast.makeText(getActivity(), "Please select any meal", Toast.LENGTH_LONG).show();
                                    else   Toast.makeText(getActivity(), "You are selecting already requested meal", Toast.LENGTH_LONG).show();
                                }







                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();

            }}
        });





        // Boolean array for initial selected items



        ;



        //TODO: Get number online

        return rootview;
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Meals Cancellation");

    }


    public static boolean isTimeAutomatic(Context c) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return Settings.Global.getInt(c.getContentResolver(), Settings.Global.AUTO_TIME, 0) == 1;
        } else {
            return android.provider.Settings.System.getInt(c.getContentResolver(), android.provider.Settings.System.AUTO_TIME, 0) == 1;
        }
    }

    public static Calendar toCalendar(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }
}

