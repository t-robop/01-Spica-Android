package com.t_robop.yuusuke.a01_spica_android.model;

import com.t_robop.yuusuke.a01_spica_android.R;

public class BlockModel {
    //ブロック
    private SpicaBlock block;

    public BlockModel(){ }
    public BlockModel(SpicaBlock block){
        this.block=block;
    }

    public SpicaBlock getBlock() {
        return block;
    }
    public void setBlock(SpicaBlock block) {
        this.block = block;
    }

    public enum SpicaBlock{
        START("スタート","00", R.drawable.icon),
        FRONT("ススム","01", R.drawable.front),
        BACK("サガル","02", R.drawable.back),
        LEFT("マガル","03", R.drawable.left),
        RIGHT("マガル","04", R.drawable.right),
        IF_START("モシモ","05", R.drawable.if_wall),
        IF_END("キケツ","06", R.drawable.ic_setting),
        FOR_START("クリカエス","07", R.drawable.ic_setting),
        FOR_END("キケツ","08", R.drawable.ic_setting),
        BREAK("オワル","09", R.drawable.ic_break);

        private String name;
        private String id;
        private int icResource;
        SpicaBlock(String name,String id,int icResource){
            this.name=name;
            this.id=id;
            this.icResource=icResource;
        }

        public String getName() {
            return name;
        }
        public String getId() {
            return id;
        }
        public int getIcResource() {
            return icResource;
        }
    }
}
