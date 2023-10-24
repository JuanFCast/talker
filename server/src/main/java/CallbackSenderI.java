import Demo.CallbackReceiver;
import Demo.CallbackReceiverPrx;
import com.zeroc.Ice.Current;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

public class CallbackSenderI implements Demo.CallbackSender{
    private List<ClientObject> clients = new ArrayList<>();
    private ArrayList<Group> groups = new ArrayList<>();

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
        ClientObject clientObject = new ClientObject(name, proxy, current);
        if(findClient(name) == null){
            clients.add(clientObject);
        }
    }

    @Override
    public void sendMessageToOne(String hostname, String message, Current current) {
        ClientObject client = findClient(hostname);
        if(client == null){
            System.out.println("This client doesn't exit");
        } else {
            CallbackReceiverPrx receiverPrx = client.getProxy();
            receiverPrx.callback(message);
        }
    }

    @Override
    public void sendMessageToGroup(String groupName, String message, Current current) {
        Group group = findGroup(groupName);
        ArrayList<ClientObject> groupClients = group.getClients();

        for (int i = 0; i < groupClients.size(); i++) {
            sendMessageToOne(groupClients.get(i).getName(), message, current);
        }
    }

    @Override
    public String listClients(Current current) {
        String listClients = "";

        for (int i = 0; i < clients.size(); i++) {
            listClients = listClients + "\n" + clients.get(i).getName();
        }
        return listClients;
    }

    @Override
    public void createGroup(String users, String groupName, Current current) {
        ArrayList<ClientObject> clientsGroup = new ArrayList<>();

        String [] usersArray = users.split(",");
        for (String user:
                usersArray) {
            ClientObject clientObject = findClient(user);
            clientsGroup.add(clientObject);
        }

        Group group = new Group(clientsGroup, groupName);
    }

    private ClientObject findClient(String name){
        for (int i = 0; i < clients.size(); i++) {
            if(clients.get(i).getName().equalsIgnoreCase(name)){
                return clients.get(i);
            }
        }
        return null;
    }

    private Group findGroup(String name){
        for (int i = 0; i < groups.size(); i++) {
            if(groups.get(i).getName().equalsIgnoreCase(name)){
                return groups.get(i);
            }
        }
        return null;
    }
}
