package com.t_robop.yuusuke.a01_spica_android.model;

import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;

import com.t_robop.yuusuke.a01_spica_android.R;

public enum SpicaBlock {

    FRONT(
            1,
            R.string.block_forward_block_name,
            R.string.block_detail_fragment_block_forward_description,
            R.drawable.ic_block_front,
            R.color.color_blue
    ),

    BACK(2,
            R.string.block_back_block_name,
            R.string.block_detail_fragment_block_back_description,
            R.drawable.ic_block_back,
            R.color.color_blue
    ),

    LEFT(3,
            R.string.block_left_block_name,
            R.string.block_detail_fragment_block_left_description,
            R.drawable.ic_block_left,
            R.color.color_blue
    ),

    RIGHT(4,
            R.string.block_right_block_name,
            R.string.block_detail_fragment_block_right_description,
            R.drawable.ic_block_right,
            R.color.color_blue
    ),

    FOR_START(5,
            R.string.block_for_start_block_name,
            R.string.block_detail_fragment_block_for_start_description,
            R.drawable.ic_block_for_start,
            R.color.color_yellow_2
    ),

    FOR_END(6,
            R.string.block_for_end_block_name,
            0, // none
            R.drawable.ic_block_for_end,
            R.color.color_yellow_2
    ),

    IF_START(7,
            R.string.block_if_start_block_name,
            R.string.block_detail_fragment_block_if_start_description,
            R.drawable.ic_block_if_wall,
            R.color.color_purple
    ),

    IF_END(8,
            R.string.block_if_end_block_name,
            0, // none
            R.drawable.ic_block_if_end,
            R.color.color_purple
    ),

    BREAK(9,
            R.string.block_break_block_name,
            R.string.block_detail_fragment_block_break_description,
            R.drawable.ic_block_break,
            R.color.color_red
    ),

    START(10,
            0, // none
            0, // none
            R.drawable.ic_block_start,
            0 // none
    ),

    END(11,
            0, // none
            0, // none
            R.drawable.ic_block_end,
            0 // none
    );

    private int id;
    @StringRes
    private int title;
    @StringRes
    private int des;
    @DrawableRes
    private int image;
    @ColorRes
    private int bgImage;

    SpicaBlock(
            int id,
            @StringRes int title,
            @StringRes int des,
            @DrawableRes int image,
            @ColorRes int bgImage) {
        this.id = id;
        this.title = title;
        this.des = des;
        this.image = image;
        this.bgImage = bgImage;
    }

    public int getId() {
        return this.id;
    }

    public int getTitle() {
        return title;
    }

    public int getDes() {
        return des;
    }

    public int getBgImage() {
        return bgImage;
    }

    public int getImage() {
        return image;
    }

    public static SpicaBlock getScriptBlock(final int id) {
        SpicaBlock[] blocks = SpicaBlock.values();
        for (SpicaBlock block : blocks) {
            if (block.getId() == id) {
                return block;
            }
        }
        return null;
    }
}
