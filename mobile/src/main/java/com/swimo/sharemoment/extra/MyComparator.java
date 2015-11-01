package com.swimo.sharemoment.extra;

import com.swimo.sharemoment.model.Leader;

import java.util.Comparator;

/**
 * Created by swimo on 01/11/15.
 */
public class MyComparator implements Comparator<Leader> {
    @Override
    public int compare(Leader lhs, Leader rhs) {
        if (lhs.getpLead() > rhs.getpLead()) {
            return -1;
        } else if (lhs.getpLead() < rhs.getpLead()) {
            return 1;
        }
        return 0;

    }
}
