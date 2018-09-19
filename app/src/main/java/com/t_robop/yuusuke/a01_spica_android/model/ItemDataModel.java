package com.t_robop.yuusuke.a01_spica_android.model;

import java.io.Serializable;
import java.util.EnumSet;

public class ItemDataModel implements Serializable {

    private int rightSpeed;
    private int leftSpeed;
    private int time;
    private BlockState blockState;
    private int loopCount;

    public enum BlockState {
        FORWARD,
        BACK,
        LEFT,
        RIGHT,
        FOR_START,
        FOR_END;

        public static BlockState intToEnum(final int value) {
            for (BlockState m : EnumSet.allOf(BlockState.class)) {
                if (m.ordinal() == value) {
                    return m;
                }
            }
            return null;
        }
    }

    public ItemDataModel() {

    }

    public ItemDataModel(BlockState blockState) {
        switch (blockState) {
            case FORWARD:
            case RIGHT:
            case LEFT:
            case BACK:
                setStandardBlockState(100, 100, 2, blockState, 0);
                break;
            case FOR_START:
                setLoopBlockState(blockState, 2);
                break;
            case FOR_END:
                setLoopBlockState(blockState, 0);
                break;
        }
    }

    private void setStandardBlockState(int rightSpeed, int leftSpeed, int time, BlockState blockState, int loopCount) {
        setRightSpeed(rightSpeed);
        setLeftSpeed(leftSpeed);
        setTime(time);
        setBlockState(blockState);
        setLoopCount(loopCount);
    }

    private void setLoopBlockState(BlockState blockState, int loopCount) {
        setBlockState(blockState);
        setLoopCount(loopCount);
    }

    public int getRightSpeed() {
        return rightSpeed;
    }

    public int getLeftSpeed() {
        return leftSpeed;
    }

    public int getTime() {
        return time;
    }

    public BlockState getBlockState() {
        return blockState;
    }

    public int getLoopCount() {
        return loopCount;
    }

    public void setRightSpeed(int rightSpeed) {
        if (rightSpeed > 255) {
            this.rightSpeed = 255;
        } else if (rightSpeed < 0) {
            this.rightSpeed = 0;
        }
        this.rightSpeed = rightSpeed;
    }

    public void setLeftSpeed(int leftSpeed) {
        if (leftSpeed > 255) {
            this.leftSpeed = 255;
        } else if (leftSpeed < 0) {
            this.leftSpeed = 0;
        }
        this.leftSpeed = leftSpeed;
    }

    public void setTime(int time) {
        if (time < 1) {
            this.time = 1;
        }
        this.time = time;
    }


    public void setBlockState(BlockState blockState) {
        this.blockState = blockState;
    }

    public void setLoopCount(int loopCount) {
        if (loopCount < 0) {
            this.loopCount = 0;
        }
        this.loopCount = loopCount;
    }

    public boolean isLoopBlock() {
        return isLoopStartBlock() || isLoopEndBlock();
    }

    public boolean isLoopStartBlock() {
        return getBlockState() == BlockState.FOR_START;
    }

    public boolean isLoopEndBlock() {
        return getBlockState() == BlockState.FOR_END;
    }

    public boolean isStandardBlock() {
        return isStandardForwardBlock() || isStandardBackBlock() || isStandardLeftBlock() || isStandardRightBlock();
    }

    public boolean isStandardForwardBlock() {
        return getBlockState() == BlockState.FORWARD;
    }

    public boolean isStandardBackBlock() {
        return getBlockState() == BlockState.BACK;
    }

    public boolean isStandardLeftBlock() {
        return getBlockState() == BlockState.LEFT;
    }

    public boolean isStandardRightBlock() {
        return getBlockState() == BlockState.RIGHT;
    }

}
