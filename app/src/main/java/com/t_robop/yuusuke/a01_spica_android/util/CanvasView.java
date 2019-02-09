package com.t_robop.yuusuke.a01_spica_android.util;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.t_robop.yuusuke.a01_spica_android.Block;
import com.t_robop.yuusuke.a01_spica_android.MyApplication;
import com.t_robop.yuusuke.a01_spica_android.R;
import com.t_robop.yuusuke.a01_spica_android.UI.Script.ScriptMainAdapter;
import com.t_robop.yuusuke.a01_spica_android.model.UIBlockModel;

public class CanvasView extends View {

    private final Paint paint;

    private float lineWeight = 2f;

    private float nowPositionX = 0;

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

    private float LineNoMargin = lineWeight / 2;

    ///////
    private int commandBlockNum = 2;
    private int commandBlockSize = 15;
    ///////
    Paint commandBlockPaint;
    Paint commandBlockLinePaint;
    ScriptMainAdapter commandBlocks;

    public CanvasView(Context context) {
        super(context);

        //- Paint関連
        paint = new Paint();
        paint.setColor(Color.RED);          // 色の指定
        paint.setStyle(Paint.Style.STROKE); // 描画設定を'線'に設定
        paint.setAntiAlias(true);           // アンチエイリアスの適応
        paint.setStrokeWidth(commandBlockSize);           // 線の太さ
        commandBlockPaint = new Paint();
        commandBlockPaint.setColor(Color.BLACK);
        commandBlockPaint.setStyle(Paint.Style.STROKE); // 描画設定を'線'に設定
        commandBlockPaint.setAntiAlias(true);
        commandBlockPaint.setStrokeWidth(commandBlockSize);           // 線の太さ
        commandBlockLinePaint = new Paint();
        commandBlockLinePaint.setColor(Color.BLACK);
        commandBlockLinePaint.setStyle(Paint.Style.STROKE); // 描画設定を'線'に設定
        commandBlockLinePaint.setAntiAlias(true);
        commandBlockLinePaint.setStrokeWidth(lineWeight);           // 線の太さ
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
        paint.setStrokeWidth(commandBlockSize);           // 線の太さ
        commandBlockPaint = new Paint();
        commandBlockPaint.setColor(Color.BLACK);
        commandBlockPaint.setStyle(Paint.Style.STROKE); // 描画設定を'線'に設定
        commandBlockPaint.setAntiAlias(true);
        commandBlockPaint.setStrokeWidth(commandBlockSize);           // 線の太さ
        commandBlockLinePaint = new Paint();
        commandBlockLinePaint.setColor(Color.BLACK);
        commandBlockLinePaint.setStyle(Paint.Style.STROKE); // 描画設定を'線'に設定
        commandBlockLinePaint.setAntiAlias(true);
        commandBlockLinePaint.setStrokeWidth(lineWeight);           // 線の太さ
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

    //描画メソッド
    @Override
    protected void onDraw(Canvas canvas) {
        //コマンドブロックを生成
        int commandBlockMargin = 50;
        canvas.drawCircle(commandBlockMargin, canvasHeight * 2 / 3, 1, commandBlockPaint);
        canvas.drawCircle(canvasWidth - commandBlockMargin, canvasHeight * 2 / 3, 1, commandBlockPaint);
        canvas.drawLine(commandBlockMargin, canvasHeight * 2 / 3, canvasWidth - commandBlockMargin, canvasHeight * 2 / 3, commandBlockLinePaint);

        float commandBlockLineLength = canvasWidth - commandBlockMargin * 2;
        for (int i = 0; i < commandBlockNum; i++) {

            ScriptMainAdapter.ScriptSet commandBlockSet = commandBlocks.getItem(i);
            UIBlockModel uiBlockTopLane = commandBlockSet.getUiBlockTopLane();
            UIBlockModel uiBlockUnderLane = commandBlockSet.getUiBlockUnderLane();
            if (uiBlockTopLane != null) {
                commandBlockPaint.setColor(getBlockColor(uiBlockTopLane.getId()));
                // trueレーン
                canvas.drawLine(commandBlockMargin + (i - 1) * (commandBlockLineLength / (commandBlockNum - 1)), canvasHeight / 3,
                        commandBlockMargin + i * (commandBlockLineLength / (commandBlockNum - 1)), canvasHeight / 3, commandBlockLinePaint);
                canvas.drawCircle(commandBlockMargin + i * (commandBlockLineLength / (commandBlockNum - 1)), canvasHeight / 3, 1, commandBlockPaint);
            } else {
                if (uiBlockUnderLane.getIfState() == 2) {
                    canvas.drawLine(commandBlockMargin + (i - 1) * (commandBlockLineLength / (commandBlockNum - 1)), canvasHeight / 3,
                            commandBlockMargin + i * (commandBlockLineLength / (commandBlockNum - 1)), canvasHeight / 3, commandBlockLinePaint);
                }
            }
            if (uiBlockUnderLane != null) {
                commandBlockPaint.setColor(getBlockColor(uiBlockUnderLane.getId()));
                // falseレーン
                int commandBlockLineWidth = 5;
                if (uiBlockUnderLane.getId().equals(Block.IfStartBlock.id)) {
                    // ifの始め
                    canvas.drawLine(commandBlockMargin + i * (commandBlockLineLength / (commandBlockNum - 1)), canvasHeight / 3 - commandBlockLineWidth / 2.0f,
                            commandBlockMargin + i * (commandBlockLineLength / (commandBlockNum - 1)), canvasHeight * 2 / 3, commandBlockLinePaint);
                } else if (uiBlockUnderLane.getId().equals(Block.IfEndBlock.id)) {
                    // ifの終わり
                    canvas.drawLine(commandBlockMargin + i * (commandBlockLineLength / (commandBlockNum - 1)), canvasHeight / 3 - commandBlockLineWidth / 2.0f,
                            commandBlockMargin + i * (commandBlockLineLength / (commandBlockNum - 1)), canvasHeight * 2 / 3, commandBlockLinePaint);
                    canvas.drawLine(commandBlockMargin + (i - 1) * (commandBlockLineLength / (commandBlockNum - 1)), canvasHeight / 3,
                            commandBlockMargin + i * (commandBlockLineLength / (commandBlockNum - 1)), canvasHeight / 3, commandBlockLinePaint);
                }
                canvas.drawCircle(commandBlockMargin + i * (commandBlockLineLength / (commandBlockNum - 1)), canvasHeight * 2 / 3, 1, commandBlockPaint);
            }
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

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return true;
    }


    public void allDelete() {
        // リストが保持しているPathのインスタンスを全て削除
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

    public int getBlockColor(String blockId) {
        switch (blockId) {
            case Block.StartBlock.id:
            case Block.EndBlock.id:
                return MyApplication.getInstance().getResources().getColor(R.color.default_text_color);
            case Block.FrontBlock.id:
            case Block.BackBlock.id:
            case Block.LeftBlock.id:
            case Block.RightBlock.id:
                return MyApplication.getInstance().getResources().getColor(R.color.color_blue);
            case Block.ForStartBlock.id:
            case Block.ForEndBlock.id:
                return MyApplication.getInstance().getResources().getColor(R.color.color_yellow_2);
            case Block.IfStartBlock.id:
            case Block.IfEndBlock.id:
                return MyApplication.getInstance().getResources().getColor(R.color.color_purple);
            case Block.BreakBlock.id:
                return MyApplication.getInstance().getResources().getColor(R.color.color_red);
        }
        return MyApplication.getInstance().getResources().getColor(R.color.default_text_color);
    }
}
