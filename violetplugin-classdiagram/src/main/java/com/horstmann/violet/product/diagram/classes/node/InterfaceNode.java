package com.horstmann.violet.product.diagram.classes.node;

import java.awt.Color;
import java.awt.geom.Point2D;

import com.horstmann.violet.framework.graphics.Separator;
import com.horstmann.violet.framework.graphics.content.ContentBackground;
import com.horstmann.violet.framework.graphics.content.ContentBorder;
import com.horstmann.violet.framework.graphics.content.ContentInsideShape;
import com.horstmann.violet.framework.graphics.content.TextContent;
import com.horstmann.violet.framework.graphics.content.VerticalLayout;
import com.horstmann.violet.framework.graphics.shape.ContentInsideRectangle;
import com.horstmann.violet.product.diagram.abstracts.node.AbstractNode;
import com.horstmann.violet.product.diagram.abstracts.node.INode;
import com.horstmann.violet.product.diagram.classes.ClassDiagramConstant;
import com.horstmann.violet.product.diagram.common.node.PointNode;
import com.horstmann.violet.product.diagram.property.text.LineText;
import com.horstmann.violet.product.diagram.property.text.MultiLineText;
import com.horstmann.violet.product.diagram.property.text.SingleLineText;
import com.horstmann.violet.product.diagram.property.text.decorator.LargeSizeDecorator;
import com.horstmann.violet.product.diagram.property.text.decorator.OneLineText;
import com.horstmann.violet.product.diagram.property.text.decorator.PrefixDecorator;
import com.horstmann.violet.product.diagram.property.text.decorator.RemoveSentenceDecorator;
import com.horstmann.violet.product.diagram.property.text.decorator.UnderlineDecorator;

/**
 * An interface node in a class diagram.
 */
public class InterfaceNode extends AbstractNode
{
    /**
     * Construct an interface node with a default size and the text <<interface>>.
     */
    public InterfaceNode()
    {
        super();

        name = new SingleLineText(nameConverter);
        methods = new MultiLineText(methodsConverter);
        createContentStructure();
    }

    protected InterfaceNode(InterfaceNode node) throws CloneNotSupportedException
    {
        super(node);
        name = node.name.clone();
        methods = node.methods.clone();
        createContentStructure();
    }

    @Override
    protected void beforeReconstruction()
    {
        super.beforeReconstruction();
        if(null == name)
        {
            name = new SingleLineText();
        }
        if(null == methods)
        {
            methods = new MultiLineText();
        }
        name.reconstruction(nameConverter);
        methods.reconstruction(methodsConverter);
    }

    @Override
    protected INode copy() throws CloneNotSupportedException
    {
        return new InterfaceNode(this);
    }

    @Override
    protected void createContentStructure()
    {
        name.setText(name.toEdit());

        TextContent nameContent = new TextContent(name);
        nameContent.setMinHeight(MIN_NAME_HEIGHT);
        nameContent.setMinWidth(MIN_WIDTH);
        TextContent methodsContent = new TextContent(methods);

        VerticalLayout verticalGroupContent = new VerticalLayout();
        verticalGroupContent.add(nameContent);
        verticalGroupContent.add(methodsContent);
        separator = new Separator.LineSeparator(getBorderColor());
        verticalGroupContent.setSeparator(separator);

        ContentInsideShape contentInsideShape = new ContentInsideRectangle(verticalGroupContent);

        setBorder(new ContentBorder(contentInsideShape, getBorderColor()));
        setBackground(new ContentBackground(getBorder(), getBackgroundColor()));
        setContent(getBackground());
    }

    @Override
    public void setBorderColor(Color borderColor)
    {
        if(null != separator)
        {
            separator.setColor(borderColor);
        }
        super.setBorderColor(borderColor);
    }

    @Override
    public void setTextColor(Color textColor)
    {
        name.setTextColor(textColor);
        methods.setTextColor(textColor);
    }

    @Override
    public String getToolTip()
    {
        return ClassDiagramConstant.CLASS_DIAGRAM_RESOURCE.getString("tooltip.interface_node");
    }

    @Override
    public boolean addChild(INode node, Point2D point)
    {
        if (node instanceof PointNode)
        {
            return true;
        }
        return false;
    }

    /**
     * Sets the name property value.
     * 
     * @param newValue the interface name
     */
    public void setName(LineText newValue)
    {
        name.setText(newValue);
    }

    /**
     * Gets the name property value.
     * 
     * @return the interface name
     */
    public LineText getName()
    {
        return name;
    }

    /**
     * Sets the methods property value.
     * 
     * @param newValue the methods of this interface
     */
    public void setMethods(LineText newValue)
    {
        methods.setText(newValue);
    }

    /**
     * Gets the methods property value.
     * 
     * @return the methods of this interface
     */
    public LineText getMethods()
    {
        return methods;
    }



    private SingleLineText name;
    private MultiLineText methods;

    private transient Separator separator = null;

    private static final int MIN_NAME_HEIGHT = 45;
    private static final int MIN_WIDTH = 100;
    private static final String STATIC = "<<static>>";

    private static LineText.Converter nameConverter = new LineText.Converter()
    {
        @Override
        public OneLineText toLineString(String rawText)
        {
            String text = rawText;
            if (rawText != null && rawText.length() > 0) {
                text = Character.toUpperCase(rawText.charAt(0)) + rawText.substring(1);
            }
            return new PrefixDecorator( new LargeSizeDecorator(new OneLineText(text)), "<center>&laquo;interface&raquo;</center>");
        }
    };
    private static final LineText.Converter methodsConverter = new LineText.Converter()
    {
        @Override
        public OneLineText toLineString(String text)
        {
            OneLineText lineString = new OneLineText(text);

            if(lineString.contains(STATIC))
            {
                lineString = new UnderlineDecorator(new RemoveSentenceDecorator(lineString, STATIC));
            }
            return lineString;
        }
    };
}
