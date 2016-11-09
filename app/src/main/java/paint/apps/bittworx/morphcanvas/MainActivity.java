package paint.apps.bittworx.morphcanvas;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
int old =-1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                update();
            }
        }, 50, 50);
    }


    public void flush(View view) {
        ((PainterView) findViewById(R.id.view)).flush();
        update();

    }

    public void corner(View view) {

        ((PainterView) findViewById(R.id.view)).corner();
        update();
        view.setBackgroundColor(((PainterView) findViewById(R.id.view)).corner ? Color.GREEN : Color.GRAY);
    }

    public void zoomIn(View view) {
        ((PainterView) findViewById(R.id.view)).zoomIn();

        update();

    }

    public void penUp(View view) {
        ((PainterView) findViewById(R.id.view)).pen(true);

        update();

    }

    public void penDown(View view) {
        ((PainterView) findViewById(R.id.view)).pen(false);

        update();

    }



    public void merge(View view) {
        ((PainterView) findViewById(R.id.view)).merge();

        update();

    }

    public void zoomOut(View view) {
        ((PainterView) findViewById(R.id.view)).zoomOut();
        update();

    }

    public void back(View view) {
        ((PainterView) findViewById(R.id.view)).back();
        update();

    }

    public void copy(View view) {
        ((PainterView) findViewById(R.id.view)).copy();
        update();

    }

    private void update() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                findViewById(R.id.view).invalidate();
            }
        });
    }
}
