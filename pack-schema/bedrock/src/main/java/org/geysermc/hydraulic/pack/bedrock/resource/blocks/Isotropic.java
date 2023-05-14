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

  public void setDown(boolean down) {
    this.down = down;
  }

  public Boolean getUp() {
    return this.up;
  }

  public void setUp(boolean up) {
    this.up = up;
  }

  public Boolean getSide() {
    return this.side;
  }

  public void setSide(boolean side) {
    this.side = side;
  }

  public Boolean getSouth() {
    return this.south;
  }

  public void setSouth(boolean south) {
    this.south = south;
  }

  public Boolean getNorth() {
    return this.north;
  }

  public void setNorth(boolean north) {
    this.north = north;
  }

  public Boolean getWest() {
    return this.west;
  }

  public void setWest(boolean west) {
    this.west = west;
  }

  public Boolean getEast() {
    return this.east;
  }

  public void setEast(boolean east) {
    this.east = east;
  }
}
