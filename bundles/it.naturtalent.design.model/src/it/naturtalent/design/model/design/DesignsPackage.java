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
	 * The feature id for the '<em><b>IProject ID</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DESIGN_GROUP__IPROJECT_ID = 1;

	/**
	 * The feature id for the '<em><b>Designs</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DESIGN_GROUP__DESIGNS = 2;

	/**
	 * The number of structural features of the '<em>Design Group</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DESIGN_GROUP_FEATURE_COUNT = 3;

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
	 * The feature id for the '<em><b>Layers</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DESIGN__LAYERS = 4;

	/**
	 * The number of structural features of the '<em>Design</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DESIGN_FEATURE_COUNT = 5;

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
	 * The feature id for the '<em><b>Layersets</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PAGE__LAYERSETS = 2;

	/**
	 * The number of structural features of the '<em>Page</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PAGE_FEATURE_COUNT = 3;

	/**
	 * The number of operations of the '<em>Page</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PAGE_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link it.naturtalent.design.model.design.impl.LayerImpl <em>Layer</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see it.naturtalent.design.model.design.impl.LayerImpl
	 * @see it.naturtalent.design.model.design.impl.DesignsPackageImpl#getLayer()
	 * @generated
	 */
	int LAYER = 4;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LAYER__NAME = 0;

	/**
	 * The feature id for the '<em><b>Shape Factory Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LAYER__SHAPE_FACTORY_NAME = 1;

	/**
	 * The feature id for the '<em><b>Shapes</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LAYER__SHAPES = 2;

	/**
	 * The number of structural features of the '<em>Layer</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LAYER_FEATURE_COUNT = 3;

	/**
	 * The number of operations of the '<em>Layer</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LAYER_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link it.naturtalent.design.model.design.impl.LayerSetImpl <em>Layer Set</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see it.naturtalent.design.model.design.impl.LayerSetImpl
	 * @see it.naturtalent.design.model.design.impl.DesignsPackageImpl#getLayerSet()
	 * @generated
	 */
	int LAYER_SET = 5;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LAYER_SET__NAME = 0;

	/**
	 * The feature id for the '<em><b>Layers</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LAYER_SET__LAYERS = 1;

	/**
	 * The number of structural features of the '<em>Layer Set</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LAYER_SET_FEATURE_COUNT = 2;

	/**
	 * The number of operations of the '<em>Layer Set</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LAYER_SET_OPERATION_COUNT = 0;


	/**
	 * The meta object id for the '{@link it.naturtalent.design.model.design.impl.ShapeImpl <em>Shape</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see it.naturtalent.design.model.design.impl.ShapeImpl
	 * @see it.naturtalent.design.model.design.impl.DesignsPackageImpl#getShape()
	 * @generated
	 */
	int SHAPE = 6;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SHAPE__NAME = 0;

	/**
	 * The feature id for the '<em><b>Class</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SHAPE__CLASS = 1;

	/**
	 * The feature id for the '<em><b>X</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SHAPE__X = 2;

	/**
	 * The feature id for the '<em><b>Y</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SHAPE__Y = 3;

	/**
	 * The feature id for the '<em><b>Width</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SHAPE__WIDTH = 4;

	/**
	 * The feature id for the '<em><b>Height</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SHAPE__HEIGHT = 5;

	/**
	 * The number of structural features of the '<em>Shape</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SHAPE_FEATURE_COUNT = 6;

	/**
	 * The number of operations of the '<em>Shape</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SHAPE_OPERATION_COUNT = 0;


	/**
	 * The meta object id for the '{@link it.naturtalent.design.model.design.impl.ShapeTypeImpl <em>Shape Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see it.naturtalent.design.model.design.impl.ShapeTypeImpl
	 * @see it.naturtalent.design.model.design.impl.DesignsPackageImpl#getShapeType()
	 * @generated
	 */
	int SHAPE_TYPE = 7;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SHAPE_TYPE__NAME = 0;

	/**
	 * The number of structural features of the '<em>Shape Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SHAPE_TYPE_FEATURE_COUNT = 1;

	/**
	 * The number of operations of the '<em>Shape Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SHAPE_TYPE_OPERATION_COUNT = 0;


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
	 * Returns the meta object for the attribute '{@link it.naturtalent.design.model.design.DesignGroup#getIProjectID <em>IProject ID</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>IProject ID</em>'.
	 * @see it.naturtalent.design.model.design.DesignGroup#getIProjectID()
	 * @see #getDesignGroup()
	 * @generated
	 */
	EAttribute getDesignGroup_IProjectID();

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
	 * Returns the meta object for the containment reference list '{@link it.naturtalent.design.model.design.Design#getLayers <em>Layers</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Layers</em>'.
	 * @see it.naturtalent.design.model.design.Design#getLayers()
	 * @see #getDesign()
	 * @generated
	 */
	EReference getDesign_Layers();

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
	 * Returns the meta object for the containment reference list '{@link it.naturtalent.design.model.design.Page#getLayersets <em>Layersets</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Layersets</em>'.
	 * @see it.naturtalent.design.model.design.Page#getLayersets()
	 * @see #getPage()
	 * @generated
	 */
	EReference getPage_Layersets();

	/**
	 * Returns the meta object for class '{@link it.naturtalent.design.model.design.Layer <em>Layer</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Layer</em>'.
	 * @see it.naturtalent.design.model.design.Layer
	 * @generated
	 */
	EClass getLayer();

	/**
	 * Returns the meta object for the attribute '{@link it.naturtalent.design.model.design.Layer#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see it.naturtalent.design.model.design.Layer#getName()
	 * @see #getLayer()
	 * @generated
	 */
	EAttribute getLayer_Name();

	/**
	 * Returns the meta object for the attribute '{@link it.naturtalent.design.model.design.Layer#getShapeFactoryName <em>Shape Factory Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Shape Factory Name</em>'.
	 * @see it.naturtalent.design.model.design.Layer#getShapeFactoryName()
	 * @see #getLayer()
	 * @generated
	 */
	EAttribute getLayer_ShapeFactoryName();

	/**
	 * Returns the meta object for the containment reference list '{@link it.naturtalent.design.model.design.Layer#getShapes <em>Shapes</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Shapes</em>'.
	 * @see it.naturtalent.design.model.design.Layer#getShapes()
	 * @see #getLayer()
	 * @generated
	 */
	EReference getLayer_Shapes();

	/**
	 * Returns the meta object for class '{@link it.naturtalent.design.model.design.LayerSet <em>Layer Set</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Layer Set</em>'.
	 * @see it.naturtalent.design.model.design.LayerSet
	 * @generated
	 */
	EClass getLayerSet();

	/**
	 * Returns the meta object for the attribute '{@link it.naturtalent.design.model.design.LayerSet#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see it.naturtalent.design.model.design.LayerSet#getName()
	 * @see #getLayerSet()
	 * @generated
	 */
	EAttribute getLayerSet_Name();

	/**
	 * Returns the meta object for the reference list '{@link it.naturtalent.design.model.design.LayerSet#getLayers <em>Layers</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Layers</em>'.
	 * @see it.naturtalent.design.model.design.LayerSet#getLayers()
	 * @see #getLayerSet()
	 * @generated
	 */
	EReference getLayerSet_Layers();

	/**
	 * Returns the meta object for class '{@link it.naturtalent.design.model.design.Shape <em>Shape</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Shape</em>'.
	 * @see it.naturtalent.design.model.design.Shape
	 * @generated
	 */
	EClass getShape();

	/**
	 * Returns the meta object for the attribute '{@link it.naturtalent.design.model.design.Shape#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see it.naturtalent.design.model.design.Shape#getName()
	 * @see #getShape()
	 * @generated
	 */
	EAttribute getShape_Name();

	/**
	 * Returns the meta object for the attribute '{@link it.naturtalent.design.model.design.Shape#getClass_ <em>Class</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Class</em>'.
	 * @see it.naturtalent.design.model.design.Shape#getClass_()
	 * @see #getShape()
	 * @generated
	 */
	EAttribute getShape_Class();

	/**
	 * Returns the meta object for the attribute '{@link it.naturtalent.design.model.design.Shape#getX <em>X</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>X</em>'.
	 * @see it.naturtalent.design.model.design.Shape#getX()
	 * @see #getShape()
	 * @generated
	 */
	EAttribute getShape_X();

	/**
	 * Returns the meta object for the attribute '{@link it.naturtalent.design.model.design.Shape#getY <em>Y</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Y</em>'.
	 * @see it.naturtalent.design.model.design.Shape#getY()
	 * @see #getShape()
	 * @generated
	 */
	EAttribute getShape_Y();

	/**
	 * Returns the meta object for the attribute '{@link it.naturtalent.design.model.design.Shape#getWidth <em>Width</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Width</em>'.
	 * @see it.naturtalent.design.model.design.Shape#getWidth()
	 * @see #getShape()
	 * @generated
	 */
	EAttribute getShape_Width();

	/**
	 * Returns the meta object for the attribute '{@link it.naturtalent.design.model.design.Shape#getHeight <em>Height</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Height</em>'.
	 * @see it.naturtalent.design.model.design.Shape#getHeight()
	 * @see #getShape()
	 * @generated
	 */
	EAttribute getShape_Height();

	/**
	 * Returns the meta object for class '{@link it.naturtalent.design.model.design.ShapeType <em>Shape Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Shape Type</em>'.
	 * @see it.naturtalent.design.model.design.ShapeType
	 * @generated
	 */
	EClass getShapeType();

	/**
	 * Returns the meta object for the attribute '{@link it.naturtalent.design.model.design.ShapeType#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see it.naturtalent.design.model.design.ShapeType#getName()
	 * @see #getShapeType()
	 * @generated
	 */
	EAttribute getShapeType_Name();

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
		 * The meta object literal for the '<em><b>IProject ID</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DESIGN_GROUP__IPROJECT_ID = eINSTANCE.getDesignGroup_IProjectID();

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
		 * The meta object literal for the '<em><b>Layers</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DESIGN__LAYERS = eINSTANCE.getDesign_Layers();

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
		 * The meta object literal for the '<em><b>Layersets</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PAGE__LAYERSETS = eINSTANCE.getPage_Layersets();

		/**
		 * The meta object literal for the '{@link it.naturtalent.design.model.design.impl.LayerImpl <em>Layer</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see it.naturtalent.design.model.design.impl.LayerImpl
		 * @see it.naturtalent.design.model.design.impl.DesignsPackageImpl#getLayer()
		 * @generated
		 */
		EClass LAYER = eINSTANCE.getLayer();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute LAYER__NAME = eINSTANCE.getLayer_Name();

		/**
		 * The meta object literal for the '<em><b>Shape Factory Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute LAYER__SHAPE_FACTORY_NAME = eINSTANCE.getLayer_ShapeFactoryName();

		/**
		 * The meta object literal for the '<em><b>Shapes</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference LAYER__SHAPES = eINSTANCE.getLayer_Shapes();

		/**
		 * The meta object literal for the '{@link it.naturtalent.design.model.design.impl.LayerSetImpl <em>Layer Set</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see it.naturtalent.design.model.design.impl.LayerSetImpl
		 * @see it.naturtalent.design.model.design.impl.DesignsPackageImpl#getLayerSet()
		 * @generated
		 */
		EClass LAYER_SET = eINSTANCE.getLayerSet();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute LAYER_SET__NAME = eINSTANCE.getLayerSet_Name();

		/**
		 * The meta object literal for the '<em><b>Layers</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference LAYER_SET__LAYERS = eINSTANCE.getLayerSet_Layers();

		/**
		 * The meta object literal for the '{@link it.naturtalent.design.model.design.impl.ShapeImpl <em>Shape</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see it.naturtalent.design.model.design.impl.ShapeImpl
		 * @see it.naturtalent.design.model.design.impl.DesignsPackageImpl#getShape()
		 * @generated
		 */
		EClass SHAPE = eINSTANCE.getShape();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SHAPE__NAME = eINSTANCE.getShape_Name();

		/**
		 * The meta object literal for the '<em><b>Class</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SHAPE__CLASS = eINSTANCE.getShape_Class();

		/**
		 * The meta object literal for the '<em><b>X</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SHAPE__X = eINSTANCE.getShape_X();

		/**
		 * The meta object literal for the '<em><b>Y</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SHAPE__Y = eINSTANCE.getShape_Y();

		/**
		 * The meta object literal for the '<em><b>Width</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SHAPE__WIDTH = eINSTANCE.getShape_Width();

		/**
		 * The meta object literal for the '<em><b>Height</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SHAPE__HEIGHT = eINSTANCE.getShape_Height();

		/**
		 * The meta object literal for the '{@link it.naturtalent.design.model.design.impl.ShapeTypeImpl <em>Shape Type</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see it.naturtalent.design.model.design.impl.ShapeTypeImpl
		 * @see it.naturtalent.design.model.design.impl.DesignsPackageImpl#getShapeType()
		 * @generated
		 */
		EClass SHAPE_TYPE = eINSTANCE.getShapeType();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SHAPE_TYPE__NAME = eINSTANCE.getShapeType_Name();

	}

} //DesignsPackage
