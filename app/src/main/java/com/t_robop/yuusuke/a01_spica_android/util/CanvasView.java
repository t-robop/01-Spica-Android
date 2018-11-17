package com.t_robop.yuusuke.a01_spica_android.util;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.t_robop.yuusuke.a01_spica_android.R;
import com.t_robop.yuusuke.a01_spica_android.UI.Script.ScriptMainActivity;
import com.t_robop.yuusuke.a01_spica_android.UI.Script.ScriptMainAdapter;
import com.t_robop.yuusuke.a01_spica_android.model.ScriptModel;

import java.util.ArrayList;

public class CanvasView extends View {

    private final Paint paint;

    private int lineWeight = 5;

    private float nowPositionX = 0;
    float y = 0;

    //canvasのサイズ
    private float canvasWidth;
    private float canvasHeight;

    //四角を描画する部分
    private float windowWidth;
    private float windowHeight;

    //**命令ブロックが沢山並んでる場所について**//
    //画面に表示できる長さ
    private float mainViewWindowWidth = 300;
    //スクロール可能な部分を含めた長さ
    private float mainViewMaxWidth = 600;

    private int LineNoMargin = lineWeight / 2;

    ///////
    private int commandBlockNum = 2;
    private int commandBlockMargin = 50;
    private int commandBlockSize = 20;
    private int commandBlockLineWidth = 5;
    ///////
    Paint commandBlockPaint;
    Paint commandBlockLinePaint;
    private float commandBlockLineLength;
    ScriptMainAdapter commandBlocks;

    public CanvasView(Context context) {
        super(context);

        //- Paint関連
        paint = new Paint();
        paint.setColor(Color.RED);          // 色の指定
        paint.setStyle(Paint.Style.STROKE); // 描画設定を'線'に設定
        paint.setAntiAlias(true);           // アンチエイリアスの適応
        paint.setStrokeWidth(lineWeight);           // 線の太さ
        commandBlockPaint = new Paint();
        commandBlockPaint.setColor(Color.BLACK);
        commandBlockPaint.setStyle(Paint.Style.STROKE); // 描画設定を'線'に設定
        commandBlockPaint.setAntiAlias(true);
        commandBlockPaint.setStrokeWidth(commandBlockSize);           // 線の太さ
        commandBlockLinePaint = new Paint();
        commandBlockLinePaint.setColor(Color.BLACK);
        commandBlockLinePaint.setStyle(Paint.Style.STROKE); // 描画設定を'線'に設定
        commandBlockLinePaint.setAntiAlias(true);
        commandBlockLinePaint.setStrokeWidth(commandBlockLineWidth);           // 線の太さ


    }

    public CanvasView(Context context, AttributeSet attrs) {
        super(context, attrs);

        //-- 初期化
        //- Path関連

        //- Paint関連
        paint = new Paint();
        paint.setColor(Color.RED);          // 色の指定
        paint.setStyle(Paint.Style.STROKE); // 描画設定を'線'に設定
        paint.setAntiAlias(true);           // アンチエイリアスの適応
        paint.setStrokeWidth(lineWeight);           // 線の太さ
        commandBlockPaint = new Paint();
        commandBlockPaint.setColor(Color.BLACK);
        commandBlockPaint.setStyle(Paint.Style.STROKE); // 描画設定を'線'に設定
        commandBlockPaint.setAntiAlias(true);
        commandBlockPaint.setStrokeWidth(commandBlockSize);           // 線の太さ
        commandBlockLinePaint = new Paint();
        commandBlockLinePaint.setColor(Color.BLACK);
        commandBlockLinePaint.setStyle(Paint.Style.STROKE); // 描画設定を'線'に設定
        commandBlockLinePaint.setAntiAlias(true);
        commandBlockLinePaint.setStrokeWidth(commandBlockLineWidth);           // 線の太さ
    }


