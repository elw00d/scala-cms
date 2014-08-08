//package ru;
//
//import org.zkoss.zul.AbstractTreeModel;
//import org.zkoss.zul.ext.TreeSelectableModel;
//
//public class TreeVm extends AbstractTreeModel<Integer> implements TreeSelectableModel {
//
//    public TreeVm(  ) {
//        super(1);
//    }
//
//    // TreeModel //
//    public Integer getChild(Integer parent, int index) {
//        return 4;
//    }
//
//    public int getChildCount(Integer parent) {
//        return 10;
//    }
//
//    @Override
//    public boolean isLeaf( Integer node ) {
//        return false;
//    }
//
//
//    /**
//     * @since 5.0.6
//     * @see org.zkoss.zul.TreeModel#getIndexOfChild(java.lang.Object,
//     *      java.lang.Object)
//     */
//    @Override
//    public int getIndexOfChild(Integer arg0, Integer arg1) {
//        return 0;
//    }
//
//}