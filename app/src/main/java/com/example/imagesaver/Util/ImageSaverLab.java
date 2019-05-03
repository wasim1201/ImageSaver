package com.example.imagesaver.Util;

import java.util.ArrayList;

public class ImageSaverLab {

    private static ArrayList<String> supplierList = new ArrayList<>();

    private static ArrayList<String> shippingTypes = new ArrayList<>();

    public static void addSupplierName(String name) {
        supplierList.add(name);
    }

    public static ArrayList<String> getSupplierList() {
        return supplierList;
    }

    public static void removeSupplierName(String name) {
        supplierList.remove(name);
    }

    public static void addShippingTypes(String type) {
        if(!shippingTypes.contains(type)){
            shippingTypes.add(type);
        }
    }

    public static void removeShippingTypes(String type) {
        shippingTypes.remove(type);

    }

    public static ArrayList<String> getShippingTypes(){
        return shippingTypes;
    }


}
