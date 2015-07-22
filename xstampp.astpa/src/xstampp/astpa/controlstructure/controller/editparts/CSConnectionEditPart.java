/*******************************************************************************
 * Copyright (c) 2013 A-STPA Stupro Team Uni Stuttgart (Lukas Balzer, Adam
 * Grahovac, Jarkko Heidenwag, Benedikt Markt, Jaqueline Patzek, Sebastian
 * Sieber, Fabian Toth, Patrick Wickenhäuser, Aliaksei Babkovich, Aleksander
 * Zotov).
 * 
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 *******************************************************************************/

package xstampp.astpa.controlstructure.controller.editparts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.eclipse.draw2d.AbstractRouter;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.ConnectionRouter;
import org.eclipse.draw2d.IClippingStrategy;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.draw2d.geometry.Translatable;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.editparts.AbstractConnectionEditPart;
import org.eclipse.gef.editpolicies.ConnectionEndpointEditPolicy;
import org.eclipse.gef.editpolicies.FeedbackHelper;
import org.eclipse.gef.editpolicies.SnapFeedbackPolicy;
import org.eclipse.swt.SWT;

import xstampp.astpa.controlstructure.controller.policys.CSConnectionDeleteEditPolicy;
import xstampp.astpa.controlstructure.controller.policys.CSDirectEditPolicy;
import xstampp.astpa.controlstructure.controller.policys.CSEditPolicy;
import xstampp.astpa.controlstructure.figure.ConnectionFigure;
import xstampp.astpa.controlstructure.figure.IAnchorFigure;
import xstampp.astpa.haz.controlstructure.interfaces.IAnchor;
import xstampp.astpa.model.controlstructure.components.CSConnection;
import xstampp.astpa.model.controlstructure.interfaces.IConnection;
import xstampp.astpa.model.interfaces.IControlStructureEditorDataModel;

/**
 * 
 * Editpart for connections.
 * 
 * @author Aliaksei Babkovich
 * @version 1.0
 */
public class CSConnectionEditPart extends AbstractConnectionEditPart implements IRelative {

	private static final float DASH = 4;
	private IAnchorFigure targetAnchor;
	private IAnchorFigure sourceAnchor;
	private IControlStructureEditorDataModel dataModel;
	private final UUID ownID;
	private final String stepId;
	private IFigure container;
	private List<IConnectable> members;
	private PolylineConnection feedback;

	/**
	 * This constructor is used to load a connection EditPart from a given model
	 * 
	 * @author Lukas Balzer
	 * 
	 * @param model
	 *            The DataModel which contains all model classes
	 * @param source
	 *            the source anchor which is given from the Anchor Model
	 * @param target
	 *            the target anchor which is given from the Anchor Model
	 * @param id
	 *            the UUID which is given from the model
	 * @param stepId
	 *            this steps id
	 */
	public CSConnectionEditPart(IControlStructureEditorDataModel model,
			IAnchorFigure source, IAnchorFigure target, UUID id, String stepId) {
		super();
		this.members = new ArrayList<>();
		this.stepId = stepId;
		this.registerAccessibility();
		this.activate();
		this.dataModel = model;
		this.ownID = id;
		this.sourceAnchor = source;
		this.targetAnchor = target;
		

	}
	
	@Override
	protected IFigure createFigure() {

		PolylineConnection connection = new ConnectionFigure();
		connection.addMouseMotionListener(this);
		connection.setLayoutManager(new XYLayout());
		connection.setLineWidth(1);
		connection.setTolerance(15);
//		RectangleEditPart part= new RectangleEditPart(this.dataModel, this.stepId);
//		part.setParent(this);
//		Component model= new Component();
//		model.setId(getId());
//		part.setModel(model);
		switch (((CSConnection) this.getModel()).getConnectionType()) {
		case ARROW_SIMPLE: {
			connection.setLineStyle(SWT.LINE_SOLID);
			break;
		}
		case ARROW_DASHED: {
			connection.setLineDash(new float[] { CSConnectionEditPart.DASH });
			break;
		}
		default:
			return null;
		}
//		this.container = part.createFigure();
//		connection.add(this.container,new Rectangle(0,0,50,50));
//		this.getViewer().getVisualPartMap().put(this.container, part);
//		part.addNotify();
		return connection;
	}

	
	@Override
	public void refresh() {
		if (this.dataModel.getConnection(((CSConnection) this.getModel())
				.getId()) == null) {
			this.deactivate();
			this.getViewer().getEditPartRegistry().remove(this);
		} else {
			
			super.refresh();
		}
		this.refreshChildren();
		this.getViewer().getControl().redraw();
		
		
		for (Object child : this.getChildren()) {
			((IControlStructureEditPart) child).refresh();
		}
	}

