package com.t_robop.yuusuke.a01_spica_android.model;

import com.t_robop.yuusuke.a01_spica_android.R;

public class BlockModel {
    //ブロック
    private SpicaBlock block;

    public BlockModel(){ }

    public SpicaBlock getBlock() {
        return block;
    }
    public void setBlock(SpicaBlock block) {
        this.block = block;
    }

    public enum SpicaBlock{
        FRONT("01", R.drawable.front),
        BACK("02", R.drawable.back),
        LEFT("03", R.drawable.left),
        RIGHT("04", R.drawable.right),
        IF_START("05", R.drawable.front),
        IF_END("06", R.drawable.front),
        FOR_START("07", R.drawable.front),
        FOR_END("08", R.drawable.front),
        BREAK("09", R.drawable.ic_break);

        private String id;
        private int icResource;
        SpicaBlock(String id,int icResource){
            this.id=id;
            this.icResource=icResource;
        }

        public String getId() {
            return id;
        }
        public int getIcResource() {
            return icResource;
        }
    }
}
