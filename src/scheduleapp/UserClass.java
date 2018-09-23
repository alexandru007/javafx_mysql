/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scheduleapp;

/**
 *
 * @author alisii
 */
public class UserClass {
    
    private static int currentUserID;
    
    public static void setCurrentUserID(int id) {
        currentUserID = id;
    }
    
    public  static int getCurrentUserID() {
        return currentUserID;
    }  
}
