/**
 */
package it.naturtalent.design.model.design;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Layer Set</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link it.naturtalent.design.model.design.LayerSet#getName <em>Name</em>}</li>
 *   <li>{@link it.naturtalent.design.model.design.LayerSet#getLayers <em>Layers</em>}</li>
 * </ul>
 *
 * @see it.naturtalent.design.model.design.DesignsPackage#getLayerSet()
 * @model
 * @generated
 */
public interface LayerSet extends EObject
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
	 * @see it.naturtalent.design.model.design.DesignsPackage#getLayerSet_Name()
	 * @model
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link it.naturtalent.design.model.design.LayerSet#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * Returns the value of the '<em><b>Layers</b></em>' reference list.
	 * The list contents are of type {@link it.naturtalent.design.model.design.Layer}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Layers</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Layers</em>' reference list.
	 * @see it.naturtalent.design.model.design.DesignsPackage#getLayerSet_Layers()
	 * @model
	 * @generated
	 */
	EList<Layer> getLayers();

} // LayerSet
