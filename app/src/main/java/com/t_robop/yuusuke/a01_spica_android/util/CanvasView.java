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

public class CanvasView extends View {

    ScriptMainActivity scriptMainActivity;

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
    private int[][] commandBlocks;


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
        boolean ifRoot = false;
        for (int i = 1; i <= commandBlockNum - 2; i++) {
            //メインルートならtrue,ifルートならfalse

            if (commandBlocks[0][i - 1] == 1) {
                canvas.drawCircle(commandBlockMargin + i * (commandBlockLineLength / (commandBlockNum - 1)), canvasHeight * 2 / 3, 1, commandBlockPaint);
            }

            switch (commandBlocks[1][i - 1]) {
                case 0://何もなし
                    break;
                case 1://ifブロック有り
                    canvas.drawCircle(commandBlockMargin + i * (commandBlockLineLength / (commandBlockNum - 1)), canvasHeight / 3, 1, commandBlockPaint);
                    canvas.drawLine(commandBlockMargin + (i - 1) * (commandBlockLineLength / (commandBlockNum - 1)), canvasHeight / 3,
                            commandBlockMargin + i * (commandBlockLineLength / (commandBlockNum - 1)), canvasHeight / 3, commandBlockLinePaint);
                    break;
                case 10://ifの初め
                    canvas.drawLine(commandBlockMargin + i * (commandBlockLineLength / (commandBlockNum - 1)), canvasHeight / 3 - commandBlockLineWidth / 2,
                            commandBlockMargin + i * (commandBlockLineLength / (commandBlockNum - 1)), canvasHeight * 2 / 3, commandBlockLinePaint);
                    break;
                case 20://ifの終わり
                    canvas.drawLine(commandBlockMargin + i * (commandBlockLineLength / (commandBlockNum - 1)), canvasHeight / 3 - commandBlockLineWidth / 2,
                            commandBlockMargin + i * (commandBlockLineLength / (commandBlockNum - 1)), canvasHeight * 2 / 3, commandBlockLinePaint);
                    canvas.drawLine(commandBlockMargin + (i - 1) * (commandBlockLineLength / (commandBlockNum - 1)), canvasHeight / 3,
                            commandBlockMargin + i * (commandBlockLineLength / (commandBlockNum - 1)), canvasHeight / 3, commandBlockLinePaint);
                    break;
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

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:                             //- 画面をタッチしたとき
                nowPositionX = event.getX();
                y = event.getY();
      //          scriptMainActivity.setScroll(nowPositionX / canvasWidth);
                break;
            case MotionEvent.ACTION_UP:                               //- 画面から指を離したとき
                nowPositionX = event.getX();
                y = event.getY();
       //         scriptMainActivity.setScroll(nowPositionX / canvasWidth);

                break;
            case MotionEvent.ACTION_MOVE:                             //- タッチしながら指をスライドさせたとき
                nowPositionX = event.getX();
                y = event.getY();
         //       scriptMainActivity.setScroll(nowPositionX / canvasWidth);

                break;
        }

        return true;   /* 返却値は必ず "true" にすること!! */
    }


    public void allDelete() {
        return;   // リストが保持しているPathのインスタンスを全て削除
    }


    ////////////ここから下全部消す/////////////////
    public void setCommandBlockNum(int num) {
        commandBlockNum = num;
        commandBlocks = new int[2][commandBlockNum - 2];

        for (int i = 0; i < commandBlockNum - 2; i++) {
            commandBlocks[0][i] = 1;
            switch (i % 5) {
                case 0:
                    commandBlocks[1][i] = 0;
                    break;
                case 1:
                    commandBlocks[1][i] = 10;
                    break;
                case 2:
                    commandBlocks[1][i] = 1;
                    break;
                case 3:
                    commandBlocks[1][i] = 1;
                    break;
                case 4:
                    commandBlocks[1][i] = 20;
                    break;
            }
        }
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
        //比率を求めて、mainViewの数値とかける。カーソルは左よりではなく中央に来るので、WindowSizeの半分を引く
        return nowPositionX / canvasWidth * mainViewMaxWidth - mainViewWindowWidth / 2;
    }

    public void setPositon(float positon) {
        nowPositionX =  positon * mainViewWindowWidth / mainViewMaxWidth + windowWidth / 2;
    }

    public void setClass(ScriptMainActivity SMA){
        this.scriptMainActivity = SMA;
    }
}
