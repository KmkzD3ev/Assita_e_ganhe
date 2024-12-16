package com.kmkz.xuuDiamondsRBXSkins.utility;

import android.content.Context;

import com.kmkz.xuuDiamondsRBXSkins.R;

import java.util.Random;

public class ScratchTools {

    public static String firstCodePart;

    static Random random = new Random();

    public static float dipToPx(Context context, float dipValue)
    {
        float density = context.getResources().getDisplayMetrics().density;
        return dipValue * density;
    }

    private static String generateCodePart(int min, int max)
    {
        int minNumber = min;
        int maxNumber = max;
        return String.valueOf((random.nextInt((maxNumber - minNumber) + 1) + minNumber));
    }
    public static String generateNewCode(Context context,int min, int max)
    {
        firstCodePart = generateCodePart(min, max);
        return context.getString(R.string.you_won) + firstCodePart + " " + context.getString(R.string.points);
    }

    public static int getPoint()
    {
        return Integer.parseInt(firstCodePart);
    }

}
