package paint.apps.bittworx.morphcanvas;

import android.graphics.Paint;
import android.graphics.Path;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by marce on 09.11.2016.
 */

public class Layer {
    private List<Bucket> buckets = new ArrayList<>();
    private boolean active = false;
    private boolean visible = true;

    public List<Bucket> getBuckets() {
        return buckets;
    }


    public void clear() {
        buckets.clear();
    }

    public void merge(Paint line, boolean corner) {

        Path p = new Path();
        List<Bucket> r = new ArrayList<>();
        for (Bucket b : buckets)
        {
            if(corner && b.isActive())
            {
                p.addPath(b.getData());
                r.add(b);
            }else if(!corner){
                p.addPath(b.getData());
                r.add(b);
            }
        }

        buckets.removeAll(r);
        buckets.add(new Bucket(line, p));
    }

    public boolean isActive() {
        return active;
    }

    public Layer setActive(boolean active) {
        this.active = active;
        return this;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}
