import Demo.CallbackReceiverPrx;

import java.util.ArrayList;

public class Group {

    private ArrayList<ClientObject> clients;
    private String name;

    public Group(ArrayList<ClientObject> clients, String name) {
        this.clients = clients;
        this.name = name;
    }

    public ArrayList<ClientObject> getClients() {
        return clients;
    }

    public void setClients(ArrayList<ClientObject> clients) {
        this.clients = clients;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void deleteUser(String name){
        for (int i = 0; i < clients.size(); i++) {
            if(clients.get(i).getName().equalsIgnoreCase(name)){
                clients.remove(i);
            }
        }
    }

    public boolean findClient(String name){
        for (ClientObject client :
                clients) {
            if (client.getName().equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }
}