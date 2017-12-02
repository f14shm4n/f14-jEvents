package f14.events;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by f14shm4n on 02.12.2017.
 */
public class EventPoolTest {

    private static int CallCounter = 0;

    private class EArg_1 implements IEventArgument {

        @Override
        public boolean isHandled() {
            return false;
        }

        @Override
        public void setHandle() {

        }
    }

    private interface EHandler_1 extends IEventHandler<EArg_1> {

    }

    private interface EHandler_2 extends IEventHandler<EArg_1> {

    }

    @Test
    public void add() {
        EventPool<EHandler_1, EArg_1> pool = new EventPool<>();

        EHandler_1 handler_1 = arg -> {
        };
        EHandler_1 handler_1_1 = arg -> {
        };


        EventHolder holder = pool.add(handler_1);

        Assert.assertNotNull(holder);

        holder = pool.add(handler_1);

        Assert.assertNull(holder);

        holder = pool.add(handler_1_1);

        Assert.assertNotNull(holder);
        Assert.assertEquals(2, pool.size());
    }

    @Test
    public void notifyHandler() {
        EventPool<EHandler_1, EArg_1> pool = new EventPool<>();

        EHandler_1 handler_1 = arg -> {
            CallCounter++;
        };
        EHandler_1 handler_1_1 = arg -> {
            CallCounter++;
        };

        pool.add(handler_1);
        pool.add(handler_1_1);

        Assert.assertEquals(2, pool.size());

        pool.notify(null);

        Assert.assertEquals(2, CallCounter);
    }
}
