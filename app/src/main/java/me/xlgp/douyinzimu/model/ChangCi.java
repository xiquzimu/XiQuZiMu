package me.xlgp.douyinzimu.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class ChangCi {
    @PrimaryKey(autoGenerate = true)
    private Integer id;

    private String content;

    @ColumnInfo(name = "show_time")
    private String showTime;

    @ColumnInfo(name = "delay_millis")
    private long delayMillis;

    @ColumnInfo(name = "cd_id")
    private long changDuanId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getChangDuanId() {
        return changDuanId;
    }

    public void setChangDuanId(long changDuanId) {
        this.changDuanId = changDuanId;
    }

    public String getShowTime() {
        return showTime;
    }

    public void setShowTime(String showTime) {
        this.showTime = showTime;
    }

    public long getDelayMillis() {
        return delayMillis;
    }

    public void setDelayMillis(long delayMillis) {
        this.delayMillis = delayMillis;
    }

    @Override
    public String toString() {
        return "ChangCi{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", showTime='" + showTime + '\'' +
                ", delayMillis=" + delayMillis +
                ", changDuanId=" + changDuanId +
                '}';
    }
}
