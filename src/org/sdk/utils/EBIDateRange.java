package org.sdk.utils;

public class EBIDateRange {

    private final long end;
    private final long start;

    public EBIDateRange(final long aStartDate, final long anEndDate) {
        start = aStartDate;
        end = anEndDate;
    }

    public int compareTo(final long astart, final long aend) {
        if (astart >= start && aend <= end) {
            return 1;
        } else if (astart <= start && aend >= end) {
            return 1;
        } else if (astart >= end && aend <= start) {
            return -1;
        }
        return 0;
    }
}
