/**
 */
package it.naturtalent.design.model.design;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each operation of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see it.naturtalent.design.model.design.DesignsFactory
 * @model kind="package"
 * @generated
 */
public interface DesignsPackage extends EPackage
{
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "design";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://it.naturtalent/design";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "design";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	DesignsPackage eINSTANCE = it.naturtalent.design.model.design.impl.DesignsPackageImpl.init();

	/**
	 * The meta object id for the '{@link it.naturtalent.design.model.design.impl.DesignsImpl <em>Designs</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see it.naturtalent.design.model.design.impl.DesignsImpl
	 * @see it.naturtalent.design.model.design.impl.DesignsPackageImpl#getDesigns()
	 * @generated
	 */
	int DESIGNS = 0;

	/**
	 * The feature id for the '<em><b>Designs</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DESIGNS__DESIGNS = 0;

	/**
	 * The number of structural features of the '<em>Designs</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DESIGNS_FEATURE_COUNT = 1;

	/**
	 * The number of operations of the '<em>Designs</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DESIGNS_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link it.naturtalent.design.model.design.impl.DesignGroupImpl <em>Design Group</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see it.naturtalent.design.model.design.impl.DesignGroupImpl
	 * @see it.naturtalent.design.model.design.impl.DesignsPackageImpl#getDesignGroup()
	 * @generated
	 */
	int DESIGN_GROUP = 1;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DESIGN_GROUP__NAME = 0;

	/**
	 * The feature id for the '<em><b>Designs</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DESIGN_GROUP__DESIGNS = 1;

	/**
	 * The number of structural features of the '<em>Design Group</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DESIGN_GROUP_FEATURE_COUNT = 2;

	/**
	 * The number of operations of the '<em>Design Group</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DESIGN_GROUP_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link it.naturtalent.design.model.design.impl.DesignImpl <em>Design</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see it.naturtalent.design.model.design.impl.DesignImpl
	 * @see it.naturtalent.design.model.design.impl.DesignsPackageImpl#getDesign()
	 * @generated
	 */
	int DESIGN = 2;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DESIGN__NAME = 0;

	/**
	 * The feature id for the '<em><b>Scale Denominator</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DESIGN__SCALE_DENOMINATOR = 1;

	/**
	 * The feature id for the '<em><b>Design URL</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DESIGN__DESIGN_URL = 2;

	/**
	 * The feature id for the '<em><b>Pages</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DESIGN__PAGES = 3;

	/**
	 * The number of structural features of the '<em>Design</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DESIGN_FEATURE_COUNT = 4;

	/**
	 * The number of operations of the '<em>Design</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DESIGN_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link it.naturtalent.design.model.design.impl.PageImpl <em>Page</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see it.naturtalent.design.model.design.impl.PageImpl
	 * @see it.naturtalent.design.model.design.impl.DesignsPackageImpl#getPage()
	 * @generated
	 */
	int PAGE = 3;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PAGE__NAME = 0;

	/**
	 * The feature id for the '<em><b>Scale Denominator</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PAGE__SCALE_DENOMINATOR = 1;

	/**
	 * The number of structural features of the '<em>Page</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PAGE_FEATURE_COUNT = 2;

	/**
	 * The number of operations of the '<em>Page</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PAGE_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link it.naturtalent.design.model.design.impl.EbeneImpl <em>Ebene</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see it.naturtalent.design.model.design.impl.EbeneImpl
	 * @see it.naturtalent.design.model.design.impl.DesignsPackageImpl#getEbene()
	 * @generated
	 */
	int EBENE = 4;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EBENE__NAME = 0;

	/**
	 * The feature id for the '<em><b>Shape Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EBENE__SHAPE_NAME = 1;

	/**
	 * The number of structural features of the '<em>Ebene</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EBENE_FEATURE_COUNT = 2;

	/**
	 * The number of operations of the '<em>Ebene</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EBENE_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link it.naturtalent.design.model.design.impl.ItemImpl <em>Item</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see it.naturtalent.design.model.design.impl.ItemImpl
	 * @see it.naturtalent.design.model.design.impl.DesignsPackageImpl#getItem()
	 * @generated
	 */
	int ITEM = 5;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ITEM__NAME = 0;

	/**
	 * The number of structural features of the '<em>Item</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ITEM_FEATURE_COUNT = 1;

	/**
	 * The number of operations of the '<em>Item</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ITEM_OPERATION_COUNT = 0;


	/**
	 * Returns the meta object for class '{@link it.naturtalent.design.model.design.Designs <em>Designs</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Designs</em>'.
	 * @see it.naturtalent.design.model.design.Designs
	 * @generated
	 */
	EClass getDesigns();

