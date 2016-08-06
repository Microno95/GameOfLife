package none.test;

import android.view.MotionEvent;

class MultiTouch {
    float motionX = 0, motionY = 0;
    float pmotionX = 0, pmotionY = 0;
    float size = 0, psize = 0;
    int id = 0;
    boolean touched = false;

    MultiTouch() {

    }

    void update(MotionEvent me, int index) {
        pmotionX = motionX;
        pmotionY = motionY;
        psize = size;

        motionX = me.getX(index);
        motionY = me.getY(index);
        size = me.getSize(index);

        id = me.getPointerId(index);
        touched = true;
    }

    void update() {
        pmotionX = motionX;
        pmotionY = motionY;
        psize = size;
        touched = false;
    }
}