/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.ijse.mvc.model;

import edu.ijse.mvc.db.DBConnection;
import edu.ijse.mvc.dto.CustomerDto;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 *
 * @author Anjana
 */
public class CustomerModel {
    public CustomerDto getCustomer(String customerId) throws Exception{
        Connection connection = DBConnection.getInstance().getConnection();
        String sql = "SELECT * FROM Customer WHERE CustID = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, customerId);
        
        ResultSet rst = statement.executeQuery();
        
        if(rst.next()){
            CustomerDto customerDto = new CustomerDto(
                    rst.getString("CustID"),
                    rst.getString("CustTitle"), 
                    rst.getString("CustName"),
                    rst.getString("DOB"), 
                    rst.getDouble("salary"), 
                    rst.getString("CustAddress"),
                    rst.getString("City"), 
                    rst.getString("Province"), 
                    rst.getString("PostalCode"));
        
            return customerDto;
        }
        return null;
    }
}
