/**
 */
package it.naturtalent.design.model.design;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Layer</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link it.naturtalent.design.model.design.Layer#getName <em>Name</em>}</li>
 *   <li>{@link it.naturtalent.design.model.design.Layer#getShapeFactoryName <em>Shape Factory Name</em>}</li>
 *   <li>{@link it.naturtalent.design.model.design.Layer#getShapes <em>Shapes</em>}</li>
 * </ul>
 *
 * @see it.naturtalent.design.model.design.DesignsPackage#getLayer()
 * @model
 * @generated
 */
public interface Layer extends EObject
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
	 * @see it.naturtalent.design.model.design.DesignsPackage#getLayer_Name()
	 * @model
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link it.naturtalent.design.model.design.Layer#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * Returns the value of the '<em><b>Shape Factory Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Shape Factory Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Shape Factory Name</em>' attribute.
	 * @see #setShapeFactoryName(String)
	 * @see it.naturtalent.design.model.design.DesignsPackage#getLayer_ShapeFactoryName()
	 * @model
	 * @generated
	 */
	String getShapeFactoryName();

	/**
	 * Sets the value of the '{@link it.naturtalent.design.model.design.Layer#getShapeFactoryName <em>Shape Factory Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Shape Factory Name</em>' attribute.
	 * @see #getShapeFactoryName()
	 * @generated
	 */
	void setShapeFactoryName(String value);

	/**
	 * Returns the value of the '<em><b>Shapes</b></em>' containment reference list.
	 * The list contents are of type {@link it.naturtalent.design.model.design.Shape}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Shapes</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Shapes</em>' containment reference list.
	 * @see it.naturtalent.design.model.design.DesignsPackage#getLayer_Shapes()
	 * @model containment="true"
	 * @generated
	 */
	EList<Shape> getShapes();

} // Layer
