module Demo {
    interface CallbackReceiver {
        void callback(string message);
    };

    interface CallbackSender {
        void initiateCallback(CallbackReceiver* proxy);
        void shutdown();
        void registerClient(string name, CallbackReceiver* proxy);
        void sendMessageToOne(string hostname, string message);
        void sendMessageToGroup(string groupName, string message, string username, CallbackReceiver* proxy);
        string listClients();
        void createGroup(string users, string groupName);
        void setHonored(string name);
        void addUserToGroup(string name, string groupName);
        void eliminateUserFromGroup(string name, string groupName);
        void deleteGroup(string groupName);
    };
};