package paint.apps.bittworx.morphcanvas;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.security.interfaces.DSAKey;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by marcel.weissgerber on 09.11.2016.
 */

public class PainterView extends View {

    private Bucket active = null;
    private List<Layer> layers = new ArrayList<>();
    private Paint back = new Paint();
    private Paint line = new Paint();
    private Paint bound = new Paint();
    private Paint actives = new Paint();


    private Point oldMover;
    private Point newMover;

    private Bucket mover = null;

    private float zoom = 1.0f;
    public boolean corner = false;
    private int pen=8;

    public boolean raster = true;

    public Layer getActiveLayer() {
        for (Layer l : layers)
            if (l.isActive())
                return l;
        return new Layer().setActive(true);
    }

    public PainterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (layers.size() == 0)
            layers.add(new Layer().setActive(true));
        back.setStyle(Paint.Style.FILL);
        back.setColor(Color.WHITE);

        line.setStyle(Paint.Style.STROKE);
        line.setStrokeWidth(20);
        line.setColor(Color.DKGRAY);
        line.setStrokeCap(Paint.Cap.ROUND);

        bound.setStyle(Paint.Style.STROKE);
        bound.setStrokeWidth(8);
        bound.setPathEffect(new DashPathEffect(new float[]{16f, 16f}, 16f));
        bound.setColor(Color.GREEN);


        actives.setStyle(Paint.Style.STROKE);
        actives.setStrokeWidth(8);
        actives.setPathEffect(new DashPathEffect(new float[]{16f, 16f}, 16f));
        actives.setColor(Color.BLUE);
    }

    @Override
    public void onDraw(Canvas canvas) {

        canvas.scale(zoom, zoom);




        if (raster) {
            new Raster(zoom).onDraw(canvas);
        }

        for (Layer l : layers)
            if (l.isVisible())
                for (Bucket n : l.getBuckets()) {
                    if (n != null && n.getData() != null) {
                        if (n.getBack() != null)
                            canvas.drawPath(n.getData(), n.getBack());

                        canvas.drawPath(n.getData(), n.getLine());
                        if (n.getBounds() != null && (corner || (mover != null && mover == n))) {
                            canvas.drawRect(n.getBounds(), bound);
                        }

                        if (n.getBounds() != null && n.isActive() && corner) {
                            canvas.drawRect(n.getBounds(), actives);

                        }
                    }
                }


    }

    public void flush() {
        if (corner) {
            getActiveLayer().clear();
            active = null;
            mover = null;
            corner = false;
        } else {
            if (getActiveLayer().getBuckets().size() > 0) {
                getActiveLayer().getBuckets().remove(0);
            }
        }
    }

    public void corner() {
        corner = !corner;
    }


    public void pen(boolean up){
        if(up)
            pen++;
        else
            pen--;

        if(pen<1)pen=1;
        if(pen>50)pen=50;


        Paint l = new Paint();
        l.setStyle(line.getStyle());
        l.setColor(line.getColor());
        l.setStrokeWidth(pen/zoom);
        line=l;
    }

    public void zoomIn() {
        if (zoom >= 4)
            return;
        if (!corner) {
            zoom += 0.1f;
        } else {
            for (Layer l : layers)
                for (Bucket b : l.getBuckets()) {
                    if (b.isActive()) {
                        Matrix m = new Matrix();
                        m.setScale(1.05f, 1.05f);
                        b.getData().transform(m);
                        b.close();
                    }
                }
        }
    }

    public void zoomOut() {
        if (zoom <= 0.1f)
            return;
        if (!corner) {
            zoom -= 0.1f;
        } else {
            for (Layer l : layers)
                for (Bucket b : l.getBuckets()) {
                    if (b.isActive()) {
                        Matrix m = new Matrix();
                        m.setScale(0.95f, 0.95f);
                        b.getData().transform(m);
                        b.close();
                    }
                }
        }
    }

    public void back() {
        Paint backs = new Paint();
        backs.setStyle(Paint.Style.FILL);
        backs.setColor(Color.argb(128, 100, 100, 150));
        for (Bucket b : getActiveLayer().getBuckets()) {
            if (b.isActive()) {
                b.setBack(b.getBack() != null ? null : backs);
            }
        }


    }

    public void merge() {
        getActiveLayer().merge(line, corner);
    }

    public void copy() {
        List<Bucket> all2 = new ArrayList<>();
        for (Bucket b : getActiveLayer().getBuckets()) {
            if (b.isActive()) {
                all2.add(new Bucket(b));
            }
        }

        getActiveLayer().getBuckets().addAll(all2);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {


        float xx = event.getX() / zoom;
        float yy = event.getY() / zoom;

        if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE) {

            if (active == null && mover == null && corner) {

                for (Layer l : layers)
                    for (Bucket b : l.getBuckets()) {
                        if (b.getBounds() != null) {
                            if (b.contains(xx, yy)) {
                                mover = b;
                                if (corner)
                                    b.setActive(!b.isActive());
                                oldMover = new Point((int) (xx), (int) (yy));
                                break;
                            }
                        }
                    }
            }

            if (mover == null && !corner) {
                if (active == null) {
                    active = new Bucket(line);
                    getActiveLayer().getBuckets().add(0, active);

                }
                active.add((int) xx, (int) yy);
            } else {
                if (mover != null && corner) {
                    newMover = new Point((int) xx, (int) yy);
                    int x = newMover.x - oldMover.x;
                    int y = newMover.y - oldMover.y;

                    oldMover = new Point((int) xx, (int) yy);
                    mover.getData().offset(x, y);

                    mover.close();
                }
            }

        }
        if (event.getAction() == MotionEvent.ACTION_UP) {

            newMover = new Point((int) xx, (int) yy);
            if (active != null) {
                active.close();
                active = null;
            }


            mover = active;
        }
        return true;
    }
}
