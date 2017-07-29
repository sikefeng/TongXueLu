package com.sikefeng.tongxuelu.diray;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by Administrator on 2016/6/27.
 */
public class LineEditText extends EditText {
    private Paint paint;
    public LineEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint=new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.BLUE);
        paint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //得到总行数
        int lintCount=getLineCount();
        //得到每行的高度
        int lineHeight=getLineHeight();
        //根据行数循环画下划线
        for (int i = 0; i <lintCount ; i++) {
            int lineY=(i+1)*lineHeight;
            canvas.drawLine(0,lineY,this.getWidth(),lineY,paint);
        }
    }
}
