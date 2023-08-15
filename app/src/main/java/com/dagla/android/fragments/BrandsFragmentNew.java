package com.dagla.android.fragments;

import android.content.Context;
import android.database.MatrixCursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AlphabetIndexer;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.dagla.android.R;
import com.dagla.android.activity.MainActivity;
import com.dagla.android.adapter.BrandsAdapterNew;
import com.dagla.android.parser.Glossary;

import java.util.ArrayList;
import java.util.Collections;

public class BrandsFragmentNew extends Fragment {


    View rootView;
    Context context;
    Bundle savedInstanceState;

    ListView lv;
    private LinearLayout mIndexerLayout;
    private FrameLayout mTitleLayout;
    private TextView mTitleText;
    private RelativeLayout mSectionToastLayout;
    private TextView mSectionToastText;
    EditText txtSearch;

    private ArrayList<Glossary> glossaries = new ArrayList<Glossary>();
    private String alphabet = "#0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private AlphabetIndexer mIndexer;
    private BrandsAdapterNew mAdapter;
    private int lastSelectedPosition = -1;

//    BrandsAdapterNew arrAdapter;
    Typeface custom_fontbold, custom_fontnormal;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //just change the fragment_dashboard
        //with the fragment you want to inflate
        //like if the class is HomeFragment it should have R.layout.home_fragment
        //if it is DashboardFragment it should have R.layout.fragment_dashboard

        context = getActivity();

        custom_fontbold = Typeface.createFromAsset(getActivity().getAssets(), "fonts/avenir-next-bold.ttf");
        custom_fontnormal = Typeface.createFromAsset(getActivity().getAssets(), "fonts/avenir-next-regular.ttf");

        ((MainActivity) getActivity()).setHeaders(getResources().getString(R.string.title_brands),true,true,true,false, false ,"0", false);
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_brands_new, container, false);

            mIndexerLayout = (LinearLayout) rootView.findViewById(R.id.indexer_layout);
            lv = (ListView) rootView.findViewById(R.id.lv);
            mTitleLayout = (FrameLayout) rootView.findViewById(R.id.title_layout);
            mTitleText = (TextView) rootView.findViewById(R.id.title_text);
            mSectionToastLayout = (RelativeLayout) rootView.findViewById(R.id.section_toast_layout);
            mSectionToastText = (TextView) rootView.findViewById(R.id.section_toast_text);
            txtSearch = (EditText) rootView.findViewById(R.id.txtSearch);

            for(int i = 0; i < alphabet.length(); i++) {
                TextView letterTextView = new TextView(getActivity());
                letterTextView.setText(alphabet.charAt(i)+"");
                letterTextView.setTextSize(14f);
                letterTextView.setTextColor(Color.parseColor("#222222"));
                letterTextView.setGravity(Gravity.CENTER);
                ViewGroup.LayoutParams params = new LinearLayout.LayoutParams(28, 0, 1.0f);
                letterTextView.setLayoutParams(params);
                letterTextView.setPadding(4, 0, 2, 0);
                mIndexerLayout.addView(letterTextView);
//                mIndexerLayout.setBackgroundResource(R.drawable.letterslist_bg);
                mIndexerLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
            }

            ArrayList arrNames = new ArrayList<String>();

            arrNames.add("Acne Studios");
            arrNames.add("Alexander McQueen");
            arrNames.add("Aquazzura");
            arrNames.add("Balenciaga");
            arrNames.add("Balmain");
            arrNames.add("Bottega Veneta");
            arrNames.add("Burberry");
            arrNames.add("Chloe");
            arrNames.add("Etro");
            arrNames.add("Fendi");
            arrNames.add("Gianvito Rossi");
            arrNames.add("Givenchy");
            arrNames.add("Gucci");
            arrNames.add("Isabel Marant");
            arrNames.add("J Crew");
            arrNames.add("Jimmy Choo");
            arrNames.add("Joseph");
            arrNames.add("Lanvin");
            arrNames.add("Marni");
            arrNames.add("Miu Miu");
            arrNames.add("Oscar de le Renta");
            arrNames.add("Prada");
            arrNames.add("Saint Laurent");
            arrNames.add("Self-Portrait");
            arrNames.add("Stella McCartney");
            arrNames.add("The Row");
            arrNames.add("Tom Ford");
            arrNames.add("Row");
            arrNames.add("Ford");
            arrNames.add("Renta");
            arrNames.add("Choo");
            arrNames.add("1 Renta");
            arrNames.add("2 Choo");
            arrNames.add("#Renta");
            arrNames.add("*Choo");

            Collections.sort(arrNames);


            MatrixCursor cursor = new MatrixCursor(new String[] {"display_name", "sort_key"});

//            for (int j = 0; j < arrNames.size(); j++ ) {
//                cursor.newRow()
//                        .add("display_name", arrNames.get(j).toString())
//                        .add("sort_key", getSortKey(arrNames.get(j).toString()));
//            }

            if (cursor.moveToFirst()) {
                do {
                    String name = cursor.getString(0);
                    String sortKey = getSortKey(cursor.getString(1));
                    Log.e("sortKey from cursor", ""+sortKey);
                    Glossary glossary = new Glossary();
                    glossary.setName(name);
                    glossary.setSortKey(sortKey);
                    glossaries.add(glossary);
                } while (cursor.moveToNext());
            }



            mAdapter = new BrandsAdapterNew(getActivity(), glossaries);
            getActivity().startManagingCursor(cursor);
            mIndexer = new AlphabetIndexer(cursor, 1, alphabet);
            mAdapter.setIndexer(mIndexer);

            if(glossaries != null && glossaries.size() > 0) {
                lv.setAdapter(mAdapter);
                lv.setOnScrollListener(mOnScrollListener);
                mIndexerLayout.setOnTouchListener(mOnTouchListener);
            }

        }



        txtSearch.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {



			}


            @Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

