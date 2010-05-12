package org.onebusaway.transit_data.model;

import java.io.Serializable;

public class TripStopTimeBean implements Serializable {

  private static final long serialVersionUID = 1L;

  private int arrivalTime;

  private int departureTime;

  private StopBean stop;

  private String stopHeadsign;

  public int getArrivalTime() {
    return arrivalTime;
  }

  public void setArrivalTime(int arrivalTime) {
    this.arrivalTime = arrivalTime;
  }

  public int getDepartureTime() {
    return departureTime;
  }

  public void setDepartureTime(int departureTime) {
    this.departureTime = departureTime;
  }

  public StopBean getStop() {
    return stop;
  }

  public void setStop(StopBean stop) {
    this.stop = stop;
  }

  public String getStopHeadsign() {
    return stopHeadsign;
  }

  public void setStopHeadsign(String stopHeadsign) {
    this.stopHeadsign = stopHeadsign;
  }
}