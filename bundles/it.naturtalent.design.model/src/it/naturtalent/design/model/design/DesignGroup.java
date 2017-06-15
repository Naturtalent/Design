/**
 */
package it.naturtalent.design.model.design;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Design Group</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link it.naturtalent.design.model.design.DesignGroup#getName <em>Name</em>}</li>
 *   <li>{@link it.naturtalent.design.model.design.DesignGroup#getIProjectID <em>IProject ID</em>}</li>
 *   <li>{@link it.naturtalent.design.model.design.DesignGroup#getDesigns <em>Designs</em>}</li>
 * </ul>
 *
 * @see it.naturtalent.design.model.design.DesignsPackage#getDesignGroup()
 * @model
 * @generated
 */
public interface DesignGroup extends EObject
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
	 * @see it.naturtalent.design.model.design.DesignsPackage#getDesignGroup_Name()
	 * @model
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link it.naturtalent.design.model.design.DesignGroup#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * Returns the value of the '<em><b>IProject ID</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>IProject ID</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>IProject ID</em>' attribute.
	 * @see #setIProjectID(String)
	 * @see it.naturtalent.design.model.design.DesignsPackage#getDesignGroup_IProjectID()
	 * @model
	 * @generated
	 */
	String getIProjectID();

	/**
	 * Sets the value of the '{@link it.naturtalent.design.model.design.DesignGroup#getIProjectID <em>IProject ID</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>IProject ID</em>' attribute.
	 * @see #getIProjectID()
	 * @generated
	 */
	void setIProjectID(String value);

	/**
	 * Returns the value of the '<em><b>Designs</b></em>' containment reference list.
	 * The list contents are of type {@link it.naturtalent.design.model.design.Design}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Designs</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Designs</em>' containment reference list.
	 * @see it.naturtalent.design.model.design.DesignsPackage#getDesignGroup_Designs()
	 * @model containment="true"
	 * @generated
	 */
	EList<Design> getDesigns();

} // DesignGroup
