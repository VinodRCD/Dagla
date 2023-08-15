package com.dagla.android.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.dagla.android.GlobalFunctions;
import com.dagla.android.R;
import com.dagla.android.activity.ExpandableHeightGridView;
import com.dagla.android.activity.MainActivity;
import com.dagla.android.adapter.CategoriesAdapterNew;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;

public class VisitUsFragment extends Fragment  implements View.OnClickListener{

    View rootView;
    Context context;
    Bundle savedInstanceState;

    Button btnNext,btnSubmit,btnNewCustomer,btnExistingCustomer;
    TextView selectSlot;
    RelativeLayout slotBookingLayout;
    LinearLayout newCustomerLayout,existingCustomerLayout;
    EditText txtName,txtMobile,txtEmail,txtAddress,txtBranch;
    TextView newCustomerTxt,existingCustomerTxt;

    private TextView lblCurrentMonth;
    private ImageView prevMonth;
    private ImageView nextMonth;
    private ExpandableHeightGridView calendarView;
    private GridCellAdapter adapter;
    private Calendar _calendar;
    private int month, year;

    Locale locale;

    Dialog dlgLoading = null;

    String clicked_date = "1-October-20";

    String slot_date="",slot_time_id;

    RelativeLayout pickerLayout;

    NumberPicker picker;
    Button pickerDoneBtn,pickerCancelBtn;

    private Animation startAnim;
    private Animation endAnim;

    ArrayList<String> slotTimeIdArrList = new ArrayList<>();
    ArrayList<String> slotTimeArrList = new ArrayList<>();

    String slotTimeIdArr[];
    String slotTimeArr[];


    ArrayList<String> branchIdArrList = new ArrayList<>();
    ArrayList<String> branchNameArrList = new ArrayList<>();

    String branchIdArr[];
    String branchNameArr[];

    String branch_Id;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //just change the fragment_dashboard
        //with the fragment you want to inflate
        //like if the class is HomeFragment it should have R.layout.home_fragment
        //if it is DashboardFragment it should have R.layout.fragment_dashboard
        context = getActivity();

        if(GlobalFunctions.getLang(getActivity()).equals("ar")){
            ((MainActivity) getActivity()).setHeaders(getResources().getString(R.string.title_visit_us),true,true,true,false, false ,"0", false);
        }else {
            ((MainActivity) getActivity()).setHeaders(getResources().getString(R.string.title_visit_us_ar),true,true,true,false, false ,"0", false);
        }

        GlobalFunctions.initImageLoader(context);

