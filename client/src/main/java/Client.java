import java.io.BufferedReader;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;


import Demo.CallbackReceiverPrx;
import Demo.CallbackSenderPrx;
import com.zeroc.Ice.LocalException;

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
                System.out.print("==> ");
                System.out.flush();
                line = in.readLine();
                if(line == null)
                {
                    break;
                }
                if(line.equals("r"))
                {
                    System.out.println("Ingrese su nombre de usuario:");
                    String username = in.readLine();
                    sender.registerUser(username, receiver);
                }
                else if(line.equals("m"))
                {
                    System.out.println("Ingrese el nombre del destinatario:");
                    String targetUser = in.readLine();
                    System.out.println("Ingrese su mensaje:");
                    String message = in.readLine();
                    sender.sendMessage(receiver, targetUser, message);
                }
                else if(line.equals("v"))
                {
                    List<String> messages = receiver.getReceivedMessages();
                    for(String msg : messages)
                    {
                        System.out.println(msg);
                    }
                }
                else if(line.equals("s"))
                {
                    sender.shutdown();
                }
                else if(line.equals("x"))
                {
                    // Nothing to do
                }
                else if(line.equals("?"))
                {
                    menu();
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
        while(!line.equals("x"));

        return 0;
    }

    private static void menu()
    {
        System.out.println(
                "usage:\n" +
                        "r: registrar usuario\n" +
                        "m: enviar mensaje a un usuario\n" +
                        "v: ver mensajes recibidos\n" +
                        "s: shutdown server\n" +
                        "x: exit\n" +
                        "?: help\n");
    }

}