package com.example.currencyconversation.viewModels;

public class VmLocator {

    private static VmLocator vmLocator;
    private VmConverter vmConverter;

    public static VmLocator getInstance() {
        if (vmLocator != null) {
            return vmLocator;
        }
        synchronized(VmLocator.class) {
            if (vmLocator == null) {
                vmLocator = new VmLocator();
            }
        }
        return vmLocator;
    }

    private VmLocator() { }

    public VmConverter getVmConverter() {
        if (vmConverter == null) {
            vmConverter = new VmConverter();
        }
        return vmConverter;
    }
}
