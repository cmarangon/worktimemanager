package ch.hftm.wtm.model;

import java.util.List;

public class StampingList {

    private List<Stamping> stampingList1;
    private StampingList2 stampingList2;

    public List<Stamping> getStampingList1() {
        return stampingList1;
    }

    public void setStampingList1(List<Stamping> stampingList1) {
        this.stampingList1 = stampingList1;
    }
    
    public StampingList2 getStampingList2() {
        return stampingList2;
    }

    public void setStampingList2(StampingList2 stampingList2) {
        this.stampingList2 = stampingList2;
    }

}
