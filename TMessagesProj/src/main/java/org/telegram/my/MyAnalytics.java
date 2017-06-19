package org.telegram.my;

/**
 * Created by Mostafa on 14/02/2016.
 */
public class MyAnalytics {
    public static class EVENTS {
        public static class PRIVACY {
            public static String Category = "Privacy";

            public static String Blocked = "Blocked";
            public static String NotificationDismissed = "NotificationDismissed";
        }

        public static class LANGUAGE {
            public static String Category = "Language";

            public static String ChangedTo = "ChangedTo";
        }

        public static class FORWARD_HIDDEN {
            public static String Category = "Forward Hidden";

            public static String Start = "Start";
            public static String Sent = "Sent";
        }

        public static class FILTER {
            public static String Category = "Filter";

            public static String ChangedTo = "ChangedTo";
        }

        public static class FAVOURITE {
            public static String Category = "Favourite";

            public static String Set = "Set";
            public static String UnSet = "UnSet";
        }

        public static class GROUP_ADMIN {
            public static String Category = "Group Admin";

            public static String Detected = "Detected";
        }

        public static class EXPORT_CONTACTS {
            public static String Category = "Export Contacts";

            public static String Start = "Start";
            public static String Done = "Done";
        }

        public static class MY_NOTES {
            public static String Category = "My Notes";

            public static String Start = "Start";
        }

        public static class SMS {
            public static String Category = "SMS";

            public static String ReSend = "ReSend";
            public static String Forward = "Forward";
            public static String New_InContactScreen = "New - In Contact Screen";
        }

        public static class REQUEST_FEATURE {
            public static String Category = "Request Feature";

            public static String Start = "Start";
        }

        public static class MULTI_SELECT_DIALOGS {
            public static String Category = "Multi Select Dialogs";

            public static String Forward = "Forward";
        }

    }
}
