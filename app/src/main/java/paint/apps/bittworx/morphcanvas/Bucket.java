package paint.apps.bittworx.morphcanvas;

import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by marcel.weissgerber on 09.11.2016.
 */

public class Bucket {

    private Path data = new Path();
    public boolean isNew = false;
    private Paint line;
    private Paint back;

    private RectF bounds = new RectF(0, 0, 0, 0);
    private boolean active = false;

    public Bucket(Paint line) {
        isNew = true;
        this.line = line;
    }

    public Bucket(Paint line,Path data){
        isNew=false;
        this.line=line;
        this.data=data;
        close();
    }

    public Bucket(Bucket bucket) {
        isNew = false;
        this.line = bucket.getLine();
        this.back = bucket.getBack();

        this.data = new Path(bucket.data);
        this.data.offset(100,100);
        close();
    }

    public Path getData() {
        return data;
    }

    public void close() {

        data.computeBounds(bounds, true);
    }

    public void add(int x, int y) {
        if (isNew) {
            data.moveTo(x, y);

        }
        isNew = false;
        data.lineTo(x, y);

    }


    public RectF getBounds() {
        return bounds;
    }

    public boolean contains(float x, float y) {
        if (bounds != null)
            return bounds.contains(x, y);
        return false;
    }


    public Paint getLine() {
        return line;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Paint getBack() {
        return back;
    }

    public void setBack(Paint back) {
        this.back = back;
    }
}
