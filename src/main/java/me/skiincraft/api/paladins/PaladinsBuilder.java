package me.skiincraft.api.paladins;

/**
 * <h1>Paladins Builder</h1>
 * <p>This class builds the Paladins API class.</p>
 * <p>The constructor of this class is the same as:</p>
 * <br><code>Paladins.getInstance()
 * <br>.setAuthkey(authKey)<br>
 * .setDevId(devId)</code></br>
 *
 * @see Paladins
 */
public class PaladinsBuilder {

    private String authKey;
    private int devId;

    /**
     * <h1>Paladins Builder</h1>
     * <p>This class builds the Paladins API class.</p>
     * <p>The constructor of this class is the same as:</p>
     * <br><code>Paladins.getInstance()
     * <br>.setAuthkey(authKey)<br>
     * .setDevId(devId)</code></br>
     *
     * @see Paladins
     */
    public PaladinsBuilder() {
    }

    public PaladinsBuilder(String authKey, int devId) {
        this.authKey = authKey;
        this.devId = devId;
    }

    public int getDevId() {
        return devId;
    }

    public PaladinsBuilder setDevId(int devId) {
        this.devId = devId;
        return this;
    }

    public String getAuthKey() {
        return authKey;
    }

    public PaladinsBuilder setAuthKey(String authKey) {
        this.authKey = authKey;
        return this;
    }

    public Paladins build() {
        return new Paladins(devId, authKey);
    }
}