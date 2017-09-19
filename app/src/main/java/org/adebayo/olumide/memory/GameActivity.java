package org.adebayo.olumide.memory;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Random;

import static android.R.interpolator.linear;
import static java.util.logging.Logger.global;

public class GameActivity extends Activity {

    Animation rotation,rotation2;
    boolean isAnimating = false;
    boolean isAnimating2 = false;
    ImageView prevImageView = null;
    Drawable prevDrawable = null;
    private ArrayList<Drawable> drawables = new ArrayList<>();
    ImageView[] currentImageViews = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        //load anim
        rotation = AnimationUtils.loadAnimation(GameActivity.this,R.anim.fruit_animate);
        rotation2 = AnimationUtils.loadAnimation(GameActivity.this,R.anim.fruit_animate2);
        rotation.setAnimationListener(animationListener);
        rotation2.setAnimationListener(animationListener2);

        //grab all images
        //setDrawables();

        Spinner spinner = findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
Log.d("D",i+"");
            //    if( 1== 1) return;
                //hide spinner
               // parent.setVisibility(View.INVISIBLE);
                switch (i){
                    case 0:
                        setDrawables(8);

                        resetContainer();
                        //draw 8 cards
                        drawCards(4,0);
                        drawCards(4,1);
                        break;
                    case 1:
                        setDrawables(12);

                        resetContainer();
                        //draw 12 cards
                        drawCards(4,0);
                        drawCards(4,1);
                        drawCards(4,2);
                        break;
                    case 2:
                        setDrawables(24);

                        resetContainer();
                        //draw 24 cards
                        drawCards(5,0);
                        drawCards(5,1);
                        drawCards(5,2);
                        drawCards(5,3);
                        drawCards(4,4);

                        break;
                    default:
                        //do nothing
                }
            }
        });

    }


    private             Random r = new Random();
    private Drawable globalDrawable =null;
    private ImageView globalImageView = null;
    private ImageView globalImageView2 = null;

    private View.OnClickListener cardClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //remove click listener
            view.setOnClickListener(null);


            //replace with fruit image
            int min = 0;
            int max = drawables.size();
           int index = r.nextInt(max - min ) + min;

            Log.d("TAG ",index+" "+max);

            Drawable _d = drawables.remove(index);

            Log.d("TTAG",_d.toString());

            ImageView _iv = (ImageView) view;
            if(_iv != null ) {
                globalDrawable = _d;
                globalImageView = _iv;

                _iv.startAnimation(rotation);
            }
            Log.d("MM",isAnimating+" ");

            while( isAnimating){}

            Log.d("MM",isAnimating+" ");

            if( prevImageView== null){
                prevImageView= _iv;
                prevDrawable = _d;
            }else{
                //do comparison

                if( prevDrawable.getConstantState().equals(_d.getConstantState())){
                    Log.d("YEZZZZ","match found");
                    prevImageView.setAlpha(0.5f);
                    _iv.setAlpha(0.5f);
                    prevImageView=null;
                    prevDrawable=null;
                }else{
                    drawables.add(prevDrawable);//add back
                    //revert image
                    //globalImageView2=prevImageView;

                    _iv.clearAnimation();
                    _iv.setImageDrawable(_d);


                    currentImageViews = new ImageView[2];
                    currentImageViews[0] = prevImageView;
                    currentImageViews[1] = _iv;

                    _iv.startAnimation(rotation2);

                    Log.d("--MM",isAnimating+" ");

                   // while(globalImageView2 != null){}
                    while(isAnimating2){}

                    Log.d("--MM",isAnimating+" ");

                    //prevImageView.setImageResource(R.drawable.fruits_front);
                    //add listener back
                    prevImageView.setOnClickListener(cardClickListener);

                    //return currently clicked imageview back to its prev state
                    drawables.add(_d);//put it back

                    globalImageView2 = _iv;

                    //_iv.startAnimation(rotation2);

                    Log.d("-MM",isAnimating+" ");

                    while(isAnimating2){}

                    Log.d("-MM",isAnimating+" ");

                    _iv.setOnClickListener(cardClickListener);
                    //_iv.setImageResource(R.drawable.fruits_front);





                    prevImageView=null;
                    prevDrawable=null;
                    //prevImageView= _iv;
                    //prevDrawable = _d;
                }

            }

        }
    };

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

            //set click listener
            _iv.setOnClickListener(cardClickListener);

            layout.addView(_iv);
            //set image
            _iv.setImageResource(R.drawable.fruits_front);

           // LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0,0,1f);
            //get layout params from parent
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) layout.getLayoutParams();

            _iv.setLayoutParams(layoutParams);

            Log.d("d","row "+row +" column "+i);
        }
    }

    private void resetContainer(){
        //grab the container
        LinearLayout container = findViewById(R.id.card_container);
        prevImageView=null;
        prevDrawable=null;
        globalImageView2=null;
        globalImageView=null;
        globalDrawable=null;
        isAnimating=false;
        container.removeAllViews();
    }

    Animation.AnimationListener animationListener = new Animation.AnimationListener(){
        @Override
        public void onAnimationStart(Animation animation) {
            isAnimating=true;

        }

        @Override
        public void onAnimationEnd(Animation animation) {
            Log.d("KK1",isAnimating+"");

            //TODO: check for nullity
            globalImageView.setImageDrawable(globalDrawable);
            isAnimating=false;
        }

        @Override
        public void onAnimationRepeat(Animation animation) {        }
    };


    Animation.AnimationListener animationListener2 = new Animation.AnimationListener(){
        @Override
        public void onAnimationStart(Animation animation) {
            //isAnimating2=true;

        }

        @Override
        public void onAnimationEnd(Animation animation) {
  //          Log.d("KK2",isAnimating2+"");
/*
            //TODO: check for nullity
            globalImageView2.setImageResource(R.drawable.fruits_front);
            isAnimating2=false;
            globalImageView2=null;
          */
            currentImageViews[0].setImageResource(R.drawable.fruits_front);
            currentImageViews[1].setImageResource(R.drawable.fruits_front);
        }

        @Override
        public void onAnimationRepeat(Animation animation) {        }
    };



    private void setDrawables(int total){
      //  private ArrayList<Drawable> _drawables = new ArrayList<>();


        Field[] drawablesFields = org.adebayo.olumide.memory.R.drawable.class.getFields();

        int count = total/2;

        for (Field field : drawablesFields) {

            if( count <1){
                break;
            }

            try {
                Log.i("LOG_TAG", "R.drawable." + field.getName());

                //we want only fruit images
                if( !field.getName().startsWith("fruit_")){
                    continue;
                }

                Drawable _drawable = getResources().getDrawable(field.getInt(null),null);

                if(_drawable != null) {
                    Log.d("TTT", _drawable.toString());
                }else{
                    Log.d("TTT","drawable not loaded");
                }

                drawables.add(_drawable);
                drawables.add(_drawable);

                count--;

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Log.d("CCC", "total images loaded "+drawables.size());
        for(Drawable dd:drawables){
            Log.d("MMM",dd.getConstantState().toString());
        }

    }
}
