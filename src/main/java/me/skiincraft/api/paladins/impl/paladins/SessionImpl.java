package me.skiincraft.api.paladins.impl.paladins;

import me.skiincraft.api.paladins.Paladins;
import me.skiincraft.api.paladins.internal.logging.PaladinsLogger;
import me.skiincraft.api.paladins.internal.session.EndPoint;
import me.skiincraft.api.paladins.internal.session.Session;
import org.slf4j.Logger;

import javax.annotation.Nullable;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

/**
 * <h1>Session</h1>
 *
 * <p>This class represents a session that was created.</p>
 * <p>Contains information about the session and Endpoint</p>
 *
 * @see Paladins
 */
public class SessionImpl implements Session {

    /**
     * This ID is used to worker names.
     */
    final static AtomicInteger nextSerialNumber = new AtomicInteger(0);
    private final String sessionId;
    private final String timeStamp;
    private final String requestMessage;
    private final EndPoint endPoint;
    private final Paladins paladins;
    private final Logger logger;
    private Timer timer;
    private Consumer<Session> validating;
    private boolean testresponse;

    /**
     * <h1>Session</h1>
     *
     * <p>This class represents a session that was created.</p>
     * <p>Contains information about the session and Endpoint</p>
     *
     * @param paladins       Is the API instance
     * @param requestMessage Is the ret_msg
     * @param sessionId      Is the sessionId
     * @param timeStamp      Is the timestamp in UTC
     * @see Paladins
     */
    public SessionImpl(String sessionId, String timeStamp, String requestMessage, Paladins paladins) {
        this.sessionId = sessionId;
        this.timeStamp = timeStamp;
        this.requestMessage = requestMessage;
        this.paladins = paladins;
        this.testresponse = true;

        endPoint = new EndpointImpl(this);
        this.logger = PaladinsLogger.getLogger(Session.class);
        worker();
    }

    /**
     * Generates an Id used in Worker.
     */
    private static int serialNumber() {
        return nextSerialNumber.getAndIncrement();
    }

    @Override
    public Consumer<Session> getOnValidating() {
        return validating;
    }

    @Override
    public void setOnValidating(@Nullable Consumer<Session> onValidating) {
        this.validating = onValidating;
    }

    public EndPoint getEndPoint() {
        return endPoint;
    }

    public Paladins getPaladins() {
        return paladins;
    }

    public String getRequestMessage() {
        return requestMessage;
    }

    public String getSessionId() {
        return sessionId;
    }

    public Timer getTimer() {
        return timer;
    }

    public boolean isValid() {
        return testresponse;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    /**
     * <h1>Session Worker</h1>
     * <p>This method will be a timer used to see if the session is still valid.</p>
     */
    private void worker() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        final int worker = serialNumber();
        final Session api = this;
        timer = new Timer("SessionWorker-" + worker);
        timer.schedule(new TimerTask() {

            public void run() {
                logger.debug("Checking if the session is still valid");
                testresponse = testSession().get();
                if (testresponse) {
                    if (validating != null) {
                        validating.accept(api);
                    }
                    return;
                }
                logger.warn("Session [{}] is no longer valid, and will be removed from the storage.", sessionId);
            }
        }, TimeUnit.MINUTES.toMillis(10), TimeUnit.MINUTES.toMillis(14));
    }

    @Override
    public String toString() {
        return "Session{" +
                "sessionId='" + sessionId + '\'' +
                '}';
    }
}
