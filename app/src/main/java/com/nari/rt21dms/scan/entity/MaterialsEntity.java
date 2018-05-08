package com.nari.rt21dms.scan.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;

import java.io.Serializable;
import org.greenrobot.greendao.annotation.Generated;


/**
 * Created by dingxujun on 2018/4/8.
 *
 * @project Nari_Scan
 */
@Entity
public class MaterialsEntity implements Serializable {
    private static final long serialVersionUID = 3358811393986336261L;
    @Id(autoincrement = true)
    private Long _ID;

    @Property(nameInDb = "MATERIALNAME")//物资名称
    private String MATERIALNAME;

    @Property(nameInDb = "MATERIALCODE")//物资编码
    private String MATERIALCODE;

    @Property(nameInDb = "MATERIALTYPE")//物资型号
    private String MATERIALTYPE;

    @Property(nameInDb = "SCANCODE")//扫描的编码
    private String SCANCODE;//扫描的编码

    @Generated(hash = 1848211964)
    public MaterialsEntity(Long _ID, String MATERIALNAME, String MATERIALCODE,
            String MATERIALTYPE, String SCANCODE) {
        this._ID = _ID;
        this.MATERIALNAME = MATERIALNAME;
        this.MATERIALCODE = MATERIALCODE;
        this.MATERIALTYPE = MATERIALTYPE;
        this.SCANCODE = SCANCODE;
    }

    @Generated(hash = 1498499037)
    public MaterialsEntity() {
    }

    public Long get_ID() {
        return this._ID;
    }

    public void set_ID(Long _ID) {
        this._ID = _ID;
    }

    public String getMATERIALNAME() {
        return this.MATERIALNAME;
    }

    public void setMATERIALNAME(String MATERIALNAME) {
        this.MATERIALNAME = MATERIALNAME;
    }

    public String getMATERIALCODE() {
        return this.MATERIALCODE;
    }

    public void setMATERIALCODE(String MATERIALCODE) {
        this.MATERIALCODE = MATERIALCODE;
    }

    public String getMATERIALTYPE() {
        return this.MATERIALTYPE;
    }

    public void setMATERIALTYPE(String MATERIALTYPE) {
        this.MATERIALTYPE = MATERIALTYPE;
    }

    public String getSCANCODE() {
        return this.SCANCODE;
    }

    public void setSCANCODE(String SCANCODE) {
        this.SCANCODE = SCANCODE;
    }
    
}