        if (rootView == null) {
            if(GlobalFunctions.getLang(getActivity()).equals("ar")){
                rootView = inflater.inflate(R.layout.fragment_visit_us_ar, container, false);
            }else {
                rootView = inflater.inflate(R.layout.fragment_visit_us, container, false);

            }

            if(GlobalFunctions.getLang(getActivity()).equals("ar")){
                locale = new Locale("ar");
            }else {
                locale = new Locale("en");
            }

            btnNext = (Button) rootView.findViewById(R.id.btnNext);
            selectSlot = (TextView) rootView.findViewById(R.id.selectSlot);
            slotBookingLayout = (RelativeLayout) rootView.findViewById(R.id.slotBookingLayout);

            prevMonth = (ImageView) rootView.findViewById(R.id.prevMonth);
            lblCurrentMonth = (TextView) rootView.findViewById(R.id.lblCurrentMonth);
            nextMonth = (ImageView) rootView.findViewById(R.id.nextMonth);

            txtName = rootView.findViewById(R.id.txtName);
            txtMobile = rootView.findViewById(R.id.txtMobile);
            txtEmail = rootView.findViewById(R.id.txtEmail);
            txtAddress = rootView.findViewById(R.id.txtAddress);
            txtBranch = (EditText) rootView.findViewById(R.id.txtBranch);
            txtBranch.setFocusable(false);
            txtBranch.setClickable(true);


            if(!GlobalFunctions.getPrefrences(getActivity(), "user_id").equals("")){
                txtName.setText(GlobalFunctions.getPrefrences(getActivity(), "name"));
                txtEmail.setText(GlobalFunctions.getPrefrences(getActivity(), "email"));
            }



            btnSubmit = (Button) rootView.findViewById(R.id.btnSubmit);




            newCustomerTxt = (TextView) rootView.findViewById(R.id.newCustomerTxt);
            existingCustomerTxt = (TextView) rootView.findViewById(R.id.existingCustomerTxt);

            btnNewCustomer = (Button) rootView.findViewById(R.id.btnNewCustomer);
            btnExistingCustomer = (Button) rootView.findViewById(R.id.btnExistingCustomer);


            newCustomerLayout = (LinearLayout) rootView.findViewById(R.id.newCustomerLayout);
            existingCustomerLayout = (LinearLayout) rootView.findViewById(R.id.existingCustomerLayout);


            calendarView = (ExpandableHeightGridView) rootView.findViewById(R.id.calendar);


            _calendar = Calendar.getInstance(Locale.getDefault());
            month = _calendar.get(Calendar.MONTH) + 1;
            year = _calendar.get(Calendar.YEAR);
            Log.d("tag", "Calendar Instance:= " + "Month: " + month + " " + "Year: " + year);


            SimpleDateFormat sdf = new SimpleDateFormat("MMMM yyyy",locale);
            String format = sdf.format(_calendar.getTime());

//            currentMonth.setText(DateFormat.format(dateTemplate,  _calendar.getTime()));

            lblCurrentMonth.setText(format);

            // Initialised
            adapter = new GridCellAdapter(getActivity(), R.id.calendar_day_gridcell, month, year);
            adapter.notifyDataSetChanged();
            calendarView.setAdapter(adapter);

//            calendarView.setOnTouchListener(new View.OnTouchListener() {
//                @Override
//                public boolean onTouch(View v, MotionEvent event) {
//                    v.getParent().requestDisallowInterceptTouchEvent(true);
//                    return false;
//                }
//
//            });


            prevMonth.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (month <= 1) {
                        month = 12;
                        year--;
                    } else {
                        month--;
                    }

                    setGridCellAdapterToDate(month, year);

                }
            });


            nextMonth.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (month > 11) {
                        month = 1;
                        year++;
                    } else {
                        month++;
                    }

                    setGridCellAdapterToDate(month, year);

                }
            });

            btnNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if( slot_date.equals("")) {
//                        Toast.makeText(getActivity(), "Please select the date", Toast.LENGTH_SHORT).show();
                        if(GlobalFunctions.getLang(getActivity()).equals("ar")){
                            alertDialog("يرجى تحديد التاريخ");
                        }else {
                            alertDialog("Please select the date");
                        }

//                    }else if(!selectSlot.getText().toString().equals("")){
//                        VisitUsCheckoutFragment fragment1 = new VisitUsCheckoutFragment();
//                        Bundle b = new Bundle();
//
//                        b.putString("VisitDate", slot_date);
////                        b.putString("VisitTimeId", slot_time_id);
//
//                        fragment1.setArguments(b);
//                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//                        fragmentManager.beginTransaction()
//                                .replace(R.id.fragment_container, fragment1, "VisitUsFragment")
//                                .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
//                                .addToBackStack(null)
//                                // .setCustomAnimations(R.anim.right_to_left, R.anim.fadeout_2,0, R.anim.left_to_right)
//                                .commitAllowingStateLoss();
                    }else {
//                        Toast.makeText(getActivity(),"Please select the slot time",Toast.LENGTH_SHORT).show();

//                        if(GlobalFunctions.getLang(getActivity()).equals("ar")){
//                            alertDialog("يرجى تحديد وقت الفتحة");
//                        }else {
//                            alertDialog("Please select the slot time");
//                        }

                        VisitUsCheckoutFragment fragment1 = new VisitUsCheckoutFragment();
                        Bundle b = new Bundle();

                        b.putString("VisitDate", slot_date);
//                        b.putString("VisitTimeId", slot_time_id);

                        fragment1.setArguments(b);
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        fragmentManager.beginTransaction()
                                .replace(R.id.fragment_container, fragment1, "VisitUsFragment")
                                .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                                .addToBackStack(null)
                                // .setCustomAnimations(R.anim.right_to_left, R.anim.fadeout_2,0, R.anim.left_to_right)
                                .commitAllowingStateLoss();

                    }

                }
            });

            btnSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    SubmitExistingCustomerData();

                }
            });




            pickerLayout = (RelativeLayout) rootView.findViewById(R.id.pickerLayout);
            picker = (NumberPicker) rootView.findViewById(R.id.picker);

            setDividerColor(picker, Color.parseColor("#E6E6E6"));

            pickerDoneBtn = (Button) rootView.findViewById(R.id.pickerDoneBtn);
            pickerCancelBtn = (Button) rootView.findViewById(R.id.pickerCancelBtn);

            TextView pickerLblQuantity = (TextView) rootView.findViewById(R.id.pickerLblQuantity);

            if(GlobalFunctions.getLang(getActivity()).equals("ar")){
//                pickerLblQuantity.setText("كمية");
                pickerDoneBtn.setText("منجز");
                pickerCancelBtn.setText("إلغاء");
            }else {
//                pickerLblQuantity.setText("Quantity");
                pickerDoneBtn.setText("Done");
                pickerCancelBtn.setText("Cancel");
            }

            startAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.sliding_up);
            endAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.sliding_down);


            pickerDoneBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    pickerLayout.startAnimation(endAnim);
                    pickerLayout.setVisibility(View.GONE);

//                    slot_time_id = slotTimeIdArrList.get(picker.getValue());
//                    selectSlot.setText(slotTimeArrList.get(picker.getValue()));

                    branch_Id = branchIdArrList.get(picker.getValue());
                    txtBranch.setText(branchNameArrList.get(picker.getValue()));

                }
            });

            pickerCancelBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    pickerLayout.startAnimation(endAnim);
                    pickerLayout.setVisibility(View.GONE);

                }
            });



            slotBookingLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(clicked_date.equals("1-October-20")){
//                        Toast.makeText(getActivity(),"Please select the date",Toast.LENGTH_SHORT).show();
                        if(GlobalFunctions.getLang(getActivity()).equals("ar")){
                            alertDialog("يرجى تحديد التاريخ");
                        }else {
                            alertDialog("Please select the date");
                        }

                    }else if(slotTimeArr != null&&slotTimeArr.length>0){
                        picker.setDisplayedValues(null);
                        picker.setMinValue(0);
                        picker.setMaxValue(slotTimeArr.length-1);
                        picker.setWrapSelectorWheel(false);
                        picker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

                        timeSlotPickerDialog();
                    }else {
//                        Toast.makeText(getActivity(),"No Slots Available",Toast.LENGTH_SHORT).show();


                        if(GlobalFunctions.getLang(getActivity()).equals("ar")){
                            alertDialog("لا فتحات المتاحة");
                        }else {
                            alertDialog("No Slots Available");
                        }
                    }


                }
            });

            newCustomerLayout.setVisibility(View.VISIBLE);
            existingCustomerLayout.setVisibility(View.GONE);

            btnNewCustomer.setBackgroundResource(R.drawable.customer_btn_2_bg);
            btnExistingCustomer.setBackgroundResource(R.drawable.customer_btn_1_bg);

            btnNewCustomer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    btnNewCustomer.setBackgroundResource(R.drawable.customer_btn_2_bg);
                    btnExistingCustomer.setBackgroundResource(R.drawable.customer_btn_1_bg);

                    newCustomerLayout.setVisibility(View.VISIBLE);
                    existingCustomerLayout.setVisibility(View.GONE);
                }
            });


            btnExistingCustomer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    btnNewCustomer.setBackgroundResource(R.drawable.customer_btn_1_bg);
                    btnExistingCustomer.setBackgroundResource(R.drawable.customer_btn_2_bg);

                    newCustomerLayout.setVisibility(View.GONE);
                    existingCustomerLayout.setVisibility(View.VISIBLE);

                }
            });



