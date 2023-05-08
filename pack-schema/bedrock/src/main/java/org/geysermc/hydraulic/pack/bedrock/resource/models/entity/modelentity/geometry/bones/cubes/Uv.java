package org.geysermc.hydraulic.pack.bedrock.resource.models.entity.modelentity.geometry.bones.cubes;

import org.geysermc.hydraulic.pack.bedrock.resource.models.entity.modelentity.geometry.bones.cubes.uv.Down;
import org.geysermc.hydraulic.pack.bedrock.resource.models.entity.modelentity.geometry.bones.cubes.uv.East;
import org.geysermc.hydraulic.pack.bedrock.resource.models.entity.modelentity.geometry.bones.cubes.uv.North;
import org.geysermc.hydraulic.pack.bedrock.resource.models.entity.modelentity.geometry.bones.cubes.uv.South;
import org.geysermc.hydraulic.pack.bedrock.resource.models.entity.modelentity.geometry.bones.cubes.uv.Up;
import org.geysermc.hydraulic.pack.bedrock.resource.models.entity.modelentity.geometry.bones.cubes.uv.West;

public class Uv {
  public North north;

  public South south;

  public East east;

  public West west;

  public Up up;

  public Down down;

  public North getNorth() {
    return this.north;
  }

  public void setNorth(North north) {
    this.north = north;
  }

  public South getSouth() {
    return this.south;
  }

  public void setSouth(South south) {
    this.south = south;
  }

  public East getEast() {
    return this.east;
  }

  public void setEast(East east) {
    this.east = east;
  }

  public West getWest() {
    return this.west;
  }

  public void setWest(West west) {
    this.west = west;
  }

  public Up getUp() {
    return this.up;
  }

  public void setUp(Up up) {
    this.up = up;
  }

  public Down getDown() {
    return this.down;
  }

  public void setDown(Down down) {
    this.down = down;
  }
}
