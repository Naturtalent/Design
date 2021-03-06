/**
 */
package it.naturtalent.design.model.design.impl;

import it.naturtalent.design.model.design.DesignsPackage;
import it.naturtalent.design.model.design.LayerSet;
import it.naturtalent.design.model.design.Page;

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
 * An implementation of the model object '<em><b>Page</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link it.naturtalent.design.model.design.impl.PageImpl#getName <em>Name</em>}</li>
 *   <li>{@link it.naturtalent.design.model.design.impl.PageImpl#getScaleDenominator <em>Scale Denominator</em>}</li>
 *   <li>{@link it.naturtalent.design.model.design.impl.PageImpl#getLayersets <em>Layersets</em>}</li>
 * </ul>
 *
 * @generated
 */
public class PageImpl extends MinimalEObjectImpl.Container implements Page
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
	 * The default value of the '{@link #getScaleDenominator() <em>Scale Denominator</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getScaleDenominator()
	 * @generated
	 * @ordered
	 */
	protected static final int SCALE_DENOMINATOR_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getScaleDenominator() <em>Scale Denominator</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getScaleDenominator()
	 * @generated
	 * @ordered
	 */
	protected int scaleDenominator = SCALE_DENOMINATOR_EDEFAULT;

	/**
	 * The cached value of the '{@link #getLayersets() <em>Layersets</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLayersets()
	 * @generated
	 * @ordered
	 */
	protected EList<LayerSet> layersets;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected PageImpl()
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
		return DesignsPackage.Literals.PAGE;
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
			eNotify(new ENotificationImpl(this, Notification.SET, DesignsPackage.PAGE__NAME, oldName, name));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getScaleDenominator()
	{
		return scaleDenominator;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setScaleDenominator(int newScaleDenominator)
	{
		int oldScaleDenominator = scaleDenominator;
		scaleDenominator = newScaleDenominator;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DesignsPackage.PAGE__SCALE_DENOMINATOR, oldScaleDenominator, scaleDenominator));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<LayerSet> getLayersets()
	{
		if (layersets == null)
		{
			layersets = new EObjectContainmentEList<LayerSet>(LayerSet.class, this, DesignsPackage.PAGE__LAYERSETS);
		}
		return layersets;
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
			case DesignsPackage.PAGE__LAYERSETS:
				return ((InternalEList<?>)getLayersets()).basicRemove(otherEnd, msgs);
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
			case DesignsPackage.PAGE__NAME:
				return getName();
			case DesignsPackage.PAGE__SCALE_DENOMINATOR:
				return getScaleDenominator();
			case DesignsPackage.PAGE__LAYERSETS:
				return getLayersets();
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
			case DesignsPackage.PAGE__NAME:
				setName((String)newValue);
				return;
			case DesignsPackage.PAGE__SCALE_DENOMINATOR:
				setScaleDenominator((Integer)newValue);
				return;
			case DesignsPackage.PAGE__LAYERSETS:
				getLayersets().clear();
				getLayersets().addAll((Collection<? extends LayerSet>)newValue);
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
			case DesignsPackage.PAGE__NAME:
				setName(NAME_EDEFAULT);
				return;
			case DesignsPackage.PAGE__SCALE_DENOMINATOR:
				setScaleDenominator(SCALE_DENOMINATOR_EDEFAULT);
				return;
			case DesignsPackage.PAGE__LAYERSETS:
				getLayersets().clear();
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
			case DesignsPackage.PAGE__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
			case DesignsPackage.PAGE__SCALE_DENOMINATOR:
				return scaleDenominator != SCALE_DENOMINATOR_EDEFAULT;
			case DesignsPackage.PAGE__LAYERSETS:
				return layersets != null && !layersets.isEmpty();
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
		result.append(", scaleDenominator: ");
		result.append(scaleDenominator);
		result.append(')');
		return result.toString();
	}

} //PageImpl
