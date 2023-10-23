module Demo {
    interface CallbackReceiver {
        void callback(string message);
    };

    interface CallbackSender {
        void initiateCallback(CallbackReceiver* proxy);
        void shutdown();
        void registerClient(string name, CallbackReceiver* proxy);
    };
};