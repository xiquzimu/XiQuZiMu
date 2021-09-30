package me.xlgp.xiquzimu.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class SearchListAdapter<T> extends BaseAdapter<T> {
    protected List<T> sourceList;
    protected List<T> filterList;

    public SearchListAdapter() {
        this(new ArrayList<T>());
    }

    public SearchListAdapter(List<T> list) {
        super(list);
        setListData(list);
    }

    private void setListData(List<T> list) {
        sourceList = list;
        filterList = sourceList;
    }

    public void filter(Predicate<T> predicate) {
        filterList = sourceList.stream().filter(predicate).collect(Collectors.toList());
        super.updateData(filterList);
    }

    @Override
    public void updateData(List<T> list) {
        setListData(list);
        super.updateData(filterList);
    }
}
