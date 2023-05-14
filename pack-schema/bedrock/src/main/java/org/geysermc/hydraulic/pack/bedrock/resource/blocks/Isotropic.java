package org.geysermc.hydraulic.pack.bedrock.resource.blocks;

import java.lang.Boolean;

public class Isotropic {
  public Boolean down;

  public Boolean up;

  public Boolean side;

  public Boolean south;

  public Boolean north;

  public Boolean west;

  public Boolean east;

  public Boolean getDown() {
    return this.down;
  }

  public void setDown(Boolean down) {
    this.down = down;
  }

  public Boolean getUp() {
    return this.up;
  }

  public void setUp(Boolean up) {
    this.up = up;
  }

  public Boolean getSide() {
    return this.side;
  }

  public void setSide(Boolean side) {
    this.side = side;
  }

  public Boolean getSouth() {
    return this.south;
  }

  public void setSouth(Boolean south) {
    this.south = south;
  }

  public Boolean getNorth() {
    return this.north;
  }

  public void setNorth(Boolean north) {
    this.north = north;
  }

  public Boolean getWest() {
    return this.west;
  }

  public void setWest(Boolean west) {
    this.west = west;
  }

  public Boolean getEast() {
    return this.east;
  }

  public void setEast(Boolean east) {
    this.east = east;
  }
}
