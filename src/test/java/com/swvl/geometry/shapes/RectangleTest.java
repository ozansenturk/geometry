package com.swvl.geometry.shapes;

import org.junit.Assert;
import org.junit.Test;

import javax.naming.OperationNotSupportedException;

public class RectangleTest {

    @Test
    public void expandTest() throws OperationNotSupportedException {
        Point point1 = new Point(30.4259967, 30.7862083);
        Point point2 = new Point(32.2359967, 32.5661083);

        Rectangle expandedRect = new Rectangle(Double.MAX_VALUE, Double.MAX_VALUE, Double.MIN_VALUE, Double.MIN_VALUE);
        expandedRect.expand(point1);
        expandedRect.expand(point2);

        Assert.assertTrue(expandedRect.isIntersected(point1));
        Assert.assertTrue(expandedRect.isIntersected(point2));
        Assert.assertTrue(expandedRect.contains(point1));
        Assert.assertTrue(expandedRect.contains(point2));
    }

    @Test
    public void containsTest() throws OperationNotSupportedException {
        Point point1 = new Point(30.4259967, 30.7862083);
        Point point2 = new Point(32.2359967, 32.5661083);
        Rectangle rectangle = new Rectangle(point1.clone(), point2.clone());

        Point point3 = new Point(31.0, 31.0);
        Assert.assertTrue(rectangle.contains(point1));
        Assert.assertTrue(rectangle.contains(point2));
        Assert.assertTrue(rectangle.contains(point3));


        Point point4 = new Point(33.0, 33.0);
        Point point5 = new Point(30.0, 30.0);

        Assert.assertFalse(rectangle.contains(point4));
        Assert.assertFalse(rectangle.contains(point5));
    }

    @Test
    public void intersectionTest() throws OperationNotSupportedException {
        Point point1 = new Point(30.4259967, 30.7862083);
        Point point2 = new Point(32.2359967, 32.5661083);
        Rectangle rectangle = new Rectangle(point1.clone(), point2.clone());

        Point point3 = new Point(31.0, 31.0);
        Assert.assertTrue(rectangle.isIntersected(point1));
        Assert.assertTrue(rectangle.isIntersected(point2));
        Assert.assertTrue(rectangle.isIntersected(point3));

        Point point4 = new Point(33.0, 33.0);
        Point point5 = new Point(30.0, 30.0);

        Assert.assertFalse(rectangle.isIntersected(point4));
        Assert.assertFalse(rectangle.isIntersected(point5));
    }

    @Test
    public void testContains2() throws OperationNotSupportedException {
        Rectangle rect = new Rectangle(2.0, 2.0, 4.0, 4.0);

        Assert.assertFalse(rect.contains(new Point(1, 2)));
        Assert.assertFalse(rect.contains(new Point(1, 3)));
        Assert.assertFalse(rect.contains(new Point(1, 4)));

        // to the bottom of minPoint
        Assert.assertFalse(rect.contains(new Point(2, 1)));
        Assert.assertFalse(rect.contains(new Point(1, 4)));
        Assert.assertFalse(rect.contains(new Point(5, 1)));

        // to the right of maxPoint
        Assert.assertFalse(rect.contains(new Point(5, 4)));
        Assert.assertFalse(rect.contains(new Point(5, 3)));
        Assert.assertFalse(rect.contains(new Point(5, 2)));

        // above of maxPoint
        Assert.assertFalse(rect.contains(new Point(2, 5)));
        Assert.assertFalse(rect.contains(new Point(3, 6)));
        Assert.assertFalse(rect.contains(new Point(3.2, 4.1)));

        // above maxPoint
        Assert.assertFalse(rect.contains(new Point(4, 5)));
        Assert.assertFalse(rect.contains(new Point(3, 5)));
        Assert.assertFalse(rect.contains(new Point(6, 5)));

        // On bottom border
        Assert.assertTrue(rect.contains(new Point(3, 2)));
        Assert.assertTrue(rect.contains(new Point(4, 2)));

        // On left border
        Assert.assertTrue(rect.contains(new Point(2, 3)));
        Assert.assertTrue(rect.contains(new Point(2, 4)));

        // On upper border
        Assert.assertTrue(rect.contains(new Point(3, 4)));

        // On right border
        Assert.assertTrue(rect.contains(new Point(4, 3)));

        // same minPoint
        Assert.assertTrue(rect.contains(new Point(2, 2)));

        // same maxPoint
        Assert.assertTrue(rect.contains(new Point(4, 4)));

        // inside rectangle
        Assert.assertTrue(rect.contains(new Point(3, 3)));
        Assert.assertTrue(rect.contains(new Point(2.5, 2.4)));
        Assert.assertTrue(rect.contains(new Point(3.1, 2.1)));
        Assert.assertTrue(rect.contains(new Point(3.9, 3.9)));
        Assert.assertTrue(rect.contains(new Point(2.001, 2.0001)));
    }