//            txtBranch.setOnKeyListener(new View.OnKeyListener() {
//                @Override
//                public boolean onKey(View v, int keyCode, KeyEvent event) {
//                    return (keyCode == KeyEvent.KEYCODE_ENTER);
//                }
//            });


            txtBranch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    BranchesPickerDialog();
                }
            });



        }



        BranchesData();


        return rootView;

    }



    /**
     *
     * @param month
     * @param year
     */
    private void setGridCellAdapterToDate(int month, int year) {
        adapter = new GridCellAdapter(getActivity(),  R.id.calendar_day_gridcell, month, year);
        _calendar.set(year, month - 1, _calendar.get(Calendar.DAY_OF_MONTH));

        SimpleDateFormat sdf = new SimpleDateFormat("MMMM yyyy",locale);
        String format = sdf.format(_calendar.getTime());

//            currentMonth.setText(DateFormat.format(dateTemplate,  _calendar.getTime()));

        lblCurrentMonth.setText(format);

        adapter.notifyDataSetChanged();
        calendarView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        clicked_date = "1-October-20";
        if (v == prevMonth) {
            if (month <= 1) {
                month = 12;
                year--;
            } else {
                month--;
            }
//            Log.d(tag, "Setting Prev Month in GridCellAdapter: " + "Month: "
//                    + month + " Year: " + year);
            setGridCellAdapterToDate(month, year);
        }
        if (v == nextMonth) {
            if (month > 11) {
                month = 1;
                year++;
            } else {
                month++;
            }
//            Log.d(tag, "Setting Next Month in GridCellAdapter: " + "Month: "
//                    + month + " Year: " + year);
            setGridCellAdapterToDate(month, year);
        }


    }

