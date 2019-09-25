package ru.buryachenko.hw_look4films.utils;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.PathShape;

import java.util.Random;


public class RandomPicture {

    public static Drawable make() {
        return make(200, (int) (1024 + Math.round(Math.random()*1024)));
    }

    private static Drawable make(int size, int linesCount) {
        Path path = new Path();
        Random random = new Random();
        int destX = size/2;
        int destY = size/2;
        path.moveTo(destX, destY);
        for (int i = 0; i < linesCount + 64; i++) {
            int distance = random.nextInt(size/2)+1;
            destX = constraint(destX + distance/2 - random.nextInt(distance), 0 , size);
            destY = constraint(destY + distance/2 - random.nextInt(distance), 0 , size);
            path.lineTo(destX,destY);
            path.addCircle(destX,destY,random.nextInt(5)+1, Path.Direction.CW);
        }
        for (int i = 0; i < linesCount/4; i++) {
            path.lineTo(random.nextInt(size),random.nextInt(size));
        }
        ShapeDrawable drawable = new ShapeDrawable(new PathShape(path, size, size));
        drawable.setIntrinsicHeight(size);
        drawable.setIntrinsicWidth(size);
        drawable.getPaint().setColor(Color.BLACK);
        drawable.getPaint().setStyle(Paint.Style.STROKE);
        return drawable;
    }

    private static int constraint(int val, int min, int max) {
        if (val < min)
            val = min;
        if (val > max)
            val = max;
        return val;
    }
}
