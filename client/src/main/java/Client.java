import Demo.CallbackReceiverPrx;
import Demo.CallbackSenderPrx;

import java.net.UnknownHostException;

public class Client {
    public static void main(String[] args)
    {
        int status = 0;
        java.util.List<String> extraArgs = new java.util.ArrayList<>();

        //
        // Try with resources block - communicator is automatically destroyed
        // at the end of this try block
        //
        try(com.zeroc.Ice.Communicator communicator = com.zeroc.Ice.Util.initialize(args, "config.client", extraArgs))
        {
            communicator.getProperties().setProperty("Ice.Default.Package", "com.zeroc.demos.Ice.callback");

            if(!extraArgs.isEmpty())
            {
                System.err.println("too many arguments");
                status = 1;
            }
            else
            {
                status = run(communicator);
            }
        }
        System.exit(status);
    }

    private static int run(com.zeroc.Ice.Communicator communicator)
    {
        CallbackSenderPrx sender = CallbackSenderPrx.checkedCast(
                communicator.propertyToProxy("CallbackSender.Proxy")).ice_twoway().ice_timeout(-1).ice_secure(false);
        if(sender == null)
        {
            System.err.println("invalid proxy");
            return 1;
        }

        com.zeroc.Ice.ObjectAdapter adapter = communicator.createObjectAdapter("Callback.Client");
        adapter.add(new CallbackReceiverI(), com.zeroc.Ice.Util.stringToIdentity("callbackReceiver"));
        adapter.activate();

        CallbackReceiverPrx receiver =
                CallbackReceiverPrx.uncheckedCast(adapter.createProxy(
                        com.zeroc.Ice.Util.stringToIdentity("callbackReceiver")));

        menu();

        java.io.BufferedReader in = new java.io.BufferedReader(new java.io.InputStreamReader(System.in));

        String line = null;
        do
        {
            try
            {
                menu();
                System.out.print("==> ");
                System.out.flush();
                line = in.readLine();
                if(line == null)
                {
                    break;
                }
                if(line.equals("1"))
                {
                    System.out.println("Enter the user name");
                    String username = in.readLine();

                    sender.registerClient(username, receiver);

                }
                else if(line.equals("2"))
                {
                    System.out.println("These are the users");
                    System.out.println(sender.listClients());
                }
                else if(line.equals("3"))
                {
                    System.out.println("These are the users");
                    System.out.println(sender.listClients());

                    System.out.println("Type the users that you want to include in this format: name,name,name");
                    String users = in.readLine();

                    System.out.println("Enter the group name");
                    String groupName = in.readLine();

                    sender.createGroup(users, groupName);
                }
                else if(line.equals("4"))
                {
                    System.out.println("who's the message for");
                    String user = in.readLine();

                    System.out.println("Enter the message");
                    String message = in.readLine();

                    sender.sendMessageToOne(user, message);
                } else if (line.equals("5")) {
                    System.out.println("Which group is the message for?");
                    String groupName = in.readLine();

                    System.out.println("Enter the message");
                    String message = in.readLine();

                    System.out.println("Who's sending the message");
                    String name = in.readLine();

                    sender.sendMessageToGroup(groupName, message, name, receiver);
                } else if(line.equals("6")) {
                    System.out.println("Who is the honored");
                    String name = in.readLine();

                    sender.setHonored(name);
                } else if(line.equals("7")) {
                    System.out.println("What do you want to modify? \n");
                    System.out.println("1. Add user \n" +
                            "2. Eliminate user \n" +
                            "3. Delete group \n");

                    System.out.print("==> ");
                    System.out.flush();
                    String option = in.readLine();

                    if(option.equals("1")){
                        System.out.println("Enter username");
                        String name = in.readLine();

                        System.out.println("Enter group name");
                        String groupName = in.readLine();

                        sender.addUserToGroup(name, groupName);
                    } else if (option.equals("2")){
                        System.out.println("Enter username");
                        String name = in.readLine();

                        System.out.println("Enter group name");
                        String groupName = in.readLine();

                        sender.eliminateUserFromGroup(name, groupName);
                    } else if (option.equals("3")){
                        System.out.println("Enter group name");
                        String groupName = in.readLine();

                        sender.deleteGroup(groupName);
                    }
                }
                else
                {
                    System.out.println("unknown command `" + line + "'");
                    menu();
                }
            }
            catch(java.io.IOException ex)
            {
                ex.printStackTrace();
            }
            catch(com.zeroc.Ice.LocalException ex)
            {
                ex.printStackTrace();
            }
        }
        while(!line.equals("8"));

        return 0;
    }

    private static void menu()
    {
        System.out.println("1. Register user \n" +
                "2. List users \n" +
                "3. Create Group \n" +
                "4. Send message  \n" +
                "5. Send message to group \n" +
                "6. Set honored \n" +
                "7. Modify group \n" +
                "8. Exit \n");
    }
}