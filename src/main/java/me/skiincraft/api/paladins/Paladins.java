package me.skiincraft.api.paladins;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.skiincraft.api.paladins.exceptions.ContextException;
import me.skiincraft.api.paladins.exceptions.RequestException;
import me.skiincraft.api.paladins.impl.paladins.SessionImpl;
import me.skiincraft.api.paladins.storage.impl.PaladinsStorageImpl;
import me.skiincraft.api.paladins.internal.logging.PaladinsLogger;
import me.skiincraft.api.paladins.internal.requests.APIRequest;
import me.skiincraft.api.paladins.internal.requests.impl.DefaultAPIRequest;
import me.skiincraft.api.paladins.internal.requests.impl.FakeAPIRequest;
import me.skiincraft.api.paladins.internal.session.Session;
import me.skiincraft.api.paladins.json.SessionJsonAdapter;
import me.skiincraft.api.paladins.objects.AccessUtils;
import me.skiincraft.api.paladins.objects.miscellany.DataUsed;
import me.skiincraft.api.paladins.storage.PaladinsStorage;
import okhttp3.OkHttpClient;
import org.slf4j.Logger;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * <h1>Paladins</h1>
 * <p>This class is the main API class, it will keep information like Storage and Token.</p>
 * <p>This class can only be instantiated once. To catch the instance use <code>Paladins.getInstance()</code></p>
 *
 * @see PaladinsBuilder
 */
public class Paladins {

    private static Paladins instance;
    private final AccessUtils accessUtils;
    private final List<Session> sessions;
    private OkHttpClient client;
    private final Logger logger;

    private int devId;
    private String authkey;

    private Paladins() {
        this.accessUtils = new AccessUtils(this);
        this.sessions = new ArrayList<>();
        this.client = new OkHttpClient();
        this.logger = PaladinsLogger.getLogger(Paladins.class);
    }

    /**
     * <h1>Instance</h1>
     * <p>Method to get the instance of Paladins API</p>
     */
    public static Paladins getInstance() {
        if (instance == null) {
            PaladinsLogger.getLogger(Paladins.class).debug("Creating a new instance of the Paladins class");
            instance = new Paladins();
        }
        return instance;
    }

    public Logger getLogger() {
        return logger;
    }

    /**
     * <p>Method used to create a new session</p>
     * <p>
     * <h3>Read the official API documentation</h3>
     * <p>https://docs.google.com/document/d/1OFS-3ocSx-1Rvg4afAnEHlT3917MAK_6eJTR6rzr-BM/view#heading=h.e3t1kvy4m4n3</p>
     * </p>
     *
     * @throws RequestException It will raise an exception in case something is wrong.
     * @see Session
     */
    public synchronized APIRequest<Session> createSession() throws RequestException {
        return new DefaultAPIRequest<>("createsession", null, (response) -> {
            try {
                Session session = new GsonBuilder().registerTypeAdapter(SessionImpl.class, new SessionJsonAdapter(this)).create()
                        .fromJson(Objects.requireNonNull(response.body(), "response is null")
                                .string(), SessionImpl.class);
                sessions.add(session);
                logger.info("A new session has been created {}", session.getSessionId());
                return session;
            } catch (IOException e) {
                logger.error("There was a problem connecting with the API:", e);
                e.printStackTrace();
                return null;
            }
        }, this);
    }

