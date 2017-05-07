/**
 */
package it.naturtalent.design.model.design;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Designs</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link it.naturtalent.design.model.design.Designs#getDesigns <em>Designs</em>}</li>
 * </ul>
 *
 * @see it.naturtalent.design.model.design.DesignsPackage#getDesigns()
 * @model
 * @generated
 */
public interface Designs extends EObject
{
	/**
	 * Returns the value of the '<em><b>Designs</b></em>' containment reference list.
	 * The list contents are of type {@link it.naturtalent.design.model.design.DesignGroup}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Designs</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Designs</em>' containment reference list.
	 * @see it.naturtalent.design.model.design.DesignsPackage#getDesigns_Designs()
	 * @model containment="true"
	 * @generated
	 */
	EList<DesignGroup> getDesigns();

} // Designs
