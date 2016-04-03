package com.ymdrech.testandroidprodge;

import java.util.Map;

/**
 * Created by e4t on 25/02/16.
 */
public abstract class AbstractDrawScreenLayoutService {

    private AbstractUpdateScreenLayoutService updateScreenLayoutService;

    private WriteDataToScreen writeDataToScreen;

    public void setWriteDataToScreen(WriteDataToScreen writeDataToScreen) {
        this.writeDataToScreen = writeDataToScreen;
    }

    public WriteDataToScreen getWriteDataToScreen() {
        return writeDataToScreen;
    }

    public void drawScreen() {
        Map<String, Object> screenLayoutData = updateScreenLayoutService.retrieveData();
        doDrawScreen(screenLayoutData);
    }

    protected abstract void doDrawScreen(Map<String, Object> screenLayoutData);

    public void setUpdateScreenLayoutService(AbstractUpdateScreenLayoutService updateScreenLayoutService) {
        this.updateScreenLayoutService = updateScreenLayoutService;
    }
}
