import Demo.CallbackReceiver;
import Demo.CallbackReceiverPrx;
import com.zeroc.Ice.Current;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

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
        final ClientObject clientObject = new ClientObject(name, proxy, current);
        final CallbackReceiverPrx callbackReceiverPrx = proxy;
        final List<ClientObject> clientsCopy = clients;
        CompletableFuture.runAsync(() -> {
            if(findClient(name) == null){
                clientsCopy.add(clientObject);
            } else {
                callbackReceiverPrx.callback("The username already exist");
            }
        });
        clients = clientsCopy;
    }

    @Override
    public void sendMessageToOne(String hostname, String message, Current current) {
        final String name = hostname;
        final String msg = message;
        CompletableFuture.runAsync(() -> {
            ClientObject client = findClient(name);
            if(client == null){
                System.out.println("This client doesn't exit");
            } else if (!client.isHonored()){
                CallbackReceiverPrx receiverPrx = client.getProxy();
                receiverPrx.callback(msg);
            }
        });
    }

    @Override
    public void sendMessageToGroup(String groupName, String message, String username, CallbackReceiverPrx proxy, Current current) {
        final String name = groupName;
        final String msg = message;
        final Current cu = current;
        final String nameuser = username;
        final CallbackReceiverPrx callbackReceiverPrx = proxy;
        CompletableFuture.runAsync(() -> {
            Group group = findGroup(name);
            System.out.println(nameuser);
            if(group.findClient(nameuser)){
                ArrayList<ClientObject> groupClients = group.getClients();

                for (int i = 0; i < groupClients.size(); i++) {
                    sendMessageToOne(groupClients.get(i).getName(), msg, cu);
                }
            } else {
                callbackReceiverPrx.callback("You aren´t in this group");
            }
        });
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
        groups.add(group);
    }

    @Override
    public void setHonored(String name, Current current) {
        ClientObject clientObject = findClient(name);
        clientObject.setHonored(true);
    }

    @Override
    public void addUserToGroup(String name, String groupName, Current current) {
        Group group = findGroup(groupName);
        group.getClients().add(findClient(name));
    }

    @Override
    public void eliminateUserFromGroup(String name, String groupName, Current current) {
        Group group = findGroup(groupName);

        group.deleteUser(name);
    }

    @Override
    public void deleteGroup(String groupName, Current current) {
        for (int i = 0; i < groups.size(); i++) {
            if(groups.get(i).getName().equalsIgnoreCase(groupName)){
                groups.remove(i);
            }
        }
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
