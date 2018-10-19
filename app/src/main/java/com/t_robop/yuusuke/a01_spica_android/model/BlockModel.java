package com.t_robop.yuusuke.a01_spica_android.model;

import com.t_robop.yuusuke.a01_spica_android.R;

public class BlockModel {
    //ブロック
    private SpicaBlock block;

    public BlockModel() {
    }

    public BlockModel(SpicaBlock block) {
        this.block = block;
    }

    public SpicaBlock getBlock() {
        return block;
    }

    public void setBlock(SpicaBlock block) {
        this.block = block;
    }

    public enum SpicaBlock {
        START("00", "スタート", "", R.drawable.ic_block_start, R.drawable.block_start),
        FRONT("01", "ススム", "", R.drawable.ic_block_front, R.drawable.block_front),
        BACK("02", "サガル", "", R.drawable.ic_block_back, R.drawable.block_back),
        LEFT("03", "マガル", "", R.drawable.ic_block_left, R.drawable.block_turn),
        RIGHT("04", "マガル", "", R.drawable.ic_block_right, R.drawable.block_turn),
        IF_START("05", "モシモ", "", R.drawable.ic_block_if_wall, R.drawable.block_if),
        IF_END("06", "キケツ", "", R.drawable.ic_block_if_end, R.drawable.block_if),
        FOR_START("07", "クリカエス", "", R.drawable.ic_setting, R.drawable.block_for),
        FOR_END("08", "キケツ", "", R.drawable.ic_setting, R.drawable.block_for),
        BREAK("09", "オワル", "", R.drawable.ic_block_break, R.drawable.block_break),
        END("99", "エンド", "", R.drawable.ic_block_end, R.drawable.block_end);

        private String id;
        private String name;
        private String description;
        private int icResource;
        private int bgResource;

        SpicaBlock(String id, String name, String description, int icResource, int bgResource) {
            this.id = id;
            this.name = name;
            this.description=description;
            this.icResource = icResource;
            this.bgResource=bgResource;
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }

        public int getIcResource() {
            return icResource;
        }

        public int getBgResource() {
            return bgResource;
        }
    }
}