    @Test
    public void testIntersection2() throws OperationNotSupportedException {
        Rectangle rect = new Rectangle(2.0, 2.0, 4.0, 4.0);

        Assert.assertFalse(rect.isIntersected(new Point(1, 2)));
        Assert.assertFalse(rect.isIntersected(new Point(1, 3)));
        Assert.assertFalse(rect.isIntersected(new Point(1, 4)));

        // to the bottom of minPoint
        Assert.assertFalse(rect.isIntersected(new Point(2, 1)));
        Assert.assertFalse(rect.isIntersected(new Point(1, 4)));
        Assert.assertFalse(rect.isIntersected(new Point(5, 1)));

        // to the right of maxPoint
        Assert.assertFalse(rect.isIntersected(new Point(5, 4)));
        Assert.assertFalse(rect.isIntersected(new Point(5, 3)));
        Assert.assertFalse(rect.isIntersected(new Point(5, 2)));

        // above of maxPoint
        Assert.assertFalse(rect.contains(new Point(2, 5)));
        Assert.assertFalse(rect.contains(new Point(3, 6)));
        Assert.assertFalse(rect.contains(new Point(3.2, 4.1)));

        // above maxPoint
        Assert.assertFalse(rect.isIntersected(new Point(4, 5)));
        Assert.assertFalse(rect.isIntersected(new Point(3, 5)));
        Assert.assertFalse(rect.isIntersected(new Point(6, 5)));

        // On bottom border
        Assert.assertTrue(rect.isIntersected(new Point(3, 2)));
        Assert.assertTrue(rect.isIntersected(new Point(4, 2)));

        // On left border
        Assert.assertTrue(rect.isIntersected(new Point(2, 3)));
        Assert.assertTrue(rect.isIntersected(new Point(2, 4)));

        // On upper border
        Assert.assertTrue(rect.isIntersected(new Point(3, 4)));

        // On right border
        Assert.assertTrue(rect.isIntersected(new Point(4, 3)));

        // same minPoint
        Assert.assertTrue(rect.isIntersected(new Point(2, 2)));

        // same maxPoint
        Assert.assertTrue(rect.isIntersected(new Point(4, 4)));

        // inside rectangle
        Assert.assertTrue(rect.isIntersected(new Point(3, 3)));
        Assert.assertTrue(rect.isIntersected(new Point(2.5, 2.4)));
        Assert.assertTrue(rect.isIntersected(new Point(3.1, 2.1)));
        Assert.assertTrue(rect.isIntersected(new Point(3.9, 3.9)));
        Assert.assertTrue(rect.isIntersected(new Point(2.001, 2.0001)));
    }


    @Test
    public void testEdgeintersectionTest() throws OperationNotSupportedException {
        Rectangle range = new Rectangle(3.0, 2.0, 6.0, 4.0);
        Rectangle rect = new Rectangle(2.0, 4.0, 6.0, 6.0);

        Assert.assertTrue(range.isIntersected(rect));
        Assert.assertFalse(range.contains(rect));
        Assert.assertTrue(range.isEdgeIntersection(rect));

        Assert.assertTrue(rect.isIntersected(range));
        Assert.assertFalse(rect.contains(range));
        Assert.assertTrue(rect.isEdgeIntersection(range));

        Point point = new Point(6.0, 4.0);
        Assert.assertTrue(range.isIntersected(point));
        Assert.assertTrue(rect.isIntersected(point));

        Rectangle rect3 = new Rectangle(2, 4, 5, 4);
        Assert.assertTrue(range.isIntersected(rect3));
        Assert.assertFalse(range.contains(rect3));
        Assert.assertTrue(range.isEdgeIntersection(rect3));

        Assert.assertTrue(rect3.isIntersected(range));
        Assert.assertFalse(rect3.contains(range));
        Assert.assertTrue(rect3.isEdgeIntersection(range));

    }

