package sample.com.benjiweber.yield;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.atomic.AtomicReference;

import static sample.com.benjiweber.yield.Completed.completed;
import static sample.com.benjiweber.yield.Exceptions.unchecked;
import static sample.com.benjiweber.yield.FlowControl.youMayProceed;
import static sample.com.benjiweber.yield.IfAbsent.ifAbsent;
import static sample.com.benjiweber.yield.Message.message;
import sample.com.benjiweber.yield.YieldDefinition;

public interface Yielderable<T> extends Iterable<T> {

    void execute(YieldDefinition<T> builder);

    default ClosableIterator<T> iterator() {
        YieldDefinition<T> yieldDefinition = new YieldDefinition<>();
        Thread collectorThread = new Thread(() -> {
            yieldDefinition.waitUntilFirstValueRequested();
            try {
                execute(yieldDefinition);
            } catch (BreakException e) {
            }
            yieldDefinition.signalComplete();
        });
        collectorThread.setDaemon(true);
        collectorThread.start();
        yieldDefinition.onClose(collectorThread::interrupt);
        return yieldDefinition.iterator();
    }
}

interface Message<T> {
    Optional<T> value();
    static <T> Message<T> message(T value) {
        return () -> Optional.of(value);
    }
}

interface Completed<T> extends Message<T> {
    static <T> Completed<T> completed() { return () -> Optional.empty(); }
}

interface FlowControl {
    static FlowControl youMayProceed = new FlowControl() {};
}

class BreakException extends RuntimeException {
    public synchronized Throwable fillInStackTrace() {
        return null;
    }
}

interface Then<T> {
    void then(Runnable r);
}
class IfAbsent {
    public static <T> Then<T> ifAbsent(Optional<T> optional) {
        return runnable -> {
            if (!optional.isPresent()) runnable.run();
        };
    }
}


