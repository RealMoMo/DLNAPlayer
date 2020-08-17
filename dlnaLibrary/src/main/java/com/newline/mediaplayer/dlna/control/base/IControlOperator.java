package com.newline.mediaplayer.dlna.control.base;

import com.newline.mediaplayer.dlna.control.ControlPointImpl;

public interface IControlOperator {
    public  int getControlStatus();
    public  void setControlStauts(int stauts);
    public void setControlPoint(ControlPointImpl controlPoint);

}
