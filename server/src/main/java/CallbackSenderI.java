import Demo.CallbackReceiver;
import Demo.CallbackReceiverPrx;
import com.zeroc.Ice.Current;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class CallbackSenderI implements Demo.CallbackSender{
    private List<ClientObject> clients = new ArrayList<>();

    @Override
    public void initiateCallback(CallbackReceiverPrx proxy, com.zeroc.Ice.Current current)
    {
        System.out.println("initiating callback");
        try
        {
            proxy.callback("success callback");
        }
        catch(com.zeroc.Ice.LocalException ex)
        {
            ex.printStackTrace();
        }
    }

    @Override
    public void shutdown(com.zeroc.Ice.Current current)
    {
        System.out.println("Shutting down...");
        try
        {
            current.adapter.getCommunicator().shutdown();
        }
        catch(com.zeroc.Ice.LocalException ex)
        {
            ex.printStackTrace();
        }
    }

    @Override
    public void registerClient(String name, CallbackReceiverPrx proxy, Current current) {
        if(findClient(name) == null){
            clients.put(name,proxy);
        }
    }

    private ClientObject findClient(String name){
        for (int i = 0; i < clients.size(); i++) {
            if(clients.get(i).getName().equals(name)){
                return clients.get(i);
            }
        }
        return null;
    }
}
