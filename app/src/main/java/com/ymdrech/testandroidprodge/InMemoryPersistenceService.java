package com.ymdrech.testandroidprodge;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by e4t on 28/02/16.
 */
public class InMemoryPersistenceService implements PersistenceService {

    private List<LocationRecord> locationRecords = new ArrayList<>();

    @Override
    public void persistLocationRecord(LocationRecord location) {
        locationRecords.add(location);
    }
}
