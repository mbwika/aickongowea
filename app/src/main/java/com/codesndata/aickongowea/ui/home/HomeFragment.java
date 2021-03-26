package com.codesndata.aickongowea.ui.home;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.codesndata.aickongowea.MainActivity;
import com.codesndata.aickongowea.R;
import com.codesndata.aickongowea.Slider.SliderAdapter;
import com.codesndata.aickongowea.Slider.SliderItem;
import com.codesndata.aickongowea.covid_19.CovidInfoActivity;
import com.codesndata.aickongowea.socialmedia.SocialMediaActivity;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    SliderView sliderView;
    private SliderAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        HomeViewModel homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        GridLayout grid_layout = root.findViewById(R.id.grid_layout);
        GridLayout bootom_grid_layout = root.findViewById(R.id.bottom_grid_layout);
        homeViewModel.getText().observe(getViewLifecycleOwner(), s -> {
        });
        //set Event
        setSingleEvent(grid_layout);
        setBottomEvent(bootom_grid_layout);
        sliderView = root.findViewById(R.id.imageSlider);

        adapter = new SliderAdapter(getActivity());
        sliderView.setSliderAdapter(adapter);
        sliderView.setIndicatorAnimation(IndicatorAnimationType.SLIDE); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderView.setSliderTransformAnimation(SliderAnimations.CUBEINROTATIONTRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_RIGHT);
        sliderView.setIndicatorSelectedColor(Color.WHITE);
        sliderView.setIndicatorUnselectedColor(Color.GRAY);
        sliderView.setScrollTimeInSec(3);
        sliderView.setAutoCycle(true);
        sliderView.startAutoCycle();

        sliderView.setOnIndicatorClickListener(position -> Log.i("GGG", "onIndicatorClicked: " + sliderView.getCurrentPagePosition()));
        addNewItem();
        return root;
    }

    private void renewItems() {
        List<SliderItem> sliderItemList = new ArrayList<>();
        //dummy data
        for (int i = 0; i < 5; i++) {
            SliderItem sliderItem = new SliderItem();
            sliderItem.setDescription("Slider Item " + (i+1));
            if (i % 2 == 0) {
                sliderItem.setImageUrl("https://images.pexels.com/photos/929778/pexels-photo-929778.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260");
            } else {
                sliderItem.setImageUrl("https://images.pexels.com/photos/747964/pexels-photo-747964.jpeg?auto=compress&cs=tinysrgb&h=750&w=1260");
            }//http://192.168.43.58/ http://192.168.42.203/
//            if (i == 0) {
//                sliderItem.setImageUrl("http://192.168.42.203/aickongowea/slider/a.jpg");
//                sliderItem.setDescription("Slider Item 1");
//            }
//            if(i == 1) {
//                sliderItem.setImageUrl("http://192.168.42.203/aickongowea/slider/b.jpg");
//                sliderItem.setDescription("Slider Item 2");
//            }
//            if(i == 2) {
//                sliderItem.setImageUrl("http://192.168.42.203/aickongowea/slider/c.jpg");
//                sliderItem.setDescription("Slider Item 3");
//            }
//            if(i == 3) {
//                sliderItem.setImageUrl("http://192.168.42.203/aickongowea/slider/d.jpg");
//                sliderItem.setDescription("Slider Item 4");
//            }
//            if(i == 4) {
//                sliderItem.setImageUrl("http://192.168.42.203/aickongowea/slider/e.jpg");
//                sliderItem.setDescription("Slider Item 5");
//            }
            sliderItemList.add(sliderItem);
        }
        adapter.renewItems(sliderItemList);
    }


    private void addNewItem() {
        SliderItem sliderItem = new SliderItem();
        sliderItem.setDescription("Slider Item Added Manually");
        sliderItem.setImageUrl("http://192.168.43.58/aickongowea/slider/a.jpg");
        adapter.addItem(sliderItem);
        renewItems();
    }

    private void setSingleEvent(GridLayout grid_layout) {
        //loop all child items
        for(int i = 0; i < grid_layout.getChildCount(); i++){
            //
            CardView cardView = (CardView) grid_layout.getChildAt(i);
            final int child_no = i;
            cardView.setOnClickListener(v -> {
                if(child_no == 0){
                }
                if(child_no == 1){
                }
                if(child_no == 2){
                }
                if(child_no == 3){
                }
                if(child_no == 4){
                }
                if(child_no == 5){
                    //call getCovidInfo() method
                    getCovidInfo();
                }
                if(child_no == 6){

                }
                if(child_no == 7){
                    //call SocialMediaPage() method
                    goToSocialMediaPage();
                }
                if(child_no == 8){
                }
            });
        }
    }

    private void getCovidInfo(){
        //Load COVID-19 Activity
        Intent covid = new Intent(getActivity(), CovidInfoActivity.class);
        startActivity(covid);
    }
    private void goToSocialMediaPage(){
        //Load Social Media Activity
        Intent media = new Intent(getActivity(), SocialMediaActivity.class);
        startActivity(media);
    }
    private void setBottomEvent(GridLayout bootom_grid_layout) {
        //loop all child items
        for(int i = 0; i < bootom_grid_layout.getChildCount(); i++){
            //
            CardView cardView = (CardView) bootom_grid_layout.getChildAt(i);
            final int child_no = i;
            cardView.setOnClickListener(v -> {
                if(child_no == 0){
                    goHome();
                }
                if(child_no == 1){
                }
                if(child_no == 2){
                }
                if(child_no == 3){
                }
                if(child_no == 4){
                }
            });
        }
    }
    public void goHome(){
        //Load COVID-19 Activity
        Intent home = new Intent(getActivity(), MainActivity.class);
        startActivity(home);
    }
}