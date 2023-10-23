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
}