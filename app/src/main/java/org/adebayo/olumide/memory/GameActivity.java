package org.adebayo.olumide.memory;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/*
@author: olumide marc adebayo - 374994
Memory Card Game simulator
 */

public class GameActivity extends Activity {

    //2 rotations: 1 for default image to fruit, the 2nd animates in reverse
    Animation rotation,rotation2;

    //will hold the prviously selected imageview and associated drawable
    ImageView prevImageView = null;
    Drawable prevDrawable = null;

    //holds images
    private ArrayList<Drawable> drawables = new ArrayList<>();

    //this holds the image views that need to be reset back to
    //default image, its used by rotation2
    ImageView[] currentImageViews = null;

    //used by rotation to know what view and drawable
    //it should update after animating
    private Drawable globalDrawable =null;
    private ImageView globalImageView = null;

    //keeps track of cards drawn
    //this is set to the imageview's tag value
    int cardsDrawn=0;

    //this will be set to the total possible matches available
    //based on the game selected
    int totalMatches = 0;

    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        //load anim
        rotation = AnimationUtils.loadAnimation(GameActivity.this,R.anim.fruit_animate);
        rotation2 = AnimationUtils.loadAnimation(GameActivity.this,R.anim.fruit_animate2);
        rotation.setAnimationListener(animationListener);
        rotation2.setAnimationListener(animationListener2);


        spinner = findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {

                parent.setVisibility(View.INVISIBLE);

                switch (i){
                    case 0:
                        parent.setVisibility(View.VISIBLE);

                        break;
                    case 1:
                        resetContainer();
                        setDrawables(8);

                        //draw 8 cards
                        drawCards(4,0);
                        drawCards(4,1);
                        totalMatches=4;
                        break;
                    case 2:
                        resetContainer();
                        setDrawables(12);

                        //draw 12 cards
                        drawCards(4,0);
                        drawCards(4,1);
                        drawCards(4,2);
                        totalMatches=6;
                        break;
                    case 3:
                        resetContainer();
                        setDrawables(24);

                        //draw 24 cards
                        drawCards(5,0);
                        drawCards(5,1);
                        drawCards(5,2);
                        drawCards(5,3);
                        drawCards(4,4);
                        totalMatches=12;

                        break;
                    default:
                        //do nothing
                }
            }
        });

    }



    /*
    click listener for the cards, its attached in the drawCards() method
     */
    private View.OnClickListener cardClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            //remove click listener
            view.setOnClickListener(null);

            int index =    Integer.parseInt((String) view.getTag());


            Drawable _d = drawables.get(index);

            ImageView _iv = (ImageView) view;
            if(_iv != null ) {
                globalDrawable = _d;
                globalImageView = _iv;

                _iv.startAnimation(rotation);
            }


            if( prevImageView== null){
                prevImageView= _iv;
                prevDrawable = _d;
            }else{
                //do comparison
                if( prevDrawable.getConstantState().equals(_d.getConstantState())){
                    prevImageView.setAlpha(0.5f);
                    _iv.setAlpha(0.5f);
                    prevImageView=null;
                    prevDrawable=null;
                    totalMatches--;//keep track

                    if( totalMatches < 1){//all possible matches found???
                        spinner.setVisibility(View.VISIBLE);
                        createToast(getResources().getString(R.string.game_over));
                    }
                }else{

                    _iv.clearAnimation();
                    _iv.setImageDrawable(_d);


                    currentImageViews = new ImageView[2];
                    currentImageViews[0] = prevImageView;
                    currentImageViews[1] = _iv;

                    _iv.startAnimation(rotation2);

                    //add listener back
                    prevImageView.setOnClickListener(cardClickListener);


                    _iv.setOnClickListener(cardClickListener);


                    prevImageView=null;
                    prevDrawable=null;
                }

            }

        }
    };

    /*
    this will draw the cards with the default image.
    input : count (how many cards)
    row : row number (no longer used really.  I had this when I was trying with gridlayout
     */
    private void drawCards(int count, int row){

        //grab the container
        LinearLayout container = findViewById(R.id.card_container);

        LinearLayout layout = new LinearLayout(this);
        container.addView(layout);
        container.setWeightSum((row+1));

        layout.setOrientation(LinearLayout.HORIZONTAL);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT,1.0f);
        params.weight=1.0f;

        layout.setLayoutParams(params);


        for(int i=0;i<count;i++){
            //create new ImageView
            ImageView _iv = new ImageView(GameActivity.this);

            //set the index for this image view
            _iv.setTag((cardsDrawn++)+"");

            //set click listener
            _iv.setOnClickListener(cardClickListener);

            layout.addView(_iv);
            //set image
            _iv.setImageResource(R.drawable.fruits_front);

            //get layout params from parent
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) layout.getLayoutParams();

            _iv.setLayoutParams(layoutParams);

        }
    }

    private void resetContainer(){
        //grab the container
        LinearLayout container = findViewById(R.id.card_container);

        //reset a bunch of global vars
        prevImageView=null;
        prevDrawable=null;
        globalImageView=null;
        globalDrawable=null;
        drawables.clear();
        container.removeAllViews();
        totalMatches=0;
        cardsDrawn=0;
    }

    Animation.AnimationListener animationListener = new Animation.AnimationListener(){
        @Override
        public void onAnimationStart(Animation animation) {}

        @Override
        public void onAnimationEnd(Animation animation) {
            //TODO: check for nullity
            globalImageView.setImageDrawable(globalDrawable);
        }
        @Override
        public void onAnimationRepeat(Animation animation) {        }
    };


    Animation.AnimationListener animationListener2 = new Animation.AnimationListener(){
        @Override
        public void onAnimationStart(Animation animation) {}

        @Override
        public void onAnimationEnd(Animation animation) {
            currentImageViews[0].setImageResource(R.drawable.fruits_front);
            currentImageViews[1].setImageResource(R.drawable.fruits_front);
        }
        @Override
        public void onAnimationRepeat(Animation animation) {        }
    };


    /*
    this loads the images into an arraylist given a number of images
    e.g if you pass in 8, it will load 4 distinct fruits but 2 of each.
     */

    private void setDrawables(int total){

        Field[] drawablesFields = org.adebayo.olumide.memory.R.drawable.class.getFields();

        int count = total/2;

        for (Field field : drawablesFields) {

            if( count <1){
                break;
            }

            try {

                //we want only fruit images
                if( !field.getName().startsWith("fruit_")){
                    continue;
                }

                Drawable _drawable = getResources().getDrawable(field.getInt(null),null);

                drawables.add(_drawable);
                drawables.add(_drawable);

                count--;

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //shuffle the list a bit :)
        if(drawables != null && drawables.size()>2){
            Collections.shuffle(drawables, new Random((long) drawables.size()));
        }
    }

    /*
    just a convenience method for toasting
     */
    private void createToast(String str){
        Toast toast = Toast.makeText(GameActivity.this,str,Toast.LENGTH_LONG);
        int xOff = 0;
        int yOff = 0;

        toast.setGravity(Gravity.CENTER,xOff,yOff);
        toast.show();

    }
}
