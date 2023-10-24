module Demo {
    interface CallbackReceiver {
        void callback(string message);
    };

    interface CallbackSender {
        void initiateCallback(CallbackReceiver* proxy);
        void shutdown();
        void registerClient(string name, CallbackReceiver* proxy);
        void sendMessageToOne(string hostname, string message);
        void sendMessageToGroup(string groupName, string message);
        string listClients();
        void createGroup(string users, string groupName);
    };
};