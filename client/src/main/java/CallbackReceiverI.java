import com.zeroc.Ice.Current;
import Demo.CallbackReceiverPrx;

public class CallbackReceiverI implements Demo.CallbackReceiver{

    @Override
    public void callback(String message, Current current) {
        System.out.println(message);
    }
}


