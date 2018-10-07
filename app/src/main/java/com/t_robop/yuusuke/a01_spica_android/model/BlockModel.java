package com.t_robop.yuusuke.a01_spica_android.model;

public class BlockModel {
    //ブロックID
    private int blockId;
    //ブロックicon
    private int iconResource;

    public BlockModel(){ }
    public BlockModel(int blockId,int iconResource){
        this.blockId=blockId;
        this.iconResource=iconResource;
    }

    public int getBlockId(){
        return this.blockId;
    }
    public void setBlockId(int blockId){
        this.blockId=blockId;
    }

    public int getIconResource(){
        return this.iconResource;
    }
    public void setIconResource(int iconResource){
        this.iconResource=iconResource;
    }
}
