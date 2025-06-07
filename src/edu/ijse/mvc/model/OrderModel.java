/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.ijse.mvc.model;

import edu.ijse.mvc.db.DBConnection;
import edu.ijse.mvc.dto.OrderDetailDto;
import edu.ijse.mvc.dto.OrderDto;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 *
 * @author Anjana
 */
public class OrderModel {

    public String placeOrder(OrderDto orderDto, ArrayList<OrderDetailDto> orderDetailDtos) throws Exception {

        Connection connection = DBConnection.getInstance().getConnection();

        try {
            connection.setAutoCommit(false);

            String orderSaveSql = "INSERT INTO Orders VALUES(?,?,?)";
            PreparedStatement orderStatement = connection.prepareStatement(orderSaveSql);
            orderStatement.setString(1, orderDto.getOrderId());
            orderStatement.setString(2, orderDto.getOrderDate());
            orderStatement.setString(3, orderDto.getCustId());

            boolean isOrderSaved = orderStatement.executeUpdate() > 0;

            if (isOrderSaved) {
                boolean isOrderDetailSaved = true;
                String orderDetailSaveSql = "INSERT INTO Orderdetail VALUES(?,?,?,?)";

                for (OrderDetailDto orderDetailDto : orderDetailDtos) {
                    PreparedStatement orderDetailStatement = connection.prepareStatement(orderDetailSaveSql);
                    orderDetailStatement.setString(1, orderDto.getOrderId());
                    orderDetailStatement.setString(2, orderDetailDto.getItemCode());
                    orderDetailStatement.setInt(3, orderDetailDto.getQty());
                    orderDetailStatement.setInt(4, orderDetailDto.getDiscount());

                    if (!(orderDetailStatement.executeUpdate() > 0)) {
                        isOrderDetailSaved = false;
                    }
                }

                if (isOrderDetailSaved) {
                    boolean isItemUpdated = true;
                    for (OrderDetailDto orderDetailDto : orderDetailDtos) {
                        String itemUpdateSql = "UPDATE Item SET QtyOnHand= QtyOnHand - ? WHERE ItemCode = ?";
                        PreparedStatement itemUpdateStatement = connection.prepareStatement(itemUpdateSql);
                        itemUpdateStatement.setInt(1, orderDetailDto.getQty());
                        itemUpdateStatement.setString(2, orderDetailDto.getItemCode());

                        if (!(itemUpdateStatement.executeUpdate() > 0)) {
                            isItemUpdated = false;
                        }
                    }
                    
                    if(isItemUpdated){
                        connection.commit();
                        return "Success";
                    } else {
                        connection.rollback();
                        return "Item Update Error";
                    }
                } else {
                    connection.rollback();
                    return "Order Detail Save Error";
                }

            } else {
                connection.rollback();
                return "Order Save Error";
            }

        } catch (Exception e) {
            e.printStackTrace();
            connection.rollback();
            return "Order Save Error";
        } finally {
            connection.setAutoCommit(true);
        }

    }

}
