// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain;

import java.io.Serializable;
import org.dddml.ffvtraceability.domain.*;

public class Gs1AppIdRange implements Serializable {
    private String start;

    public String getStart()
    {
        return this.start;
    }

    public void setStart(String start)
    {
        this.start = start;
    }

    private String end;

    public String getEnd()
    {
        return this.end;
    }

    public void setEnd(String end)
    {
        this.end = end;
    }

    private String item;

    public String getItem()
    {
        return this.item;
    }

    public void setItem(String item)
    {
        this.item = item;
    }

    public Gs1AppIdRange()
    {
    }

    public Gs1AppIdRange(String start, String end, String item)
    {
        this.start = start;
        this.end = end;
        this.item = item;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }

        Gs1AppIdRange other = (Gs1AppIdRange)obj;
        return true 
            && (start == other.start || (start != null && start.equals(other.start)))
            && (end == other.end || (end != null && end.equals(other.end)))
            && (item == other.item || (item != null && item.equals(other.item)))
            ;
    }

    @Override
    public int hashCode()
    {
        int hash = 0;
        if (this.start != null) {
            hash += 13 * this.start.hashCode();
        }
        if (this.end != null) {
            hash += 13 * this.end.hashCode();
        }
        if (this.item != null) {
            hash += 13 * this.item.hashCode();
        }
        return hash;
    }

    @Override
    public String toString() {
        return "Gs1AppIdRange{" +
                "start=" + '\'' + start + '\'' +
                ", end=" + '\'' + end + '\'' +
                ", item=" + '\'' + item + '\'' +
                '}';
    }


}
