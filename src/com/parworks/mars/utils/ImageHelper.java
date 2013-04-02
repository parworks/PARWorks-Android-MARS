/*******************************************************************************
 * Copyright 2013 PAR Works, Inc
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.parworks.mars.utils;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Shader.TileMode;

public class ImageHelper {
	
    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap
                .getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }
    
    public static Bitmap drawShadow(Bitmap bitmap, int leftRightThk, int bottomThk, int padTop) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        int newW = w - (leftRightThk * 2);
        int newH = h - (bottomThk + padTop);

        Bitmap.Config conf = Bitmap.Config.ARGB_8888;
        Bitmap bmp = Bitmap.createBitmap(w, h, conf);
        Bitmap sbmp = Bitmap.createScaledBitmap(bitmap, newW, newH, false);

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        Canvas c = new Canvas(bmp);

        // Left
        int leftMargin = (leftRightThk + 7)/2;
        Shader lshader = new LinearGradient(0, 0, leftMargin, 0, Color.TRANSPARENT, Color.BLACK, TileMode.CLAMP);
        paint.setShader(lshader);
        c.drawRect(0, padTop, leftMargin, newH, paint); 

        // Right
        Shader rshader = new LinearGradient(w - leftMargin, 0, w, 0, Color.BLACK, Color.TRANSPARENT, TileMode.CLAMP);
        paint.setShader(rshader);
        c.drawRect(newW, padTop, w, newH, paint);

        // Bottom
        Shader bshader = new LinearGradient(0, newH, 0, bitmap.getHeight(), Color.BLACK, Color.TRANSPARENT, TileMode.CLAMP);
        paint.setShader(bshader);
        c.drawRect(leftMargin -3, newH, newW + leftMargin + 3, bitmap.getHeight(), paint);
        c.drawBitmap(sbmp, leftRightThk, 0, null);

        return bmp;
    }
}
