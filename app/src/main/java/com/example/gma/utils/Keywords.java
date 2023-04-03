package com.example.gma.utils;

public class Keywords {

    public static class TASK_STATUS{
        public static final int COMPLETED = 0;
        public static final int SEND = 1;
        public static final int FAILED = -1;
    }
    public static class FIREBASE_DOC{
        public static final String users = "users";
        public static final String tasks = "tasks";
        public static final String vehicles = "vehicles";
    }
    public static class USER_STATUS{
        public static final String enable = "enabled";
        public static final String disable = "disabled";
    }
    public static class USER_TYPE{
        public static final String admin = "Admin";
        public static final String driver = "Driver";
        public static final String publc = "Public";
    }
}