	/**
	 * Returns the meta object for the containment reference list '{@link it.naturtalent.design.model.design.Designs#getDesigns <em>Designs</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Designs</em>'.
	 * @see it.naturtalent.design.model.design.Designs#getDesigns()
	 * @see #getDesigns()
	 * @generated
	 */
	EReference getDesigns_Designs();

	/**
	 * Returns the meta object for class '{@link it.naturtalent.design.model.design.DesignGroup <em>Design Group</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Design Group</em>'.
	 * @see it.naturtalent.design.model.design.DesignGroup
	 * @generated
	 */
	EClass getDesignGroup();

	/**
	 * Returns the meta object for the attribute '{@link it.naturtalent.design.model.design.DesignGroup#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see it.naturtalent.design.model.design.DesignGroup#getName()
	 * @see #getDesignGroup()
	 * @generated
	 */
	EAttribute getDesignGroup_Name();

	/**
	 * Returns the meta object for the containment reference list '{@link it.naturtalent.design.model.design.DesignGroup#getDesigns <em>Designs</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Designs</em>'.
	 * @see it.naturtalent.design.model.design.DesignGroup#getDesigns()
	 * @see #getDesignGroup()
	 * @generated
	 */
	EReference getDesignGroup_Designs();

	/**
	 * Returns the meta object for class '{@link it.naturtalent.design.model.design.Design <em>Design</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Design</em>'.
	 * @see it.naturtalent.design.model.design.Design
	 * @generated
	 */
	EClass getDesign();

	/**
	 * Returns the meta object for the attribute '{@link it.naturtalent.design.model.design.Design#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see it.naturtalent.design.model.design.Design#getName()
	 * @see #getDesign()
	 * @generated
	 */
	EAttribute getDesign_Name();

	/**
	 * Returns the meta object for the attribute '{@link it.naturtalent.design.model.design.Design#getScaleDenominator <em>Scale Denominator</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Scale Denominator</em>'.
	 * @see it.naturtalent.design.model.design.Design#getScaleDenominator()
	 * @see #getDesign()
	 * @generated
	 */
	EAttribute getDesign_ScaleDenominator();

	/**
	 * Returns the meta object for the attribute '{@link it.naturtalent.design.model.design.Design#getDesignURL <em>Design URL</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Design URL</em>'.
	 * @see it.naturtalent.design.model.design.Design#getDesignURL()
	 * @see #getDesign()
	 * @generated
	 */
	EAttribute getDesign_DesignURL();

	/**
	 * Returns the meta object for the containment reference list '{@link it.naturtalent.design.model.design.Design#getPages <em>Pages</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Pages</em>'.
	 * @see it.naturtalent.design.model.design.Design#getPages()
	 * @see #getDesign()
	 * @generated
	 */
	EReference getDesign_Pages();

	/**
	 * Returns the meta object for class '{@link it.naturtalent.design.model.design.Page <em>Page</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Page</em>'.
	 * @see it.naturtalent.design.model.design.Page
	 * @generated
	 */
	EClass getPage();

	/**
	 * Returns the meta object for the attribute '{@link it.naturtalent.design.model.design.Page#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see it.naturtalent.design.model.design.Page#getName()
	 * @see #getPage()
	 * @generated
	 */
	EAttribute getPage_Name();

	/**
	 * Returns the meta object for the attribute '{@link it.naturtalent.design.model.design.Page#getScaleDenominator <em>Scale Denominator</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Scale Denominator</em>'.
	 * @see it.naturtalent.design.model.design.Page#getScaleDenominator()
	 * @see #getPage()
	 * @generated
	 */
	EAttribute getPage_ScaleDenominator();

	/**
	 * Returns the meta object for class '{@link it.naturtalent.design.model.design.Ebene <em>Ebene</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Ebene</em>'.
	 * @see it.naturtalent.design.model.design.Ebene
	 * @generated
	 */
	EClass getEbene();

	/**
	 * Returns the meta object for the attribute '{@link it.naturtalent.design.model.design.Ebene#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see it.naturtalent.design.model.design.Ebene#getName()
	 * @see #getEbene()
	 * @generated
	 */
	EAttribute getEbene_Name();

	/**
	 * Returns the meta object for the attribute '{@link it.naturtalent.design.model.design.Ebene#getShapeName <em>Shape Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Shape Name</em>'.
	 * @see it.naturtalent.design.model.design.Ebene#getShapeName()
	 * @see #getEbene()
	 * @generated
	 */
	EAttribute getEbene_ShapeName();

