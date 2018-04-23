/**
 */
package it.naturtalent.design.model.design.impl;

import it.naturtalent.design.model.design.DesignsPackage;
import it.naturtalent.design.model.design.Layer;

import it.naturtalent.design.model.design.Shape;
import java.util.Collection;
import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Layer</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link it.naturtalent.design.model.design.impl.LayerImpl#getName <em>Name</em>}</li>
 *   <li>{@link it.naturtalent.design.model.design.impl.LayerImpl#getShapeFactoryName <em>Shape Factory Name</em>}</li>
 *   <li>{@link it.naturtalent.design.model.design.impl.LayerImpl#getShapes <em>Shapes</em>}</li>
 * </ul>
 *
 * @generated
 */
public class LayerImpl extends MinimalEObjectImpl.Container implements Layer
{
	/**
	 * The default value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected static final String NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected String name = NAME_EDEFAULT;

	/**
	 * The default value of the '{@link #getShapeFactoryName() <em>Shape Factory Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getShapeFactoryName()
	 * @generated
	 * @ordered
	 */
	protected static final String SHAPE_FACTORY_NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getShapeFactoryName() <em>Shape Factory Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getShapeFactoryName()
	 * @generated
	 * @ordered
	 */
	protected String shapeFactoryName = SHAPE_FACTORY_NAME_EDEFAULT;

	/**
	 * The cached value of the '{@link #getShapes() <em>Shapes</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getShapes()
	 * @generated
	 * @ordered
	 */
	protected EList<Shape> shapes;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected LayerImpl()
	{
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass()
	{
		return DesignsPackage.Literals.LAYER;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setName(String newName)
	{
		String oldName = name;
		name = newName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DesignsPackage.LAYER__NAME, oldName, name));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getShapeFactoryName()
	{
		return shapeFactoryName;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setShapeFactoryName(String newShapeFactoryName)
	{
		String oldShapeFactoryName = shapeFactoryName;
		shapeFactoryName = newShapeFactoryName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DesignsPackage.LAYER__SHAPE_FACTORY_NAME, oldShapeFactoryName, shapeFactoryName));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Shape> getShapes()
	{
		if (shapes == null)
		{
			shapes = new EObjectContainmentEList<Shape>(Shape.class, this, DesignsPackage.LAYER__SHAPES);
		}
		return shapes;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs)
	{
		switch (featureID)
		{
			case DesignsPackage.LAYER__SHAPES:
				return ((InternalEList<?>)getShapes()).basicRemove(otherEnd, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType)
	{
		switch (featureID)
		{
			case DesignsPackage.LAYER__NAME:
				return getName();
			case DesignsPackage.LAYER__SHAPE_FACTORY_NAME:
				return getShapeFactoryName();
			case DesignsPackage.LAYER__SHAPES:
				return getShapes();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue)
	{
		switch (featureID)
		{
			case DesignsPackage.LAYER__NAME:
				setName((String)newValue);
				return;
			case DesignsPackage.LAYER__SHAPE_FACTORY_NAME:
				setShapeFactoryName((String)newValue);
				return;
			case DesignsPackage.LAYER__SHAPES:
				getShapes().clear();
				getShapes().addAll((Collection<? extends Shape>)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID)
	{
		switch (featureID)
		{
			case DesignsPackage.LAYER__NAME:
				setName(NAME_EDEFAULT);
				return;
			case DesignsPackage.LAYER__SHAPE_FACTORY_NAME:
				setShapeFactoryName(SHAPE_FACTORY_NAME_EDEFAULT);
				return;
			case DesignsPackage.LAYER__SHAPES:
				getShapes().clear();
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID)
	{
		switch (featureID)
		{
			case DesignsPackage.LAYER__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
			case DesignsPackage.LAYER__SHAPE_FACTORY_NAME:
				return SHAPE_FACTORY_NAME_EDEFAULT == null ? shapeFactoryName != null : !SHAPE_FACTORY_NAME_EDEFAULT.equals(shapeFactoryName);
			case DesignsPackage.LAYER__SHAPES:
				return shapes != null && !shapes.isEmpty();
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString()
	{
		if (eIsProxy()) return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (name: ");
		result.append(name);
		result.append(", shapeFactoryName: ");
		result.append(shapeFactoryName);
		result.append(')');
		return result.toString();
	}

} //LayerImpl
