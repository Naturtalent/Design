/**
 */
package it.naturtalent.design.model.design.impl;

import it.naturtalent.design.model.design.Design;
import it.naturtalent.design.model.design.DesignGroup;
import it.naturtalent.design.model.design.DesignsPackage;

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
 * An implementation of the model object '<em><b>Design Group</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link it.naturtalent.design.model.design.impl.DesignGroupImpl#getName <em>Name</em>}</li>
 *   <li>{@link it.naturtalent.design.model.design.impl.DesignGroupImpl#getIProjectID <em>IProject ID</em>}</li>
 *   <li>{@link it.naturtalent.design.model.design.impl.DesignGroupImpl#getDesigns <em>Designs</em>}</li>
 * </ul>
 *
 * @generated
 */
public class DesignGroupImpl extends MinimalEObjectImpl.Container implements DesignGroup
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
	 * The default value of the '{@link #getIProjectID() <em>IProject ID</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getIProjectID()
	 * @generated
	 * @ordered
	 */
	protected static final String IPROJECT_ID_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getIProjectID() <em>IProject ID</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getIProjectID()
	 * @generated
	 * @ordered
	 */
	protected String iProjectID = IPROJECT_ID_EDEFAULT;

	/**
	 * The cached value of the '{@link #getDesigns() <em>Designs</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDesigns()
	 * @generated
	 * @ordered
	 */
	protected EList<Design> designs;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected DesignGroupImpl()
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
		return DesignsPackage.Literals.DESIGN_GROUP;
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
			eNotify(new ENotificationImpl(this, Notification.SET, DesignsPackage.DESIGN_GROUP__NAME, oldName, name));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getIProjectID()
	{
		return iProjectID;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setIProjectID(String newIProjectID)
	{
		String oldIProjectID = iProjectID;
		iProjectID = newIProjectID;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DesignsPackage.DESIGN_GROUP__IPROJECT_ID, oldIProjectID, iProjectID));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Design> getDesigns()
	{
		if (designs == null)
		{
			designs = new EObjectContainmentEList<Design>(Design.class, this, DesignsPackage.DESIGN_GROUP__DESIGNS);
		}
		return designs;
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
			case DesignsPackage.DESIGN_GROUP__DESIGNS:
				return ((InternalEList<?>)getDesigns()).basicRemove(otherEnd, msgs);
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
			case DesignsPackage.DESIGN_GROUP__NAME:
				return getName();
			case DesignsPackage.DESIGN_GROUP__IPROJECT_ID:
				return getIProjectID();
			case DesignsPackage.DESIGN_GROUP__DESIGNS:
				return getDesigns();
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
			case DesignsPackage.DESIGN_GROUP__NAME:
				setName((String)newValue);
				return;
			case DesignsPackage.DESIGN_GROUP__IPROJECT_ID:
				setIProjectID((String)newValue);
				return;
			case DesignsPackage.DESIGN_GROUP__DESIGNS:
				getDesigns().clear();
				getDesigns().addAll((Collection<? extends Design>)newValue);
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
			case DesignsPackage.DESIGN_GROUP__NAME:
				setName(NAME_EDEFAULT);
				return;
			case DesignsPackage.DESIGN_GROUP__IPROJECT_ID:
				setIProjectID(IPROJECT_ID_EDEFAULT);
				return;
			case DesignsPackage.DESIGN_GROUP__DESIGNS:
				getDesigns().clear();
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
			case DesignsPackage.DESIGN_GROUP__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
			case DesignsPackage.DESIGN_GROUP__IPROJECT_ID:
				return IPROJECT_ID_EDEFAULT == null ? iProjectID != null : !IPROJECT_ID_EDEFAULT.equals(iProjectID);
			case DesignsPackage.DESIGN_GROUP__DESIGNS:
				return designs != null && !designs.isEmpty();
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
		result.append(", iProjectID: ");
		result.append(iProjectID);
		result.append(')');
		return result.toString();
	}

} //DesignGroupImpl
