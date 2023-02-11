package com.driver;

import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class OrderRepository {

    Map<String,Order> orderList=new HashMap<>();
    Map<String,DeliveryPartner> partnerList=new HashMap<>();

    Map<String, List<Order>> partnerOrderList =new HashMap<>();//key is partner id
    List<String>assignOrderList=new ArrayList<>();
    Map<String, List<String>> partnerOrderNameList =new HashMap<>();//key is partner id

    //adding new order
    public void addOrder(Order order){
        String key=order.getId();
        orderList.put(key,order);
    }
    //add new partner
    public void addPartner(String partnerId){
        DeliveryPartner partner=new DeliveryPartner(partnerId);
        partnerList.put(partnerId,partner);
        //adding full order in list
        List<Order>list=new ArrayList<>();
        partnerOrderList.put(partnerId,list);
        //adding order name in list
        List<String>nameList=new ArrayList<>();
        partnerOrderNameList.put(partnerId,nameList);
    }
    //add partner order pair
    public void addOrderPartnerPair(String orderId, String partnerId){
        //getting order from orderId
        Order neworder=orderList.get(orderId);
        List<Order> currentOrderList=partnerOrderList.get(partnerId);
        currentOrderList.add(neworder);//add new order in list
        partnerOrderList.put(partnerId,currentOrderList);//save the list with partner Id

        assignOrderList.add(neworder.getId());
        //adding order in ordername list
        List<String>list=partnerOrderNameList.get(partnerId);
        list.add(neworder.getId());
        partnerOrderNameList.put(partnerId,list);

        //updating the numberOfOrder of that partnet
        DeliveryPartner deliveryPartner=partnerList.get(partnerId);
        int numberOfOrder=deliveryPartner.getNumberOfOrders();
        numberOfOrder =numberOfOrder+1;
        deliveryPartner.setNumberOfOrders(numberOfOrder);
        partnerList.put(partnerId,deliveryPartner);
    }

    //get a order
    public Order getOrderById(String orderId){
        return orderList.get(orderId);
    }

    //get a partner
    public DeliveryPartner getPartnerById(String partnerId){
        return partnerList.get(partnerId);
    }
    //getNoofOrder of a partner
    public int getOrderCountByPartnerId(String partnerId){
        DeliveryPartner deliveryPartner=partnerList.get(partnerId);
        return deliveryPartner.getNumberOfOrders();
    }
    //get order by partner Id
    public  List<String> getOrdersByPartnerId(String partnerId){
        return partnerOrderNameList.get(partnerId);
    }
    //get list of all order

    public List<String> getAllOrders(){
        List<String> currentList=new ArrayList<>();
        for (String id : orderList.keySet()){
            currentList.add(id);
        }
        return currentList;
    }
    //get count of unassigned order
    public int getCountOfUnassignedOrders(){
        List<String> currentList=new ArrayList<>();
        for (String id : orderList.keySet()){
            currentList.add(id);
        }
        int totalsize=currentList.size();
        return totalsize-assignOrderList.size();
    }
    //order left after time passed
    public int getOrdersLeftAfterGivenTimeByPartnerId(String time, String partnerId){
        List<Order>currentList=partnerOrderList.get(partnerId);
        int count=0;
        String hour=time.substring(0,2);
        String minute=time.substring(3);
        int mainhour=Integer.valueOf(hour);
        int mainminute=Integer.valueOf(minute);
        int maintime=(mainhour*60)+mainminute;
        for(int i=0;i<currentList.size();i++){
            Order order=currentList.get(i);
            int tim=order.getDeliveryTime();
            if (tim>maintime){
                count++;
            }
        }
        return count;
    }
    //last delivary time
    //delete a partner by id
    public void deletePartnerById(String partnerId){
        List<String>list=partnerOrderNameList.get(partnerId);
        for (int i=0;i<list.size();i++){
            for (int j=0;j<assignOrderList.size();j++){
                if (assignOrderList.get(j).equals(list.get(i))){
                    assignOrderList.remove(j);
                    break;
                }
            }
        }
        partnerOrderNameList.remove(partnerId);
        partnerOrderNameList.remove(partnerId);
        partnerList.remove(partnerId);
    }
    //delete an order
    public void deleteOrderById(String orderId){
        //delete from patner order list
        boolean workdone=false;
        String partnerId="";
        for(String order : partnerOrderList.keySet()){
            List<Order>list=partnerOrderList.get(order);
            for(int i=0;i<list.size();i++){
                if(list.get(i).getId().equals(orderId)){
                    list.remove(i);
                    workdone=true;
                    partnerId=order;
                    break;
                }
                if (workdone==true){
                    partnerOrderList.put(order,list);
                    break;
                }
            }
        }
        //delete from partnerorder name list
        boolean work=false;
        for(String order : partnerOrderNameList.keySet()){
            if(order.equals(partnerId)){
                List<String>list=partnerOrderNameList.get(partnerId);
                for(int i=0;i<list.size();i++){
                    if(list.get(i).equals(orderId)){
                        list.remove(i);
                        work=true;
                        partnerId=order;
                        break;
                    }
                    if (work==true){
                        partnerOrderNameList.put(order,list);
                        break;
                    }
                }
            }
        }
        DeliveryPartner deliveryPartner=partnerList.get(partnerId);
        int count=deliveryPartner.getNumberOfOrders();
        count -=1;
        deliveryPartner.setNumberOfOrders(count);
        partnerList.put(partnerId,deliveryPartner);
        orderList.remove(orderId);

    }
}
