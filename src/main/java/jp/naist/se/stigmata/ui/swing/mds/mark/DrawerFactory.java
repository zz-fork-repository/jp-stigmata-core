package jp.naist.se.stigmata.ui.swing.mds.mark;

import java.util.HashMap;
import java.util.Map;

import jp.naist.se.stigmata.ui.swing.mds.GeometoryType;
import jp.naist.se.stigmata.ui.swing.mds.MarkDrawer;

public class DrawerFactory{
    private static final DrawerFactory instance = new DrawerFactory();

    private Map<GeometoryType, MarkDrawer> drawers = new HashMap<GeometoryType, MarkDrawer>();

    private DrawerFactory(){
        drawers.put(GeometoryType.CROSS,                  new CrossDrawer());
        drawers.put(GeometoryType.XMARK,                  new XMarkDrawer());
        drawers.put(GeometoryType.STAR,                   new StarDrawer());
        drawers.put(GeometoryType.CIRCLE,                 new CircleDrawer());
        drawers.put(GeometoryType.FILLED_CIRCLE,          new CircleDrawer(true));
        drawers.put(GeometoryType.RHOMBUS,                new RhombusDrawer());
        drawers.put(GeometoryType.FILLED_RHOMBUS,         new RhombusDrawer(true));
        drawers.put(GeometoryType.RECTANGLE,              new RectangleDrawer());
        drawers.put(GeometoryType.FILLED_RECTANGLE,       new RectangleDrawer(true));
        drawers.put(GeometoryType.UPPER_TRIANGLE,         new UpperTriangleDrawer());
        drawers.put(GeometoryType.FILLED_UPPER_TRIANGLE,  new UpperTriangleDrawer(true));
        drawers.put(GeometoryType.DOWNER_TRIANGLE,        new DownerTriangleDrawer());
        drawers.put(GeometoryType.FILLED_DOWNER_TRIANGLE, new DownerTriangleDrawer(true));
    }

    public static DrawerFactory getInstance(){
        return instance;
    }

    public MarkDrawer create(GeometoryType type){
        return drawers.get(type);
    }
}
