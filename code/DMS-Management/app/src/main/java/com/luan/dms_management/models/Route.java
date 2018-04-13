package com.luan.dms_management.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by luan.nt on 9/6/2017.
 */

public class Route extends RealmObject {
    @PrimaryKey
    private String RouteCode;
    private String RouteName;

    public String getRouteCode() {
        return RouteCode;
    }

    public void setRouteCode(String routeCode) {
        RouteCode = routeCode;
    }

    public String getRouteName() {
        return RouteName;
    }

    public void setRouteName(String routeName) {
        RouteName = routeName;
    }
}
