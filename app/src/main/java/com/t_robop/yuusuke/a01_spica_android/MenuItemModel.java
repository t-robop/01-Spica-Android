package com.t_robop.yuusuke.a01_spica_android;

class MenuItemModel {

    private int itemId;
    private int itemImage;
    private String cmdName;
    private String cmdDetail;

    MenuItemModel(int imageResource, String cmdName, String cmdDetail){
        this.itemImage = imageResource;
        this.cmdName = cmdName;
        this.cmdDetail = cmdDetail;
    }

    public int getItemId() {
        return itemId;
    }

    public int getItemImage() {
        return itemImage;
    }

    public String getCmdName() {
        return cmdName;
    }

    public String getCmdDetail() {
        return cmdDetail;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public void setItemImage(int itemImage) {
        this.itemImage = itemImage;
    }

    public void setCmdName(String cmdName) {
        this.cmdName = cmdName;
    }

    public void setCmdDetail(String cmdDetail) {
        this.cmdDetail = cmdDetail;
    }
}
