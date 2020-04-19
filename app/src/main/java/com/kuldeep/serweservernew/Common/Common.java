package com.kuldeep.serweservernew.Common;


import com.kuldeep.serweservernew.Model.User;

public class Common {
    public static User currentUser;

    public static final String UPDATE = "Update";
    public static final String DELETE = "Delete";

    // Request to upload image
    public static final int PICK_IMAGE_REQUEST = 71;


    public static String convertCodeToStatus(String code)
    {
        if(code.equals("0"))
            return "Placed";
            else
                if(code.equals("1"))
                    return "On My Way";

                else
                    return "Shipped";
    }

}