    /**
     * <p>Method used to check if a session is still valid</p>
     * <p>
     * <h3>Read the official API documentation</h3>
     * <p>https://docs.google.com/document/d/1OFS-3ocSx-1Rvg4afAnEHlT3917MAK_6eJTR6rzr-BM/view#heading=h.e3t1kvy4m4n3</p>
     * </p>
     *
     * @param sessionId The session id to be tested
     * @throws RequestException Will throw an exception if the session is invalid
     */
    public synchronized APIRequest<Boolean> testSession(String sessionId) {
        return new DefaultAPIRequest<>("testsession", sessionId, null, (response) -> {
            try {
                String json = Objects.requireNonNull(response.body(), "response is null").string();
                if (AccessUtils.checkResponse(json)) {
                    logger.info("TestSession: Session {} is still valid", sessionId);
                    return true;
                }
                Stream<Session> activeSessions = sessions.stream().filter((session) -> session.getSessionId().equalsIgnoreCase(sessionId));
                if (activeSessions.findAny().isPresent()) {
                    logger.warn("[{}] Sessions have been removed for being invalid.", activeSessions.count());
                    logger.debug("Session [{}] is invalid", sessionId);
                    sessions.removeAll(activeSessions.collect(Collectors.toList()));
                }
                throw new RequestException(json, json);
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }, this);
    }

    /**
     * <h1>ResumeSession</h1>
     * <p>This method will be used to resume a session.</p>
     * <p>It will use the testSession() method to verify the session.
     * <br>If it is valid, a new Session instance will be created.</br>
     * </p>
     *
     * @param sessionId The session id to be resumed
     * @throws RequestException Will throw an exception if the session is invalid.
     */
    public synchronized APIRequest<Session> resumeSession(String sessionId) {
        if (testSession(sessionId).get()) {
            logger.info("Session [{}] was successfully resumed", sessionId);
            return new FakeAPIRequest<>(new SessionImpl(sessionId, null, null, this), 200);
        }
        throw new RequestException("You tried to resume an invalid session.");
    }

    /**
     * <h1>DataUsed</h1>
     * <p>This method is used to check how much has been consumed from the API</p>
     *
     * @param sessionId The active session
     * @throws RequestException Will throw an exception if the session is invalid.
     */
    public synchronized APIRequest<DataUsed> getDataUsed(String sessionId) {
        return new DefaultAPIRequest<>("getdataused", sessionId, null, (response) -> {
            try {
                String json = Objects.requireNonNull(response.body(), "response is null").string();
                if (AccessUtils.checkResponse(json)) {
                    return new Gson().fromJson(json, DataUsed[].class)[0];
                }
                throw new RequestException(json, json);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }, this);
    }

    /**
     * <h1>DataUsed</h1>
     * <p>This method is used to check how much has been consumed from the API</p>
     *
     * @param session The active session
     * @throws RequestException Will throw an exception if the session is invalid.
     */
    public synchronized APIRequest<DataUsed> getDataUsed(Session session) {
        return getDataUsed(session.getSessionId());
    }

    /**
     * @return The storage such as Matches, Champions and Cards.
     */
    public PaladinsStorage getStorage() {
        return PaladinsStorageImpl.getInstance();
    }

    /**
     * Are the utils used in the API, this class does not interfere negatively in the functioning of the API
     */
    public AccessUtils getAccessUtils() {
        return accessUtils;
    }

    /**
     * <p>This method will return a copy of the list of active sessions</p>
     * <p>Remember that it is a copy, removing any object from this list will not delete the session</p>
     */
    public List<Session> getSessions() {
        return new ArrayList<>(sessions);
    }

    /**
     * @return The API Developer ID present in this instance.
     */
    public int getDevId() {
        return devId;
    }

    /**
     * <p>Define DevId in this instance</p>
     * <p>It cannot be changed if there is an active session</p>
     *
     * @param devId The Developer Id
     * @throws ContextException If you have an active session.
     */
    public Paladins setDevId(int devId) {
        if (sessions.size() != 0)
            throw new ContextException("You cannot change the data after a session has already been created!");
        this.devId = devId;
        return this;
    }

    /**
     * @return The API authentication key present in this instance.
     */
    public String getAuthkey() {
        return authkey;
    }

    /**
     * <p>Define Authkey in this instance</p>
     * <p>It cannot be changed if there is an active session</p>
     *
     * @param authkey The API access token
     * @throws ContextException If you have an active session.
     */
    public Paladins setAuthkey(String authkey) {
        if (sessions.size() != 0) {
            throw new ContextException("You cannot change the data after a session has already been created!");
        }
        this.authkey = authkey;
        return this;
    }

    public OkHttpClient getClient() {
        return client;
    }

    public Paladins setClient(@Nonnull OkHttpClient client) {
        this.client = client;
        return this;
    }

    @Override
    public String toString() {
        return "Paladins{" +
                "sessions=" + sessions.size() +
                ", devId=" + devId +
                ", authkey='" + authkey + '\'' +
                '}';
    }
}
