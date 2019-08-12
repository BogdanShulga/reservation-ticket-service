import handlers.Service;
import handlers.Booking;
import mainObjects.Cinema;
import mainObjects.Client;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.*;

public class Main {
    public static void main(String[] args) {
        Map<LocalDate, List<Cinema>> dateListMap = new ConcurrentHashMap<>();
        List<Client> clients = new CopyOnWriteArrayList<>();
        Set<Booking> bookings = ConcurrentHashMap.newKeySet();

        ExecutorService service = Executors.newSingleThreadExecutor();
        boolean stop = false;
        while (!stop) {
            try {
                Future<?> f = service.submit(new Service(dateListMap, clients, bookings));
                f.get(15, TimeUnit.MINUTES);
            }
            catch (final InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
            catch (final TimeoutException e) {
                System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                System.out.println("Your booking is discarded, because session time has elapsed");
                continue;
            }
            finally {
                service.shutdown();
            }
            stop = true;
        }

    }
}
