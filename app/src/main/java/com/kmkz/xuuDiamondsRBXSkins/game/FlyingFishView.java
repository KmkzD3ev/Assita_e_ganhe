package com.kmkz.xuuDiamondsRBXSkins.game;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;

import com.kmkz.xuuDiamondsRBXSkins.R;
import com.kmkz.xuuDiamondsRBXSkins.activities.GameOverActivity;

public class FlyingFishView extends View {

    Bitmap[] fish = new Bitmap[2];
    int fishX = 10;
    int fishY;
    int fishSpeed;
    int canvasWidth, canvasHeight;
    int yellowX, yellowY, yellowSpeed = 18;
    Bitmap yellowBall;
    int greenX, greenY, greenSpeed = 25;
    Bitmap greenBall;
    int redX, redY, redSpeed = 33;
    Bitmap redBall;
    boolean touch = false;
    Bitmap backgroundImage;
    Paint scorePaint = new Paint();
    Bitmap[] life = new Bitmap[2];
    int score, lifeCountOfFish;
    Bitmap scaledYellowBall;
    Bitmap scaledGreenBall;
    Bitmap scaledRedBall;
    SoundPool soundPool;
    boolean loadedSound;
    int soundJump;

    public FlyingFishView(Context context) {
        super(context);

        fish[0] = BitmapFactory.decodeResource(getResources(), R.drawable.fish1);
        fish[1] = BitmapFactory.decodeResource(getResources(), R.drawable.fish2);

        backgroundImage = BitmapFactory.decodeResource(getResources(), R.drawable.bg_game);

        // Yellow Ball ---------------------
        yellowBall = BitmapFactory.decodeResource(getResources(), R.drawable.crypto_one);

        // Green Ball ---------------------
        greenBall = BitmapFactory.decodeResource(getResources(), R.drawable.crypto_two);

        // Red Ball ---------------------
        redBall = BitmapFactory.decodeResource(getResources(), R.drawable.bomb_icon);

        scaledYellowBall = Bitmap.createScaledBitmap(yellowBall, 125, 125, false);
        scaledGreenBall = Bitmap.createScaledBitmap(greenBall, 125, 125, false);
        scaledRedBall = Bitmap.createScaledBitmap(redBall, 125, 125, false);

        scorePaint.setColor(getResources().getColor(R.color.white));
        scorePaint.setTextSize(70);
        Typeface customFont = ResourcesCompat.getFont(context, R.font.font1);
        scorePaint.setTypeface(customFont);

        life[0] = BitmapFactory.decodeResource(getResources(), R.drawable.hearts);
        life[1] = BitmapFactory.decodeResource(getResources(), R.drawable.heart_grey);


        // Sound
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();
        SoundPool.Builder builder = new SoundPool.Builder();
        builder.setAudioAttributes(audioAttributes).setMaxStreams(5);
        this.soundPool = builder.build();
        this.soundPool.setOnLoadCompleteListener((soundPool, sampleId, status) -> loadedSound = true);
        soundJump = this.soundPool.load(context, R.raw.jump_sound, 1);

        fishY = 550;
        score = 0;
        lifeCountOfFish = 3;
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);

        canvasWidth = getWidth();
        canvasHeight = getHeight();

        canvas.drawBitmap(backgroundImage, 0, 0, null);

        int minFishY = fish[0].getHeight();
        int maxFishY = canvasHeight - fish[0].getHeight() * 3;
        fishY += fishSpeed;

        if (fishY < minFishY) {
            fishY = minFishY;
        }
        if (fishY > maxFishY) {
            fishY = maxFishY;
        }
        fishSpeed += 2;

        if (touch) {
            canvas.drawBitmap(fish[1], fishX, fishY, null);
            touch = false;
        } else {
            canvas.drawBitmap(fish[0], fishX, fishY, null);
        }

        // Yellow Ball ---------------------
        yellowX -= yellowSpeed;

        if (hitBallChecker(yellowX, yellowY)) {
            score += 1; // 1 points for each yellow ball
            yellowX = -100;
        }

        if (yellowX < 0) {
            yellowX = canvasWidth + 21;
            yellowY = (int) Math.floor(Math.random() * (maxFishY - minFishY)) + minFishY;
        }
        canvas.drawBitmap(scaledYellowBall, yellowX - (float) scaledYellowBall.getWidth() / 2, yellowY - (float) scaledYellowBall.getHeight() / 2, null);

        // ---------------------

        // Green Ball ---------------------
        greenX -= greenSpeed;

        if (hitBallChecker(greenX, greenY)) {
            score += 2; // 2 points for each green ball
            greenX = -100;
        }

        if (greenX < 0) {
            greenX = canvasWidth + 21;
            greenY = (int) Math.floor(Math.random() * (maxFishY - minFishY)) + minFishY;
        }

        canvas.drawBitmap(scaledGreenBall, greenX - (float) scaledGreenBall.getWidth() / 2, greenY - (float) scaledGreenBall.getHeight() / 2, null);
        // ---------------------

        // Red Ball ---------------------
        redX -= redSpeed;

        if (hitBallChecker(redX, redY)) {
            redX = -100;
            lifeCountOfFish--;

            if (lifeCountOfFish == 0) {
                // Game Over ---------------------
                @SuppressLint("DrawAllocation")
                Intent intent = new Intent(getContext(), GameOverActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.putExtra("score", score);
                getContext().startActivity(intent);
            }
        }

        if (redX < 0) {
            redX = canvasWidth + 21;
            redY = (int) Math.floor(Math.random() * (maxFishY - minFishY)) + minFishY;
        }

        canvas.drawBitmap(scaledRedBall, redX - (float) scaledRedBall.getWidth() / 2, redY - (float) scaledRedBall.getHeight() / 2, null);

        // ---------------------


        canvas.drawText("Score : " + score, 20, 60, scorePaint);

        for (int i = 0; i < 3; i++) {
            int x = (int) (580 + life[0].getWidth() * 1.5 * i);
            int y = 30;

            if (i < lifeCountOfFish) {
                canvas.drawBitmap(life[0], x, y, null);
            } else {
                canvas.drawBitmap(life[1], x, y, null);
            }
        }
    }

    public boolean hitBallChecker(int x, int y) {
        return fishX < x && x < (fishX + fish[0].getWidth()) && fishY < y && y < (fishY + fish[0].getHeight());
    }

    @SuppressLint("ClickableViewAccessibility")
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            touch = true;
            fishSpeed = -22;

            // jump sound
            if(loadedSound)
            {
                int steamId = this.soundPool.play(this.soundJump, (float) 0.3, (float) 0.3, 1, 0, 1f );
            }
        }
        return true;
    }
}
