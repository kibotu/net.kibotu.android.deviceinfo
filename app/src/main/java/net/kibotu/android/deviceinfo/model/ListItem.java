package net.kibotu.android.deviceinfo.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nyaruhodo on 21.02.2016.
 */
public class ListItem {

    private String label;
    private String value;
    private String description;
    private List<ListItem> children;

    public ListItem setLabel(String label) {
        this.label = label;
        return this;
    }

    public List<ListItem> getChildren() {
        return children;
    }

    public ListItem setChildren(List<ListItem> children) {
        this.children = children;
        return this;
    }

    public ListItem addChild(ListItem child) {
        if (children == null)
            children = new ArrayList<>();

        this.children.add(child);
        return this;
    }

    public String getLabel() {
        return label;
    }


    public ListItem setValue(Object value) {
        this.value = value.toString();
        return this;
    }

    public String getValue() {
        return value;
    }

    public ListItem setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ListItem listItem = (ListItem) o;

        if (label != null ? !label.equals(listItem.label) : listItem.label != null) return false;
        if (value != null ? !value.equals(listItem.value) : listItem.value != null) return false;
        if (description != null ? !description.equals(listItem.description) : listItem.description != null)
            return false;
        return children != null ? children.equals(listItem.children) : listItem.children == null;

    }

    @Override
    public int hashCode() {
        int result = label != null ? label.hashCode() : 0;
        result = 31 * result + (value != null ? value.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (children != null ? children.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ListItem{" +
                "label='" + label + '\'' +
                ", value='" + value + '\'' +
                ", description='" + description + '\'' +
                ", children=" + children +
                '}';
    }
}
