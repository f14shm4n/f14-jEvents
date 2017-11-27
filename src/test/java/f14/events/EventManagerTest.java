package f14.events;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by f14shm4n on 15.11.2017.
 */

public class EventManagerTest {

    private static int sEventCalls = 0;
    private static int sEventErrors = 0;

    private class EventArg1 extends BaseEventArgument {
    }

    private class EventArg2 extends BaseEventArgument {
    }

    private class EventHandler_1 implements IEventHandler<EventArg1> {
        @Override
        public void handle(EventArg1 arg) {
            sEventCalls++;
            System.out.println("Handle from " + getClass().getSimpleName());
        }
    }

    private class EventHandler_2 implements IEventHandler<EventArg2> {
        @Override
        public void handle(EventArg2 arg) {
            sEventCalls++;
            System.out.println("Handle from " + getClass().getSimpleName());
        }
    }

    private class ErrorGenHandler implements IEventHandler<EventArg1> {
        @Override
        public void handle(EventArg1 arg) {
            sEventCalls++;
            System.out.println("Handle from " + getClass().getSimpleName());

            sEventErrors++;
            throw new NullPointerException();
        }
    }

    private interface InterfaceEventHandler extends IEventHandler<EventArg1> {
    }

    @Test
    public void NotifySpecifiedHandler() {
        EventManager manager = new EventManager();

        manager.add(new EventHandler_1());
        manager.add(new EventHandler_1());
        manager.add(new EventHandler_2());

        manager.notify(EventHandler_1.class, null);

        Assert.assertEquals(2, sEventCalls);
    }

    @Test
    public void NotifyAnonymousHandler() {
        EventManager pool = new EventManager();

        pool.add(new EventHandler_1());
        pool.add(new EventHandler_1());
        pool.add(new EventHandler_2());
        pool.add(new IEventHandler() {
            @Override
            public void handle(IEventArgument arg) {
                sEventCalls++;
                System.out.println("Handle from " + getClass().getSimpleName());
            }
        });

        pool.notify(IEventHandler.class, new EventArg2());

        System.out.println("Fired count: " + sEventCalls);

        // Call EventHandler_2 and anonymous instance
        Assert.assertEquals(2, sEventCalls);
    }

    @Test
    public void RemoveEventHandler() {
        EventManager manager = new EventManager();

        IEventHandler eh1 = new EventHandler_1();

        manager.add(eh1);
        manager.add(new EventHandler_1());
        manager.add(new EventHandler_2());

        manager.remove(eh1);

        Assert.assertEquals(2, manager.size());
    }

    @Test
    public void RemoveEventHolder() {
        EventManager manager = new EventManager();

        EventHolder holder = manager.add(new EventHandler_1());

        manager.add(new EventHandler_1());
        manager.add(new EventHandler_2());

        manager.remove(holder);

        Assert.assertEquals(2, manager.size());
    }

    @Test
    public void EventHolder_ErrorHandler() {
        EventManager manager = new EventManager();

        manager.add(new ErrorGenHandler())
                .error(e -> {
                    System.out.println("Ex: " + e.getMessage());
                    Assert.assertTrue(e instanceof NullPointerException);
                });
        manager.add(new EventHandler_1());
        manager.add(new EventHandler_2());

        manager.notify(ErrorGenHandler.class, new EventArg1());

        Assert.assertEquals(1, sEventCalls);
        Assert.assertEquals(1, sEventErrors);
    }

    @Test
    public void EventHolder_ErrorStrategy_REMOVE() {
        EventManager manager = new EventManager();

        manager.add(new ErrorGenHandler())
                .error(e -> {
                    System.out.println("Ex: " + e.getMessage());
                    Assert.assertTrue(e instanceof NullPointerException);
                })
                .errorStrategy(ErrorStrategy.REMOVE);

        manager.add(new EventHandler_1());
        manager.add(new EventHandler_2());

        System.out.println("Size before: " + manager.size());

        manager.notify(ErrorGenHandler.class, new EventArg1());

        System.out.println("Size after: " + manager.size());

        Assert.assertEquals(1, sEventCalls);
        Assert.assertEquals(1, sEventErrors);
        Assert.assertEquals(2, manager.size());
    }
}
