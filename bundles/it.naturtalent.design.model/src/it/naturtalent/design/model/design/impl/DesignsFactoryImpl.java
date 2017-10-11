/**
 */
package it.naturtalent.design.model.design.impl;

import it.naturtalent.design.model.design.*;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class DesignsFactoryImpl extends EFactoryImpl implements DesignsFactory
{
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static DesignsFactory init()
	{
		try
		{
			DesignsFactory theDesignsFactory = (DesignsFactory)EPackage.Registry.INSTANCE.getEFactory(DesignsPackage.eNS_URI);
			if (theDesignsFactory != null)
			{
				return theDesignsFactory;
			}
		}
		catch (Exception exception)
		{
			EcorePlugin.INSTANCE.log(exception);
		}
		return new DesignsFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DesignsFactoryImpl()
	{
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EObject create(EClass eClass)
	{
		switch (eClass.getClassifierID())
		{
			case DesignsPackage.DESIGNS: return createDesigns();
			case DesignsPackage.DESIGN_GROUP: return createDesignGroup();
			case DesignsPackage.DESIGN: return createDesign();
			case DesignsPackage.PAGE: return createPage();
			case DesignsPackage.EBENE: return createEbene();
			case DesignsPackage.ITEM: return createItem();
			case DesignsPackage.LAYER: return createLayer();
			case DesignsPackage.LAYER_SET: return createLayerSet();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Designs createDesigns()
	{
		DesignsImpl designs = new DesignsImpl();
		return designs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DesignGroup createDesignGroup()
	{
		DesignGroupImpl designGroup = new DesignGroupImpl();
		return designGroup;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Design createDesign()
	{
		DesignImpl design = new DesignImpl();
		return design;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Page createPage()
	{
		PageImpl page = new PageImpl();
		return page;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Ebene createEbene()
	{
		EbeneImpl ebene = new EbeneImpl();
		return ebene;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Item createItem()
	{
		ItemImpl item = new ItemImpl();
		return item;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Layer createLayer()
	{
		LayerImpl layer = new LayerImpl();
		return layer;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public LayerSet createLayerSet()
	{
		LayerSetImpl layerSet = new LayerSetImpl();
		return layerSet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DesignsPackage getDesignsPackage()
	{
		return (DesignsPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static DesignsPackage getPackage()
	{
		return DesignsPackage.eINSTANCE;
	}

} //DesignsFactoryImpl