//    @Override
//    public void onDestroy() {
////        Log.d(tag, "Destroying View ...");
//        super.onDestroy();
//    }

    // Inner Class
    public class GridCellAdapter extends BaseAdapter implements View.OnClickListener {
        private static final String tag = "GridCellAdapter";
        private final Context _context;

        private final List<String> list;
        private static final int DAY_OFFSET = 1;
        private final String[] weekdays = new String[] { "Sun", "Mon", "Tue",
                "Wed", "Thu", "Fri", "Sat" };
        private final String[] months = { "January", "February", "March",
                "April", "May", "June", "July", "August", "September",
                "October", "November", "December" };
        private final int[] daysOfMonth = { 31, 28, 31, 30, 31, 30, 31, 31, 30,
                31, 30, 31 };
        private int daysInMonth;
        private int currentDayOfMonth;
        private int currentWeekDay;
        private TextView gridcell;
        private ImageView currentBack;
        private RelativeLayout cellLayout;
        //		private TextView num_events_per_day;
//		private final HashMap<String, Integer> eventsPerMonthMap;
        private final SimpleDateFormat dateFormatter = new SimpleDateFormat(
                "dd-MMM-yyyy");



        // Days in Current Month
        public GridCellAdapter(Context context, int textViewResourceId,
                               int month, int year) {
            super();
            this._context = context;
            this.list = new ArrayList<String>();
//            Log.d(tag, "==> Passed in Date FOR Month: " + month + " "
//                    + "Year: " + year);
            Calendar calendar = Calendar.getInstance();
            setCurrentDayOfMonth(calendar.get(Calendar.DAY_OF_MONTH));
            setCurrentWeekDay(calendar.get(Calendar.DAY_OF_WEEK));
//            Log.d(tag, "New Calendar:= " + calendar.getTime().toString());
//            Log.d(tag, "CurrentDayOfWeek :" + getCurrentWeekDay());
//            Log.d(tag, "CurrentDayOfMonth :" + getCurrentDayOfMonth());

            // Print Month
            printMonth(month, year);

            // Find Number of Events
//			eventsPerMonthMap = findNumberOfEventsPerMonth(year, month);
        }

        private String getMonthAsString(int i) {
            return months[i];
        }

        private String getWeekDayAsString(int i) {
            return weekdays[i];
        }

        private int getNumberOfDaysOfMonth(int i) {
            return daysOfMonth[i];
        }

        public String getItem(int position) {
            return list.get(position);
        }

        @Override
        public int getCount() {
            return list.size();
        }

        /**
         * Prints Month
         *
         * @param mm
         * @param yy
         */
        private void printMonth(int mm, int yy) {
//            Log.d(tag, "==> printMonth: mm: " + mm + " " + "yy: " + yy);
            int trailingSpaces = 0;
            int daysInPrevMonth = 0;
            int prevMonth = 0;
            int prevYear = 0;
            int nextMonth = 0;
            int nextYear = 0;

            int currentMonth = mm - 1;
            String currentMonthName = getMonthAsString(currentMonth);
            daysInMonth = getNumberOfDaysOfMonth(currentMonth);

//            Log.d(tag, "Current Month: " + " " + currentMonthName + " having "
//                    + daysInMonth + " days.");

            GregorianCalendar cal = new GregorianCalendar(yy, currentMonth, 1);
//            Log.d(tag, "Gregorian Calendar:= " + cal.getTime().toString());

            if (currentMonth == 11) {
                prevMonth = currentMonth - 1;
                daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
                nextMonth = 0;
                prevYear = yy;
                nextYear = yy + 1;
//                Log.d(tag, "*->PrevYear: " + prevYear + " PrevMonth:"
//                        + prevMonth + " NextMonth: " + nextMonth
//                        + " NextYear: " + nextYear);
            } else if (currentMonth == 0) {
                prevMonth = 11;
                prevYear = yy - 1;
                nextYear = yy;
                daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
                nextMonth = 1;
//                Log.d(tag, "**--> PrevYear: " + prevYear + " PrevMonth:"
//                        + prevMonth + " NextMonth: " + nextMonth
//                        + " NextYear: " + nextYear);
            } else {
                prevMonth = currentMonth - 1;
                nextMonth = currentMonth + 1;
                nextYear = yy;
                prevYear = yy;
                daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
//                Log.d(tag, "***---> PrevYear: " + prevYear + " PrevMonth:"
//                        + prevMonth + " NextMonth: " + nextMonth
//                        + " NextYear: " + nextYear);
            }

            int currentWeekDay = cal.get(Calendar.DAY_OF_WEEK) - 1;
            trailingSpaces = currentWeekDay;

//            Log.d(tag, "Week Day:" + currentWeekDay + " is "
//                    + getWeekDayAsString(currentWeekDay));
//            Log.d(tag, "No. Trailing space to Add: " + trailingSpaces);
//            Log.d(tag, "No. of Days in Previous Month: " + daysInPrevMonth);

            if (cal.isLeapYear(cal.get(Calendar.YEAR)))
                if (mm == 2)
                    ++daysInMonth;
                else if (mm == 3)
                    ++daysInPrevMonth;

            // Trailing Month days
            for (int i = 0; i < trailingSpaces; i++) {
//                Log.d(tag,
//                        "PREV MONTH:= "
//                                + prevMonth
//                                + " => "
//                                + getMonthAsString(prevMonth)
//                                + " "
//                                + String.valueOf((daysInPrevMonth
//                                - trailingSpaces + DAY_OFFSET)
//                                + i));
                list.add(String
                        .valueOf((daysInPrevMonth - trailingSpaces + DAY_OFFSET)
                                + i)
                        + "-GREY"
                        + "-"
                        + getMonthAsString(prevMonth)
                        + "-"
                        + prevYear);
            }

            // Current Month Days
            for (int i = 1; i <= daysInMonth; i++) {
//                Log.d(currentMonthName, String.valueOf(i) + " "
//                        + getMonthAsString(currentMonth) + " " + yy);
//				if (i == getCurrentDayOfMonth()) {
//					list.add(String.valueOf(i) + "-BLUE" + "-"
//							+ getMonthAsString(currentMonth) + "-" + yy);
//				} else {
//					list.add(String.valueOf(i) + "-WHITE" + "-"
//							+ getMonthAsString(currentMonth) + "-" + yy);
//				}



                Calendar calendar1 = Calendar.getInstance();

                int year1 = calendar1.get(Calendar.YEAR);
                int month1 = calendar1.get(Calendar.MONTH);
                int day1 = calendar1.get(Calendar.DAY_OF_MONTH);

                String eventDate = i+"-"+getMonthAsString(currentMonth)+"-"+yy;

//                Log.d("EventDate",eventDate);

//                if (eventDatesArr!=null && eventDatesArr.length>0&&Arrays.asList(eventDatesArr).contains(eventDate)) {
//                    list.add(String.valueOf(i) + "-EVENT" + "-"
//                            + getMonthAsString(currentMonth) + "-" + yy);
//
//                }else if ((year1 == yy && month1 == currentMonth && i == getCurrentDayOfMonth())) {
//                    list.add(String.valueOf(i) + "-BLUE" + "-"
//                            + getMonthAsString(currentMonth) + "-" + yy);
//                }else if(clicked_date.equals(eventDate)){
//                    list.add(String.valueOf(i) + "-ORANGE" + "-"
//                            + getMonthAsString(currentMonth) + "-" + yy);
//                }else{
//                    list.add(String.valueOf(i) + "-WHITE" + "-"
//                            + getMonthAsString(currentMonth) + "-" + yy);
//                }


//                if ((year1 == yy && month1 == currentMonth && i == getCurrentDayOfMonth())) {
//                    list.add(String.valueOf(i) + "-ORANGE" + "-"
//                            + getMonthAsString(currentMonth) + "-" + yy);
//                }else {
//                    list.add(String.valueOf(i) + "-WHITE" + "-"
//                            + getMonthAsString(currentMonth) + "-" + yy);
//                }

                Date date1 = null,date2 = null;
                String week_day = null;
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-d", Locale.ENGLISH);
                    SimpleDateFormat outputFormat2 = new SimpleDateFormat("EEEE", Locale.ENGLISH);
                    date1 = sdf.parse(""+year1+"-"+(month1+1)+"-"+day1);
                    date2 = sdf.parse(""+yy+"-"+(currentMonth+1)+"-"+i);
                    week_day = outputFormat2.format(date2);
                    Log.e("week_day", week_day);

                } catch (Exception e) {
                    e.printStackTrace();
                }


                if(date1.after(date2)||date1.equals(date2)){
                    Log.e("app", "Date1 is before Date2");
//                    Log.e("PreviousDates*********", ""+yy+"-"+currentMonth+"-"+i);


                }

                Log.e("Dates1*********", ""+year1+"-"+(month1-1)+"-"+day1);
                Log.e("Dates2*********", ""+yy+"-"+(currentMonth-1)+"-"+i);


                if(clicked_date.equals(eventDate)){
                    list.add(String.valueOf(i) + "-ORANGE" + "-"
                            + getMonthAsString(currentMonth) + "-" + yy);
                }else if (date1.after(date2)||week_day.equals("Friday")||date1.equals(date2)) {
                    list.add(String.valueOf(i) + "-RED" + "-"
                            + getMonthAsString(currentMonth) + "-" + yy);
                }else if ((year1 == yy && month1 == currentMonth && i == getCurrentDayOfMonth())) {
                    list.add(String.valueOf(i) + "-BLUE" + "-"
                            + getMonthAsString(currentMonth) + "-" + yy);
//                }else if (week_day.equals("Friday")) {
//                    list.add(String.valueOf(i) + "-RED" + "-"
//                            + getMonthAsString(currentMonth) + "-" + yy);
                }else{
                    list.add(String.valueOf(i) + "-WHITE" + "-"
                            + getMonthAsString(currentMonth) + "-" + yy);
                }

            }

            // Leading Month days
            for (int i = 0; i < list.size() % 7; i++) {
//                Log.d(tag, "NEXT MONTH:= " + getMonthAsString(nextMonth));
                list.add(String.valueOf(i + 1) + "-GREY" + "-"
                        + getMonthAsString(nextMonth) + "-" + nextYear);
            }
        }


        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = convertView;
            if (row == null) {
                LayoutInflater inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                if(GlobalFunctions.getLang(getActivity()).equals("ar")){
                    row = inflater.inflate(R.layout.screen_gridcell_ar, parent, false);
                }else {
                    row = inflater.inflate(R.layout.screen_gridcell, parent, false);
                }

            }

            // Get a reference to the Day gridcell
            gridcell = (TextView) row.findViewById(R.id.calendar_day_gridcell);
