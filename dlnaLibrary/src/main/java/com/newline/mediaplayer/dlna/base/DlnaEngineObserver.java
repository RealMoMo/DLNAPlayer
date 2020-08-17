package com.newline.mediaplayer.dlna.base;

public interface DlnaEngineObserver {
    public void initEngine();
    public boolean startEngine();
    public boolean stopEngine();
    public boolean restartEngine();

}
