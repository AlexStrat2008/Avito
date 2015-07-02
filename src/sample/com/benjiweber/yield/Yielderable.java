package sample.com.benjiweber.yield;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.atomic.AtomicReference;

import static sample.com.benjiweber.yield.Exceptions.unchecked;
import static sample.com.benjiweber.yield.Yielderable.Completed.completed;
import static sample.com.benjiweber.yield.Yielderable.FlowControl.youMayProceed;
import static sample.com.benjiweber.yield.Yielderable.IfAbsent.ifAbsent;
import static sample.com.benjiweber.yield.Yielderable.Message.message;

public interface Yielderable<T> extends Iterable<T> {

    void execute(YieldDefinition<T> builder) throws IOException, URISyntaxException;

    default ClosableIterator<T> iterator() {
        YieldDefinition<T> yieldDefinition = new YieldDefinition<>();
        Thread collectorThread = new Thread(() -> {
            yieldDefinition.waitUntilFirstValueRequested();
            try {
                execute(yieldDefinition);
            } catch (BreakException e) {
            } catch (URISyntaxException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            yieldDefinition.signalComplete();
        });
        collectorThread.setDaemon(true);
        collectorThread.start();
        yieldDefinition.onClose(collectorThread::interrupt);
        return yieldDefinition.iterator();
    }


    class YieldDefinition<T> implements Iterable<T>, ClosableIterator<T> {
        private final SynchronousQueue<Message<T>> dataChannel = new SynchronousQueue<>();
        private final SynchronousQueue<FlowControl> flowChannel = new SynchronousQueue<>();
        private final AtomicReference<Optional<T>> currentValue = new AtomicReference<>(Optional.empty());
        private List<Runnable> toRunOnClose = new CopyOnWriteArrayList<>();

        public void returning(T value) {
            publish(value);
            waitUntilNextValueRequested();
        }

        public void breaking() {
            throw new BreakException();
        }

        @Override
        public ClosableIterator<T> iterator() {
            return this;
        }

        @Override
        public boolean hasNext() {
            calculateNextValue();
            Message<T> message = unchecked(() -> dataChannel.take());
            if (message instanceof Completed) return false;
            currentValue.set(message.value());
            return true;
        }

        @Override
        public T next() {
            try {
                ifAbsent(currentValue.get()).then(this::hasNext);
                return currentValue.get().get();
            } finally {
                currentValue.set(Optional.empty());
            }
        }

        public void signalComplete() {
            unchecked(() -> this.dataChannel.put(completed()));
        }

        public void waitUntilFirstValueRequested() {
            waitUntilNextValueRequested();
        }

        private void waitUntilNextValueRequested() {
            unchecked(() -> flowChannel.take());
        }

        private void publish(T value) {
            unchecked(() -> dataChannel.put(message(value)));
        }

        private void calculateNextValue() {
            unchecked(() -> flowChannel.put(youMayProceed));
        }

        @Override
        public void close() {
            toRunOnClose.forEach(Runnable::run);
        }

        public void onClose(Runnable onClose) {
            this.toRunOnClose.add(onClose);
        }

        @Override
        protected void finalize() throws Throwable {
            close();
            super.finalize();
        }
    }

    interface Message<T> {
        Optional<T> value();

        static <T> Message<T> message(T value) {
            return () -> Optional.of(value);
        }
    }

    interface Completed<T> extends Message<T> {
        static <T> Completed<T> completed() {
            return () -> Optional.empty();
        }
    }

    interface FlowControl {
        static FlowControl youMayProceed = new FlowControl() {
        };
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
}

