package com.example.moneynote.db;

public class TypeBean {
    int id;
    String typeName;//类型名称
    int ImgID;//未被选中图片id
    int sImgID;//被选中图片id
    int kind;//收入-1 支出-0

    public TypeBean() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public int getImgID() {
        return ImgID;
    }

    public void setImgID(int imgID) {
        ImgID = imgID;
    }

    public int getsImgID() {
        return sImgID;
    }

    public int getKind() {
        return kind;
    }

    public void setsImgID(int sImgID) {
        this.sImgID = sImgID;
    }

    public void setKind(int kind) {
        this.kind = kind;
    }
    public TypeBean(int id, String typeName, int imgID, int sImgID, int kind) {
        this.id = id;
        this.typeName = typeName;
        this.ImgID = imgID;
        this.sImgID = sImgID;
        this.kind = kind;
    }


}
