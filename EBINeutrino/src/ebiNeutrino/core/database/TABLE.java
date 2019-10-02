package ebiNeutrino.core.database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * EBI NEUTRINO DATABASE TABLE CLASS BEAN
 **/
public final class TABLE {

    private List<HashMap<String, Object>> ROWS = null;
    private final HashMap<String, Object> ROW = null;

    public TABLE() {
        ROWS = new ArrayList<HashMap<String, Object>>();
    }

    public final TABLE CLEAR() {
        ROWS.clear();
        return this;
    }

    public final TABLE NEW_ROW(final int rowNr) {
        ROWS.add(rowNr, new HashMap<String, Object>());
        return this;
    }

    public final HashMap<String, Object> GET_ROW(final int rowNr){
        return ROWS.get(rowNr);
    }

    public final TABLE ADD_COLUMN(final int rowNr,final String name, final Object value){
        if( ROWS.get(rowNr) != null) {
            ROWS.get(rowNr).put(name, value);
        }
        return this;
    }

    public final HashMap<String, Object> GET_COLUMNS(final int rowNr) {
        HashMap<String, Object> ROW = null;
        if (ROWS.size() > 0) {
            ROW = ROWS.get(rowNr);
        }
        return ROW;
    }

    public final Object GET_COLUMN(final int rowNr, final String NAME) {
        Object OBJ = null;
        if (ROWS.size() > 0) {
            OBJ = ROWS.get(rowNr).get(NAME);
        } else {
            OBJ = "";
        }
        return OBJ;
    }

    public final List<HashMap<String, Object>> GET_ROWS() {
        return ROWS;
    }

    public final TABLE MERGE(final List<HashMap<String, Object>> RS) {
        GET_ROWS().addAll(RS);
        return this;
    }

    public final List<HashMap<String, Object>> MERGE_ROWS(final List<HashMap<String, Object>> RS) {
        final Iterator<HashMap<String, Object>> iter = RS.iterator();
        while (iter.hasNext()) {
            GET_ROWS().add(iter.next());
        }
        return GET_ROWS();
    }
}
