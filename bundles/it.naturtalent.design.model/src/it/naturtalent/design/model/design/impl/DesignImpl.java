/**
 */
package it.naturtalent.design.model.design.impl;

import it.naturtalent.design.model.design.Design;
import it.naturtalent.design.model.design.DesignsPackage;
import it.naturtalent.design.model.design.Layer;
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
 * An implementation of the model object '<em><b>Design</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link it.naturtalent.design.model.design.impl.DesignImpl#getName <em>Name</em>}</li>
 *   <li>{@link it.naturtalent.design.model.design.impl.DesignImpl#getScaleDenominator <em>Scale Denominator</em>}</li>
 *   <li>{@link it.naturtalent.design.model.design.impl.DesignImpl#getDesignURL <em>Design URL</em>}</li>
 *   <li>{@link it.naturtalent.design.model.design.impl.DesignImpl#getPages <em>Pages</em>}</li>
 *   <li>{@link it.naturtalent.design.model.design.impl.DesignImpl#getLayers <em>Layers</em>}</li>
 * </ul>
 *
 * @generated
 */
public class DesignImpl extends MinimalEObjectImpl.Container implements Design
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
	 * The default value of the '{@link #getDesignURL() <em>Design URL</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDesignURL()
	 * @generated
	 * @ordered
	 */
	protected static final String DESIGN_URL_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getDesignURL() <em>Design URL</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDesignURL()
	 * @generated
	 * @ordered
	 */
	protected String designURL = DESIGN_URL_EDEFAULT;

	/**
	 * The cached value of the '{@link #getPages() <em>Pages</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPages()
	 * @generated
	 * @ordered
	 */
	protected EList<Page> pages;

	/**
	 * The cached value of the '{@link #getLayers() <em>Layers</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLayers()
	 * @generated
	 * @ordered
	 */
	protected EList<Layer> layers;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected DesignImpl()
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
		return DesignsPackage.Literals.DESIGN;
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
			eNotify(new ENotificationImpl(this, Notification.SET, DesignsPackage.DESIGN__NAME, oldName, name));
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
			eNotify(new ENotificationImpl(this, Notification.SET, DesignsPackage.DESIGN__SCALE_DENOMINATOR, oldScaleDenominator, scaleDenominator));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getDesignURL()
	{
		return designURL;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDesignURL(String newDesignURL)
	{
		String oldDesignURL = designURL;
		designURL = newDesignURL;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DesignsPackage.DESIGN__DESIGN_URL, oldDesignURL, designURL));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Page> getPages()
	{
		if (pages == null)
		{
			pages = new EObjectContainmentEList<Page>(Page.class, this, DesignsPackage.DESIGN__PAGES);
		}
		return pages;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Layer> getLayers()
	{
		if (layers == null)
		{
			layers = new EObjectContainmentEList<Layer>(Layer.class, this, DesignsPackage.DESIGN__LAYERS);
		}
		return layers;
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
			case DesignsPackage.DESIGN__PAGES:
				return ((InternalEList<?>)getPages()).basicRemove(otherEnd, msgs);
			case DesignsPackage.DESIGN__LAYERS:
				return ((InternalEList<?>)getLayers()).basicRemove(otherEnd, msgs);
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
			case DesignsPackage.DESIGN__NAME:
				return getName();
			case DesignsPackage.DESIGN__SCALE_DENOMINATOR:
				return getScaleDenominator();
			case DesignsPackage.DESIGN__DESIGN_URL:
				return getDesignURL();
			case DesignsPackage.DESIGN__PAGES:
				return getPages();
			case DesignsPackage.DESIGN__LAYERS:
				return getLayers();
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
			case DesignsPackage.DESIGN__NAME:
				setName((String)newValue);
				return;
			case DesignsPackage.DESIGN__SCALE_DENOMINATOR:
				setScaleDenominator((Integer)newValue);
				return;
			case DesignsPackage.DESIGN__DESIGN_URL:
				setDesignURL((String)newValue);
				return;
			case DesignsPackage.DESIGN__PAGES:
				getPages().clear();
				getPages().addAll((Collection<? extends Page>)newValue);
				return;
			case DesignsPackage.DESIGN__LAYERS:
				getLayers().clear();
				getLayers().addAll((Collection<? extends Layer>)newValue);
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
			case DesignsPackage.DESIGN__NAME:
				setName(NAME_EDEFAULT);
				return;
			case DesignsPackage.DESIGN__SCALE_DENOMINATOR:
				setScaleDenominator(SCALE_DENOMINATOR_EDEFAULT);
				return;
			case DesignsPackage.DESIGN__DESIGN_URL:
				setDesignURL(DESIGN_URL_EDEFAULT);
				return;
			case DesignsPackage.DESIGN__PAGES:
				getPages().clear();
				return;
			case DesignsPackage.DESIGN__LAYERS:
				getLayers().clear();
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
			case DesignsPackage.DESIGN__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
			case DesignsPackage.DESIGN__SCALE_DENOMINATOR:
				return scaleDenominator != SCALE_DENOMINATOR_EDEFAULT;
			case DesignsPackage.DESIGN__DESIGN_URL:
				return DESIGN_URL_EDEFAULT == null ? designURL != null : !DESIGN_URL_EDEFAULT.equals(designURL);
			case DesignsPackage.DESIGN__PAGES:
				return pages != null && !pages.isEmpty();
			case DesignsPackage.DESIGN__LAYERS:
				return layers != null && !layers.isEmpty();
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
		result.append(", designURL: ");
		result.append(designURL);
		result.append(')');
		return result.toString();
	}

} //DesignImpl
