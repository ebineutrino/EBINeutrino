package org.core;

public class EBIVersion {

    private final String majVersion = "7";
    private final String minVersion = "";
    private final String build = "";
    public static EBIVersion version = null;

    public EBIVersion() {
    }

    public String getVersion() {
        return majVersion + minVersion + build;
    }

    public static EBIVersion getInstance() {
        if (version == null) {
            version = new EBIVersion();
        }
        return version;
    }
}