	@Override
	protected List getModelChildren() {
		return new ArrayList<>();
	}
	@Override
	protected void createEditPolicies() {
		this.installEditPolicy(EditPolicy.CONNECTION_ROLE,
				new CSConnectionDeleteEditPolicy(this.dataModel, this.stepId));
		this.installEditPolicy(EditPolicy.CONNECTION_ENDPOINTS_ROLE,
				new ConnectionEndpointEditPolicy());
	}

	@Override
	public IFigure getFeedback() {
		return getFeedback(null);
	}
	@Override
	public IFigure getFeedback(IConnectable member) {
		if(this.feedback == null){
			this.feedback = new PolylineConnection();
			this.feedback.setAlpha(150);
			this.feedback.setAntialias(SWT.ON);
			this.feedback.setLineWidth(4);
			this.feedback.setForegroundColor(ColorConstants.darkGreen);
		}
		this.feedback.setVisible(true);
		
		if(member != null){
			this.feedback.setConnectionRouter(new FeedbackRouter(member));
			updateFeedback();
		}else{
			this.feedback.setPoints(getConnectionFigure().getPoints());
		}
		return this.feedback;
	}
	
	@Override
	protected ConnectionAnchor getTargetConnectionAnchor() {
		IAnchor target = this.dataModel.getConnection(this.ownID)
				.getTargetAnchor();
		this.targetAnchor.updateAnchor(target);
		this.getFigure().revalidate();
		return this.targetAnchor;
	}

	@Override
	protected ConnectionAnchor getSourceConnectionAnchor() {
		IAnchor source = this.dataModel.getConnection(this.ownID)
				.getSourceAnchor();
		this.sourceAnchor.updateAnchor(source);
		this.getFigure().revalidate();
		return this.sourceAnchor;
	}

	/**
	 * This getter is called to get the Target Anchor the connection is
	 * momentarily connected with
	 * 
	 * @author Lukas Balzer
	 * 
	 * @return The Anchor at which the connection ends
	 */
	public ConnectionAnchor getTargetAnchor() {
		return this.targetAnchor;
	}

	/**
	 * This getter is called to get the Source Anchor the connection is
	 * momentarily connected with
	 * 
	 * @author Lukas Balzer
	 * 
	 * @return The Anchor at which the connection starts
	 */
	public ConnectionAnchor getSourceAnchor() {
		return this.sourceAnchor;
	}

	@Override
	public UUID getId() {
		return ((IConnection) this.getModel()).getId();
	}

	@Override
	public void translateToRoot(Translatable t) {
		// does nothing by default

	}
	@Override
	public void mouseDragged(MouseEvent me) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseEntered(MouseEvent me) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseExited(MouseEvent me) {
		((ConnectionFigure)getConnectionFigure()).disableFeedback();
		
	}
	@Override
	public void mouseHover(MouseEvent me) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseMoved(MouseEvent me) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void eraseFeedback() {
		if(this.feedback != null){
			this.feedback.setVisible(false);
		}
		
	}

	@Override
	public void setMember(IConnectable member) {
		this.members.add(member);
	}

	private class FeedbackRouter extends AbstractRouter{
		private IFigure figure;
		public FeedbackRouter(IConnectable member) {
			this.figure=member.getFigure();
		}
		@Override
		public void route(Connection connection) {
			PointList list = getConnectionFigure().getPoints().getCopy();
			Point a=list.removePoint(list.size()-1);
			
			Point b=list.getPoint(list.size()-1);
			Point center = this.figure.getBounds().getCenter().getCopy();
			
			for(int i=0;i<10;i++){
				Rectangle tmp = new Rectangle(center,b);
				tmp = tmp.intersect(this.figure.getBounds());
				center = tmp.getCenter().getCopy();
			}
			list.addPoint(center);
			list.addPoint(b);
			list.addPoint(a);
			connection.setPoints(list);
		}
		
	}

	@Override
	public void updateFeedback() {
		if(this.feedback != null){
			this.feedback.getConnectionRouter().route(this.feedback);
		}
	}
}

class RectangleClipping implements IClippingStrategy{
	private Rectangle clippingRect;
	public RectangleClipping(Rectangle rect) {
		this.clippingRect = rect;
	}
	@Override
	public Rectangle[] getClip(IFigure childFigure) {
		return new Rectangle[]{this.clippingRect};
	}
	
}

