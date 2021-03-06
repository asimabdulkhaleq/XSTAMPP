/*******************************************************************************
 * Copyright (c) 2013, 2017 A-STPA Stupro Team Uni Stuttgart (Lukas Balzer, Adam Grahovac, Jarkko
 * Heidenwag, Benedikt Markt, Jaqueline Patzek, Sebastian Sieber, Fabian Toth, Patrick
 * Wickenhäuser, Aliaksei Babkovich, Aleksander Zotov).
 * 
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 *******************************************************************************/

package xstampp.astpa.model.controlstructure.components;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.eclipse.draw2d.geometry.Point;

import xstampp.astpa.model.controlstructure.interfaces.IConnection;
import xstampp.astpa.model.controlstructure.interfaces.IRectangleComponent;

/**
 * This class contains functions necessary for connection model
 * 
 * @author Lukas Balzer, Aliaksei Babkovich, Fabian Toth
 */
@XmlRootElement(name = "connection")
@XmlAccessorType(XmlAccessType.NONE)
public class CSConnection implements IConnection {

  @XmlElement
  private UUID id;

  @XmlElement
  private ConnectionType connectionType;

  @XmlElement
  private Anchor sourceAnchor;

  @XmlElement
  private Anchor targetAnchor;

  @XmlElement
  private List<Point> bendPoints;

  /**
   * Constructs a new connection
   * 
   * @author Lukas Balzer, Fabian Toth
   * 
   * @param sourceAnchor
   *          The anchor where connection begins
   * @param targetAnchor
   *          The anchor where the connection ends
   * @param connectionType
   *          The type of connection
   */
  public CSConnection(Anchor sourceAnchor, Anchor targetAnchor, ConnectionType connectionType) {
    this.id = UUID.randomUUID();
    this.sourceAnchor = sourceAnchor;
    this.targetAnchor = targetAnchor;
    this.connectionType = connectionType;
    this.bendPoints = null;
  }

  /**
   * Empty constructor used for JAXB. Do not use it!
   * 
   * @author Fabian Toth
   */
  public CSConnection() {
  }

  @Override
  public Anchor getSourceAnchor() {
    return this.sourceAnchor;
  }

  /**
   * @param sourceAnchor
   *          the sourceFigureId to set
   */
  public void setSourceAnchor(Anchor sourceAnchor) {
    this.sourceAnchor = sourceAnchor;
  }

  @Override
  public Anchor getTargetAnchor() {
    return this.targetAnchor;
  }

  /**
   * @param targetAnchor
   *          the targetFigureId to set
   */
  public void setTargetAnchor(Anchor target) {
    this.targetAnchor = target;
  }

  @Override
  public ConnectionType getConnectionType() {
    return this.connectionType;
  }

  /**
   * @param connectionType
   *          the connectionType to set
   */
  public void setConnectionType(ConnectionType connectionType) {
    this.connectionType = connectionType;
  }

  @Override
  public ComponentType getComponentType() {
    return ComponentType.CONNECTION;
  }

  @Override
  public UUID getId() {
    return this.id;
  }

  /**
   * @param id
   *          the id to set
   */
  public void setId(UUID id) {
    this.id = id;
  }

  /**
   * Checks if the connection connects the component with the given id
   * 
   * @author Fabian Toth
   * 
   * @param componentId
   *          the id of the component
   * @return true if this connection connects the component
   */
  public boolean connectsComponent(UUID componentId) {
    return this.sourceAnchor.getOwnerId().equals(componentId)
        || this.targetAnchor.getOwnerId().equals(componentId);
  }

  @Override
  public List<IRectangleComponent> getChildren() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public int getChildCount() {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public List<IRectangleComponent> getChildren(boolean step0) {
    // TODO Auto-generated method stub
    return null;
  }

  public boolean addBendPoint(int x, int y) {
    if (this.bendPoints == null) {
      this.bendPoints = new ArrayList<>();
    }
    return this.bendPoints.add(new Point(x, y));
  }

  public boolean removeBendPoint(int x, int y) {
    boolean contains = this.bendPoints.contains(new Point(x, y));
    boolean result = this.bendPoints.remove(new Point(x, y));
    if (this.bendPoints.isEmpty()) {
      this.bendPoints = null;
    }
    return result;
  }

  public List<Point> getBendPoints() {

    ArrayList<Point> arrayList = new ArrayList<Point>();
    if (bendPoints != null) {
      arrayList.addAll(bendPoints);
    }
    return arrayList;
  }
}
