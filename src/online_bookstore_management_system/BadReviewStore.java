/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package online_bookstore_management_system;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

/**
 *
 * @author tasniafarinifa
 */
public class BadReviewStore {
 public static final List<Review> ALL = new ArrayList<>();
    public static void store(Review r) { ALL.add(r); }
}
