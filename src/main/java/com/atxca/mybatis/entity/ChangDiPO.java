package com.atxca.mybatis.entity;

import com.atxca.sportshall.PO.VenuePO;
import com.atxca.sportshall.PO.Vhouse;

import java.util.List;

public class ChangDiPO {
    private Vhouse vhouse;
    private List<VenuePO> venuePOList;

    public Vhouse getVhouse() {
        return vhouse;
    }

    public void setVhouse(Vhouse vhouse) {
        this.vhouse = vhouse;
    }

    public List<VenuePO> getVenuePOList() {
        return venuePOList;
    }

    public void setVenuePOList(List<VenuePO> venuePOList) {
        this.venuePOList = venuePOList;
    }
}
