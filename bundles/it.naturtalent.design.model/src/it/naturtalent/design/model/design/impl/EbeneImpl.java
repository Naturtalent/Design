/**
 */
package it.naturtalent.design.model.design.impl;

import it.naturtalent.design.model.design.DesignsPackage;
import it.naturtalent.design.model.design.Ebene;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Ebene</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link it.naturtalent.design.model.design.impl.EbeneImpl#getName <em>Name</em>}</li>
 *   <li>{@link it.naturtalent.design.model.design.impl.EbeneImpl#getShapeName <em>Shape Name</em>}</li>
 * </ul>
 *
 * @generated
 */
public class EbeneImpl extends MinimalEObjectImpl.Container implements Ebene
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
	 * The default value of the '{@link #getShapeName() <em>Shape Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getShapeName()
	 * @generated
	 * @ordered
	 */
	protected static final String SHAPE_NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getShapeName() <em>Shape Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getShapeName()
	 * @generated
	 * @ordered
	 */
	protected String shapeName = SHAPE_NAME_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EbeneImpl()
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
		return DesignsPackage.Literals.EBENE;
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
			eNotify(new ENotificationImpl(this, Notification.SET, DesignsPackage.EBENE__NAME, oldName, name));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getShapeName()
	{
		return shapeName;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setShapeName(String newShapeName)
	{
		String oldShapeName = shapeName;
		shapeName = newShapeName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DesignsPackage.EBENE__SHAPE_NAME, oldShapeName, shapeName));
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
			case DesignsPackage.EBENE__NAME:
				return getName();
			case DesignsPackage.EBENE__SHAPE_NAME:
				return getShapeName();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eSet(int featureID, Object newValue)
	{
		switch (featureID)
		{
			case DesignsPackage.EBENE__NAME:
				setName((String)newValue);
				return;
			case DesignsPackage.EBENE__SHAPE_NAME:
				setShapeName((String)newValue);
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
			case DesignsPackage.EBENE__NAME:
				setName(NAME_EDEFAULT);
				return;
			case DesignsPackage.EBENE__SHAPE_NAME:
				setShapeName(SHAPE_NAME_EDEFAULT);
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
			case DesignsPackage.EBENE__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
			case DesignsPackage.EBENE__SHAPE_NAME:
				return SHAPE_NAME_EDEFAULT == null ? shapeName != null : !SHAPE_NAME_EDEFAULT.equals(shapeName);
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
		result.append(", shapeName: ");
		result.append(shapeName);
		result.append(')');
		return result.toString();
	}

} //EbeneImpl
