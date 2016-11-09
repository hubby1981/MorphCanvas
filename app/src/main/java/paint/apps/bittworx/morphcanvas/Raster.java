package paint.apps.bittworx.morphcanvas;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Created by marce on 09.11.2016.
 */

public class Raster {

    public int size = 32;

    public Raster(float zoom){
        size/=zoom;
    }

    public void onDraw(Canvas canvas) {
        Paint line = new Paint();
        line.setStrokeWidth(2);
        line.setColor(Color.argb(25, 200, 50, 50));
        line.setStyle(Paint.Style.STROKE);
        int w = canvas.getClipBounds().width() / size;
        int h = canvas.getClipBounds().height() / w;
        int xx = 0;
        int yy = 0;
        for (int y = 0; y <= h; y++) {
            for (int x = 0; x <= size; x++) {
                canvas.drawRect(new Rect(xx,yy,xx+w,yy+w),line);
                xx+=w;
            }
            xx=0;
            yy+=w;
        }

    }
}