//            gridcell.setOnClickListener(this);

            currentBack = (ImageView) row.findViewById(R.id.currentBack);

            cellLayout = (RelativeLayout) row.findViewById(R.id.cellLayout);
            cellLayout.setOnClickListener(this);

            // ACCOUNT FOR SPACING

//            Log.d(tag, "Current Day: " + getCurrentDayOfMonth());
            String[] day_color = list.get(position).split("-");
            String theday = day_color[0];
            String themonth = day_color[2];
            String theyear = day_color[3];

            // Set the Day GridCell
            gridcell.setText(theday);
            gridcell.setTag(theday + "-" + themonth + "-" + theyear);
//            Log.d(tag, "Setting GridCell " + theday + "-" + themonth + "-"
//                    + theyear);
            cellLayout.setTag(theday + "-" + themonth + "-" + theyear);

            gridcell.setTextColor(getResources().getColor(R.color.text_color));

//            if (day_color[1].equals("ORANGE")) {
//                gridcell.setTextColor(getResources().getColor(R.color.text_color));
//                currentBack.setVisibility(View.GONE);
//                cellLayout.setVisibility(View.VISIBLE);
////                currentBack.setBackgroundResource(R.drawable.cal_click_background);
//            }
////
////            if (day_color[1].equals("BLUE")) {
////                gridcell.setTextColor(getResources().getColor(R.color.white));
////                currentBack.setVisibility(View.VISIBLE);
////                currentBack.setBackgroundResource(R.drawable.current_date_background);
////            }
////
////
//            if (day_color[1].equals("GREY")) {
//                gridcell.setTextColor(getResources().getColor(R.color.white_color));
//                cellLayout.setVisibility(View.GONE);
//                currentBack.setVisibility(View.GONE);
//            }
////
////
//            if (day_color[1].equals("WHITE")) {
//                gridcell.setTextColor(getResources().getColor(R.color.text_color));
//                cellLayout.setVisibility(View.VISIBLE);
//                currentBack.setVisibility(View.GONE);
//            }



            if (day_color[1].equals("ORANGE")) {
                gridcell.setTextColor(getResources().getColor(R.color.white_color));
                currentBack.setVisibility(View.VISIBLE);
                cellLayout.setVisibility(View.VISIBLE);
                currentBack.setBackgroundResource(R.drawable.cal_click_background);
            }

            if (day_color[1].equals("RED")) {
                gridcell.setTextColor(getResources().getColor(R.color.disable_date_color));
                cellLayout.setVisibility(View.VISIBLE);
                currentBack.setVisibility(View.GONE);
            }


            if (day_color[1].equals("GREY")) {
                gridcell.setTextColor(getResources().getColor(R.color.white_color));
                cellLayout.setVisibility(View.GONE);
                currentBack.setVisibility(View.GONE);
            }


            if (day_color[1].equals("WHITE")) {
                gridcell.setTextColor(getResources().getColor(R.color.text_color));
                cellLayout.setVisibility(View.VISIBLE);
                currentBack.setVisibility(View.GONE);
            }





