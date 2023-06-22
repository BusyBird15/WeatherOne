package com.google.appinventor.components.runtime.util;

import java.util.ArrayList;
import java.util.List;

public class PlayerListDelta {
    public static PlayerListDelta NO_CHANGE = new PlayerListDelta(new ArrayList(), new ArrayList());
    private List<String> playersAdded;
    private List<String> playersRemoved;

    public PlayerListDelta(List<String> playersRemoved2, List<String> playersAdded2) {
        this.playersRemoved = playersRemoved2;
        this.playersAdded = playersAdded2;
    }

    public List<String> getPlayersAdded() {
        return this.playersAdded;
    }

    public List<String> getPlayersRemoved() {
        return this.playersRemoved;
    }
}
