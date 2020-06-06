package com.andy.plugin.scan.result;

import com.andy.plugin.model.InvokeScanned;
import com.andy.plugin.model.Params;
import com.andy.plugin.scan.ScanResultHandler;
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
