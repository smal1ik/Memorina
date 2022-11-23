package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    ImageView first, second;
    int cardsOpen = 0;
    int[][] faces = new int[4][4];
    ImageView[] v;

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        v = new ImageView[]{findViewById(R.id.i00),findViewById(R.id.i01),findViewById(R.id.i02),findViewById(R.id.i03),
                findViewById(R.id.i10),findViewById(R.id.i11),findViewById(R.id.i12),findViewById(R.id.i13),
                findViewById(R.id.i20),findViewById(R.id.i21),findViewById(R.id.i22),findViewById(R.id.i23),
                findViewById(R.id.i30),findViewById(R.id.i31),findViewById(R.id.i32),findViewById(R.id.i33)};
        newGame(v[0]);
    }

    public void newGame(View view){

        for(int i = 0; i < 16; i++){
            faces[i/4][i%4] = -1;
            v[i].setVisibility(View.VISIBLE);
            v[i].setImageDrawable(getResources().getDrawable(R.drawable.back));
        }

        int[] cards = new int[]{R.drawable.zeus,R.drawable.aparat,R.drawable.invoker,R.drawable.juger,
                R.drawable.pudge,R.drawable.sky,R.drawable.tusk,R.drawable.wd};

        Random rand = new Random();

        int i = 0;
        while (i < 8){
            int f = rand.nextInt(16);
            int s = rand.nextInt(16);
            if(f != s && faces[f/4][f%4] == -1 && faces[s/4][s%4] == -1){
                faces[f/4][f%4] = cards[i];
                faces[s/4][s%4] = cards[i];
                i++;
            }
        }

    }

    public void onFlip(View view){

        String tag = view.getTag().toString();
        int x = Integer.parseInt(tag.substring(0, 1));
        int y = Integer.parseInt(tag.substring(1,2));

        switch (cardsOpen){
            case 0:
                cardsOpen += 1;
                first = (ImageView) view;
                first.setImageDrawable(getResources().getDrawable(faces[x][y]));
                break;
            case 1:
                if(view != first){
                    second = (ImageView) view;
                    cardsOpen += 1;
                    second.setImageDrawable(getResources().getDrawable(faces[x][y]));
                    FlipTask flipTask = new FlipTask(first, second,this);
                    flipTask.execute(1);
                }
                break;
        }


    }


    public class FlipTask extends AsyncTask<Integer, Void, Void>{
        ImageView first, second;
        Context context;

        public FlipTask(ImageView first, ImageView second, Context context){
            super();
            this.first = first;
            this.second = second;
            this.context = context;
        }

        protected Void  doInBackground(Integer... integers) {
            int seconds = integers[0] * 1000;

            try {
                Thread.sleep(seconds);
            } catch (InterruptedException e) {}

            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);

            String f = first.getTag().toString();
            String s = second.getTag().toString();

            int fx = Integer.parseInt(f.substring(0, 1));
            int fy = Integer.parseInt(f.substring(1,2));
            int sx = Integer.parseInt(s.substring(0, 1));
            int sy = Integer.parseInt(s.substring(1,2));

            if(faces[fx][fy] == faces[sx][sy]){
                faces[fx][fy] = -1;
                faces[sx][sy] = -1;
                first.setVisibility(View.INVISIBLE);
                second.setVisibility(View.INVISIBLE);
            }else{
                first.setImageDrawable(getResources().getDrawable(R.drawable.back));
                second.setImageDrawable(getResources().getDrawable(R.drawable.back));
            }

            boolean check = true;
            for (int i = 0; i < 16; i++){
                if(faces[i/4][i%4] != -1) {
                    check = false;
                    break;
                }
            }

            if (check){
                Toast.makeText(context, "Победа!", Toast.LENGTH_LONG).show();
            }

            cardsOpen = 0;
        }

    }

}