    //canvasのサイズを取得
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        canvasWidth = this.getWidth();
        canvasHeight = this.getHeight();
        windowWidth = mainViewWindowWidth / mainViewMaxWidth * canvasWidth;
        windowHeight = canvasHeight;

    }

    public void windowSizeChange(float minSize, float maxSize) {

        float tempWindowWidth = windowWidth;
        windowWidth = minSize / maxSize * canvasWidth;
        nowPositionX = nowPositionX * windowWidth / tempWindowWidth;

        mainViewWindowWidth = minSize;
        mainViewMaxWidth = maxSize;

    }


    //======================================================================================
    //--  描画メソッド
    //======================================================================================
    @Override
    protected void onDraw(Canvas canvas) {
        //コマンドブロックを生成
        canvas.drawCircle(commandBlockMargin, canvasHeight * 2 / 3, 1, commandBlockPaint);
        canvas.drawCircle(canvasWidth - commandBlockMargin, canvasHeight * 2 / 3, 1, commandBlockPaint);
        canvas.drawLine(commandBlockMargin, canvasHeight * 2 / 3, canvasWidth - commandBlockMargin, canvasHeight * 2 / 3, commandBlockLinePaint);

        commandBlockLineLength = canvasWidth - commandBlockMargin * 2;
        for (int i = 0; i < commandBlockNum; i++) {

            ScriptMainAdapter.ScriptSet commandBlockSet = commandBlocks.getItem(i);
            ScriptModel specialScript = commandBlockSet.getScriptSpecial();
            ScriptModel defaultScript = commandBlockSet.getScriptDefault();
            if (specialScript != null) {
                // trueレーン
                canvas.drawCircle(commandBlockMargin + i * (commandBlockLineLength / (commandBlockNum - 1)), canvasHeight / 3, 1, commandBlockPaint);
                canvas.drawLine(commandBlockMargin + (i - 1) * (commandBlockLineLength / (commandBlockNum - 1)), canvasHeight / 3,
                        commandBlockMargin + i * (commandBlockLineLength / (commandBlockNum - 1)), canvasHeight / 3, commandBlockLinePaint);
            } else {
                if (defaultScript.getIfState() == 2) {
                    canvas.drawLine(commandBlockMargin + (i - 1) * (commandBlockLineLength / (commandBlockNum - 1)), canvasHeight / 3,
                            commandBlockMargin + i * (commandBlockLineLength / (commandBlockNum - 1)), canvasHeight / 3, commandBlockLinePaint);
                }
            }
            if (defaultScript != null) {
                // falseレーン
                if (defaultScript.getBlock() == ScriptModel.SpicaBlock.IF_START) {
                    // ifの始め
                    canvas.drawLine(commandBlockMargin + i * (commandBlockLineLength / (commandBlockNum - 1)), canvasHeight / 3 - commandBlockLineWidth / 2,
                            commandBlockMargin + i * (commandBlockLineLength / (commandBlockNum - 1)), canvasHeight * 2 / 3, commandBlockLinePaint);
                } else if (defaultScript.getBlock() == ScriptModel.SpicaBlock.IF_END) {
                    // ifの終わり
                    canvas.drawLine(commandBlockMargin + i * (commandBlockLineLength / (commandBlockNum - 1)), canvasHeight / 3 - commandBlockLineWidth / 2,
                            commandBlockMargin + i * (commandBlockLineLength / (commandBlockNum - 1)), canvasHeight * 2 / 3, commandBlockLinePaint);
                    canvas.drawLine(commandBlockMargin + (i - 1) * (commandBlockLineLength / (commandBlockNum - 1)), canvasHeight / 3,
                            commandBlockMargin + i * (commandBlockLineLength / (commandBlockNum - 1)), canvasHeight / 3, commandBlockLinePaint);
                }
                canvas.drawCircle(commandBlockMargin + i * (commandBlockLineLength / (commandBlockNum - 1)), canvasHeight * 2 / 3, 1, commandBlockPaint);
            }

            //canvas.drawCircle(commandBlockMargin + i*(commandBlockLineLength / (commandBlockNum - 1))  , canvasHeight / 2, 1, commandBlockPaint);
        }


        //windowを作る
        if (nowPositionX <= windowWidth / 2) {
            nowPositionX = windowWidth / 2;
        }
        if (nowPositionX >= canvasWidth - windowWidth / 2) {
            nowPositionX = canvasWidth - windowWidth / 2;
        }

        canvas.drawLine(nowPositionX - windowWidth / 2, 0 + LineNoMargin, nowPositionX + windowWidth / 2, 0 + LineNoMargin, paint);
        canvas.drawLine(nowPositionX - windowWidth / 2, canvasHeight - LineNoMargin, nowPositionX + windowWidth / 2, canvasHeight - LineNoMargin, paint);
        canvas.drawLine(nowPositionX - windowWidth / 2 + LineNoMargin, 0, nowPositionX - windowWidth / 2 + LineNoMargin, windowHeight, paint);
        canvas.drawLine(nowPositionX + windowWidth / 2 - LineNoMargin, 0, nowPositionX + windowWidth / 2 - LineNoMargin, windowHeight, paint);

        invalidate();   // 再描画
    }


    private Path drawingPath;

    @Override
    public boolean onTouchEvent(MotionEvent event) {

//        switch (event.getAction()) {
//            case MotionEvent.ACTION_DOWN:                             //- 画面をタッチしたとき
//                nowPositionX = event.getX();
//                y = event.getY();
//                scriptMainActivity.setScroll(getPosition());
//                break;
//            case MotionEvent.ACTION_UP:                               //- 画面から指を離したとき
//                nowPositionX = event.getX();
//                y = event.getY();
//                scriptMainActivity.setScroll(getPosition());
//
//                break;
//            case MotionEvent.ACTION_MOVE:                             //- タッチしながら指をスライドさせたとき
//                nowPositionX = event.getX();
//                y = event.getY();
//                scriptMainActivity.setScroll(getPosition());
//
//                break;
//        }

        return true;   /* 返却値は必ず "true" にすること!! */
    }


    public void allDelete() {
        return;   // リストが保持しているPathのインスタンスを全て削除
    }

    public void setCommandBlocks(ScriptMainAdapter commandBlocks) {
        this.commandBlocks = commandBlocks;
        this.commandBlockNum = commandBlocks.getItemCount();
        invalidate();
    }

    public int getCommandBlockNum() {
        return commandBlockNum;
    }

    public float getPosition() {
        if (nowPositionX <= windowWidth / 2) {
            nowPositionX = windowWidth / 2;
        }
        if (nowPositionX >= canvasWidth - windowWidth / 2) {
            nowPositionX = canvasWidth - windowWidth / 2;
        }
        //比率を求める
        return (nowPositionX - windowWidth / 2) / (canvasWidth - windowWidth);
    }

    public void setPositon(float position) {
        if (mainViewMaxWidth != mainViewWindowWidth) {
            nowPositionX = position / (mainViewMaxWidth - mainViewWindowWidth) * (canvasWidth - windowWidth) + windowWidth / 2;
        } else {
            nowPositionX = 0;
        }
    }
}
