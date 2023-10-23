import Demo.CallbackReceiver;
import Demo.CallbackReceiverPrx;
import com.zeroc.Ice.Current;

import java.util.HashMap;
import java.util.Map;

public class CallbackSenderI implements Demo.CallbackSender{
    private Map<String, CallbackReceiverPrx> clients= new HashMap<>();

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
        clients.put(name,proxy);

    }
}
