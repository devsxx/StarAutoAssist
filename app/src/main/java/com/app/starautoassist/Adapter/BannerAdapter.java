package com.app.starautoassist.Adapter;


import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.app.starautoassist.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class BannerAdapter extends PagerAdapter {
        private Activity activity;
        ArrayList<String> images;

        public BannerAdapter(Activity activity,ArrayList<String> images){
            this.activity = activity;
            this.images = images;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            LayoutInflater inflater = activity.getLayoutInflater();
            View viewItem = inflater.inflate(R.layout.banneritem, container, false);
            ImageView imageView = viewItem.findViewById(R.id.serviceimage);
            Glide.with(activity)
                    .load(images.get(position))
                    .into(imageView);

            container.addView(viewItem);

            return viewItem;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return images.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            // TODO Auto-generated method stub
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            // TODO Auto-generated method stub
            container.removeView((View) object);
        }
    }

