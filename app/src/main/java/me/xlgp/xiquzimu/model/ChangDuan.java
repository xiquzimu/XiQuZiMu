package me.xlgp.xiquzimu.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity
public class ChangDuan {
    @PrimaryKey(autoGenerate = true)
    private Integer id;

    private String name;
    /**
     * 剧种
     */
    private String juZhong;

    /**
     * 剧目
     */
    private String juMu;

    /**
     * 时间差补
     */
    private Integer offset;

    private long createTime;

    public ChangDuan() {
        //默认提前8s
        this.offset = -8;
        createTime = (new Date()).getTime();
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJuZhong() {
        return juZhong;
    }

    public void setJuZhong(String juZhong) {
        this.juZhong = juZhong;
    }

    public String getJuMu() {
        return juMu;
    }

    public void setJuMu(String juMu) {
        this.juMu = juMu;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }
}
