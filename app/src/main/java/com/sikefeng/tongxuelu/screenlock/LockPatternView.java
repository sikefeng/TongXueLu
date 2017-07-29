package com.sikefeng.tongxuelu.screenlock;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.sikefeng.tongxuelu.R;

import java.util.ArrayList;
import java.util.List;

public class LockPatternView extends View {

    private Matrix matrix = new Matrix();
    private static final int POINT_SIZE = 5;
    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Point[][] points = new Point[3][3];
    private List<Point> pointList = new ArrayList<Point>();

    private boolean isInit, isSelect, isFinish, moveNoPoint;
    private float width, height, offsetX, offsetY, bitmapR, moveX, moveY;
    private Bitmap pointNormal, pointPressed, pointError, linePressed, lineError;

    public LockPatternView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    public LockPatternView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }

    public LockPatternView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // TODO Auto-generated constructor stub
    }


    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (!isInit) {
            initPoints();
        }
        //����
        points2Canvas(canvas);
        //����
        if (pointList.size() > 0) {
            Point a = pointList.get(0);
            for (int i = 0; i < pointList.size(); i++) {
                Point b = pointList.get(i);
                line2Canvas(canvas, a, b);
                a = b;
            }
            //������������
            if (moveNoPoint) {
                line2Canvas(canvas, a, new Point(moveX, moveY));
            }

        }
    }

    //��������
    private void line2Canvas(Canvas canvas, Point a, Point b) {
        //�ߵĳ���
        float lineLength = (float) Point.distance(a, b);
        float degress = getDegress(a, b);
        canvas.rotate(degress, a.x, a.y);
        if (a.state == Point.STATE_PRESSED) {
            matrix.setScale(lineLength / linePressed.getWidth(), 1);
            matrix.postTranslate(a.x - linePressed.getWidth()/2 , a.y - linePressed.getHeight()/2 );
            canvas.drawBitmap(linePressed, matrix, paint);
        } else {
            matrix.setScale(lineLength / lineError.getWidth(), 1);
            matrix.postTranslate(a.x - lineError.getWidth()/2 , a.y - lineError.getHeight()/2);
            canvas.drawBitmap(lineError, matrix, paint);
        }
        canvas.rotate(-degress, a.x, a.y);
    }

    //��ʼ����
    private void initPoints() {
        // TODO Auto-generated method stub
        //��ȡ���ֵĿ��
        width = getWidth();
        height = getHeight();

        if (width > height) {//����
            offsetX = (width - height) / 2;
            width = height;
        } else {//����
            offsetY = (height - width) / 2;
            height = width;
        }

        //ͼƬ��Դ
//        pointNormal = BitmapFactory.decodeResource(getResources(), R.drawable.point_normal);
//        pointPressed = BitmapFactory.decodeResource(getResources(), R.mipmap.point_press);
//        pointError = BitmapFactory.decodeResource(getResources(), R.mipmap.point_error);
        pointNormal = BitmapFactory.decodeResource(getResources(), R.mipmap.locus_round_original);
        pointPressed = BitmapFactory.decodeResource(getResources(), R.mipmap.locus_round_click1);
        pointError = BitmapFactory.decodeResource(getResources(), R.mipmap.locus_round_click_error);
        linePressed = BitmapFactory.decodeResource(getResources(), R.drawable.line_pressed);
        lineError = BitmapFactory.decodeResource(getResources(), R.drawable.line_error);

        //�������
        points[0][0] = new Point(offsetX + width / 4, offsetY + width / 4);
        points[0][1] = new Point(offsetX + width / 2, offsetY + width / 4);
        points[0][2] = new Point(offsetX + width - width / 4, offsetY + width / 4);

        points[1][0] = new Point(offsetX + width / 4, offsetY + width / 2);
        points[1][1] = new Point(offsetX + width / 2, offsetY + width / 2);
        points[1][2] = new Point(offsetX + width - width / 4, offsetY + width / 2);

        points[2][0] = new Point(offsetX + width / 4, offsetY + width - width / 4);
        points[2][1] = new Point(offsetX + width / 2, offsetY + width - width / 4);
        points[2][2] = new Point(offsetX + width - width / 4, offsetY + width - width / 4);

        //ͼƬ��Դ�İ뾶
        bitmapR = pointPressed.getHeight() / 2;
        //��������
        int index = 1;
        for (Point[] points : this.points) {
            for (Point point : points) {
                point.index = index;
                index++;
            }
        }

        //��ʼ�����
        isInit = true;

    }

    //������Ƶ�������
    private void points2Canvas(Canvas canvas) {
        // TODO Auto-generated method stub

        for (int i = 0; i < points.length; i++) {
            for (int j = 0; j < points[i].length; j++) {
                Point point = points[i][j];
                if (point.state == Point.STATE_PRESSED) {
                    canvas.drawBitmap(pointPressed, point.x - bitmapR, point.y - bitmapR, paint);
                } else if (point.state == Point.STATE_ERROR) {
                    canvas.drawBitmap(pointError, point.x - bitmapR, point.y - bitmapR, paint);
                } else {
                    canvas.drawBitmap(pointNormal, point.x - bitmapR, point.y - bitmapR, paint);
                }

            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub
        moveNoPoint = false;
        isFinish = false;
        moveX = event.getX();
        moveY = event.getY();
        Point point = null;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //���»���
                if (onPatterChangeLister != null) {
                    onPatterChangeLister.onPatterStart(true);
                }
                resetPoint();
                point = checkSelectPoint();
                if (point != null) {
                    isSelect = true;
                }
                break;

            case MotionEvent.ACTION_MOVE:

                if (isSelect) {
                    point = checkSelectPoint();
                    if (point == null) {
                        moveNoPoint = true;
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                isFinish = true;
                isSelect = false;

                break;
        }
        //ѡ���ظ����
        if (!isFinish && isSelect && point != null) {
            //�����
            if (crossPoint(point)) {
                moveNoPoint = true;
            } else {//�µ�
                point.state = Point.STATE_PRESSED;
                pointList.add(point);
            }
        }
        //���ƽ���
        if (isFinish) {
            //���Ʋ�����
            if (pointList.size() == 1) {


                //���ƴ���resetPoint();
            } else if (pointList.size() < POINT_SIZE && pointList.size() > 0) {
                errorPoint();
                if (onPatterChangeLister != null) {
                    String password = "";
                    for (int i = 0; i < pointList.size(); i++) {
                        password = password + pointList.get(i).index;
                    }
                    onPatterChangeLister.onPatterChangeListener(null);
                }
            } else {//���Ƴɹ�
                if (onPatterChangeLister != null) {
                    String password = "";
                    for (int i = 0; i < pointList.size(); i++) {
                        password = password + pointList.get(i).index;
                    }
                    if (!TextUtils.isEmpty(password)) {
                        onPatterChangeLister.onPatterChangeListener(password);
                    }

                }

            }

        }
        //ˢ����ͼ
        postInvalidate();
        return true;
    }

    //�����ļ��
    private boolean crossPoint(Point point) {
        if (pointList.contains(point)) {
            return true;
        } else {
            return false;
        }

    }

    public void resetPoint() {
        for (int i = 0; i < pointList.size(); i++) {
            Point point = pointList.get(i);
            point.state = Point.STATE_NORMAL;
        }
        pointList.clear();
        if (onPatterChangeLister != null) {
            onPatterChangeLister.onReSetPoint(true);
        }
    }

    //���ƴ���
    public void errorPoint() {
        for (Point point : pointList) {
            point.state = Point.STATE_ERROR;
        }
    }

    private Point checkSelectPoint() {
        for (int i = 0; i < points.length; i++) {
            for (int j = 0; j < points[i].length; j++) {
                Point point = points[i][j];
                if (Point.with(point.x, point.y, bitmapR, moveX, moveY)) {
                    return point;
                }
            }
        }

        return null;
    }

    public float getDegress(Point a, Point b) {
        float ax = a.x;
        float ay = a.y;
        float bx = b.x;
        float by = b.y;
        float degress = 0;
        if (bx == ax) {
            if (by > ay) {
                degress = 90;
            } else if (by < ay) {
                degress = 270;
            }
        } else if (by == ay) {
            if (bx > ax) {
                degress = 0;
            } else if (bx < ax) {
                degress = 180;
            }
        } else {
            degress = (float) Math.toDegrees(Math.atan2(b.y - a.y, b.x - a.x));
        }
        return degress;

    }

    //�Զ����
    public static class Point {
        public static int STATE_NORMAL = 0;//����״̬
        public static int STATE_PRESSED = 1;//ѡ��״̬
        public static int STATE_ERROR = 2;//����״̬

        public float x, y;
        public int index = 0, state = 0;

        public Point() {
        }

        public Point(float x, float y) {
            this.x = x;
            this.y = y;
        }

        //����֮��ľ���
        public static double distance(Point a, Point b) {
            return Math.sqrt(Math.abs(a.x - b.x) * Math.abs(a.x - b.x) + Math.abs(a.y - b.y) * Math.abs(a.y - b.y));
        }

        //�Ƿ��غ�
        public static boolean with(float pointX, float pointY, float r, float moveX, float moveY) {

            return Math.sqrt((pointX - moveX) * (pointX - moveX) + (pointY - moveY) * (pointY - moveY)) < r;//����
        }
    }

    //���ü�����
    private OnPatterChangeLister onPatterChangeLister;

    public static interface OnPatterChangeLister {
        void onPatterChangeListener(String password);

        void onPatterStart(boolean isStart);

        void onReSetPoint(boolean isRet);
    }

    public void setOnPatterChangeListener(OnPatterChangeLister onPatterChangeLister) {
        if (onPatterChangeLister != null) {
            this.onPatterChangeLister = onPatterChangeLister;
        }
    }

}
