package com.example.currencyconversation.viewModels;

public class VmLocator {

    private static VmLocator vmLocator;
    private VmCurrencyConverter vmCurrencyConverter;

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

    public VmCurrencyConverter getVmCurrencyConverter() {
        if (vmCurrencyConverter == null) {
            vmCurrencyConverter = new VmCurrencyConverter();
        }
        return vmCurrencyConverter;
    }
}
