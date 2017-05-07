/**
 */
package it.naturtalent.design.model.design;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Ebene</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link it.naturtalent.design.model.design.Ebene#getName <em>Name</em>}</li>
 *   <li>{@link it.naturtalent.design.model.design.Ebene#getShapeName <em>Shape Name</em>}</li>
 * </ul>
 *
 * @see it.naturtalent.design.model.design.DesignsPackage#getEbene()
 * @model
 * @generated
 */
public interface Ebene extends EObject
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
	 * @see it.naturtalent.design.model.design.DesignsPackage#getEbene_Name()
	 * @model
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link it.naturtalent.design.model.design.Ebene#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * Returns the value of the '<em><b>Shape Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Shape Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Shape Name</em>' attribute.
	 * @see #setShapeName(String)
	 * @see it.naturtalent.design.model.design.DesignsPackage#getEbene_ShapeName()
	 * @model
	 * @generated
	 */
	String getShapeName();

	/**
	 * Sets the value of the '{@link it.naturtalent.design.model.design.Ebene#getShapeName <em>Shape Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Shape Name</em>' attribute.
	 * @see #getShapeName()
	 * @generated
	 */
	void setShapeName(String value);

} // Ebene
