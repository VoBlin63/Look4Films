package ru.buryachenko.hw_look4films.utils;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.PathShape;

import java.util.Random;


public class RandomPicture {

    public static Drawable make(int width, int height) {
        return make(2048, width, height);
    }

    private static Drawable make(int linesCount, int sizeX, int sizeY) {
        Path path = new Path();
        Random random = new Random();
        int destX = sizeX / 2;
        int destY = sizeY / 2;
        path.moveTo(destX, destY);
        for (int i = 0; i < linesCount; i++) {
            int distance = random.nextInt(27) + 7;
            int diffX = random.nextInt(distance) - distance / 2;
            int diffY = random.nextInt(distance) - distance / 2;
            destX = constraint(destX + diffX, 0, sizeX);
            destY = constraint(destY + diffY, 0, sizeY);
            path.lineTo(destX, destY);
            path.addCircle(destX, destY, random.nextInt(5) + 1, Path.Direction.CW);
        }
        ShapeDrawable drawable = new ShapeDrawable(new PathShape(path, sizeX, sizeY));
        drawable.setIntrinsicHeight(sizeY);
        drawable.setIntrinsicWidth(sizeX);
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
