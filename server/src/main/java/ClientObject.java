public class ClientObject {

    private String name;
    private CallbackReceiverPrx proxy;
    private Current current;

    public ClientObject(String name, CallbackReceiverPrx proxy, Current current) {
        this.name = name;
        this.proxy = proxy;
        this.current = current;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CallbackReceiverPrx getProxy() {
        return proxy;
    }

    public void setProxy(CallbackReceiverPrx proxy) {
        this.proxy = proxy;
    }

    public Current getCurrent() {
        return current;
    }

    public void setCurrent(Current current) {
        this.current = current;
    }
}