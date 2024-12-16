package com.kmkz.xuuDiamondsRBXSkins.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;


import com.kmkz.xuuDiamondsRBXSkins.R;
import com.kmkz.xuuDiamondsRBXSkins.activities.InstructionsActivity;

public class MyViewPagerAdapter extends PagerAdapter {

    LayoutInflater layoutInflater;
    int[] about_images_array = {
            R.drawable.ic_spin,
            R.drawable.ic_watch,
            R.drawable.ic_gift,
            R.drawable.ic_withdrawal
    };

    public MyViewPagerAdapter() {
    }

    @NonNull
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        layoutInflater = LayoutInflater.from(container.getContext());
        View view = layoutInflater.inflate(R.layout.item_stepper_wizard, container, false);

        String[] about_title_array = container.getContext().getResources().getStringArray(R.array.about_title_array);
        String[] about_description_array = container.getContext().getResources().getStringArray(R.array.about_description_array);

        ((TextView) view.findViewById(R.id.title)).setText(about_title_array[position]);
        ((TextView) view.findViewById(R.id.description)).setText(about_description_array[position]);
        ((ImageView) view.findViewById(R.id.image)).setImageResource(about_images_array[position]);
        container.addView(view);

        return view;
    }

    @Override
    public int getCount() {
        return InstructionsActivity.MAX_STEP;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object obj) {
        return view == obj;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, @NonNull Object object) {
        View view = (View) object;
        container.removeView(view);
    }
}