    @Test
    public void testUpperOpenBoundedContains() {
        Rectangle rect = new Rectangle(2.0, 2.0, 4.0, 4.0);

        // to the left of minPoint
        Assert.assertFalse(rect.upperOpenBoundedContains(new Point(1, 2)));
        Assert.assertFalse(rect.upperOpenBoundedContains(new Point(1, 3)));
        Assert.assertFalse(rect.upperOpenBoundedContains(new Point(1, 4)));

        // to the bottom of minPoint
        Assert.assertFalse(rect.upperOpenBoundedContains(new Point(2, 1)));
        Assert.assertFalse(rect.upperOpenBoundedContains(new Point(1, 4)));
        Assert.assertFalse(rect.upperOpenBoundedContains(new Point(5, 1)));

        // to the right of maxPoint
        Assert.assertFalse(rect.upperOpenBoundedContains(new Point(5, 4)));
        Assert.assertFalse(rect.upperOpenBoundedContains(new Point(5, 3)));
        Assert.assertFalse(rect.upperOpenBoundedContains(new Point(5, 2)));

        // above of maxPoint
        Assert.assertFalse(rect.upperOpenBoundedContains(new Point(2, 5)));
        Assert.assertFalse(rect.upperOpenBoundedContains(new Point(3, 6)));
        Assert.assertFalse(rect.upperOpenBoundedContains(new Point(3.2, 4.1)));

        // above maxPoint
        Assert.assertFalse(rect.upperOpenBoundedContains(new Point(4, 5)));
        Assert.assertFalse(rect.upperOpenBoundedContains(new Point(3, 5)));
        Assert.assertFalse(rect.upperOpenBoundedContains(new Point(6, 5)));

        // On bottom border
        Assert.assertTrue(rect.upperOpenBoundedContains(new Point(3, 2)));
        Assert.assertFalse(rect.upperOpenBoundedContains(new Point(4, 2)));

        // On left border
        Assert.assertTrue(rect.upperOpenBoundedContains(new Point(2, 3)));
        Assert.assertFalse(rect.upperOpenBoundedContains(new Point(2, 4)));

        // On upper border
        Assert.assertFalse(rect.upperOpenBoundedContains(new Point(3, 4)));

        // On right border
        Assert.assertFalse(rect.upperOpenBoundedContains(new Point(4, 3)));

        // same minPoint
        Assert.assertTrue(rect.upperOpenBoundedContains(new Point(2, 2)));

        // same maxPoint
        Assert.assertFalse(rect.upperOpenBoundedContains(new Point(4, 4)));

        // inside rectangle
        Assert.assertTrue(rect.upperOpenBoundedContains(new Point(3, 3)));
        Assert.assertTrue(rect.upperOpenBoundedContains(new Point(2.5, 2.4)));
        Assert.assertTrue(rect.upperOpenBoundedContains(new Point(3.1, 2.1)));
        Assert.assertTrue(rect.upperOpenBoundedContains(new Point(3.9, 3.9)));
        Assert.assertTrue(rect.upperOpenBoundedContains(new Point(2.001, 2.0001)));
    }

    @Test
    public void testNoIntersectionWithRectangle() throws OperationNotSupportedException {
        Rectangle rect1 = new Rectangle(3, 2, 6, 4);
        Rectangle rect2 = new Rectangle(2, 5, 6, 6);

        Assert.assertFalse(rect1.isIntersected(rect2));
        Assert.assertFalse(rect1.contains(rect2));
        Assert.assertFalse(rect1.isEdgeIntersection(rect2));

        Assert.assertFalse(rect2.isIntersected(rect1));
        Assert.assertFalse(rect2.contains(rect1));
        Assert.assertFalse(rect2.isEdgeIntersection(rect1));
    }

