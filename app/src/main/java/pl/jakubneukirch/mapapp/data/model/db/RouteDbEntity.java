package pl.jakubneukirch.mapapp.data.model.db;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "routes")
public class RouteDbEntity {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private long timestamp;

    public RouteDbEntity(long timestamp) {
        this.timestamp = timestamp;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public boolean equals(Object obj) {
        return true;
    }
}
