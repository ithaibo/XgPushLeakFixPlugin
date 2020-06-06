package com.andy.plugin.scan.result;

import com.andy.plugin.model.InvokeScanned;
import com.andy.plugin.model.Params;
import com.andy.plugin.scan.ScanResultHandler;
import com.andy.plugin.util.PluginUtil;

import java.util.Collection;

public class MethodScanResultHandler implements ScanResultHandler {
    public static MethodScanResultHandler create() {
        return new MethodScanResultHandler();
    }

    @Override
    public void handle() {
        handle(Params.methodScannedList);
    }

    private void handle(Collection<InvokeScanned> result) {
        if (null == result || result.isEmpty()) return;

        PluginUtil.dumpJsonFile(result, "methodInvokeList");
    }

}