    @Test
    public void testFullIntersectionWithRectangle() throws OperationNotSupportedException {
        Rectangle rect1 = new Rectangle(3, 2, 6, 4);
        Rectangle rect2 = new Rectangle(3, 2, 6, 4);

        Assert.assertTrue(rect1.isIntersected(rect2));
        Assert.assertTrue(rect1.contains(rect2));
        Assert.assertTrue(rect1.isEdgeIntersection(rect2));

        Assert.assertTrue(rect2.isIntersected(rect1));
        Assert.assertTrue(rect2.contains(rect1));
        Assert.assertTrue(rect2.isEdgeIntersection(rect1));
    }

    @Test
    public void testVertixIntersection() throws OperationNotSupportedException {
        Rectangle rect1 = new Rectangle(3, 2, 6, 4);
        Rectangle rect2 = new Rectangle(6, 4, 8, 10);

        Assert.assertTrue(rect1.isIntersected(rect2));
        Assert.assertFalse(rect1.contains(rect2));
        Assert.assertTrue(rect1.isEdgeIntersection(rect2));

        Assert.assertTrue(rect2.isIntersected(rect1));
        Assert.assertFalse(rect2.contains(rect1));
        Assert.assertTrue(rect2.isEdgeIntersection(rect1));
    }

    /**
     * Line intersect the left side of rectangle
     */
    @Test
    public void testLineIntersectionLeftSide() throws OperationNotSupportedException {
        Rectangle rect = new Rectangle(3, 2, 6, 4);

        /* horizontal line (m=0) */
        Line line1 = new Line(new Point(2, 3), new Point(4, 3));

        /*  right-inclined Line (m >1) */
        Line line2 = new Line(new Point(2, 2), new Point(4, 3));

        /* left-inclined Line (m <1) */
        Line line3 = new Line(new Point(4, 3), new Point(2, 5));


        Assert.assertTrue(rect.isIntersected(line1));
        Assert.assertTrue(rect.isIntersected(line2));
        Assert.assertTrue(rect.isIntersected(line3));
    }

    /**
     * Line intersect the right side of rectangle
     */
    @Test
    public void testLineIntersectionRightSide() throws OperationNotSupportedException {
        Rectangle rect = new Rectangle(3, 2, 6, 4);

        /* horizontal line (m=0) */
        Line line1 = new Line(new Point(7, 3), new Point(4, 3));
        /*  right-inclined Line (m >1) */
        Line line2 = new Line(new Point(4, 3), new Point(7, 4));
        /* left-inclined Line (m <1) */
        Line line3 = new Line(new Point(7, 1), new Point(3, 3));

        Assert.assertTrue(rect.isIntersected(line1));
        Assert.assertTrue(rect.isIntersected(line2));
        Assert.assertTrue(rect.isIntersected(line3));
    }

    /**
     * Line intersect the upper side of rectangle
     */
    @Test
    public void testLineIntersectionUpperSide() throws OperationNotSupportedException {
        Rectangle rect = new Rectangle(3, 2, 6, 4);

        /* vertical line (m not defined) */
        Line line1 = new Line(new Point(4, 3), new Point(4, 5));

        /*  right-inclined Line (m >1) */
        Line line2 = new Line(new Point(4, 3), new Point(5, 6));

        /* left-inclined Line (m <1) */
        Line line3 = new Line(new Point(5, 3), new Point(4, 6));

        Assert.assertTrue(rect.isIntersected(line1));
        Assert.assertTrue(rect.isIntersected(line2));
        Assert.assertTrue(rect.isIntersected(line3));
    }


    /**
     * Line intersect the bottom side of rectangle
     */
    @Test
    public void testLineIntersectionBottomSide() throws OperationNotSupportedException {
        Rectangle rect = new Rectangle(3, 2, 6, 4);

        /* vertical line (m not defined) */
        Line line1 = new Line(new Point(4, 1), new Point(4, 3));

        /*  right-inclined Line (m >1) */
        Line line2 = new Line(new Point(2, 2), new Point(4, 3));

        /* left-inclined Line (m <1) */
        Line line3 = new Line(new Point(5, 1), new Point(4, 3));

        Assert.assertTrue(rect.isIntersected(line1));
        Assert.assertTrue(rect.isIntersected(line2));
        Assert.assertTrue(rect.isIntersected(line3));
    }

}
