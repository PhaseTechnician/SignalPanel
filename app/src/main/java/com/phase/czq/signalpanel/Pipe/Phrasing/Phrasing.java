package com.phase.czq.signalpanel.Pipe.Phrasing;

import java.util.ArrayList;
import java.util.List;

public abstract class Phrasing {

    protected StringBuffer buffer;
    abstract public List<String> phraseMessage(String message);
}