//                String text = txtSearch.getText().toString().toLowerCase(Locale.getDefault());
//
//                mAdapter.filter(text);

                mAdapter.getFilter().filter(txtSearch.getText().toString());
			}
		});

        return rootView;

    }

    private String getSortKey(String sortKeyString) {
        String key = sortKeyString.substring(0, 1).toUpperCase();
        if (key.matches("[A-Z]")||key.matches("[0-9]")) {
            return key;
        }
        return "#";
    }

    private AbsListView.OnScrollListener mOnScrollListener = new AbsListView.OnScrollListener() {

        private int lastFirstVisibleItem = -1;

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            if(scrollState == SCROLL_STATE_IDLE) {
                //mIndexerLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                //mIndexerLayout.setBackgroundResource(R.drawable.letterslist_bg);
            } else {
                //mIndexerLayout.setBackgroundResource(R.drawable.letterslist_bg);
            }
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                             int totalItemCount) {
            // firstVisibleItem corresponding to the index of AlphabetIndexer(eg, B-->Alphabet index is 2)
            int sectionIndex = mIndexer.getSectionForPosition(firstVisibleItem);
            //next section Index corresponding to the positon in the listview
            int nextSectionPosition = mIndexer.getPositionForSection(sectionIndex + 1);
            Log.d("OnScroll", "onScroll()-->firstVisibleItem="+firstVisibleItem+", sectionIndex="
                    +sectionIndex+", nextSectionPosition="+nextSectionPosition);
            if(firstVisibleItem != lastFirstVisibleItem) {
                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) mTitleLayout.getLayoutParams();
                params.topMargin = 0;
                mTitleLayout.setLayoutParams(params);
                mTitleText.setText(String.valueOf(alphabet.charAt(sectionIndex)));
//                ((TextView) mIndexerLayout.getChildAt(sectionIndex)).setBackgroundColor(getResources().getColor(R.color.letter_bg_color));
                ((TextView) mIndexerLayout.getChildAt(sectionIndex)).setBackgroundColor(Color.parseColor("#FFFFFF"));
                lastFirstVisibleItem = firstVisibleItem;
            }

            // update AlphabetIndexer background
            if(sectionIndex != lastSelectedPosition) {
                if(lastSelectedPosition != -1) {
                    ((TextView) mIndexerLayout.getChildAt(lastSelectedPosition)).setBackgroundColor(getResources().getColor(android.R.color.transparent));
                }
                lastSelectedPosition = sectionIndex;
            }

            if(nextSectionPosition == firstVisibleItem + 1) {
                View childView = view.getChildAt(0);
                if(childView != null) {
                    int sortKeyHeight = mTitleLayout.getHeight();
                    int bottom = childView.getBottom();
                    ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) mTitleLayout.getLayoutParams();
                    /*if(bottom < sortKeyHeight) {
                        float pushedDistance = bottom - sortKeyHeight;
                        params.topMargin = (int) pushedDistance;
                        mTitleLayout.setLayoutParams(params);
                    } else {*/
                    if(params.topMargin != 0) {
                        params.topMargin = 0;
                        mTitleLayout.setLayoutParams(params);
                    }
//                    }
                }
            }

        }

    };

    private View.OnTouchListener mOnTouchListener = new View.OnTouchListener() {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            float alphabetHeight = mIndexerLayout.getHeight();
            float y = event.getY();
            int sectionPosition = (int) ((y / alphabetHeight) / (1f / 37f));
            if (sectionPosition < 0) {
                sectionPosition = 0;
            } else if (sectionPosition > 36) {
                sectionPosition = 36;
            }
            if(lastSelectedPosition != sectionPosition) {
                if(-1 != lastSelectedPosition){
//                    ((TextView) mIndexerLayout.getChildAt(lastSelectedPosition)).setBackgroundColor(getResources().getColor(android.R.color.transparent));
                    ((TextView) mIndexerLayout.getChildAt(lastSelectedPosition)).setBackgroundColor(Color.parseColor("#FFFFFF"));
                }
                lastSelectedPosition = sectionPosition;
            }
            String sectionLetter = String.valueOf(alphabet.charAt(sectionPosition));
            int position = mIndexer.getPositionForSection(sectionPosition);
            TextView textView = (TextView) mIndexerLayout.getChildAt(sectionPosition);
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
//                    mIndexerLayout.setBackgroundResource(R.drawable.letterslist_bg);
                    mIndexerLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
//                    textView.setBackgroundColor(getResources().getColor(R.color.letter_bg_color));
                    mSectionToastLayout.setVisibility(View.VISIBLE);
                    mSectionToastText.setText(sectionLetter);
                    lv.smoothScrollToPositionFromTop(position,0,1);
                    break;
                case MotionEvent.ACTION_MOVE:
//                    mIndexerLayout.setBackgroundResource(R.drawable.letterslist_bg);
                    mIndexerLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
//                    textView.setBackgroundColor(getResources().getColor(R.color.letter_bg_color));
                    mSectionToastLayout.setVisibility(View.VISIBLE);
                    mSectionToastText.setText(sectionLetter);
                    lv.smoothScrollToPositionFromTop(position,0,1);
                    break;
                case MotionEvent.ACTION_UP:
                    //mIndexerLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                    mSectionToastLayout.setVisibility(View.GONE);
                default:
                    mSectionToastLayout.setVisibility(View.GONE);
                    break;
            }
            return true;
        }

    };
}
