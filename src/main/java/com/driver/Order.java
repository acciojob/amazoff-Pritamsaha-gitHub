package com.driver;

public class Order {

    private String id;
    private int deliveryTime;

    public Order(String id, String deliveryTime) {

        // The deliveryTime has to converted from string to int and then stored in the attribute
        //deliveryTime  = HH*60 + MM
        this.id=id;
        String hour=deliveryTime.substring(0,2);
        String minute=deliveryTime.substring(3);
        int mainhour=Integer.valueOf(hour);
        int mainminute=Integer.valueOf(minute);
        int maintime=(mainhour*60)+mainminute;
        this.deliveryTime=maintime;
    }

    public String getId() {
        return id;
    }

    public int getDeliveryTime() {return deliveryTime;}
}
