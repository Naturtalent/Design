/**
 */
package it.naturtalent.design.model.design;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Design</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link it.naturtalent.design.model.design.Design#getName <em>Name</em>}</li>
 *   <li>{@link it.naturtalent.design.model.design.Design#getScaleDenominator <em>Scale Denominator</em>}</li>
 *   <li>{@link it.naturtalent.design.model.design.Design#getDesignURL <em>Design URL</em>}</li>
 *   <li>{@link it.naturtalent.design.model.design.Design#getPages <em>Pages</em>}</li>
 * </ul>
 *
 * @see it.naturtalent.design.model.design.DesignsPackage#getDesign()
 * @model
 * @generated
 */
public interface Design extends EObject
{
	/**
	 * Returns the value of the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see #setName(String)
	 * @see it.naturtalent.design.model.design.DesignsPackage#getDesign_Name()
	 * @model
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link it.naturtalent.design.model.design.Design#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * Returns the value of the '<em><b>Scale Denominator</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Scale Denominator</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Scale Denominator</em>' attribute.
	 * @see #setScaleDenominator(int)
	 * @see it.naturtalent.design.model.design.DesignsPackage#getDesign_ScaleDenominator()
	 * @model
	 * @generated
	 */
	int getScaleDenominator();

	/**
	 * Sets the value of the '{@link it.naturtalent.design.model.design.Design#getScaleDenominator <em>Scale Denominator</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Scale Denominator</em>' attribute.
	 * @see #getScaleDenominator()
	 * @generated
	 */
	void setScaleDenominator(int value);

	/**
	 * Returns the value of the '<em><b>Design URL</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Design URL</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Design URL</em>' attribute.
	 * @see #setDesignURL(String)
	 * @see it.naturtalent.design.model.design.DesignsPackage#getDesign_DesignURL()
	 * @model
	 * @generated
	 */
	String getDesignURL();

	/**
	 * Sets the value of the '{@link it.naturtalent.design.model.design.Design#getDesignURL <em>Design URL</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Design URL</em>' attribute.
	 * @see #getDesignURL()
	 * @generated
	 */
	void setDesignURL(String value);

	/**
	 * Returns the value of the '<em><b>Pages</b></em>' containment reference list.
	 * The list contents are of type {@link it.naturtalent.design.model.design.Page}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Pages</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Pages</em>' containment reference list.
	 * @see it.naturtalent.design.model.design.DesignsPackage#getDesign_Pages()
	 * @model containment="true"
	 * @generated
	 */
	EList<Page> getPages();

} // Design
