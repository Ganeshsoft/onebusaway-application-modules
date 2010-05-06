package org.onebusaway.api.actions.api.where;

import java.io.IOException;

import org.apache.struts2.rest.DefaultHttpHeaders;
import org.onebusaway.api.actions.api.ApiActionSupport;
import org.onebusaway.api.impl.MaxCountSupport;
import org.onebusaway.api.model.transit.BeanFactoryV2;
import org.onebusaway.exceptions.ServiceException;
import org.onebusaway.geospatial.model.CoordinateBounds;
import org.onebusaway.geospatial.services.SphericalGeometryLibrary;
import org.onebusaway.transit_data.model.ListBean;
import org.onebusaway.transit_data.model.TripDetailsBean;
import org.onebusaway.transit_data.model.TripsForBoundsQueryBean;
import org.onebusaway.transit_data.services.TransitDataService;
import org.springframework.beans.factory.annotation.Autowired;

public class TripsForLocationController extends ApiActionSupport {

  private static final long serialVersionUID = 1L;

  private static final int V2 = 2;

  private static final double MAX_BOUNDS_RADIUS = 20000.0;

  @Autowired
  private TransitDataService _service;

  private double _lat;

  private double _lon;

  private double _latSpan;

  private double _lonSpan;

  private long _time = 0;

  private MaxCountSupport _maxCount = new MaxCountSupport();

  private boolean _includeTrips = false;

  private boolean _includeSchedules = false;

  public TripsForLocationController() {
    super(V2);
  }

  public void setLat(double lat) {
    _lat = lat;
  }

  public void setLon(double lon) {
    _lon = lon;
  }

  public void setLatSpan(double latSpan) {
    _latSpan = latSpan;
  }

  public void setLonSpan(double lonSpan) {
    _lonSpan = lonSpan;
  }

  public void setTime(long time) {
    _time = time;
  }

  public void setMaxCount(int maxCount) {
    _maxCount.setMaxCount(maxCount);
  }

  public void setIncludeTrips(boolean includeTrips) {
    _includeTrips = includeTrips;
  }

  public void setIncludeSchedules(boolean includeSchedules) {
    _includeSchedules = includeSchedules;
  }

  public DefaultHttpHeaders index() throws IOException, ServiceException {

    if (!isVersion(V2))
      return setUnknownVersionResponse();

    if (hasErrors())
      return setValidationErrorsResponse();

    CoordinateBounds bounds = getSearchBounds();

    long time = System.currentTimeMillis();
    if (_time != 0)
      time = _time;

    TripsForBoundsQueryBean query = new TripsForBoundsQueryBean();
    query.setBounds(bounds);
    query.setTime(time);
    query.setMaxCount(_maxCount.getMaxCount());
    query.setIncludeTripBeans(_includeTrips);
    query.setIncludeTripSchedules(_includeSchedules);

    ListBean<TripDetailsBean> trips = _service.getTripsForBounds(query);

    BeanFactoryV2 factory = getBeanFactoryV2();
    return setOkResponse(factory.getTripDetailsResponse(trips));
  }

  private CoordinateBounds getSearchBounds() {

    CoordinateBounds maxBounds = SphericalGeometryLibrary.bounds(_lat, _lon,
        MAX_BOUNDS_RADIUS);
    _latSpan = Math.min(_latSpan, maxBounds.getMaxLat() - maxBounds.getMinLat());
    _lonSpan = Math.min(_lonSpan, maxBounds.getMaxLon() - maxBounds.getMinLon());

    return SphericalGeometryLibrary.boundsFromLatLonOffset(_lat, _lon,
        _latSpan / 2, _lonSpan / 2);

  }
}