//
//
//            if (day_color[1].equals("EVENT")) {
//                gridcell.setTextColor(getResources().getColor(R.color.lightgray));
//                currentBack.setVisibility(View.VISIBLE);
//                currentBack.setBackgroundResource(R.drawable.event_date_background);
//            }
            return row;
        }

        @Override
        public void onClick(View view) {
            String date_month_year = (String) view.getTag();

//            Fragment selectionFragment = new Selection_Fragment();
//            Bundle bundle1 = new Bundle();
//            bundle1.putString("SelectedDate", selected_date);
//            selectionFragment.setArguments(bundle1);
//            ((Main_Activity) getActivity()).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectionFragment).addToBackStack(null).commit();




            if(gridcell.getVisibility()==View.VISIBLE){

                if(pickerLayout.getVisibility()==View.VISIBLE){
                    pickerLayout.startAnimation(endAnim);
                    pickerLayout.setVisibility(View.GONE);
                }


                clicked_date = date_month_year;



                SimpleDateFormat inputFormat1 = new SimpleDateFormat("d-MMMM-yyyy", Locale.ENGLISH);
                SimpleDateFormat inputFormat2 = new SimpleDateFormat("yyyy-MM-d", Locale.ENGLISH);
                SimpleDateFormat outputFormat1 = new SimpleDateFormat("ddMMyyyy", Locale.ENGLISH);

                SimpleDateFormat outputFormat2 = new SimpleDateFormat("EEEE", Locale.ENGLISH);

                Calendar calendar1 = Calendar.getInstance();

                int year1 = calendar1.get(Calendar.YEAR);
                int month1 = calendar1.get(Calendar.MONTH);
                int day1 = calendar1.get(Calendar.DAY_OF_MONTH);


                Date date1 = null,date2 = null;
                try {
                    Date date = inputFormat1.parse(date_month_year);
                    String parse_event_date = outputFormat1.format(date);
                    String week_day = outputFormat2.format(date);

                    date1 = inputFormat2.parse(""+year1+"-"+(month1+1)+"-"+day1);
                    date2 = inputFormat1.parse(date_month_year);

                    Log.v("Date",parse_event_date);
                    slot_date = parse_event_date;
                    selectSlot.setText("");



                    if(date1.after(date2)||week_day.equals("Friday")||date1.equals(date2)){
//                        if(GlobalFunctions.getLang(getActivity()).equals("ar")){
//                            alertDialog("لا فتحات المتاحة");
//                        }else {
//                            alertDialog("No Slots Available");
//                        }
                    }else {
//                        loadData();

                        setGridCellAdapterToDate(month, year);
                    }



                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }


            }else {
//                slot_date = "";
            }



        }

        public int getCurrentDayOfMonth() {
            return currentDayOfMonth;
        }

        private void setCurrentDayOfMonth(int currentDayOfMonth) {
            this.currentDayOfMonth = currentDayOfMonth;
        }

        public void setCurrentWeekDay(int currentWeekDay) {
            this.currentWeekDay = currentWeekDay;
        }

        public int getCurrentWeekDay() {
            return currentWeekDay;
        }


    }


    private void loadData() {

        if (GlobalFunctions.hasConnection(getActivity())) {

            slotTimeIdArrList.clear();
            slotTimeArrList.clear();

            showLoading();

            AsyncHttpClient client = new AsyncHttpClient();

            RequestParams params = new RequestParams();

            params.put("ran", GlobalFunctions.getRandom());
            params.put("visitDate", slot_date);
            client.get(GlobalFunctions.serviceURL + "VisitRequestSlots" , params, new AsyncHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] bytes) {

                    hideLoading();

                    String response = new String(bytes);

                    Log.d("onSuccess", response);

                    JSONArray arr, arr1;
                    JSONObject obj, obj1, obj2;

                    try {

                        obj = ((JSONObject) new JSONTokener(response).nextValue()).getJSONObject("result");

                        if (obj.getString("status").equalsIgnoreCase("1")) {

                            arr = obj.getJSONArray("data");

                            for (int i = 0; i < arr.length(); i++) {

                                String visit_slot_id = ""+arr.getJSONObject(i).get("visit_slot_id");
                                String visit_slots_desc = ""+arr.getJSONObject(i).get("visit_slots_desc");

                                slotTimeIdArrList.add(visit_slot_id);
                                slotTimeArrList.add(""+visit_slots_desc);

                            }

                            slotTimeIdArr = slotTimeIdArrList.toArray(new String[slotTimeIdArrList.size()]);
                            slotTimeArr = slotTimeArrList.toArray(new String[slotTimeArrList.size()]);


                        } else {

                            GlobalFunctions.showToastError(getActivity(), obj.getString("msg"));

                        }

                    } catch (JSONException e) {

                        if (GlobalFunctions.getLang(getActivity()).equals("ar")) {
                            GlobalFunctions.showToastError(getActivity(), getString(R.string.msg_error_ar));
                        }else {
                            GlobalFunctions.showToastError(getActivity(), getString(R.string.msg_error));
                        }

                    }

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] bytes, Throwable throwable) {

                    hideLoading();

                    if (GlobalFunctions.getLang(getActivity()).equals("ar")) {
                        GlobalFunctions.showToastError(getActivity(), getString(R.string.msg_error_ar));
                    }else {
                        GlobalFunctions.showToastError(getActivity(), getString(R.string.msg_error));
                    }

                }
            });

        } else {

            if (GlobalFunctions.getLang(getActivity()).equals("ar")) {
                GlobalFunctions.showToastError(getActivity(), getString(R.string.msg_no_internet_ar));
            }else {
                GlobalFunctions.showToastError(getActivity(), getString(R.string.msg_no_internet));
            }

        }

    }


    private void visitUsContentData() {

        if (GlobalFunctions.hasConnection(getActivity())) {

            showLoading();

            AsyncHttpClient client = new AsyncHttpClient();

            RequestParams params = new RequestParams();

            params.put("ran", GlobalFunctions.getRandom());

            client.get(GlobalFunctions.serviceURL + "getRequestVisitContent" , params, new AsyncHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] bytes) {

                    hideLoading();

                    String response = new String(bytes);

                    Log.d("onSuccess", response);

                    JSONArray arr, arr1;
                    JSONObject obj, obj1, obj2;

                    try {

                        obj = ((JSONObject) new JSONTokener(response).nextValue()).getJSONObject("result");

                        if (obj.getString("status").equalsIgnoreCase("1")) {

                            String title_en = obj.getString("RQDishTitleEN");
                            String title_ar = obj.getString("RQDishTitleAR");
                            String ex_desc_en = obj.getString("RQDishExDescEN");
                            String ex_desc_ar = obj.getString("RQDishExDescAR");
                            String new_desc_en = obj.getString("RQDishNewDescEN");
                            String new_desc_ar = obj.getString("RQDishNewDescAR");
                            String ex_thanks_en = obj.getString("RQDishExThanksEN");
                            String ex_thanks_ar = obj.getString("RQDishExThanksAR");
                            String new_thanks_en = obj.getString("RQDishNewThanksEN");
                            String new_thanks_ar = obj.getString("RQDishNewThanksAR");

                            if (GlobalFunctions.getLang(getActivity()).equals("ar")) {
//                                newCustomerTxt.setText(new_desc_ar);
//                                existingCustomerTxt.setText(ex_desc_ar);

                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                    newCustomerTxt.setText(Html.fromHtml(new_desc_ar, Html.FROM_HTML_MODE_COMPACT));
                                    existingCustomerTxt.setText(Html.fromHtml(ex_desc_ar, Html.FROM_HTML_MODE_COMPACT));
                                } else {
                                    newCustomerTxt.setText(Html.fromHtml(new_desc_ar));
                                    existingCustomerTxt.setText(Html.fromHtml(ex_desc_ar));
                                }

                            }else {
//                                newCustomerTxt.setText(new_desc_en);
//                                existingCustomerTxt.setText(ex_desc_en);

                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                    newCustomerTxt.setText(Html.fromHtml(new_desc_en, Html.FROM_HTML_MODE_COMPACT));
                                    existingCustomerTxt.setText(Html.fromHtml(ex_desc_en, Html.FROM_HTML_MODE_COMPACT));
                                } else {
                                    newCustomerTxt.setText(Html.fromHtml(new_desc_en));
                                    existingCustomerTxt.setText(Html.fromHtml(ex_desc_en));
                                }

                            }

                        } else {

                            GlobalFunctions.showToastError(getActivity(), obj.getString("msg"));

                        }

                    } catch (JSONException e) {

                        if (GlobalFunctions.getLang(getActivity()).equals("ar")) {
                            GlobalFunctions.showToastError(getActivity(), getString(R.string.msg_error_ar));
                        }else {
                            GlobalFunctions.showToastError(getActivity(), getString(R.string.msg_error));
                        }

                    }

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] bytes, Throwable throwable) {

                    hideLoading();

                    if (GlobalFunctions.getLang(getActivity()).equals("ar")) {
                        GlobalFunctions.showToastError(getActivity(), getString(R.string.msg_error_ar));
                    }else {
                        GlobalFunctions.showToastError(getActivity(), getString(R.string.msg_error));
                    }

                }
            });

        } else {

            if (GlobalFunctions.getLang(getActivity()).equals("ar")) {
                GlobalFunctions.showToastError(getActivity(), getString(R.string.msg_no_internet_ar));
            }else {
                GlobalFunctions.showToastError(getActivity(), getString(R.string.msg_no_internet));
            }

        }

    }


    private void BranchesData() {

        if (GlobalFunctions.hasConnection(getActivity())) {

            branchIdArrList.clear();
            branchNameArrList.clear();

            showLoading();

            AsyncHttpClient client = new AsyncHttpClient();

            RequestParams params = new RequestParams();

            params.put("ran", GlobalFunctions.getRandom());

            client.get(GlobalFunctions.serviceURL + "getBranches" , params, new AsyncHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] bytes) {

                    hideLoading();

                    String response = new String(bytes);

                    Log.d("onSuccess", response);

                    JSONArray arr, arr1;
                    JSONObject obj, obj1, obj2;

                    try {

                        obj = ((JSONObject) new JSONTokener(response).nextValue()).getJSONObject("result");

                        if (obj.getString("status").equalsIgnoreCase("1")) {

                            arr = obj.getJSONArray("data");

                            for (int i = 0; i < arr.length(); i++) {

                                String branch_id = ""+arr.getJSONObject(i).get("branch_id");
                                String branch_name = ""+arr.getJSONObject(i).get("branch_name");

                                branchIdArrList.add(branch_id);
                                branchNameArrList.add(branch_name);


                            }

                            branchIdArr = branchIdArrList.toArray(new String[branchIdArrList.size()]);
                            branchNameArr = branchNameArrList.toArray(new String[branchNameArrList.size()]);


                        } else {

                            GlobalFunctions.showToastError(getActivity(), obj.getString("msg"));

                        }


                        visitUsContentData();

                    } catch (JSONException e) {

                        if (GlobalFunctions.getLang(getActivity()).equals("ar")) {
                            GlobalFunctions.showToastError(getActivity(), getString(R.string.msg_error_ar));
                        }else {
                            GlobalFunctions.showToastError(getActivity(), getString(R.string.msg_error));
                        }

                    }

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] bytes, Throwable throwable) {

                    hideLoading();

                    if (GlobalFunctions.getLang(getActivity()).equals("ar")) {
                        GlobalFunctions.showToastError(getActivity(), getString(R.string.msg_error_ar));
                    }else {
                        GlobalFunctions.showToastError(getActivity(), getString(R.string.msg_error));
                    }

                }
            });

        } else {

            if (GlobalFunctions.getLang(getActivity()).equals("ar")) {
                GlobalFunctions.showToastError(getActivity(), getString(R.string.msg_no_internet_ar));
            }else {
                GlobalFunctions.showToastError(getActivity(), getString(R.string.msg_no_internet));
            }

        }

    }


    private void SubmitExistingCustomerData() {

        if (GlobalFunctions.isEmptyText(txtName)) {
            txtName.requestFocus();
            if(GlobalFunctions.getLang(getActivity()).equals("ar")){
                txtName.setError(getString(R.string.req_name_ar));
            }else {
                txtName.setError(getString(R.string.req_name));
            }

            return;
        }

        if (GlobalFunctions.isEmptyText(txtMobile)) {
            txtMobile.requestFocus();
            if(GlobalFunctions.getLang(getActivity()).equals("ar")){
                txtMobile.setError(getString(R.string.req_mobile_number_ar));
            }else {
                txtMobile.setError(getString(R.string.req_mobile_number));
            }

            return;
        }

        if (GlobalFunctions.containsSpaces(txtMobile)) {
            txtMobile.requestFocus();
            if(GlobalFunctions.getLang(getActivity()).equals("ar")){
                txtMobile.setError(getString(R.string.mobile_cannot_contain_spaces_ar));
            }else {
                txtMobile.setError(getString(R.string.mobile_cannot_contain_spaces));
            }

            return;
        }

        if (GlobalFunctions.isEmptyText(txtEmail)) {
            txtEmail.requestFocus();
            if(GlobalFunctions.getLang(getActivity()).equals("ar")){
                txtEmail.setError(getString(R.string.req_email_address_ar));
            }else {
                txtEmail.setError(getString(R.string.req_email_address));
            }

            return;
        }

        if (GlobalFunctions.containsSpaces(txtEmail)) {
            txtEmail.requestFocus();
            if(GlobalFunctions.getLang(getActivity()).equals("ar")){
                txtEmail.setError(getString(R.string.email_cannot_contain_spaces_ar));
            }else {
                txtEmail.setError(getString(R.string.email_cannot_contain_spaces));
            }

            return;
        }


        if (GlobalFunctions.isEmptyText(txtAddress)) {
            txtAddress.requestFocus();
            if(GlobalFunctions.getLang(getActivity()).equals("ar")){
                txtAddress.setError(getString(R.string.req_address_ar));
            }else {
                txtAddress.setError(getString(R.string.req_address));
            }

            return;
        }


        if (GlobalFunctions.isEmptyText(txtBranch)) {
//            txtAddress.requestFocus();
            if(GlobalFunctions.getLang(getActivity()).equals("ar")){
                txtBranch.setError(getString(R.string.please_select_branch_ar));
            }else {
                txtBranch.setError(getString(R.string.please_select_branch));
            }

            return;
        }




        if (GlobalFunctions.hasConnection(getActivity())) {


            showLoading();

            AsyncHttpClient client = new AsyncHttpClient();

            RequestParams params = new RequestParams();


            params.put("vemailAddress", txtEmail.getText().toString().trim());
            params.put("vname", txtName.getText().toString().trim());
            params.put("branch", branch_Id);
            params.put("vaddress", txtAddress.getText().toString().trim());
            params.put("vmobileNo", txtMobile.getText().toString().trim());

            params.put("ran", GlobalFunctions.getRandom());

            client.get(GlobalFunctions.serviceURL + "submitVisitRequestExWeb" , params, new AsyncHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] bytes) {

                    hideLoading();

                    String response = new String(bytes);

                    Log.d("onSuccess", response);

                    JSONArray arr, arr1;
                    JSONObject obj, obj1, obj2;

                    try {

                        obj = ((JSONObject) new JSONTokener(response).nextValue()).getJSONObject("result");

                        if (obj.getString("status").equalsIgnoreCase("1")) {

//                            txtName.setText("");
//                            txtMobile.setText("");fcr
//                            txtEmail.setText("");
//                            txtAddress.setText("");
//                            txtBranch.setText("");

                            successDialog(obj.getString("msg"));

                        } else {

                            GlobalFunctions.showToastError(getActivity(), obj.getString("msg"));

                        }

                    } catch (JSONException e) {

                        if (GlobalFunctions.getLang(getActivity()).equals("ar")) {
                            GlobalFunctions.showToastError(getActivity(), getString(R.string.msg_error_ar));
                        }else {
                            GlobalFunctions.showToastError(getActivity(), getString(R.string.msg_error));
                        }

                    }

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] bytes, Throwable throwable) {

                    hideLoading();

                    if (GlobalFunctions.getLang(getActivity()).equals("ar")) {
                        GlobalFunctions.showToastError(getActivity(), getString(R.string.msg_error_ar));
                    }else {
                        GlobalFunctions.showToastError(getActivity(), getString(R.string.msg_error));
                    }

                }
            });

        } else {

            if (GlobalFunctions.getLang(getActivity()).equals("ar")) {
                GlobalFunctions.showToastError(getActivity(), getString(R.string.msg_no_internet_ar));
            }else {
                GlobalFunctions.showToastError(getActivity(), getString(R.string.msg_no_internet));
            }

        }

    }


    private void showLoading() {

        if (dlgLoading == null) {

            dlgLoading = GlobalFunctions.showLoading(getActivity());

        } else {

            dlgLoading.show();
        }

    }

    private void hideLoading() {

        dlgLoading.dismiss();

    }

    public void timeSlotPickerDialog(){

        pickerLayout.startAnimation(startAnim);
        pickerLayout.setVisibility(View.VISIBLE);

        picker.setMinValue(0);
//        picker.setDisplayedValues(null);
        picker.setMaxValue(slotTimeArr.length-1);
        picker.setValue(0);
        picker.setDisplayedValues(slotTimeArr);
        picker.setWrapSelectorWheel(false);
        picker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

    }


    public void BranchesPickerDialog(){

        pickerLayout.startAnimation(startAnim);
        pickerLayout.setVisibility(View.VISIBLE);

        picker.setMinValue(0);
//        picker.setDisplayedValues(null);
        picker.setMaxValue(branchNameArr.length-1);
        picker.setValue(0);
        picker.setDisplayedValues(branchNameArr);
        picker.setWrapSelectorWheel(false);
        picker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

    }

    private void setDividerColor(NumberPicker picker, int color) {

        java.lang.reflect.Field[] pickerFields = NumberPicker.class.getDeclaredFields();
        for (java.lang.reflect.Field pf : pickerFields) {
            if (pf.getName().equals("mSelectionDivider")) {
                pf.setAccessible(true);
                try {
                    ColorDrawable colorDrawable = new ColorDrawable(color);
                    pf.set(picker, colorDrawable);
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (Resources.NotFoundException e) {
                    e.printStackTrace();
                }
                catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }



    public void alertDialog(String msg) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.info_dailog_box);
        WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.gravity = Gravity.BOTTOM;
//        layoutParams.x = 0;   //x position
//        layoutParams.y = 30;
        dialog.getWindow().setAttributes(layoutParams);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        final TextView alertMessage = (TextView) dialog.findViewById(R.id.alertMessage);

        alertMessage.setText(msg);

        Button okBtn = (Button) dialog.findViewById(R.id.okBtn);


        if (GlobalFunctions.getLang(getActivity()).equals("ar")) {
            okBtn.setText("تم");
        }else {
            okBtn.setText("Ok");
        }


        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();

            }
        });

        dialog.show();

    }


    public void successDialog(String msg) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.login_success_dialog_box);
        WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.gravity = Gravity.BOTTOM;
//        layoutParams.x = 0;   //x position
//        layoutParams.y = 30;
        dialog.getWindow().setAttributes(layoutParams);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        final TextView alertMessage = (TextView) dialog.findViewById(R.id.alertMessage);
        alertMessage.setText(msg);
        Button okBtn = (Button) dialog.findViewById(R.id.okBtn);


        if (GlobalFunctions.getLang(getActivity()).equals("ar")) {
            okBtn.setText(getString(R.string.ok_ar));
        }else {
            okBtn.setText(getString(R.string.ok));
        }

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();

//                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        dialog.show();

    }

}
