package bloodbank.thread;

import bloodbank.service.StockService;
import javax.swing.SwingUtilities;
import java.util.function.Consumer;

public class ExpiryMonitor extends Thread {
    private final StockService stockService;
    private final Consumer<String> statusListener;
    private volatile boolean running=true;
    public ExpiryMonitor(StockService service,Consumer<String> listener){this.stockService=service;this.statusListener=listener;setName("Blood-Expiry-Monitor");setDaemon(true);}
    public void stopMonitoring(){running=false;interrupt();}
    public void run(){while(running){try{stockService.removeExpired();SwingUtilities.invokeLater(()->statusListener.accept("Stock expiry check completed"));Thread.sleep(5000);}catch(InterruptedException exception){if(!running)break;}catch(Exception exception){SwingUtilities.invokeLater(()->statusListener.accept("Expiry check failed: "+exception.getMessage()));try{Thread.sleep(5000);}catch(InterruptedException ignored){if(!running)break;}}}}
}
