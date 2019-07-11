package com.phase.czq.signalpanel;

import android.util.Log;

import java.security.PublicKey;

public abstract class PipeLine {
    abstract  public void sendMessage(String message);

    abstract  public boolean isReceived();

    abstract public byte[] getReceive();

    abstract  public void close();

    abstract public boolean open();
}
