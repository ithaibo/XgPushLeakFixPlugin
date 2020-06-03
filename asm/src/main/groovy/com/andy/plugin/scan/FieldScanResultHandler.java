package com.andy.plugin.scan;

import com.andy.plugin.model.InvokeScanned;
import com.andy.plugin.util.PluginUtil;

import java.util.Collection;

public class FieldScanResultHandler implements ScanResultHandler {

    public static FieldScanResultHandler create() {
        return new FieldScanResultHandler();
    }

    @Override
    public void handle() {
        handle(Params.fieldScannedList);
    }

    private void handle(Collection<InvokeScanned> resultScanned) {
        if (null == resultScanned ||resultScanned.isEmpty()) return;

        PluginUtil.dumpJsonFile(resultScanned, "fieldInvokeList");
    }
}