	/**
	 * Returns the meta object for class '{@link it.naturtalent.design.model.design.Item <em>Item</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Item</em>'.
	 * @see it.naturtalent.design.model.design.Item
	 * @generated
	 */
	EClass getItem();

	/**
	 * Returns the meta object for the attribute '{@link it.naturtalent.design.model.design.Item#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see it.naturtalent.design.model.design.Item#getName()
	 * @see #getItem()
	 * @generated
	 */
	EAttribute getItem_Name();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	DesignsFactory getDesignsFactory();

	/**
	 * <!-- begin-user-doc -->
	 * Defines literals for the meta objects that represent
	 * <ul>
	 *   <li>each class,</li>
	 *   <li>each feature of each class,</li>
	 *   <li>each operation of each class,</li>
	 *   <li>each enum,</li>
	 *   <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * @generated
	 */
	interface Literals
	{
		/**
		 * The meta object literal for the '{@link it.naturtalent.design.model.design.impl.DesignsImpl <em>Designs</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see it.naturtalent.design.model.design.impl.DesignsImpl
		 * @see it.naturtalent.design.model.design.impl.DesignsPackageImpl#getDesigns()
		 * @generated
		 */
		EClass DESIGNS = eINSTANCE.getDesigns();

		/**
		 * The meta object literal for the '<em><b>Designs</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DESIGNS__DESIGNS = eINSTANCE.getDesigns_Designs();

		/**
		 * The meta object literal for the '{@link it.naturtalent.design.model.design.impl.DesignGroupImpl <em>Design Group</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see it.naturtalent.design.model.design.impl.DesignGroupImpl
		 * @see it.naturtalent.design.model.design.impl.DesignsPackageImpl#getDesignGroup()
		 * @generated
		 */
		EClass DESIGN_GROUP = eINSTANCE.getDesignGroup();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DESIGN_GROUP__NAME = eINSTANCE.getDesignGroup_Name();

		/**
		 * The meta object literal for the '<em><b>Designs</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DESIGN_GROUP__DESIGNS = eINSTANCE.getDesignGroup_Designs();

		/**
		 * The meta object literal for the '{@link it.naturtalent.design.model.design.impl.DesignImpl <em>Design</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see it.naturtalent.design.model.design.impl.DesignImpl
		 * @see it.naturtalent.design.model.design.impl.DesignsPackageImpl#getDesign()
		 * @generated
		 */
		EClass DESIGN = eINSTANCE.getDesign();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DESIGN__NAME = eINSTANCE.getDesign_Name();

		/**
		 * The meta object literal for the '<em><b>Scale Denominator</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DESIGN__SCALE_DENOMINATOR = eINSTANCE.getDesign_ScaleDenominator();

		/**
		 * The meta object literal for the '<em><b>Design URL</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DESIGN__DESIGN_URL = eINSTANCE.getDesign_DesignURL();

		/**
		 * The meta object literal for the '<em><b>Pages</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DESIGN__PAGES = eINSTANCE.getDesign_Pages();

		/**
		 * The meta object literal for the '{@link it.naturtalent.design.model.design.impl.PageImpl <em>Page</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see it.naturtalent.design.model.design.impl.PageImpl
		 * @see it.naturtalent.design.model.design.impl.DesignsPackageImpl#getPage()
		 * @generated
		 */
		EClass PAGE = eINSTANCE.getPage();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PAGE__NAME = eINSTANCE.getPage_Name();

		/**
		 * The meta object literal for the '<em><b>Scale Denominator</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PAGE__SCALE_DENOMINATOR = eINSTANCE.getPage_ScaleDenominator();

		/**
		 * The meta object literal for the '{@link it.naturtalent.design.model.design.impl.EbeneImpl <em>Ebene</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see it.naturtalent.design.model.design.impl.EbeneImpl
		 * @see it.naturtalent.design.model.design.impl.DesignsPackageImpl#getEbene()
		 * @generated
		 */
		EClass EBENE = eINSTANCE.getEbene();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute EBENE__NAME = eINSTANCE.getEbene_Name();

		/**
		 * The meta object literal for the '<em><b>Shape Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute EBENE__SHAPE_NAME = eINSTANCE.getEbene_ShapeName();

		/**
		 * The meta object literal for the '{@link it.naturtalent.design.model.design.impl.ItemImpl <em>Item</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see it.naturtalent.design.model.design.impl.ItemImpl
		 * @see it.naturtalent.design.model.design.impl.DesignsPackageImpl#getItem()
		 * @generated
		 */
		EClass ITEM = eINSTANCE.getItem();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ITEM__NAME = eINSTANCE.getItem_Name();

	}

} //DesignsPackage
