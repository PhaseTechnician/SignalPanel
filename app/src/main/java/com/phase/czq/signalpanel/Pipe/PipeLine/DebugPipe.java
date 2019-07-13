package com.phase.czq.signalpanel.Pipe.PipeLine;

import android.util.Log;

public class DebugPipe extends PipeLine {
    @Override
    public void sendMessage(String message) {
        Log.i("DebugPipeLine",message);
    }

    @Override
    public boolean isReceived() {
        return false;
    }

    @Override
    public byte[] getReceive() {
        return new byte[0];
    }

    @Override
    public void close() {

    }

    @Override
    public boolean open() {
        return false;
    }
}
